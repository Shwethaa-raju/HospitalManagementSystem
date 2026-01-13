package com.hms.doctorservice.dto;

import java.time.LocalTime;
import java.util.UUID;

public class Slot {
    private final UUID doctorId;
    private final LocalTime startTime;
    private final LocalTime endTime;

    public Slot(UUID doctorId, LocalTime startTime, LocalTime endTime) {
        if (endTime.isBefore(startTime) || endTime.equals(startTime)) {
            throw new IllegalArgumentException("Slot endTime must be after startTime");
        }
        this.doctorId = doctorId;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public UUID getDoctorId() {
        return doctorId;
    }

    /**
     * Interval overlap check
     * Returns true if this slot overlaps with another interval
     */
    public boolean overlaps(LocalTime otherStart, LocalTime otherEnd) {
        return startTime.isBefore(otherEnd) && endTime.isAfter(otherStart);
    }

    /**
     * Overlap with another Slot
     */
    public boolean overlaps(Slot other) {
        return overlaps(other.startTime, other.endTime);
    }

    @Override
    public String toString() {
        return "Slot{" +
                "startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}
