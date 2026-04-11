package appointment_system.strategy;

import appointment_system.domain.AppointmentSlot;

public class VirtualBookingRule implements BookingRuleStrategy {

    @Override
    public boolean isValid(AppointmentSlot slot) {
        return slot.getMaxParticipants() == 1;
    }

    @Override
    public String getErrorMessage() {
        return "Virtual appointments must allow exactly one participant";
    }
}