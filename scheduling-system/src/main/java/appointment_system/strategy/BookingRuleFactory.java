package appointment_system.strategy;

import appointment_system.domain.AppointmentType;

/**
 * Factory for selecting booking rule strategies based on appointment type.
 *
 */
public class BookingRuleFactory {

    public BookingRuleStrategy getRule(AppointmentType type) {
        if (type == null) {
            throw new IllegalArgumentException("Appointment type must not be null");
        }

        switch (type) {
            case URGENT:
                return new UrgentBookingRule();
            case FOLLOW_UP:
                return new FollowUpBookingRule();
            case ASSESSMENT:
                return new AssessmentBookingRule();
            case VIRTUAL:
                return new VirtualBookingRule();
            case INDIVIDUAL:
                return new IndividualBookingRule();
            case GROUP:
                return new GroupBookingRule();
            case IN_PERSON:
            default:
                return new DefaultBookingRule();
        }
    }
}