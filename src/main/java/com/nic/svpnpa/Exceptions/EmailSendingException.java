/**
 * 
 */
package com.nic.svpnpa.Exceptions;

/**
 * @author praka
 *
 */
public class EmailSendingException extends RuntimeException {
    public EmailSendingException(String message, Throwable cause) {
        super(message, cause);
    }
}
