package appointment_system.strategy;

import appointment_system.domain.AppointmentSlot;

/**
 * Strategy interface for appointment-type-specific booking rules.
 *
 
 */
public interface BookingRuleStrategy {

    /**
     * Validates whether the slot satisfies the rule for a specific type.
     *
     * @param slot appointment slot
     * @return true if valid, otherwise false
     */
    boolean isValid(AppointmentSlot slot);

    /**
     * @return validation error message when the rule is violated
     */
    String getErrorMessage();
}