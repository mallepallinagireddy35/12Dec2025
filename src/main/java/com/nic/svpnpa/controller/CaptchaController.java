package com.nic.svpnpa.controller;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nic.svpnpa.model.CaptchaDto;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author NPA
 */

@Controller
@RequestMapping("/captcha")
public class CaptchaController {

	private static final Logger logger = LogManager.getLogger(CaptchaController.class);

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public CaptchaDto generateCaptchaImage(HttpSession session, HttpServletRequest request,
			HttpServletResponse response, Model model) {
		CaptchaDto captchaDto = new CaptchaDto();
		try {
			// Your CAPTCHA image generation code here
			response.setContentType("image/jpg");
			int iTotalChars = 1; // 6
			int iHeight = 40;
			int iWidth = 165;
			Font fntStyle1 = new Font("Arial", Font.BOLD, 25);
			SecureRandom randChars = new SecureRandom();
			// String sImageCode = (Long.toString(Math.abs(randChars.nextLong()),
			// 36)).substring(0, iTotalChars);
			String sImageCode = "a";
			BufferedImage biImage = new BufferedImage(iWidth, iHeight, BufferedImage.TYPE_INT_RGB);
			Graphics2D g2dImage = (Graphics2D) biImage.getGraphics();
			int iCircle = 15;
			for (int i = 0; i < iCircle; i++) {
				Color color = new Color(255, 204, 102);
				Color color1 = new Color(255, 255, 204);
				GradientPaint gp = new GradientPaint(1, 0, color, 0, 1, color1, true);
				g2dImage.setPaint(gp);
				g2dImage.fillRect(0, 0, iWidth, iHeight);

				g2dImage.setColor(new Color(44, 44, 202));
				// g2dImage.setColor(new Color(randChars.nextInt(255), randChars.nextInt(255),
				// randChars.nextInt(255)));
			}
			g2dImage.setFont(fntStyle1);
			for (int i = 0; i < iTotalChars; i++) {
				// g2dImage.setColor(new Color(randChars.nextInt(255), randChars.nextInt(255),
				// randChars.nextInt(255)));
				if (i % 2 == 0) {
					g2dImage.drawString(sImageCode.toUpperCase().substring(i, i + 1), 25 * i, 24);
				} else {
					g2dImage.drawString(sImageCode.toUpperCase().substring(i, i + 1), 25 * i, 35);
				}
			}

			// Store the CAPTCHA value in the session
			session.setAttribute("captcha_security", sImageCode.toUpperCase());

			// Convert the BufferedImage to a byte array
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			try {
				ImageIO.write(biImage, "jpeg", baos);
				baos.flush();
			} catch (IOException e) {
			}

			captchaDto.setImage(baos.toByteArray());
			captchaDto.setCaptchaString(sImageCode.toUpperCase());

			// Convert the image byte array to a Base64-encoded string
			String base64Image = Base64.getEncoder().encodeToString(captchaDto.getImage());

			// Set the CAPTCHA image as a model attribute
			model.addAttribute("captchaImage", "data:image/jpeg;base64," + base64Image);
		} catch (Exception e) {
			logger.error("Exception in AdminController::generateCaptchaImage::" + e.getMessage());
			logger.error("Exception in AdminController::generateCaptchaImage::" + e.getCause());
			logger.error("Exception in AdminController::generateCaptchaImage::" + e.getClass() + "::"
					+ e.getLocalizedMessage());
		}
		return captchaDto;
	}

	@GetMapping("/getCaptcha")
	@ResponseBody
	public ResponseEntity<?> getCaptcha(HttpSession session, @RequestHeader(name = "X-XSRF-TOKEN") String csrfToken,
			@RequestHeader(name = "X-XSRF-HEADER") String csrfHeader) {
		Map<String, String> response = new HashMap<>();
		try {
			String captchaSession = (String) session.getAttribute("captcha_security");

			response.put("success", "success");
			response.put("captchaSession", captchaSession);
		} catch (Exception e) {
			logger.error("Exception in AdminController::getCaptcha::" + e.getMessage());
			logger.error("Exception in AdminController::getCaptcha::" + e.getCause());
			logger.error("Exception in AdminController::getCaptcha::" + e.getClass() + "::" + e.getLocalizedMessage());
		}
		return ResponseEntity.ok(response);
	}
}

/*
 * @Controller
 * 
 * @RequestMapping("/captcha") public class CaptchaController {
 * 
 * @RequestMapping(method = RequestMethod.GET) public void
 * index(HttpServletRequest request, HttpServletResponse response,HttpSession
 * session) throws ServletException, IOException {
 * 
 * 
 * try { session.removeAttribute("captcha_security"); }catch(Exception e) {}
 * response.setContentType("image/jpg"); int iTotalChars = 6; int iHeight = 40;
 * int iWidth = 165; Font fntStyle1 = new Font("Arial", Font.BOLD, 25);
 * SecureRandom randChars = new SecureRandom (); String sImageCode =
 * (Long.toString(Math.abs(randChars.nextLong()), 36)).substring(0,
 * iTotalChars); BufferedImage biImage = new BufferedImage(iWidth, iHeight,
 * BufferedImage.TYPE_INT_RGB); Graphics2D g2dImage = (Graphics2D)
 * biImage.getGraphics(); int iCircle = 15; for (int i = 0; i < iCircle; i++) {
 * Color color = new Color(255, 204, 102); Color color1 = new Color(255, 255,
 * 204); GradientPaint gp = new GradientPaint(1, 0, color, 0, 1, color1, true);
 * g2dImage.setPaint(gp); g2dImage.fillRect(0, 0, iWidth, iHeight);
 * 
 * g2dImage.setColor(new Color(44, 44, 202)); //g2dImage.setColor(new
 * Color(randChars.nextInt(255), randChars.nextInt(255),
 * randChars.nextInt(255))); } g2dImage.setFont(fntStyle1); for (int i = 0; i <
 * iTotalChars; i++) { //g2dImage.setColor(new Color(randChars.nextInt(255),
 * randChars.nextInt(255), randChars.nextInt(255))); if (i % 2 == 0) {
 * g2dImage.drawString(sImageCode.toUpperCase().substring(i, i + 1), 25 * i,
 * 24); } else { g2dImage.drawString(sImageCode.toUpperCase().substring(i, i +
 * 1), 25 * i, 35); } } session.setAttribute("captcha_security",
 * sImageCode.toUpperCase()); //
 * System.out.println("captcha_security=Servlet==="+session.getAttribute(
 * "captcha_security").toString()); OutputStream osImage =
 * response.getOutputStream(); ImageIO.write(biImage, "jpeg", osImage);
 * g2dImage.dispose();
 * 
 * 
 * }
 * 
 * 
 * 
 * 
 * }
 */