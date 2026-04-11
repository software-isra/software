package appointment_system.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import appointment_system.domain.Appointment;
import appointment_system.notification.NotificationService;
import appointment_system.repository.AppointmentRepository;

/**
 * Service responsible for generating and sending appointment reminders.
 *
 */
public class ReminderService {

    private final AppointmentRepository appointmentRepository;
    private final NotificationService notificationService;

    public ReminderService(AppointmentRepository appointmentRepository,
                           NotificationService notificationService) {
        if (appointmentRepository == null) {
            throw new IllegalArgumentException("Appointment repository must not be null");
        }
        if (notificationService == null) {
            throw new IllegalArgumentException("Notification service must not be null");
        }

        this.appointmentRepository = appointmentRepository;
        this.notificationService = notificationService;
    }

    /**
     * Sends reminders for appointments happening within the next 24 hours.
     *
     * @return list of generated reminder messages
     */
    public List<String> sendUpcomingAppointmentReminders() {
        List<String> generatedMessages = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        for (Appointment appointment : appointmentRepository.getAppointments()) {
            LocalDateTime appointmentTime = appointment.getSlot().getStartTime();

            boolean isFutureAppointment = appointmentTime.isAfter(now);
            boolean withinNext24Hours =
                    Duration.between(now, appointmentTime).toHours() <= 24;

            if (isFutureAppointment && withinNext24Hours) {
                String message = buildReminderMessage(appointment);
                notificationService.sendReminder(appointment.getUsername(), message);
                generatedMessages.add(message);
            }
        }

        return generatedMessages;
    }

    /**
     * Builds a reminder message for a single appointment.
     *
     * @param appointment the appointment
     * @return formatted reminder message
     */
    public String buildReminderMessage(Appointment appointment) {
        if (appointment == null) {
            throw new IllegalArgumentException("Appointment must not be null");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        return "Reminder: You have an appointment on "
                + appointment.getSlot().getStartTime().format(formatter)
                + " for "
                + appointment.getSlot().getDurationMinutes()
                + " minutes.";
    }
}