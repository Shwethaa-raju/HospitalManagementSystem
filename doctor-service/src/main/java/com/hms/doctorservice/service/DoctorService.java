package com.hms.doctorservice.service;
import com.hms.doctorservice.dto.Slot;
import com.hms.doctorservice.model.Doctor;
import com.hms.doctorservice.model.DoctorSchedule;
import com.hms.doctorservice.repository.DoctorRepository;
import com.hms.doctorservice.repository.DoctorScheduleRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class DoctorService {

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
        return doctors.stream().map(Doctor::getDegrees).toList();
    }
}
