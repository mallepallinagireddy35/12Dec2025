package com.nic.svpnpa.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.nic.svpnpa.util.Retrieving_Json_Object;
import com.nic.svpnpa.dao.GenericDao;
import com.nic.svpnpa.dao.LoginDao;
import com.nic.svpnpa.model.CaptchaDto;
import com.nic.svpnpa.model.CommonBean;
import com.nic.svpnpa.model.JsonBean;
import com.nic.svpnpa.model.WebMenuBean;
import com.nic.svpnpa.security.jwt.JwtUtils;
import com.nic.svpnpa.security.services.UserDetailsImpl;
import com.nic.svpnpa.service.SVPNPAService;
import com.nic.svpnpa.sms.SmsService;
import com.nic.svpnpa.util.FormStructureValidation;
import com.nic.svpnpa.util.GenericValidationService;
import com.nic.svpnpa.util.GenericValidator;
import com.nic.svpnpa.util.HashEncrypter;
import com.nic.svpnpa.util.SortDistByValue;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.*;
import jakarta.validation.Valid;

/**
 * @author NAGIREDDY
 *
 */

@Controller
public class GenericController {

	private static final Logger logger = LogManager.getLogger(GenericController.class);
	
	@Autowired
	GenericDao genericDao;

	@Autowired
	CaptchaController  captchaController;
	
//------------------------------------------------------------
	@Autowired
	private LoginDao loginDaoImpl;
	
	@Autowired
	LoginUserDetailsController loginController;
	
	@Autowired
	private SVPNPAService service;
	
	private String lang = "E";
	
	String role;
	
	@Autowired
	JwtUtils jwtUtils;
	
	private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();	

//----------------------------------------------------------	
	private List stateList;
	
	String mykey = "RLBS";
	
//---------------SMS-Code--------------------
	
	@Autowired
	private final SmsService smsService;

    @Autowired
    public GenericController(SmsService smsService) {
        this.smsService = smsService;
    }
	
    
    private String generatedOtp;
    private long otpCreationTime;
//-------------------------------------------	
	
	@RequestMapping(value = "/getFieldsSelectionForm", method = RequestMethod.GET)		
	public ModelAndView getFieldsSelectionForm(Model model,HttpServletRequest request, HttpServletResponse response) {
		try {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
	    String currentDate = dateFormat.format(new Date());
	    model.addAttribute("currentDate", currentDate);
	    request.setAttribute("currentDate", currentDate);
		}catch(Exception e){
			logger.error("Exception in GenericController::getFieldsSelectionForm::" + e.getMessage());
	        logger.error("Exception in GenericController::getFieldsSelectionForm::" + e.getCause());
	        logger.error("Exception in GenericController::getFieldsSelectionForm::" + e.getClass() + "::" + e.getLocalizedMessage());
	    }
		return new ModelAndView("Generic/generate_form");
		
	}
 
	
	@PostMapping(value = "/formGenerate")		
	public ModelAndView formGeneration(Model model,@Valid @ModelAttribute("form")JsonBean bean,BindingResult result,HttpServletRequest request, HttpServletResponse response,HttpSession session) {
    try {
    	    	  
    	String jsonString = bean.getJsonData().replace("\\", "");   
        String finalJson = mykey + HashEncrypter.getHashforLogin1(jsonString);
        String name = bean.getForm_name().replaceAll("\\s", "");
        if (finalJson.equals(bean.getJsonCheck())) {        	
        	FormStructureValidation gvs = new FormStructureValidation();
            List err = gvs.validate(bean);
            ObjectError error ; 
            if(err.size()>0) {
                error = new ObjectError("global", err.toString());
                result.addError(error);
            }           
            
            if(result.hasErrors()) {
            	try {
            		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            	    String currentDate = dateFormat.format(new Date());
            	    model.addAttribute("currentDate", currentDate);
            	    request.setAttribute("currentDate", currentDate);
            	    request.setAttribute("msg1", err);
            		}catch(Exception e){
            			logger.error("Exception in GenericController::getFieldsSelectionForm::" + e.getMessage());
            	        logger.error("Exception in GenericController::getFieldsSelectionForm::" + e.getCause());
            	        logger.error("Exception in GenericController::getFieldsSelectionForm::" + e.getClass() + "::" + e.getLocalizedMessage());
            	    }
            		return new ModelAndView("Generic/generate_form");
            }	
            	
        	
        	
        	int k =genericDao.generateForm(bean);        	
        	JSONParser parser = new JSONParser();
            Object obj = parser.parse(jsonString);
            JSONArray array = (JSONArray) obj;            
            JSONArray array1 = new JSONArray();        	
            for (int i = 1; i < array.size(); i++) {
                JSONObject jsonObject = (JSONObject) array.get(i);
                array1.add(i - 1, jsonObject);
            }
            request.setAttribute("form_name", bean.getForm_name());	
        	request.setAttribute("fields", bean.getFields());        	
        	request.setAttribute("jsonArray", array1); 
        	java.util.List<CommonBean> stateList = new ArrayList<CommonBean>();        
            	stateList = genericDao.getStateList();
            if(k>0) {
                model.addAttribute("msg", "Successfully New Form Generated");
                session.setAttribute("msg", "Successfully New Form Generated");
                return new ModelAndView("redirect:/getDynamicFormList");            	
            }else {
            	model.addAttribute("msg", "New Form Generation Fail");
            	session.setAttribute("msg", "New Form Generation Fail");
            	return new ModelAndView("redirect:/getDynamicFormList");            	 
            }
            
        } else {
        	model.addAttribute("msg", "New Form Generation Fail");
        	session.setAttribute("msg", "New Form Generation Fail");
        	return new ModelAndView("redirect:/getDynamicFormList");
        }
    } catch (Exception e) {
    	logger.error("Exception in GenericController::formGeneration::" + e.getMessage());
        logger.error("Exception in GenericController::formGeneration::" + e.getCause());
        logger.error("Exception in GenericController::formGeneration::" + e.getClass() + "::" + e.getLocalizedMessage());
    }
    return new ModelAndView("redirect:/getDynamicFormList");
	}

	@RequestMapping(value = "/UpdateFormStructure", method = RequestMethod.POST)		
	public ModelAndView updateFormStructure(Model model,@Valid @ModelAttribute("form")JsonBean bean,BindingResult result,HttpServletRequest request, HttpServletResponse response,HttpSession session) {
		java.util.List<JsonBean> list = new ArrayList<JsonBean>();
    try {
    	list = (ArrayList) genericDao.getList();
		request.setAttribute("list",list);
		
    	String jsonString = bean.getJsonData().replace("\\", "");
        String mykey = "RLBS";
        String finalJson = mykey + HashEncrypter.getHashforLogin1(jsonString);
        String name = bean.getForm_name().replaceAll("\\s", "");
        if (finalJson.equals(bean.getJsonCheck())) {
        	FormStructureValidation gvs = new FormStructureValidation();
            List err = gvs.validate(bean);
            ObjectError error ; 
          //  System.out.println("errList==="+err.size());
            if(err.size()>0) {
                error = new ObjectError("global", err.toString());
                result.addError(error);
            }           
            
            //System.out.println("Controller===="+result.hasErrors());
            if(result.hasErrors()) {

      try {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
	    String currentDate = dateFormat.format(new Date());
	    model.addAttribute("currentDate", currentDate);
	    request.setAttribute("currentDate", currentDate);
	    
	    list = (ArrayList) genericDao.getList();
		request.setAttribute("list",list);
		request.setAttribute("msg1", err);
		}catch(Exception e) {
			logger.error("Exception in GenericController::editFormFields::" + e.getMessage());
	        logger.error("Exception in GenericController::editFormFields::" + e.getCause());
	        logger.error("Exception in GenericController::editFormFields::" + e.getClass() + "::" + e.getLocalizedMessage());
	    }	
	    model.addAttribute("form", new JsonBean()); 
		return new ModelAndView("Generic/edit_form_fields");		
	}
        	
        	int k =genericDao.updateFormStructure(bean, session);        	
        	JSONParser parser = new JSONParser();
            Object obj1 = parser.parse(jsonString);
            JSONArray array = (JSONArray) obj1;            
            JSONArray array1 = new JSONArray();        	
            for (int i = 1; i < array.size(); i++) {
                JSONObject jsonObject = (JSONObject) array.get(i);
                array1.add(i - 1, jsonObject);
            }
            request.setAttribute("form_name", bean.getForm_name());	
        	request.setAttribute("fields", bean.getFields());        	
        	request.setAttribute("jsonArray", array1); 
        	if(k>0) {
                request.setAttribute("msg", "Successfully updated the form Fields");
                model.addAttribute("msg", "Successfully updated the form Fields");
        	}else {
            	request.setAttribute("msg1", "updation Fail");
            	model.addAttribute("msg1", "updation Fail");	
        	}
        } else {
        	request.setAttribute("msg1", "updation Fail");
        	model.addAttribute("msg1", "updation Fail");
        }
        
    } catch (Exception e) {
    	logger.error("Exception in GenericController::updateFormStructure::" + e.getMessage());
        logger.error("Exception in GenericController::updateFormStructure::" + e.getCause());
        logger.error("Exception in GenericController::updateFormStructure::" + e.getClass() + "::" + e.getLocalizedMessage());
    }
    model.addAttribute("form", new JsonBean());
    return new ModelAndView("Generic/edit_form_fields");
	}

	@RequestMapping(value = "/insertFormData", method = RequestMethod.POST)
	public ModelAndView insertFormData(Model model,@Valid @ModelAttribute("form") JsonBean bean,BindingResult result,HttpServletRequest request, HttpServletResponse response,HttpSession session) {
	String jsonString = "",otp_verify = "N"; 
	try {		
		//-----------------Newly-added----------------------		
				session.setAttribute("form_id", bean.getForm_id().toString().trim());
		//---------------------------------------------------		
				try {
					String captcha = (String) session.getAttribute("captcha_security");		
					if(bean.getCaptcha_key().equalsIgnoreCase("Y")) {
					if (!bean.getCaptcha().equalsIgnoreCase(captcha)) {
						session.setAttribute("msg", "Invalid Captcha");
						return new ModelAndView("redirect:/getDynamicFormList");
					}
					}
				if(bean.getSms_key().equalsIgnoreCase("Y")) {
				int otp = (Integer) session.getAttribute("otp");
				if (otp == bean.getOtp() && isValidOtp()) {
					otp_verify = "Y";
				} else {
		        	session.setAttribute("msg", "Invalid OTP");
					return new ModelAndView("redirect:/getPublicForm");
		        }
				}
				}catch(Exception e) {			
				}		
		jsonString = bean.getJsonData().toString();				
        String mykey = "RLBS";
        String finalJson = mykey + HashEncrypter.getHashforLogin1(jsonString);
        String jsonString1 = jsonString;
        int m = 0;
        if (finalJson.equals(bean.getJsonCheck())) {           
        GenericValidationService gvs = new GenericValidationService();
        List err = gvs.validate(bean);
        ObjectError error ; 
        if(err.size()>0) {
            error = new ObjectError("global", err.toString());
            result.addError(error);
        }           
        
        if(result.hasErrors()) {
        	//System.out.println("result====="+result.getAllErrors().get(0).getDefaultMessage());
        	//--------------------------------------------------------------
            java.util.List<CommonBean> stateList = new ArrayList<CommonBean>();        
        	stateList = genericDao.getStateList();
             String form_id = bean.getForm_id();
    	     HashMap<String, Object> formDetails = new HashMap<>();
    	    // System.out.println("form id==="+form_id);
    	     java.util.List<JsonBean> list1 = genericDao.getForm(form_id,session);
    	     String str = "";
    	     JSONArray array3 = new JSONArray(); 
    	     if (list1.size()>0) {
    	    JsonBean bean1 = (JsonBean)list1.get(0);
    	    String jsonString2 = bean1.getJsonstring();    	        	   
        	
        	JSONParser parser = new JSONParser();
            Object obj = parser.parse(jsonString2);
            JSONArray array = (JSONArray) obj;            
                   	
            for (int i = 1; i < array.size(); i++) {
                JSONObject jsonObject = (JSONObject) array.get(i);
                array3.add(i - 1, jsonObject);
            }
        	                    	       	
        	 str = Retrieving_Json_Object.getJsonTable_Fields2(array3,bean1,stateList);
             
    	     }
    //--------------------------------------------------------        
    	     
    	    request.setAttribute("msg", err);
        	request.setAttribute("str", str);                         
            request.setAttribute("jsonArray", array3);
            request.setAttribute("form_id", bean.getForm_id());
            request.setAttribute("form_name", bean.getForm_name());
            request.setAttribute("fields", bean.getFields());
            request.setAttribute("captcha_key", bean.getCaptcha_key());
            request.setAttribute("flag", "insert");
            
            return new ModelAndView("Generic/dynamic_form"); 
        }
        
        String name = bean.getForm_name().replaceAll("\\s", "");
        int no = Integer.parseInt(bean.getFields());
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(bean.getJsonFields());
        JSONArray array = (JSONArray) obj;
        
        JSONParser parser1 = new JSONParser();
        Object obj3 = parser1.parse(jsonString);
        JSONArray array1 = (JSONArray) obj3;                       	        	           
        	
        	   JSONArray jsonArray = new JSONArray();
               JSONObject obj2 = new JSONObject();
               for (int i = 0; i < no; i++) {
            	   JSONObject jsonObject1 = (JSONObject) array.get(i);
                   JSONObject obj1 = null;
                   String data = null;                   
                   String dtype = (String) jsonObject1.get("dtype");
                   String label = (String) jsonObject1.get("label");
                     if (dtype.equals("dropdown") || dtype.equals("radiobutton") || dtype.equals("textarea") || dtype.equals("password") || dtype.equals("email") || dtype.equals("date") || dtype.equals("number") || dtype.equals("mobile") || dtype.equals("panno") || dtype.equals("aadhaarno") || dtype.equals("file") || dtype.equals("text") || dtype.equals("time")) {
                       obj1 = (JSONObject) array1.get(i);               
                       data = (String) obj1.get(label);
                       obj2.put(label, data);
                     } else if (dtype.equals("checkbox")) {
                       obj1 = (JSONObject) array1.get(i);        
                       ArrayList<String> list = (ArrayList<String>) obj1.get(label);
                       if(list.size()>0) {
                       String array2[] = new String[list.size()];
                       for (int j = 0; j < list.size(); j++) {
                           array2[j] = list.get(j);
                           data = (String) array2[j];
                           obj2.put(label, data);
                       }
                     }else {
                    	 obj2.put(label, ""); 
                     }
                   } else if (dtype.equals("specialcombo")) {
                       obj1 = (JSONObject) array1.get(i);
                   //    System.out.println("Data-SERVER-SIDE==StateCode==" + obj1.get("stateCode"));   
                       data = (String) obj1.get("stateCode");                     
                       obj2.put("stateCode", data);
                       obj2.put("stateName", bean.getStateName());  
                       data = (String) obj1.get("distCode");                       
                       obj2.put("distCode", data);
                       obj2.put("distName", bean.getDistName());                          
                       data = (String) obj1.get("mandalCode");               
                       obj2.put("mandalCode", data);
                       obj2.put("mandalName", bean.getMandalName());                          
                       data = (String) obj1.get("villCode");           
                       obj2.put("villCode", data);
                       obj2.put("villName", bean.getVillName());                         
                   } else if (dtype.equals("otp")) {                    	 
                       obj2.put(label, otp_verify);   
                    }                                                                                     
               }
               m = genericDao.insertFormData(array,obj2,bean.getForm_name(),bean);                 
               } else {
               session.setAttribute("msg", "Authentication Fail");
        	   return new ModelAndView("redirect:/getDynamicFormList");
               }
        		if(m == 200) {
        			session.setAttribute("msg", "Duplicate Data is not allowed");
    	            model.addAttribute("msg", "Duplicate Data is not allowed");	
        		}else if(m>0) {
	            session.setAttribute("msg", "Successfully Stored the Data");
	            model.addAttribute("msg", "Successfully Stored the Data");
	            }else {
	            session.setAttribute("msg", "Data Insertion Fail");
	            model.addAttribute("msg", "Data Insertion Fail"); 
	            }	        	         
    } catch (Exception e) {
    	logger.error("Exception in GenericController::insertFormData::" + e.getMessage());
        logger.error("Exception in GenericController::insertFormData::" + e.getCause());
        logger.error("Exception in GenericController::insertFormData::" + e.getClass() + "::" + e.getLocalizedMessage());
    }
	return new ModelAndView("redirect:/getDynamicFormList");
	}
		
	@RequestMapping(value = "/EditFormFields", method = RequestMethod.GET)
	public ModelAndView editFormFields(Model model,HttpServletRequest request,HttpServletResponse response) {	
		java.util.List<JsonBean> list = new ArrayList<JsonBean>();
		ModelAndView mv = null;
		try {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
	    String currentDate = dateFormat.format(new Date());
	    model.addAttribute("currentDate", currentDate);
	    request.setAttribute("currentDate", currentDate);
	    
	    list = (ArrayList) genericDao.getList();
		request.setAttribute("list",list);
		}catch(Exception e) {
			logger.error("Exception in GenericController::editFormFields::" + e.getMessage());
	        logger.error("Exception in GenericController::editFormFields::" + e.getCause());
	        logger.error("Exception in GenericController::editFormFields::" + e.getClass() + "::" + e.getLocalizedMessage());
	    }	
	    model.addAttribute("form", new JsonBean()); 
		mv = new ModelAndView("Generic/edit_form_fields");
		return mv;
	}
		
	@RequestMapping(value = "/getDynamicFormList", method = RequestMethod.GET)
	public ModelAndView getDynamicFormList(Model model,HttpServletRequest request,HttpServletResponse response,HttpSession session) {	
		java.util.List<JsonBean> list = new ArrayList<JsonBean>();
		ModelAndView mv = null;String msg = "";
		try {
	    list = (ArrayList) genericDao.getFormsList();		
		if(list.size()>0) {
		request.setAttribute("list",list);
		}
		try {
		msg = session.getAttribute("msg").toString();
		session.removeAttribute("msg");
		}catch(Exception e) {
			if(list.size()<0 || list.size() == 0) {
				msg = "Forms Verify and Approval was Pending";
		 }
		}
		request.setAttribute("msg",msg);
		model.addAttribute("msg", msg);
		}catch(Exception e) {
			logger.error("Exception in GenericController::getDynamicFormList::" + e.getMessage());
	        logger.error("Exception in GenericController::getDynamicFormList::" + e.getCause());
	        logger.error("Exception in GenericController::getDynamicFormList::" + e.getClass() + "::" + e.getLocalizedMessage());
	    }	
	    model.addAttribute("form", new JsonBean());
		mv = new ModelAndView("Generic/dynamic_form_list");
		return mv;
	}
	
	@RequestMapping(value = "/getDynamicForm", method = {RequestMethod.POST,RequestMethod.GET })		
	public ModelAndView getDynamicForm(Model model,@ModelAttribute("form")JsonBean form,@RequestParam("_csrf")String csrfToken ,HttpServletRequest request, HttpServletResponse response,HttpSession session) {
    try {    	
    	CaptchaDto captchaDto = captchaController.generateCaptchaImage(session, request, response, model);
//	    System.out.println(captchaDto.getCaptchaString());
	    // Add the CAPTCHA string to the model
	    model.addAttribute("captchaString", captchaDto.getCaptchaString());
	    session.setAttribute("captchaString", captchaDto.getCaptchaString());
	    // Render the CAPTCHA image
	    byte[] captchaImage = captchaDto.getImage();

	    // Convert the image byte array to a Base64-encoded string
	    String base64Image = Base64.getEncoder().encodeToString(captchaImage);
	    model.addAttribute("captchaImage", base64Image);
	    session.setAttribute("captchaImage", base64Image);
    	java.util.List<CommonBean> stateList = new ArrayList<CommonBean>();        
        	stateList = genericDao.getStateList();
             String form_id= request.getParameter("form_id");
    	     HashMap<String, Object> formDetails = new HashMap<>();
    	     int count  = genericDao.checkFormExpiryDate(form_id,session);
    	     if(count>0) {
    	     java.util.List<JsonBean> list = genericDao.getForm(form_id,session);
    	     if (list.size()>0) {
    	    JsonBean bean = (JsonBean)list.get(0);
    	    String jsonString = bean.getJsonstring();    	        	   
        	
        	JSONParser parser = new JSONParser();
            Object obj = parser.parse(jsonString);
            JSONArray array = (JSONArray) obj;            
            JSONArray array1 = new JSONArray();        	
            for (int i = 1; i < array.size(); i++) {
                JSONObject jsonObject = (JSONObject) array.get(i);
                array1.add(i - 1, jsonObject);
            }
        	            
        	bean.setCaptchaImage(base64Image);       	
        	String str = Retrieving_Json_Object.getJsonTable_Fields2(array1,bean,stateList);
            request.setAttribute("str", str);                         
            request.setAttribute("jsonArray", array1);
            request.setAttribute("str", str);
            request.setAttribute("form_id", bean.getForm_id());
            request.setAttribute("form_name", bean.getForm_name());
            request.setAttribute("fields", bean.getFields());
            request.setAttribute("captcha_key", bean.getCaptcha_key());
            request.setAttribute("flag", "insert");
                       
            return new ModelAndView("Generic/dynamic_form"); 
        } else {
        	session.setAttribute("msg", "New Form Generation Fail");        	
        	return new ModelAndView("redirect:/getDynamicFormList");
        }
    } else {
    	session.setAttribute("msg", "Form will not open, Form Expiry Date was Expired");        	
    	return new ModelAndView("redirect:/getDynamicFormList");
    }
    } catch (Exception e) {
    	logger.error("Exception in GenericController::getDynamicForm::" + e.getMessage());
        logger.error("Exception in GenericController::getDynamicForm::" + e.getCause());
        logger.error("Exception in GenericController::getDynamicForm::" + e.getClass() + "::" + e.getLocalizedMessage());
    }
    return new ModelAndView("Generic/dynamic_form");
	}
		
	@RequestMapping(value = "/getDynamicRecordsList", method = RequestMethod.GET)
	public ModelAndView getDynamicRecordsList(Model model,HttpServletRequest request,HttpServletResponse response,HttpSession session) {	
		java.util.List<JsonBean> list = new ArrayList<JsonBean>();
		ModelAndView mv = null;String msg = "";
		try {
	    list = (ArrayList) genericDao.getFormsRecordList();		
		request.setAttribute("list",list);
		try {
		msg = session.getAttribute("msg").toString();
		session.removeAttribute("msg");
		}catch(Exception e) {}
		request.setAttribute("msg",msg);
		model.addAttribute("msg", msg);
		}catch(Exception e) {
			logger.error("Exception in GenericController::getDynamicRecordsList::" + e.getMessage());
	        logger.error("Exception in GenericController::getDynamicRecordsList::" + e.getCause());
	        logger.error("Exception in GenericController::getDynamicRecordsList::" + e.getClass() + "::" + e.getLocalizedMessage());
	    }	
	    model.addAttribute("form", new JsonBean());
		mv = new ModelAndView("Generic/dynamic_record_list");
		return mv;
	}
	
	@RequestMapping(value = "/updateFormData", method = RequestMethod.POST)
	public ModelAndView updateFormData(Model model,@Valid @ModelAttribute("form")JsonBean bean,BindingResult result,HttpServletRequest request, HttpServletResponse response,HttpSession session) { 
	String jsonString = "",otp_verify = "N"; 
	try {
		String captcha = (String) session.getAttribute("captcha_security");		
		if(bean.getCaptcha_key().equalsIgnoreCase("Y")) {
		if (!bean.getCaptcha().equalsIgnoreCase(captcha)) {
			session.setAttribute("msg", "Invalid Captcha");
			return new ModelAndView("redirect:/getDynamicFormList");
		}
		}
		if(bean.getSms_key().equalsIgnoreCase("Y")) {
		int otp = (Integer) session.getAttribute("otp");
		if (otp == bean.getOtp() && isValidOtp()) {
			otp_verify = "Y";
		} else {        	
        	session.setAttribute("msg", "Invalid OTP");
        	return new ModelAndView("redirect:/getDynamicFormList");
        }
	}
		
		jsonString = bean.getJsonData().toString();
		String jsonString1 = jsonString.replace("\\", "");	    
        String mykey = "RLBS";
        String finalJson = mykey + HashEncrypter.getHashforLogin1(jsonString);
        String name = bean.getForm_name().replaceAll("\\s", "");
        int no = Integer.parseInt(bean.getFields());
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(bean.getJsonFields());
        JSONArray array = (JSONArray) obj;
        
        JSONParser parser1 = new JSONParser();
        Object obj3 = parser1.parse(jsonString);
        JSONArray array1 = (JSONArray) obj3;            
        
        int m = 0;
        if (finalJson.equals(bean.getJsonCheck())) { 
        	
//          GenericValidator validator = new GenericValidator();
//          validator.validate(bean, result);
          GenericValidationService gvs = new GenericValidationService();
          List err = gvs.validate(bean);
          ObjectError error ; 
//          System.out.println("errList==="+err.size());
          if(err.size()>0) {
              error = new ObjectError("global", err.toString());
              result.addError(error);
          }                     
//          System.out.println("Controller===="+result.hasErrors());
          if(result.hasErrors()) {  	    
        	 String form_id= request.getParameter("form_id");
      	     String record_id= request.getParameter("record_id");
      	     java.util.List<JsonBean> list5 = genericDao.getForm(form_id,session);
      	     if (list5.size()>0) {
      	    JsonBean bean1 = (JsonBean)list5.get(0);
      	    String jsonString3 = bean1.getJsonstring();    	        	   
          	
          	JSONParser parser2 = new JSONParser();
              Object obj4 = parser2.parse(jsonString3);
              JSONArray array3 = (JSONArray) obj4;            
              JSONArray array4 = new JSONArray();        	
              for (int i = 1; i < array3.size(); i++) {
                  JSONObject jsonObject = (JSONObject) array3.get(i);
                  array4.add(i - 1, jsonObject);
              } 
              
              
              java.util.List<JsonBean> list1 = genericDao.getFormData(form_id,record_id,session);                    	
          	ArrayList al = new ArrayList(0);
          	String str = Retrieving_Json_Object.getJsonTable_Fields4(array4,list1,bean);                                  
              request.setAttribute("jsonArray", array1); 
              request.setAttribute("str", str);
              request.setAttribute("record_id", record_id);
              request.setAttribute("form_id", bean.getForm_id());            
              request.setAttribute("form_name", bean.getForm_name());            
              request.setAttribute("fields", bean.getFields());
              request.setAttribute("captcha_key", bean.getCaptcha_key());
              request.setAttribute("flag", "update");
              request.setAttribute("msg", err);
              return new ModelAndView("Generic/data_update_form"); 	
        }
          }
        	
        	   JSONArray jsonArray = new JSONArray();
               JSONObject obj2 = new JSONObject();
               for (int i = 0; i < no; i++) {
            	   JSONObject jsonObject1 = (JSONObject) array.get(i);
                   JSONObject obj1 = null;
                   String data = null;
                   String dtype = (String) jsonObject1.get("dtype");
                   String label = (String) jsonObject1.get("label");
                     if (dtype.equals("dropdown") || dtype.equals("radiobutton") || dtype.equals("textarea") || dtype.equals("password") || dtype.equals("email") || dtype.equals("date") || dtype.equals("number") || dtype.equals("mobile") || dtype.equals("panno") || dtype.equals("aadhaarno") || dtype.equals("file") || dtype.equals("text") || dtype.equals("time")) {
                       obj1 = (JSONObject) array1.get(i);             
                       data = (String) obj1.get(label);
                       obj2.put(label, data);
                     } else if (dtype.equals("checkbox")) {
                       obj1 = (JSONObject) array1.get(i);      
                       ArrayList<String> list = (ArrayList<String>) obj1.get(label);
                       if(list.size()>0) {
                       String array2[] = new String[list.size()];
                       for (int j = 0; j < list.size(); j++) {
                           array2[j] = list.get(j);
                           data = (String) array2[j];
                           obj2.put(label, data);
                       }
                     }else {
                    	 obj2.put(label, ""); 
                     }
                   }else if (dtype.equals("specialcombo")) {                      
                       obj1 = (JSONObject) array1.get(i);   
                       data = (String) obj1.get("stateCode");                       
                       obj2.put("stateCode", data);
                       obj2.put("stateName", bean.getStateName());   
                       data = (String) obj1.get("distCode");                       
                       obj2.put("distCode", data);
                       obj2.put("distName", bean.getDistName());   
                       data = (String) obj1.get("mandalCode");                        
                       obj2.put("mandalCode", data);
                       obj2.put("mandalName", bean.getMandalName());   
                       data = (String) obj1.get("villCode");                        
                       obj2.put("villCode", data);
                       obj2.put("villName", bean.getVillName()); 
                   } else if (dtype.equals("otp")) {                    	 
                       obj2.put(label, otp_verify);   
                    }                                                                                       
               }
               m = genericDao.updateFormData(array,obj2,bean);                 
               } else {
            	   session.setAttribute("msg", "Authentication Fail");
            	   return new ModelAndView("redirect:/getDynamicRecordsList");  
               }
               
		if(m == 200) {
			session.setAttribute("msg", "Duplicate Data is not allowed");
            model.addAttribute("msg", "Duplicate Data is not allowed");	
		}else if(m>0) {
            session.setAttribute("msg", "Successfully Updated the Data");
            model.addAttribute("msg", "Successfully Updated the Data");
            }else {
            session.setAttribute("msg", "Data Updation Fail");
            model.addAttribute("msg", "Data Updation Fail"); 
            }        
         
    } catch (Exception e) {
    	e.printStackTrace();
    	logger.error("Exception in GenericController::updateFormData::" + e.getMessage());
        logger.error("Exception in GenericController::updateFormData::" + e.getCause());
        logger.error("Exception in GenericController::updateFormData::" + e.getClass() + "::" + e.getLocalizedMessage());
    }

	return new ModelAndView("redirect:/getDynamicRecordsList");
	}	
//-------------------Angular-Methods---------------------
	@RequestMapping(value = "/getFormStructure", method = RequestMethod.POST)
	public ModelAndView getFormStructure(Model model,HttpServletRequest request,HttpServletResponse response,HttpSession session) {
     try {
    	// System.out.println("getFormStructure=====");
     String form_id= request.getParameter("form_id");
     HashMap<String, Object> formDetails = new HashMap<>();
     java.util.List<JsonBean> list = genericDao.getFormStructure(form_id,session);
     if (list.size()>0) {
    JsonBean bean = (JsonBean)list.get(0);
    String jsonString = bean.getJsonstring();
          
    formDetails.put("form_id", bean.getForm_id());
    formDetails.put("form_name", bean.getForm_name());
    formDetails.put("fields", bean.getFields());
    
    String table = "<div>";
    int j =1;
    int no = Integer.parseInt(bean.getFields());
    
    JSONParser parser = new JSONParser();
    Object obj = parser.parse(jsonString);
    JSONArray array = (JSONArray) obj;            
    
    for (int i = 1; i <= no; i++) {
    	JSONObject jsonObject = (JSONObject) array.get(i);
    	String dtype = (String) jsonObject.get("dtype");
    	boolean con = false;
    	StringTokenizer st = new StringTokenizer(bean.getCon_value(),"||");
    	while (st.hasMoreTokens()) {
    		if(dtype.equalsIgnoreCase(st.nextToken())) {
    		con	= true;
    		}else {}}
    	table = table + "<div class='row'><h6 class='text-decoration-underline'>Field " + i + "</h6></div><div class='row m-3 flex-nowrap'> \n";
    			if(con == true) {
    				table = table  + "<div class='col-2'><div class='form-check'><input class='form-check-input' type='checkbox' name='con_check' id='con_check"+ i +"' checked='true'> <label class='form-check-label'for='con_check"+ i +"'> Unique </label></div></div> \n";
    			}else {
    				table = table  + "<div class='col-2'><div class='form-check'><input class='form-check-input' type='checkbox' name='con_check' id='con_check"+ i +"'> <label class='form-check-label'for='con_check"+ i +"'> Unique </label></div></div> \n"; 	
    			} 
                
                table = table  + " <div class='col-2'><div class='floating-label'><input type='text' name='label' class='form-control' required='true' id='label_" + i + "' value='"+jsonObject.get("label")+"' onkeypress='return ValidateAlpha(event)' onblur='return checkString(this, 'Label Name');' minlength='1' maxlength='50'  autocomplete='off' placeholder=''/> <label for='label_" + i + "'>Label Name</label></div></div>  \n"
                		+ " <div class='col-2'><div class='form-group floating-label'><select class='form-select' name='dtype'  id='dtype_" + i + "' required='true' autocomplete='off' onchange ='getShowTypeEdit(this.value, " + i + ")' ><option  value='"+jsonObject.get("dtype")+"'>"+jsonObject.get("dtype")+"</option><option value=''>Select</option><option value='text'>Text</option><option value='number'>Number</option><option value='dropdown'>Drop down</option><option value='radiobutton'>Radio button</option><option value='checkbox'>Check box</option><option value='textarea'>Text area</option><option value='password'>Password</option><option value='email'>Email</option><option value='date'>Date</option><option value='file'>File upload</option><option value='specialcombo'>Special combo</option><option value='mobile'>Mobile</option><option value='panno'>PAN Number</option><option value='aadhaarno'>Aadhaar Number</option><option value='time'>Time</option></select> <label for='dtype_" + i + "'>Data Type</label></div></div>\n"            
                		+ " <div class='col-2'><div class='floating-label'><input type='text' name='minsize' class='form-control'  id='minsize_" + i + "' value='"+jsonObject.get("minsize")+"' required='true' minlength='1' maxlength='3' onkeypress='return isNumber(event)' autocomplete='off' placeholder=''><label for='minsize_\" + i + \"'>Min Size</label></div></div>\n"                
                		+ " <div class='col-2'><div class='floating-label'><input type='text' name='maxsize' placeholder='' class='form-control' id='maxsize_" + i + "' value='"+jsonObject.get("maxsize")+"' required='true' maxlength='3' minlength='1' onkeypress='return isNumber(event)' autocomplete='off'> <label for='maxsize_" + i + "'>Max Size</label></div></div> \n"      
                		+ " <div class='col-2'><div class='form-group floating-label'><select class='form-select' name='mandatory' id='mandatory_" + i + "' required='true'><option value='"+jsonObject.get("mandatory")+"'>"+jsonObject.get("mandatory")+"</option><option value=''>Select</option><option value='true'>True</option><option value='false'>False</option></select> <label for='mandatory_" + i + "'>Mandatory</label></div></div>\n"; 
                                                                              
            if(jsonObject.get("dtype").equals("dropdown")) { 
        table = table + " <div class='col-2' id='sss_" + i + "'><div class='floating-label'id='ssr_" + i + "'><input type='text' name='no1' id='no1_" + i + "' value='"+jsonObject.get("no1")+"' minlength='1' maxlength='2' onkeypress='return isNumber(event)' onchange ='getShow1(this.value," + i + ")' autocomplete='off' placeholder='' class='form-control'/><label for='no1_" + i + "'>No of values</label></div></div>\n"
    	        + " <div class='col-2' id='nnn_" + i + "'><div class='dropdown'><button class='btn btn-outline-secondary dropdown-toggle w-100 show' type='button' id='dropdownMenuButton' data-bs-toggle='dropdown' aria-haspopup='true' aria-expanded='true'>Dropdown Values</button><div class='dropdown-menu w-100 show' aria-labelledby='dropdownMenuButton'><div id='vvv_" + i + "'>";
        JSONArray ss = (JSONArray) jsonObject.get("dropvalues");
        for (int z = 0; z < ss.size(); z++) {
        	//System.out.println(ss.get(z));
        	table = table + "<div class='dropdown-item'><input type='text' name='dropvalues' class='form-control' placeholder='' id='dropvalues_" +  i + "" +  z + "' value='"+ss.get(z)+"' minlength ='1' maxlength='25' onkeypress='return ValidateAlpha(event)' onblur='return checkString(this, 'Drop Value');' autocomplete='off'></div>";
        }
        table = table   + " </div></div></div></div> \n" 
        
						+ " <div class='col-2' id='rrr_" + i + "' style='display:none;'><div class='floating-label'id='bbb_" + i + "' style='display:none;'><input type='text' name='no2' id='no2_" + i + "' minlength='1' maxlength='2' onkeypress='return isNumber(event)' onchange ='getShow3(this.value," + i + ")' autocomplete='off' class='form-control'placeholder='No of Values' /> <label for='no2_" + i + "'>No of values</label></div></div> \n"
						+ " <div class='col-2' id='rr_" + i + "' style='display:none;'><div class='dropdown'><button class='btn btn-outline-secondary dropdown-toggle w-100 show' type='button' id='dropdownMenuButton' data-bs-toggle='dropdown' aria-haspopup='true' aria-expanded='true'>Radio button Values</button><div class='dropdown-menu w-100 show' aria-labelledby='dropdownMenuButton'><div id='bb_" + i + "'></div></div></div></div>\n"

				  + " <div class='col-2' id='ccc_" + i + "' style='display:none;'><div class='floating-label'id='cb_" + i + "' style='display:none;'><input type='text' name='no3'  id='no3_" + i + "' minlength='1' maxlength='2' onkeypress='return isNumber(event)' onchange ='getShow5(this.value," + i + ")' autocomplete='off' class='form-control'placeholder='No of Values' /> <label for='no3_" + i + "'>No of values</label></div></div> \n"
                  + " <div class='col-2' id='cc_" + i + "' style='display:none;'><div class='dropdown'><button class='btn btn-outline-secondary dropdown-toggle w-100 show' type='button' id='dropdownMenuButton' data-bs-toggle='dropdown' aria-haspopup='true' aria-expanded='true'>Check box Values</button><div class='dropdown-menu w-100 show' aria-labelledby='dropdownMenuButton'><div id='cbv_" + i + "'></div></div></div></div>\n"
                  
                  + " <div class='col-2' id='ff2_" + i + "' style='display:none;'><div class='form-group floating-label' id='ff3_" + i + "'><select class='form-select' name='file_type' id='file_type"+ i +"' ><option value=''>Select</option><option value='pdf'>PDF</option> <option value='jpg|jpeg|png'>JPG|JPEG|PNG</option></select> <label for='file_type"+ i +"' >File Format</label></div></div>\n";
        
            }else if(jsonObject.get("dtype").equals("radiobutton")) {   
        table = table +  " <div class='col-2' id='rrr_" + i + "'><div class='floating-label'id='bbb_" + i + "'><input type='text' name='no2' id='no2_" + i + "' value='"+jsonObject.get("no2")+"' minlength='1' maxlength='2' onkeypress='return isNumber(event)' onchange ='getShow3(this.value," + i + ")' autocomplete='off' placeholder='' class='form-control'/><label for='no2_" + i + "'>No of values</label></div></div>\n"
        + " <div class='col-2' id='rr_" + i + "'><div class='dropdown'><button class='btn btn-outline-secondary dropdown-toggle w-100 show' type='button' id='dropdownMenuButton' data-bs-toggle='dropdown' aria-haspopup='true' aria-expanded='true'>Radio Values</button><div class='dropdown-menu w-100 show' aria-labelledby='dropdownMenuButton'><div id='bb_" + i + "'>";
        
        
        JSONArray nn = (JSONArray) jsonObject.get("radiovalues");
        for (int xx = 0; xx < nn.size(); xx++) {
        	table = table + "<div class='dropdown-item'><input type='text' name='radiovalues' class='form-control' placeholder='' id='radiovalues_" +  i + "" + xx + "' value='"+nn.get(xx)+"' minlength ='1' maxlength='25' onkeypress='return ValidateAlpha(event)' onblur='return checkString(this, 'Radio Value');' autocomplete='off'></div>";
        }
        table = table   + " </div></div></div></div> \n"

				+ " <div class='col-2' id='sss_" + i + "' style='display:none;'><div class='floating-label' id='ssr_" + i + "' style='display:none;'><input type='text' name='no1' id='no1_" + i + "' minlength='1' maxlength='2'onkeypress='return isNumber(event)' onchange ='getShow1(this.value," + i + ")' autocomplete='off' class='form-control'placeholder='No of Values' /> <label for='no1_" + i + "'>No of values</label></div></div> \n"
				+ " <div class='col-2' id='nnn_" + i + "' style='display:none;'><div class='dropdown'><button class='btn btn-outline-secondary dropdown-toggle w-100 show' type='button' id='dropdownMenuButton' data-bs-toggle='dropdown' aria-haspopup='true' aria-expanded='true'>Dropdown Values</button><div class='dropdown-menu w-100 show' aria-labelledby='dropdownMenuButton'><div id='vvv_" + i + "'></div></div></div></div>\n"  
                
                
				+ " <div class='col-2' id='ccc_" + i + "' style='display:none;'><div class='floating-label'id='cb_" + i + "' style='display:none;'><input type='text' name='no3'  id='no3_" + i + "' minlength='1' maxlength='2' onkeypress='return isNumber(event)' onchange ='getShow5(this.value," + i + ")' autocomplete='off' class='form-control'placeholder='No of Values' /> <label for='no3_" + i + "'>No of values</label></div></div> \n"
                  + " <div class='col-2' id='cc_" + i + "' style='display:none;'><div class='dropdown'><button class='btn btn-outline-secondary dropdown-toggle w-100 show' type='button' id='dropdownMenuButton' data-bs-toggle='dropdown' aria-haspopup='true' aria-expanded='true'>Check box Values</button><div class='dropdown-menu w-100 show' aria-labelledby='dropdownMenuButton'><div id='cbv_" + i + "'></div></div></div></div>\n"
                
                  
                  + " <div class='col-2' id='ff2_" + i + "' style='display:none;'><div class='form-group floating-label' id='ff3_" + i + "'><select class='form-select' name='file_type' id='file_type"+ i +"' ><option value=''>Select</option><option value='pdf'>PDF</option> <option value='jpg|jpeg|png'>JPG|JPEG|PNG</option></select> <label for='file_type"+ i +"' >File Format</label></div></div>\n";           
            }else if(jsonObject.get("dtype").equals("checkbox")) {
            	table = table  +  " <div class='col-2' id='ccc_" + i + "'><div class='floating-label' id='cb_" + i + "'><input type='text' name='no3' id='no3_" + i + "' value='"+jsonObject.get("no3")+"' minlength='1' maxlength='2' onkeypress='return isNumber(event)' onchange ='getShow5(this.value," + i + ")' autocomplete='off' placeholder='' class='form-control'/><label for='no3_" + i + "'>No of values</label></div></div>\n"
                        + " <div class='col-2' id='cc_" + i + "'><div class='dropdown'><button class='btn btn-outline-secondary dropdown-toggle w-100 show' type='button' id='dropdownMenuButton' data-bs-toggle='dropdown' aria-haspopup='true' aria-expanded='true'>Checkbox Values</button><div class='dropdown-menu w-100 show' aria-labelledby='dropdownMenuButton'><div id='cbv_" + i + "'>";
    	JSONArray nn2 = (JSONArray) jsonObject.get("checkvalues");
    	for (int mm = 0; mm < nn2.size(); mm++) {
            
            table = table + "<div class='dropdown-item'><input type='text' name='checkvalues' class='form-control' placeholder='' id='checkvalues_" +  i + "" + mm + "' value='"+nn2.get(mm)+"' minlength ='1' maxlength='25' onkeypress='return ValidateAlpha(event)' onblur='return checkString(this, 'Check Value');' autocomplete='off'></div>";
    	}
    	table = table   + " </div></div></div></div> \n"
				+ " <div class='col-2' id='sss_" + i + "' style='display:none;'><div class='floating-label' id='ssr_" + i + "' style='display:none;'><input type='text' name='no1' id='no1_" + i + "' minlength='1' maxlength='2'onkeypress='return isNumber(event)' onchange ='getShow1(this.value," + i + ")' autocomplete='off' class='form-control'placeholder='No of Values' /> <label for='no1_" + i + "'>No of values</label></div></div> \n"
				+ " <div class='col-2' id='nnn_" + i + "' style='display:none;'><div class='dropdown'><button class='btn btn-outline-secondary dropdown-toggle w-100 show' type='button' id='dropdownMenuButton' data-bs-toggle='dropdown' aria-haspopup='true' aria-expanded='true'>Dropdown Values</button><div class='dropdown-menu w-100 show' aria-labelledby='dropdownMenuButton'><div id='vvv_" + i + "'></div></div></div></div>\n"        
                
				+ " <div class='col-2' id='rrr_" + i + "' style='display:none;'><div class='floating-label'id='bbb_" + i + "' style='display:none;'><input type='text' name='no2' id='no2_" + i + "' minlength='1' maxlength='2' onkeypress='return isNumber(event)' onchange ='getShow3(this.value," + i + ")' autocomplete='off' class='form-control'placeholder='No of Values' /> <label for='no2_" + i + "'>No of values</label></div></div> \n"
				+ " <div class='col-2' id='rr_" + i + "' style='display:none;'><div class='dropdown'><button class='btn btn-outline-secondary dropdown-toggle w-100 show' type='button' id='dropdownMenuButton' data-bs-toggle='dropdown' aria-haspopup='true' aria-expanded='true'>Radio button Values</button><div class='dropdown-menu w-100 show' aria-labelledby='dropdownMenuButton'><div id='bb_" + i + "'></div></div></div></div>\n"
                
				+ " <div class='col-2' id='ff2_" + i + "' style='display:none;'><div class='form-group floating-label' id='ff3_" + i + "'><select class='form-select' name='file_type' id='file_type"+ i +"' ><option value=''>Select</option><option value='pdf'>PDF</option> <option value='jpg|jpeg|png'>JPG|JPEG|PNG</option></select> <label for='file_type"+ i +"' >File Format</label></div></div>\n";
    	
            }else if(jsonObject.get("dtype").equals("file")) {           	
                table =table + "<div class='col-2' id='ff2_" + i + "'><div class='form-group floating-label' id='ff3_" + i + "'><select class='form-select' name='file_type' id='file_type"+ i +"' ><option value='"+jsonObject.get("file_type")+"'>"+jsonObject.get("file_type")+"</option><option value='pdf'>PDF</option> <option value='jpg|jpeg|png'>JPG|JPEG|PNG</option></select> <label for='file_type"+ i +"' >File Format</label></div></div> \n"; 
                
                		table = table   + " \n"
                      
					+ " <div class='col-2' id='sss_" + i + "' style='display:none;'><div class='floating-label' id='ssr_" + i + "' style='display:none;'><input type='text' name='no1' id='no1_" + i + "' minlength='1' maxlength='2'onkeypress='return isNumber(event)' onchange ='getShow1(this.value," + i + ")' autocomplete='off' class='form-control'placeholder='No of Values' /> <label for='no1_" + i + "'>No of values</label></div></div> \n"
					+ " <div class='col-2' id='nnn_" + i + "' style='display:none;'><div class='dropdown'><button class='btn btn-outline-secondary dropdown-toggle w-100 show' type='button' id='dropdownMenuButton' data-bs-toggle='dropdown' aria-haspopup='true' aria-expanded='true'>Dropdown Values</button><div class='dropdown-menu w-100 show' aria-labelledby='dropdownMenuButton'><div id='vvv_" + i + "'></div></div></div></div>\n"

                     + " <div class='col-2' id='rrr_" + i + "' style='display:none;'><div class='floating-label'id='bbb_" + i + "' style='display:none;'><input type='text' name='no2' id='no2_" + i + "' minlength='1' maxlength='2' onkeypress='return isNumber(event)' onchange ='getShow3(this.value," + i + ")' autocomplete='off' class='form-control'placeholder='No of Values' /> <label for='no2_" + i + "'>No of values</label></div></div> \n"
                     + " <div class='col-2' id='rr_" + i + "' style='display:none;'><div class='dropdown'><button class='btn btn-outline-secondary dropdown-toggle w-100 show' type='button' id='dropdownMenuButton' data-bs-toggle='dropdown' aria-haspopup='true' aria-expanded='true'>Radio button Values</button><div class='dropdown-menu w-100 show' aria-labelledby='dropdownMenuButton'><div id='bb_" + i + "'></div></div></div></div>\n"   
                        
				+ " <div class='col-2' id='ccc_" + i + "' style='display:none;'><div class='floating-label'id='cb_" + i + "' style='display:none;'><input type='text' name='no3'  id='no3_" + i + "' minlength='1' maxlength='2' onkeypress='return isNumber(event)' onchange ='getShow5(this.value," + i + ")' autocomplete='off' class='form-control'placeholder='No of Values' /> <label for='no3_" + i + "'>No of values</label></div></div> \n"
				+ " <div class='col-2' id='cc_" + i + "' style='display:none;'><div class='dropdown'><button class='btn btn-outline-secondary dropdown-toggle w-100 show' type='button' id='dropdownMenuButton' data-bs-toggle='dropdown' aria-haspopup='true' aria-expanded='true'>Check box Values</button><div class='dropdown-menu w-100 show' aria-labelledby='dropdownMenuButton'><div id='cbv_" + i + "'></div></div></div></div>\n";

            }else if(jsonObject.get("dtype").equals("textarea") || jsonObject.get("dtype").equals("password") || jsonObject.get("dtype").equals("email") || jsonObject.get("dtype").equals("date") ||  jsonObject.get("dtype").equals("text") || jsonObject.get("dtype").equals("number") || jsonObject.get("dtype").equals("mobile") || jsonObject.get("dtype").equals("panno") || jsonObject.get("dtype").equals("aadhaarno") || jsonObject.get("dtype").equals("specialcombo")) {
                    table = table +  " <div class='col-2' id='sss_" + i + "' style='display:none;'><div class='floating-label' id='ssr_" + i + "' style='display:none;'><input type='text' name='no1' id='no1_" + i + "' minlength='1' maxlength='2'onkeypress='return isNumber(event)' onchange ='getShow1(this.value," + i + ")' autocomplete='off' class='form-control'placeholder='No of Values' /> <label for='no1_" + i + "'>No of values</label></div></div> \n"
                        		  + " <div class='col-2' id='nnn_" + i + "' style='display:none;'><div class='dropdown'><button class='btn btn-outline-secondary dropdown-toggle w-100 show' type='button' id='dropdownMenuButton' data-bs-toggle='dropdown' aria-haspopup='true' aria-expanded='true'>Dropdown Values</button><div class='dropdown-menu w-100 show' aria-labelledby='dropdownMenuButton'><div id='vvv_" + i + "'></div></div></div></div>\n"
                                                
					+ " <div class='col-2' id='rrr_" + i + "' style='display:none;'><div class='floating-label'id='bbb_" + i + "' style='display:none;'><input type='text' name='no2' id='no2_" + i + "' minlength='1' maxlength='2' onkeypress='return isNumber(event)' onchange ='getShow3(this.value," + i + ")' autocomplete='off' class='form-control'placeholder='No of Values' /> <label for='no2_" + i + "'>No of values</label></div></div> \n"
					+ " <div class='col-2' id='rr_" + i + "' style='display:none;'><div class='dropdown'><button class='btn btn-outline-secondary dropdown-toggle w-100 show' type='button' id='dropdownMenuButton' data-bs-toggle='dropdown' aria-haspopup='true' aria-expanded='true'>Radio button Values</button><div class='dropdown-menu w-100 show' aria-labelledby='dropdownMenuButton'><div id='bb_" + i + "'></div></div></div></div>\n"

					+ " <div class='col-2' id='ccc_" + i + "' style='display:none;'><div class='floating-label'id='cb_" + i + "' style='display:none;'><input type='text' name='no3'  id='no3_" + i + "' minlength='1' maxlength='2' onkeypress='return isNumber(event)' onchange ='getShow5(this.value," + i + ")' autocomplete='off' class='form-control'placeholder='No of Values' /> <label for='no3_" + i + "'>No of values</label></div></div> \n"
                  + " <div class='col-2' id='cc_" + i + "' style='display:none;'><div class='dropdown'><button class='btn btn-outline-secondary dropdown-toggle w-100 show' type='button' id='dropdownMenuButton' data-bs-toggle='dropdown' aria-haspopup='true' aria-expanded='true'>Check box Values</button><div class='dropdown-menu w-100 show' aria-labelledby='dropdownMenuButton'><div id='cbv_" + i + "'></div></div></div></div>\n"
                  + " <div class='col-2' id='ff2_" + i + "' style='display:none;'><div class='form-group floating-label' id='ff3_" + i + "'><select class='form-select' name='file_type' id='file_type"+ i +"' ><option value=''>Select</option><option value='pdf'>PDF</option> <option value='jpg|jpeg|png'>JPG|JPEG|PNG</option></select> <label for='file_type"+ i +"' >File Format</label></div></div>\n";          
            }
            table = table   + " </div></div></div></div>";
    }
    table = table + "<div class='row'><h6 class='text-decoration-underline mb-3'>Form Settings</h6>\n"
    	    + " <div class='col-2'><div class='form-group floating-label'><input input type='date' name='from_date' id='from_date' value='"+bean.getFrom_date()+"' class='form-control' placeholder='' /> <label for='from_date'>Expiry From Date</label></div></div> \n"
    	    + " <div class='col-2'><div class='form-group floating-label'><input type='date' name='to_date' id='to_date' value='"+bean.getTo_date()+"' class='form-control' placeholder='' /> <label for='to_date'>ExpiryTo Date</label></div></div> \n"    
    	    + " <div class='col-3'><label>Captcha required?</label>";
    	    if(bean.getCaptcha_key().trim().equalsIgnoreCase("Y")) { 
    table = table + "<div class='btn-group btn-group-sm' role='group'><input type='radio' class='btn-check' name='captcha' id='captcha1' value='Y' checked> <label class='btn btn-outline-primary' for='captcha1'>Yes</label> <input type='radio' class='btn-check' name='captcha' id='captcha2' value='N'> <label class='btn btn-outline-primary' for='captcha2'>No</label></div>";
     	   }else if(bean.getCaptcha_key().trim().equalsIgnoreCase("N")) {     		      
    table = table + "<div class='btn-group btn-group-sm' role='group'><input type='radio' class='btn-check' name='captcha' id='captcha1' value='Y'> <label class='btn btn-outline-primary' for='captcha1'>Yes</label> <input type='radio' class='btn-check' name='captcha' id='captcha2' value='N' checked> <label class='btn btn-outline-primary' for='captcha2'>No</label></div>";
     	   }
    table = table + "</div><div class='col-5'><label>OTP Required to submit this form?</label>";
    	    if(bean.getSms_key().trim().equalsIgnoreCase("Y")) { 
    table = table + "<div class='btn-group btn-group-sm' role='group'><input type='radio' class='btn-check' name='sms' id='sms1' value='Y' checked> <label class='btn btn-outline-primary' for='sms1'>Yes</label> <input type='radio' class='btn-check' name='sms' id='sms2' value='N'> <label class='btn btn-outline-primary' for='sms2'>No</label></div>";
      	   }else if(bean.getSms_key().trim().equalsIgnoreCase("N")) {     		      
    table = table + "<div class='btn-group btn-group-sm' role='group'><input type='radio' class='btn-check' name='sms' id='sms1' value='Y'> <label class='btn btn-outline-primary' for='sms1'>Yes</label> <input type='radio' class='btn-check' name='sms' id='sms2' value='N' checked> <label class='btn btn-outline-primary' for='sms2'>No</label></div>";
      	   }    
    table = table + "</div>\n";
    	        //  + "<h6 class='text-decoration-underline m-3  text-danger'>NOTE:</h6><p class='m-0'>Note1</p><p class='m-0'>Note2</p><p class='m-0'>Note3</p>";
    	    table = table + "</div>";
//     	   }
    formDetails.put("jsonstring", table);
    formDetails.put("success", "success");
     } else {         
         formDetails.put("form_id", form_id);
         formDetails.put("success", "No Data Found");
     }

     
     String jsonArray = "";
     Gson gson = new GsonBuilder().setPrettyPrinting().create();
     jsonArray = gson.toJson(formDetails);
     response.getWriter().println(jsonArray);
 } catch (Exception e) {
	 logger.error("Exception in GenericController::getFormStructure::" + e.getMessage());
     logger.error("Exception in GenericController::getFormStructure::" + e.getCause());
     logger.error("Exception in GenericController::getFormStructure::" + e.getClass() + "::" + e.getLocalizedMessage());
 }
 return null;
 }
	
	
	@RequestMapping(value = "/getListOfRecords", method = RequestMethod.POST)
	public ModelAndView getListOfRecords(Model model,HttpServletRequest request,HttpServletResponse response,HttpSession session) {
     try {
    	// System.out.println("getListOfRecords=====");
     String form_id= request.getParameter("form_id");
     HashMap<String, Object> recordDetails = new HashMap<>();
     java.util.List<JsonBean> list = genericDao.getListOfRecords(form_id,session);
     if (list.size()>0) {
    JsonBean bean = (JsonBean)list.get(0);
    String jsonString = bean.getJsonstring();

    
    recordDetails.put("form_id", form_id);
    recordDetails.put("list", list);
        
    recordDetails.put("success", "success");
     } else {
    	 list = new ArrayList<>();
    	 recordDetails.put("list", list);
    	 recordDetails.put("form_id", form_id);
    	 recordDetails.put("success", "No Data Found");
     }

     String jsonArray = "";
     Gson gson = new GsonBuilder().setPrettyPrinting().create();
     jsonArray = gson.toJson(recordDetails);
     response.getWriter().println(jsonArray);
 } catch (Exception e) {
	 logger.error("Exception in GenericController::getListOfRecords::" + e.getMessage());
     logger.error("Exception in GenericController::getListOfRecords::" + e.getCause());
     logger.error("Exception in GenericController::getListOfRecords::" + e.getClass() + "::" + e.getLocalizedMessage());
 }
 return null;
 }
	
	@RequestMapping(value = "/getDynamicRecord", method = RequestMethod.POST)		
	public ModelAndView getDynamicRecord(Model model,@ModelAttribute("form")JsonBean form,HttpServletRequest request, HttpServletResponse response,HttpSession session) {
    try {
    	
    	CaptchaDto captchaDto = captchaController.generateCaptchaImage(session, request, response, model);
	    // Add the CAPTCHA string to the model
	    model.addAttribute("captchaString", captchaDto.getCaptchaString());
	    session.setAttribute("captchaString", captchaDto.getCaptchaString());
	    // Render the CAPTCHA image
	    byte[] captchaImage = captchaDto.getImage();

	    // Convert the image byte array to a Base64-encoded string
	    String base64Image = Base64.getEncoder().encodeToString(captchaImage);
	    model.addAttribute("captchaImage", base64Image);
	    session.setAttribute("captchaImage", base64Image);

    	     String form_id= request.getParameter("form_id");
    	     String record_id= request.getParameter("record_id");
    	     java.util.List<JsonBean> list = genericDao.getForm(form_id,session);
    	     if (list.size()>0) {
    	    JsonBean bean = (JsonBean)list.get(0);
    	    String jsonString = bean.getJsonstring();    	        	   
        	
        	JSONParser parser = new JSONParser();
            Object obj = parser.parse(jsonString);
            JSONArray array = (JSONArray) obj;            
            JSONArray array1 = new JSONArray();        	
            for (int i = 1; i < array.size(); i++) {
                JSONObject jsonObject = (JSONObject) array.get(i);
                array1.add(i - 1, jsonObject);
            } 
            bean.setCaptchaImage(base64Image);
            int count = genericDao.checkFormExpiryDate(form_id, session);
            if(count>0) {
            java.util.List<JsonBean> list1 = genericDao.getFormData(form_id,record_id,session);                    	
        	ArrayList al = new ArrayList(0);
        	String str = Retrieving_Json_Object.getJsonTable_Fields4(array1,list1,bean);                                  
            request.setAttribute("jsonArray", array1); 
            request.setAttribute("str", str);
            request.setAttribute("record_id", record_id);
            request.setAttribute("form_id", bean.getForm_id());            
            request.setAttribute("form_name", bean.getForm_name());            
            request.setAttribute("fields", bean.getFields());
            request.setAttribute("captcha_key", bean.getCaptcha_key());
            request.setAttribute("sms_key", bean.getSms_key());
            request.setAttribute("flag", "update");
            return new ModelAndView("Generic/data_update_form"); 
        } else {
        	session.setAttribute("msg", "No Data Found");        	
        	return new ModelAndView("redirect:/getDynamicFormList");
        }
    } else {
    	session.setAttribute("msg", "Form will not open, Form Expiry Date was Expired");        	
    	return new ModelAndView("redirect:/getDynamicFormList");
    }
    } catch (Exception e) {
    	logger.error("Exception in GenericController::getDynamicRecord::" + e.getMessage());
        logger.error("Exception in GenericController::getDynamicRecord::" + e.getCause());
        logger.error("Exception in GenericController::getDynamicRecord::" + e.getClass() + "::" + e.getLocalizedMessage());
    }
    return new ModelAndView("Generic/data_update_form");
	}

//------------------------------------------------------	

    @ModelAttribute("StateList")
    public Map<String, String> getStateList() {       
    	java.util.List<CommonBean> stateList = new ArrayList<CommonBean>();        
        Map<String, String> list = new TreeMap<String, String>();
        Map<String, String> statelist = new TreeMap<String, String>();
        try {
        	stateList = genericDao.getStateList();
            int i = 0;
            while (i < stateList.size()) {
                CommonBean e = (CommonBean) stateList.get(i);
                String stateCode = e.getStateCode();
                String stateName = e.getStateName();
                list.put(stateCode, stateName);
                i++;
            }
            statelist = SortDistByValue.getSortedMap(list);
            
        } catch (Exception e) {
            logger.error("Exception in GenericController::getStateList::" + e.getMessage());
            logger.error("Exception in GenericController::getStateList::" + e.getCause());
            logger.error("Exception in GenericController::getStateList::" + e.getClass() + "::" + e.getLocalizedMessage());           
        }
        return statelist;
    }
    @RequestMapping("/getDistrictList")		
	public ModelAndView getDistrictList(Model model,HttpServletRequest request, HttpServletResponse response,HttpSession session) {
     String stateCode = request.getParameter("stateCode");     
     String stringBuffer = "";
     PrintWriter out = null;
     List distList = null;
     try {
    	 out = response.getWriter();
    	 distList = genericDao.getDistrictList(stateCode);
         for (Object object : distList) {
             stringBuffer += object + "##";
         }
         logger.debug("stringBuffer:::" + stringBuffer);
         response.setContentType("text/plain");
         response.setHeader("Pragma", "no-cache");//for HTTP1.0
         response.setHeader("cache-control", "no-cache");// for HTTP/1.1.
         out.write(stringBuffer.toString());
     } catch (IOException  e) {
         logger.error("Exception in GenericController::getDistrictList::" + e.getMessage());
         logger.error("Exception in GenericController::getDistrictList::" + e.getCause());
         logger.error("Exception in GenericController::getDistrictList::" + e.getClass() + "::" + e.getLocalizedMessage());         
     }
     return null;    	
	}
	
	@RequestMapping("/getMandalList")		
	public ModelAndView getMandalList(Model model,HttpServletRequest request, HttpServletResponse response,HttpSession session) {
     String stateCode = request.getParameter("stateCode");
     String distCode = request.getParameter("distCode");
     String stringBuffer = "";
     PrintWriter out = null;
     List mandalList = null;
     try {
    	 out = response.getWriter();
    	 mandalList = genericDao.getMandalList(stateCode,distCode);
         for (Object object : mandalList) {
             stringBuffer += object + "##";
         }
         response.setContentType("text/plain");
         response.setHeader("Pragma", "no-cache");//for HTTP1.0
         response.setHeader("cache-control", "no-cache");// for HTTP/1.1.
         out.write(stringBuffer.toString());
     } catch (IOException  e) {
         logger.error("Exception in GenericController::getMandalList::" + e.getMessage());
         logger.error("Exception in GenericController::getMandalList::" + e.getCause());
         logger.error("Exception in GenericController::getMandalList::" + e.getClass() + "::" + e.getLocalizedMessage());         
     }
     return null;    	
	}
	@RequestMapping("/getVillageList")		
	public ModelAndView getVillageList(Model model,HttpServletRequest request, HttpServletResponse response,HttpSession session) {
     String stateCode = request.getParameter("stateCode");
     String distCode = request.getParameter("distCode");
     String mandalCode = request.getParameter("mandalCode");
     String stringBuffer = "";
     PrintWriter out = null;
     List villList = null;
     try {
    	 out = response.getWriter();
    	 villList = genericDao.getVillageList(stateCode,distCode,mandalCode);
         for (Object object : villList) {
             stringBuffer += object + "##";
         }
         response.setContentType("text/plain");
         response.setHeader("Pragma", "no-cache");//for HTTP1.0
         response.setHeader("cache-control", "no-cache");// for HTTP/1.1.
         out.write(stringBuffer.toString());
     } catch (IOException  e) {
         logger.error("Exception in GenericController::getVillageList::" + e.getMessage());
         logger.error("Exception in GenericController::getVillageList::" + e.getCause());
         logger.error("Exception in GenericController::getVillageList::" + e.getClass() + "::" + e.getLocalizedMessage());         
     }
     return null;    	
	}
	
	@RequestMapping(value = "/ViewFile")
    public ModelAndView ViewFile(Model model, HttpServletRequest request, HttpServletResponse response) {
        try {
            
            ServletOutputStream sos;
            response.setContentType("application/pdf");
            sos = response.getOutputStream();
            String form_id = request.getParameter("form_id");
            String record_id = request.getParameter("record_id");
            String column_id = request.getParameter("column_id");
            
            String filepath = genericDao.getFilePath(form_id,record_id,column_id); 
            
     	   Path path = Paths.get(filepath);
     	   byte[] data = Files.readAllBytes(path); 
            sos.write(data);

            sos.flush();
            sos.close();

        } catch (Exception e) {
            logger.error("Exception in GenericController::ViewFile::" + e.getMessage());
            logger.error("Exception in GenericController::ViewFile::" + e.getCause());
            logger.error("Exception in GenericController::ViewFile::" + e.getClass() + "::" + e.getLocalizedMessage()); 
        }
        return null;
    }
	
	@RequestMapping(value = "/getFormsReportPage", method = RequestMethod.GET)
	public ModelAndView getFormsReportPage(Model model,HttpServletRequest request,HttpServletResponse response,HttpSession session) {	
		java.util.List<JsonBean> list = new ArrayList<JsonBean>();
		ModelAndView mv = null;String msg = "";
		try {
	    list = (ArrayList) genericDao.getFormsList();          	
		request.setAttribute("list",list);
		}catch(Exception e) {
            logger.error("Exception in GenericController::getFormsReportPage::" + e.getMessage());
            logger.error("Exception in GenericController::getFormsReportPage::" + e.getCause());
            logger.error("Exception in GenericController::getFormsReportPage::" + e.getClass() + "::" + e.getLocalizedMessage());
		}		
	    model.addAttribute("form", new JsonBean());
		mv = new ModelAndView("Generic/form_report_page");
		return mv;
	}

	@RequestMapping(value = "/getFormsReport", method = RequestMethod.POST)		
	public ModelAndView getFormsReport(Model model,@ModelAttribute("form")JsonBean form,HttpServletRequest request, HttpServletResponse response,HttpSession session) {
    try {
             String form_id= request.getParameter("form_id");
    	     HashMap<String, Object> formDetails = new HashMap<>();
    	     java.util.List<JsonBean> columnsList = genericDao.getColumnsList(form_id,session);
    	     if(columnsList.size()>0) {
    	     java.util.List<JsonBean> reportList = genericDao.getFormsReport(form_id,session);
    	     	List formslist = (ArrayList) genericDao.getFormsList();
    			request.setAttribute("list",formslist);
    	       
    	     request.setAttribute("columnsList", columnsList);
             request.setAttribute("reportList", reportList);            
             request.setAttribute("form_name", request.getParameter("form_name"));
            return new ModelAndView("Generic/form_report"); 
        } else {      
        	java.util.List<JsonBean> list = new ArrayList<JsonBean>();
        	list = (ArrayList) genericDao.getFormsList();          	
    		request.setAttribute("list",list);
    		
        	request.setAttribute("msg", "No Data Found");        	
        	return new ModelAndView("Generic/form_report_page");
        }
    } catch (Exception e) {
        logger.error("Exception in GenericController::getFormsReport::" + e.getMessage());
        logger.error("Exception in GenericController::getFormsReport::" + e.getCause());
        logger.error("Exception in GenericController::getFormsReport::" + e.getClass() + "::" + e.getLocalizedMessage());    
    }
    return new ModelAndView("Generic/form_report");
	}

	@RequestMapping(value = "/EditFormExpiryDate", method = RequestMethod.GET)
	public ModelAndView editFormExpiryDate(Model model,HttpServletRequest request,HttpServletResponse response) {	
		java.util.List<JsonBean> list = new ArrayList<JsonBean>();
		ModelAndView mv = null;
		try {
	    list = (ArrayList) genericDao.getFormsList();		
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
	    String currentDate = dateFormat.format(new Date());
	    model.addAttribute("currentDate", currentDate);
	    request.setAttribute("currentDate", currentDate);
		request.setAttribute("list",list);
		}catch(Exception e) {
	        logger.error("Exception in GenericController::editFormExpiryDate::" + e.getMessage());
	        logger.error("Exception in GenericController::editFormExpiryDate::" + e.getCause());
	        logger.error("Exception in GenericController::editFormExpiryDate::" + e.getClass() + "::" + e.getLocalizedMessage());
		}		
	    model.addAttribute("form", new JsonBean());
		mv = new ModelAndView("Generic/edit_form_expiry_date");
		return mv;
	}
	
	@RequestMapping(value = "/getFormExpiryDetails", method = RequestMethod.POST)
	public ModelAndView getFormExpiryDetails(Model model,HttpServletRequest request,HttpServletResponse response,HttpSession session) {
     try {
     String form_id= request.getParameter("form_id");
     HashMap<String, Object> formExpiryDetails = new HashMap<>();
     java.util.List<JsonBean> list = genericDao.getFormExpiryDetails(form_id,session);
     if (list.size()>0) {
    JsonBean bean = (JsonBean)list.get(0);
    
    
    formExpiryDetails.put("form_id", form_id);
    formExpiryDetails.put("from_date", bean.getFrom_date());
    formExpiryDetails.put("to_date", bean.getTo_date());          
    
   // System.out.println(bean.getFrom_date()+"==="+bean.getTo_date());
    formExpiryDetails.put("success", "success");
     } else {    	 
    	 formExpiryDetails.put("form_id", form_id);
    	 formExpiryDetails.put("success", "No Data Found");
     }

     String jsonArray = "";
     Gson gson = new GsonBuilder().setPrettyPrinting().create();
     jsonArray = gson.toJson(formExpiryDetails);
     response.getWriter().println(jsonArray);
 } catch (Exception e) {
     logger.error("Exception in GenericController::getFormExpiryDetails::" + e.getMessage());
     logger.error("Exception in GenericController::getFormExpiryDetails::" + e.getCause());
     logger.error("Exception in GenericController::getFormExpiryDetails::" + e.getClass() + "::" + e.getLocalizedMessage());
 }
 return null;
 }
	
	@RequestMapping(value = "/UpdateFormExpiryDate", method = RequestMethod.POST)		
	public ModelAndView updateFormExpiryDate(Model model,@ModelAttribute("form")JsonBean bean,HttpServletRequest request, HttpServletResponse response,HttpSession session) {
		java.util.List<JsonBean> list = new ArrayList<JsonBean>();
    try {
    	list = (ArrayList) genericDao.getList1();         		
		request.setAttribute("list",list);
        	int k =genericDao.updateFormExpiryDate(bean, session);
        	if(k>0) {        	
            request.setAttribute("msg", "Successfully updated the Form Expiry Details");
            model.addAttribute("msg", "Successfully updated the Form Expiry Details");
        } else {
        	request.setAttribute("msg1", "updation Fail");
        	model.addAttribute("msg1", "updation Fail");
        }
        
    } catch (Exception e) {
        logger.error("Exception in GenericController::updateFormExpiryDate::" + e.getMessage());
        logger.error("Exception in GenericController::updateFormExpiryDate::" + e.getCause());
        logger.error("Exception in GenericController::updateFormExpiryDate::" + e.getClass() + "::" + e.getLocalizedMessage());
    }
    model.addAttribute("form", new JsonBean());
    return new ModelAndView("Generic/edit_form_expiry_date");
	}
	
	@RequestMapping(value = "/FormApprovePage", method = RequestMethod.GET)
	public ModelAndView formApprovePage(Model model,HttpServletRequest request,HttpServletResponse response) {	
		java.util.List<JsonBean> list = new ArrayList<JsonBean>();
		ModelAndView mv = null;
		try {
	    list = (ArrayList) genericDao.getList2();
		request.setAttribute("list",list);
		}catch(Exception e) {
	        logger.error("Exception in GenericController::formApprovePage::" + e.getMessage());
	        logger.error("Exception in GenericController::formApprovePage::" + e.getCause());
	        logger.error("Exception in GenericController::formApprovePage::" + e.getClass() + "::" + e.getLocalizedMessage());
		}		
	    model.addAttribute("form", new JsonBean());
		mv = new ModelAndView("Generic/form_approve_page");
		return mv;
	}
	
	@RequestMapping(value = "/getFormApprove", method = RequestMethod.POST)		
	public ModelAndView getFormApprove(Model model,@ModelAttribute("form")JsonBean bean,HttpServletRequest request, HttpServletResponse response,HttpSession session) {
		java.util.List<JsonBean> list = new ArrayList<JsonBean>();
    try {
    	list = (ArrayList) genericDao.getList2();		
		request.setAttribute("list",list);
        	int k =genericDao.updateFormApprove(bean, session);
        	if(k>0) {        	
            request.setAttribute("msg", "Successfully updated the Form Approval");
            model.addAttribute("msg", "Successfully updated the Form Approval");
        } else {
        	request.setAttribute("msg1", "updation Fail");
        	model.addAttribute("msg1", "updation Fail");
        }
        
    } catch (Exception e) {
        logger.error("Exception in GenericController::getFormApprove::" + e.getMessage());
        logger.error("Exception in GenericController::getFormApprove::" + e.getCause());
        logger.error("Exception in GenericController::getFormApprove::" + e.getClass() + "::" + e.getLocalizedMessage());
    }
    model.addAttribute("form", new JsonBean());
    return new ModelAndView("Generic/form_approve_page");
	}
	
	@RequestMapping("/form_Check")		
	public ModelAndView form_Check(Model model,HttpServletRequest request, HttpServletResponse response,HttpSession session) {
     String form_name = request.getParameter("form_name");     
     PrintWriter out = null;
     int status = 0;
     try {
    	 out = response.getWriter();
    	 status = genericDao.getForm_Check(form_name);        
         response.setContentType("text/plain");
         response.setHeader("Pragma", "no-cache");//for HTTP1.0
         response.setHeader("cache-control", "no-cache");// for HTTP/1.1.
         out.write((status + "").toString());
     } catch (IOException  e) {
         logger.error("Exception in GenericController::form_Check::" + e.getMessage());
         logger.error("Exception in GenericController::form_Check::" + e.getCause());
         logger.error("Exception in GenericController::form_Check::" + e.getClass() + "::" + e.getLocalizedMessage());         
     }
     return null;    	
	}
	
	@RequestMapping(value = "/getRecordsList", method = RequestMethod.GET)
	public ModelAndView getRecordsList(Model model,HttpServletRequest request,HttpServletResponse response,HttpSession session) {	
		java.util.List<JsonBean> list = new ArrayList<JsonBean>();
		ModelAndView mv = null;
		try {
	    list = (ArrayList) genericDao.getFormsRecordList();           		
		request.setAttribute("list",list);
		}catch(Exception e) {
	         logger.error("Exception in GenericController::getRecordsList::" + e.getMessage());
	         logger.error("Exception in GenericController::getRecordsList::" + e.getCause());
	         logger.error("Exception in GenericController::getRecordsList::" + e.getClass() + "::" + e.getLocalizedMessage());
		}		
	    model.addAttribute("form", new JsonBean());
		mv = new ModelAndView("Generic/del_records_page");
		return mv;
	}
	
	@RequestMapping(value = "/DeleteFormData", method = RequestMethod.POST)		
	public ModelAndView deleteFormData(Model model,@ModelAttribute("form")JsonBean form,HttpServletRequest request, HttpServletResponse response,HttpSession session) {
		java.util.List<JsonBean> list = new ArrayList<JsonBean>();
    try {
    	     String form_id= request.getParameter("form_id");
    	     String record_id= request.getParameter("record_id");
    	     list = (ArrayList) genericDao.getFormsRecordList();           		
    		 request.setAttribute("list",list);
    		 
    		 int k =genericDao.deleteFormData(form, session);
         	if(k>0) {        	
             request.setAttribute("msg", "Successfully deleted the Form Data");
             model.addAttribute("msg", "Successfully deleted the Form Data");
         } else {
         	request.setAttribute("msg1", "Deletion Fail");
         	model.addAttribute("msg1", "Deletion Fail");
         }
    } catch (Exception e) {
        logger.error("Exception in GenericController::deleteFormData::" + e.getMessage());
        logger.error("Exception in GenericController::deleteFormData::" + e.getCause());
        logger.error("Exception in GenericController::deleteFormData::" + e.getClass() + "::" + e.getLocalizedMessage());
    }
    return new ModelAndView("Generic/del_records_page");
	}

	@RequestMapping(value = "/DeleteFormStructurePage", method = RequestMethod.GET)
	public ModelAndView deleteFormStructurePage(Model model,HttpServletRequest request,HttpServletResponse response) {	
		java.util.List<JsonBean> list = new ArrayList<JsonBean>();
		ModelAndView mv = null;
		try {		
	    list = (ArrayList) genericDao.getList3();
		request.setAttribute("list",list);
		}catch(Exception e) {
			logger.error("Exception in GenericController::deleteFormStructurePage::" + e.getMessage());
	        logger.error("Exception in GenericController::deleteFormStructurePage::" + e.getCause());
	        logger.error("Exception in GenericController::deleteFormStructurePage::" + e.getClass() + "::" + e.getLocalizedMessage());
	    }	
	    model.addAttribute("form", new JsonBean()); 
		mv = new ModelAndView("Generic/del_form_structure");
		return mv;
	}
	
	@RequestMapping(value = "/DeleteFormStructure", method = RequestMethod.POST)		
	public ModelAndView deleteFormStructure(Model model,@ModelAttribute("form")JsonBean form,HttpServletRequest request, HttpServletResponse response,HttpSession session) {
		java.util.List<JsonBean> list = new ArrayList<JsonBean>();
    try {
    	     String form_id= request.getParameter("form_id");
    		 list = (ArrayList) genericDao.getList3();          		
    		 request.setAttribute("list",list);
    		 
    		int k =genericDao.deleteFormStructure(form, session);
         	if(k>0) {        	
             request.setAttribute("msg", "Successfully deleted the Form Structure");
             model.addAttribute("msg", "Successfully deleted the Form Structure");
         } else {
         	request.setAttribute("msg1", "Deletion Fail");
         	model.addAttribute("msg1", "Deletion Fail");
         }
    } catch (Exception e) {
        logger.error("Exception in GenericController::deleteFormStructure::" + e.getMessage());
        logger.error("Exception in GenericController::deleteFormStructure::" + e.getCause());
        logger.error("Exception in GenericController::deleteFormStructure::" + e.getClass() + "::" + e.getLocalizedMessage());
    }
    return new ModelAndView("Generic/del_form_structure");
	}

	@RequestMapping(value = "/getDynamicForm_Preview", method = RequestMethod.GET)		
	public ModelAndView getDynamicForm_Preview(Model model,HttpServletRequest request, HttpServletResponse response,HttpSession session) {
    try {
    	java.util.List<CommonBean> stateList = new ArrayList<CommonBean>();        
        	stateList = genericDao.getStateList();
             String form_id= request.getParameter("form_id");
    	     HashMap<String, Object> formDetails = new HashMap<>();
    	     java.util.List<JsonBean> list = genericDao.getForm(form_id,session);
    	     if (list.size()>0) {
    	    JsonBean bean = (JsonBean)list.get(0);
    	    String jsonString = bean.getJsonstring();    	        	   
        	
        	JSONParser parser = new JSONParser();
            Object obj = parser.parse(jsonString);
            JSONArray array = (JSONArray) obj;            
            JSONArray array1 = new JSONArray();        	
            for (int i = 1; i < array.size(); i++) {
                JSONObject jsonObject = (JSONObject) array.get(i);
                array1.add(i - 1, jsonObject);
            }
        	            
        	       	
        	String str = Retrieving_Json_Object.getJsonTable_Fields2(array1,bean,stateList);
            request.setAttribute("str", str);                         
            request.setAttribute("jsonArray", array1);
            request.setAttribute("str", str);
            request.setAttribute("form_id", bean.getForm_id());
            request.setAttribute("form_name", bean.getForm_name());
            request.setAttribute("fields", bean.getFields());
            request.setAttribute("captcha_key", bean.getCaptcha_key());
            request.setAttribute("flag", "insert");
                       
            return new ModelAndView("Generic/dynamic_form"); 
        } else {
        	session.setAttribute("msg", "New Form Generation Fail");        	
        	return new ModelAndView("redirect:/getDynamicFormList");
        }
    } catch (Exception e) {
    	logger.error("Exception in GenericController::getDynamicForm::" + e.getMessage());
        logger.error("Exception in GenericController::getDynamicForm::" + e.getCause());
        logger.error("Exception in GenericController::getDynamicForm::" + e.getClass() + "::" + e.getLocalizedMessage());
    }
    return new ModelAndView("Generic/dynamic_form");
	}
//----------------------------------------------------------	
//	@RequestMapping(value = "/getCaptcha", method = {  RequestMethod.GET, RequestMethod.POST })
//	public ModelAndView getCaptcha(@RequestParam("_csrf")String csrfToken ,Model model ,HttpServletRequest request, HttpServletResponse response) {
//	System.out.println("getCaptcha===="+csrfToken);
//	HttpSession session = request.getSession(false);
//	if (session == null) {
//		System.out.println("session is null....");
//	    // Handle session not created
//	    // Return an error response or redirect to an error page
//	}
//	boolean valid = false;
//	String jsonArray = "";
//	Map latestCpt = null;
//	Gson gson = new GsonBuilder().setPrettyPrinting().create();	
//	latestCpt = new HashMap();
//	try {
////		String csrfToken = request.getParameter("_csrf");
//		System.out.println("csrfToken====="+csrfToken);
////		if (csrfToken != null) {
////            // Validate the CSRF token
////            if (!csrfToken.getToken().equals(csrfToken.getRequestedToken())) {
////                // CSRF token mismatch, handle accordingly
////                model.addAttribute("error", "CSRF token mismatch");
////                return "error-page";
////            }
////        } else {
////            // CSRF token not found, handle accordingly
////            model.addAttribute("error", "CSRF token not found");
////            return "error-page";
////        }
//		
//		String captchaSession = session.getAttribute("captcha_security").toString(); 
//		System.out.println("getCpatcha===="+captchaSession);
//		latestCpt.put("success", "success");
//		latestCpt.put("captchaSession", captchaSession);
//		jsonArray = gson.toJson(latestCpt);
//		response.getWriter().println(jsonArray);
//	} catch (Exception e) {	
//		logger.error("Exception in GenericController::getCaptcha::" + e.getMessage());
//        logger.error("Exception in GenericController::getCaptcha::" + e.getCause());
//        logger.error("Exception in GenericController::getCaptcha::" + e.getClass() + "::" + e.getLocalizedMessage());
//	} 
//	return null;		
//}
	
	
	@GetMapping("/getCaptcha")
	@ResponseBody
	public ResponseEntity<?> getCaptcha(HttpSession session) {
	    Map<String, String> response = new HashMap<>();
//	    System.out.println("session"+session);
	    String captchaSession = (String) session.getAttribute("captcha_security");

	    response.put("success", "success");
	    response.put("captchaSession", captchaSession);

	    return ResponseEntity.ok(response);
	}
	
	@RequestMapping(value = "/getFormDataPreviewPage", method = RequestMethod.GET)
	public ModelAndView getFormDataPreviewPage(Model model,HttpServletRequest request,HttpServletResponse response,HttpSession session) {	
		java.util.List<JsonBean> list = new ArrayList<JsonBean>();
		ModelAndView mv = null;String msg = "";
		try {
	    list = (ArrayList) genericDao.getFormsRecordList();		
		request.setAttribute("list",list);
		try {
		msg = session.getAttribute("msg").toString();
		session.removeAttribute("msg");
		}catch(Exception e) {}
		request.setAttribute("msg",msg);
		model.addAttribute("msg", msg);
		}catch(Exception e) {
			logger.error("Exception in GenericController::getFormDataPreviewPage::" + e.getMessage());
	        logger.error("Exception in GenericController::getFormDataPreviewPage::" + e.getCause());
	        logger.error("Exception in GenericController::getFormDataPreviewPage::" + e.getClass() + "::" + e.getLocalizedMessage());
	    }	
	    model.addAttribute("form", new JsonBean());
		mv = new ModelAndView("Generic/data_preview_page");
		return mv;
	}
	
	@RequestMapping(value = "/getFormDataPreview", method = {  RequestMethod.GET, RequestMethod.POST })		
	public ModelAndView getFormDataPreview(@RequestParam("_csrf") String csrfToken,Model model,@ModelAttribute("form")JsonBean form,HttpServletRequest request, HttpServletResponse response,HttpSession session) {
    try {
    	     String form_id= request.getParameter("form_id");
    	     String record_id= request.getParameter("record_id");
    	     java.util.List<JsonBean> list = genericDao.getForm(form_id,session);
    	     if (list.size()>0) {
    	    JsonBean bean = (JsonBean)list.get(0);
    	    String jsonString = bean.getJsonstring();    	        	   
        	
        	JSONParser parser = new JSONParser();
            Object obj = parser.parse(jsonString);
            JSONArray array = (JSONArray) obj;            
            JSONArray array1 = new JSONArray();        	
            for (int i = 1; i < array.size(); i++) {
                JSONObject jsonObject = (JSONObject) array.get(i);
                array1.add(i - 1, jsonObject);
            } 
            
            
            java.util.List<JsonBean> list1 = genericDao.getFormData(form_id,record_id,session);                    	
        	ArrayList al = new ArrayList(0);
        	String str = Retrieving_Json_Object.getJsonTable_Data_Preview(array1,list1,bean);                                  
            request.setAttribute("jsonArray", array1); 
            request.setAttribute("str", str);
            request.setAttribute("record_id", record_id);
            request.setAttribute("form_id", bean.getForm_id());            
            request.setAttribute("form_name", bean.getForm_name());            
            request.setAttribute("fields", bean.getFields());
            request.setAttribute("captcha_key", bean.getCaptcha_key());
            request.setAttribute("flag", "update");
            return new ModelAndView("Generic/data_preview"); 
        } else {
        	session.setAttribute("msg", "No Data Found");        	
        	return new ModelAndView("redirect:/getDynamicFormList");
        }
    } catch (Exception e) {
    	logger.error("Exception in GenericController::getFormDataPreview::" + e.getMessage());
        logger.error("Exception in GenericController::getFormDataPreview::" + e.getCause());
        logger.error("Exception in GenericController::getFormDataPreview::" + e.getClass() + "::" + e.getLocalizedMessage());
    }    
    return new ModelAndView("Generic/data_preview");
	}

	@RequestMapping(value = "/FormVerifyPage", method = RequestMethod.GET)
	public ModelAndView FormVerifyPage(Model model,HttpServletRequest request,HttpServletResponse response) {	
		java.util.List<JsonBean> list = new ArrayList<JsonBean>();
		ModelAndView mv = null;
		try {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
	    String currentDate = dateFormat.format(new Date());
	    model.addAttribute("currentDate", currentDate);
	    request.setAttribute("currentDate", currentDate);
	    
	    list = (ArrayList) genericDao.getList();
		request.setAttribute("list",list);
		}catch(Exception e) {
			logger.error("Exception in GenericController::editFormFields::" + e.getMessage());
	        logger.error("Exception in GenericController::editFormFields::" + e.getCause());
	        logger.error("Exception in GenericController::editFormFields::" + e.getClass() + "::" + e.getLocalizedMessage());
	    }		
	    model.addAttribute("form", new JsonBean());
		mv = new ModelAndView("Generic/form_verify_page");
		return mv;
	}
	
	@RequestMapping(value = "/getVerifyFormStructure", method = RequestMethod.POST)
	public ModelAndView getVerifyFormStructure(Model model,HttpServletRequest request,HttpServletResponse response,HttpSession session) {
     try {
     String form_id= request.getParameter("form_id");
     HashMap<String, Object> formDetails = new HashMap<>();
     java.util.List<JsonBean> list = genericDao.getFormStructure(form_id,session);
     if (list.size()>0) {
    JsonBean bean = (JsonBean)list.get(0);
    String jsonString = bean.getJsonstring();
          
    formDetails.put("form_id", bean.getForm_id());
    formDetails.put("form_name", bean.getForm_name());
    formDetails.put("fields", bean.getFields());
    
    String table = "<div>";
    int j =1;
    int no = Integer.parseInt(bean.getFields());
    
    JSONParser parser = new JSONParser();
    Object obj = parser.parse(jsonString);
    JSONArray array = (JSONArray) obj;            
    
    for (int i = 1; i <= no; i++) {
    	JSONObject jsonObject = (JSONObject) array.get(i);
    	String dtype = (String) jsonObject.get("dtype");
    	boolean con = false;
    	StringTokenizer st = new StringTokenizer(bean.getCon_value(),"||");
    	while (st.hasMoreTokens()) {
    		if(dtype.equalsIgnoreCase(st.nextToken())) {
    		con	= true;
    		}else {}}
    	table = table + "<div class='row'><h6 class='text-decoration-underline'>Field " + i + "</h6></div><div class='row m-3 flex-nowrap'> \n";
    			if(con == true) {
    				table = table  + "<div class='col-2'><div class='form-check'><input class='form-check-input' type='checkbox' name='con_check' id='con_check"+ i +"' checked='true'> <label class='form-check-label'for='con_check"+ i +"'> Unique </label></div></div> \n";
    			}else {
    				table = table  + "<div class='col-2'><div class='form-check'><input class='form-check-input' type='checkbox' name='con_check' id='con_check"+ i +"'> <label class='form-check-label'for='con_check"+ i +"'> Unique </label></div></div> \n"; 	
    			} 
                
                table = table  + " <div class='col-2'><div class='floating-label'><input type='text' name='label' class='form-control' required='true' id='label_" + i + "' value='"+jsonObject.get("label")+"' onkeypress='return ValidateAlpha(event)' onblur='return checkString(this, 'Label Name');' minlength='1' maxlength='50' readonly='true'  autocomplete='off' placeholder=''/> <label for='label_" + i + "'>Label Name</label></div></div>  \n"
                		+ " <div class='col-2'><div class='form-group floating-label'><select class='form-select' name='dtype'  id='dtype_" + i + "' required='true' autocomplete='off' onchange ='getShowTypeEdit(this.value, " + i + ")' ><option  value='"+jsonObject.get("dtype")+"'>"+jsonObject.get("dtype")+"</option><option value=''>Select</option><option value='text'>Text</option><option value='number'>Number</option><option value='dropdown'>Drop down</option><option value='radiobutton'>Radio button</option><option value='checkbox'>Check box</option><option value='textarea'>Text area</option><option value='password'>Password</option><option value='email'>Email</option><option value='date'>Date</option><option value='file'>File upload</option><option value='specialcombo'>Special combo</option><option value='mobile'>Mobile</option><option value='panno'>PAN Number</option><option value='aadhaarno'>Aadhaar Number</option><option value='time'>Time</option></select> <label for='dtype_" + i + "'>Data Type</label></div></div>\n"            
                		+ " <div class='col-2'><div class='floating-label'><input type='text' name='minsize' class='form-control'  id='minsize_" + i + "' value='"+jsonObject.get("minsize")+"' required='true' readonly='true' minlength='1' maxlength='3' onkeypress='return isNumber(event)' autocomplete='off' placeholder=''><label for='minsize_\" + i + \"'>Min Size</label></div></div>\n"                
                		+ " <div class='col-2'><div class='floating-label'><input type='text' name='maxsize' placeholder='' class='form-control' id='maxsize_" + i + "' value='"+jsonObject.get("maxsize")+"' required='true' readonly='true' maxlength='3' minlength='1' onkeypress='return isNumber(event)' autocomplete='off'> <label for='maxsize_" + i + "'>Max Size</label></div></div> \n"      
                		+ " <div class='col-2'><div class='form-group floating-label'><select class='form-select' name='mandatory' id='mandatory_" + i + "' required='true'><option value='"+jsonObject.get("mandatory")+"'>"+jsonObject.get("mandatory")+"</option><option value=''>Select</option><option value='true'>True</option><option value='false'>False</option></select> <label for='mandatory_" + i + "'>Mandatory</label></div></div>\n"; 
                                                                              
            if(jsonObject.get("dtype").equals("dropdown")) { 
        table = table + " <div class='col-2' id='sss_" + i + "'><div class='floating-label'id='ssr_" + i + "'><input type='text' name='no1' id='no1_" + i + "' readonly='true' value='"+jsonObject.get("no1")+"' minlength='1' maxlength='2' onkeypress='return isNumber(event)' onchange ='getShow1(this.value," + i + ")' autocomplete='off' placeholder='' class='form-control'/><label for='no1_" + i + "'>No of values</label></div></div>\n"
    	        + " <div class='col-2' id='nnn_" + i + "'><div class='dropdown'><button class='btn btn-outline-secondary dropdown-toggle w-100 show' type='button' id='dropdownMenuButton' data-bs-toggle='dropdown' aria-haspopup='true' aria-expanded='true'>Dropdown Values</button><div class='dropdown-menu w-100 show' aria-labelledby='dropdownMenuButton'><div id='vvv_" + i + "'>";
        JSONArray ss = (JSONArray) jsonObject.get("dropvalues");
        for (int z = 0; z < ss.size(); z++) {
        	//System.out.println(ss.get(z));
        	table = table + "<div class='dropdown-item'><input type='text' name='dropvalues' readonly='true' class='form-control' placeholder='' id='dropvalues_" +  i + "" +  z + "' value='"+ss.get(z)+"' minlength ='1' maxlength='25' onkeypress='return ValidateAlpha(event)' onblur='return checkString(this, 'Drop Value');' autocomplete='off'></div>";
        }
        table = table   + " </div></div></div></div> \n" 
        
+ " <div class='col-2' id='rrr_" + i + "' style='display:none;'><div class='floating-label'id='bbb_" + i + "' style='display:none;'><input type='text' name='no2' id='no2_" + i + "' minlength='1' maxlength='2' onkeypress='return isNumber(event)' onchange ='getShow3(this.value," + i + ")' autocomplete='off' class='form-control'placeholder='No of Values' /> <label for='no2_" + i + "'>No of values</label></div></div> \n"
+ " <div class='col-2' id='rr_" + i + "' style='display:none;'><div class='dropdown'><button class='btn btn-outline-secondary dropdown-toggle w-100 show' type='button' id='dropdownMenuButton' data-bs-toggle='dropdown' aria-haspopup='true' aria-expanded='true'>Radio button Values</button><div class='dropdown-menu w-100 show' aria-labelledby='dropdownMenuButton'><div id='bb_" + i + "'></div></div></div></div>\n"

				  + " <div class='col-2' id='ccc_" + i + "' style='display:none;'><div class='floating-label'id='cb_" + i + "' style='display:none;'><input type='text' name='no3'  id='no3_" + i + "' minlength='1' maxlength='2' onkeypress='return isNumber(event)' onchange ='getShow5(this.value," + i + ")' autocomplete='off' class='form-control'placeholder='No of Values' /> <label for='no3_" + i + "'>No of values</label></div></div> \n"
                  + " <div class='col-2' id='cc_" + i + "' style='display:none;'><div class='dropdown'><button class='btn btn-outline-secondary dropdown-toggle w-100 show' type='button' id='dropdownMenuButton' data-bs-toggle='dropdown' aria-haspopup='true' aria-expanded='true'>Check box Values</button><div class='dropdown-menu w-100 show' aria-labelledby='dropdownMenuButton'><div id='cbv_" + i + "'></div></div></div></div>\n"
                  
                  + " <div class='col-2' id='ff2_" + i + "' style='display:none;'><div class='form-group floating-label' id='ff3_" + i + "'><select class='form-select' name='file_type' id='file_type"+ i +"' ><option value=''>Select</option><option value='pdf'>PDF</option> <option value='jpg|jpeg|png'>JPG|JPEG|PNG</option></select> <label for='file_type"+ i +"' >File Format</label></div></div>\n";
        
            }else if(jsonObject.get("dtype").equals("radiobutton")) {   
        table = table +  " <div class='col-2' id='rrr_" + i + "'><div class='floating-label'id='bbb_" + i + "'><input type='text' name='no2' id='no2_" + i + "' readonly='true' value='"+jsonObject.get("no2")+"' minlength='1' maxlength='2' onkeypress='return isNumber(event)' onchange ='getShow3(this.value," + i + ")' autocomplete='off' placeholder='' class='form-control'/><label for='no2_" + i + "'>No of values</label></div></div>\n"
        + " <div class='col-2' id='rr_" + i + "'><div class='dropdown'><button class='btn btn-outline-secondary dropdown-toggle w-100 show' type='button' id='dropdownMenuButton' data-bs-toggle='dropdown' aria-haspopup='true' aria-expanded='true'>Radio Values</button><div class='dropdown-menu w-100 show' aria-labelledby='dropdownMenuButton'><div id='bb_" + i + "'>";
        
        
        JSONArray nn = (JSONArray) jsonObject.get("radiovalues");
        for (int xx = 0; xx < nn.size(); xx++) {
        	table = table + "<div class='dropdown-item'><input type='text' name='radiovalues' readonly='true' class='form-control' placeholder='' id='radiovalues_" +  i + "" + xx + "' value='"+nn.get(xx)+"' minlength ='1' maxlength='25' onkeypress='return ValidateAlpha(event)' onblur='return checkString(this, 'Radio Value');' autocomplete='off'></div>";
        }
        table = table   + " </div></div></div></div> \n"

				+ " <div class='col-2' id='sss_" + i + "' style='display:none;'><div class='floating-label' id='ssr_" + i + "' style='display:none;'><input type='text' name='no1' id='no1_" + i + "' minlength='1' maxlength='2'onkeypress='return isNumber(event)' onchange ='getShow1(this.value," + i + ")' autocomplete='off' class='form-control'placeholder='No of Values' /> <label for='no1_" + i + "'>No of values</label></div></div> \n"
				+ " <div class='col-2' id='nnn_" + i + "' style='display:none;'><div class='dropdown'><button class='btn btn-outline-secondary dropdown-toggle w-100 show' type='button' id='dropdownMenuButton' data-bs-toggle='dropdown' aria-haspopup='true' aria-expanded='true'>Dropdown Values</button><div class='dropdown-menu w-100 show' aria-labelledby='dropdownMenuButton'><div id='vvv_" + i + "'></div></div></div></div>\n"  
                
                
				+ " <div class='col-2' id='ccc_" + i + "' style='display:none;'><div class='floating-label'id='cb_" + i + "' style='display:none;'><input type='text' name='no3'  id='no3_" + i + "' minlength='1' maxlength='2' onkeypress='return isNumber(event)' onchange ='getShow5(this.value," + i + ")' autocomplete='off' class='form-control'placeholder='No of Values' /> <label for='no3_" + i + "'>No of values</label></div></div> \n"
                  + " <div class='col-2' id='cc_" + i + "' style='display:none;'><div class='dropdown'><button class='btn btn-outline-secondary dropdown-toggle w-100 show' type='button' id='dropdownMenuButton' data-bs-toggle='dropdown' aria-haspopup='true' aria-expanded='true'>Check box Values</button><div class='dropdown-menu w-100 show' aria-labelledby='dropdownMenuButton'><div id='cbv_" + i + "'></div></div></div></div>\n"
                
                  
                  + " <div class='col-2' id='ff2_" + i + "' style='display:none;'><div class='form-group floating-label' id='ff3_" + i + "'><select class='form-select' name='file_type' id='file_type"+ i +"' ><option value=''>Select</option><option value='pdf'>PDF</option> <option value='jpg|jpeg|png'>JPG|JPEG|PNG</option></select> <label for='file_type"+ i +"' >File Format</label></div></div>\n";           
            }else if(jsonObject.get("dtype").equals("checkbox")) {
            	table = table  +  " <div class='col-2' id='ccc_" + i + "'><div class='floating-label' id='cb_" + i + "'><input type='text' name='no3' id='no3_" + i + "' readonly='true' value='"+jsonObject.get("no3")+"' minlength='1' maxlength='2' onkeypress='return isNumber(event)' onchange ='getShow5(this.value," + i + ")' autocomplete='off' placeholder='' class='form-control'/><label for='no3_" + i + "'>No of values</label></div></div>\n"
                        + " <div class='col-2' id='cc_" + i + "'><div class='dropdown'><button class='btn btn-outline-secondary dropdown-toggle w-100 show' type='button' id='dropdownMenuButton' data-bs-toggle='dropdown' aria-haspopup='true' aria-expanded='true'>Checkbox Values</button><div class='dropdown-menu w-100 show' aria-labelledby='dropdownMenuButton'><div id='cbv_" + i + "'>";
    	JSONArray nn2 = (JSONArray) jsonObject.get("checkvalues");
    	for (int mm = 0; mm < nn2.size(); mm++) {
            
            table = table + "<div class='dropdown-item'><input type='text' name='checkvalues' class='form-control' placeholder='' id='checkvalues_" +  i + "" + mm + "' readonly='true' value='"+nn2.get(mm)+"' minlength ='1' maxlength='25' onkeypress='return ValidateAlpha(event)' onblur='return checkString(this, 'Check Value');' autocomplete='off'></div>";
    	}
    	table = table   + " </div></div></div></div> \n"
				+ " <div class='col-2' id='sss_" + i + "' style='display:none;'><div class='floating-label' id='ssr_" + i + "' style='display:none;'><input type='text' name='no1' id='no1_" + i + "' minlength='1' maxlength='2'onkeypress='return isNumber(event)' onchange ='getShow1(this.value," + i + ")' autocomplete='off' class='form-control'placeholder='No of Values' /> <label for='no1_" + i + "'>No of values</label></div></div> \n"
				+ " <div class='col-2' id='nnn_" + i + "' style='display:none;'><div class='dropdown'><button class='btn btn-outline-secondary dropdown-toggle w-100 show' type='button' id='dropdownMenuButton' data-bs-toggle='dropdown' aria-haspopup='true' aria-expanded='true'>Dropdown Values</button><div class='dropdown-menu w-100 show' aria-labelledby='dropdownMenuButton'><div id='vvv_" + i + "'></div></div></div></div>\n"        
                
				+ " <div class='col-2' id='rrr_" + i + "' style='display:none;'><div class='floating-label'id='bbb_" + i + "' style='display:none;'><input type='text' name='no2' id='no2_" + i + "' minlength='1' maxlength='2' onkeypress='return isNumber(event)' onchange ='getShow3(this.value," + i + ")' autocomplete='off' class='form-control'placeholder='No of Values' /> <label for='no2_" + i + "'>No of values</label></div></div> \n"
				+ " <div class='col-2' id='rr_" + i + "' style='display:none;'><div class='dropdown'><button class='btn btn-outline-secondary dropdown-toggle w-100 show' type='button' id='dropdownMenuButton' data-bs-toggle='dropdown' aria-haspopup='true' aria-expanded='true'>Radio button Values</button><div class='dropdown-menu w-100 show' aria-labelledby='dropdownMenuButton'><div id='bb_" + i + "'></div></div></div></div>\n"
                
				+ " <div class='col-2' id='ff2_" + i + "' style='display:none;'><div class='form-group floating-label' id='ff3_" + i + "'><select class='form-select' name='file_type' id='file_type"+ i +"' ><option value=''>Select</option><option value='pdf'>PDF</option> <option value='jpg|jpeg|png'>JPG|JPEG|PNG</option></select> <label for='file_type"+ i +"' >File Format</label></div></div>\n";
    	
            }else if(jsonObject.get("dtype").equals("file")) {           	
                table =table + "<div class='col-2' id='ff2_" + i + "'><div class='form-group floating-label' id='ff3_" + i + "'><select class='form-select' name='file_type' id='file_type"+ i +"' ><option value='"+jsonObject.get("file_type")+"'>"+jsonObject.get("file_type")+"</option><option value='pdf'>PDF</option> <option value='jpg|jpeg|png'>JPG|JPEG|PNG</option></select> <label for='file_type"+ i +"' >File Format</label></div></div> \n"; 
                
                		table = table   + " \n"
                      
					+ " <div class='col-2' id='sss_" + i + "' style='display:none;'><div class='floating-label' id='ssr_" + i + "' style='display:none;'><input type='text' name='no1' id='no1_" + i + "' minlength='1' maxlength='2'onkeypress='return isNumber(event)' onchange ='getShow1(this.value," + i + ")' autocomplete='off' class='form-control'placeholder='No of Values' /> <label for='no1_" + i + "'>No of values</label></div></div> \n"
					+ " <div class='col-2' id='nnn_" + i + "' style='display:none;'><div class='dropdown'><button class='btn btn-outline-secondary dropdown-toggle w-100 show' type='button' id='dropdownMenuButton' data-bs-toggle='dropdown' aria-haspopup='true' aria-expanded='true'>Dropdown Values</button><div class='dropdown-menu w-100 show' aria-labelledby='dropdownMenuButton'><div id='vvv_" + i + "'></div></div></div></div>\n"

                     + " <div class='col-2' id='rrr_" + i + "' style='display:none;'><div class='floating-label'id='bbb_" + i + "' style='display:none;'><input type='text' name='no2' id='no2_" + i + "' minlength='1' maxlength='2' onkeypress='return isNumber(event)' onchange ='getShow3(this.value," + i + ")' autocomplete='off' class='form-control'placeholder='No of Values' /> <label for='no2_" + i + "'>No of values</label></div></div> \n"
                     + " <div class='col-2' id='rr_" + i + "' style='display:none;'><div class='dropdown'><button class='btn btn-outline-secondary dropdown-toggle w-100 show' type='button' id='dropdownMenuButton' data-bs-toggle='dropdown' aria-haspopup='true' aria-expanded='true'>Radio button Values</button><div class='dropdown-menu w-100 show' aria-labelledby='dropdownMenuButton'><div id='bb_" + i + "'></div></div></div></div>\n"   
                        
				+ " <div class='col-2' id='ccc_" + i + "' style='display:none;'><div class='floating-label'id='cb_" + i + "' style='display:none;'><input type='text' name='no3'  id='no3_" + i + "' minlength='1' maxlength='2' onkeypress='return isNumber(event)' onchange ='getShow5(this.value," + i + ")' autocomplete='off' class='form-control'placeholder='No of Values' /> <label for='no3_" + i + "'>No of values</label></div></div> \n"
				+ " <div class='col-2' id='cc_" + i + "' style='display:none;'><div class='dropdown'><button class='btn btn-outline-secondary dropdown-toggle w-100 show' type='button' id='dropdownMenuButton' data-bs-toggle='dropdown' aria-haspopup='true' aria-expanded='true'>Check box Values</button><div class='dropdown-menu w-100 show' aria-labelledby='dropdownMenuButton'><div id='cbv_" + i + "'></div></div></div></div>\n";

            }else if(jsonObject.get("dtype").equals("textarea") || jsonObject.get("dtype").equals("password") || jsonObject.get("dtype").equals("email") || jsonObject.get("dtype").equals("date") ||  jsonObject.get("dtype").equals("text") || jsonObject.get("dtype").equals("number") || jsonObject.get("dtype").equals("mobile") || jsonObject.get("dtype").equals("panno") || jsonObject.get("dtype").equals("aadhaarno") || jsonObject.get("dtype").equals("specialcombo")) {
                    table = table +  " <div class='col-2' id='sss_" + i + "' style='display:none;'><div class='floating-label' id='ssr_" + i + "' style='display:none;'><input type='text' name='no1' id='no1_" + i + "' minlength='1' maxlength='2'onkeypress='return isNumber(event)' onchange ='getShow1(this.value," + i + ")' autocomplete='off' class='form-control'placeholder='No of Values' /> <label for='no1_" + i + "'>No of values</label></div></div> \n"
                        		  + " <div class='col-2' id='nnn_" + i + "' style='display:none;'><div class='dropdown'><button class='btn btn-outline-secondary dropdown-toggle w-100 show' type='button' id='dropdownMenuButton' data-bs-toggle='dropdown' aria-haspopup='true' aria-expanded='true'>Dropdown Values</button><div class='dropdown-menu w-100 show' aria-labelledby='dropdownMenuButton'><div id='vvv_" + i + "'></div></div></div></div>\n"
                                                
					+ " <div class='col-2' id='rrr_" + i + "' style='display:none;'><div class='floating-label'id='bbb_" + i + "' style='display:none;'><input type='text' name='no2' id='no2_" + i + "' minlength='1' maxlength='2' onkeypress='return isNumber(event)' onchange ='getShow3(this.value," + i + ")' autocomplete='off' class='form-control'placeholder='No of Values' /> <label for='no2_" + i + "'>No of values</label></div></div> \n"
					+ " <div class='col-2' id='rr_" + i + "' style='display:none;'><div class='dropdown'><button class='btn btn-outline-secondary dropdown-toggle w-100 show' type='button' id='dropdownMenuButton' data-bs-toggle='dropdown' aria-haspopup='true' aria-expanded='true'>Radio button Values</button><div class='dropdown-menu w-100 show' aria-labelledby='dropdownMenuButton'><div id='bb_" + i + "'></div></div></div></div>\n"

					+ " <div class='col-2' id='ccc_" + i + "' style='display:none;'><div class='floating-label'id='cb_" + i + "' style='display:none;'><input type='text' name='no3'  id='no3_" + i + "' minlength='1' maxlength='2' onkeypress='return isNumber(event)' onchange ='getShow5(this.value," + i + ")' autocomplete='off' class='form-control'placeholder='No of Values' /> <label for='no3_" + i + "'>No of values</label></div></div> \n"
                  + " <div class='col-2' id='cc_" + i + "' style='display:none;'><div class='dropdown'><button class='btn btn-outline-secondary dropdown-toggle w-100 show' type='button' id='dropdownMenuButton' data-bs-toggle='dropdown' aria-haspopup='true' aria-expanded='true'>Check box Values</button><div class='dropdown-menu w-100 show' aria-labelledby='dropdownMenuButton'><div id='cbv_" + i + "'></div></div></div></div>\n"
                  + " <div class='col-2' id='ff2_" + i + "' style='display:none;'><div class='form-group floating-label' id='ff3_" + i + "'><select class='form-select' name='file_type' id='file_type"+ i +"' ><option value=''>Select</option><option value='pdf'>PDF</option> <option value='jpg|jpeg|png'>JPG|JPEG|PNG</option></select> <label for='file_type"+ i +"' >File Format</label></div></div>\n";          
            }
            table = table   + " </div></div></div></div>";
    }
    table = table + "<div class='row'><h6 class='text-decoration-underline mb-3'>Form Settings</h6>\n"
    	    + " <div class='col-2'><div class='form-group floating-label'><input input type='date' name='from_date' id='from_date' value='"+bean.getFrom_date()+"' class='form-control' placeholder='' /> <label for='from_date'>Expiry From Date</label></div></div> \n"
    	    + " <div class='col-2'><div class='form-group floating-label'><input type='date' name='to_date' id='to_date' value='"+bean.getTo_date()+"' class='form-control' placeholder='' /> <label for='to_date'>ExpiryTo Date</label></div></div> \n"    
    	    + " <div class='col-3'><label>Captcha required?</label>";
    	    if(bean.getCaptcha_key().trim().equalsIgnoreCase("Y")) { 
     		   table = table+ "<div class='btn-group btn-group-sm' role='group'><input type='radio' class='btn-check' name='captcha'id='captcha1' value='Y' checked> <label class='btn btn-outline-primary' for='captcha1'>Yes</label> <input type='radio' class='btn-check' name='captcha' id='captcha2' value='N'> <label class='btn btn-outline-primary' for='captcha2'>No</label></div>";
     	   }else if(bean.getCaptcha_key().trim().equalsIgnoreCase("N")) {     		      
    table = table  + " <div class='btn-group btn-group-sm' role='group'><input type='radio' class='btn-check' name='captcha'id='captcha1' value='Y'> <label class='btn btn-outline-primary' for='captcha1'>Yes</label> <input type='radio' class='btn-check' name='captcha' id='captcha2' value='N' checked> <label class='btn btn-outline-primary' for='captcha2'>No</label></div>"    	    
    	    + " </div>\n";
     	   }
    table = table + "</div>";
    table = table + " <div class='col-5'><label>OTP Required to submit this form?</label>"; 
    	    if(bean.getSms_key().trim().equalsIgnoreCase("Y")) { 
      		   table = table+ "<div class='btn-group btn-group-sm' role='group'><input type='radio' class='btn-check' name='sms' id='sms1' value='Y' checked> <label class='btn btn-outline-primary' for='sms1'>Yes</label> <input type='radio' class='btn-check' name='sms' id='sms2' value='N'> <label class='btn btn-outline-primary' for='sms2'>No</label></div>";
      	   }else if(bean.getSms_key().trim().equalsIgnoreCase("N")) {     		      
     table = table  + " <div class='btn-group btn-group-sm' role='group'><input type='radio' class='btn-check' name='sms' id='sms1' value='Y'> <label class='btn btn-outline-primary' for='sms1'>Yes</label> <input type='radio' class='btn-check' name='sms' id='sms2' value='N' checked> <label class='btn btn-outline-primary' for='sms2'>No</label></div>"    	    
     	    + " </div>\n";
      	   }
//    	    + "<h6 class='text-decoration-underline m-3  text-danger'>NOTE:</h6><p class='m-0'>Note1</p><p class='m-0'>Note2</p><p class='m-0'>Note3</p>";
    	   table = table + "</div>";     	   
    formDetails.put("jsonstring", table);
    formDetails.put("success", "success");
     } else {         
         formDetails.put("form_id", form_id);
         formDetails.put("success", "No Data Found");
     }

     String jsonArray = "";
     Gson gson = new GsonBuilder().setPrettyPrinting().create();
     jsonArray = gson.toJson(formDetails);
     response.getWriter().println(jsonArray);
 } catch (Exception e) {
	 logger.error("Exception in GenericController::getVerifyFormStructure::" + e.getMessage());
     logger.error("Exception in GenericController::getVerifyFormStructure::" + e.getCause());
     logger.error("Exception in GenericController::getVerifyFormStructure::" + e.getClass() + "::" + e.getLocalizedMessage());
 }
 return null;
 }
	
	@RequestMapping(value = "/getFormVerify", method = RequestMethod.POST)		
	public ModelAndView getFormVerify(Model model,@ModelAttribute("form")JsonBean bean,HttpServletRequest request, HttpServletResponse response,HttpSession session) {
		java.util.List<JsonBean> list = new ArrayList<JsonBean>();
    try {
    	list = (ArrayList) genericDao.getList();		
		request.setAttribute("list",list);
        	int k =genericDao.updateFormVerify(bean, session);
        	if(k>0) {        	
            request.setAttribute("msg", "Successfully updated the Form Verify");
            model.addAttribute("msg", "Successfully updated the Form Verify");
        } else {
        	request.setAttribute("msg1", "updation Fail");
        	model.addAttribute("msg1", "updation Fail");
        }
        
    } catch (Exception e) {
        logger.error("Exception in GenericController::getFormVerify::" + e.getMessage());
        logger.error("Exception in GenericController::getFormVerify::" + e.getCause());
        logger.error("Exception in GenericController::getFormVerify::" + e.getClass() + "::" + e.getLocalizedMessage());
    }
    model.addAttribute("form", new JsonBean());
    return new ModelAndView("Generic/form_verify_page");
	}
	
	
//--------------------------------------------Public-Form-----------------------------------------------
	
	
//	@RequestMapping(value = "/getPublicForm", method = RequestMethod.GET)		
//	public ModelAndView getPublicForm(Model model,HttpServletRequest request, HttpServletResponse response,HttpSession session1) {
//		String username = "", msg = "";
//		String form_id = "1";
//		try {
//            msg = session1.getAttribute("msg").toString(); 
//            }catch(Exception e) {}
//			session1.invalidate();
//		HttpSession session = request.getSession();
//		ModelAndView modelAndView = new ModelAndView();
//		try {    	
//			UserDetails userDetails = loginController.getUserDetails();
//			try {
//			username = userDetails.getUsername();
//			}catch(Exception ex) {}
//		List<WebMenuBean> list1 = loginDaoImpl.getDynamicMenuList(username);
//		session.setAttribute("menulist", list1);
//		session.setAttribute("username", "");
//		session.setAttribute("lang",lang);
//		this.role=service.getRole(username);
//		session.setAttribute("role", service.getRole(username));
//		service.role=role;
//    	
//    	CaptchaDto captchaDto = captchaController.generateCaptchaImage(session, request, response, model);
//	    model.addAttribute("captchaString", captchaDto.getCaptchaString());
//	    session.setAttribute("captchaString", captchaDto.getCaptchaString());
//	    byte[] captchaImage = captchaDto.getImage();
//	    String base64Image = Base64.getEncoder().encodeToString(captchaImage);
//	    model.addAttribute("captchaImage", base64Image);
//	    session.setAttribute("captchaImage", base64Image);    	
////             String form_id= request.getParameter("form_id");
//    	     HashMap<String, Object> formDetails = new HashMap<>();
//    	     int count  = genericDao.checkFormExpiryDate(form_id,session);
//    	     if(count>0) {
//    	     java.util.List<JsonBean> list = genericDao.getForm(form_id,session);
//    	     if (list.size()>0) {
//    	    JsonBean bean = (JsonBean)list.get(0);
//    	    String jsonString = bean.getJsonstring();    	        	   
//        	
//        	JSONParser parser = new JSONParser();
//            Object obj = parser.parse(jsonString);
//            JSONArray array = (JSONArray) obj;            
//            JSONArray array1 = new JSONArray();        	
//            for (int i = 1; i < array.size(); i++) {
//                JSONObject jsonObject = (JSONObject) array.get(i);
//                array1.add(i - 1, jsonObject);
//            }	            
//        	bean.setCaptchaImage(base64Image);       	
//        	String str = Retrieving_Json_Object.getJsonTable_Fields2(array1,bean,stateList);
//            request.setAttribute("str", str);                         
//            request.setAttribute("jsonArray", array1);
//            request.setAttribute("form_id", bean.getForm_id());
//            request.setAttribute("form_name", bean.getForm_name());
//            request.setAttribute("fields", bean.getFields());
//            request.setAttribute("captcha_key", bean.getCaptcha_key());
//            request.setAttribute("flag", "insert");
//            request.setAttribute("msg", msg);
//            
//            return new ModelAndView("Generic/travel_form"); 
//        } else {
//        	request.setAttribute("msg", "New Form Generation Fail");        	
//        	return new ModelAndView("Generic/travel_form");
//        }
//    } else {
//    	request.setAttribute("msg", "Form will not open, Form Expiry Date was Expired");        	
//    	return new ModelAndView("Generic/travel_form");
//    }
//        
//    } catch (Exception e) {
//    	e.printStackTrace();
//    	logger.error("Exception in GenericController::getPublicForm::" + e.getMessage());
//        logger.error("Exception in GenericController::getPublicForm::" + e.getCause());
//        logger.error("Exception in GenericController::getPublicForm::" + e.getClass() + "::" + e.getLocalizedMessage());
//    }
//    return new ModelAndView("Generic/travel_form");
//	}
	
//-------------------------------Newly-Added------------------------	
	@GetMapping(value = "/getForm")
	public ModelAndView getForm(Model model,@RequestParam("form_id")String form_id,HttpServletRequest request, HttpServletResponse response,HttpSession session) {
	try {		
        session.setAttribute("form_id", form_id);
        session.setAttribute("msg", "");
	} catch (Exception e) {
    	logger.error("Exception in GenericController::getForm::" + e.getMessage());
        logger.error("Exception in GenericController::getForm::" + e.getCause());
        logger.error("Exception in GenericController::getForm::" + e.getClass() + "::" + e.getLocalizedMessage());
    }
	return new ModelAndView("redirect:/getPublicForm");
	}
	
	@RequestMapping(value = "/getPublicForm", method = RequestMethod.GET)		
	public ModelAndView getPublicForm(Model model,HttpServletRequest request, HttpServletResponse response,HttpSession session) {
		String username = "", msg = "";
//		String form_id = "1";
		String form_id = "";
		try {
            msg = session.getAttribute("msg").toString();
            form_id = session.getAttribute("form_id").toString();
            }catch(Exception e) {}
			session.removeAttribute("form_id");
//			session1.invalidate();
//		HttpSession session = request.getSession();
		request.setAttribute("msg", msg);
		request.setAttribute("flag", "insert");
		ModelAndView modelAndView = new ModelAndView();
		try { 
			UserDetails userDetails = loginController.getUserDetails();
			try {
			username = userDetails.getUsername();
			}catch(Exception ex) {
			}
		List<WebMenuBean> list1 = loginDaoImpl.getDynamicMenuList(username,lang);
		session.setAttribute("menulist", list1);
		if (!username.equalsIgnoreCase("") && !username.equalsIgnoreCase("null")) {
			session.setAttribute("username", username);
		} else {
			session.setAttribute("username", "");
		}
		session.setAttribute("lang",lang);
		this.role=service.getRole(username);
		session.setAttribute("role", service.getRole(username));
		service.role=role;
		System.out.println("form_id===="+form_id);
		String number = "^[0-9]*$";
		Pattern pattern = Pattern.compile(number);
        Matcher matcher = pattern.matcher(form_id);
        if (!matcher.matches()) {
        	request.setAttribute("str", "");                         
	         request.setAttribute("jsonArray", "");
	         request.setAttribute("form_id", "");
	         request.setAttribute("form_name", "");
	         request.setAttribute("fields", "");
	         request.setAttribute("captcha_key", "");
	         
	     	request.setAttribute("msg", "Invalid Form");        	
	     	return new ModelAndView("Generic/travel_form");
      }
    	
    	CaptchaDto captchaDto = captchaController.generateCaptchaImage(session, request, response, model);
	    model.addAttribute("captchaString", captchaDto.getCaptchaString());
	    session.setAttribute("captchaString", captchaDto.getCaptchaString());
	    byte[] captchaImage = captchaDto.getImage();
	    String base64Image = Base64.getEncoder().encodeToString(captchaImage);
	    model.addAttribute("captchaImage", base64Image);
	    session.setAttribute("captchaImage", base64Image);    	
//             String form_id= request.getParameter("form_id");
    	     HashMap<String, Object> formDetails = new HashMap<>();
    	     int count  = genericDao.checkFormExpiryDate(form_id,session);
    	     if(count == 200) {
    	    	 request.setAttribute("str", "");                         
    	         request.setAttribute("jsonArray", "");
    	         request.setAttribute("form_id", "");
    	         request.setAttribute("form_name", "");
    	         request.setAttribute("fields", "");
    	         request.setAttribute("captcha_key", "");
    	         
    	     	request.setAttribute("msg", "Form does not exist");        	
    	     	return new ModelAndView("Generic/travel_form");
    	     }else if(count>0) {
    	     java.util.List<JsonBean> list = genericDao.getForm(form_id,session);
    	     if (list.size()>0) {
    	    JsonBean bean = (JsonBean)list.get(0);
    	    String jsonString = bean.getJsonstring();    	        	   
        	
        	JSONParser parser = new JSONParser();
            Object obj = parser.parse(jsonString);
            JSONArray array = (JSONArray) obj;            
            JSONArray array1 = new JSONArray();        	
            for (int i = 1; i < array.size(); i++) {
                JSONObject jsonObject = (JSONObject) array.get(i);
                array1.add(i - 1, jsonObject);
            }	            
        	bean.setCaptchaImage(base64Image);       	
        	String str = Retrieving_Json_Object.getJsonTable_Fields2(array1,bean,stateList);
            request.setAttribute("str", str);                         
            request.setAttribute("jsonArray", array1);
            request.setAttribute("form_id", bean.getForm_id());
            request.setAttribute("form_name", bean.getForm_name());
            request.setAttribute("fields", bean.getFields());
            request.setAttribute("captcha_key", bean.getCaptcha_key());
            request.setAttribute("sms_key", bean.getSms_key());
            
            
            
            return new ModelAndView("Generic/travel_form"); 
        } else {
        	request.setAttribute("str", "");                         
            request.setAttribute("jsonArray", "");
            request.setAttribute("form_id", "");
            request.setAttribute("form_name", "");
            request.setAttribute("fields", "");
            request.setAttribute("captcha_key", "");
        	
        	request.setAttribute("msg", "New Form Generation Fail");        	
        	return new ModelAndView("Generic/travel_form");
        }
    } else {
    	request.setAttribute("str", "");                         
        request.setAttribute("jsonArray", "");
        request.setAttribute("form_id", "");
        request.setAttribute("form_name", "");
        request.setAttribute("fields", "");
        request.setAttribute("captcha_key", "");
        
    	request.setAttribute("msg", "Form will not open, Form Expiry Date was Expired");        	
    	return new ModelAndView("Generic/travel_form");
    }
        
    } catch (Exception e) {
    	logger.error("Exception in GenericController::getPublicForm::" + e.getMessage());
        logger.error("Exception in GenericController::getPublicForm::" + e.getCause());
        logger.error("Exception in GenericController::getPublicForm::" + e.getClass() + "::" + e.getLocalizedMessage());
    }
    return new ModelAndView("Generic/travel_form");
	}
	
//----------------------------End----------------------------------		

//	@RequestMapping(value = "/insertPublicFormData", method = RequestMethod.POST)
//	public ModelAndView insertPublicFormData(Model model,@Valid @ModelAttribute("form") JsonBean bean,@RequestParam("_csrf")String csrfToken ,BindingResult result,HttpServletRequest request, HttpServletResponse response,HttpSession session) {	
//		String jsonString = ""; 
//	try {
////-----------------Newly-added----------------------		
//		session.setAttribute("form_id", bean.getForm_id().toString().trim());
////---------------------------------------------------		
//		String captcha = (String) session.getAttribute("captcha_security");		
//		if(bean.getCaptcha_key().equalsIgnoreCase("Y")) {
//		if (!bean.getCaptcha().equalsIgnoreCase(captcha)) {
//			session.setAttribute("msg", "Invalid Captcha");
//			return new ModelAndView("redirect:/getPublicForm");
//		}
//		}
//		jsonString = bean.getJsonData().toString();				
//        String mykey = "RLBS";
//        String finalJson = mykey + HashEncrypter.getHashforLogin1(jsonString);
//        String jsonString1 = jsonString;
//        int m = 0;
//        if (finalJson.equals(bean.getJsonCheck())) {           
//        GenericValidationService gvs = new GenericValidationService();
//        List err = gvs.validate(bean);
//        ObjectError error ; 
//        if(err.size()>0) {
//            error = new ObjectError("global", err.toString());
//            result.addError(error);
//        }           
//        
//        System.out.println("Controller===="+result.hasErrors());
//        if(result.hasErrors()) {             
//    	    session.setAttribute("msg", err);                        
//            return new ModelAndView("redirect:/getPublicForm"); 
//        }
//        
//        String name = bean.getForm_name().replaceAll("\\s", "");
//        int no = Integer.parseInt(bean.getFields());
//        JSONParser parser = new JSONParser();
//        Object obj = parser.parse(bean.getJsonFields());
//        JSONArray array = (JSONArray) obj;
//        
//        JSONParser parser1 = new JSONParser();
//        Object obj3 = parser1.parse(jsonString);
//        JSONArray array1 = (JSONArray) obj3;                       	        	           
//        	
//        	   JSONArray jsonArray = new JSONArray();
//               JSONObject obj2 = new JSONObject();
//               for (int i = 0; i < no; i++) {
//            	   JSONObject jsonObject1 = (JSONObject) array.get(i);
//                   JSONObject obj1 = null;
//                   String data = null;                   
//                   String dtype = (String) jsonObject1.get("dtype");
//                   String label = (String) jsonObject1.get("label");
//                     if (dtype.equals("dropdown") || dtype.equals("radiobutton") || dtype.equals("textarea") || dtype.equals("password") || dtype.equals("email") || dtype.equals("date") || dtype.equals("number") || dtype.equals("mobile") || dtype.equals("panno") || dtype.equals("aadhaarno") || dtype.equals("file") || dtype.equals("text") || dtype.equals("time")) {
//                       obj1 = (JSONObject) array1.get(i);               
//                       data = (String) obj1.get(label);
//                       obj2.put(label, data);
//                     } else if (dtype.equals("checkbox")) {
//                       obj1 = (JSONObject) array1.get(i);        
//                       ArrayList<String> list = (ArrayList<String>) obj1.get(label);
//                       if(list.size()>0) {
//                       String array2[] = new String[list.size()];
//                       for (int j = 0; j < list.size(); j++) {
//                           array2[j] = list.get(j);
//                           data = (String) array2[j];
//                           obj2.put(label, data);
//                       }
//                     }else {
//                    	 obj2.put(label, ""); 
//                     }
//                   } else if (dtype.equals("specialcombo")) {
//                       obj1 = (JSONObject) array1.get(i);         
//                       data = (String) obj1.get("stateCode");                     
//                       obj2.put("stateCode", data);
//                       obj2.put("stateName", bean.getStateName());  
//                       data = (String) obj1.get("distCode");                       
//                       obj2.put("distCode", data);
//                       obj2.put("distName", bean.getDistName());                          
//                       data = (String) obj1.get("mandalCode");               
//                       obj2.put("mandalCode", data);
//                       obj2.put("mandalName", bean.getMandalName());                          
//                       data = (String) obj1.get("villCode");           
//                       obj2.put("villCode", data);
//                       obj2.put("villName", bean.getVillName());                         
//                   }                                                                                     
//               }
//               m = genericDao.insertFormData(array,obj2,bean.getForm_name(),bean);                 
//               } else {
//               session.setAttribute("msg", "Authentication Fail");
//               return new ModelAndView("redirect:/getPublicForm");
//               }
//        		if(m == 200) {
//        			session.setAttribute("msg", "Duplicate Data is not allowed");
//    	            model.addAttribute("msg", "Duplicate Data is not allowed");	
//        		}else if(m>0) {
//	            session.setAttribute("msg", "Successfully Stored the Data");
//	            model.addAttribute("msg", "Successfully Stored the Data");
//	            }else {
//	            session.setAttribute("msg", "Data Insertion Fail");
//	            model.addAttribute("msg", "Data Insertion Fail"); 
//	            }	        	         
//    } catch (Exception e) {
//    	e.printStackTrace();
//    	logger.error("Exception in GenericController::insertPublicFormData::" + e.getMessage());
//        logger.error("Exception in GenericController::insertPublicFormData::" + e.getCause());
//        logger.error("Exception in GenericController::insertPublicFormData::" + e.getClass() + "::" + e.getLocalizedMessage());
//    }
//	return new ModelAndView("redirect:/getPublicForm");
//	}
//--------------------------------------------	
	
	@RequestMapping(value = "/insertPublicFormData", method = RequestMethod.POST)
	public ModelAndView insertPublicFormData(Model model,@Valid @ModelAttribute("form") JsonBean bean,@RequestParam("_csrf")String csrfToken ,BindingResult result,HttpServletRequest request, HttpServletResponse response,HttpSession session) {	
		String jsonString = "",mobileno = "",otp_verify = ""; 
	try {
//-----------------Newly-added----------------------		
		session.setAttribute("form_id", bean.getForm_id().toString().trim());
//---------------------------------------------------		
		try {
		String captcha = (String) session.getAttribute("captcha_security");		
		if(bean.getCaptcha_key().equalsIgnoreCase("Y")) {
		if (!bean.getCaptcha().equalsIgnoreCase(captcha)) {
			session.setAttribute("msg", "Invalid Captcha");
			return new ModelAndView("redirect:/getPublicForm");
		}
		}
		if(bean.getSms_key().equalsIgnoreCase("Y")) {
		int otp = (Integer) session.getAttribute("otp");
		if (otp == bean.getOtp() && isValidOtp()) {
			otp_verify = "Y";
		} else {        	
        	session.setAttribute("msg", "Invalid OTP");
			return new ModelAndView("redirect:/getPublicForm");
        }
		}
		}catch(Exception e) {
			otp_verify = "N";
		}
		jsonString = bean.getJsonData().toString();				
        String mykey = "RLBS";
        String finalJson = mykey + HashEncrypter.getHashforLogin1(jsonString);
        String jsonString1 = jsonString;
        int m = 0;
        System.out.println("finalJson==="+finalJson.toString()+"===JsonCheck==="+bean.getJsonCheck());
        if (finalJson.equals(bean.getJsonCheck())) {           
        GenericValidationService gvs = new GenericValidationService();
        List err = gvs.validate(bean);
        ObjectError error ; 
        if(err.size()>0) {
            error = new ObjectError("global", err.toString());
            result.addError(error);
        }           
        
        if(result.hasErrors()) {             
    	    session.setAttribute("msg", err);                        
            return new ModelAndView("redirect:/getPublicForm"); 
        }
        
        String name = bean.getForm_name().replaceAll("\\s", "");
        int no = Integer.parseInt(bean.getFields());
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(bean.getJsonFields());
        JSONArray array = (JSONArray) obj;
        
        JSONParser parser1 = new JSONParser();
        Object obj3 = parser1.parse(jsonString);
        JSONArray array1 = (JSONArray) obj3;                       	        	           
        	
        	   JSONArray jsonArray = new JSONArray();
               JSONObject obj2 = new JSONObject();
               for (int i = 0; i < no; i++) {
            	   JSONObject jsonObject1 = (JSONObject) array.get(i);
                   JSONObject obj1 = null;
                   String data = null;                   
                   String dtype = (String) jsonObject1.get("dtype");
                   String label = (String) jsonObject1.get("label");
                     if (dtype.equals("dropdown") || dtype.equals("radiobutton") || dtype.equals("textarea") || dtype.equals("password") || dtype.equals("email") || dtype.equals("date") || dtype.equals("number") || dtype.equals("mobile") || dtype.equals("panno") || dtype.equals("aadhaarno") || dtype.equals("file") || dtype.equals("text") || dtype.equals("time")) {                    	 
                       obj1 = (JSONObject) array1.get(i);               
                       data = (String) obj1.get(label);
                       obj2.put(label, data);
                       if(dtype.equals("mobile")) { 
                      	 mobileno = data.trim();
                      	 }                     
                     } else if (dtype.equals("checkbox")) {
                       obj1 = (JSONObject) array1.get(i);        
                       ArrayList<String> list = (ArrayList<String>) obj1.get(label);
                       if(list.size()>0) {
                       String array2[] = new String[list.size()];
                       for (int j = 0; j < list.size(); j++) {
                           array2[j] = list.get(j);
                           data = (String) array2[j];
                           obj2.put(label, data);
                       }
                     }else {
                    	 obj2.put(label, ""); 
                     }
                   } else if (dtype.equals("specialcombo")) {
                       obj1 = (JSONObject) array1.get(i);         
                       data = (String) obj1.get("stateCode");                     
                       obj2.put("stateCode", data);
                       obj2.put("stateName", bean.getStateName());  
                       data = (String) obj1.get("distCode");                       
                       obj2.put("distCode", data);
                       obj2.put("distName", bean.getDistName());                          
                       data = (String) obj1.get("mandalCode");               
                       obj2.put("mandalCode", data);
                       obj2.put("mandalName", bean.getMandalName());                          
                       data = (String) obj1.get("villCode");           
                       obj2.put("villCode", data);
                       obj2.put("villName", bean.getVillName());                         
                     } else if (dtype.equals("otp")) {                    	 
                         obj2.put(label, otp_verify);   
                      }                                                                                     
               }
               
//               if(bean.getSms_key().equalsIgnoreCase("Y")) {               
////--------------------SMS-Code--------------------------------------------
//            	   			int otp = getGenOTP();
//            	   			System.out.println("otp==="+otp);
//                            session.setAttribute("otp", otp);
//                            String msg = "" + otp;
//                            try {
////                             smsService.sendSms("7013790343", "Please enter OTP "+otp+" for verifying your mobile number to participate in SVPNPA 75 Day Fitness Programme-SVPNPA");
////                             smsService.sendSms("7013790343", "Welcome to SVPNPA your OTP is "+otp+"");
//                            } catch (Exception rt) {
//                            } 
//                            request.setAttribute("form_name", bean.getForm_name());
//                            request.setAttribute("phone", mobileno.substring(6, 10)); 
//                            bean.setPhone(mobileno.substring(6, 10));
//                            session.setAttribute("array", array);
//                            session.setAttribute("obj2", obj2);
//                            session.setAttribute("bean", bean);
//                            return new ModelAndView("Generic/otp_verify");
////----------------------------------------------------------------------
//               }else {
//            	   obj2.put("Otp_Verify", "N");
                   m = genericDao.insertFormData(array,obj2,bean.getForm_name(),bean);
	               if(m == 200) {
	       			session.setAttribute("msg", "Duplicate Data is not allowed");
	   	            model.addAttribute("msg", "Duplicate Data is not allowed");	
	       			}else if(m>0) {
		            session.setAttribute("msg", "Successfully Stored the Data");
		            model.addAttribute("msg", "Successfully Stored the Data");
		            }else {
		            session.setAttribute("msg", "Data Insertion Fail");
		            model.addAttribute("msg", "Data Insertion Fail"); 
		            }
//                  }
               
               } else {
               session.setAttribute("msg", "Authentication Fail");
               return new ModelAndView("redirect:/getPublicForm");
               }        			        	        
    } catch (Exception e) {
    	e.printStackTrace();
    	logger.error("Exception in GenericController::insertPublicFormData::" + e.getMessage());
        logger.error("Exception in GenericController::insertPublicFormData::" + e.getCause());
        logger.error("Exception in .GenericController::insertPublicFormData::" + e.getClass() + "::" + e.getLocalizedMessage());
    }
	return new ModelAndView("redirect:/getPublicForm");
	}
	
//	@RequestMapping(value = "/otpVerify", method = RequestMethod.POST)
//	public ModelAndView otpVerify(Model model,@ModelAttribute("otp") JsonBean form,@RequestParam("_csrf")String csrfToken ,BindingResult result,HttpServletRequest request, HttpServletResponse response) {	
//		HttpSession session = request.getSession(false);
//		int m = 0;
//		JsonBean bean = (JsonBean) session.getAttribute("bean");
//		int otp = (int) session.getAttribute("otp");
//		System.out.println("otp==="+otp+"===="+form.getOtp());
//	try {
//        if (otp == form.getOtp()) {
////            int i = dao.otpVerify(form);
////        	int i = 0;
////            if (i > 0) {
////            } else {
////                request.setAttribute("msg", "Sorry..! Verification Failed. Please try again");
////                request.setAttribute("form_name", bean.getForm_name());
////                request.setAttribute("phone", bean.getPhone());
////                return new ModelAndView("Generic/otp_verify", "form", new JsonBean());
////            }
//        	JSONArray array = (JSONArray) session.getAttribute("array");
//        	JSONObject obj2 = (JSONObject) session.getAttribute("obj2");
//        	obj2.put("Otp_Verify", "Y");
//        	m = genericDao.insertFormData(array,obj2,bean.getForm_name(),bean);
//            if(m == 200) {
//    			session.setAttribute("msg", "Duplicate Data is not allowed");
//	            model.addAttribute("msg", "Duplicate Data is not allowed");	
//    			}else if(m>0) {
//	            session.setAttribute("msg", "Successfully Mobile OTP Verified and Stored the Data");
//	            model.addAttribute("msg", "Successfully Stored the Data");
//	            }else {
//	            session.setAttribute("msg", "Data Insertion Fail");
//	            model.addAttribute("msg", "Data Insertion Fail"); 
//	            }
//        	
//            return new ModelAndView("redirect:/getPublicForm");
//        } else {
//            request.setAttribute("msg", "Wrong OTP. Please enter Valid OTP");
//            request.setAttribute("form_name", bean.getForm_name());
//            request.setAttribute("phone", bean.getPhone());
//            return new ModelAndView("Generic/otp_verify", "form", new JsonBean());
//        }
//
//	} catch (Exception e) {
//		logger.error("Exception in GenericController::otpVerify::" + e.getMessage());
//        logger.error("Exception in GenericController::otpVerify::" + e.getCause());
//        logger.error("Exception in GenericController::otpVerify::" + e.getClass() + "::" + e.getLocalizedMessage());
//        
//        request.setAttribute("form_name", bean.getForm_name());
//        request.setAttribute("phone", bean.getPhone());
//        return new ModelAndView("Generic/otp_verify");
//    }
//	
//	}
//	
//	@RequestMapping(value = "/ResendOTP", method = RequestMethod.GET)
//	public void resendOTP(Model model,@ModelAttribute("otp") JsonBean form,@RequestParam("_csrf")String csrfToken ,BindingResult result,HttpServletRequest request, HttpServletResponse response) {	
//		HttpSession session = request.getSession(false);
//	
//		String json = "";
//        try {           
////--------------------SMS-Code--------------------------------------------
//   			int otp = getGenOTP();
//   			System.out.println("ResendOTP==="+otp);
//            session.setAttribute("otp", otp);
//            String msg = "" + otp;
//            try {
////             smsService.sendSms("7013790343", "Please enter OTP "+otp+" for verifying your mobile number to participate in SVPNPA 75 Day Fitness Programme-SVPNPA");
////             smsService.sendSms("7013790343", "Welcome to SVPNPA your OTP is "+otp+"");
//             
//            } catch (Exception rt) {
//            } 
//            json = "" + 200;
//        
//        response.setContentType("application/json");
//        response.getWriter().write(json);
//        }catch (Exception e) {
//			logger.error("Exception in GenericController::resendOTP::" + e.getMessage());
//	        logger.error("Exception in GenericController::resendOTP::" + e.getCause());
//	        logger.error("Exception in GenericController::resendOTP::" + e.getClass() + "::" + e.getLocalizedMessage());	        	        
//	    }
//		
//		}
//        
//	@RequestMapping(value = "/generateOtp", method = RequestMethod.GET)
//	public void generateOtp(Model model,@ModelAttribute("form") JsonBean form,@RequestParam("_csrf")String csrfToken ,@RequestParam("mobile")String mobile ,BindingResult result,HttpServletRequest request, HttpServletResponse response) {	
//		HttpSession session = request.getSession(false);
//	
//		String json = "";
//        try {           
////--------------------SMS-Code--------------------------------------------
//        	System.out.println("mobile===="+mobile);
//        	if(mobile.equalsIgnoreCase("") || mobile.equalsIgnoreCase("null")) {
//        		json = "" + 100 + "$" + "Mobile No is Empty";	
//        	}else {
//   			int otp = getGenOTP();
//   			System.out.println("generateOtp==="+otp);
//            session.setAttribute("otp", otp);
//            String msg = "" + otp;
//            long remainingTime = 0;
//            try {
////             smsService.sendSms("7013790343", "Please enter OTP "+otp+" for verifying your mobile number to participate in SVPNPA 75 Day Fitness Programme-SVPNPA");
//                smsService.sendSms(mobile, "Welcome to SVPNPA your OTP is "+otp+"" , otp);            	
//            	model.addAttribute("otp", otp);
//            } catch (Exception rt) {
//            } 
//            json = "" + 200;
//        	}
//        
//        response.setContentType("application/json");
//        response.getWriter().write(json);
//        }catch (Exception e) {
//			logger.error("Exception in GenericController::generateOtp::" + e.getMessage());
//	        logger.error("Exception in GenericController::generateOtp::" + e.getCause());
//	        logger.error("Exception in GenericController::generateOtp::" + e.getClass() + "::" + e.getLocalizedMessage());	        	        
//	    }
//		
//		}

    public  int getGenOTP() {
        Random r = new Random();
        int numbers = 100000 + (int) (r.nextFloat() * 899900);
        return numbers;
    }

//   ---------------------------------------------------
    @PostMapping("/generateOtp")
    @ResponseBody
    public String generateOtp(@RequestParam("_csrf")String csrfToken ,@RequestParam("mobile")String mobile,HttpServletRequest request ) {
        // Generate a new OTP
    	HttpSession session = request.getSession(false);
    	int otp = getGenOTP();
    	System.out.println("otp==="+otp);
        generatedOtp = generateRandomOtp();
		      try {
		    //smsService.sendSms("7013790343", "Your OTP is "+otp+" for submitting the request. The OTP is valid for 10 minutes -SVPNPA");
		    	  smsService.sendSms(mobile.trim(), "Your OTP is "+otp+" for submitting the request. The OTP is valid for 10 minutes -SVPNPA");
		    
//    	    smsService.sendSms(mobile, "Welcome to SVPNPA your OTP is "+otp+"" , otp);            	   	
		   } catch (Exception rt) {
		   }    	
    	generatedOtp = ""+otp;
        otpCreationTime = System.currentTimeMillis();
        session.setAttribute("otp", otp);
        return generatedOtp;
    }

    @PostMapping("/validateOtp")
    @ResponseBody
    public String validateOtp(@RequestParam("_csrf")String csrfToken ,@RequestParam ("enteredOtp")String enteredOtp,HttpServletRequest request) {
        // Validate the OTP
    	HttpSession session = request.getSession(false);
    	String otp = session.getAttribute("otp").toString(); 
//        if (generatedOtp != null && generatedOtp.equals(enteredOtp) && isValidOtp()) {
    	if (generatedOtp != null && generatedOtp.equals(enteredOtp)) {
        	if(isValidOtp()) {
        	 return "valid";
        	}else {
        	 return "invalid OTP";	
        	}
        } else {
            return "Wrong OTP";
        }
    }

    private String generateRandomOtp() {
        // Generate a 6-digit random OTP
        return String.valueOf((int) (Math.random() * 900000) + 100000);
    }

    private boolean isValidOtp() {
        // Check if the OTP is still valid (within 10 minutes)
        return System.currentTimeMillis() - otpCreationTime <= 10 * 60 * 1000;
    }

//--------------------------------------------------------    
	
}