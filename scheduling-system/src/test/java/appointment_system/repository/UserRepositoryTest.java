package appointment_system.repository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import appointment_system.domain.Admin;
import appointment_system.domain.User;

class UserRepositoryTest {

    @Test
    void testFindAdminByUsername() {
        UserRepository repo = new UserRepository();

        Admin admin = repo.findAdminByUsername("admin");

        assertNotNull(admin);
        assertEquals("admin", admin.getUsername());
    }

    @Test
    void testFindUserByUsernameReturnsNullWhenMissing() {
        UserRepository repo = new UserRepository();

        User user = repo.findUserByUsername("missing");

        assertNull(user);
    }

    @Test
    void testAddUserAndFindIt() {
        UserRepository repo = new UserRepository();
        User user = new User("sami", "1234");

        repo.addUser(user);

        User found = repo.findUserByUsername("sami");

        assertNotNull(found);
        assertEquals("sami", found.getUsername());
        assertEquals("1234", found.getPassword());
    }
}
