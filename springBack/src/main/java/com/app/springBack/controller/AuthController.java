package com.app.springBack.controller;

import lombok.AllArgsConstructor;

import java.util.HashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.springBack.auth.*;
import com.app.springBack.service.UserService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping
public class AuthController {

    private final AuthService authService;
    private final UserService userService; 

    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginDto loginDto, HttpServletResponse response) {
        String token = authService.login(loginDto);

        Cookie cookie = new Cookie("accessToken", token);
        cookie.setHttpOnly(true); // No JavaScript => important for security
        cookie.setPath("/"); // Access cookie from any link
        cookie.setMaxAge(3600); // 1 Hour expiration
        // cookie.setSecure(true); // HTTPS

        response.addCookie(cookie);

        AuthResponseDto authResponseDto = new AuthResponseDto();
        authResponseDto.setAccessToken(token);

        return new ResponseEntity<>(authResponseDto, HttpStatus.OK);
    }

    @GetMapping("/auth/check")
    public ResponseEntity<HashMap<String, String>> checkAuthStatus() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        HashMap<String, String> response = new HashMap<>();

        // Check if authentication is null or if the user is anonymous
        if (authentication == null || !(authentication instanceof UsernamePasswordAuthenticationToken)) {
            response.put("error", "User is not logged in");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        // Handle the case where the username is 'anonymousUser'
        String username = authentication.getName();
        if ("anonymousUser".equals(username)) {
            response.put("error", "User is not logged in");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        try {
            // Try to get the userId from the database
            int userId = this.userService.getByUsername(username).getId();
            response.put("success", "User is logged in");
            response.put("userId", Integer.toString(userId));
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (UsernameNotFoundException e) {
            // Handle the case where the user doesn't exist
            response.put("error", "User not found");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
}
