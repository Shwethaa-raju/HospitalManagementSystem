package com.hms.pdfservice.storage;

public interface FileStorage {
    String upload(byte[] content, String fileName, String contentType);
}
