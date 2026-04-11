package appointment_system.notification;

public interface NotificationService {
    void sendReminder(String recipient, String message);
}