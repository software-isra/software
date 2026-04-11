package appointment_system.repository;

import java.util.ArrayList;
import java.util.List;

import appointment_system.domain.Appointment;
import appointment_system.domain.AppointmentSlot;

/**
 * In-memory repository for appointment slots and bookings.
 */
public class AppointmentRepository {

    private final List<AppointmentSlot> slots = new ArrayList<>();
    private final List<Appointment> appointments = new ArrayList<>();

    public void addSlot(AppointmentSlot slot) {
        if (slot == null) {
            throw new IllegalArgumentException("Slot must not be null");
        }
        slots.add(slot);
    }

    public List<AppointmentSlot> getSlots() {
        return new ArrayList<>(slots);
    }

    public void saveAppointment(Appointment appointment) {
        if (appointment == null) {
            throw new IllegalArgumentException("Appointment must not be null");
        }
        appointments.add(appointment);
    }

    public List<Appointment> getAppointments() {
        return new ArrayList<>(appointments);
    }

    public boolean containsSlot(AppointmentSlot slot) {
        return slots.contains(slot);
    }

    public boolean hasBooking(String username, AppointmentSlot slot) {
        for (Appointment appointment : appointments) {
            if (!appointment.isCancelled()
                    && appointment.getUsername().equals(username)
                    && appointment.getSlot().equals(slot)) {
                return true;
            }
        }
        return false;
    }

    public Appointment findActiveAppointment(String username, AppointmentSlot slot) {
        for (Appointment appointment : appointments) {
            if (!appointment.isCancelled()
                    && appointment.getUsername().equals(username)
                    && appointment.getSlot().equals(slot)) {
                return appointment;
            }
        }
        return null;
    }

    public Appointment findActiveAppointmentByUsername(String username) {
        for (Appointment appointment : appointments) {
            if (!appointment.isCancelled()
                    && appointment.getUsername().equals(username)) {
                return appointment;
            }
        }
        return null;
    }
}