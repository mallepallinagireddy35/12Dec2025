/**
 * 
 */
package com.nic.svpnpa.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nic.svpnpa.dao.UserRepository;
import com.nic.svpnpa.model.User;

/**
 * @author praka
 *
 */
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsernameAndStatus(username.trim());
    }

    public Optional<User> findByResetToken(String resetToken) {
        return userRepository.findByResetToken(resetToken);
    }
   
    public void save(User user) {
        userRepository.save(user);
    }
    public void unlockUser(String username) {
        Optional<User> userOptional = userRepository.findByUsernameAndStatus(username.trim());
        try {
        userOptional.ifPresent(user -> {
            user.setLocked(false);
            user.setFailed_login_attempts(0);
            userRepository.save(user);
        });
        }catch(Exception e) {
        	e.printStackTrace();
        }
//       System.out.println("username received = [" + username + "]");
//
//    	try {
//    	    Optional<User> userOptional = userRepository.findByUsername(username.trim());
//    	    System.out.println("found = " + userOptional.isPresent());
//
//    	    if (userOptional.isPresent()) {
//    	        User user = userOptional.get();
//    	        System.out.println("DB username = [" + user.getUsername() + "]");
//    	        System.out.println("DB failed attempts = " + user.getFailed_login_attempts());
//    	        System.out.println("DB locked = " + user.isLocked());
//
//    	        user.setLocked(false);
//    	        user.setFailed_login_attempts(0);
//    	        userRepository.save(user);
//    	        System.out.println("After save: locked = " + user.isLocked());
//    	        System.out.println("After save: failed attempts = "
//    	                + user.getFailed_login_attempts());
//    	        System.out.println("User unlocked: " + username);
//    	    } else {
//    	        System.out.println("User not found: " + username);
//    	    }
//    	} catch (Exception e) {
//    	    e.printStackTrace();
//    	}

    }

    public void lockUser(String username) {
        Optional<User> userOptional = userRepository.findByUsernameAndStatus(username.trim());
        userOptional.ifPresent(user -> {
            user.setLocked(true);
            userRepository.save(user);
        });
    }
    
    public String getPasswordHashByUsername(String username) {
        Optional<User> userOptional = userRepository.findByUsernameAndStatus(username.trim());
        
        return userOptional.map(User::getPassword).orElse(null); // Assuming getPasswordHash() returns the hash from User entity
    }
    
    

}