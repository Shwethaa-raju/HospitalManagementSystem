package com.hms.doctorservice.controller;

import com.hms.doctorservice.service.DoctorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/doctors")

public class DoctorController {

    private final DoctorService doctorService;
    DoctorController(DoctorService doctorService){
        this.doctorService = doctorService;
    }

    @GetMapping
    public ResponseEntity<List<String>> getPatients() {
        List<String> doctorsNames = doctorService.getDoctorNames();
        return ResponseEntity.ok().body(doctorsNames);
    }

}
