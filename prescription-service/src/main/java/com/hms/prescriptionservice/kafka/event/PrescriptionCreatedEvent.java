package com.hms.prescriptionservice.kafka.event;

import java.util.UUID;

public class PrescriptionCreatedEvent {
    private UUID prescriptionId;
    private UUID appointmentId;

    public UUID getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(UUID prescriptionId) {
        this.prescriptionId = prescriptionId;
    }

    public UUID getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(UUID appointmentId) {
        this.appointmentId = appointmentId;
    }
}
