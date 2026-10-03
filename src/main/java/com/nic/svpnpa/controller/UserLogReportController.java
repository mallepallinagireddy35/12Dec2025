/**
 * 
 */
package com.nic.svpnpa.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
/**
 * @author praka
 *
 */import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.stereotype.Controller;
 import org.springframework.ui.Model;
 import org.springframework.web.bind.annotation.GetMapping;
 import org.springframework.web.bind.annotation.PostMapping;
 import org.springframework.web.bind.annotation.RequestMapping;

import com.nic.svpnpa.model.UserLog;
import com.nic.svpnpa.model.UserLogReportForm;
import com.nic.svpnpa.service.UserLogService;

import org.springframework.web.bind.annotation.ModelAttribute;
 import java.time.LocalDateTime;
 import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Controller
public class UserLogReportController {

	private static final Logger logger = LogManager.getLogger(UserLogReportController.class);
	
    @Autowired
    private UserLogService userLogService;

    @GetMapping("/logreport")
    public String showUserLogReportForm(Model model) {
        model.addAttribute("reportForm", new UserLogReportForm());
        return "userlog_report";
    }

    @PostMapping("/logreport")
    public String generateUserLogReport(@ModelAttribute UserLogReportForm reportForm, Model model) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        try {
            LocalDateTime startDate = LocalDateTime.parse(reportForm.getStartDate(), formatter);
            LocalDateTime endDate = LocalDateTime.parse(reportForm.getEndDate(), formatter);

            List<UserLog> userLogs = userLogService.getUserLogsByDateRange(startDate, endDate);
            model.addAttribute("userLogs", userLogs);
        } catch (DateTimeParseException e) {
    		logger.error("Error in AdminController::generateUserLogReport::" + e.getMessage());
            logger.error("Error in AdminController::generateUserLogReport::" + e.getCause());
            logger.error("Error in AdminController::generateUserLogReport::" + e.getClass() + "::" + e.getLocalizedMessage());
        }

        return "userlog_report";
    }
}