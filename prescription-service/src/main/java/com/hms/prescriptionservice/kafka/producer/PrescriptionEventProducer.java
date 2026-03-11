package com.hms.prescriptionservice.kafka.producer;

import com.hms.prescriptionservice.kafka.event.PrescriptionCreatedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class PrescriptionEventProducer {

    private final KafkaTemplate<String, PrescriptionCreatedEvent> kafkaTemplate;

    private static final String TOPIC = "PRESCRIPTION_CREATED";

    public PrescriptionEventProducer(KafkaTemplate<String, PrescriptionCreatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishPrescriptionCreated(PrescriptionCreatedEvent event) {
        kafkaTemplate.send(
                TOPIC,
                event.getPrescriptionId().toString(), // key
                event
        );

    }
}