package appointment_system.strategy;

import appointment_system.domain.AppointmentSlot;

public class GroupBookingRule implements BookingRuleStrategy {

    @Override
    public boolean isValid(AppointmentSlot slot) {
        return slot.getMaxParticipants() >= 2;
    }

    @Override
    public String getErrorMessage() {
        return "Group appointments must allow at least two participants";
    }
}