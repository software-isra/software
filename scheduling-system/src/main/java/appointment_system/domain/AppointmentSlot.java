package appointment_system.domain;

import java.time.LocalDateTime;

/**
 * Represents a bookable appointment slot.
 *
 */
public class AppointmentSlot {

    private final LocalDateTime startTime;
    private final int durationMinutes;
    private final int maxParticipants;
    private int bookedParticipants;

    public AppointmentSlot(LocalDateTime startTime, int durationMinutes, int maxParticipants) {
        if (startTime == null) {
            throw new IllegalArgumentException("Start time must not be null");
        }
        if (durationMinutes <= 0) {
            throw new IllegalArgumentException("Duration must be greater than zero");
        }
        if (maxParticipants <= 0) {
            throw new IllegalArgumentException("Max participants must be greater than zero");
        }

        this.startTime = startTime;
        this.durationMinutes = durationMinutes;
        this.maxParticipants = maxParticipants;
        this.bookedParticipants = 0;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public int getBookedParticipants() {
        return bookedParticipants;
    }

    public int getRemainingCapacity() {
        return maxParticipants - bookedParticipants;
    }

    public boolean isFull() {
        return bookedParticipants >= maxParticipants;
    }

    public boolean isInPast() {
        return startTime.isBefore(LocalDateTime.now());
    }

    public double getBookingRatio() {
        return (double) bookedParticipants / maxParticipants;
    }

    public int getBookingPercentage() {
        return (int) Math.round(getBookingRatio() * 100);
    }

    public void addParticipant() {
        if (isFull()) {
            throw new IllegalStateException("Slot is already full");
        }
        bookedParticipants++;
    }

    public void removeParticipant() {
        if (bookedParticipants > 0) {
            bookedParticipants--;
        }
    }
}