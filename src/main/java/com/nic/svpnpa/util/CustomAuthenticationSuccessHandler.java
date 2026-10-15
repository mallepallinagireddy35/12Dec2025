/**
 * 
 */
package com.nic.svpnpa.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
/**
 * @author praka
 *
 */
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.nic.svpnpa.Exceptions.PreviousSessionExpireException;
import com.nic.svpnpa.Exceptions.UserAlreadyLoggedInException;
import com.nic.svpnpa.controller.AuthController;
import com.nic.svpnpa.dao.UserLogRepository;
import com.nic.svpnpa.dao.UserRepository;
import com.nic.svpnpa.model.User;
import com.nic.svpnpa.model.UserLog;
import com.nic.svpnpa.security.services.UserDetailsServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
	@Autowired
	UserRepository userRepository;
	private static final Logger logger = LogManager.getLogger(AuthController.class);

	@Autowired
	private UserLogRepository userLogRepository;

	/*
	 * @Autowired private UserLogRepository userLogRepository;
	 */
//	@Override
//	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
//			Authentication authentication) throws IOException, ServletException {
//		// Redirect the user to the URL provided in the continue parameter, or a default
//		// URL if continue is not set
//
//		String username = authentication.getName();
//
//		Optional<User> userOptional = userRepository.findByUsername(username);
//
//		if (userOptional.isPresent()) {
//			User user = userOptional.get();
//
//			Object[] userStatusInfo = getUserStatusInfo(user.getUsername());
//			if (user.isLoggedIn()) {
//				HttpSession oldSession = request.getSession(false); // Get the current session if it exists
//				if (oldSession != null) {
//					oldSession.invalidate();
//					throw new UserAlreadyLoggedInException("User is already logged in");
//				}
//
//				if (userStatusInfo.length == 1) { // Check the length of the outer array
//					Object[] innerArray = (Object[]) userStatusInfo[0]; // Cast the first element to an Object[] (inner
//																		// array)
//					if (innerArray.length == 3 && innerArray[2] instanceof Boolean) {
//						boolean lastLoginWithin30Minutes = (Boolean) innerArray[2];
//						if (!lastLoginWithin30Minutes) {
//							// Updating the loggedIn attribute to false
//							user.setLoggedIn(false);
////				            user.setFailed_login_attempts(0);
//							userRepository.save(user);
//
//							throw new PreviousSessionExpireException(
//									"You have not logged out in your Previous Session");
//						}
//					}
//				}
//		
//				// Invalidate the existing session }
//
//
//			}
//
//			user.setFailed_login_attempts(0);
//			user.setLocked(false);
//			user.setLoggedIn(true);
//			userRepository.save(user);
//
//			UserLog loginLog = new UserLog();
//			loginLog.setUser(findUserByUsername(username)); // Implement this method to find the user
//			loginLog.setAction("login");
//			loginLog.setTimestamp(LocalDateTime.now());
//			userLogRepository.save(loginLog);
//
//		}
//
//		response.sendRedirect("/");
//
//	}

	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
	        Authentication authentication) throws IOException, ServletException {
	    String username = authentication.getName();

	    Optional<User> userOptional = userRepository.findByUsername(username.trim());
	    System.out.println("username==="+username);
		System.out.println("userOptional===="+userOptional.isEmpty());
	    if (userOptional.isPresent()) {
	        User user = userOptional.get();

	        if (user.isLoggedIn()) {
	        	HttpSession oldSession = request.getSession(false);
	     // Check if the last login was more than 30 minutes ago
     	   Object[] userStatusInfo = getUserStatusInfo(user.getUsername());
         if (userStatusInfo.length == 1) {
             Object[] innerArray = (Object[]) userStatusInfo[0];
             if (innerArray.length == 3 && innerArray[2] instanceof Boolean) {
                 boolean lastLoginWithin5Minutes = (Boolean) innerArray[2];
                 if (!lastLoginWithin5Minutes) {
                     // Last login was more than 30 minutes ago, invalidate the session
                     // Set the loggedIn attribute to false and ask the user to relogin
                     user.setLoggedIn(false);
                     userRepository.save(user);
                     if (oldSession != null) {
                         oldSession.invalidate();
                     }
                     throw new PreviousSessionExpireException("You have not properly logged out in your Previous Session");
                 } else {
           	      
     	        	
     	        	
    	        	 
                     if (oldSession != null) {
                         oldSession.invalidate();
                         throw new UserAlreadyLoggedInException("User Already Logged In with a different Browser/Device....!!");
                     }
    	            
    	        }
             }
         }
        
	        }
	        // Continue with the login process as before
	        user.setFailed_login_attempts(0);
	        user.setLocked(false);
	        user.setLoggedIn(true);
	        userRepository.save(user);

	        UserLog loginLog = new UserLog();
	        loginLog.setUser(findUserByUsername(username));
	        loginLog.setAction("login");
	        loginLog.setTimestamp(LocalDateTime.now());
	        userLogRepository.save(loginLog);
	    }

	    response.sendRedirect("/home");
	}

	private User findUserByUsername(String username) {
		Optional<User> userOptional = userRepository.findByUsername(username.trim());
		return userOptional.orElse(null); // Return null if user not found
	}

	public Object[] getUserStatusInfo(String username) {
		LocalDateTime cutoffTime = LocalDateTime.now().minusMinutes(5);
		Object[] userStatusInfo = userRepository.findUserStatusInfo(username, cutoffTime);
		// Add logging to see the values
		//logger.info("UserStatusInfo for {}: {}", username, Arrays.toString(userStatusInfo));
		return userStatusInfo;
	}

}
