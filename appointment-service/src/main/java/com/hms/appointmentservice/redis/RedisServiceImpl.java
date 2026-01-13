package com.hms.appointmentservice.redis;

import com.hms.appointmentservice.dto.Slot;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class RedisServiceImpl implements RedisService{
    private final StringRedisTemplate redisTemplate;
    private static final Duration RESERVATION_TTL = Duration.ofMinutes(10);

    public RedisServiceImpl(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public List<Slot> getReservedSlots(List<UUID> doctorIds, LocalDate date){
        List<Slot> allSlots = new ArrayList<>();
        for (UUID doctorId : doctorIds) {
            String pattern = buildPattern(doctorId, date);

            Set<String> keys = redisTemplate.keys(pattern);

            if (keys == null || keys.isEmpty()) {
                continue;
            }

            List<Slot> newSlots = keys.stream()
                    .map(this::extractSlotTime)
                    .toList();

            allSlots.addAll(newSlots);
        }
        return allSlots;
    }

    private String buildPattern(UUID doctorId, LocalDate date) {
        return String.format("reserve-%s-%s-*-*", doctorId, date);
    }

    private Slot extractSlotTime(String redisKey) {
        String[] parts = redisKey.split("-");

        // parts[0] = reserve
        // parts[1] = doctorId
        // parts[2] = date
        // parts[3] = startTime
        // parts[4] = endTime

        UUID doctorId = UUID.fromString(parts[1]);
        LocalTime startTime = LocalTime.parse(parts[3]);
        LocalTime endTime = LocalTime.parse(parts[4]);

        return new Slot(doctorId, startTime, endTime);
    }

    @Override
    public boolean reserveSlot(
            UUID doctorId,
            LocalDate date,
            LocalTime startTime,
            LocalTime endTime,
            UUID patientId
    ){
        String key = String.format(
                "reserve-%s-%s-%s-%s",
                doctorId,
                date,
                startTime,
                endTime
        );

        return Boolean.TRUE.equals(
                redisTemplate.opsForValue()
                        .setIfAbsent(key, patientId.toString(), RESERVATION_TTL)
        );
    }

    @Override
    public UUID getReservedBy(String reservationKey) {

        String value = redisTemplate.opsForValue().get(reservationKey);

        if (value == null) {
            return null;
        }

        return UUID.fromString(value);
    }

    @Override
    public void removeReservation(String reservationKey) {
        redisTemplate.delete(reservationKey);
    }
}
