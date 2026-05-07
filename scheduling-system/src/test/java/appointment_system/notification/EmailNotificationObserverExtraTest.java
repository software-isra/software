package appointment_system.notification;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;

class EmailNotificationObserverExtraTest {

    @Test
    void testNotifyDoesNotThrow() {
        EmailNotificationObserver observer = new EmailNotificationObserver();

        assertDoesNotThrow(() ->
                observer.notify("Test email notification message"));
    }

    @Test
    void testNotifyMultipleMessagesDoesNotThrow() {
        EmailNotificationObserver observer = new EmailNotificationObserver();

        assertDoesNotThrow(() -> {
            observer.notify("First message");
            observer.notify("Second message");
        });
    }
}
