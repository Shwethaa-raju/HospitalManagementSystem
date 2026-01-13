package com.hms.appointmentservice.repository;

import com.hms.appointmentservice.model.Appointment;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {

    @Query("""
        SELECT a
        FROM Appointment a
        WHERE a.doctorId IN :doctorIds
          AND a.visitDate = :date
    """)
    List<Appointment> findByDoctorIdsAndDate(
            @Param("doctorIds") Set<UUID> doctorIds,
            @Param("date") LocalDate date
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        SELECT a
        FROM Appointment a
        WHERE a.doctorId = :doctorId
          AND a.visitDate = :date
    """)
    List<Appointment> findAndLockByDoctorIdAndVisitDate(
            @Param("doctorId") UUID doctorId,
            @Param("date") LocalDate date
    );

}
