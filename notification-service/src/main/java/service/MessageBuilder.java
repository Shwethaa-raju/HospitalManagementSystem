package service;

import org.springframework.stereotype.Component;

@Component
public class MessageBuilder {

    public String buildBillMessage(String name, String pdfUrl) {
        return """
        Hi %s,

        Your hospital bill is ready.
        You can download it here:
        %s

        Thank you.
        """.formatted(name, pdfUrl);
    }
}
