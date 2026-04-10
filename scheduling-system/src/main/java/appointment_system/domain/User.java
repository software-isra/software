package appointment_system.domain;


public class User {

    private final String username;
    private final String password;

    /**
     * Creates a user.
     *
     * @param username the username, must not be blank
     * @param password the password, must not be blank
     * @throws IllegalArgumentException if username or password is blank
     */
    public User(String username, String password) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username must not be blank");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password must not be blank");
        }

        this.username = username.trim();
        this.password = password;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Checks whether the provided password matches the stored password.
     *
     * @param rawPassword password to verify
     * @return true if matched, otherwise false
     */
    public boolean matchesPassword(String rawPassword) {
        return password.equals(rawPassword);
    }
}