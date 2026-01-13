package com.hms.prescriptionservice.service;

import com.hms.prescriptionservice.kafka.event.PrescriptionCreatedEvent;
import com.hms.prescriptionservice.model.Prescription;
import com.hms.prescriptionservice.repository.PrescriptionRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class PrescriptionService {
    private final JdbcTemplate jdbcTemplate;
    private final PrescriptionRepository prescriptionRepository;
    private final KafkaTemplate<String, PrescriptionCreatedEvent> kafkaTemplate;


    public PrescriptionService(JdbcTemplate jdbcTemplate, PrescriptionRepository prescriptionRepository, KafkaTemplate<String, PrescriptionCreatedEvent> kafkaTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.prescriptionRepository = prescriptionRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void generateRandomPrescription(UUID appointmentId) {

        // 1. Fetch random medicines
        List<UUID> medicineIds = jdbcTemplate.queryForList(
                """
                SELECT id FROM medicine
                ORDER BY random()
                LIMIT 2
                """,
                UUID.class
        );

        // 2. Fetch random lab tests
        List<UUID> labTestIds = jdbcTemplate.queryForList(
                """
                SELECT id FROM lab_test
                ORDER BY random()
                LIMIT 1
                """,
                UUID.class
        );

        // 3. Build medicines payload
        List<Map<String, Object>> medicines = medicineIds.stream()
                .map(id -> {
                    Map<String, Object> m = new HashMap<>();
                    m.put("medicineId", id);
                    m.put("frequency", "2 times a day");
                    m.put("days", 5);
                    return m;
                })
                .toList();

        // 4. Build lab tests payload
        List<Map<String, Object>> labTests = labTestIds.stream()
                .map(id -> {
                    Map<String, Object> lt = new HashMap<>();
                    lt.put("labTestId", id);
                    return lt;
                })
                .toList();

        // 5. Create prescription
        Prescription prescription = new Prescription();
        prescription.setAppointmentId(appointmentId);
        prescription.setMedicines(medicines);
        prescription.setLabTests(labTests);

        prescriptionRepository.save(prescription);

        publishPrescriptionCreatedEvent(prescription);

        // call grpc server in pdfService to create bill pdf
    }

    private void publishPrescriptionCreatedEvent(Prescription prescription) {

        PrescriptionCreatedEvent event = new PrescriptionCreatedEvent();
        event.setPrescriptionId(prescription.getId());
        event.setAppointmentId(prescription.getAppointmentId());

        kafkaTemplate.send(
                "PRESCRIPTION_CREATED",
                prescription.getId().toString(),
                event
        );
    }


}
