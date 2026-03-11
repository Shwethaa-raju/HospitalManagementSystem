package com.hms.pdfservice.event;

public class PdfGeneratedEvent {
    String url;
    String documentId;
    String documentType;
    String fileName;

    public PdfGeneratedEvent(){}

    public PdfGeneratedEvent(
        String url,
        String documentId,
        String documentType,
        String fileName
    ){
        this.url = url;
        this.documentId = documentId;
        this.documentType = documentType;
        this.fileName = fileName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
