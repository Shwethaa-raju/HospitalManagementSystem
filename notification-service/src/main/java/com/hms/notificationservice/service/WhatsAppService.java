package com.hms.notificationservice.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class WhatsAppService {

    public WhatsAppService(
            @Value("${twilio.account.sid}") String sid,
            @Value("${twilio.auth.token}") String token
    ) {
        Twilio.init(sid, token);
    }

    public void sendWhatsApp(String phone, String documentType, String pdfUrl) {

        Message.creator(
                new PhoneNumber("whatsapp:" + phone),
                new PhoneNumber("whatsapp:+14155238886"),
                "Your " + documentType + " is ready.\nDownload: " + pdfUrl
        ).create();
    }
}