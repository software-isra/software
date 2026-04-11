package appointment_system.strategy;

import appointment_system.domain.AppointmentSlot;

public class DefaultBookingRule implements BookingRuleStrategy {

    @Override
    public boolean isValid(AppointmentSlot slot) {
        return true;
    }

    @Override
    public String getErrorMessage() {
        return "Invalid appointment configuration";
    }
}