package appointment_system.strategy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import appointment_system.domain.AppointmentSlot;

class BookingRulesExtraTest {

    @Test
    void testFollowUpBookingRuleValidWhenDurationIs45OrLess() {
        FollowUpBookingRule rule = new FollowUpBookingRule();
        AppointmentSlot slot = new AppointmentSlot(LocalDateTime.now().plusDays(1), 45, 1);

        assertTrue(rule.isValid(slot));
    }

    @Test
    void testFollowUpBookingRuleInvalidWhenDurationExceeds45() {
        FollowUpBookingRule rule = new FollowUpBookingRule();
        AppointmentSlot slot = new AppointmentSlot(LocalDateTime.now().plusDays(1), 60, 1);

        assertFalse(rule.isValid(slot));
        assertEquals("Follow-up appointments must not exceed 45 minutes", rule.getErrorMessage());
    }

    @Test
    void testAssessmentBookingRuleValidWhenDurationIs120OrLess() {
        AssessmentBookingRule rule = new AssessmentBookingRule();
        AppointmentSlot slot = new AppointmentSlot(LocalDateTime.now().plusDays(1), 120, 1);

        assertTrue(rule.isValid(slot));
    }

    @Test
    void testAssessmentBookingRuleInvalidWhenDurationExceeds120() {
        AssessmentBookingRule rule = new AssessmentBookingRule();
        AppointmentSlot slot = new AppointmentSlot(LocalDateTime.now().plusDays(1), 150, 1);

        assertFalse(rule.isValid(slot));
        assertEquals("Assessment appointments must not exceed 120 minutes", rule.getErrorMessage());
    }

    @Test
    void testIndividualBookingRuleValidWhenMaxParticipantsIsOne() {
        IndividualBookingRule rule = new IndividualBookingRule();
        AppointmentSlot slot = new AppointmentSlot(LocalDateTime.now().plusDays(1), 30, 1);

        assertTrue(rule.isValid(slot));
    }

    @Test
    void testIndividualBookingRuleInvalidWhenMaxParticipantsIsMoreThanOne() {
        IndividualBookingRule rule = new IndividualBookingRule();
        AppointmentSlot slot = new AppointmentSlot(LocalDateTime.now().plusDays(1), 30, 2);

        assertFalse(rule.isValid(slot));
        assertEquals("Individual appointments must allow exactly one participant", rule.getErrorMessage());
    }
}
