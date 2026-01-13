package client;

import org.springframework.stereotype.Component;

@Component
public class MailClient {

    private final JavaMailSender mailSender;

    public EmailClient(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void send(String to, String subject, String body) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }
}
