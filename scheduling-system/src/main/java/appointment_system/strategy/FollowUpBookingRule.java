package appointment_system.strategy;

import appointment_system.domain.AppointmentSlot;

public class FollowUpBookingRule implements BookingRuleStrategy {

    @Override
    public boolean isValid(AppointmentSlot slot) {
        return slot.getDurationMinutes() <= 45;
    }

    @Override
    public String getErrorMessage() {
        return "Follow-up appointments must not exceed 45 minutes";
    }
}