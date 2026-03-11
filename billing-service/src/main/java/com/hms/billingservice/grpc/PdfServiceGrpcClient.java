package com.hms.billingservice.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pdf.PdfRequest;
import pdf.PdfResponse;
import pdf.PdfServiceGrpc;

@Service
public class PdfServiceGrpcClient {

    private final PdfServiceGrpc.PdfServiceBlockingStub blockingStub;

    public PdfServiceGrpcClient(
            @Value("${pdf.service.address:localhost}") String serverAddress,
            @Value("${pdf.service.grpc.port:9095}") int serverPort
    ) {

        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(serverAddress, serverPort)
                .usePlaintext()
                .build();

        blockingStub = PdfServiceGrpc.newBlockingStub(channel);
    }

    public String generatePdf(String documentId, String documentType, String htmlContent, String fileName) {

        PdfRequest request = PdfRequest.newBuilder()
                .setDocumentId(documentId)
                .setDocumentType(documentType)
                .setHtmlContent(htmlContent)
                .setFileName(fileName)
                .build();

        PdfResponse response = blockingStub.generatePdf(request);

        return response.getPdfUrl();
    }
}