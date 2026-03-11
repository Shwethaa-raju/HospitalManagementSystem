package com.hms.appointmentservice.events;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;


public class AppointmentBookedEvent {

    private UUID appointmentId;
    private UUID doctorId;
    private UUID patientId;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;

    public AppointmentBookedEvent(UUID appointmentId,
                                  UUID doctorId,
                                  UUID patientId,
                                  LocalDate date,
                                  LocalTime startTime,
                                  LocalTime endTime) {

        this.appointmentId = appointmentId;
        this.doctorId = doctorId;
        this.patientId = patientId;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public UUID getAppointmentId() { return appointmentId; }
    public UUID getDoctorId() { return doctorId; }
    public UUID getPatientId() { return patientId; }
    public LocalDate getDate() { return date; }
    public LocalTime getStartTime() { return startTime; }
    public LocalTime getEndTime() { return endTime; }
}