/**
 * 
 */
package com.nic.svpnpa.Exceptions;

import org.springframework.security.core.AuthenticationException;

/**
 * @author praka
 *
 */
public class UserAlreadyLoggedInException extends AuthenticationException {
    public UserAlreadyLoggedInException(String message) {
        super(message);
    }
}