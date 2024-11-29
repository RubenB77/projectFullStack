package com.app.springBack.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private JwtTokenProvider jwtTokenProvider;
    private UserDetailsService userDetailsService;

    //Constructor
    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, UserDetailsService userDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
    }


    // This method is executed for every request intercepted by the filter.
    // And, it extract the token from the request header and validate the token.
    @Override
    protected void doFilterInternal(@SuppressWarnings("null") HttpServletRequest request,
                                    @SuppressWarnings("null") HttpServletResponse response,
                                    @SuppressWarnings("null") FilterChain filterChain) throws ServletException, IOException {

        if (request.getRequestURI().startsWith("/register")) {
            filterChain.doFilter(request, response); // Proceed without filtering
            return;
        }

        // Get JWT token from HTTP request
        String token = getTokenFromRequest(request);

        // Validate Token
        if(StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)){
            // get username from token
            String username = jwtTokenProvider.getUsername(token);

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
            );

            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        filterChain.doFilter(request, response);
    }

    // Extract the token
    // private String getTokenFromRequest(HttpServletRequest request){
    //     String bearerToken = request.getHeader("Authorization");
    //     if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")){
    //         return bearerToken.substring(7, bearerToken.length());
    //     }

    //     return null;
    // }

    private String getTokenFromRequest(HttpServletRequest request) {
        // // Check if the token is in the Authorization header (for Bearer token)
        // String bearerToken = request.getHeader("Authorization");
        // if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
        //     return bearerToken.substring(7);  // Extract token from "Bearer <token>"
        // }
    
        // If token is not in Authorization header, check the cookies
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("accessToken".equals(cookie.getName())) {
                    return cookie.getValue();  // Return token from the accessToken cookie
                }
            }
        }
    
        // If no token found, return null
        return null;
    }
    
}
