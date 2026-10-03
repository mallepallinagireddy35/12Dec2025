/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nic.svpnpa.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nic.svpnpa.service.SVPNPAService;

public class Hash_512 {

    /**
     *
     * @param input
     * @return
     */
	
	private static final Logger logger = LogManager.getLogger(Hash_512.class);
	
    public static String encryptThisString(String input) {
        try {

            // //System.out.println("hash-- after---"+hashtext);
            Hash_512 h = new Hash_512();
            String hashtext = h.get_SHA_512_SecurePassword(input, "");

            // return the HashText 
            return hashtext;

        } // For specifying wrong message digest algorithms  // For specifying wrong message digest algorithms  // For specifying wrong message digest algorithms  // For specifying wrong message digest algorithms  // For specifying wrong message digest algorithms  // For specifying wrong message digest algorithms  // For specifying wrong message digest algorithms  // For specifying wrong message digest algorithms  // For specifying wrong message digest algorithms  // For specifying wrong message digest algorithms  // For specifying wrong message digest algorithms  // For specifying wrong message digest algorithms  // For specifying wrong message digest algorithms  // For specifying wrong message digest algorithms  // For specifying wrong message digest algorithms  // For specifying wrong message digest algorithms 
        catch (Exception e) {
        	logger.error("Exception in Hash_512::encryptThisString::" + e.getMessage());
	        logger.error("Exception in Hash_512::encryptThisString::" + e.getCause());
	        logger.error("Exception in Hash_512::encryptThisString::" + e.getClass() + "::" + e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
    }

    public String get_SHA_512_SecurePassword(String passwordToHash, String salt) {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(salt.getBytes(StandardCharsets.UTF_8));
            byte[] bytes = md.digest(passwordToHash.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
        	logger.error("Exception in Hash_512::get_SHA_512_SecurePassword::" + e.getMessage());
	        logger.error("Exception in Hash_512::get_SHA_512_SecurePassword::" + e.getCause());
	        logger.error("Exception in Hash_512::get_SHA_512_SecurePassword::" + e.getClass() + "::" + e.getLocalizedMessage());
        }
        return generatedPassword;
    }
}
