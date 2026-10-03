/**
 * 
 */
package com.nic.svpnpa.service;

/**
 * @author praka
 *
 */
import com.nic.svpnpa.model.UserLog;
import java.time.LocalDateTime;
import java.util.List;

public interface UserLogService {
	
	List<UserLog> getUserLogsByDateRange(LocalDateTime startDate, LocalDateTime endDate);
}
