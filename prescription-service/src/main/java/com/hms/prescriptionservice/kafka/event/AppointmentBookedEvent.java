package com.hms.prescriptionservice.kafka.event;

import java.time.LocalDate;
import java.util.UUID;

public class AppointmentBookedEvent {
    private UUID appointmentId;

    public UUID getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(UUID appointmentId) {
        this.appointmentId = appointmentId;
    }

//    private UUID patientId;
//    private UUID doctorId;
//    private LocalDate appointmentDate;
}
