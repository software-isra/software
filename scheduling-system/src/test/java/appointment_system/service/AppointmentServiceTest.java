package appointment_system.service;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import appointment_system.domain.AppointmentSlot;
import appointment_system.repository.AppointmentRepository;

class AppointmentServiceTest {

    private AppointmentService service;
    private AppointmentRepository repo;

    @BeforeEach
    void setup() {
        repo = new AppointmentRepository();
        service = new AppointmentService(repo);
    }

    @Test
    void testGetAvailableSlots() {
        AppointmentSlot slot1 = new AppointmentSlot(LocalDateTime.now().plusDays(1), 60, 1);
        AppointmentSlot slot2 = new AppointmentSlot(LocalDateTime.now().plusDays(1).plusHours(1), 60, 1);

        slot2.addParticipant(); // full

        repo.addSlot(slot1);
        repo.addSlot(slot2);

        List<AppointmentSlot> result = service.getAvailableSlots();

        assertEquals(1, result.size());
        assertEquals(slot1, result.get(0));
    }

    @Test
    void testBookAppointment() {
        AppointmentSlot slot = new AppointmentSlot(LocalDateTime.now().plusDays(1), 60, 1);
        repo.addSlot(slot);

        boolean actual = service.bookAppointment("user1", slot);

        assertTrue(actual);
        assertEquals(1, repo.getAppointments().size());
    }

    @Test
    void testBookAppointmentWhenFull() {
        AppointmentSlot slot = new AppointmentSlot(LocalDateTime.now().plusDays(1), 60, 1);
        slot.addParticipant(); // full
        repo.addSlot(slot);

        boolean actual = service.bookAppointment("user1", slot);

        assertFalse(actual);
    }

    @Test
    void testInvalidDuration() {
        AppointmentSlot slot = new AppointmentSlot(LocalDateTime.now().plusDays(1), 200, 2);
        repo.addSlot(slot);

        assertThrows(IllegalArgumentException.class, () -> {
            service.bookAppointment("user1", slot);
        });
    }

    @Test
    void testRejectNullUsername() {
        AppointmentSlot slot = new AppointmentSlot(LocalDateTime.now().plusDays(1), 60, 1);
        repo.addSlot(slot);

        assertThrows(IllegalArgumentException.class, () -> {
            service.bookAppointment(null, slot);
        });
    }

    @Test
    void testRejectPastSlot() {
        AppointmentSlot slot = new AppointmentSlot(LocalDateTime.now().minusDays(1), 60, 1);
        repo.addSlot(slot);

        assertThrows(IllegalArgumentException.class, () -> {
            service.bookAppointment("user1", slot);
        });
    }

    @Test
    void testRejectDuplicateBookingForSameUserAndSlot() {
        AppointmentSlot slot = new AppointmentSlot(LocalDateTime.now().plusDays(1), 60, 2);
        repo.addSlot(slot);

        assertTrue(service.bookAppointment("user1", slot));
        assertFalse(service.bookAppointment("user1", slot));
    }
}