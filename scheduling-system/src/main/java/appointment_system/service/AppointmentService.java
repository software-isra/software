package appointment_system.service;

import java.util.Comparator;
import java.util.List;
import appointment_system.domain.Appointment;
import appointment_system.domain.AppointmentSlot;
import appointment_system.repository.AppointmentRepository;

/**
 * Handles booking operations and booking validation rules.
 * 
 */
public class AppointmentService {

    private static final int MAX_DURATION = 120;

    private final AppointmentRepository repository;

    public AppointmentService(AppointmentRepository repository) {
        if (repository == null) {
            throw new IllegalArgumentException("Appointment repository must not be null");
        }
        this.repository = repository;
    }

    public List<AppointmentSlot> getAvailableSlots() {
        return repository.getSlots()
                .stream()
                .filter(slot -> !slot.isFull())
                .sorted(Comparator.comparing(AppointmentSlot::getStartTime))
                .toList();
    }

    public List<Appointment> getAllAppointments() {
        return repository.getAppointments();
    }

    public boolean bookAppointment(String username, AppointmentSlot slot) {
        validateBookingRequest(username, slot);

        if (slot.isFull()) {
            return false;
        }

        if (repository.hasBooking(username.trim(), slot)) {
            return false;
        }

        slot.addParticipant();
        Appointment appointment = new Appointment(slot, username.trim());
        repository.saveAppointment(appointment);
        return true;
    }

    private void validateBookingRequest(String username, AppointmentSlot slot) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username is required");
        }

        if (slot == null) {
            throw new IllegalArgumentException("Appointment slot is required");
        }

        if (!repository.containsSlot(slot)) {
            throw new IllegalArgumentException("Selected slot does not exist in the system");
        }

        if (slot.isInPast()) {
            throw new IllegalArgumentException("Cannot book an appointment in the past");
        }

        if (slot.getDurationMinutes() > MAX_DURATION) {
            throw new IllegalArgumentException("Duration exceeds allowed limit");
        }
    }
}