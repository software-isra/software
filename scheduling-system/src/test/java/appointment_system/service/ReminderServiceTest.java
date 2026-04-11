package appointment_system.service;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import appointment_system.domain.Appointment;
import appointment_system.domain.AppointmentSlot;
import appointment_system.notification.NotificationService;
import appointment_system.repository.AppointmentRepository;

class ReminderServiceTest {

    private AppointmentRepository repo;
    private NotificationService notificationService;
    private ReminderService reminderService;

    @BeforeEach
    void setUp() {
        repo = new AppointmentRepository();
        notificationService = new NotificationService();
        reminderService = new ReminderService(repo, notificationService);
    }

    @Test
    void testSendUpcomingAppointmentRemindersReturnsMessageForAppointmentWithin24Hours() {
        AppointmentSlot slot = new AppointmentSlot(LocalDateTime.now().plusHours(5), 60, 2);
        repo.addSlot(slot);

        slot.addParticipant();
        repo.saveAppointment(new Appointment(slot, "user1"));

        List<String> messages = reminderService.sendUpcomingAppointmentReminders();

        assertEquals(1, messages.size());
        assertTrue(messages.get(0).contains("Reminder: You have an appointment on"));
    }

    @Test
    void testSendUpcomingAppointmentRemindersIgnoresAppointmentAfter24Hours() {
        AppointmentSlot slot = new AppointmentSlot(LocalDateTime.now().plusDays(2), 60, 2);
        repo.addSlot(slot);

        slot.addParticipant();
        repo.saveAppointment(new Appointment(slot, "user1"));

        List<String> messages = reminderService.sendUpcomingAppointmentReminders();

        assertTrue(messages.isEmpty());
    }

    @Test
    void testSendUpcomingAppointmentRemindersIgnoresPastAppointment() {
        AppointmentSlot slot = new AppointmentSlot(LocalDateTime.now().minusHours(2), 60, 2);
        repo.addSlot(slot);

        slot.addParticipant();
        repo.saveAppointment(new Appointment(slot, "user1"));

        List<String> messages = reminderService.sendUpcomingAppointmentReminders();

        assertTrue(messages.isEmpty());
    }

    @Test
    void testBuildReminderMessageRejectsNullAppointment() {
        assertThrows(IllegalArgumentException.class, () ->
                reminderService.buildReminderMessage(null));
    }

    @Test
    void testBuildReminderMessageContainsDateAndDuration() {
        AppointmentSlot slot = new AppointmentSlot(LocalDateTime.now().plusHours(3), 45, 2);
        Appointment appointment = new Appointment(slot, "user1");

        String message = reminderService.buildReminderMessage(appointment);

        assertTrue(message.contains("Reminder: You have an appointment on"));
        assertTrue(message.contains("45 minutes"));
    }
}