/**
 * 
 */
package com.nic.svpnpa.dao;


import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nic.svpnpa.model.UserLog;

import java.util.Date;
import java.util.List;

/**
 * @author praka
 *
 */
public interface UserLogRepository extends JpaRepository<UserLog, Long> {
	 List<UserLog> findByTimestampBetweenAndAction(LocalDateTime startDate, LocalDateTime endDate, String action);
	 
	  List<UserLog> findByTimestampBetween(LocalDateTime startDate, LocalDateTime endDate);
}
