package appointment_system.service;
import appointment_system.repository.UserRepository;

import appointment_system.domain.Admin;
public class AuthenticationService {

	

	    private UserRepository repository;
	    private Admin loggedInAdmin;

	    public AuthenticationService(UserRepository repository) {
	        this.repository = repository;
	    }

	    public boolean login(String username, String password) {

	        Admin admin = repository.findByUsername(username);

	        if (admin != null && admin.getPassword().equals(password)) {
	            loggedInAdmin = admin;
	            return true;
	        }

	        return false;
	    }

	    public void logout() {
	        loggedInAdmin = null;
	    }

	    public boolean isLoggedIn() {
	        return loggedInAdmin != null;
	    }
	}


