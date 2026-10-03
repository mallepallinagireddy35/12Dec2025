/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package com.org.nic.igrs.validation;
package com.nic.svpnpa.util;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nic.svpnpa.service.SVPNPAService;

/**
 *
 * @author IGRS
 */
public class SqlValidation {
	
	private static final Logger logger = LogManager.getLogger(SqlValidation.class);

    public boolean isSecuritySensitive(String parameter) throws IOException,NullPointerException {
        boolean result = true;
        parameter = parameter.toLowerCase();

        try {
            if (parameter != null && parameter.trim().length() > 0) {
                String[] blackList1 = {"--", ";--", ";", "/*", "*/", "@@", "??", "||", "<", ">",
                    "<script", ".js", "xp_", "dbshut", "http", "</script>", "javascript:", "<style", "</style>",
                    "alert(", "<a href=", "<img src=", "<frame", "<iframe", "style=", ".html", "|", "alter",
                    "begin", "create", "cursor", "declare", "delete",
                    "drop", "fetch", "insert",
                    "kill", "select"};
                String[] blackList = {"/*", "*/"};

                parameter = parameter.toLowerCase(); //convert the input to lowercase
                for (int i = 0; i < blackList.length; i++) {
                    if (parameter.indexOf(blackList[i]) >= 0) { //this condition needs to be checked
                        result = false;
                    }
                }
            }
        } catch (Exception e) {
        	logger.error("Exception in SqlValidation::isSecuritySensitive::" + e.getMessage());
	        logger.error("Exception in SqlValidation::isSecuritySensitive::" + e.getCause());
	        logger.error("Exception in SqlValidation::isSecuritySensitive::" + e.getClass() + "::" + e.getLocalizedMessage());
            return false;
        }
        return result;
    }

}
