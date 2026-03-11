package com.hms.appointmentservice.service;

import com.hms.appointmentservice.dto.Slot;
import com.hms.appointmentservice.events.AppointmentBookedEvent;
import com.hms.appointmentservice.grpc.DoctorServiceGrpcClient;
import com.hms.appointmentservice.kafka.AppointmentEventProducer;
import com.hms.appointmentservice.model.Appointment;
import com.hms.appointmentservice.redis.RedisService;
import com.hms.appointmentservice.repository.AppointmentRepository;
import doctor.DoctorScheduleResponse;
import doctor.DoctorServiceGrpc;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Service
public class AppointmentService {
    private final AppointmentRepository appointmentRepo;
    private final DoctorServiceGrpcClient doctorServiceGrpcClient;
    private final RedisService redisService;
    private final AppointmentEventProducer eventProducer;

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final Logger log = LoggerFactory.getLogger(AppointmentService.class);

    public AppointmentService(AppointmentRepository appointmentRepo, DoctorServiceGrpcClient doctorServiceGrpcClient, RedisService redisService, AppointmentEventProducer eventProducer) {
        this.appointmentRepo = appointmentRepo;
        this.doctorServiceGrpcClient = doctorServiceGrpcClient;
        this.redisService = redisService;
        this.eventProducer = eventProducer;
    }

    public List<Slot> getAvailableSlotsForDoctorSpecialityAndDate(String speciality, String dateStr){
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate date = LocalDate.parse(dateStr, dateFormatter);

        // List of all slots for given speciality and date
        List<Slot> allSlots = fetchAllSlots(speciality, dateStr);
        log.info("allSlots: {}", allSlots);

        // List of all doctor ids
        Set<UUID> doctorIds = allSlots.stream().map(Slot::getDoctorId).collect(Collectors.toSet());
        log.info("Doctor IDs: {}", doctorIds);

        // List of booked appointments
        List<Slot> bookedSlots = getBookedSlots(doctorIds, date);
        log.info("bookedSlots : {}", bookedSlots);

        //List of reservations
        List<Slot> reservedSlots = new ArrayList<>( redisService.getReservedSlots(new ArrayList<>(doctorIds), date));
        log.info("reservedSlots : {}", reservedSlots);

        return filterAvailableSlots(allSlots, bookedSlots, reservedSlots);
    }

    /*
     * Fetch available slots for a single doctor
     */
    public List<Slot> getAvailableSlotsForDoctor(UUID doctorId, LocalDate date) {
        // List of all slots for given doctor and date
        List<Slot> doctorSlots = fetchDoctorSlots(doctorId, date);

        List<Slot> bookedSlots = getBookedSlots(Set.of(doctorId), date);

        List<Slot> reservedSlots = new ArrayList<>( redisService.getReservedSlots(List.of(doctorId), date));

        return filterAvailableSlots(doctorSlots, bookedSlots, reservedSlots);
    }

    // get free available slots given doctor speciality and date
    public List<Slot> getAvailableSlots(String speciality, String dateStr){
        return getAvailableSlotsForDoctorSpecialityAndDate(speciality, dateStr);
    }

    public boolean reserveSlot(UUID doctorId, LocalDate date, LocalTime startTime, LocalTime endTime, UUID patientId) {

        List<Slot> availableSlots = getAvailableSlotsForDoctor(doctorId, date);

        Slot requestedSlot = new Slot(doctorId, startTime, endTime);
        if (!availableSlots.contains(requestedSlot)) {
            return false;
        }

        return redisService.reserveSlot(doctorId, date, startTime, endTime, patientId);
    }

    @Transactional
    public boolean bookSlot(UUID doctorId, LocalDate date, LocalTime startTime, LocalTime endTime, UUID patientId) {

        String rKey = "reserve=" + doctorId + "=" + date + "=" + startTime + "=" + endTime;
        log.info("rKey : "+rKey);

        // 1. Check reservation belongs to user
        UUID reservedBy = redisService.getReservedBy(rKey);
        if (reservedBy == null || !reservedBy.equals(patientId)) return false;

        log.info("reservedBy : "+reservedBy);

        // 2. Lock existing bookings
        List<Appointment> bookings = appointmentRepo.findAndLockByDoctorIdAndVisitDate(doctorId, date);

        // 3. Check conflict
        boolean conflict = bookings.stream().anyMatch(v ->
                v.getStartTime().isBefore(endTime) &&
                        v.getEndTime().isAfter(startTime)
        );

        if (conflict) return false;

        // 4. Save booking
        Appointment appt = new Appointment(doctorId, patientId, date, startTime, endTime);
        appointmentRepo.save(appt);

        // 5. Delete reservation key
        redisService.removeReservation(rKey);

        // 🚀 PRODUCE EVENT
        AppointmentBookedEvent event =
                new AppointmentBookedEvent(
                        appt.getAppointmentId(),
                        doctorId,
                        patientId,
                        date,
                        startTime,
                        endTime
                );

        eventProducer.publishAppointmentBooked(event);

        return true;
    }

    /*
     * ------------------ Helper Methods ------------------
     */

    private List<Slot> fetchAllSlots(String speciality, String dateStr) {

        DoctorScheduleResponse response = doctorServiceGrpcClient.getAllSlots(speciality, dateStr);

        return response.getSlotsList()
                .stream()
                .map(slot -> new Slot(
                        UUID.fromString(slot.getDoctorId()),
                        LocalTime.parse(slot.getStartTime(), TIME_FORMATTER),
                        LocalTime.parse(slot.getEndTime(), TIME_FORMATTER)
                ))
                .toList();
    }

    private List<Slot> fetchDoctorSlots(UUID doctorId, LocalDate date) {
        DoctorScheduleResponse response = doctorServiceGrpcClient.getDoctorSlots(doctorId, date);

        return response.getSlotsList()
                .stream()
                .map(slot -> new Slot(
                        UUID.fromString(slot.getDoctorId()),
                        LocalTime.parse(slot.getStartTime(), TIME_FORMATTER),
                        LocalTime.parse(slot.getEndTime(), TIME_FORMATTER)
                ))
                .toList();
    }

    private List<Slot> getBookedSlots(Set<UUID> doctorIds, LocalDate date) {

        List<Appointment> booked = appointmentRepo.findByDoctorIdsAndDate(doctorIds, date);

        return booked.stream()
                .map(a -> new Slot(
                        a.getDoctorId(),
                        a.getStartTime(),
                        a.getEndTime()
                )).toList();
    }

    private List<Slot> filterAvailableSlots(List<Slot> allSlots,
                                            List<Slot> bookedSlots,
                                            List<Slot> reservedSlots) {

        return allSlots.stream()
                .filter(slot -> !bookedSlots.contains(slot))
                .filter(slot -> !reservedSlots.contains(slot))
                .toList();
    }

}