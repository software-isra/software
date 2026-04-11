package appointment_system.notification;

public class EmailNotificationObserver implements Observer {

    @Override
    public void notify(String message) {
        System.out.println("📧 Email: " + message);
    }
}