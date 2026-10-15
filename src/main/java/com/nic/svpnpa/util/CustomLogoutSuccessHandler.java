/**
 * 
 */
package com.nic.svpnpa.util;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;

import com.nic.svpnpa.dao.UserLogRepository;
import com.nic.svpnpa.dao.UserRepository;
import com.nic.svpnpa.model.User;
import com.nic.svpnpa.model.UserLog;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * @author praka
 *
 */
@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

	@Autowired
	private UserLogRepository userLogRepository;

	@Autowired
	private UserRepository userRepository;

	SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();

	
	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
        request.getSession().invalidate();

		// Add logout logging logic here

		if (authentication != null) {
			String username = authentication.getName();

			
			Optional<User> userOptional = userRepository.findByUsername(username.trim());
			
			if(userOptional.isPresent()) {
				User user = userOptional.get();
				if (user != null) {
					UserLog logoutLog = new UserLog();
					logoutLog.setUser(user);
					logoutLog.setAction("logout");
					logoutLog.setTimestamp(LocalDateTime.now());
					userLogRepository.save(logoutLog);
					user.setFailed_login_attempts(0);
					user.setLocked(false);
					user.setLoggedIn(false);
				
					userRepository.save(user);
				}				
			}

		}
		this.logoutHandler.logout(request, response, authentication);
		response.sendRedirect("/getsignin?logout");
	}

	
}