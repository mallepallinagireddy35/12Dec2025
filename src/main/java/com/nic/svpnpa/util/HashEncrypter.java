package com.nic.svpnpa.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nic.svpnpa.service.SVPNPAService;

public class HashEncrypter {
	
	private static final Logger logger = LogManager.getLogger(HashEncrypter.class);
	
	public static String getHash(String salt, String passwd) {
        /*
         public HashEncrypter(int randomKey) {
         */
        String myRandom = salt + passwd;
        byte[] defaultBytes = myRandom.getBytes();
        try {
            MessageDigest algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update(defaultBytes);
            byte messageDigest[] = algorithm.digest();

            //StringBuffer hexString = new StringBuffer();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                //if(hex.length()==1) sb.append("0");
                String hex = Integer.toHexString(0xff & messageDigest[i]);
                if (hex.length() == 1) {
                    sb.append("0");
                }
                sb.append(hex);

                // hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            }
            String foo = messageDigest.toString();
            myRandom = sb + "";

        } catch (NoSuchAlgorithmException e) {
        	logger.error("Exception in HashEncrypter::getHash::" + e.getMessage());
	        logger.error("Exception in HashEncrypter::getHash::" + e.getCause());
	        logger.error("Exception in HashEncrypter::getHash::" + e.getClass() + "::" + e.getLocalizedMessage());
        }
        return myRandom;
    }
    
    public static String getHashforLogin(String salt, String passwd){
    /*
    public HashEncrypter(int randomKey) {
      */  
        String myRandom=salt+passwd;
        byte[] defaultBytes = myRandom.getBytes();
    try{
        MessageDigest algorithm = MessageDigest.getInstance("SHA-256");
        algorithm.reset();
        algorithm.update(defaultBytes);
        byte messageDigest[] = algorithm.digest();
        
        //StringBuffer hexString = new StringBuffer();
        StringBuffer sb = new StringBuffer();
        for (int i=0;i<messageDigest.length;i++) {
            //if(hex.length()==1) sb.append("0");
            String hex=Integer.toHexString(0xff & messageDigest[i]);
            if(hex.length()==1) sb.append("0");
            sb.append(hex);


           // hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
         }
        String foo = messageDigest.toString();
       myRandom=sb+"";
     
    
}catch(NoSuchAlgorithmException e){
	logger.error("Exception in HashEncrypter::getHashforLogin::" + e.getMessage());
    logger.error("Exception in HashEncrypter::getHashforLogin::" + e.getCause());
    logger.error("Exception in HashEncrypter::getHashforLogin::" + e.getClass() + "::" + e.getLocalizedMessage());                  
}
     return myRandom;
        
        
    }
    
      public static String getHash1(String salt, String reference_name) {
        /*
         public HashEncrypter(int randomKey) {
         */
        String myRandom = salt + reference_name;
        byte[] defaultBytes = myRandom.getBytes();
        try {
            MessageDigest algorithm = MessageDigest.getInstance("SHA1");
            algorithm.reset();
            algorithm.update(defaultBytes);
            byte messageDigest[] = algorithm.digest();

            //StringBuffer hexString = new StringBuffer();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                //if(hex.length()==1) sb.append("0");
                String hex = Integer.toHexString(0xff & messageDigest[i]);
                if (hex.length() == 1) {
                    sb.append("0");
                }
                sb.append(hex);
               // System.out.println("hes=="+sb);
                // hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            }
            String foo = messageDigest.toString();
            myRandom = sb + "";

        } catch (NoSuchAlgorithmException e) {
        	logger.error("Exception in HashEncrypter::getHash1::" + e.getMessage());
            logger.error("Exception in HashEncrypter::getHash1::" + e.getCause());
            logger.error("Exception in HashEncrypter::getHash1::" + e.getClass() + "::" + e.getLocalizedMessage());                  
        }
        return myRandom;


    }
      

    public static String getSha1(String input) {
        String outcome = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA1");
            md.update(input.getBytes());
            byte[] output = md.digest();
            outcome = bytesToHex(output);
        } catch (NoSuchAlgorithmException e) {
            outcome = null;
            logger.error("Exception in HashEncrypter::getSha1::" + e.getMessage());
            logger.error("Exception in HashEncrypter::getSha1::" + e.getCause());
            logger.error("Exception in HashEncrypter::getSha1::" + e.getClass() + "::" + e.getLocalizedMessage());
            return outcome;
        }
        return outcome;
    }

    public static String bytesToHex(byte[] b) {
        char hexDigit[] = {'0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        StringBuilder buf = new StringBuilder();
        for (int j = 0; j < b.length; j++) {
            buf.append(hexDigit[(b[j] >> 4) & 0x0f]);
            buf.append(hexDigit[b[j] & 0x0f]);
        }
        return buf.toString();
    }
   
    public static String getHashforLogin1(String passwd){
    /*
    public HashEncrypter(int randomKey) {
      */  
        String myRandom=passwd;
        byte[] defaultBytes = myRandom.getBytes();
    try{
        MessageDigest algorithm = MessageDigest.getInstance("SHA-256");
        algorithm.reset();
        algorithm.update(defaultBytes);
        byte messageDigest[] = algorithm.digest();
        
        //StringBuffer hexString = new StringBuffer();
        StringBuffer sb = new StringBuffer();
        for (int i=0;i<messageDigest.length;i++) {
            //if(hex.length()==1) sb.append("0");
            String hex=Integer.toHexString(0xff & messageDigest[i]);
            if(hex.length()==1) sb.append("0");
            sb.append(hex);


           // hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
         }
        String foo = messageDigest.toString();
       myRandom=sb+"";
     
    
	}catch(NoSuchAlgorithmException e){
		logger.error("Exception in HashEncrypter::getHashforLogin1::" + e.getMessage());
        logger.error("Exception in HashEncrypter::getHashforLogin1::" + e.getCause());
        logger.error("Exception in HashEncrypter::getHashforLogin1::" + e.getClass() + "::" + e.getLocalizedMessage());                          
	}
     return myRandom;               
    }
    
    
}
