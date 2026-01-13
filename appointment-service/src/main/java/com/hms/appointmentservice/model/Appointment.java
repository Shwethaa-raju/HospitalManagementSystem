package com.hms.appointmentservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Entity
public class Appointment {
    @Id
    @GeneratedValue
    private UUID appointmentId;
    private UUID doctorId;
    private UUID patientId;
    private LocalDate visitDate;
    private LocalTime startTime;
    private LocalTime endTime;

    protected Appointment() {}

    public Appointment( UUID doctorId, UUID patientId, LocalDate visitDate, LocalTime startTime, LocalTime endTime) {
        if (doctorId == null || patientId == null) {
            throw new IllegalArgumentException("doctorId and patientId are required");
        }
        if (!endTime.isAfter(startTime)) {
            throw new IllegalArgumentException("endTime must be after startTime");
        }

        this.doctorId = doctorId;
        this.patientId = patientId;
        this.visitDate = visitDate;
        this.startTime = startTime;
        this.endTime = endTime;
    }


    public UUID getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(UUID doctorId) {
        this.doctorId = doctorId;
    }

    public UUID getPatientId() {
        return patientId;
    }

    public void setPatientId(UUID patientId) {
        this.patientId = patientId;
    }

    public LocalDate getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(LocalDate visitDate) {
        this.visitDate = visitDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }
}
