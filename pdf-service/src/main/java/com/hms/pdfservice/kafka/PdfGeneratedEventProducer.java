package com.hms.pdfservice.kafka;

import com.hms.pdfservice.event.PdfGeneratedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class PdfGeneratedEventProducer {

    private final KafkaTemplate<String, PdfGeneratedEvent> kafkaTemplate;

    public PdfGeneratedEventProducer(KafkaTemplate<String, PdfGeneratedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishPdfGenerated(PdfGeneratedEvent event, String topic) {
        kafkaTemplate.send(topic, event.getDocumentId(), event);
    }
}
