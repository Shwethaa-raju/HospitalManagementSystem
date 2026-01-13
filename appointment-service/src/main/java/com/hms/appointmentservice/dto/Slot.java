package com.hms.appointmentservice.dto;

import java.time.LocalTime;
import java.util.UUID;

public class Slot {
    private final UUID doctorId;
    private final LocalTime start;
    private final LocalTime end;

    public Slot(UUID doctorId, LocalTime start, LocalTime end) {
        if (end.isBefore(start) || end.equals(start)) {
            throw new IllegalArgumentException("Slot end time must be after start time");
        }
        this.doctorId = doctorId;
        this.start = start;
        this.end = end;
    }

    public LocalTime getStart() {
        return start;
    }

    public LocalTime getEnd() {
        return end;
    }

    public UUID getDoctorId() {
        return doctorId;
    }

    /**
     * Interval overlap check
     * Returns true if this slot overlaps with another interval
     */
    public boolean overlaps(LocalTime otherStart, LocalTime otherEnd) {
        return start.isBefore(otherEnd) && end.isAfter(otherStart);
    }

    /**
     * Overlap with another Slot
     */
    public boolean overlaps(Slot other) {
        return overlaps(other.start, other.end);
    }

    @Override
    public String toString() {
        return "Slot{" +
                "start=" + start +
                ", end=" + end +
                '}';
    }
}
