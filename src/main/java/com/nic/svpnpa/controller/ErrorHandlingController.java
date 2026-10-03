/**
 * 
 */
package com.nic.svpnpa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletResponse;

/**
 * @author praka
 *
 */
@Controller
public class ErrorHandlingController {

	
	@GetMapping("/error/access-denied")
    public String showAccessDenied(Model model) {
//		int statusCode = HttpServletResponse.SC_FORBIDDEN;
		
		/* model.addAttribute("statusCode", statusCode); */
		model.addAttribute("errorMessage", "Access Denied! Unauthorized to Access This Resource..");
        return "custom_error";
    }
	
}
