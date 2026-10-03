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
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

import com.nic.svpnpa.model.JsonBean;
import com.nic.svpnpa.service.SVPNPAService;

@Component
public class GenericValidator implements Validator{
	
	private static final Logger logger = LogManager.getLogger(GenericValidator.class);

	@Override
	public boolean supports(Class<?> clazz) {
		return JsonBean.class.isAssignableFrom(clazz);		
	}

	@Override
	public void validate(Object target, Errors errors) {
		JsonBean bean = (JsonBean) target;
		
	    String dropdown = "^[a-zA-Z ]*$"; 
	    String specialcombo = "^[0-9]*$";
	    String radiobutton = "^[a-zA-Z]*$";
	    String checkbox = "^[a-zA-Z]*$";
	    String textarea = "^[0-9a-zA-Z ,.*-/()&$#@(\\.[_\"\']+) ]+$";
	    String password = "^[0-9a-zA-Z *&$#@ ]+$";
	    String email = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Zruna-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	    String date = "^[0-9]{4}[-]?[//]?[0-9]{2}[-]?[//]?[0-9]{2}$";
	    String text = "^[0-9a-zA-Z ,.*-/()&$#@(\\.[_\"\']+) ]+$";
	    String number = "^[0-9]*$";
//	    String mobile = "^[0-9]*$";
	    String mobile = "^[6-7]{1}[0-9]{9}$";
	    String aadhaarno = "^[0-9]*$";
	    String panno = "^[A-Z]{5}?[//]?[0-9]{4}?[//]?[A-Z]{1}$";
		

	    
		int no = Integer.parseInt(bean.getFields());
		String jsonString = bean.getJsonData().toString();
		JSONParser parser = new JSONParser();
        Object obj = null;
		try {
			obj = parser.parse(bean.getJsonFields());
		} catch (ParseException e) {
			e.printStackTrace();
		}
        JSONArray array = (JSONArray) obj;
        
        JSONParser parser1 = new JSONParser();
        Object obj3 = null;
		try {
			obj3 = parser1.parse(jsonString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
        JSONArray array1 = (JSONArray) obj3;            
        int y = 0;
        try {
        for (int i = 0; i < no; i++) {
     	   JSONObject jsonObject1 = (JSONObject) array.get(i);
            JSONObject obj1 = (JSONObject) array1.get(i);
            String data = null;                   
            String dtype = (String) jsonObject1.get("dtype");
            String label = (String) jsonObject1.get("label");

            String minsize = (String) jsonObject1.get("minsize");
            String maxsize = (String) jsonObject1.get("maxsize");

            String mandatory = (String) jsonObject1.get("mandatory");

            String file_type = (String) jsonObject1.get("file_type");

            int len = 0;
            try {
            data = (String) obj1.get(label);
            len = data.length();
            }catch(Exception e) {
             e.printStackTrace();
            }
            if(mandatory.equalsIgnoreCase("true")) {
              if (dtype.equals("dropdown") ) { 
                	data = (String) obj1.get(label);
                	ValidationUtils.rejectIfEmptyOrWhitespace(errors, ""+dtype+"", ""+dtype+".required", " *select "+label+"");                  	
                	if(data.length() > Integer.parseInt(maxsize)){
            			errors.rejectValue(""+dtype+"", "Length Exceeded, Max Length is "+maxsize+"");
            		}
                	if(data.length() < Integer.parseInt(minsize)){
            			errors.rejectValue(""+dtype+"", "Length Exceeded, Min Length is "+minsize+"");
            		}
                	Pattern pattern = Pattern.compile(dropdown);
                    Matcher matcher = pattern.matcher(data);
                    if (!matcher.matches()) {
                        errors.rejectValue(""+dtype+"", "select  Valid "+label+"");
                    }
              }else if (dtype.equals("radiobutton") ) { 	
            	  data = (String) obj1.get(label);
              	ValidationUtils.rejectIfEmptyOrWhitespace(errors, ""+dtype+"", ""+dtype+".required", " *select "+label+"");                  	
              	if(data.length() > Integer.parseInt(maxsize)){
          			errors.rejectValue(""+dtype+"", "Length Exceeded, Max Length is "+maxsize+"");
          		}
              	if(data.length() < Integer.parseInt(minsize)){
          			errors.rejectValue(""+dtype+"", "Length Exceeded, Min Length is "+minsize+"");
          		}
              	Pattern pattern = Pattern.compile(radiobutton);
                  Matcher matcher = pattern.matcher(data);
                  if (!matcher.matches()) {
                      errors.rejectValue(""+dtype+"", "select  Valid "+label+"");
                }
              }else if (dtype.equals("textarea") ) { 	
            	  data = (String) obj1.get(label);
              	ValidationUtils.rejectIfEmptyOrWhitespace(errors, ""+dtype+"", ""+dtype+".required", " *select "+label+"");                  	
              	if(data.length() > Integer.parseInt(maxsize)){
          			errors.rejectValue(""+dtype+"", "Length Exceeded, Max Length is "+maxsize+"");
          		}
              	if(data.length() < Integer.parseInt(minsize)){
          			errors.rejectValue(""+dtype+"", "Length Exceeded, Min Length is "+minsize+"");
          		}
              	Pattern pattern = Pattern.compile(textarea);
                  Matcher matcher = pattern.matcher(data);
                  if (!matcher.matches()) {
                      errors.rejectValue(""+dtype+"", "Enter  Valid "+label+"");
                } 
              }else if (dtype.equals("password") ) { 	
            	  data = (String) obj1.get(label);
              	ValidationUtils.rejectIfEmptyOrWhitespace(errors, ""+dtype+"", ""+dtype+".required", " *select "+label+"");                  	
              	if(data.length() > Integer.parseInt(maxsize)){
          			errors.rejectValue(""+dtype+"", "Length Exceeded, Max Length is "+maxsize+"");
          		}
              	if(data.length() < Integer.parseInt(minsize)){
          			errors.rejectValue(""+dtype+"", "Length Exceeded, Min Length is "+minsize+"");
          		}
              	Pattern pattern = Pattern.compile(password);
                  Matcher matcher = pattern.matcher(data);
                  if (!matcher.matches()) {
                      errors.rejectValue(""+dtype+"", "Enter  Valid "+label+"");
                }  
              }else if (dtype.equals("email") ) { 	
            	  data = (String) obj1.get(label);
              	ValidationUtils.rejectIfEmptyOrWhitespace(errors, ""+dtype+"", ""+dtype+".required", " *select "+label+"");                  	
              	if(data.length() > Integer.parseInt(maxsize)){
          			errors.rejectValue(""+dtype+"", "Length Exceeded, Max Length is "+maxsize+"");
          		}
              	if(data.length() < Integer.parseInt(minsize)){
          			errors.rejectValue(""+dtype+"", "Length Exceeded, Min Length is "+minsize+"");
          		}
              	Pattern pattern = Pattern.compile(email);
                  Matcher matcher = pattern.matcher(data);
                  if (!matcher.matches()) {
                      errors.rejectValue(""+dtype+"", "Enter  Valid "+label+"");
                } 
              }else if (dtype.equals("date") ) { 	
            	  data = (String) obj1.get(label);
              	ValidationUtils.rejectIfEmptyOrWhitespace(errors, ""+dtype+"", ""+dtype+".required", " *select "+label+"");                  	
              	if(data.length() > Integer.parseInt(maxsize)){
          			errors.rejectValue(""+dtype+"", "Length Exceeded, Max Length is "+maxsize+"");
          		}
              	if(data.length() < Integer.parseInt(minsize)){
          			errors.rejectValue(""+dtype+"", "Length Exceeded, Min Length is "+minsize+"");
          		}
              	Pattern pattern = Pattern.compile(date);
                  Matcher matcher = pattern.matcher(data);
                  if (!matcher.matches()) {
                      errors.rejectValue(""+dtype+"", "Enter  Valid "+label+"");
                }
              }else if (dtype.equals("number") ) { 	
            	  data = (String) obj1.get(label);
              	ValidationUtils.rejectIfEmptyOrWhitespace(errors, ""+dtype+"", ""+dtype+".required", " *select "+label+"");                  	
              	if(data.length() > Integer.parseInt(maxsize)){
          			errors.rejectValue(""+dtype+"", "Length Exceeded, Max Length is "+maxsize+"");
          		}
              	if(data.length() < Integer.parseInt(minsize)){
          			errors.rejectValue(""+dtype+"", "Length Exceeded, Min Length is "+minsize+"");
          		}
              	Pattern pattern = Pattern.compile(number);
                  Matcher matcher = pattern.matcher(data);
                  if (!matcher.matches()) {
                      errors.rejectValue(""+dtype+"", "Enter  Valid "+label+"");
                }   
              }else if (dtype.equals("mobile") ) { 	
            	  data = (String) obj1.get(label);
              	ValidationUtils.rejectIfEmptyOrWhitespace(errors, ""+dtype+"", ""+dtype+".required", " *select "+label+"");                  	
              	if(data.length() > Integer.parseInt(maxsize)){
          			errors.rejectValue(""+dtype+"", "Length Exceeded, Max Length is "+maxsize+"");
          		}
              	if(data.length() < Integer.parseInt(minsize)){
          			errors.rejectValue(""+dtype+"", "Length Exceeded, Min Length is "+minsize+"");
          		}
              	if(data.equalsIgnoreCase("1111111111") || data.equalsIgnoreCase("2222222222") || data.equalsIgnoreCase("3333333333") || data.equalsIgnoreCase("4444444444") || data.equalsIgnoreCase("5555555555") || data.equalsIgnoreCase("6666666666") || data.equalsIgnoreCase("7777777777") || data.equalsIgnoreCase("8888888888") || data.equalsIgnoreCase("9999999999") || data.equalsIgnoreCase("0000000000")) {
              		errors.rejectValue(""+dtype+"", "Enter  Valid "+label+"");	
              	}
              	Pattern pattern = Pattern.compile(mobile);
                  Matcher matcher = pattern.matcher(data);
                  if (!matcher.matches()) {
                      errors.rejectValue(""+dtype+"", "Enter  Valid "+label+"");
                }  
              }else if (dtype.equals("panno") ) { 	
            	  data = (String) obj1.get(label);
              	ValidationUtils.rejectIfEmptyOrWhitespace(errors, ""+dtype+"", ""+dtype+".required", " *select "+label+"");                  	
              	if(data.length() > Integer.parseInt(maxsize)){
          			errors.rejectValue(""+dtype+"", "Length Exceeded, Max Length is "+maxsize+"");
          		}
              	if(data.length() < Integer.parseInt(minsize)){
          			errors.rejectValue(""+dtype+"", "Length Exceeded, Min Length is "+minsize+"");
          		}
              	Pattern pattern = Pattern.compile(panno);
                  Matcher matcher = pattern.matcher(data);
                  if (!matcher.matches()) {
                      errors.rejectValue(""+dtype+"", "Enter  Valid "+label+"");
                }   
              }else if (dtype.equals("aadhaarno") ) { 	
            	  data = (String) obj1.get(label);
              	ValidationUtils.rejectIfEmptyOrWhitespace(errors, ""+dtype+"", ""+dtype+".required", " *select "+label+"");                  	
              	if(data.length() > Integer.parseInt(maxsize)){
          			errors.rejectValue(""+dtype+"", "Length Exceeded, Max Length is "+maxsize+"");
          		}
              	if(data.length() < Integer.parseInt(minsize)){
          			errors.rejectValue(""+dtype+"", "Length Exceeded, Min Length is "+minsize+"");
          		}

              	VerhoeffAlgorithm va = new VerhoeffAlgorithm();
      			if (!va.validateVerhoeff(data)) {      				
      				errors.rejectValue(""+dtype+"", "Invalid Aadhar Number,Please Enter the Correct Aadhar No.");
      			}
              }else if (dtype.equals("text")) {

            	  data = (String) obj1.get(label);
              	ValidationUtils.rejectIfEmptyOrWhitespace(errors, ""+dtype+"", ""+dtype+".required", " *select "+label+"");                  	
              	if(len > Integer.parseInt(maxsize)){

          			errors.rejectValue(""+dtype+"", "Length Exceeded, Max Length is "+maxsize+"");
          		}
              	if(data.length() < Integer.parseInt(minsize)){
          			errors.rejectValue(""+dtype+"", "Length Exceeded, Min Length is "+minsize+"");
          		}              	
              	Pattern pattern = Pattern.compile(text);
                  Matcher matcher = pattern.matcher(data);
                  if (!matcher.matches()) {
                      errors.rejectValue(""+dtype+"", "Enter  Valid "+label+"");
                }
              }else if (dtype.equals("file")) { 	
            	  data = (String) obj1.get(label);
              	ValidationUtils.rejectIfEmptyOrWhitespace(errors, ""+dtype+"", ""+dtype+".required", " *select "+label+"");
              	MultipartFile multipartFile = null;
              	long filesize = 0,filesize1 = 0,filesize2 = 0;
            	if(y==0) {
            	multipartFile = bean.getFile0();
            	filesize = bean.getFile0().getSize();
            	} else if(y==1) {
                	multipartFile = bean.getFile1();
                	filesize = bean.getFile1().getSize();
                }else if(y==2) {
                	multipartFile = bean.getFile2();
                	filesize = bean.getFile2().getSize();
                }else if(y==3) {
                	multipartFile = bean.getFile3();
                	filesize = bean.getFile3().getSize();
                }
            	String filename = multipartFile.getOriginalFilename(); 
            	if(!filename.equalsIgnoreCase("")) {
            	int index = filename.lastIndexOf('.');
            	String extension = "";
                if(index > 0) {
                  extension = filename.substring(index + 1);
                  if(file_type == "pdf"){   
                      if ("pdf".equalsIgnoreCase(extension) || "PDF".equalsIgnoreCase(extension) || "png".equalsIgnoreCase(extension) || "PNG".equalsIgnoreCase(extension) || "jpg".equalsIgnoreCase(extension) || "JPG".equalsIgnoreCase(extension)) {              	  
                      }else{
                           ValidationUtils.rejectIfEmptyOrWhitespace(errors, ""+dtype+"", ""+dtype+".required", "* Invalid File");
                           errors.rejectValue(""+dtype+"", "Invalid  "+label+"");
                      }
                      filesize1 = filesize/1024;
                      filesize2 = filesize1/1024;
                      if(filesize2>1) {
                    	  errors.rejectValue(""+dtype+"", "File size exceeds 1 MB, "+label+"");
                      }else if(filesize<=0) {
                    	  errors.rejectValue(""+dtype+"", "File size should not 0 KB, "+label+"");
                      }
                      
                  }else{
                  if ("jpeg".equalsIgnoreCase(extension) || "JPEG".equalsIgnoreCase(extension) || "png".equalsIgnoreCase(extension) || "PNG".equalsIgnoreCase(extension) || "jpg".equalsIgnoreCase(extension) || "JPG".equalsIgnoreCase(extension)) {              	  
                  }else{
                       ValidationUtils.rejectIfEmptyOrWhitespace(errors, ""+dtype+"", ""+dtype+".required", "* Invalid File");
                       errors.rejectValue(""+dtype+"", "Invalid  "+label+"");
                  }
                  filesize1 = filesize/1024;
                  if(filesize<=0) {
                	  errors.rejectValue(""+dtype+"", "File Size should not 0 KB, "+label+"");
                  }else if(filesize1>200) {
                	  errors.rejectValue(""+dtype+"", "File size exceeds 200 KB, "+label+"");
                  }
                }
                }
                  
                }
              	              	              
                
              y++;
              } else if (dtype.equals("checkbox")) {
                obj1 = (JSONObject) array1.get(i);        
                ArrayList<String> list = (ArrayList<String>) obj1.get(label);
                if(list.size()>0) {
                String array2[] = new String[list.size()];
                for (int j = 0; j < list.size(); j++) {
                    array2[j] = list.get(j);
                    data = (String) array2[j];
                    
                  	ValidationUtils.rejectIfEmptyOrWhitespace(errors, ""+dtype+"", ""+dtype+".required", " *select "+label+"");                  	
                  	if(data.length() > Integer.parseInt(maxsize)){
              			errors.rejectValue(""+dtype+"", "Length Exceeded, Max Length is "+maxsize+"");
              		}
                  	if(data.length() < Integer.parseInt(minsize)){
              			errors.rejectValue(""+dtype+"", "Length Exceeded, Min Length is "+minsize+"");
              		}
                  	Pattern pattern = Pattern.compile(checkbox);
                      Matcher matcher = pattern.matcher(data);
                      if (!matcher.matches()) {
                          errors.rejectValue(""+dtype+"", "Select  Valid "+label+"");
                    } 
                }
              }else {             	 
            	  errors.rejectValue(""+dtype+"", "Select  "+label+""); 
              }
            } else if (dtype.equals("specialcombo")) {
                obj1 = (JSONObject) array1.get(i);

                data = (String) obj1.get("stateCode");                     
                
              	ValidationUtils.rejectIfEmptyOrWhitespace(errors, ""+dtype+"", ""+dtype+".required", " *select "+label+"");                  	
              	if(data.length() > 5){
          			errors.rejectValue(""+dtype+"", "State Code Length Exceeded, Max Length is 5");
          		}
              	if(data.length() < Integer.parseInt(minsize)){
          			errors.rejectValue(""+dtype+"", "State Code Length Exceeded, Min Length is 1");
          		}
              	Pattern pattern = Pattern.compile(number);
                  Matcher matcher = pattern.matcher(data);
                  if (!matcher.matches()) {
                      errors.rejectValue(""+dtype+"", "Select Valid State");
                } 
                
                
                data = (String) obj1.get("distCode");                       
                ValidationUtils.rejectIfEmptyOrWhitespace(errors, ""+dtype+"", ""+dtype+".required", " *select "+label+"");                  	
              	if(data.length() > 5){
          			errors.rejectValue(""+dtype+"", "District Code Length Exceeded, Max Length is 5");
          		}
              	if(data.length() < Integer.parseInt(minsize)){
          			errors.rejectValue(""+dtype+"", "District Code Length Exceeded, Min Length is 1");
          		}
              	Pattern pattern1 = Pattern.compile(number);
                  Matcher matcher1 = pattern1.matcher(data);
                  if (!matcher1.matches()) {
                      errors.rejectValue(""+dtype+"", "Select Valid District");
                }
                data = (String) obj1.get("mandalCode");               
                ValidationUtils.rejectIfEmptyOrWhitespace(errors, ""+dtype+"", ""+dtype+".required", " *select "+label+"");                  	
              	if(data.length() > 5){
          			errors.rejectValue(""+dtype+"", "Mandal Code Length Exceeded, Max Length is 5");
          		}
              	if(data.length() < Integer.parseInt(minsize)){
          			errors.rejectValue(""+dtype+"", "Mandal Code Length Exceeded, Min Length is 1");
          		}
              	Pattern pattern2 = Pattern.compile(number);
                  Matcher matcher2 = pattern2.matcher(data);
                  if (!matcher2.matches()) {
                      errors.rejectValue(""+dtype+"", "Select Valid Mandal");
                }                        
                data = (String) obj1.get("villCode");           
                ValidationUtils.rejectIfEmptyOrWhitespace(errors, ""+dtype+"", ""+dtype+".required", " *select "+label+"");                  	
              	if(data.length() > 10){
          			errors.rejectValue(""+dtype+"", "Village Code Length Exceeded, Max Length is 10");
          		}
              	if(data.length() < Integer.parseInt(minsize)){
          			errors.rejectValue(""+dtype+"", "Village Code Length Exceeded, Min Length is 1");
          		}
              	Pattern pattern3 = Pattern.compile(number);
                  Matcher matcher3 = pattern3.matcher(data);
                  if (!matcher3.matches()) {
                      errors.rejectValue(""+dtype+"", "Select Valid Village");
                }                         
            }
                
              }else {
//-------------Mandatory-False------------
            	  if (dtype.equals("dropdown") ) { 
                  	data = (String) obj1.get(label);
                  	if(data != "" && data !=null) {                  	
                  	if(data.length() > Integer.parseInt(maxsize)){
              			errors.rejectValue(""+dtype+"", "Length Exceeded, Max Length is "+maxsize+"");
              		}
                  	if(data.length() < Integer.parseInt(minsize)){
              			errors.rejectValue(""+dtype+"", "Length Exceeded, Min Length is "+minsize+"");
              		}
                  	Pattern pattern = Pattern.compile(dropdown);
                      Matcher matcher = pattern.matcher(data);
                      if (!matcher.matches()) {
                          errors.rejectValue(""+dtype+"", "select  Valid "+label+"");
                      }
            	  }
                }else if (dtype.equals("radiobutton") ) { 	
              	  data = (String) obj1.get(label);
                if(data != "" && data != null) {                  	
                	if(data.length() > Integer.parseInt(maxsize)){
            			errors.rejectValue(""+dtype+"", "Length Exceeded, Max Length is "+maxsize+"");
            		}
                	if(data.length() < Integer.parseInt(minsize)){
            			errors.rejectValue(""+dtype+"", "Length Exceeded, Min Length is "+minsize+"");
            		}
                	Pattern pattern = Pattern.compile(radiobutton);
                    Matcher matcher = pattern.matcher(data);
                    if (!matcher.matches()) {
                        errors.rejectValue(""+dtype+"", "select  Valid "+label+"");
                  }
                }
                }else if (dtype.equals("textarea") ) { 	
              	  data = (String) obj1.get(label);
                	if(data !="" && data !=null) {                  	
                	if(data.length() > Integer.parseInt(maxsize)){
            			errors.rejectValue(""+dtype+"", "Length Exceeded, Max Length is "+maxsize+"");
            		}
                	if(data.length() < Integer.parseInt(minsize)){
            			errors.rejectValue(""+dtype+"", "Length Exceeded, Min Length is "+minsize+"");
            		}
                	Pattern pattern = Pattern.compile(textarea);
                    Matcher matcher = pattern.matcher(data);
                    if (!matcher.matches()) {
                        errors.rejectValue(""+dtype+"", "Enter  Valid "+label+"");
                  } 
                }
                }else if (dtype.equals("password") ) { 	
              	  data = (String) obj1.get(label);
                	if(data !="" && data !=null) {                  	
                	if(data.length() > Integer.parseInt(maxsize)){
            			errors.rejectValue(""+dtype+"", "Length Exceeded, Max Length is "+maxsize+"");
            		}
                	if(data.length() < Integer.parseInt(minsize)){
            			errors.rejectValue(""+dtype+"", "Length Exceeded, Min Length is "+minsize+"");
            		}
                	Pattern pattern = Pattern.compile(password);
                    Matcher matcher = pattern.matcher(data);
                    if (!matcher.matches()) {
                        errors.rejectValue(""+dtype+"", "Enter  Valid "+label+"");
                  } 
                }
                }else if (dtype.equals("email") ) { 	
              	  data = (String) obj1.get(label);
              	if(data !="" && data !=null) {                  	
                	if(data.length() > Integer.parseInt(maxsize)){
            			errors.rejectValue(""+dtype+"", "Length Exceeded, Max Length is "+maxsize+"");
            		}
                	if(data.length() < Integer.parseInt(minsize)){
            			errors.rejectValue(""+dtype+"", "Length Exceeded, Min Length is "+minsize+"");
            		}
                	Pattern pattern = Pattern.compile(email);
                    Matcher matcher = pattern.matcher(data);
                    if (!matcher.matches()) {
                        errors.rejectValue(""+dtype+"", "Enter  Valid "+label+"");
                  } 
              	}
                }else if (dtype.equals("date") ) { 	
              	  data = (String) obj1.get(label);
              	if(data !="" && data !=null) {                  	
                	if(data.length() > Integer.parseInt(maxsize)){
            			errors.rejectValue(""+dtype+"", "Length Exceeded, Max Length is "+maxsize+"");
            		}
                	if(data.length() < Integer.parseInt(minsize)){
            			errors.rejectValue(""+dtype+"", "Length Exceeded, Min Length is "+minsize+"");
            		}
                	Pattern pattern = Pattern.compile(date);
                    Matcher matcher = pattern.matcher(data);
                    if (!matcher.matches()) {
                        errors.rejectValue(""+dtype+"", "Enter  Valid "+label+"");
                  }
              	}
                }else if (dtype.equals("number") ) { 	
              	  data = (String) obj1.get(label);
              	if(data !="" && data !=null) {                  	
                	if(data.length() > Integer.parseInt(maxsize)){
            			errors.rejectValue(""+dtype+"", "Length Exceeded, Max Length is "+maxsize+"");
            		}
                	if(data.length() < Integer.parseInt(minsize)){
            			errors.rejectValue(""+dtype+"", "Length Exceeded, Min Length is "+minsize+"");
            		}
                	Pattern pattern = Pattern.compile(number);
                    Matcher matcher = pattern.matcher(data);
                    if (!matcher.matches()) {
                        errors.rejectValue(""+dtype+"", "Enter  Valid "+label+"");
                  } 
              	}
                }else if (dtype.equals("mobile") ) { 	
              	  data = (String) obj1.get(label);
              	if(data !="" && data !=null) {                  	
                	if(data.length() > Integer.parseInt(maxsize)){
            			errors.rejectValue(""+dtype+"", "Length Exceeded, Max Length is "+maxsize+"");
            		}
                	if(data.length() < Integer.parseInt(minsize)){
            			errors.rejectValue(""+dtype+"", "Length Exceeded, Min Length is "+minsize+"");
            		}
                	if(data.equalsIgnoreCase("1111111111") || data.equalsIgnoreCase("2222222222") || data.equalsIgnoreCase("3333333333") || data.equalsIgnoreCase("4444444444") || data.equalsIgnoreCase("5555555555") || data.equalsIgnoreCase("6666666666") || data.equalsIgnoreCase("7777777777") || data.equalsIgnoreCase("8888888888") || data.equalsIgnoreCase("9999999999") || data.equalsIgnoreCase("0000000000")) {
                		errors.rejectValue(""+dtype+"", "Enter  Valid "+label+"");	
                	}
                	Pattern pattern = Pattern.compile(mobile);
                    Matcher matcher = pattern.matcher(data);
                    if (!matcher.matches()) {
                        errors.rejectValue(""+dtype+"", "Enter  Valid "+label+"");
                  } 
              	}
                }else if (dtype.equals("panno") ) { 	
              	  data = (String) obj1.get(label);
              	if(data !="" && data !=null) {                  	
                	if(data.length() > Integer.parseInt(maxsize)){
            			errors.rejectValue(""+dtype+"", "Length Exceeded, Max Length is "+maxsize+"");
            		}
                	if(data.length() < Integer.parseInt(minsize)){
            			errors.rejectValue(""+dtype+"", "Length Exceeded, Min Length is "+minsize+"");
            		}
                	Pattern pattern = Pattern.compile(panno);
                    Matcher matcher = pattern.matcher(data);
                    if (!matcher.matches()) {
                        errors.rejectValue(""+dtype+"", "Enter  Valid "+label+"");
                  }  
              	}
                }else if (dtype.equals("aadhaarno") ) { 	
              	  data = (String) obj1.get(label);
              	if(data !="" && data !=null) {                  	
                	if(data.length() > Integer.parseInt(maxsize)){
            			errors.rejectValue(""+dtype+"", "Length Exceeded, Max Length is "+maxsize+"");
            		}
                	if(data.length() < Integer.parseInt(minsize)){
            			errors.rejectValue(""+dtype+"", "Length Exceeded, Min Length is "+minsize+"");
            		}
                	VerhoeffAlgorithm va = new VerhoeffAlgorithm();

          			if (!va.validateVerhoeff(data)) {      				
          				errors.rejectValue(""+dtype+"", "Invalid Aadhar Number,Please Enter the Correct Aadhar No.");
          			}
              	}
                }else if (dtype.equals("text")) { 	
              	  data = (String) obj1.get(label);
              	if(data !="" && data !=null) {                  	
                	if(data.length() > Integer.parseInt(maxsize)){
            			errors.rejectValue(""+dtype+"", "Length Exceeded, Max Length is "+maxsize+"");
            		}
                	if(data.length() < Integer.parseInt(minsize)){
            			errors.rejectValue(""+dtype+"", "Length Exceeded, Min Length is "+minsize+"");
            		}              	
                	Pattern pattern = Pattern.compile(text);
                    Matcher matcher = pattern.matcher(data);
                    if (!matcher.matches()) {
                        errors.rejectValue(""+dtype+"", "Enter  Valid "+label+"");
                  }
              	}
                }else if (dtype.equals("file")) { 	
              	  data = (String) obj1.get(label);
              	if(data !="" && data !=null) {
                	MultipartFile multipartFile = null;
                	long filesize = 0,filesize1 = 0,filesize2 = 0;
              	if(y==0) {
              	multipartFile = bean.getFile0();
              	filesize = bean.getFile0().getSize();
              	} else if(y==1) {
                  	multipartFile = bean.getFile1();
                  	filesize = bean.getFile1().getSize();
                  }else if(y==2) {
                  	multipartFile = bean.getFile2();
                  	filesize = bean.getFile2().getSize();
                  }else if(y==3) {
                  	multipartFile = bean.getFile3();
                  	filesize = bean.getFile3().getSize();
                  }
              	String filename = multipartFile.getOriginalFilename(); 
              	if(!filename.equalsIgnoreCase("")) {
              	int index = filename.lastIndexOf('.');
              	String extension = "";
                  if(index > 0) {
                    extension = filename.substring(index + 1);
                    if(file_type == "pdf"){   
                        if ("pdf".equalsIgnoreCase(extension) || "PDF".equalsIgnoreCase(extension) || "png".equalsIgnoreCase(extension) || "PNG".equalsIgnoreCase(extension) || "jpg".equalsIgnoreCase(extension) || "JPG".equalsIgnoreCase(extension)) {              	  
                        }else{
                             ValidationUtils.rejectIfEmptyOrWhitespace(errors, ""+dtype+"", ""+dtype+".required", "* Invalid File");
                             errors.rejectValue(""+dtype+"", "Invalid  "+label+"");
                        }
                        filesize1 = filesize/1024;
                        filesize2 = filesize1/1024;
                        if(filesize2>1) {
                      	  errors.rejectValue(""+dtype+"", "File size exceeds 1 MB, "+label+"");
                        }else if(filesize<=0) {
                      	  errors.rejectValue(""+dtype+"", "File size should not 0 KB, "+label+"");
                        }
                        
                    }else{
                    if ("jpeg".equalsIgnoreCase(extension) || "JPEG".equalsIgnoreCase(extension) || "png".equalsIgnoreCase(extension) || "PNG".equalsIgnoreCase(extension) || "jpg".equalsIgnoreCase(extension) || "JPG".equalsIgnoreCase(extension)) {              	  
                    }else{
                         ValidationUtils.rejectIfEmptyOrWhitespace(errors, ""+dtype+"", ""+dtype+".required", "* Invalid File");
                         errors.rejectValue(""+dtype+"", "Invalid  "+label+"");
                    }
                    filesize1 = filesize/1024;
                    if(filesize<=0) {
                  	  errors.rejectValue(""+dtype+"", "File Size should not 0 KB, "+label+"");
                    }else if(filesize1>200) {
                  	  errors.rejectValue(""+label+"", "File size exceeds 200 KB, "+label+"");
                    }
                  }
                  }
                    
                  }
                	              	              
              	}  
                y++;
              	
                } else if (dtype.equals("checkbox")) {
                  obj1 = (JSONObject) array1.get(i);        
                  ArrayList<String> list = (ArrayList<String>) obj1.get(label);
                  if(list.size()>0) {
                  String array2[] = new String[list.size()];
                  for (int j = 0; j < list.size(); j++) {
                      array2[j] = list.get(j);
                      data = (String) array2[j];
                      
                      if(data !="" && data !=null) {                  	
                    	if(data.length() > Integer.parseInt(maxsize)){
                			errors.rejectValue(""+dtype+"", "Length Exceeded, Max Length is "+maxsize+"");
                		}
                    	if(data.length() < Integer.parseInt(minsize)){
                			errors.rejectValue(""+dtype+"", "Length Exceeded, Min Length is "+minsize+"");
                		}
                    	Pattern pattern = Pattern.compile(checkbox);
                        Matcher matcher = pattern.matcher(data);
                        if (!matcher.matches()) {
                            errors.rejectValue(""+dtype+"", "Select  Valid "+label+"");
                      }                        
                  }
                  }
                }else {             	 
              	  errors.rejectValue(""+dtype+"", "Select  "+label+""); 
                }
              } else if (dtype.equals("specialcombo")) {
                  obj1 = (JSONObject) array1.get(i);

                  data = (String) obj1.get("stateCode");                     
                  
                  if(data !="" && data !=null) {                  	
                	if(data.length() > 5){
            			errors.rejectValue(""+dtype+"", "State Code Length Exceeded, Max Length is 5");
            		}
                	if(data.length() < Integer.parseInt(minsize)){
            			errors.rejectValue(""+dtype+"", "State Code Length Exceeded, Min Length is 1");
            		}
                	Pattern pattern = Pattern.compile(number);
                    Matcher matcher = pattern.matcher(data);
                    if (!matcher.matches()) {
                        errors.rejectValue(""+dtype+"", "Select Valid State");
                  } 
              }
                  
                  data = (String) obj1.get("distCode");                       
                  if(data !="" && data !=null) {                  	
                	if(data.length() > 5){
            			errors.rejectValue(""+dtype+"", "District Code Length Exceeded, Max Length is 5");
            		}
                	if(data.length() < Integer.parseInt(minsize)){
            			errors.rejectValue(""+dtype+"", "District Code Length Exceeded, Min Length is 1");
            		}
                	Pattern pattern1 = Pattern.compile(number);
                    Matcher matcher1 = pattern1.matcher(data);
                    if (!matcher1.matches()) {
                        errors.rejectValue(""+dtype+"", "Select Valid District");
                  }
              }
                  data = (String) obj1.get("mandalCode");               
                  if(data !="" && data !=null) {                  	
                	if(data.length() > 5){
            			errors.rejectValue(""+dtype+"", "Mandal Code Length Exceeded, Max Length is 5");
            		}
                	if(data.length() < Integer.parseInt(minsize)){
            			errors.rejectValue(""+dtype+"", "Mandal Code Length Exceeded, Min Length is 1");
            		}
                	Pattern pattern2 = Pattern.compile(number);
                    Matcher matcher2 = pattern2.matcher(data);
                    if (!matcher2.matches()) {
                        errors.rejectValue(""+dtype+"", "Select Valid Mandal");
                  }
                  }
                  data = (String) obj1.get("villCode");           
                  if(data !="" && data !=null) {                  	
                	if(data.length() > 10){
            			errors.rejectValue(""+dtype+"", "Village Code Length Exceeded, Max Length is 10");
            		}
                	if(data.length() < Integer.parseInt(minsize)){
            			errors.rejectValue(""+dtype+"", "Village Code Length Exceeded, Min Length is 1");
            		}
                	Pattern pattern3 = Pattern.compile(number);
                    Matcher matcher3 = pattern3.matcher(data);
                    if (!matcher3.matches()) {
                        errors.rejectValue(""+dtype+"", "Select Valid Village");
                  } 
                 }
                    
              }
	  
            	  
            	  
              }
        }
        
		
		      
        }catch(Exception e) {
        	logger.error("Exception in GenericValidator::validate::" + e.getMessage());
	        logger.error("Exception in GenericValidator::validate::" + e.getCause());
	        logger.error("Exception in GenericValidator::validate::" + e.getClass() + "::" + e.getLocalizedMessage());
        }
		
	}
		
    }
