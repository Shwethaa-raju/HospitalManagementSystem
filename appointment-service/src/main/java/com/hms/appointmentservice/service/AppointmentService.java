package com.hms.appointmentservice.service;

import com.hms.appointmentservice.dto.Slot;
import com.hms.appointmentservice.grpc.DoctorServiceGrpcClient;
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
    private static final Logger log = LoggerFactory.getLogger(AppointmentService.class);
    private final AppointmentRepository appointmentRepo;
    private final DoctorServiceGrpcClient doctorServiceGrpcClient;
    private final RedisService redisService;

    AppointmentService(AppointmentRepository appointmentRepo, DoctorServiceGrpcClient doctorServiceGrpcClient, RedisService redisService){
        this.appointmentRepo = appointmentRepo;
        this.doctorServiceGrpcClient = doctorServiceGrpcClient;
        this.redisService = redisService;

    }

    // get free available slots given doctor speciality and date
    public List<Slot> getAvailableSlots(String speciality, String dateStr){

        // send request to doctor service and get all slots
        DoctorScheduleResponse response = doctorServiceGrpcClient.getAllSlots(speciality, dateStr);
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        List<Slot> allSlots = response.getSlotsList()
                .stream()
                .map(slot -> new Slot(
                        UUID.fromString(slot.getDoctorId()),
                        LocalTime.parse(slot.getStartTime(), timeFormatter),
                        LocalTime.parse(slot.getEndTime(), timeFormatter)
                ))
                .toList();


        // getting already booked slots from appointment db.
        Set<UUID> doctorIds = allSlots.stream()
                .map(Slot::getDoctorId)
                .collect(Collectors.toSet());
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate date = LocalDate.parse(dateStr, dateFormatter);
        List<Appointment> booked = appointmentRepo.findByDoctorIdsAndDate(doctorIds, date);

        List<Slot> bookedSlots = new ArrayList<>();;
        for (Appointment apt : booked) {
            LocalTime start = apt.getStartTime();
            LocalTime end = apt.getEndTime();
            UUID doctorId = apt.getDoctorId();
            bookedSlots.add(new Slot( doctorId, start, end ));
        }

        // Remove Redis-reserved slots (held by someone else)
        List<UUID> doctorUuidList = new ArrayList<>(doctorIds);
        List<Slot> reservedSlots = redisService.getReservedSlots(doctorUuidList, date);

        return allSlots.stream()
                .filter(s -> !bookedSlots.contains(s))
                .filter(s -> !reservedSlots.contains(s))
                .toList();
    }


    public boolean reserveSlot(UUID doctorId, LocalDate date, LocalTime startTime, LocalTime endTime,  UUID patientId) {
        return redisService.reserveSlot(doctorId, date, startTime, endTime, patientId);
    }

    @Transactional
    public boolean bookSlot(UUID doctorId, LocalDate date, LocalTime startTime, LocalTime endTime, UUID patientId) {

        String rKey = "reserve:" + doctorId + ":" + date + ":" + startTime + ":" + endTime;

        // 1. Check reservation belongs to user
        UUID reservedBy = redisService.getReservedBy(rKey);
        if (reservedBy == null || !reservedBy.equals(patientId)) return false;

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

        return true;
    }
}