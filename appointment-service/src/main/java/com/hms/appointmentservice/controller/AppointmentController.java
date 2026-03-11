package com.hms.appointmentservice.controller;

import com.hms.appointmentservice.dto.Slot;
import com.hms.appointmentservice.service.AppointmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/appointment")
public class AppointmentController {


    private final AppointmentService appointmentService;
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping("/slots")
    public ResponseEntity<List<Slot>> getAvailableSlots(
            @RequestParam String speciality,
            @RequestParam String date
    ) {
        List<Slot> availableSlots = appointmentService.getAvailableSlots(speciality, date);
        return ResponseEntity.ok(availableSlots);
    }


    @PostMapping("/reserve")
    public ResponseEntity<String> reserveSlot(
            @RequestParam UUID doctorId,
            @RequestParam String dateStr,
            @RequestParam String startTime,
            @RequestParam String endTime,
            @RequestParam UUID patientId
    ) {

        LocalDate date = LocalDate.parse(dateStr, DATE_FORMATTER);
        LocalTime start = LocalTime.parse(startTime, TIME_FORMATTER);
        LocalTime end = LocalTime.parse(endTime, TIME_FORMATTER);
        boolean reserved = appointmentService.reserveSlot(doctorId, date,start, end, patientId );

        String requestedSlot = String.format(
                "The slot for DoctorId: %s, Date: %s, Time Slot: %s - %s is reserved successfully",
                doctorId, date, startTime, endTime
        );

        return ResponseEntity.ok(reserved? requestedSlot : "The requested slot has been reserved already or it doesn't exist");
    }

    @PostMapping("/book")
    public ResponseEntity<String> bookSlot(
            @RequestParam UUID doctorId,
            @RequestParam String dateStr,
            @RequestParam String startTime,
            @RequestParam String endTime,
            @RequestParam UUID patientId
    ) {

        LocalDate date = LocalDate.parse(dateStr, DATE_FORMATTER);
        LocalTime start = LocalTime.parse(startTime, TIME_FORMATTER);
        LocalTime end = LocalTime.parse(endTime, TIME_FORMATTER);

        boolean booked = appointmentService.bookSlot(doctorId, date,start, end, patientId);

        String requestedSlot = String.format(
                "The slot for DoctorId: %s, Date: %s, Time Slot: %s - %s is booked successfully",
                doctorId, date, startTime, endTime
        );

        return ResponseEntity.ok(booked? requestedSlot : "The requested slot could not be booked");
    }

}
