package com.hms.prescriptionservice.kafka.event;

import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PrescriptionCreatedEvent {

    private ObjectId prescriptionId;
    private UUID appointmentId;

    // Useful for billing / notifications
    private UUID patientId;
    private UUID doctorId;

    /**
     * Each item:
     * {
     *   medicineId: UUID,
     *   frequency: String,
     *   days: int
     * }
     */
    private List<Map<String, Object>> medicines;

    /**
     * Each item:
     * {
     *   labTestId: UUID
     * }
     */
    private List<Map<String, Object>> labTests;

    private LocalDateTime createdAt;

    public PrescriptionCreatedEvent() {}

    public PrescriptionCreatedEvent(
            ObjectId prescriptionId,
            UUID appointmentId,
            UUID patientId,
            UUID doctorId,
            List<Map<String, Object>> medicines,
            List<Map<String, Object>> labTests,
            LocalDateTime createdAt
    ) {
        this.prescriptionId = prescriptionId;
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.medicines = medicines;
        this.labTests = labTests;
        this.createdAt = createdAt;
    }

    public ObjectId getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(ObjectId prescriptionId) {
        this.prescriptionId = prescriptionId;
    }

    public UUID getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(UUID appointmentId) {
        this.appointmentId = appointmentId;
    }

    public UUID getPatientId() {
        return patientId;
    }

    public void setPatientId(UUID patientId) {
        this.patientId = patientId;
    }

    public UUID getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(UUID doctorId) {
        this.doctorId = doctorId;
    }

    public List<Map<String, Object>> getMedicines() {
        return medicines;
    }

    public void setMedicines(List<Map<String, Object>> medicines) {
        this.medicines = medicines;
    }

    public List<Map<String, Object>> getLabTests() {
        return labTests;
    }

    public void setLabTests(List<Map<String, Object>> labTests) {
        this.labTests = labTests;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}