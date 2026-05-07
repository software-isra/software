package appointment_system.notification;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class EmailNotificationObserverTest2 {

    @Test
    void notifyShouldNotThrowException() {
        EmailNotificationObserver observer = new EmailNotificationObserver();

        assertDoesNotThrow(() ->
                observer.notify("Test email notification message")
        );
    }
}
