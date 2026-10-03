///**
// * 
// */
//package com.nic.svpnpa.controller;
//
//import org.springframework.security.web.csrf.CsrfToken;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ModelAttribute;
//
//import jakarta.servlet.http.HttpServletResponse;
//
///**
// * @author praka
// *
// */
//@ControllerAdvice
//public class CsrfControllerAdvice {
//
//	@ModelAttribute
//	public void getCsrfToken(HttpServletResponse response, CsrfToken csrfToken) {
//		response.setHeader(csrfToken.getHeaderName(), csrfToken.getToken());
//	}
//
//}
//
//
