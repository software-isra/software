package appointment_system.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import appointment_system.domain.AppointmentType;
import org.junit.jupiter.api.Test;

class DomainTest {

    @Test
    void testUserCreationAndUsername() {
        User user = new User("ali", "1234");
        assertEquals("ali", user.getUsername());
        assertTrue(user.matchesPassword("1234"));
        assertFalse(user.matchesPassword("wrong"));
    }

    @Test
    void testUserRejectsBlankUsername() {
        assertThrows(IllegalArgumentException.class, () -> new User("", "1234"));
    }

    @Test
    void testUserRejectsBlankPassword() {
        assertThrows(IllegalArgumentException.class, () -> new User("ali", ""));
    }

    @Test
    void testAdminCreation() {
        Admin admin = new Admin("admin", "pass");
        assertEquals("admin", admin.getUsername());
        assertTrue(admin.matchesPassword("pass"));
    }

    @Test
    void testAppointmentSlotCreation() {
        LocalDateTime time = LocalDateTime.now().plusDays(1);
        AppointmentSlot slot = new AppointmentSlot(time, 60, 2);

        assertEquals(time, slot.getStartTime());
        assertEquals(60, slot.getDurationMinutes());
        assertEquals(2, slot.getMaxParticipants());
        assertEquals(0, slot.getBookedParticipants());
        assertEquals(2, slot.getRemainingCapacity());
        assertFalse(slot.isFull());
        assertFalse(slot.isInPast());
    }

    @Test
    void testAppointmentSlotRejectsNullStartTime() {
        assertThrows(IllegalArgumentException.class, () -> new AppointmentSlot(null, 60, 2));
    }

    @Test
    void testAppointmentSlotRejectsInvalidDuration() {
        assertThrows(IllegalArgumentException.class, () -> new AppointmentSlot(LocalDateTime.now().plusDays(1), 0, 2));
    }

    @Test
    void testAppointmentSlotRejectsInvalidMaxParticipants() {
        assertThrows(IllegalArgumentException.class, () -> new AppointmentSlot(LocalDateTime.now().plusDays(1), 60, 0));
    }

    @Test
    void testAddParticipantAndIsFull() {
        AppointmentSlot slot = new AppointmentSlot(LocalDateTime.now().plusDays(1), 60, 2);

        slot.addParticipant();
        assertEquals(1, slot.getBookedParticipants());
        assertEquals(1, slot.getRemainingCapacity());
        assertFalse(slot.isFull());

        slot.addParticipant();
        assertEquals(2, slot.getBookedParticipants());
        assertEquals(0, slot.getRemainingCapacity());
        assertTrue(slot.isFull());
    }

    @Test
    void testAddParticipantThrowsWhenFull() {
        AppointmentSlot slot = new AppointmentSlot(LocalDateTime.now().plusDays(1), 60, 1);

        slot.addParticipant();

        assertThrows(IllegalStateException.class, slot::addParticipant);
    }

    @Test
    void testRemoveParticipant() {
        AppointmentSlot slot = new AppointmentSlot(LocalDateTime.now().plusDays(1), 60, 2);

        slot.addParticipant();
        slot.removeParticipant();

        assertEquals(0, slot.getBookedParticipants());
        assertEquals(2, slot.getRemainingCapacity());
    }

    @Test
    void testAppointmentCreation() {
        AppointmentSlot slot = new AppointmentSlot(LocalDateTime.now().plusDays(1), 60, 2);
        Appointment appointment = new Appointment(slot, "user1");

        assertEquals(slot, appointment.getSlot());
        assertEquals("user1", appointment.getUsername());
        assertEquals(AppointmentStatus.CONFIRMED, appointment.getStatus());
    }

    @Test
    void testAppointmentRejectsNullSlot() {
        assertThrows(IllegalArgumentException.class, () -> new Appointment(null, "user1"));
    }

    @Test
    void testAppointmentRejectsBlankUsername() {
        AppointmentSlot slot = new AppointmentSlot(LocalDateTime.now().plusDays(1), 60, 2);
        assertThrows(IllegalArgumentException.class, () -> new Appointment(slot, ""));
    }
    @Test
    void testAppointmentTypeStoredCorrectly() {
        AppointmentSlot slot = new AppointmentSlot(LocalDateTime.now().plusDays(1), 30, 1);
        Appointment appointment = new Appointment(slot, "user1", AppointmentType.URGENT);

        assertEquals(AppointmentType.URGENT, appointment.getType());
    }

    @Test
    void testDefaultAppointmentTypeIsInPerson() {
        AppointmentSlot slot = new AppointmentSlot(LocalDateTime.now().plusDays(1), 60, 1);
        Appointment appointment = new Appointment(slot, "user1");

        assertEquals(AppointmentType.IN_PERSON, appointment.getType());
    }
}