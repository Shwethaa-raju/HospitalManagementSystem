package com.hms.pdfservice.generator;

public interface PdfGenerator <T>{
    byte[] generate(T data);
}
