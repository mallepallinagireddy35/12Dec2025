package com.nic.svpnpa.security.services;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nic.svpnpa.Exceptions.AccountLockedException;

import com.nic.svpnpa.dao.UserRepository;
import com.nic.svpnpa.model.User;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {
  

@Autowired	
 UserRepository userRepository;

  
  @Override
  @Transactional
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

    if (user.isLocked()) {
    	 throw new AccountLockedException("Account is locked due to too many failed attempts.");
    }
   
    return UserDetailsImpl.build(user);
  }
  
  
  
  
}
