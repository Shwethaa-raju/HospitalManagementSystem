package com.hms.notificationservice.kafka.consumer;

import com.hms.notificationservice.events.PdfGeneratedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import com.hms.notificationservice.service.NotificationService;

@Service
public class PdfGeneratedListener {

    private final NotificationService notificationService;

    public PdfGeneratedListener(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @KafkaListener(
            topics = {"BILL_PDF_GENERATED", "PRESCRIPTION_PDF_GENERATED"},
            groupId = "notification-service"
    )
    public void consume(PdfGeneratedEvent event) {
        notificationService.sendNotifications(event);
    }
}