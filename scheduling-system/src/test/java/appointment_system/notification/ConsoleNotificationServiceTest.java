package appointment_system.notification;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;

class ConsoleNotificationServiceTest {

    @Test
    void testSendReminderDoesNotThrow() {
        ConsoleNotificationService service = new ConsoleNotificationService();

        assertDoesNotThrow(() ->
                service.sendReminder("ali@gmail.com", "Your appointment is tomorrow.")
        );
    }

    @Test
    void testSendReminderMultipleTimes() {
        ConsoleNotificationService service = new ConsoleNotificationService();

        assertDoesNotThrow(() -> {
            service.sendReminder("ali@gmail.com", "message1");
            service.sendReminder("ali@gmail.com", "message2");
        });
    }
}
