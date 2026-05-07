package appointment_system.notification;

import java.util.logging.Logger;

public class ConsoleNotificationService implements NotificationService {

    private static final Logger LOGGER = Logger.getLogger(ConsoleNotificationService.class.getName());

    @Override
    public void sendReminder(String recipient, String message) {
        LOGGER.info("Reminder sent to " + recipient + ": " + message);
    }
}
