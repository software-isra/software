package appointment_system.service;

import appointment_system.domain.AppointmentSlot;

/**
 * Allows administrators to manage reservations.
 *
 
 */
public class AdminReservationService {

    private final AuthenticationService authenticationService;
    private final AppointmentService appointmentService;

    public AdminReservationService(AuthenticationService authenticationService,
                                   AppointmentService appointmentService) {
        if (authenticationService == null) {
            throw new IllegalArgumentException("Authentication service must not be null");
        }
        if (appointmentService == null) {
            throw new IllegalArgumentException("Appointment service must not be null");
        }

        this.authenticationService = authenticationService;
        this.appointmentService = appointmentService;
    }

    public boolean cancelReservation(String username, AppointmentSlot slot) {
        requireAdminLogin();
        return appointmentService.cancelAppointment(username, slot);
    }

    public boolean modifyReservation(String username, AppointmentSlot oldSlot, AppointmentSlot newSlot) {
        requireAdminLogin();
        return appointmentService.modifyAppointment(username, oldSlot, newSlot);
    }

    private void requireAdminLogin() {
        if (!authenticationService.isLoggedIn()) {
            throw new IllegalStateException("Administrator must be logged in");
        }
    }
}