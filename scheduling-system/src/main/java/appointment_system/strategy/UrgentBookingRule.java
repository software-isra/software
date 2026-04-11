package appointment_system.strategy;

import appointment_system.domain.AppointmentSlot;

public class UrgentBookingRule implements BookingRuleStrategy {

    @Override
    public boolean isValid(AppointmentSlot slot) {
        return slot.getDurationMinutes() <= 30;
    }

    @Override
    public String getErrorMessage() {
        return "Urgent appointments must not exceed 30 minutes";
    }
}