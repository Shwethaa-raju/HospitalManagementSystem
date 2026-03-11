package com.hms.notificationservice.events;

public class PdfGeneratedEvent {

    private String documentId;
    private String documentType;
    private String pdfUrl;

    private String patientEmail;
    private String patientPhone;

    public String getDocumentId() { return documentId; }
    public String getDocumentType() { return documentType; }
    public String getPdfUrl() { return pdfUrl; }
    public String getPatientEmail() { return patientEmail; }
    public String getPatientPhone() { return patientPhone; }

    public void setDocumentId(String documentId) { this.documentId = documentId; }
    public void setDocumentType(String documentType) { this.documentType = documentType; }
    public void setPdfUrl(String pdfUrl) { this.pdfUrl = pdfUrl; }
    public void setPatientEmail(String patientEmail) { this.patientEmail = patientEmail; }
    public void setPatientPhone(String patientPhone) { this.patientPhone = patientPhone; }
}