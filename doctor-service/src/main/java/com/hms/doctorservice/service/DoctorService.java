package com.hms.doctorservice.service;
import com.hms.doctorservice.dto.Slot;
import com.hms.doctorservice.model.Doctor;
import com.hms.doctorservice.model.DoctorSchedule;
import com.hms.doctorservice.repository.DoctorRepository;
import com.hms.doctorservice.repository.DoctorScheduleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class DoctorService {
    private static final Logger log = LoggerFactory.getLogger(DoctorService.class);
    private final DoctorScheduleRepository scheduleRepo;
    private final DoctorRepository doctorRepo;
    public DoctorService(DoctorScheduleRepository scheduleRepo, DoctorRepository doctorRepo) {
        this.scheduleRepo = scheduleRepo;
        this.doctorRepo = doctorRepo;
    }

    // Given date and doctor speciality, return total slots of all doctors.
    public List<Slot> getAllSlots(String speciality, LocalDate date){
        Integer dayOfWeek = date.getDayOfWeek().getValue()-1; // 1–7
        List<DoctorSchedule> schedules = scheduleRepo.findBySpecialityAndDay(speciality, dayOfWeek);

        List<Slot> slots = new ArrayList<>();

        for (DoctorSchedule ds : schedules) {
            LocalTime start = ds.getStartTime();
            LocalTime end = ds.getEndTime();
            int duration = ds.getSlotDuration();

            for (LocalTime t = start;
                 !t.plusMinutes(duration).isAfter(end);
                 t = t.plusMinutes(duration)) {

                slots.add(new Slot( ds.getDoctor().getDoctorId(), t, t.plusMinutes(duration) ));
            }
        }
        return slots;
    }

    // get all doctor names
    public List<String> getDoctorNames(){
        List<Doctor> doctors = doctorRepo.findAll();
        return doctors.stream().map(Doctor::getName).toList();
    }

    public List<Slot> getSlotsForDoctor(UUID doctorId, LocalDate date) {

        Integer dayOfWeek = date.getDayOfWeek().getValue() - 1;

        List<DoctorSchedule> schedules = scheduleRepo.findByDoctor_DoctorIdAndDayOfWeek(doctorId, dayOfWeek);

        log.info("getSlotsForDoctor : {0}", schedules);
        List<Slot> slots = new ArrayList<>();

        for (DoctorSchedule ds : schedules) {

            LocalTime start = ds.getStartTime();
            LocalTime end = ds.getEndTime();
            int duration = ds.getSlotDuration();

            for (LocalTime t = start; !t.plusMinutes(duration).isAfter(end); t = t.plusMinutes(duration)) {
                slots.add(new Slot( doctorId,  t, t.plusMinutes(duration)));
            }
        }

        return slots;
    }
}
