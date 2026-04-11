package appointment_system.notification;

/**
 * Simple notification service that prints reminders to the console.
 *
 
 */
public class ConsoleNotificationService implements NotificationService {

    @Override
    public void sendReminder(String recipient, String message) {
        System.out.println("Reminder sent to " + recipient + ": " + message);
    }
}