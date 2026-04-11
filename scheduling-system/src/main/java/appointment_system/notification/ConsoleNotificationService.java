package appointment_system.notification;

public class ConsoleNotificationService implements NotificationService {

    @Override
    public void sendReminder(String recipient, String message) {
        System.out.println("Reminder sent to " + recipient + ": " + message);
    }
}