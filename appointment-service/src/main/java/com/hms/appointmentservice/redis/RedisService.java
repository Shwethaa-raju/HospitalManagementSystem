package com.hms.appointmentservice.redis;


import com.hms.appointmentservice.dto.Slot;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public interface RedisService {
    public List<Slot> getReservedSlots(List<UUID> doctorIds, LocalDate date );
    public boolean reserveSlot(
            UUID doctorId,
            LocalDate date,
            LocalTime startTime,
            LocalTime endTime,
            UUID patientId
    );
    public UUID getReservedBy(String reservationKey);
    public void removeReservation(String reservationKey);
}
