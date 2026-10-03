/**
 * 
 */
package com.nic.svpnpa.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.nic.svpnpa.Exceptions.EmailSendingException;
import com.nic.svpnpa.dao.UserRepository;
import com.nic.svpnpa.model.CaptchaDto;
import com.nic.svpnpa.model.PasswordForm;
import com.nic.svpnpa.model.User;
import com.nic.svpnpa.security.services.UserDetailsServiceImpl;
import com.nic.svpnpa.service.UserPasswordResetService;
import com.nic.svpnpa.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * @author praka
 *
 */
@Controller
public class PasswordController {
	
	private static final Logger logger = LogManager.getLogger(PasswordController.class);

	private static final int MAX_PREVIOUS_PASSWORDS = 2;
	@Autowired
	UserRepository userRepository;

	@Autowired
	UserDetailsServiceImpl userDetails;
	
	@Autowired
	BCryptPasswordEncoder encoder;

	@Autowired
	UserService userService;
	
	@Autowired
	CaptchaController  captchaController;

	
	@Autowired
	private UserPasswordResetService passwordResetService;

	@GetMapping("/change-password")
	public String showChangePasswordForm(Model model,HttpSession session, HttpServletRequest request, HttpServletResponse response) {
		try {
	    CaptchaDto captchaDto = captchaController.generateCaptchaImage(session, request, response, model);
	    // Add the CAPTCHA string to the model
	    model.addAttribute("captchaString", captchaDto.getCaptchaString());
	    session.setAttribute("captchaString", captchaDto.getCaptchaString());
	    // Render the CAPTCHA image
	    byte[] captchaImage = captchaDto.getImage();

	    // Convert the image byte array to a Base64-encoded string
	    String base64Image = Base64.getEncoder().encodeToString(captchaImage);
	    model.addAttribute("captchaImage", base64Image);
		model.addAttribute("passwordForm", new PasswordForm());
		} catch (Exception e) {
			logger.error("Exception in PasswordController::showChangePasswordForm::" + e.getMessage());
            logger.error("Exception in PasswordController::showChangePasswordForm::" + e.getCause());
            logger.error("Exception in PasswordController::showChangePasswordForm::" + e.getClass() + "::" + e.getLocalizedMessage());
		}
		return "change-password";
	}

	@PostMapping("/change-password")
	public ModelAndView processChangePasswordForm(@Valid @ModelAttribute("passwordForm") PasswordForm passwordForm,
	                                              BindingResult result,HttpSession session,Model model, HttpServletRequest request,HttpServletResponse response) {		
	    ModelAndView modelAndView = new ModelAndView("change-password");
	    try {
	    CaptchaDto captchaDto = captchaController.generateCaptchaImage(session, request, response, model);
	    // Add the CAPTCHA string to the model
	    model.addAttribute("captchaString", captchaDto.getCaptchaString());
	    session.setAttribute("captchaString", captchaDto.getCaptchaString());
	    // Render the CAPTCHA image
	    byte[] captchaImage = captchaDto.getImage();
	    // Convert the image byte array to a Base64-encoded string
	    String base64Image = Base64.getEncoder().encodeToString(captchaImage);
	    model.addAttribute("captchaImage", base64Image);
	    if (result.hasErrors()) {
	        List<String> errorMessages = new ArrayList<>();
	        for (ObjectError error : result.getAllErrors()) {
	            errorMessages.add(error.getDefaultMessage());
	        }	       
	        // Combine the error messages into a single string
	        String errorMessage = String.join("; ", errorMessages);
	        modelAndView.addObject("errorMessages", errorMessage);
	    
	        return modelAndView;
	    }
	    
	    
		  String captcha = (String) session.getAttribute("captcha_security");		
			
			if (!passwordForm.getCaptcha().equalsIgnoreCase(captcha)) {
				
				   modelAndView.addObject("errorMessages", "Invalid Captcha!! Please Re Enter!!.");
				
			//session.setAttribute("msg", "Invalid Captcha");
				 return modelAndView;
			}
	    

	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    String currentPrincipalName = authentication.getName();
	    Optional<User> optionalUser = userRepository.findByUsername(currentPrincipalName);

	    if (optionalUser.isPresent()) {
	        String loggedInUserPasswordHash = userService.getPasswordHashByUsername(currentPrincipalName);

	        if (loggedInUserPasswordHash == null) {
	            modelAndView.addObject("errorMessages", "User not found.");
	            return modelAndView;
	        }
	        
	        
	        

	        String enteredOldPassword = passwordForm.getOldPassword();
	       // String hashedEnteredOldPassword = encoder.encode(enteredOldPassword);

	        
	        
	        if (!encoder.matches(enteredOldPassword, loggedInUserPasswordHash)) {
	            modelAndView.addObject("errorMessages", "Old password is incorrect.");
	            return modelAndView;
	        }

	        User user = optionalUser.get();
	        // Check if the old password and new password are the same
	        if (enteredOldPassword.equals(passwordForm.getNewPassword())) {
	            modelAndView.addObject("errorMessages", "Old password and new password cannot be the same.");
	            return modelAndView;
	        }

	        String encodedNewPassword = encoder.encode(passwordForm.getNewPassword());

	        
		     // Check if the new password matches any of the previous passwords
	     // Check if the new password matches any of the previous passwords
	        boolean newPasswordMatchesPrevious = false;
	        for (String previousPasswordHash : user.getPreviousPasswords()) {
	            if (encoder.matches(passwordForm.getNewPassword(), previousPasswordHash)) {
	            	newPasswordMatchesPrevious = true;
	                break;
	            }
	        }
	        
	        if (newPasswordMatchesPrevious) {
	            modelAndView.addObject("errorMessages", "Password has been used before. Please choose a different one.");
	            return modelAndView;
	        }
	        
	     // Add the current password to the list of previous passwords
	        user.getPreviousPasswords().add(loggedInUserPasswordHash);

	        
	     // Limit the number of stored previous passwords (e.g., keep the last N passwords)
	        if (user.getPreviousPasswords().size() > MAX_PREVIOUS_PASSWORDS) {
	            Iterator<String> iterator = user.getPreviousPasswords().iterator();
	            iterator.next(); // Remove the oldest password
	            iterator.remove();
	        }
	        
	        

	        user.setPassword(encodedNewPassword);
	        userRepository.save(user);

	        modelAndView.addObject("successMessage", "Password changed successfully");
	        
	        return modelAndView;
	    } else {
	        modelAndView.addObject("errorMessages", "User not found");
//	        return modelAndView;
	    }
	    } catch (Exception e) {
			logger.error("Exception in PasswordController::processChangePasswordForm::" + e.getMessage());
            logger.error("Exception in PasswordController::processChangePasswordForm::" + e.getCause());
            logger.error("Exception in PasswordController::processChangePasswordForm::" + e.getClass() + "::" + e.getLocalizedMessage());
		}
	    return modelAndView;
	}
	


	@GetMapping("/forgot-password")
	public ModelAndView showForgotPasswordForm(HttpSession session, HttpServletRequest request, HttpServletResponse response,Model model) {
		   ModelAndView modelAndView = new ModelAndView("forgot_password_form");
		CaptchaDto captchaDto = captchaController.generateCaptchaImage(session, request, response, model);
		try {
	    // Add the CAPTCHA string to the model
	    model.addAttribute("captchaString", captchaDto.getCaptchaString());
	    session.setAttribute("captchaString", captchaDto.getCaptchaString());
	    // Render the CAPTCHA image
	    byte[] captchaImage = captchaDto.getImage();

	    // Convert the image byte array to a Base64-encoded string
	    String base64Image = Base64.getEncoder().encodeToString(captchaImage);
	    model.addAttribute("captchaImage", base64Image);
	} catch (Exception e) {
		logger.error("Exception in PasswordController::showForgotPasswordForm::" + e.getMessage());
        logger.error("Exception in PasswordController::showForgotPasswordForm::" + e.getCause());
        logger.error("Exception in PasswordController::showForgotPasswordForm::" + e.getClass() + "::" + e.getLocalizedMessage());
	}
	    return modelAndView;
	}

	@PostMapping("/forgot-password")
	public String initiatePasswordReset(@RequestParam("username") String username,@RequestParam("captcha") String captcha,HttpSession session) throws Exception {
		try {
		 String scaptcha = (String) session.getAttribute("captcha_security");							
			if (!captcha.equalsIgnoreCase(scaptcha)) {
				//session.setAttribute("msg", "Invalid Captcha");
				return "redirect:/forgot-password?badcaptcha";
				}
		Optional<User> userOptional = userService.findByUsername(username);
		if (userOptional.isPresent()) {
			User user = userOptional.get();
			 try {
			        String response = passwordResetService.generatePasswordResetToken(user);
			        if (response == null || response.equals("")) {
			            return "redirect:/forgot-password?timedout";
			        } else {
			            return "redirect:/forgot-password?success";
			        }
			 } catch (EmailSendingException e) {
			        // Handle the email sending exception here
			      //  e.printStackTrace(); // Print the exception for debugging purposes
			        return "redirect:/forgot-password?emailError";
			    }  

		}else {
			return "redirect:/forgot-password?noUserFound";				
		}
		} catch (Exception e) {
			logger.error("Exception in PasswordController::initiatePasswordReset::" + e.getMessage());
	        logger.error("Exception in PasswordController::initiatePasswordReset::" + e.getCause());
	        logger.error("Exception in PasswordController::initiatePasswordReset::" + e.getClass() + "::" + e.getLocalizedMessage());
		}
		return null;
		//return "redirect:/forgot-password?error";
	}

	@GetMapping("/reset-password")
	public String showResetPasswordForm(@RequestParam("token") String token, Model model) {
		Optional<User> userOptional = userService.findByResetToken(token);
		if (userOptional.isPresent()) {
			model.addAttribute("token", token);
			return "reset_password_form";
		}
		return "redirect:/forgot-password?error";
	}

	@PostMapping("/reset-password") 
	public String resetPassword(@RequestParam("token") String token, @RequestParam("password") String newPassword,@RequestParam("confirmPassword") String confirmPassword,Model model) { 
		try {
		Optional<User> userOptional = userService.findByResetToken(token); 		
		if (userOptional.isPresent()) { 
			User user = userOptional.get(); 
			LocalDateTime tokenExpiryTime = user.getResetTokenExpiry(); 			
			if(LocalDateTime.now().isBefore(tokenExpiryTime)) { // Token is not expired  then invalidate/reset the token
  	  if (newPassword.equals(confirmPassword)) { // Update user's password and
  	   passwordResetService.resetPassword(user,encoder.encode(newPassword));   	   
  	   return "redirect:/getsignin?successpwdreset=password-reset-success";   	   
  	  } 
  	  else { //Passwords don't match, show an error message 
  		  model.addAttribute("token",token); 
  		  model.addAttribute("error", "Passwords do not match"); 
  		  return "password_reset_form"; 
  		  }
  	  
  	  }else { // Token has expired, show an error message
  	  model.addAttribute("error", "Token has expired"); 
  	  return "password_reset_form"; 
  	  } 
	}else { // Token is invalid, show an error message
  	  model.addAttribute("error", "Invalid token"); 
  	  return "password_reset_form"; 
  	}
		} catch (Exception e) {
			logger.error("Exception in PasswordController::resetPassword::" + e.getMessage());
	        logger.error("Exception in PasswordController::resetPassword::" + e.getCause());
	        logger.error("Exception in PasswordController::resetPassword::" + e.getClass() + "::" + e.getLocalizedMessage());
		}
		return null;
  	  }
	
	@PostMapping("/check-new-password")
    public ResponseEntity<?> checkNewPassword(@RequestParam String newPassword,@RequestHeader(name = "X-XSRF-TOKEN") String csrfToken,
            @RequestHeader(name = "X-XSRF-HEADER") String csrfHeader) {
		try {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        Optional<User> optionalUser = userRepository.findByUsername(currentPrincipalName);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
         // Check if the new password and old password are the same
            String oldPassword = userService.getPasswordHashByUsername(currentPrincipalName);
            
//            String encodednewPassword =  encoder.encode(newPassword);
            if (encoder.matches(newPassword, oldPassword)) {
                return ResponseEntity.ok().body(Collections.singletonMap("error", "Old password and new password cannot be the same."));
            }
            for (String previousPasswordHash : user.getPreviousPasswords()) {
                if (encoder.matches(newPassword, previousPasswordHash)) {
                    // Password matches a previous one, return an error message
                   // return ResponseEntity.badRequest().body("Password has been used before. Please choose a different one.");
                	return ResponseEntity.ok().body(Collections.singletonMap("error", "Password has been used before. Please choose a different one."));

                }
            }
        }
	} catch (Exception e) {
		logger.error("Exception in PasswordController::checkNewPassword::" + e.getMessage());
        logger.error("Exception in PasswordController::checkNewPassword::" + e.getCause());
        logger.error("Exception in PasswordController::checkNewPassword::" + e.getClass() + "::" + e.getLocalizedMessage());
	}
        // No match found, allow password change
        return ResponseEntity.ok().build();
    }







}