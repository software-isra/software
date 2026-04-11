package appointment_system.notification;

/**
 * Notification abstraction used by the system to send reminder messages.
 *
 * @author Team 3
 * @version 1.0
 */
public interface NotificationService {

    void sendReminder(String recipient, String message);
}