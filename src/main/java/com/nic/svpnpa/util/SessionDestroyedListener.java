/**
 * 
 */
package com.nic.svpnpa.util;

import java.time.LocalDateTime;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.session.HttpSessionDestroyedEvent;
import org.springframework.stereotype.Component;

import com.nic.svpnpa.Exceptions.SessionExpiredException;
import com.nic.svpnpa.controller.AuthController;
import com.nic.svpnpa.dao.UserLogRepository;
import com.nic.svpnpa.dao.UserRepository;
import com.nic.svpnpa.model.User;
import com.nic.svpnpa.model.UserLog;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * @author praka
 *
 */
@Component
public class SessionDestroyedListener implements ApplicationListener<HttpSessionDestroyedEvent> {
	
	private static final Logger logger = LogManager.getLogger(SessionDestroyedListener.class);
    @Autowired
    private UserLogRepository userLogRepository;
    @Autowired 
    private UserRepository userRepository;
    
    @Autowired 
    private HttpServletResponse response;
    
    @Autowired 
    private HttpServletRequest request;
    

    @Override
    public void onApplicationEvent(HttpSessionDestroyedEvent event) {
    	
    	 // Check if the session is null or if the "username" attribute is not set
        HttpSession session = event.getSession();
        if (session == null || session.getAttribute("username") == null) {
            return; // Skip listener logic
        }
        String username = getUsernameFromSession(event.getSession());
        
        // Check if the user is still logged in (session attribute present)
        if (username != null) {
            User user = findUserByUsername(username);
            
            // Check if the user is already logged out (additional check)
            if (user != null && user.isLoggedIn()) {
                // Extract the username or user details from the session if needed
                // Create a UserLog entry for session expiration

                UserLog logEntry = new UserLog();
                logEntry.setUser(user); // Set the user or user details
                logEntry.setAction("session_expired");
                logEntry.setTimestamp(LocalDateTime.now());
                userLogRepository.save(logEntry);

                // Reset user attributes when the session expires
                user.setFailed_login_attempts(0);
                user.setLocked(false);
                user.setLoggedIn(false);
                userRepository.save(user);
                try {
                    throw new SessionExpiredException("Session expired");
                } catch (SessionExpiredException e) {
                    // Log the exception or handle it as needed
                	 logger.error("Session expired for user: " + username, e); 
                }
                               
            }
        }
       
    }

    private User findUserByUsername(String username) {
        Optional<User> userOptional = userRepository.findByUsernameAndStatus(username.trim());
        return userOptional.orElse(null); // Return null if user not found
    }

    private String getUsernameFromSession(HttpSession session) {
        // Retrieve the username attribute from the session
        return (String) session.getAttribute("username");
    }
}
