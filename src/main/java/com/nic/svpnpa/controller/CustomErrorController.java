/**
 * 
 */
package com.nic.svpnpa.controller;

/**
 * @author praka
 *
 */
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.HttpClientErrorException.Unauthorized;

@Controller
public class CustomErrorController {

    @Autowired
    private ErrorAttributes errorAttributes;
    
    private static final Logger logger = LogManager.getLogger(CustomErrorController.class);

    @GetMapping("/customerror")
    public String handleError(HttpServletRequest request, Model model) {
    	try {
    	Integer statusCode =0;
    	String errorMessage ="";        
        if (request != null && !request.getParameterMap().isEmpty()) {
        // Get the error status code
        statusCode = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (statusCode == null) {
            statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
        }

        // Set appropriate error message based on the status code
        
        if (statusCode == HttpStatus.NOT_FOUND.value()) {
            errorMessage = "Page not found";
        } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
            errorMessage = "Access is denied";
        } else {
            // Get the exception attribute from the request
            Throwable exception = (Throwable) request.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);

            // Set appropriate error message based on the exception
            if (exception instanceof BadCredentialsException) {
                errorMessage = "Bad Credentials";
            } else if (exception instanceof InsufficientAuthenticationException) {
                errorMessage = "Access is denied";
            } else if (exception instanceof Unauthorized){
                errorMessage = "You need to login to access this resource with proper Credentials.";
            }else {
            	errorMessage = "An error occurred";            	
            }
        }
        model.addAttribute("requestPath", request.getRequestURI());		
        }else{
        	

        	statusCode = (Integer) request.getAttribute("errorCode");
            errorMessage = (String) request.getAttribute("errorMessage");
            String requestPath = (String) request.getAttribute("requestPath");
            model.addAttribute("requestPath", requestPath);		
        }
//        model.addAttribute("statusCode", statusCode);
        model.addAttribute("errorMessage", errorMessage);
        request.setAttribute("statusCode", statusCode);


        // Add other custom attributes to the model as needed
        model.addAttribute("customAttribute", "This is a custom attribute for the error page.");
        clearAuthenticationError(request.getSession());
    	}catch(Exception e) {
    		logger.error("Exception in CustomErrorController::handleError::" + e.getMessage());
            logger.error("Exception in CustomErrorController::handleError::" + e.getCause());
            logger.error("Exception in CustomErrorController::handleError::" + e.getClass() + "::" + e.getLocalizedMessage());
				
    	}
        return "custom_error"; // error.html is the name of your error page template
    }
    // Helper method to clear authentication error from the session
    private void clearAuthenticationError(HttpSession session) {
        if (session != null) {
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }
    }
}