package appointment_system.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import appointment_system.notification.NotificationService;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import appointment_system.domain.Appointment;
import appointment_system.domain.AppointmentSlot;
import appointment_system.notification.NotificationService;
import appointment_system.repository.AppointmentRepository;

class ReminderServiceTest {

    private AppointmentRepository repository;
    private NotificationService notificationService;
    private ReminderService reminderService;

    @BeforeEach
    void setUp() {
        repository = new AppointmentRepository();
        notificationService = Mockito.mock(NotificationService.class);
        reminderService = new ReminderService(repository, notificationService);
    }

    @Test
    void testSendReminderForUpcomingAppointment() {
        AppointmentSlot slot = new AppointmentSlot(LocalDateTime.now().plusHours(5), 60, 2);
        repository.addSlot(slot);

        Appointment appointment = new Appointment(slot, "user1");
        repository.saveAppointment(appointment);

        List<String> messages = reminderService.sendUpcomingAppointmentReminders();

        assertEquals(1, messages.size());
        assertTrue(messages.get(0).contains("Reminder: You have an appointment"));

        verify(notificationService, times(1))
                .sendReminder(Mockito.eq("user1"), Mockito.anyString());
    }

    @Test
    void testDoNotSendReminderForFarFutureAppointment() {
        AppointmentSlot slot = new AppointmentSlot(LocalDateTime.now().plusDays(3), 60, 2);
        repository.addSlot(slot);

        Appointment appointment = new Appointment(slot, "user2");
        repository.saveAppointment(appointment);

        List<String> messages = reminderService.sendUpcomingAppointmentReminders();

        assertEquals(0, messages.size());

        verify(notificationService, times(0))
                .sendReminder(Mockito.anyString(), Mockito.anyString());
    }

    @Test
    void testBuildReminderMessage() {
        AppointmentSlot slot = new AppointmentSlot(LocalDateTime.now().plusHours(2), 45, 2);
        Appointment appointment = new Appointment(slot, "user3");

        String message = reminderService.buildReminderMessage(appointment);

        assertTrue(message.contains("Reminder: You have an appointment"));
        assertTrue(message.contains("45 minutes"));
    }
}