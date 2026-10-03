/**
 * 
 */
package com.nic.svpnpa.util;

import java.io.IOException;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * @author praka
 *
 */
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

	/*
	 * @Override public void handle(HttpServletRequest request, HttpServletResponse
	 * response, AccessDeniedException accessDeniedException) throws IOException {
	 * // Set a user-friendly response
	 * 
	 * response.setStatus(HttpServletResponse.SC_FORBIDDEN); response.getWriter().
	 * write("Access Denied! Unauthorized to Access This Resource.."); }
	 */
    
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        // Set status to 403 Forbidden
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        // Forward the request to the error page
        request.getRequestDispatcher("/error/access-denied").forward(request, response);
    }
}
