package appointment_system.domain;

/**
 * Represents a confirmed appointment booked by a user.
 
 */
public class Appointment {

    private AppointmentSlot slot;
    private final String username;
    private AppointmentStatus status;
    private final AppointmentType type;

    public Appointment(AppointmentSlot slot, String username) {
        this(slot, username, AppointmentType.IN_PERSON);
    }

    public Appointment(AppointmentSlot slot, String username, AppointmentType type) {
        if (slot == null) {
            throw new IllegalArgumentException("Slot must not be null");
        }
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username must not be blank");
        }
        if (type == null) {
            throw new IllegalArgumentException("Appointment type must not be null");
        }

        this.slot = slot;
        this.username = username.trim();
        this.status = AppointmentStatus.CONFIRMED;
        this.type = type;
    }

    public AppointmentSlot getSlot() {
        return slot;
    }

    public void setSlot(AppointmentSlot slot) {
        if (slot == null) {
            throw new IllegalArgumentException("Slot must not be null");
        }
        this.slot = slot;
    }

    public String getUsername() {
        return username;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public AppointmentType getType() {
        return type;
    }

    public void cancel() {
        this.status = AppointmentStatus.AVAILABLE;
    }

    public boolean isCancelled() {
        return this.status == AppointmentStatus.AVAILABLE;
    }
}