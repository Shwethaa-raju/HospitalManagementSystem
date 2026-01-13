package com.hms.doctorservice.repository;

import com.hms.doctorservice.model.DoctorSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DoctorScheduleRepository extends JpaRepository<DoctorSchedule, UUID> {
    @Query("""
        SELECT ds
        FROM DoctorSchedule ds
        JOIN ds.doctor d
        WHERE LOWER(d.speciality) = LOWER(:speciality)
          AND ds.dayOfWeek = :dayOfWeek
    """)
    List<DoctorSchedule> findBySpecialityAndDay(
            @Param("speciality") String speciality,
            @Param("dayOfWeek") Integer dayOfWeek
    );
}
