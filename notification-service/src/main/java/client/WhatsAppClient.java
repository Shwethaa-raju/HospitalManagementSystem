package client;

import org.springframework.stereotype.Component;

@Component
public class WhatsAppClient {

    public WhatsAppClient(
            @Value("${twilio.sid}") String sid,
            @Value("${twilio.token}") String token) {

        Twilio.init(sid, token);
    }

    public void send(String phone, String message) {

        Message.creator(
                new PhoneNumber("whatsapp:" + phone),
                new PhoneNumber("whatsapp:+14155238886"), // Twilio sandbox
                message
        ).create();
    }
}
