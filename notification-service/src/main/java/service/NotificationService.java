package service;

import client.MailClient;
import client.PatientClient;
import client.WhatsAppClient;
import dto.BillPdfReadyRequest;
import dto.PatientDTO;
import dto.PrescriptionPdfReadyRequest;
import model.Notification;
import org.springframework.stereotype.Service;
import repository.NotificationRepository;

import java.util.UUID;

@Service
public class NotificationService {

    private final PatientClient patientClient;
    private final MailClient mailClient;
    private final WhatsAppClient whatsAppClient;
    private final NotificationRepository repository;
    private final MessageBuilder messageBuilder;

    public NotificationService(
            PatientClient patientClient,
            MailClient mailClient,
            WhatsAppClient whatsAppClient,
            NotificationRepository repository,
            MessageBuilder messageBuilder) {

        this.patientClient = patientClient;
        this.mailClient = mailClient;
        this.whatsAppClient = whatsAppClient;
        this.repository = repository;
        this.messageBuilder = messageBuilder;
    }

    public void notifyPatient(BillPdfReadyRequest request) {

        PatientDTO patient = patientClient.getPatient(request.getPatientId());
        String message = messageBuilder.buildBillMessage(
                patient.getName(),
                request.getPdfUrl()
        );

        sendEmail(patient, request, message);
        sendWhatsApp(patient, request, message);
    }

    public void notifyPatient(PrescriptionPdfReadyRequest request) {

        PatientDTO patient = patientClient.getPatient(request.getPatientId());
        String message = messageBuilder.buildBillMessage(
                patient.getName(),
                request.getPdfUrl()
        );

        sendEmail(patient, request, message);
        sendWhatsApp(patient, request, message);
    }

    private void sendEmail(PatientDTO patient,
                           BillPdfReadyRequest req,
                           String message) {

        Notification n = new Notification();
        n.setId(UUID.randomUUID());
        n.setReferenceId(req.getBillId());
        n.setPatientId(req.getPatientId());
        n.setChannel("EMAIL");

        try {
            mailClient.send(
                    patient.getEmail(),
                    "Your Hospital Bill",
                    message
            );
            n.setStatus("SENT");
        } catch (Exception e) {
            n.setStatus("FAILED");
            n.setErrorMessage(e.getMessage());
        }

        repository.save(n);
    }

    private void sendWhatsApp(PatientDTO patient,
                              BillPdfReadyRequest req,
                              String message) {

        Notification n = new Notification();
        n.setId(UUID.randomUUID());
        n.setReferenceId(req.getBillId());
        n.setPatientId(req.getPatientId());
        n.setChannel("WHATSAPP");

        try {
            whatsAppClient.send(patient.getPhoneNo(), message);
            n.setStatus("SENT");
        } catch (Exception e) {
            n.setStatus("FAILED");
            n.setErrorMessage(e.getMessage());
        }

        repository.save(n);
    }
}
