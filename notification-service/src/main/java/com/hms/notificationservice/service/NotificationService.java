package com.hms.notificationservice.service;

import com.hms.notificationservice.events.PdfGeneratedEvent;
import org.springframework.stereotype.Service;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.annotation.Recover;

@Service
public class NotificationService {

    private final EmailService emailService;
    private final WhatsAppService whatsAppService;

    public NotificationService(
            EmailService emailService,
            WhatsAppService whatsAppService
    ) {
        this.emailService = emailService;
        this.whatsAppService = whatsAppService;
    }


    public void sendNotifications(PdfGeneratedEvent event) {

        sendEmailWithRetry(event);
        sendWhatsAppWithRetry(event);

    }

    @Retryable(value = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 2000))
    public void sendEmailWithRetry(PdfGeneratedEvent event) {

        emailService.sendEmail(
                event.getPatientEmail(),
                event.getDocumentType(),
                event.getPdfUrl()
        );
    }

    @Retryable(value = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 2000))
    public void sendWhatsAppWithRetry(PdfGeneratedEvent event) {

        whatsAppService.sendWhatsApp(
                event.getPatientPhone(),
                event.getDocumentType(),
                event.getPdfUrl()
        );
    }

    @Recover
    public void recover(Exception e, PdfGeneratedEvent event) {

        System.out.println(
                "Failed to send notification after retries for document: "
                        + event.getDocumentId()
        );

        // TO DO
        // push to Kafka DLQ or persist failure
    }
}