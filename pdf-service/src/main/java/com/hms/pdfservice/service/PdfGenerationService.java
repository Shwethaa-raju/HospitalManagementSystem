package com.hms.pdfservice.service;

import com.hms.pdfservice.event.PdfGeneratedEvent;
import com.hms.pdfservice.kafka.PdfGeneratedEventProducer;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;

@Service
public class PdfGenerationService {

    private final S3Service s3Service;
    private final PdfGeneratedEventProducer pdfGeneratedEventProducer;
    PdfGenerationService(S3Service s3Service, PdfGeneratedEventProducer pdfGeneratedEventProducer){
        this.s3Service = s3Service;
        this.pdfGeneratedEventProducer = pdfGeneratedEventProducer;
    }

    public String generatePdf(String documentId, String documentType, String fileName, String htmlContent) {

        try {

            File file = createPdf(htmlContent, fileName);
            System.out.println(file.getAbsolutePath());
            System.out.println(file.exists());

            // uploadToS3
            String key = documentType + "/" + documentId + "/" + fileName;
            String url = s3Service.uploadFile(key, file);
            System.out.println("S3 Url where " + documentType + " file was uploaded : " + url);

            // Publish bill / prescription pdf generated kafka event to be consumed by notification service
            PdfGeneratedEvent event = new PdfGeneratedEvent(url, documentId, documentType, fileName);
            String topic = documentType.toUpperCase() + "_PDF_GENERATED";
            pdfGeneratedEventProducer.publishPdfGenerated(event, topic);
            return url;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("PDF generation failed", e);
        }
    }

    private File createPdf(String html, String fileName) throws Exception {

        File file = new File("/tmp/" + fileName);

        try (OutputStream os = new FileOutputStream(file)) {

            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withHtmlContent(html, null);
            builder.toStream(os);
            builder.run();
        }

        return file;
    }



}