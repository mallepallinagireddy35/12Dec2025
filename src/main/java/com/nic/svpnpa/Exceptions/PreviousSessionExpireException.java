/**
 * 
 */
package com.nic.svpnpa.Exceptions;

import org.springframework.security.core.AuthenticationException;

/**
 * @author praka
 *
 */
public class PreviousSessionExpireException extends AuthenticationException {

	public PreviousSessionExpireException(String message) {
        super(message);
    }
	
}
