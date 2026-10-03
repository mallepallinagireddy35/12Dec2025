/**
 * 
 */
package com.nic.svpnpa.model;

/**
 * @author praka
 *
 */
public class CaptchaDto {
    private byte[] image;
    private String captchaString;
	public byte[] getImage() {
		return image;
	}
	public void setImage(byte[] image) {
		this.image = image;
	}
	public String getCaptchaString() {
		return captchaString;
	}
	public void setCaptchaString(String captchaString) {
		this.captchaString = captchaString;
	}

    // Constructors, getters, and setters
    
    
    
}