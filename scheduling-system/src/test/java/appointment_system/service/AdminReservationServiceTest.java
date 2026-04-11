package appointment_system.service;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import appointment_system.domain.AppointmentSlot;
import appointment_system.repository.AppointmentRepository;
import appointment_system.repository.UserRepository;

class AdminReservationServiceTest {

    private AppointmentRepository appointmentRepository;
    private AppointmentService appointmentService;
    private AuthenticationService authenticationService;
    private AdminReservationService adminReservationService;

    @BeforeEach
    void setUp() {
        appointmentRepository = new AppointmentRepository();
        appointmentService = new AppointmentService(appointmentRepository);
        authenticationService = new AuthenticationService(new UserRepository());
        adminReservationService = new AdminReservationService(authenticationService, appointmentService);
    }

    @Test
    void testAdminCanCancelReservationWhenLoggedIn() {
        AppointmentSlot slot = new AppointmentSlot(LocalDateTime.now().plusDays(1), 60, 2);
        appointmentRepository.addSlot(slot);

        appointmentService.bookAppointment("user1", slot);

        authenticationService.login("admin", "1234");

        boolean cancelled = adminReservationService.cancelReservation("user1", slot);

        assertTrue(cancelled);
        assertEquals(0, slot.getBookedParticipants());
    }

    @Test
    void testAdminCannotManageReservationsWithoutLogin() {
        AppointmentSlot slot = new AppointmentSlot(LocalDateTime.now().plusDays(1), 60, 2);
        appointmentRepository.addSlot(slot);
        appointmentService.bookAppointment("user1", slot);

        assertThrows(IllegalStateException.class, () -> {
            adminReservationService.cancelReservation("user1", slot);
        });
    }

    @Test
    void testAdminCanModifyReservationWhenLoggedIn() {
        AppointmentSlot oldSlot = new AppointmentSlot(LocalDateTime.now().plusDays(1), 60, 2);
        AppointmentSlot newSlot = new AppointmentSlot(LocalDateTime.now().plusDays(1).plusHours(2), 60, 2);

        appointmentRepository.addSlot(oldSlot);
        appointmentRepository.addSlot(newSlot);

        appointmentService.bookAppointment("user1", oldSlot);

        authenticationService.login("admin", "1234");

        boolean modified = adminReservationService.modifyReservation("user1", oldSlot, newSlot);

        assertTrue(modified);
        assertEquals(0, oldSlot.getBookedParticipants());
        assertEquals(1, newSlot.getBookedParticipants());
    }
}