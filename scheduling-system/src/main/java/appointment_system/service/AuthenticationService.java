package appointment_system.service;

import appointment_system.domain.Admin;
import appointment_system.repository.UserRepository;

/**
 * Handles administrator authentication and session state.
 *
 * @author Team 3
 * @version 2.0
 */
public class AuthenticationService {

    private final UserRepository repository;
    private Admin loggedInAdmin;

    public AuthenticationService(UserRepository repository) {
        if (repository == null) {
            throw new IllegalArgumentException("User repository must not be null");
        }
        this.repository = repository;
    }

    public boolean login(String username, String password) {
        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            return false;
        }

        Admin admin = repository.findByUsername(username.trim());

        if (admin != null && admin.matchesPassword(password)) {
            loggedInAdmin = admin;
            return true;
        }

        loggedInAdmin = null;
        return false;
    }

    public void logout() {
        loggedInAdmin = null;
    }

    public boolean isLoggedIn() {
        return loggedInAdmin != null;
    }

    public Admin getLoggedInAdmin() {
        return loggedInAdmin;
    }
}