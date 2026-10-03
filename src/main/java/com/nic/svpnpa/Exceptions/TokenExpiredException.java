/**
 * 
 */
package com.nic.svpnpa.Exceptions;

/**
 * @author praka
 *
 */
public class TokenExpiredException extends RuntimeException {

    public TokenExpiredException(String message) {
        super(message);
    }
}
