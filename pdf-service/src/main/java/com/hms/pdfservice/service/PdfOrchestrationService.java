package com.hms.pdfservice.service;

import com.hms.pdfservice.client.BillingClient;
import com.hms.pdfservice.dto.BillDTO;
import com.hms.pdfservice.generator.BillPdfGenerator;
import com.hms.pdfservice.storage.FileStorage;

import java.util.UUID;

public class PdfOrchestrationService {
    private final BillingClient billingClient;
    private final BillPdfGenerator billPdfGenerator;
    private final FileStorage fileStorage;

    public PdfOrchestrationService(
            BillingClient billingClient,
            BillPdfGenerator billPdfGenerator,
            FileStorage fileStorage) {

        this.billingClient = billingClient;
        this.billPdfGenerator = billPdfGenerator;
        this.fileStorage = fileStorage;
    }

    public String generateBillPdf(UUID billId) {

        // 1. Fetch bill data
        BillDTO bill = billingClient.getBill(billId);

        // 2. Generate PDF bytes
        byte[] pdfBytes = billPdfGenerator.generate(bill);

        // 3. Store PDF
        String fileName = "bills/" + billId + ".pdf";
        String pdfUrl = fileStorage.upload(
                pdfBytes,
                fileName,
                "application/pdf"
        );

        // 4. Notify Billing Service
        billingClient.updatePdfUrl(billId, pdfUrl);

        return pdfUrl;
    }
}
