package appointment_system.strategy;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import appointment_system.domain.AppointmentSlot;
import appointment_system.domain.AppointmentType;

class BookingRuleFactoryTest {

    private BookingRuleFactory factory;

    @BeforeEach
    void setUp() {
        factory = new BookingRuleFactory();
    }

    @Test
    void testGetRuleForUrgent() {
        BookingRuleStrategy rule = factory.getRule(AppointmentType.URGENT);
        assertNotNull(rule);

        AppointmentSlot validSlot = new AppointmentSlot(LocalDateTime.now().plusDays(1), 30, 1);
        AppointmentSlot invalidSlot = new AppointmentSlot(LocalDateTime.now().plusDays(1), 60, 1);

        assertTrue(rule.isValid(validSlot));
        assertFalse(rule.isValid(invalidSlot));
    }

    @Test
    void testGetRuleForVirtual() {
        BookingRuleStrategy rule = factory.getRule(AppointmentType.VIRTUAL);
        assertNotNull(rule);

        AppointmentSlot validSlot = new AppointmentSlot(LocalDateTime.now().plusDays(1), 30, 1);
        AppointmentSlot invalidSlot = new AppointmentSlot(LocalDateTime.now().plusDays(1), 30, 2);

        assertTrue(rule.isValid(validSlot));
        assertFalse(rule.isValid(invalidSlot));
    }

    @Test
    void testGetRuleForIndividual() {
        BookingRuleStrategy rule = factory.getRule(AppointmentType.INDIVIDUAL);
        assertNotNull(rule);

        AppointmentSlot validSlot = new AppointmentSlot(LocalDateTime.now().plusDays(1), 30, 1);
        AppointmentSlot invalidSlot = new AppointmentSlot(LocalDateTime.now().plusDays(1), 30, 3);

        assertTrue(rule.isValid(validSlot));
        assertFalse(rule.isValid(invalidSlot));
    }

    @Test
    void testGetRuleForGroup() {
        BookingRuleStrategy rule = factory.getRule(AppointmentType.GROUP);
        assertNotNull(rule);

        AppointmentSlot validSlot = new AppointmentSlot(LocalDateTime.now().plusDays(1), 60, 3);
        AppointmentSlot invalidSlot = new AppointmentSlot(LocalDateTime.now().plusDays(1), 60, 1);

        assertTrue(rule.isValid(validSlot));
        assertFalse(rule.isValid(invalidSlot));
    }

    @Test
    void testGetRuleForInPerson() {
        BookingRuleStrategy rule = factory.getRule(AppointmentType.IN_PERSON);
        assertNotNull(rule);

        AppointmentSlot slot = new AppointmentSlot(LocalDateTime.now().plusDays(1), 60, 2);
        assertTrue(rule.isValid(slot));
    }
}