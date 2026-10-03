package com.nic.svpnpa.controller;


import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.nic.svpnpa.dao.RoleRepository;
import com.nic.svpnpa.dao.UserRepository;
import com.nic.svpnpa.model.ERole;
import com.nic.svpnpa.model.Role;
import com.nic.svpnpa.model.UnlockUserRequest;
import com.nic.svpnpa.model.User;
import com.nic.svpnpa.payload.request.SignupRequest;
import com.nic.svpnpa.payload.response.MessageResponse;
import com.nic.svpnpa.service.UserService;
import org.springframework.web.bind.annotation.RequestMethod;


/*@CrossOrigin(origins = "*", maxAge = 3600)*/
@RestController
public class UserRegistrationController {
	private static final Logger logger = LogManager.getLogger(UserRegistrationController.class);

  @Autowired
  UserRepository userRepository;

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  BCryptPasswordEncoder encoder;

  @Autowired
  private UserService userService;
  

  @ResponseBody
  @PostMapping(value = "/signup")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest, BindingResult bindingResult, @RequestHeader(name = "X-XSRF-TOKEN") String csrfToken,
          @RequestHeader(name = "X-XSRF-HEADER") String csrfHeader, HttpSession session) {
	  try {
		/*
		 * String csrfToken = request.getParameter("_csrf");
		 * System.out.println("csrfToken====="+csrfToken); if (csrfToken != null) { //
		 * Validate the CSRF token if
		 * (!csrfToken.getToken().equals(csrfToken.getRequestedToken())) { // CSRF token
		 * mismatch, handle accordingly model.addAttribute("error",
		 * "CSRF token mismatch"); return "error-page"; } // } else { // // CSRF token
		 * not found, handle accordingly // model.addAttribute("error",
		 * "CSRF token not found"); // return "error-page"; // }
		 */	  
	  if (bindingResult.hasErrors()) {
          // Handle validation errors and return appropriate response
		  
		  Map<String, String> validationErrors = new HashMap<>();
          for (FieldError error : bindingResult.getFieldErrors()) {
              validationErrors.put(error.getField(), error.getDefaultMessage());
          }
          return ResponseEntity.badRequest().body(validationErrors);
			/*
			 * // For example, return a JSON response with error details StringBuilder
			 * errorMessage = new StringBuilder();
			 * bindingResult.getAllErrors().forEach(error ->
			 * errorMessage.append(error.getDefaultMessage()).append("\n")); return  
			 * ResponseEntity.badRequest().body(errorMessage.toString());
			 */
      }
	  
	  String captcha = (String) session.getAttribute("captcha_security");		
		
		if (!signUpRequest.getCaptcha().equalsIgnoreCase(captcha)) {
		//session.setAttribute("msg", "Invalid Captcha");
			 return ResponseEntity.badRequest().body("Wrong Captcha!Please Re Enter Details!!");
		}
		
	  

    if (userRepository.existsByUsername(signUpRequest.getUsername())) {
      return ResponseEntity.badRequest().body("{\"message\": \"Username is already in use!\", \"errorType\": \"username\"}");
      
    }

    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
      return ResponseEntity.badRequest().body("{\"message\": \"Email is already in use!\", \"errorType\": \"email\"}");
    }

    // Create new user's account
    User user = new User(signUpRequest.getUsername(),signUpRequest.getFirstName() , signUpRequest.getLastName()  ,signUpRequest.getEmail(),
        encoder.encode(signUpRequest.getPassword()),signUpRequest.getUsername());

    Set<String> strRoles = signUpRequest.getRole();
    Set<Role> roles = new HashSet<>();

    if (strRoles == null) {
      Role userRole = roleRepository.findByName(ERole.ROLE_USER)
          .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
      roles.add(userRole);
    } else {
      strRoles.forEach(role -> {
        switch (role) {
        case "admin":
          Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
          roles.add(adminRole);

          break;
        case "creator":
          Role modRole = roleRepository.findByName(ERole.ROLE_CREATOR)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
          roles.add(modRole);

          break;
        case "verifier":
            Role verRole = roleRepository.findByName(ERole.ROLE_VERIFIER)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(verRole);

            break;
        case "approver":
            Role aprRole = roleRepository.findByName(ERole.ROLE_APPROVER)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(aprRole);

            break;
        default:
          Role userRole = roleRepository.findByName(ERole.ROLE_USER)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
          roles.add(userRole);
        }
      });
    }

    user.setRoles(roles);
    user.setStatus("A");
    userRepository.save(user);
	  } catch (Exception e) {
		  logger.error("Exception in UserRegistrationController::registerUser::" + e.getMessage());
          logger.error("Exception in UserRegistrationController::registerUser::" + e.getCause());
          logger.error("Exception in UserRegistrationController::registerUser::" + e.getClass() + "::" + e.getLocalizedMessage());
	  }
    return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
  }
  
  @GetMapping("/unlcokUser")
  public ModelAndView showUnlockForm(Model model) {
	  try {
      List<User> lockedUsers = userRepository.findByLocked(true);
      
      model.addAttribute("lockedUsers", lockedUsers);
	  } catch (Exception e) {
		  logger.error("Exception in UserRegistrationController::showUnlockForm::" + e.getMessage());
          logger.error("Exception in UserRegistrationController::showUnlockForm::" + e.getCause());
          logger.error("Exception in UserRegistrationController::showUnlockForm::" + e.getClass() + "::" + e.getLocalizedMessage());
	  }
      return new ModelAndView("unlockUser"); // Return the name of the Thymeleaf view template
  }
  
  @GetMapping("/userReport")
  public ModelAndView userReport(Model model) {
	  try {
      List<User> usersList = userRepository.getAllUsersList();      
      model.addAttribute("usersList", usersList);
	  } catch (Exception e) {
		  e.printStackTrace();
		  logger.error("Exception in UserRegistrationController::userReport::" + e.getMessage());
          logger.error("Exception in UserRegistrationController::userReport::" + e.getCause());
          logger.error("Exception in UserRegistrationController::userReport::" + e.getClass() + "::" + e.getLocalizedMessage());
	  }
      return new ModelAndView("userReport"); // Return the name of the Thymeleaf view template
  }

//  @PostMapping("/unlcokUser")
//  public String unlockUsers(@RequestParam("username") List<String> usernames) {
//      for (String username : usernames) {
//          Optional<User> userOptional = userRepository.findByUsername(username);
//          userOptional.ifPresent(user -> {
//              user.setLocked(false);
//              userRepository.save(user);
//          });
//      }
//      return "redirect:/unlock?success";
//  }
	
  
  @PostMapping(value = "/unlock-user")
  public ResponseEntity<String> unlockUser(@RequestHeader(name = "X-XSRF-TOKEN") String csrfToken,
                                           @RequestHeader(name = "X-XSRF-HEADER") String csrfHeader,
                                           @RequestBody UnlockUserRequest request) {
	  try {
      String username = request.getUsername();
      boolean isLocked = request.getIsLocked();
      
      if (isLocked) {    	
          // Logic to unlock user
          userService.unlockUser(username);
          return ResponseEntity.ok("User unlocked successfully");
      } else {
          // Logic to lock user if needed
          userService.lockUser(username);
          return ResponseEntity.ok("User locked successfully");
      }
	  } catch (Exception e) {
		  logger.error("Exception in UserRegistrationController::unlockUser::" + e.getMessage());
          logger.error("Exception in UserRegistrationController::unlockUser::" + e.getCause());
          logger.error("Exception in UserRegistrationController::unlockUser::" + e.getClass() + "::" + e.getLocalizedMessage());
	  }
	  return ResponseEntity.ok("User locked successfully");
  }
  
  @ResponseBody
  @PostMapping(value = "/updateUserDetails")
  public ResponseEntity<?> updateUserDetails(@Valid @RequestBody SignupRequest signUpRequest, BindingResult bindingResult, @RequestHeader(name = "X-XSRF-TOKEN") String csrfToken,
          @RequestHeader(name = "X-XSRF-HEADER") String csrfHeader, HttpSession session) {
	  try {
			  if (bindingResult.hasErrors()) {
          // Handle validation errors and return appropriate response
		  
		  Map<String, String> validationErrors = new HashMap<>();
          for (FieldError error : bindingResult.getFieldErrors()) {
              validationErrors.put(error.getField(), error.getDefaultMessage());
          }
          return ResponseEntity.badRequest().body(validationErrors);
		
      }
	  
	  String captcha = (String) session.getAttribute("captcha_security");		
		
		if (!signUpRequest.getCaptcha().equalsIgnoreCase(captcha)) {
		//session.setAttribute("msg", "Invalid Captcha");
			 return ResponseEntity.badRequest().body("Wrong Captcha!Please Re Enter Details!!");
		}
		
	  

//    if (userRepository.existsByUsername(signUpRequest.getUsername())) {
//      return ResponseEntity.badRequest().body("{\"message\": \"Username is already in use!\", \"errorType\": \"username\"}");
//      
//    }

//    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
//      return ResponseEntity.badRequest().body("{\"message\": \"Email is already in use!\", \"errorType\": \"email\"}");
//    }

    // Create new user's account
    User user = new User(signUpRequest.getUsername(),signUpRequest.getFirstName() , signUpRequest.getLastName()  ,signUpRequest.getEmail(),
        encoder.encode(signUpRequest.getPassword()),signUpRequest.getUsername());

    Set<String> strRoles = signUpRequest.getRole();
    Set<Role> roles = new HashSet<>();

    if (strRoles == null) {
      Role userRole = roleRepository.findByName(ERole.ROLE_USER)
          .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
      roles.add(userRole);
    } else {
      strRoles.forEach(role -> {
        switch (role) {
        case "admin":
          Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
          roles.add(adminRole);

          break;
        case "creator":
          Role modRole = roleRepository.findByName(ERole.ROLE_CREATOR)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
          roles.add(modRole);

          break;
        case "verifier":
            Role verRole = roleRepository.findByName(ERole.ROLE_VERIFIER)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(verRole);

            break;
        case "approver":
            Role aprRole = roleRepository.findByName(ERole.ROLE_APPROVER)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(aprRole);

            break;
        default:
          Role userRole = roleRepository.findByName(ERole.ROLE_USER)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
          roles.add(userRole);
        }
      });
    }

    
    user.setRoles(roles);
    user.setId(signUpRequest.getId());
    user.setStatus("A");
    User user1 = userRepository.findById(signUpRequest.getId()).orElseThrow(() -> new RuntimeException("User not found"));
    userRepository.save(user);
	  } catch (Exception e) {
		  logger.error("Exception in UserRegistrationController::updateUserDetails::" + e.getMessage());
          logger.error("Exception in UserRegistrationController::updateUserDetails::" + e.getCause());
          logger.error("Exception in UserRegistrationController::updateUserDetails::" + e.getClass() + "::" + e.getLocalizedMessage());
	  }
    return ResponseEntity.ok(new MessageResponse("User Updated successfully!"));
  }

	/*
	 * @PostMapping(value = "/unlock-user") public ResponseEntity<String>
	 * unlockUser(@RequestHeader(name = "X-XSRF-TOKEN") String csrfToken,
	 * 
	 * @RequestHeader(name = "X-XSRF-HEADER") String
	 * csrfHeader,@RequestParam("username") String username,
	 * 
	 * @RequestParam("isLocked") boolean isLocked) { if (isLocked) { // Logic to
	 * unlock user userService.unlockUser(username); return
	 * ResponseEntity.ok("User unlocked successfully"); } else { // Logic to lock
	 * user if needed userService.lockUser(username); return
	 * ResponseEntity.ok("User locked successfully"); }
	 * 
	 * 
	 * }
	 */

	/*
	 * @PostMapping("/signup") public ResponseEntity<?>
	 * registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
	 * 
	 * 
	 * logger.info("----------- inside registerUser-----/signup-------- "); if
	 * (userRepository.existsByUsername(signUpRequest.getUsername())) { return
	 * ResponseEntity.badRequest().body(new
	 * MessageResponse("Error: Username is already taken!")); }
	 * 
	 * if (userRepository.existsByEmail(signUpRequest.getEmail())) { return
	 * ResponseEntity.badRequest().body(new
	 * MessageResponse("Error: Email is already in use!")); }
	 * 
	 * Set<String> roleName = signUpRequest.getRole();
	 * 
	 * Set<Role> roleSet = new HashSet<Role>();
	 * 
	 * 
	 * 
	 * // Create new user's account User user = new
	 * User(signUpRequest.getUsername(),signUpRequest.getFirstName(),signUpRequest.
	 * getLastName(), signUpRequest.getEmail(),
	 * encoder.encode(signUpRequest.getPassword()),roleSet);
	 * 
	 * Set<String> strRoles = signUpRequest.getRole(); Set<Role> roles = new
	 * HashSet<>();
	 * 
	 * if (strRoles == null) { Role userRole =
	 * roleRepository.findByName(ERole.ROLE_USER) .orElseThrow(() -> new
	 * RuntimeException("Error: Role is not found.")); roles.add(userRole); } else {
	 * strRoles.forEach(role -> { switch (role) { case "admin": Role adminRole =
	 * roleRepository.findByName(ERole.ROLE_ADMIN) .orElseThrow(() -> new
	 * RuntimeException("Error: Role is not found.")); roles.add(adminRole);
	 * 
	 * break; case "mod": Role modRole =
	 * roleRepository.findByName(ERole.ROLE_CREATOR) .orElseThrow(() -> new
	 * RuntimeException("Error: Role is not found.")); roles.add(modRole);
	 * 
	 * break; default: Role userRole1 = roleRepository.findByName(ERole.ROLE_USER)
	 * .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
	 * roles.add(userRole1); } }); }
	 * 
	 * user.setRoles(roles); userRepository.save(user);
	 * 
	 * return ResponseEntity.ok(new
	 * MessageResponse("User registered successfully!")); }
	 */
}