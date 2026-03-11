package com.hms.prescriptionservice.model;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Document(collection = "prescriptions")
public class Prescription {

    @Id
    private ObjectId id;

    private UUID appointmentId;
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

    private LocalDateTime createdAt = LocalDateTime.now();

    String pdfUrl;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public UUID getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(UUID appointmentId) {
        this.appointmentId = appointmentId;
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

    public String getPdfUrl() {
        return pdfUrl;
    }

    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }
}
