package appointment_system.notification;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;

import appointment_system.domain.User;

class ConsoleNotificationServiceTest {

    @Test
    void testSendReminderDoesNotThrow() {
        ConsoleNotificationService service = new ConsoleNotificationService();
        User user = new User("ali", "1234", "ali@gmail.com");

        assertDoesNotThrow(() ->
                service.sendReminder(user, "Reminder", "Your appointment is tomorrow."));
    }

    @Test
    void testSendReminderMultipleTimes() {
        ConsoleNotificationService service = new ConsoleNotificationService();
        User user = new User("ali", "1234", "ali@gmail.com");

        assertDoesNotThrow(() -> {
            service.sendReminder(user, "subject1", "message1");
            service.sendReminder(user, "subject2", "message2");
        });
    }
}
