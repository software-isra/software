package appointment_system.strategy;

import appointment_system.domain.AppointmentSlot;

public class AssessmentBookingRule implements BookingRuleStrategy {

    @Override
    public boolean isValid(AppointmentSlot slot) {
        return slot.getDurationMinutes() <= 120;
    }

    @Override
    public String getErrorMessage() {
        return "Assessment appointments must not exceed 120 minutes";
    }
}