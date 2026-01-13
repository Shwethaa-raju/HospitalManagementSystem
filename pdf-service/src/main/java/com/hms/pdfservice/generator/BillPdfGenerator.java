package com.hms.pdfservice.generator;

import com.hms.pdfservice.dto.BillDTO;

import java.io.ByteArrayOutputStream;

public class BillPdfGenerator implements PdfGenerator<BillDTO>{
    @Override
    public byte[] generate(BillDTO bill) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        return out.toByteArray();
    }
}
