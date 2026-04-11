package appointment_system.service;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import appointment_system.domain.AppointmentSlot;
import appointment_system.repository.AppointmentRepository;
import appointment_system.domain.AppointmentType;
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
    @Test
    void testCancelFutureAppointment() {
        AppointmentSlot slot = new AppointmentSlot(LocalDateTime.now().plusDays(1), 60, 2);
        repo.addSlot(slot);

        assertTrue(service.bookAppointment("user1", slot));
        assertTrue(service.cancelAppointment("user1", slot));

        assertEquals(0, slot.getBookedParticipants());
        assertEquals(0, service.getAllAppointments().size());
    }

    @Test
    void testCannotCancelNonExistingAppointment() {
        AppointmentSlot slot = new AppointmentSlot(LocalDateTime.now().plusDays(1), 60, 2);
        repo.addSlot(slot);

        boolean cancelled = service.cancelAppointment("user1", slot);

        assertFalse(cancelled);
    }

    @Test
    void testModifyFutureAppointment() {
        AppointmentSlot oldSlot = new AppointmentSlot(LocalDateTime.now().plusDays(1), 60, 2);
        AppointmentSlot newSlot = new AppointmentSlot(LocalDateTime.now().plusDays(1).plusHours(2), 60, 2);

        repo.addSlot(oldSlot);
        repo.addSlot(newSlot);

        assertTrue(service.bookAppointment("user1", oldSlot));
        assertTrue(service.modifyAppointment("user1", oldSlot, newSlot));

        assertEquals(0, oldSlot.getBookedParticipants());
        assertEquals(1, newSlot.getBookedParticipants());
    }

    @Test
    void testCannotModifyToFullSlot() {
        AppointmentSlot oldSlot = new AppointmentSlot(LocalDateTime.now().plusDays(1), 60, 2);
        AppointmentSlot newSlot = new AppointmentSlot(LocalDateTime.now().plusDays(1).plusHours(1), 60, 1);

        repo.addSlot(oldSlot);
        repo.addSlot(newSlot);

        assertTrue(service.bookAppointment("user1", oldSlot));
        newSlot.addParticipant(); // make full

        boolean modified = service.modifyAppointment("user1", oldSlot, newSlot);

        assertFalse(modified);
    }
    @Test
    void testBookAppointmentWithUrgentType() {
        AppointmentSlot slot = new AppointmentSlot(LocalDateTime.now().plusDays(1), 30, 1);
        repo.addSlot(slot);

        boolean booked = service.bookAppointment("user1", slot, AppointmentType.URGENT);

        assertTrue(booked);
        assertEquals(AppointmentType.URGENT, service.getAllAppointments().get(0).getType());
    }

    @Test
    void testUrgentAppointmentRejectsLongDuration() {
        AppointmentSlot slot = new AppointmentSlot(LocalDateTime.now().plusDays(1), 60, 1);
        repo.addSlot(slot);

        assertThrows(IllegalArgumentException.class, () -> {
            service.bookAppointment("user1", slot, AppointmentType.URGENT);
        });
    }

    @Test
    void testVirtualAppointmentRequiresSingleParticipant() {
        AppointmentSlot slot = new AppointmentSlot(LocalDateTime.now().plusDays(1), 30, 2);
        repo.addSlot(slot);

        assertThrows(IllegalArgumentException.class, () -> {
            service.bookAppointment("user1", slot, AppointmentType.VIRTUAL);
        });
    }

    @Test
    void testIndividualAppointmentRequiresSingleParticipant() {
        AppointmentSlot slot = new AppointmentSlot(LocalDateTime.now().plusDays(1), 30, 2);
        repo.addSlot(slot);

        assertThrows(IllegalArgumentException.class, () -> {
            service.bookAppointment("user1", slot, AppointmentType.INDIVIDUAL);
        });
    }

    @Test
    void testGroupAppointmentRequiresAtLeastTwoParticipants() {
        AppointmentSlot slot = new AppointmentSlot(LocalDateTime.now().plusDays(1), 60, 1);
        repo.addSlot(slot);

        assertThrows(IllegalArgumentException.class, () -> {
            service.bookAppointment("user1", slot, AppointmentType.GROUP);
        });
    }

    @Test
    void testGroupAppointmentAcceptedWhenCapacityIsTwoOrMore() {
        AppointmentSlot slot = new AppointmentSlot(LocalDateTime.now().plusDays(1), 60, 3);
        repo.addSlot(slot);

        boolean booked = service.bookAppointment("user1", slot, AppointmentType.GROUP);

        assertTrue(booked);
        assertEquals(AppointmentType.GROUP, service.getAllAppointments().get(0).getType());
    }
}