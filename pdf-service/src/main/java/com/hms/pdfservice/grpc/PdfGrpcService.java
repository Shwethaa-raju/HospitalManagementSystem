package com.hms.pdfservice.grpc;

import com.hms.pdfservice.service.PdfGenerationService;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import pdf.PdfRequest;
import pdf.PdfResponse;
import pdf.PdfServiceGrpc;

@GrpcService
public class PdfGrpcService extends PdfServiceGrpc.PdfServiceImplBase {

    private final PdfGenerationService pdfGenerationService;

    public PdfGrpcService(PdfGenerationService pdfGenerationService) {
        this.pdfGenerationService = pdfGenerationService;
    }

    @Override
    public void generatePdf(PdfRequest request, StreamObserver<PdfResponse> responseObserver) {

        String pdfUrl = pdfGenerationService.generatePdf(
                request.getDocumentId(),
                request.getDocumentType(),
                request.getFileName(),
                request.getHtmlContent()
        );

        PdfResponse response = PdfResponse.newBuilder()
                .setPdfUrl(pdfUrl)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}