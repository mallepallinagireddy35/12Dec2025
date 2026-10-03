package com.nic.svpnpa.security.jwt;

import java.io.IOException;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.FlashMapManager;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;

@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

	private static final Logger logger = LogManager.getLogger(AuthEntryPointJwt.class);

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		logger.error("Unauthorized error: {}", authException.getMessage());
	
//		Model model = (Model) request.getAttribute("org.springframework.web.servlet.View.pathVariables");
		
		
		/*
		 * response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		 * response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		 * 
		 * final Map<String, Object> body = new HashMap<>(); body.put("status",
		 * HttpServletResponse.SC_UNAUTHORIZED); body.put("error", "Unauthorized");
		 * body.put("message", authException.getMessage()); body.put("path",
		 * request.getServletPath());
		 * 
		 * final ObjectMapper mapper = new ObjectMapper();
		 * mapper.writeValue(response.getOutputStream(), body);
		 */
			ModelAndView modelAndView = new ModelAndView();
	        modelAndView.addObject("errorCode", HttpServletResponse.SC_UNAUTHORIZED);
	        modelAndView.addObject("errorMessage", "You need to login to access this resource with proper Credentials.");
	        modelAndView.addObject("requestPath", request.getServletPath());
		
	        FlashMap flashMap = new FlashMap();
	        flashMap.put("errorCode", HttpServletResponse.SC_UNAUTHORIZED);
	        flashMap.put("errorMessage", "You need to login to access this resource.");
	        flashMap.put("requestPath", request.getRequestURI());

	        // Save the flash map
	        FlashMapManager flashMapManager = RequestContextUtils.getFlashMapManager(request);
	        if (flashMapManager != null) {
	            flashMapManager.saveOutputFlashMap(flashMap, request, response);
	        }
	        
	        request.setAttribute("errorCode", HttpServletResponse.SC_UNAUTHORIZED);
	        request.setAttribute("errorMessage", "You need to login to access this resource with proper Credentials.");
	        request.setAttribute("requestPath", request.getRequestURI());



	        // Forward to the redirected page
	        request.getRequestDispatcher("/customerror").forward(request, response);
			
		/*
		 * response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
		 * "Error: Unauthorized");
		 */

		/*
		 * final Map<String, Object> errorAttributes = new LinkedHashMap<>();
		 * errorAttributes.put("status", HttpServletResponse.SC_UNAUTHORIZED);
		 * errorAttributes.put("error", "Unauthorized"); errorAttributes.put("message",
		 * authException.getMessage()); errorAttributes.put("path",
		 * request.getServletPath());
		 * 
		 * request.setAttribute("errorAttributes", errorAttributes);
		 * 
		 * // Redirect to the error page
		 * request.getRequestDispatcher("/error").forward(request, response);
		 */
        
	     // Set the redirect view and add the model attributes to it
			/*
			 * RedirectView redirectView = new RedirectView("/customerror", true);
			 * modelAndView.setView(redirectView);
			 * 
			 * // Return the ModelAndView to redirect try {
			 * modelAndView.getView().render(modelAndView.getModel(), request, response); }
			 * catch (Exception e) { // TODO Auto-generated catch block e.printStackTrace();
			 * }
			 */
	        
//	        request.getRequestDispatcher("/customerror").forward(request, response);
	}
}
