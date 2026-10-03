/**
 * 
 */
package com.nic.svpnpa.Exceptions;

import org.springframework.security.core.AuthenticationException;

/**
 * @author praka
 *
 */
public class AccountLockedException extends AuthenticationException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 3177021308267333779L;

	public AccountLockedException(String message) {
        super(message);
    }
}