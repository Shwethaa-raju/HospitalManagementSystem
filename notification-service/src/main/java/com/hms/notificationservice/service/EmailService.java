package com.hms.notificationservice.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(String to, String documentType, String pdfUrl) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(to);
        message.setSubject("Your " + documentType + " from Hospital");

        message.setText(
                "Dear Patient,\n\n" +
                        "Your " + documentType + " has been generated.\n\n" +
                        "Download it here:\n" +
                        pdfUrl + "\n\n" +
                        "Regards,\nHospital Team"
        );

        mailSender.send(message);
    }
}