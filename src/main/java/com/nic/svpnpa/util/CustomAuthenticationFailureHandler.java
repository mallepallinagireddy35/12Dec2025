/**
 * 
 */
package com.nic.svpnpa.util;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nic.svpnpa.Exceptions.AccountLockedException;
import com.nic.svpnpa.Exceptions.BadCaptchaException;
import com.nic.svpnpa.Exceptions.PreviousSessionExpireException;
import com.nic.svpnpa.Exceptions.SessionExpiredException;
import com.nic.svpnpa.Exceptions.UserAlreadyLoggedInException;
import com.nic.svpnpa.dao.UserRepository;
import com.nic.svpnpa.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * @author praka
 *
 */
@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

	private ObjectMapper objectMapper = new ObjectMapper();
	
	@Autowired
	UserRepository userRepository;

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {

		// custom logic here
		response.setStatus(HttpStatus.UNAUTHORIZED.value());

		Map<String, Object> data = new HashMap<>();
		data.put("timestamp", Calendar.getInstance().getTime());
		data.put("exception", exception.getMessage());
		response.getOutputStream().println(objectMapper.writeValueAsString(data));
		
		String username = request.getParameter("username"); // Retrieve the username parameter
		Optional<User> userOptional = userRepository.findByUsername(username.trim());
	System.out.println("username==="+username);
	System.out.println("userOptional===="+userOptional.isPresent());
	System.out.println("exception.getMessage==="+exception.getMessage());

		if (userOptional.isPresent()) {
			
			User user = userOptional.get();		
			System.out.println("======"+user.getEmail());
			 int newFailedAttempts = user.getFailed_login_attempts() + 1;
	            user.setFailed_login_attempts(newFailedAttempts);
	            user.setLastLoginAttempt(LocalDateTime.now());

	            if (newFailedAttempts >= 3) {
	                user.setLocked(true);
	            	}
	            //	user.setLocked(false);
	            	userRepository.save(user);		
				}

		

		 if (exception instanceof InternalAuthenticationServiceException) {
	            Throwable cause = exception.getCause();
	            
	            if (cause instanceof AccountLockedException) {
	                response.sendRedirect("/getsignin?locked");
	            } else {
	                response.sendRedirect("/getsignin?error=unknown");
	            }
		 }
		 else if(exception instanceof PreviousSessionExpireException) {
			 response.sendRedirect("/getsignin?userLogExists");
		 }		 
		 else if(exception instanceof UserAlreadyLoggedInException) {
			 response.sendRedirect("/getsignin?userExists");
		 }else if (exception instanceof BadCaptchaException) {
			 
			 response.sendRedirect("/getsignin?badcaptcha");
		 }
	    else if (exception instanceof BadCredentialsException) {
//			response.sendRedirect("/getsignin?error=bad_credentials");
//-------------------local-------------------------------------------------------------------------------
	    	response.sendRedirect(request.getScheme() + "://" + request.getServerName() + ":"+ request.getServerPort() + "/getsignin?error=bad_credentials");
//--------------------Production--------------------------------------------------------------------------	
//	    	response.sendRedirect(request.getScheme() + "://" + request.getServerName() +  "/getsignin?error=bad_credentials");
////	    	response.sendRedirect("https://www.svpnpa.gov.in/getsignin?error=bad_credentials");

	    } else if (exception instanceof AccountLockedException) {
			response.sendRedirect("/getsignin?locked");
		} else if (exception instanceof DisabledException) {
			response.sendRedirect("/getsignin?error=disabled");
		} else if (exception instanceof AccountExpiredException) {
			response.sendRedirect("/getsignin?error=expired");
		} else {
			response.sendRedirect("/getsignin?error=unknown");
		}

		/*
		 * response.sendRedirect("/login?error=invalid");
		 * request.getRequestDispatcher("/signin?error=true").forward(request,
		 * response);
		 */
//        response.sendRedirect("/signin?error=true");
	}
}
