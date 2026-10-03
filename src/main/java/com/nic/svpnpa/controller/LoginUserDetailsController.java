/**
 * 
 */
package com.nic.svpnpa.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


/**
 * @author praka
 *
 */
@Controller
public class LoginUserDetailsController {


		@GetMapping("/user-details")
		public UserDetails getUserDetails() {
	        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
	            return (UserDetails) authentication.getPrincipal();
	        } else {
	            // Handle the case when user details are not available (e.g., user not authenticated)
	            return null;
	        }
	    }
	    /*public UserDetails getUserDetails(@AuthenticationPrincipal UserDetails userDetails) {
	        return userDetails;
	    }*/
	
	
}
