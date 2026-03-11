package com.hms.appointmentservice.kafka;

import com.hms.appointmentservice.events.AppointmentBookedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class AppointmentEventProducer {

    private static final String TOPIC = "APPOINTMENT_BOOKED";

    private final KafkaTemplate<String, AppointmentBookedEvent> kafkaTemplate;

    public AppointmentEventProducer(KafkaTemplate<String, AppointmentBookedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishAppointmentBooked(AppointmentBookedEvent event) {
        kafkaTemplate.send(TOPIC, event.getAppointmentId().toString(), event);
    }
}