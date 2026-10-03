/**
 * 
 */
package com.nic.svpnpa.service.impl;

import com.nic.svpnpa.dao.UserLogRepository;
/**
 * @author praka
 *
 */
import com.nic.svpnpa.model.UserLog;

import com.nic.svpnpa.service.UserLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserLogServiceImpl implements UserLogService {

    @Autowired
    private UserLogRepository userLogRepository;

    

    @Override
    public List<UserLog> getUserLogsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return userLogRepository.findByTimestampBetween(startDate, endDate);
    }
}
