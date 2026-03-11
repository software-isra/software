package appointment_system.repository;

import java.util.HashMap;
import java.util.Map;

import appointment_system.domain.Admin;

public class UserRepository {

    private Map<String, Admin> admins = new HashMap<>();

    public UserRepository() {

        admins.put("admin", new Admin("admin", "1234"));
    }

    public Admin findByUsername(String username) {

        return admins.get(username);
    }
}