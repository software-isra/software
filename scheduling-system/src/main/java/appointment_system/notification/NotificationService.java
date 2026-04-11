package appointment_system.notification;

import java.util.ArrayList;
import java.util.List;

public class NotificationService {

    private final List<Observer> observers = new ArrayList<>();

    public void attach(Observer observer) {
        observers.add(observer);
    }

    public void notifyAllObservers(String message) {
        for (Observer observer : observers) {
            observer.notify(message);
        }
    }
}