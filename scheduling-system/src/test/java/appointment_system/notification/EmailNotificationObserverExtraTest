package appointment_system.notification;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import appointment_system.domain.User;

class EmailNotificationObserverExtraTest {

    @Test
    void testNotifyRejectsUserWithNullEmail() {
        EmailNotificationObserver observer =
                new EmailNotificationObserver("test@gmail.com", "pass");

        User user = new User("ali", "1234", "ali@gmail.com") {
            @Override
            public String getEmail() {
                return null;
            }
        };

        assertThrows(IllegalArgumentException.class, () ->
                observer.notify(user, "subject", "message"));
    }
}
