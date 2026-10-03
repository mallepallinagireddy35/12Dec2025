/**
 * 
 */
package com.nic.svpnpa.Exceptions;

/**
 * @author praka
 *
 */
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SessionExpiredException.class)
    public ModelAndView handleSessionExpiredException(SessionExpiredException ex,
                                                      HttpServletRequest request,
                                                      HttpServletResponse response) {
        // Log the exception and request/response details for debugging
        // ... (other request/response details)

        // Perform the redirection to the desired URL
        ModelAndView modelAndView = new ModelAndView("redirect:/getsignin?sessionExpired");
        return modelAndView;
    }
    
    
    @ExceptionHandler(UserAlreadyLoggedInException.class)
    public ModelAndView handleUserAlreadyLoggedInException(UserAlreadyLoggedInException ex,
                                                      HttpServletRequest request,
                                                      HttpServletResponse response) {
        // Log the exception and request/response details for debugging
        // ... (other request/response details)

        // Perform the redirection to the desired URL
        ModelAndView modelAndView = new ModelAndView("redirect:/getsignin?userExists");
        return modelAndView;
    }
}
