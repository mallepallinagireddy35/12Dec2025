package com.nic.svpnpa;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.Scheduled;

import com.nic.svpnpa.controller.SVPNPAController;

@SpringBootApplication
@EnableCaching
public class SvpnpaApplication extends SpringBootServletInitializer{
	
	private static final Logger logger = LogManager.getLogger(SvpnpaApplication.class);
	
	@Autowired
    private CacheManager cacheManager;
	
	@Scheduled(fixedRateString = "${clear.all.cache.fixed.rate}", initialDelayString = "${clear.all.cache.init.delay}") // reset cache every hr, with delay of 1hr after app start
    public void reportCurrentTime() {
        cacheManager.getCacheNames().parallelStream().forEach(name -> cacheManager.getCache(name).clear());
    }
	

    public static void main(String[] args) {
    	System.out.println("Application Started - 07032024");
    	    	
    	 SpringApplication.run(SvpnpaApplication.class, args);
//		System.setProperty("java.io.tmpdir", "/" + cachePath + "/Temp");
//        for(int i=0;i<10;i++) {
//        String uuid = UUID.randomUUID().toString().replace("-", "");        
//        System.out.println("uuid==="+uuid);
//        }

    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
      return application.sources(SvpnpaApplication.class);
     }
    
    public void run(String args) throws Exception {  
//    	clearTempDirectory();            	
    }

//    private void clearTempDirectory() {
//    	try {
//        // Get the path to the temporary directory
//        String tempDirPath = System.getProperty("java.io.tmpdir");
//
//        // Create a File object representing the temporary directory
//        File tempDir = new File(tempDirPath);
//
//        // Check if the directory exists
//        if (tempDir.exists() && tempDir.isDirectory()) {
//            // Get the list of files in the temporary directory
//            File[] files = tempDir.listFiles();
//
//            // Delete each file in the temporary directory
//            if (files != null) {            	
//            	logger.error("SvpnpaApplication::clearTempDirectory::Files in the Temporary Directory");
//                for (File file : files) {                	
//                	logger.error("SvpnpaApplication::clearTempDirectory::File Name:::"+file.getName());
//                    file.delete();
//                }
//            }else {            	
//            	logger.error("SvpnpaApplication::clearTempDirectory::No Files in the Temporary Directory");
//            }
//           
//            logger.error("SvpnpaApplication::clearTempDirectory::Temporary directory cleared");
//        } else {           
//            logger.error("SvpnpaApplication::clearTempDirectory::Temporary directory does not exist");
//        }
//        
//    	} catch (Exception e) {			
//			logger.error("Exception in SvpnpaApplication::clearTempDirectory::" + e.getMessage());
//            logger.error("Exception in SvpnpaApplication::clearTempDirectory::" + e.getCause());
//            logger.error("Exception in SvpnpaApplication::clearTempDirectory::" + e.getClass() + "::" + e.getLocalizedMessage());
//		}
//    }


}
