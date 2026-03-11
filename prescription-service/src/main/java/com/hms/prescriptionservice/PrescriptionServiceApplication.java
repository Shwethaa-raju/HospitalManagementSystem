package com.hms.prescriptionservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PrescriptionServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PrescriptionServiceApplication.class, args);
        System.out.println(System.getenv("SPRING_DATA_MONGODB_URI"));
        System.out.println(System.getenv("SPRING_DATA_MONGODB_HOST"));
    }

}
