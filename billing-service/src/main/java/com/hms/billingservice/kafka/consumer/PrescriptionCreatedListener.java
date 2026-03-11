package com.hms.billingservice.kafka.consumer;

import com.hms.billingservice.kafka.event.PrescriptionCreatedEvent;
import com.hms.billingservice.service.BillingService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class PrescriptionCreatedListener {
    private final BillingService billingService;

    PrescriptionCreatedListener(BillingService billingService){
        this.billingService = billingService;
    }

    @KafkaListener(
            topics = "PRESCRIPTION_CREATED",
            groupId = "billing-service"
    )
    public void consumePrescriptionCreatedTopic(PrescriptionCreatedEvent event) {
        System.out.println("receieved PRESCRIPTION_CREATED event" + event.getAppointmentId());
        billingService.createBill(event.getPrescriptionId(), event.getAppointmentId(), event.getMedicines(), event.getLabTests());
    }
}
