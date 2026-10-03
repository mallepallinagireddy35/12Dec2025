/**
 * 
 */
package com.nic.svpnpa.Exceptions;

/**
 * @author praka
 *
 */
public class SessionExpiredException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SessionExpiredException(String msg) {
        super(msg);
    }
}
