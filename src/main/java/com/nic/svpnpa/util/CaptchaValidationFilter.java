/**
 * 
 */
package com.nic.svpnpa.util;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.nic.svpnpa.Exceptions.BadCaptchaException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@Component
public class CaptchaValidationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (isLoginRequest(request)) {
            HttpSession session = request.getSession(false);

            if (session != null) {
                String expectedCaptcha = (String) session.getAttribute("captcha_security");
                String enteredCaptcha = obtainCaptcha(request);

                if (expectedCaptcha == null || enteredCaptcha == null || !expectedCaptcha.equalsIgnoreCase(enteredCaptcha)) {
                    // Captcha validation failed
                    redirectWithCaptchaError(request, response);
                    return;
                }
            }
        }

        filterChain.doFilter(request, response);
    }

	/*
	 * private boolean isLoginRequest(HttpServletRequest request) {
	 * 
	 * System.out.println("..."+request.getRequestURI());
	 * System.out.println("..."+request.getMethod()); return
	 * request.getRequestURI().equals("/loginSubmit") &&
	 * request.getMethod().equalsIgnoreCase("POST"); }
	 */

    private boolean isLoginRequest(HttpServletRequest request) {
        
        boolean isLoginSubmit = request.getRequestURI().equals("/loginSubmit") && request.getMethod().equalsIgnoreCase("POST");
        
         
        return isLoginSubmit;
    }
    
    private String obtainCaptcha(HttpServletRequest request) {
        return request.getParameter("captcha");
    }

    private void redirectWithCaptchaError(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect("/getsignin?badcaptcha");
    }
}
