/**
 * 
 */
package com.nic.svpnpa.Exceptions;

/**
 * @author praka
 *
 */
import org.springframework.security.core.AuthenticationException;

public class BadCaptchaException extends AuthenticationException {

    public BadCaptchaException(String msg) {
        super(msg);
    }

    public BadCaptchaException(String msg, Throwable t) {
        super(msg, t);
    }
}
