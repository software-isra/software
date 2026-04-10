package appointment_system.domain;

/**
 * Represents a confirmed appointment booked by a user.

 */
public class Appointment {

    private final AppointmentSlot slot;
    private final String username;
    private final AppointmentStatus status;

    /**
     * Creates a confirmed appointment.
     *
     * @param slot the appointment slot
     * @param username username of the customer
     */
    public Appointment(AppointmentSlot slot, String username) {
        if (slot == null) {
            throw new IllegalArgumentException("Slot must not be null");
        }
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username must not be blank");
        }

        this.slot = slot;
        this.username = username.trim();
        this.status = AppointmentStatus.CONFIRMED;
    }

    public AppointmentSlot getSlot() {
        return slot;
    }

    public String getUsername() {
        return username;
    }

    public AppointmentStatus getStatus() {
        return status;
    }
}