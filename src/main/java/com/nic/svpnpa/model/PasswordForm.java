/**
 * 
 */
package com.nic.svpnpa.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * @author praka
 *
 */
public class PasswordForm {

    @NotBlank
    private String oldPassword;
    
    @NotBlank
    @Size(min = 6, max = 6, message = "Invalid Captcha!! Please Re Enter!!")
    private String captcha;
    
    @NotBlank
    @Size(min = 64, max = 64, message = "Password complexities does not match!!")
    private String newPassword;
    
    @NotBlank
    private String confirmPassword;

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public String getCaptcha() {
		return captcha;
	}

	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}
    
    // Getters and setters
    
    
}