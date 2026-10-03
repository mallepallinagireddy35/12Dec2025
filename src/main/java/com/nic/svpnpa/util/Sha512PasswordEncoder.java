/**
 * 
 */
package com.nic.svpnpa.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
/**
 * @author praka
 *
 */
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Sha512PasswordEncoder implements PasswordEncoder {
	
	private static final Logger logger = LogManager.getLogger(Sha512PasswordEncoder.class);

  @Override
  public String encode(CharSequence rawPassword) {
    return hashWithSHA512(rawPassword.toString());
  }

  @Override
  public boolean matches(
      CharSequence rawPassword, String encodedPassword) {
    String hashedPassword = encode(rawPassword);
    return encodedPassword.equals(hashedPassword);
  }

  private String hashWithSHA512(String input) {
    StringBuilder result = new StringBuilder();
    try {
      MessageDigest md = MessageDigest.getInstance("SHA-512");
      byte [] digested = md.digest(input.getBytes());
      for (int i = 0; i < digested.length; i++) {
        result.append(Integer.toHexString(0xFF & digested[i]));
      }
    } catch (NoSuchAlgorithmException e) {
    	logger.error("Exception in Sha512PasswordEncoder::hashWithSHA512::" + e.getMessage());
        logger.error("Exception in Sha512PasswordEncoder::hashWithSHA512::" + e.getCause());
        logger.error("Exception in Sha512PasswordEncoder::hashWithSHA512::" + e.getClass() + "::" + e.getLocalizedMessage());
      throw new RuntimeException("Bad algorithm");
    }
    return result.toString();
  }
}