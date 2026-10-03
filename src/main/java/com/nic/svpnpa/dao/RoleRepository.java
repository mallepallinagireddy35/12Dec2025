package com.nic.svpnpa.dao;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nic.svpnpa.model.ERole;
import com.nic.svpnpa.model.Role;
import com.nic.svpnpa.model.User;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
  Optional<Role> findByName(ERole name);
  
  @Query(value = "SELECT DISTINCT r.role_name FROM roles r LEFT JOIN user_roles u ON r.id = u.role_id WHERE u.user_id = :user_id", nativeQuery = true)
	String findByRoleName(@Param("user_id") BigInteger user_id);
  
}