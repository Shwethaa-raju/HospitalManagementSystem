package com.hms.billingservice.kafka.consumer;

import com.hms.billingservice.service.BillingService;
import com.hms.prescriptionservice.kafka.event.PrescriptionCreatedEvent;
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
        billingService.createBill(event.getPrescriptionId());
    }
}
