package com.hms.pdfservice.client;

import com.hms.pdfservice.dto.BillDTO;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.UUID;

public class BillingClient {
    private final RestTemplate restTemplate;

    public BillingClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public BillDTO getBill(UUID billId) {
        return restTemplate.getForObject(
                "http://billing-service/internal/bill/" + billId,
                BillDTO.class
        );
    }

    public void updatePdfUrl(UUID billId, String pdfUrl) {
        restTemplate.patchForObject(
                "http://billing-service/internal/bill/" + billId + "/pdf",
                Map.of("pdfUrl", pdfUrl),
                Void.class
        );
    }
}
