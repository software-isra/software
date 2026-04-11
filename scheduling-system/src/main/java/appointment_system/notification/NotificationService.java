package appointment_system.notification;

/**
 * Notification abstraction used by the system to send reminder messages.
 *
 
 */
public interface NotificationService {

    /**
     * Sends a reminder message to a recipient.
     *
     * @param recipient the user identifier or username
     * @param message reminder content
     */
    void sendReminder(String recipient, String message);
}