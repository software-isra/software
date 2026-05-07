package appointment_system.notification;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ConsoleNotificationService implements NotificationService {

    private static final Logger LOGGER = Logger.getLogger(ConsoleNotificationService.class.getName());

    @Override
    public void sendReminder(String recipient, String message) {
        LOGGER.log(Level.INFO, "Reminder sent to {0}: {1}", new Object[]{recipient, message});
    }
}
