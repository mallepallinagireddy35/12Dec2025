package com.nic.svpnpa.controller;

import java.io.IOException;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nic.svpnpa.model.CaptchaDto;
import com.nic.svpnpa.model.Role;
import com.nic.svpnpa.model.User;
import com.nic.svpnpa.model.UserLog;
import com.nic.svpnpa.model.WebMenuBean;
import com.nic.svpnpa.payload.request.LoginRequest;
import com.nic.svpnpa.payload.response.JwtResponse;
import com.nic.svpnpa.Exceptions.BadCaptchaException;
import com.nic.svpnpa.dao.AdminDao;
import com.nic.svpnpa.dao.RoleRepository;
import com.nic.svpnpa.dao.UserLogRepository;
import com.nic.svpnpa.dao.UserRepository;
import com.nic.svpnpa.security.jwt.JwtUtils;
import com.nic.svpnpa.security.services.UserDetailsImpl;
import com.nic.svpnpa.security.services.UserDetailsServiceImpl;
import com.nic.svpnpa.util.CaptchaValidationFilter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;



@Controller
public class AuthController {

	@Autowired
	CaptchaController  captchaController;
	
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	UserLogRepository userLogRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	BCryptPasswordEncoder encoder;
	@Autowired
	UserDetailsServiceImpl userDetails;
	
	@Autowired
	AdminDao AdminDaoImpl;
	
	@Autowired
	LoginUserDetailsController loginController;
	
//	@Autowired
//	private UserPasswordResetService passwordResetService;
	@Autowired
	JwtUtils jwtUtils;
	
	private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();	
	
	private SecurityContextRepository securityContextRepository =
	        new HttpSessionSecurityContextRepository();
	private static final Logger logger = LogManager.getLogger(AuthController.class);

	@GetMapping("/registration")
	public String showRegistrationForm(Model model, HttpServletRequest request, HttpServletResponse response,HttpSession session) {
		User user = new User(); // Create a new User object
		// List<String> roleList = roleRepository.findAllRoleNames(); // Fetch role
		List<Role> roleList =  new ArrayList<Role>();
		// names from the database
		try {
	    CaptchaDto captchaDto = captchaController.generateCaptchaImage(session, request, response, model);
	    // Add the CAPTCHA string to the model
	    model.addAttribute("captchaString", captchaDto.getCaptchaString());
	    session.setAttribute("captchaString", captchaDto.getCaptchaString());
	    // Render the CAPTCHA image
	    byte[] captchaImage = captchaDto.getImage();

	    // Convert the image byte array to a Base64-encoded string
	    String base64Image = Base64.getEncoder().encodeToString(captchaImage);
	    model.addAttribute("captchaImage", base64Image);
		model.addAttribute("user", user);
//		model.addAttribute("roleList", roleRepository.findAll());
		
		String username = "";
			UserDetails userDetails = loginController.getUserDetails();
			try {
			username = userDetails.getUsername();
			}catch(Exception ex) {}
		roleList = AdminDaoImpl.getRolesList(username);
		 model.addAttribute("roleList", roleList);
		} catch (Exception e) {
			logger.error("Exception in AuthController::showRegistrationForm::" + e.getMessage());
            logger.error("Exception in AuthController::showRegistrationForm::" + e.getCause());
            logger.error("Exception in AuthController::showRegistrationForm::" + e.getClass() + "::" + e.getLocalizedMessage());
		}
		return "registration";
	}

	@GetMapping("/getsignin")
	public String login(@RequestParam(defaultValue = "") String error, Model m, HttpServletRequest request,
			HttpServletResponse response, RedirectAttributes redirectAttributes) throws ServletException, IOException {
		HttpSession session = request.getSession();
		try {
		redirectAttributes.addFlashAttribute("error", error);
//		m.addAttribute("error", error);
		
	    // Call the generateCaptchaImage method from CaptchaController to get the CAPTCHA string
	    CaptchaDto captchaDto = captchaController.generateCaptchaImage(session, request, response, m);
//	    System.out.println(captchaDto.getCaptchaString());
	    // Add the CAPTCHA string to the model
	    m.addAttribute("captchaString", captchaDto.getCaptchaString());
	    session.setAttribute("captchaString", captchaDto.getCaptchaString());
	    // Render the CAPTCHA image
	    byte[] captchaImage = captchaDto.getImage();

	    // Convert the image byte array to a Base64-encoded string
	    String base64Image = Base64.getEncoder().encodeToString(captchaImage);
	    m.addAttribute("captchaImage", base64Image);
		} catch (Exception e) {
			logger.error("Exception in AuthController::showRegistrationForm::" + e.getMessage());
            logger.error("Exception in AuthController::showRegistrationForm::" + e.getCause());
            logger.error("Exception in AuthController::showRegistrationForm::" + e.getClass() + "::" + e.getLocalizedMessage());
		}
		return "sign-in";
	}

	/*
	 * @PostMapping("/signin")
	 * 
	 * @ResponseBody public ModelAndView
	 * loginSubmit(@Valid @RequestParam("username") String
	 * username,@RequestParam("password") String password,HttpServletResponse
	 * response) {
	 * 
	 * logger.
	 * info("----------- inside AuthController.SIGNIN-----loginSubmit-------- ");
	 * ModelAndView modelAndView = new ModelAndView();
	 * System.out.println("password..."+password); // Validate the password before
	 * proceeding boolean isPasswordValid = authenticateUser(username,password); //
	 * Implement this method
	 * 
	 * if (!isPasswordValid) { modelAndView.setViewName("sign-in");
	 * modelAndView.addObject("error", "Invalid credentials. Please try again.");
	 * return modelAndView; }
	 * 
	 * Authentication authentication = authenticationManager.authenticate( new
	 * UsernamePasswordAuthenticationToken(username, password));
	 * 
	 * Optional<User> userOptional = userRepository.findByUsername(username); User
	 * user = userOptional.get(); String encoded = encoder.encode(password); if
	 * (userOptional.isPresent()) { UserDetailsImpl userDetails = (UserDetailsImpl)
	 * authentication.getPrincipal(); if (userDetails.getUsername().equals(username)
	 * && userDetails.getPassword().equals(encoded)) { // Generate JWT token here
	 * using your JWT utility class and add it to the // response header or as a
	 * cookie. // String jwtToken = generateJwtToken(username);
	 * SecurityContextHolder.getContext().setAuthentication(authentication); String
	 * jwt = jwtUtils.generateJwtToken(authentication); List<String> roles =
	 * userDetails.getAuthorities().stream().map(item -> item.getAuthority())
	 * .collect(Collectors.toList());
	 * 
	 * response.setHeader("Authorization", "Bearer " + jwt); // Redirect the user to
	 * the home page or some other secured page. modelAndView = new
	 * ModelAndView("redirect:/admin_home"); modelAndView.addObject("token", jwt);
	 * userRepository.save(user); // case) return modelAndView; } else {
	 * 
	 * // Login failed, redirect back to the login page with an error message.
	 * modelAndView = new ModelAndView("sign-in"); // Return the login page view
	 * (login.html in this userRepository.save(user); // case)
	 * modelAndView.addObject("error", "Invalid credentials. Please try again.");
	 * return modelAndView; }
	 * 
	 * 
	 * }
	 * 
	 * 
	 * return modelAndView; }
	 */
	/*
	 * @PostMapping("/signin") public ResponseEntity<?>
	 * authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
	 * 
	 * Authentication authentication = authenticationManager .authenticate(new
	 * UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
	 * loginRequest.getPassword()));
	 * 
	 * SecurityContextHolder.getContext().setAuthentication(authentication); String
	 * jwt = jwtUtils.generateJwtToken(authentication);
	 * 
	 * UserDetailsImpl userDetails = (UserDetailsImpl)
	 * authentication.getPrincipal(); List<String> roles =
	 * userDetails.getAuthorities().stream().map(item -> item.getAuthority())
	 * .collect(Collectors.toList());
	 * 
	 * return ResponseEntity .ok(new JwtResponse(jwt, userDetails.getId(),
	 * userDetails.getUsername(), userDetails.getEmail(), roles)); }
	 */
//	@PostMapping("/signin")
//	@ResponseBody
//	public ModelAndView loginSubmit(@RequestParam("_csrf") CsrfToken csrfToken,
//	        @Valid @RequestParam("username") String username,
//	        @RequestParam("password") String password,@RequestParam("captcha") String captcha,
//	        HttpServletResponse response,HttpServletRequest request,HttpSession session) {
//
//
//	    ModelAndView modelAndView = new ModelAndView();
//	    //System.out.println(" AuthController password..." + password);
//	    String captcha1 = (String) session.getAttribute("captcha_security");		
//
//		if (!captcha.equalsIgnoreCase(captcha1)) {
//			modelAndView.setViewName("sign-in");
//	        modelAndView.addObject("error", "Invalid Captcha");
//	        return modelAndView;
//		}
//
//	    // Validate the password before proceeding
//	    boolean isPasswordValid = authenticateUser(username, password); // Implement this method
//
//	    if (!isPasswordValid) {
//	        modelAndView.setViewName("sign-in");
//	        modelAndView.addObject("error", "Invalid credentials. Please try again.");
//	        return modelAndView;
//	    }
//
//	    try {
//	        Authentication authentication = authenticationManager.authenticate(
//	                new UsernamePasswordAuthenticationToken(username, password));
//
//	       
//	        SecurityContextHolder.getContext().setAuthentication(authentication);
//	        
//	        String jwt = jwtUtils.generateJwtToken(authentication);
//	        response.setHeader("Authorization", "Bearer " + jwt);
//	        
//
//	        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
//	        List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
//					.collect(Collectors.toList());
//	    	
//	        modelAndView.setViewName("redirect:/");
//	        modelAndView.addObject("token", jwt);
//	        modelAndView.addObject("id", userDetails.getId());
//	        modelAndView.addObject("username", userDetails.getUsername());
//	        modelAndView.addObject("email", userDetails.getEmail());
//	        modelAndView.addObject("roles", roles);
//
//	        return modelAndView;
//	    } catch (AuthenticationException e) {
//	        modelAndView.setViewName("sign-in");
//	        modelAndView.addObject("error", "Invalid credentials. Please try again.");
//	        return modelAndView;
//	    }
//	}

	@PostMapping("/loginSubmit")
	@ResponseBody
	public ModelAndView loginSubmit(@Valid @RequestParam("username") String username, @RequestParam("password") String password,
			@RequestParam("captcha") String captcha, HttpServletResponse response, HttpServletRequest request,
			HttpSession session) {

		ModelAndView modelAndView = new ModelAndView();

					
		// Validate the password before proceeding
		boolean isPasswordValid = authenticateUser(username, password); // Implement this method
		System.out.println("isPasswordValid====");
		if (!isPasswordValid) {
			System.out.println("isPasswordValid====");
			modelAndView.setViewName("sign-in");
			modelAndView.addObject("error", "Invalid credentials. Please try again.");
			return modelAndView;
		}

		try {
			Authentication authentication = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(username, password));

			SecurityContextHolder.getContext().setAuthentication(authentication);
			SecurityContext context = SecurityContextHolder.createEmptyContext(); 
			context.setAuthentication(authentication); 
			//SecurityContextHolder.setContext(context); 
			 this.securityContextHolderStrategy.setContext(context);
			

			// save the security context in the session repository
//			securityContextRepository.save(request, response, SecurityContextHolder.getContext());
			String jwt = jwtUtils.generateJwtToken(authentication);
			response.setHeader("Authorization", "Bearer " + jwt);

			UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
			List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
					.collect(Collectors.toList());
			modelAndView.setViewName("redirect:/");
			modelAndView.addObject("token", jwt);
			response.setHeader("Authorization", "Bearer " + jwt);
			/*
			 * modelAndView.addObject("id", userDetails.getId());
			 * modelAndView.addObject("username", userDetails.getUsername());
			 * modelAndView.addObject("email", userDetails.getEmail());
			 * modelAndView.addObject("roles", roles);
			 */
			return modelAndView;
		} catch (BadCaptchaException e) {
			  modelAndView.setViewName("sign-in"); 
			  modelAndView.addObject("error","Invalid Captcha");
			  logger.error("Exception in AuthController::loginSubmit::" + e.getMessage());
	          logger.error("Exception in AuthController::loginSubmit::" + e.getCause());
	          logger.error("Exception in AuthController::loginSubmit::" + e.getClass() + "::" + e.getLocalizedMessage());
			
			  
            return modelAndView;
        } catch (AuthenticationException e) {
        	e.printStackTrace();
        	System.out.println("catch==========");
			modelAndView.setViewName("sign-in");
			modelAndView.addObject("error", "Invalid credentials. Please try again.");
			
			logger.error("Exception in AuthController::loginSubmit::" + e.getMessage());
            logger.error("Exception in AuthController::loginSubmit::" + e.getCause());
            logger.error("Exception in AuthController::loginSubmit::" + e.getClass() + "::" + e.getLocalizedMessage());			
			return modelAndView;
		}
	}

	public boolean validatePassword(String enteredPassword, User userDetails) {
		return encoder.matches(enteredPassword, userDetails.getPassword());
	}

	public boolean authenticateUser(String username, String enteredPassword) {
		Optional<User> userOptional = userRepository.findByUsername(username);
		User user = userOptional.get();

		if (user != null) {
			return validatePassword(enteredPassword, user);
		}

		return false;
	}

	@RequestMapping(value = "/logout", method = { RequestMethod.GET, RequestMethod.POST })
	public String logout(Model m, HttpServletRequest request, HttpServletResponse response,HttpSession session) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		try {
		if (authentication != null) {
			String username = authentication.getName();

			Optional<User> userOptional = userRepository.findByUsername(username);

			if (userOptional.isPresent()) {
				User user = userOptional.get();
				if (user != null) {
					UserLog logoutLog = new UserLog();
					logoutLog.setUser(user);
					logoutLog.setAction("logout");
					logoutLog.setTimestamp(LocalDateTime.now());
					userLogRepository.save(logoutLog);
					user.setFailed_login_attempts(0);
					user.setLocked(false);
					user.setLoggedIn(false);

					userRepository.save(user);
				}
			}
		}

		session.invalidate();
		// Perform logout logic...
		}catch(Exception e) {
		logger.error("Exception in AuthController::logout::" + e.getMessage());
        logger.error("Exception in AuthController::logout::" + e.getCause());
        logger.error("Exception in AuthController::logout::" + e.getClass() + "::" + e.getLocalizedMessage());
		}
		return "redirect:/getsignin?logout";
	}
	
	@GetMapping("/registrationAdmin")
	public String showRegistrationFormAdmin(Model model, HttpServletRequest request, HttpServletResponse response,HttpSession session) {
		User user = new User(); // Create a new User object
		// List<String> roleList = roleRepository.findAllRoleNames(); // Fetch role
		List<Role> roleList =  new ArrayList<Role>();
		// names from the database
		try {
	    CaptchaDto captchaDto = captchaController.generateCaptchaImage(session, request, response, model);
	    // Add the CAPTCHA string to the model
	    model.addAttribute("captchaString", captchaDto.getCaptchaString());
	    session.setAttribute("captchaString", captchaDto.getCaptchaString());
	    // Render the CAPTCHA image
	    byte[] captchaImage = captchaDto.getImage();

	    // Convert the image byte array to a Base64-encoded string 
	    String base64Image = Base64.getEncoder().encodeToString(captchaImage);
	    model.addAttribute("captchaImage", base64Image);
		model.addAttribute("user", user);
//		model.addAttribute("roleList", roleRepository.findAll());
		
		String username = "";
			UserDetails userDetails = loginController.getUserDetails();
			try {
			username = userDetails.getUsername();
			}catch(Exception ex) {}
		roleList = AdminDaoImpl.getRolesList(username);
		 model.addAttribute("roleList", roleList);
		} catch (Exception e) {
			logger.error("Exception in AuthController::showRegistrationFormAdmin::" + e.getMessage());
            logger.error("Exception in AuthController::showRegistrationFormAdmin::" + e.getCause());
            logger.error("Exception in AuthController::showRegistrationFormAdmin::" + e.getClass() + "::" + e.getLocalizedMessage());
		}
		return "registration_admin";
	}
	
	@GetMapping("/editUserDetails")
	public String editUserDetails(@RequestParam("username") String username,@RequestParam("id") String id,Model model, HttpServletRequest request, HttpServletResponse response,HttpSession session) {
		List<Role> roleList =  new ArrayList<Role>();
		// names from the database
		try {
			Optional<User> userOptional = userRepository.findByUsername(username);
			User user = userOptional.get();
				
	    CaptchaDto captchaDto = captchaController.generateCaptchaImage(session, request, response, model);
	    // Add the CAPTCHA string to the model
	    model.addAttribute("captchaString", captchaDto.getCaptchaString());
	    session.setAttribute("captchaString", captchaDto.getCaptchaString());
	    // Render the CAPTCHA image
	    byte[] captchaImage = captchaDto.getImage();
	    // Convert the image byte array to a Base64-encoded string 
	    String base64Image = Base64.getEncoder().encodeToString(captchaImage);
	    model.addAttribute("captchaImage", base64Image);
		model.addAttribute("user", user);
		roleList = AdminDaoImpl.getRolesList(username);
		model.addAttribute("roleList", roleList);
		
	    BigInteger user_id = new BigInteger(id);
		String role_name = roleRepository.findByRoleName(user_id);
		model.addAttribute("role_name", role_name);
		model.addAttribute("user_id", user_id);
		
		System.out.println("role_name==="+role_name);
		} catch (Exception e) {
			logger.error("Exception in AuthController::showRegistrationFormAdmin::" + e.getMessage());
            logger.error("Exception in AuthController::showRegistrationFormAdmin::" + e.getCause());
            logger.error("Exception in AuthController::showRegistrationFormAdmin::" + e.getClass() + "::" + e.getLocalizedMessage());
		}
		return "editUserDetails";
	}

}