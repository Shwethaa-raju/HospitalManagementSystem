package kafka.consumer;

import dto.BillPdfReadyRequest;
import dto.PrescriptionPdfReadyRequest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import service.NotificationService;

@Component
public class PdfGeneratedListener {
    private final NotificationService notificationService;

    PdfGeneratedListener(NotificationService notificationService){
        this.notificationService = notificationService;
    }

    @KafkaListener(
            topics = "BILL_PDF_GENERATED",
            groupId = "notification-service"
    )
    public void consumeBillPdfGenerated(BillPdfReadyRequest event) {
        notificationService.notifyPatient(event);
    }

    @KafkaListener(
            topics = "PRESCRIPTION_PDF_GENERATED",
            groupId = "notification-service"
    )
    public void consumePrescriptionPdfGenerated(PrescriptionPdfReadyRequest event) {
        notificationService.notifyPatient(event);
    }
}
