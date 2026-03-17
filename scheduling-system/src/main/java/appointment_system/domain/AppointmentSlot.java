package appointment_system.domain;
import java.time.LocalDateTime;

public class AppointmentSlot {
	private LocalDateTime startTime;
    private int durationMinutes;
    private int maxParticipants;
    private int bookedParticipants;
/**
 * 
 * @param startTime
 * @param durationMinutes
 * @param maxParticipants
 */
    public AppointmentSlot(LocalDateTime startTime, int durationMinutes, int maxParticipants) {
        this.startTime = startTime;
        this.durationMinutes = durationMinutes;
        this.maxParticipants = maxParticipants;
        this.bookedParticipants = 0;
    }
/**
 * 
 * @return
 */
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

    public boolean isFull() {
        return bookedParticipants >= maxParticipants;
    }

    public void addParticipant() {
        if (!isFull()) {
            bookedParticipants++;
        }
    }
}
