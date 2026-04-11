package appointment_system.service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import appointment_system.domain.Appointment;
import appointment_system.domain.AppointmentSlot;
import appointment_system.domain.AppointmentType;
import appointment_system.repository.AppointmentRepository;
import appointment_system.strategy.BookingRuleFactory;
import appointment_system.strategy.BookingRuleStrategy;

/**
 * Handles booking operations and booking validation rules.
 *
 */
public class AppointmentService {

    private static final int MAX_DURATION = 120;

    private final AppointmentRepository repository;
    private final BookingRuleFactory bookingRuleFactory;

    public AppointmentService(AppointmentRepository repository) {
        if (repository == null) {
            throw new IllegalArgumentException("Appointment repository must not be null");
        }
        this.repository = repository;
        this.bookingRuleFactory = new BookingRuleFactory();
    }

    public List<AppointmentSlot> getAvailableSlots() {
        return repository.getSlots()
                .stream()
                .filter(slot -> !slot.isFull())
                .sorted(Comparator.comparing(AppointmentSlot::getStartTime))
                .collect(Collectors.toList());
    }

    public List<Appointment> getAllAppointments() {
        return repository.getAppointments()
                .stream()
                .filter(appointment -> !appointment.isCancelled())
                .collect(Collectors.toList());
    }

    public boolean bookAppointment(String username, AppointmentSlot slot) {
        return bookAppointment(username, slot, AppointmentType.IN_PERSON);
    }

    public boolean bookAppointment(String username, AppointmentSlot slot, AppointmentType type) {
        validateBookingRequest(username, slot, type);

        if (slot.isFull()) {
            return false;
        }

        if (repository.hasBooking(username.trim(), slot)) {
            return false;
        }

        slot.addParticipant();
        Appointment appointment = new Appointment(slot, username.trim(), type);
        repository.saveAppointment(appointment);
        return true;
    }

    public boolean cancelAppointment(String username, AppointmentSlot slot) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username is required");
        }
        if (slot == null) {
            throw new IllegalArgumentException("Appointment slot is required");
        }

        Appointment appointment = repository.findActiveAppointment(username.trim(), slot);
        if (appointment == null) {
            return false;
        }

        if (slot.isInPast()) {
            return false;
        }

        slot.removeParticipant();
        appointment.cancel();
        return true;
    }

    public boolean modifyAppointment(String username, AppointmentSlot oldSlot, AppointmentSlot newSlot) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username is required");
        }
        if (oldSlot == null || newSlot == null) {
            throw new IllegalArgumentException("Old and new slots are required");
        }

        Appointment appointment = repository.findActiveAppointment(username.trim(), oldSlot);
        if (appointment == null) {
            return false;
        }

        if (oldSlot.isInPast()) {
            return false;
        }

        validateBookingRequest(username, newSlot, appointment.getType());

        if (newSlot.isFull()) {
            return false;
        }

        if (repository.hasBooking(username.trim(), newSlot)) {
            return false;
        }

        oldSlot.removeParticipant();
        newSlot.addParticipant();
        appointment.setSlot(newSlot);

        return true;
    }

    private void validateBookingRequest(String username, AppointmentSlot slot, AppointmentType type) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username is required");
        }

        if (slot == null) {
            throw new IllegalArgumentException("Appointment slot is required");
        }

        if (type == null) {
            throw new IllegalArgumentException("Appointment type is required");
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

        BookingRuleStrategy rule = bookingRuleFactory.getRule(type);
        if (!rule.isValid(slot)) {
            throw new IllegalArgumentException(rule.getErrorMessage());
        }
    }
}