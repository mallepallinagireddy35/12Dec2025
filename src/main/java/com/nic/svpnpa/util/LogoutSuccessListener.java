
package com.nic.svpnpa.util;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.LogoutSuccessEvent;
import org.springframework.stereotype.Component;

import com.nic.svpnpa.dao.UserLogRepository;
import com.nic.svpnpa.dao.UserRepository;
import com.nic.svpnpa.model.User;
import com.nic.svpnpa.model.UserLog;

/**
 * @author praka
 *
 */
@Component
public class LogoutSuccessListener implements ApplicationListener<LogoutSuccessEvent> {

	@Autowired
	private UserLogRepository userLogRepository;

	@Autowired
	private UserRepository userRepository;

	@Override
	public void onApplicationEvent(LogoutSuccessEvent event) {
		String username = event.getAuthentication().getName();
		User user = findUserByUsername(username);

		if (user != null) {
			UserLog logoutLog = new UserLog();
			logoutLog.setUser(user);
			logoutLog.setAction("logout");
			logoutLog.setTimestamp(LocalDateTime.now());
			userLogRepository.save(logoutLog);
		}
	}

	private User findUserByUsername(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        return userOptional.orElse(null);  //Return null if user not found
    }
}
