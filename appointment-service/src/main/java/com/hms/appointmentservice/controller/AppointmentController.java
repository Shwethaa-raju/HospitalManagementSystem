package com.hms.appointmentservice.controller;

import com.hms.appointmentservice.dto.Slot;
import com.hms.appointmentservice.service.AppointmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/appointment")
public class AppointmentController {


    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping
    public ResponseEntity<List<Slot>> getAvailableSlots() {
        List<Slot> availableSlots = appointmentService.getAvailableSlots("DERMATOLOGY", "22-12-2025");
        return ResponseEntity.ok().body(availableSlots);
    }


}
