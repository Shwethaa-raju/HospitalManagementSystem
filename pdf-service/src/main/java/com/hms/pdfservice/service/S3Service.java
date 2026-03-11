package com.hms.pdfservice.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;

@Service
public class S3Service {

    private final S3Client s3Client;

    @Value("${s3.bucket}")
    private String bucketName;
    @Value("${aws.region}")
    private String region;
    public S3Service(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public String uploadFile(String key, File file) {

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType("application/pdf")
                .build();

        s3Client.putObject(
                putObjectRequest,
                RequestBody.fromFile(file)
        );

        return getFileUrl(key);
    }

    private String getFileUrl(String key) {
        return String.format(
                "https://%s.s3.%s.amazonaws.com/%s",
                bucketName,
                region,
                key
        );
    }
}