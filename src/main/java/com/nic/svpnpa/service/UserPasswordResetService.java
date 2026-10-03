/**
 * 
 */
package com.nic.svpnpa.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.nic.svpnpa.Exceptions.EmailSendingException;
import com.nic.svpnpa.dao.UserRepository;
import com.nic.svpnpa.model.User;
import com.nic.svpnpa.util.AESEncryption;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;

/**
 * @author praka
 *
 */
@Service
public class UserPasswordResetService {
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private HttpServletRequest request;

	private static final int TOKEN_LENGTH = 32;
	private static final long TOKEN_EXPIRATION_DURATION_HOURS = 1; // 1 hour

	public String generatePasswordResetToken(User user) throws Exception {
		String token = generateRandomToken();
		LocalDateTime tokenExpiryTime = calculateExpiryTime();
		user.setResetToken(token);
		user.setResetTokenExpiry(tokenExpiryTime);
		userRepository.save(user);
		// Send email with token to user
	String response =	sendResetTokenEmail(user.getEmail(), token);

	return response;
	}

	public void resetPassword(User user, String newPassword) {
		user.setPassword(newPassword);
		user.setResetToken(null);
		user.setResetTokenExpiry(null); // Reset the expiry time
		userRepository.save(user);
	}

	private String sendResetTokenEmail(String to, String token) throws Exception {
		String response =null;
		User user = userRepository.findByEmail(to);
		if (user != null) {
			String contextPath = request.getContextPath();

			String fullUrl = getFullUrlWithContextPath(contextPath, "/reset-password?token=" + token);

//			String message = "<p><strong style=\"color: blue;\">Click this link to reset your password:</strong></p>"
//					+ "<p style=\"margin: 10px 0;\"><a href=\"" + fullUrl + "\">" + fullUrl + "</a></p>"
//					+ "<p style=\"color: red;\">Please note that this link will expire in 1 hour.</p>";

			

			RestTemplate restTemplate = new RestTemplate();
			String url = "http://localhost:8088/send_email";
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			String validate = AESEncryption.encrypt("sVPNPA-" + System.currentTimeMillis());
			String jsonString = "{\"id\":\"" + 5 + "\",\"text\":\"" + fullUrl + "\",\"validate\":\"" + validate
					+ "\"}";

			HttpEntity<String> requestEntity = new HttpEntity<>(jsonString, headers);
			try {
			ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity,
					String.class);
			
			response = responseEntity.getBody();

			}catch (Exception e) {

//				 System.out.println("Exception while sending email: " + e.getMessage());
				    throw new EmailSendingException("An error occurred while sending the email.", e);
			}

			
			
		}

		return response;
	}
	private String getFullUrlWithContextPath(String contextPath, String relativePath) {
	    String scheme = request.getScheme();
	    String serverName = request.getServerName();
	    int serverPort = request.getServerPort();

	    return scheme + "://" + serverName + ":" + serverPort + contextPath + relativePath;
	}
	private String generateRandomToken() {

		// Generating and return a unique token here
		return RandomStringUtils.randomAlphanumeric(TOKEN_LENGTH);

	}

	public static LocalDateTime calculateExpiryTime() {
		return LocalDateTime.now().plus(TOKEN_EXPIRATION_DURATION_HOURS, ChronoUnit.HOURS);
	}
}