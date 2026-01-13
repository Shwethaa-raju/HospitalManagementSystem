package com.hms.pdfservice.generator;

import com.hms.pdfservice.dto.PrescriptionDTO;

import java.io.ByteArrayOutputStream;

public class PrescriptionPdfGenerator implements PdfGenerator<PrescriptionDTO>{
    @Override
    public byte[] generate(PrescriptionDTO prescription) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        return out.toByteArray();
    }
}
