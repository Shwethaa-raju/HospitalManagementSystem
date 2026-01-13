package com.hms.pdfservice.storage;

public class S3FileStorage implements FileStorage{
    @Override
    public String upload(byte[] content,
                         String fileName,
                         String contentType) {

//        s3Client.putObject(
//                PutObjectRequest.builder()
//                        .bucket(bucketName)
//                        .key(fileName)
//                        .contentType(contentType)
//                        .build(),
//                RequestBody.fromBytes(content)
//        );
//
//        return "https://s3.amazonaws.com/" + bucketName + "/" + fileName;
        return "";
    }
}

