package com.app.springBack.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.app.springBack.repository.UserRepository;

@Service
public class UserSecurityService {

    private UserRepository userRepository;

    public UserSecurityService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * 
     * @param authentication the authentication context
     * @param id the Id to compare with the User in the Authentication context
     * @return
     */
    public boolean isLoggedUser(Authentication authentication, Long id) {
        // Retrieve the logged-in user's User object
        org.springframework.security.core.userdetails.User userDetails = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();

        // Assuming the username is unique, fetch the full User entity from the database
        int loggedInUserId = userRepository.findByUsername(userDetails.getUsername())
                                            .orElseThrow(() -> new UsernameNotFoundException("User not found"))
                                            .getId();
    
        // Check if the logged-in user is the same as the requested user ID or if the user is an admin
        return loggedInUserId == id ;
    }
}

