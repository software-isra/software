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
 service = new AuthenticationService(repo);}

// test login success
    @Test
    void testLoginSuccess() {
 boolean result = service.login("admin", "1234");

 assertTrue(result); // expected = true
 assertTrue(service.isLoggedIn()); }

// test login failure (wrong password)
    @Test
void testLoginFailureWrongPassword() {
 boolean result = service.login("admin", "wrong");
        assertFalse(result); // expected = false
        assertFalse(service.isLoggedIn());
    }
    // test login failure (user not found)
    @Test
void testLoginFailureUserNotFound() {
 boolean result = service.login("notfound", "1234");
assertFalse(result);
assertFalse(service.isLoggedIn());}
    // Test Logout
    @Test
    void testLogout() {
service.login("admin", "1234");
  service.logout();

  assertFalse(service.isLoggedIn());
    } }