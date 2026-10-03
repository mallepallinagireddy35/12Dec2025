package com.nic.svpnpa.dao;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nic.svpnpa.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
//  Optional<User> findByUsername(String username);
	
	@Query("SELECT u FROM User u WHERE TRIM(u.username) = TRIM(:username) and u.status='A'")
	Optional<User> findByUsername(@Param("username") String username);

  Boolean existsByUsername(String username);

  Boolean existsByEmail(String email);
  
  User findByEmail(String email);
  
  Optional<User> findByResetToken(String resetToken);
  
  List<User> findByLocked(boolean locked);
  
  
  @Query("SELECT u.loggedIn, u.locked, (SELECT ul.timestamp > :cutoffTime FROM UserLog ul WHERE ul.user.id = u.id ORDER BY ul.timestamp DESC LIMIT 1) FROM User u WHERE u.username = :username")
  Object[] findUserStatusInfo(String username, LocalDateTime cutoffTime);

  
  @Query("select u from User u where u.status='A' order by u.id")
  List<User> getAllUsersList();

  
  Optional<User> findById(Long id);
 
  

}