package appointment_system.notification;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class ConsoleNotificationServiceTest {

    @Test
    void testSendReminder() {
        ConsoleNotificationService service = new ConsoleNotificationService();

        assertDoesNotThrow(() -> {
            service.sendReminder("user1", "Test message");
        });
    }
}