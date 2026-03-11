package com.hms.prescriptionservice.service;

import com.hms.prescriptionservice.grpc.PdfServiceGrpcClient;
import com.hms.prescriptionservice.kafka.event.PrescriptionCreatedEvent;
import com.hms.prescriptionservice.kafka.producer.PrescriptionEventProducer;
import com.hms.prescriptionservice.model.Prescription;
import com.hms.prescriptionservice.repository.PrescriptionRepository;
import jakarta.transaction.Transactional;
import org.bson.types.ObjectId;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PrescriptionService {

    private final JdbcTemplate jdbcTemplate;
    private final PrescriptionRepository prescriptionRepository;
    private final PrescriptionEventProducer prescriptionEventProducer;
    private final PrescriptionHtmlBuilder prescriptionHtmlBuilder;
    private final PdfServiceGrpcClient pdfServiceGrpcClient;

    public PrescriptionService(
            JdbcTemplate jdbcTemplate,
            PrescriptionRepository prescriptionRepository,
            PrescriptionEventProducer prescriptionEventProducer,
            PrescriptionHtmlBuilder prescriptionHtmlBuilder,
            PdfServiceGrpcClient pdfServiceGrpcClient) {

        this.jdbcTemplate = jdbcTemplate;
        this.prescriptionRepository = prescriptionRepository;
        this.prescriptionEventProducer = prescriptionEventProducer;
        this.prescriptionHtmlBuilder = prescriptionHtmlBuilder;
        this.pdfServiceGrpcClient = pdfServiceGrpcClient;
    }

    public void generateRandomPrescription(UUID appointmentId, UUID doctorId, UUID patientId) {

        // Create prescription in DB
        Prescription prescription = createPrescriptionInDb(appointmentId, doctorId, patientId);

        // Publish event for billing service
        publishPrescriptionCreatedEvent(prescription, doctorId, patientId);

        // Generate HTML
        String html = prescriptionHtmlBuilder.buildHtml(prescription);

        //Call PDF service
        String pdfUrl = pdfServiceGrpcClient.generatePdf(
                prescription.getId().toString(),
                "PRESCRIPTION",
                html,
                "prescription_" + prescription.getId() + ".pdf"
        );

        // Update pdfUrl in DB
        updatePrescriptionPdfUrl(prescription.getId(), pdfUrl);
    }

    @Transactional
    public Prescription createPrescriptionInDb(UUID appointmentId, UUID doctorId, UUID patientId) {

        // Fetch random medicines
        List<UUID> medicineIds = jdbcTemplate.queryForList(
                """
                SELECT id FROM medicine
                ORDER BY random()
                LIMIT 2
                """,
                UUID.class
        );

        // Fetch random lab tests
        List<UUID> labTestIds = jdbcTemplate.queryForList(
                """
                SELECT id FROM lab_test
                ORDER BY random()
                LIMIT 1
                """,
                UUID.class
        );

        // Build medicines payload
        List<Map<String, Object>> medicines = medicineIds.stream()
                .map(id -> {
                    Map<String, Object> m = new HashMap<>();
                    m.put("medicineId", id);
                    m.put("frequency", "2 times a day");
                    m.put("days", 5);
                    return m;
                })
                .toList();

        // Build lab tests payload
        List<Map<String, Object>> labTests = labTestIds.stream()
                .map(id -> {
                    Map<String, Object> lt = new HashMap<>();
                    lt.put("labTestId", id);
                    return lt;
                })
                .toList();

        Prescription prescription = new Prescription();
        prescription.setAppointmentId(appointmentId);
        prescription.setMedicines(medicines);
        prescription.setLabTests(labTests);

        prescriptionRepository.save(prescription);

        return prescription;
    }

    @Transactional
    public void updatePrescriptionPdfUrl(ObjectId prescriptionId, String pdfUrl) {

        Prescription prescription = prescriptionRepository
                .findById(prescriptionId)
                .orElseThrow();

        prescription.setPdfUrl(pdfUrl);
    }

    private void publishPrescriptionCreatedEvent(Prescription prescription, UUID doctorId, UUID patientId) {

        PrescriptionCreatedEvent event = new PrescriptionCreatedEvent();
        event.setPrescriptionId(prescription.getId());
        event.setAppointmentId(prescription.getAppointmentId());
        event.setPatientId(patientId);
        event.setDoctorId(doctorId);
        event.setMedicines(prescription.getMedicines());
        event.setLabTests(prescription.getLabTests());
        event.setCreatedAt(prescription.getCreatedAt());

        prescriptionEventProducer.publishPrescriptionCreated(event);
    }
}