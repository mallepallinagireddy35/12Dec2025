/**
 * 
 */
package com.nic.svpnpa.util;

import java.io.IOException;
import java.util.Collection;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * @author praka
 *
 */

@WebFilter(urlPatterns = "/*")
public class SameSiteCookieFilter implements Filter {
	
	
	 @Override
	    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
	            throws IOException, ServletException {
	        if (response instanceof HttpServletResponse) {
	            HttpServletRequest httpRequest = (HttpServletRequest) request;
	            HttpServletResponse httpResponse = (HttpServletResponse) response;
	            // Get the request URI
	            
	                      // Your logic to filter GET and POST requests
	                // Example: Set secure and HttpOnly flags for cookies
	                Cookie[] cookies = httpRequest.getCookies();
	                if (cookies != null) {
	                    for (Cookie cookie : cookies) {
	                        cookie.setSecure(true); // Forces the cookie to be sent over HTTPS only
	                        cookie.setHttpOnly(true); // Prevents JavaScript access to the cookie
	                        httpResponse.addCookie(cookie);
	                    }
	                }

	                // Add SameSite=Strict attribute to cookies
	                addSameSiteAttribute(httpResponse);
	                chain.doFilter(request, response);
	    
	        }
	        }
    private void addSameSiteAttribute(HttpServletResponse response) {
        Collection<String> headers = response.getHeaders("Set-Cookie");
        boolean firstHeader = true;
        for (String header : headers) {  
            if (firstHeader) {
                response.setHeader("Set-Cookie", String.format("%s; %s", header, "SameSite=Strict"));
                firstHeader = false;
                continue;
            }
            response.addHeader("Set-Cookie", String.format("%s; %s", header, "SameSite=Strict"));
        }
    }

   
}