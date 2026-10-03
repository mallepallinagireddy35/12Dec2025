/**
 * 
 */
/*
 * package com.nic.svpnpa.controller;
 * 
 *//**
	 * @author praka
	 *
	 *//*
		 * import jakarta.servlet.http.Cookie; import
		 * jakarta.servlet.http.HttpServletResponse; import
		 * org.springframework.http.ResponseEntity; import
		 * org.springframework.web.bind.annotation.GetMapping; import
		 * org.springframework.web.bind.annotation.RestController;
		 * 
		 * @RestController public class CookieController {
		 * 
		 * @GetMapping("/setCookie") public ResponseEntity<Void>
		 * setCookie(HttpServletResponse response) { Cookie cookie = new
		 * Cookie("myCookie", "cookieValue"); cookie.setSecure(true); // Forces the
		 * cookie to be sent over HTTPS only cookie.setHttpOnly(true); // Prevents
		 * JavaScript access to the cookie cookie.setPath("/"); // Set the cookie's path
		 * if needed (e.g., to restrict it to a specific path)
		 * 
		 * // Set SameSite attribute by modifying the Set-Cookie header String
		 * sameSiteValue = "Strict"; // Set your desired SameSite value here
		 * response.setHeader("Set-Cookie", String.format("%s; %s; %s", cookie.getName()
		 * + "=" + cookie.getValue(), "SameSite=" + sameSiteValue, "Secure="+true));
		 * response.addCookie(cookie);
		 * 
		 * 
		 * return ResponseEntity.ok().build(); } }
		 */