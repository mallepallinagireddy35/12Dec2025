package com.nic.svpnpa.util;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.multipart.MultipartFile;

import com.nic.svpnpa.model.JsonBean;
import com.nic.svpnpa.service.SVPNPAService;

public class FormStructureValidation {
	
	private static final Logger logger = LogManager.getLogger(FormStructureValidation.class);

	public ArrayList<String> validate(Object target) {
		JsonBean bean = (JsonBean) target;
		
	    String dropdown = "^[a-zA-Z ]*$"; 
	    String number = "^[0-9]*$";
	    String filetype = "^[a-zA-Z| ]*$";
	    String textarea = "^[0-9a-zA-Z ,.*-/()&$#@(\\.[_\"\']+) ]+$";
		


		int len = 0;String msg = "";
		int y = 0;
        ArrayList<String> errlist = new ArrayList<String>();
		try {        
        String form_name = bean.getForm_name();

        	if(form_name.equalsIgnoreCase("") || form_name.equalsIgnoreCase("null")) {
        		msg = "Enter  Form Name";
        		errlist.add(msg);
        	}else 
        		if(form_name.length() > 100){
        			msg = "Form Name: Length Exceeded Max Length is 100";
        			errlist.add(msg);
    		}else 
        	if(form_name.length() < 1){
        		msg = "Form Name: Length Exceeded Min Length is 1";
        		errlist.add(msg);
    		}else {
        	Pattern pattern = Pattern.compile(textarea);
            Matcher matcher = pattern.matcher(form_name);
            if (!matcher.matches()) {
            	msg = "Enter  Valid Form Name";
            	errlist.add(msg);
            }
    		}
      
            String fields = bean.getFields();

        	if(fields.equalsIgnoreCase("") || fields.equalsIgnoreCase("null")) {
        		msg = "Enter  No Of Fields";
        		errlist.add(msg);
        	}else 
        		if(fields.length() > 2){
        			msg = "No Of Fields: Length Exceeded Max Length is 2";
        			errlist.add(msg);
    		}else 
        	if(fields.length() < 1){
        		msg = "No Of Fields: Length Exceeded Min Length is 1";
        		errlist.add(msg);
    		}else {
        	Pattern pattern1 = Pattern.compile(number);
            Matcher matcher1 = pattern1.matcher(fields);
            if (!matcher1.matches()) {
            	msg = "Enter  Valid No Of Fields";
            	errlist.add(msg);
            }
    		}
	   
		int no = Integer.parseInt(bean.getFields());

		String jsonString = bean.getJsonData().toString();

		JSONParser parser = new JSONParser();
        Object obj = null;
		try {
//			obj = parser.parse(bean.getJsonFields());
			obj = parser.parse(jsonString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
        JSONArray array = (JSONArray) obj;
        JSONArray array1 = new JSONArray();
        for (int i = 1; i < array.size(); i++) {
            JSONObject jsonObject = (JSONObject) array.get(i);
            array1.add(i - 1, jsonObject);
        }
        
//        JSONParser parser1 = new JSONParser();
//        Object obj3 = null;
//		try {
//			obj3 = parser1.parse(jsonString);
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
//        JSONArray array1 = (JSONArray) obj3;            
        
        for (int i = 0; i < no; i++) {
     	   JSONObject jsonObject1 = (JSONObject) array1.get(i);
//            JSONObject obj1 = (JSONObject) array1.get(i);                   
            String dtype = (String) jsonObject1.get("dtype");
            String label = (String) jsonObject1.get("label");

            String minsize = (String) jsonObject1.get("minsize");
            String maxsize = (String) jsonObject1.get("maxsize");

            String mandatory = (String) jsonObject1.get("mandatory");

            
          	if(label.equalsIgnoreCase("") || label.equalsIgnoreCase("null")) {
        		msg = "Enter  Label Name";
        		errlist.add(msg);
        	}else 
        		if(label.length() > 50){
        			msg = "Label Name: Length Exceeded Max Length is 50";
        			errlist.add(msg);
    		}else 
        	if(label.length() < 1){
        		msg = "Label Name: Length Exceeded Min Length is 1";
        		errlist.add(msg);
    		}else {
        	Pattern pattern2 = Pattern.compile(dropdown);
            Matcher matcher2 = pattern2.matcher(label);
            if (!matcher2.matches()) {
            	msg = "Enter  Valid Label Name";
            	errlist.add(msg);
            }
    		}
            
            if(dtype.equalsIgnoreCase("") || dtype.equalsIgnoreCase("null")) {
        		msg = "Select  Data Type";
        		errlist.add(msg);
        	}else 
        		if(dtype.length() > 30){
        			msg = "Data Type: Length Exceeded Max Length is 30";
        			errlist.add(msg);
    		}else 
        	if(dtype.length() < 1){
        		msg = "Data Type: Length Exceeded Min Length is 1";
        		errlist.add(msg);
    		}else {
        	Pattern pattern3 = Pattern.compile(dropdown);
            Matcher matcher3 = pattern3.matcher(dtype);
            if (!matcher3.matches()) {
            	msg = "Select  Valid Data Type";
            	errlist.add(msg);
            }
    		}
            
            if(minsize.equalsIgnoreCase("") || minsize.equalsIgnoreCase("null")) {
        		msg = "Enter  Minimum Size";
        		errlist.add(msg);
        	}else 
        		if(minsize.length() > 3){
        			msg = "Minimum Size: Length Exceeded Max Length is 3";
        			errlist.add(msg);
    		}else 
        	if(minsize.length() < 1){
        		msg = "Minimum Size: Length Exceeded Min Length is 1";
        		errlist.add(msg);
    		}else {
        	Pattern pattern4 = Pattern.compile(number);
            Matcher matcher4 = pattern4.matcher(minsize);
            if (!matcher4.matches()) {
            	msg = "Enter  Valid Minimum Size";
            	errlist.add(msg);
            }
    		}
            
            if(maxsize.equalsIgnoreCase("") || maxsize.equalsIgnoreCase("null")) {
        		msg = "Enter  Maximum Size";
        		errlist.add(msg);
        	}else 
        		if(maxsize.length() > 3){
        			msg = "Maximum Size: Length Exceeded Max Length is 3";
        			errlist.add(msg);
    		}else 
        	if(maxsize.length() < 1){
        		msg = "Maximum Size: Length Exceeded Min Length is 1";
        		errlist.add(msg);
    		}else {
        	Pattern pattern5 = Pattern.compile(number);
            Matcher matcher5 = pattern5.matcher(maxsize);
            if (!matcher5.matches()) {
            	msg = "Enter  Valid Maximum Size";
            	errlist.add(msg);
            }
    		}
            
            if(mandatory.equalsIgnoreCase("") || mandatory.equalsIgnoreCase("null")) {
        		msg = "Select  Mandatory";
        		errlist.add(msg);
        	}else 
        		if(mandatory.length() > 5){
        			msg = "Mandatory Size: Length Exceeded Max Length is 5";
        			errlist.add(msg);
    		}else 
        	if(mandatory.length() < 4){
        		msg = "Mandatory Size: Length Exceeded Min Length is 4";
        		errlist.add(msg);
    		}else {
        	Pattern pattern5 = Pattern.compile(dropdown);
            Matcher matcher5 = pattern5.matcher(mandatory);
            if (!matcher5.matches()) {
            	msg = "Select  Valid Mandatory";
            	errlist.add(msg);
            }
    		}
                        

              if (dtype.equals("dropdown") ) { 
                  String no1 = (String) jsonObject1.get("no1");               	
                	if(no1.equalsIgnoreCase("") || no1.equalsIgnoreCase("null")) {
                		msg = "Enter  No of Drop Values can't be blank or zero";
                		errlist.add(msg);
                	}else 
                		if(no1.length() > 2){
                			msg = "No of Drop Values: Length Exceeded Max Length is 2";
                			errlist.add(msg);
            		}else 
                	if(no1.length() < 1){
                		msg = "No of Drop Values: Length Exceeded Min Length is 1";
                		errlist.add(msg);
            		}else {
                	Pattern pattern = Pattern.compile(number);
                    Matcher matcher = pattern.matcher(no1);
                    if (!matcher.matches()) {
                    	msg = "Enter  Valid No of Drop Values";
                    	errlist.add(msg);
                    }
              }
                	for (int ii = 0; ii < Integer.parseInt(no1); ii++) {
                        String dropvalues = (String) jsonObject1.get("dropvalues");               	
                    	if(dropvalues.equalsIgnoreCase("") || dropvalues.equalsIgnoreCase("null")) {
                    		msg = "Enter  Drop Value";
                    		errlist.add(msg);
                    	}else 
                    		if(dropvalues.length() > Integer.parseInt(maxsize)){
                    			msg = "Drop Value: Length Exceeded Max Length is "+maxsize+"";
                    			errlist.add(msg);
                		}else 
                    	if(dropvalues.length() < Integer.parseInt(minsize)){
                    		msg = "Drop Value: Length Exceeded Min Length is "+minsize+"";
                    		errlist.add(msg);
                		}else {
                    	Pattern pattern = Pattern.compile(dropdown);
                        Matcher matcher = pattern.matcher(dropvalues);
                        if (!matcher.matches()) {
                        	msg = "Enter  Valid Drop Value";
                        	errlist.add(msg);
                        }
                  }	
                	}
              }else if (dtype.equals("radiobutton") ) { 	
                  String no2 = (String) jsonObject1.get("no2");               	
                	if(no2.equalsIgnoreCase("") || no2.equalsIgnoreCase("null")) {
                		msg = "Enter  No of Radio Values can't be blank or zero";
                		errlist.add(msg);
                	}else 
                		if(no2.length() > 2){
                			msg = "No of Radio Values: Length Exceeded Max Length is 2";
                			errlist.add(msg);
            		}else 
                	if(no2.length() < 1){
                		msg = "No of Radio Values: Length Exceeded Min Length is 1";
                		errlist.add(msg);
            		}else {
                	Pattern pattern = Pattern.compile(number);
                    Matcher matcher = pattern.matcher(no2);
                    if (!matcher.matches()) {
                    	msg = "Enter  Valid No of Radio Values";
                    	errlist.add(msg);
                    }
              }
                	for (int ii = 0; ii < Integer.parseInt(no2); ii++) {
                        String radiovalues = (String) jsonObject1.get("radiovalues");               	
                    	if(radiovalues.equalsIgnoreCase("") || radiovalues.equalsIgnoreCase("null")) {
                    		msg = "Enter  Radio Value";
                    		errlist.add(msg);
                    	}else 
                    		if(radiovalues.length() > Integer.parseInt(maxsize)){
                    			msg = "Radio Value: Length Exceeded Max Length is "+maxsize+"";
                    			errlist.add(msg);
                		}else 
                    	if(radiovalues.length() < Integer.parseInt(minsize)){
                    		msg = "Radio Value: Length Exceeded Min Length is "+minsize+"";
                    		errlist.add(msg);
                		}else {
                    	Pattern pattern = Pattern.compile(dropdown);
                        Matcher matcher = pattern.matcher(radiovalues);
                        if (!matcher.matches()) {
                        	msg = "Enter  Valid Radio Value";
                        	errlist.add(msg);
                        }
                  }	
                	}

              }else if (dtype.equals("file")) { 	
            	  String file_type = (String) jsonObject1.get("file_type");

            	  if(file_type.equalsIgnoreCase("") || file_type.equalsIgnoreCase("null")) {
              		msg = "Select File Type like Pdf/Jpg/Png/Jpeg";
              		errlist.add(msg);
              	}else 
              		if(file_type.length() > 12){
              			msg = "File Type : Length Exceeded Max Length is 12";
              			errlist.add(msg);
          		}else 
              	if(file_type.length() < 3){
              		msg = "File Type : Length Exceeded Min Length is 3";
              		errlist.add(msg);
//              	}else if (!file_type.equalsIgnoreCase("pdf") || !file_type.equalsIgnoreCase("PDF") || !file_type.equalsIgnoreCase("png") || !file_type.equalsIgnoreCase("PNG") || !file_type.equalsIgnoreCase("jpg") || !file_type.equalsIgnoreCase("JPG")) {  
              	}else if (!file_type.equalsIgnoreCase("pdf") && !file_type.equalsIgnoreCase("jpg|jpeg|png") ) {              		              	

              		
              		msg = "Select Valid File Type";
              		errlist.add(msg);
          		}else {
              	Pattern pattern5 = Pattern.compile(filetype);
                  Matcher matcher5 = pattern5.matcher(file_type);
                  if (!matcher5.matches()) {
     
                  	msg = "Select  Valid File Type";
                  	errlist.add(msg);
                  }
          		}                  	               
              } else if (dtype.equals("checkbox")) {
            	  String no3 = (String) jsonObject1.get("no3");               	
              	if(no3.equalsIgnoreCase("") || no3.equalsIgnoreCase("null")) {
              		msg = "Enter  No of Check Values can't be blank or zero";
              		errlist.add(msg);
              	}else 
              		if(no3.length() > 2){
              			msg = "No of Check Values: Length Exceeded Max Length is 2";
              			errlist.add(msg);
          		}else 
              	if(no3.length() < 1){
              		msg = "No of Check Values: Length Exceeded Min Length is 1";
              		errlist.add(msg);
          		}else {
              	Pattern pattern = Pattern.compile(number);
                  Matcher matcher = pattern.matcher(no3);
                  if (!matcher.matches()) {
                  	msg = "Enter  Valid No of Check Values";
                  	errlist.add(msg);
                  }
            }
              	for (int ii = 0; ii < Integer.parseInt(no3); ii++) {
                      String checkvalues = (String) jsonObject1.get("checkvalues");               	
                  	if(checkvalues.equalsIgnoreCase("") || checkvalues.equalsIgnoreCase("null")) {
                  		msg = "Enter  Check Value";
                  		errlist.add(msg);
                  	}else 
                  		if(checkvalues.length() > Integer.parseInt(maxsize)){
                  			msg = "Check Value: Length Exceeded Max Length is "+maxsize+"";
                  			errlist.add(msg);
              		}else 
                  	if(checkvalues.length() < Integer.parseInt(minsize)){
                  		msg = "Check Value: Length Exceeded Min Length is "+minsize+"";
                  		errlist.add(msg);
              		}else {
                  	Pattern pattern = Pattern.compile(dropdown);
                      Matcher matcher = pattern.matcher(checkvalues);
                      if (!matcher.matches()) {
                      	msg = "Enter  Valid Check Value";
                      	errlist.add(msg);
                      }
                }	
              	}

                                         	  
              }
        }
		}catch(Exception e) {
			logger.error("Exception in FormStructureValidation::validate::" + e.getMessage());
	        logger.error("Exception in FormStructureValidation::validate::" + e.getCause());
	        logger.error("Exception in FormStructureValidation::validate::" + e.getClass() + "::" + e.getLocalizedMessage());
		}

	return errlist;
}
	
}
