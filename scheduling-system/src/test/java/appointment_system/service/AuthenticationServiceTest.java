package appointment_system.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import appointment_system.repository.UserRepository;

class AuthenticationServiceTest {

    private AuthenticationService service;

    @BeforeEach
    void setUp() {
        UserRepository repo = new UserRepository();
        service = new AuthenticationService(repo);
    }

    @Test
    void testLoginSuccess() {
        boolean result = service.login("admin", "1234");

        assertTrue(result);
        assertTrue(service.isLoggedIn());
        assertNotNull(service.getLoggedInAdmin());
    }

    @Test
    void testLoginFailureWrongPassword() {
        boolean result = service.login("admin", "wrong");

        assertFalse(result);
        assertFalse(service.isLoggedIn());
    }

    @Test
    void testLoginFailureUserNotFound() {
        boolean result = service.login("notfound", "1234");

        assertFalse(result);
        assertFalse(service.isLoggedIn());
    }

    @Test
    void testLoginFailureBlankUsername() {
        boolean result = service.login("", "1234");

        assertFalse(result);
        assertFalse(service.isLoggedIn());
    }

    @Test
    void testLogout() {
        service.login("admin", "1234");
        service.logout();

        assertFalse(service.isLoggedIn());
        assertNull(service.getLoggedInAdmin());
    }
}