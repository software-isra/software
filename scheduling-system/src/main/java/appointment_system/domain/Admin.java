package appointment_system.domain;

/**
 * Represents an administrator in the appointment scheduling system.
 
 */
public class Admin extends User {

    /**
     * Creates an administrator account.
     *
     * @param username admin username
     * @param password admin password
     */
    public Admin(String username, String password) {
        super(username, password);
    }
}