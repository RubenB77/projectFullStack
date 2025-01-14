package com.app.springBack.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.app.springBack.model.UserDetailsImpl;

@Service
public class AuthServiceUser {

    /**
     *  Function to check if the userId in the url is the same as the logged user (in the authentication context)
     * @param userId The userId from the url
     * @return boolean true if the userId corresponds to the logged user false otherwise
     */
    public boolean isRightUser(int userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetailsImpl)) {
            return false;
        }
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userId == userDetails.getUserId();
    }
    
    
}
