package appointment_system.domain;
/**
 *Represents an appointment in the scheduling system
 *Each appointment contains a time slot, the username of the user
 *who booked the appointment, and the appointment status
 */
 public class Appointment {
	private AppointmentSlot slot;
    private String username;
    private AppointmentStatus status;
/**
 * 
 * @param slot
 * @param username
 */
    public Appointment(AppointmentSlot slot, String username) {
        this.slot = slot;
        this.username = username;
        this.status = AppointmentStatus.CONFIRMED;
    }
/**
 * 
 * @return slot
 */
    public AppointmentSlot getSlot() {
        return slot;
    }
/**
 * 
 * @return username 
 */
    public String getUsername() {
        return username;
    }
/**
 * 
 * @return Appointment Status
 */
    public AppointmentStatus getStatus() {
        return status;
    }
}
