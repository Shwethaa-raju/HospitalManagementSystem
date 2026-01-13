package com.hms.prescriptionservice.kafka.consumer;

import com.hms.prescriptionservice.kafka.event.AppointmentBookedEvent;
import com.hms.prescriptionservice.service.PrescriptionService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class AppointmentBookedListener {
    private final PrescriptionService prescriptionService;

    public AppointmentBookedListener(PrescriptionService prescriptionService) {
        this.prescriptionService = prescriptionService;
    }

    @KafkaListener(
            topics = "APPOINTMENT_BOOKED",
            groupId = "prescription-service"
    )
    public void consumeAppointmentBookedTopic(AppointmentBookedEvent event) {
        prescriptionService.generateRandomPrescription(event.getAppointmentId());
    }

}
