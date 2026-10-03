/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nic.svpnpa.util;

import com.nic.svpnpa.controller.GenericController;
import com.nic.svpnpa.model.CommonBean;
import com.nic.svpnpa.model.JsonBean;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.Set;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.mock.web.*;
import org.springframework.web.multipart.MultipartFile;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

/**
 *
 * @author crd
 */
public class Retrieving_Json_Object {
	private static final Logger logger = LogManager.getLogger(Retrieving_Json_Object.class);
  
//    ---------------------------------New-Design-----------------------------
    
    public static String getJsonTable_Fields2(JSONArray array, JsonBean bean, List stateList) {
		String str = "", str6 = "";
		int ab = 1, z = 0, x = 0;
		String str1 = "<html><head>" + "</head><body>" + "<div class='container'>";
		String str5 = "</div></body></html>";
		try {
			if (array.size() > 0) {
				for (int i = 0; i < array.size(); i++) {
					JSONObject JsonObject = (JSONObject) array.get(i);
					String label = (String) JsonObject.get("label");
					String dtype = (String) JsonObject.get("dtype");

					if (i % 3 == 0) {
						str6 = str6 + "<div class='row my-3 g-2 d-flex'>";
					}
//             if(ab==1){
//             str6 = str6 + "<tr>";
//             }
					if (dtype.equalsIgnoreCase("radiobutton")) {
						ArrayList<String> list1 = (ArrayList<String>) JsonObject.get("radiovalues");
						String array2[] = new String[list1.size()];
						for (int j = 0; j < list1.size(); j++) {
							array2[j] = list1.get(j);
						}
						str6 = str6 + "<div class='col-4'>" + label + "" +"<br>";
						for (String n : array2) {
							str6 = str6
									+ "<div class='form-check form-check-inline'><input class='form-check-input' type='radio' name='radio_button_"
									+ i + "' id='radio_button_" + i + "' value='" + n
									+ "'><label class='form-check-label' for='radio_button_" + i + "'>" + n
									+ "</label>"									
									+ "</div>";
						}
						str6 = str6 + "</div>";
					} else if (dtype.equalsIgnoreCase("dropdown")) {
						JsonObject = (JSONObject) array.get(i);
						ArrayList<String> list = (ArrayList<String>) JsonObject.get("dropvalues");
						String array1[] = new String[list.size()];
						for (int j = 0; j < list.size(); j++) {
							array1[j] = list.get(j);
						}
						str6 = str6
								+ "<div class='col-4'><div class='form-group floating-label'><select class='form-select' name='dropdown' id='dropdown_"
								+ i + "'><option value=''>Select</option>";
						for (String k : array1) {
							str6 = str6 + "<option value='" + k + "'>" + k + "</option>";
						}

						str6 = str6 + "</select> <label for='dropdown_" + i + "'>" + label + "</label></div></div>";
					} else if (dtype.equalsIgnoreCase("checkbox")) {
						JsonObject = (JSONObject) array.get(i);
						ArrayList<String> list2 = (ArrayList<String>) JsonObject.get("checkvalues");
						String array3[] = new String[list2.size()];
						for (int j = 0; j < list2.size(); j++) {
							array3[j] = list2.get(j);
						}
						str6 = str6 + "<div class='col-4'>" + label + ""+"<br>";
						for (String m : array3) {
							str6 = str6 + "<div class='form-check form-check-inline'>\r\n"
									+ "  <input class='form-check-input' type='checkbox' name='check_box_" + i
									+ "' value='" + m + "' id='check_box_" + i + "'>\r\n"
									+ "  <label class='form-check-label' for='check_box_" + i + "'>" + m
									+ "</label>\r\n" + "</div>\r\n";
						}
						str6 = str6 + "</div>";

					} else if (dtype.equalsIgnoreCase("textarea")) {
						JsonObject = (JSONObject) array.get(i);
						str6 = str6
								+ "<div class='col-4'><div class='form-group floating-label'><textarea type='text' class='form-control' placeholder='"
								+ label + "' name='textarea' id='textarea_" + i
								+ "' maxlength='1000' onblur='return textarea(this, '" + label
								+ "');' size='50px'></textarea><label for='textarea-placeholder'>" + label
								+ "</label></div></div>";
					} else if (dtype.equalsIgnoreCase("password")) {
						JsonObject = (JSONObject) array.get(i);
						str6 = str6
								+ "<div class='col-4'><div class='floating-label'><input type='password' name='password' id='password_"+ i
								+ "'   AUTOCOMPLETE='OFF'  label='FormName' class='form-control' placeholder='Enter'/> <label for='password_"
								+ i + "'>" + label + "</label></div></div>";
					} else if (dtype.equalsIgnoreCase("email")) {
						JsonObject = (JSONObject) array.get(i);
						str6 = str6
								+ "<div class='col-4'><div class='floating-label'><input type='email' name='email' id='email_"
								+ i
								+ "' maxlength='50' onblur='return ValidateEmail(this);'  AUTOCOMPLETE='OFF'  label='FormName' class='form-control' placeholder='Enter'/> <label for='email_"
								+ i + "'>" + label + "</label></div></div>";
					} else if (dtype.equalsIgnoreCase("date")) {
						JsonObject = (JSONObject) array.get(i);
						str6 = str6 + "<div class='col-4'><div class='floating-label'><input type='date' name='date" + x
								+ "' id='date" + x
								+ "' minlength='10' maxlength='10'  AUTOCOMPLETE='OFF' label='FormName' class='form-control' placeholder='Enter'/> <label for='date"
								+ x + "'>" + label + "</label></div></div>";
						x++;
					} else if (dtype.equalsIgnoreCase("number")) {
						JsonObject = (JSONObject) array.get(i);
						str6 = str6
								+ "<div class='col-4'><div class='floating-label'><input type='text' name='number' id='number_"
								+ i
								+ "' minlength='1' maxlength='10' onkeypress='return isNumber(event)'  AUTOCOMPLETE='OFF' label='FormName' class='form-control' placeholder='Enter'/> <label for='number_"
								+ i + "'>" + label + "</label></div>"
								+ "</div>";
					} else if (dtype.equalsIgnoreCase("panno")) {
						JsonObject = (JSONObject) array.get(i);
						str6 = str6
								+ "<div class='col-4'><div class='floating-label'><input type='text' name='panno' id='panno_"
								+ i
								+ "' minlength='10' maxlength='10' style='text-transform:uppercase;' onkeyup='return wordCapital(this)'  AUTOCOMPLETE='OFF' label='FormName' class='form-control' placeholder='Enter'/> <label for='panno_"
								+ i + "'>" + label + "</label></div></div>";
					} else if (dtype.equalsIgnoreCase("aadhaarno")) {
						JsonObject = (JSONObject) array.get(i);
						str6 = str6
								+ "<div class='col-4'><div class='floating-label'><input type='text' name='aadhaarno' id='aadhaarno_"
								+ i
								+ "' minlength='12' maxlength='12' onkeypress='return isNumber(event)' onchange='return aadharValid(this)'  AUTOCOMPLETE='OFF'  label='FormName' class='form-control' placeholder='Enter'/> <label for='aadhaarno_"
								+ i + "'>" + label + "</label></div></div>";
					} else if (dtype.equalsIgnoreCase("mobile")) {
						JsonObject = (JSONObject) array.get(i);
						str6 = str6
								+ "<div class='col-4'><div class='floating-label'><input type='text' name='mobile' id='mobile_"
								+ i
								+ "' onkeypress='return isNumber(event)' onblur='return checkmobile(this);' minlength='10' maxlength='10' AUTOCOMPLETE='OFF'  label='FormName' class='form-control' placeholder='Enter'/> <label for='mobile_"
								+ i + "'>" + label + "</label></div></div>";
					} else if (dtype.equalsIgnoreCase("file")) {
						JsonObject = (JSONObject) array.get(i);
						str6 = str6 + "<div class='col-4'><div class='floating-label'><input type='file' name='file" + z
								+ "' id='file" + z
								+ "' accept='JPG/JPEG/PNG/PDF/*   label='FormName'  class='form-control' /> <label for='file" + z + "'>" + label+ "</label></div>"                    
								+ "<input type='hidden' name='base64' id='base64_" + i + "' />" + "</div>";

						z++;
					} else if (dtype.equalsIgnoreCase("text")) {
						JsonObject = (JSONObject) array.get(i);
						str6 = str6
								+ "<div class='col-4'><div class='floating-label'><input type='text' class='form-control' name='text' id='text_"
								+ i + "' onkeypress='return ValidateAlpha(event)' onblur='return checkString(this, '"
								+ label
								+ "');'  AUTOCOMPLETE='OFF'  label='FormName' placeholder='Enter'/> <label for='text_"
								+ i + "'>" + label + "</label>"
								+ "</div></div>";
					} else if (dtype.equalsIgnoreCase("specialcombo")) {
						JsonObject = (JSONObject) array.get(i);
						str6 = str6 + "<div class='col-4'><div class='form-group floating-label'><select class='form-select' name='stateCode' id='stateCode'><option value=''>Select State</option>";
						for (int u = 0; u < stateList.size(); u++) {
							CommonBean cb = (CommonBean) stateList.get(u);
							str6 = str6 + "<option value='" + cb.getStateCode() + "'>" + cb.getStateName()+ "</option>";
						}
						str6 = str6 + "</select> <label for='stateCode'>State</label></div></div>";
						str6 = str6	+ "<div class='col-4'><div class='form-group floating-label'><select class='form-select' name='distCode' id='distCode'><option value=''>Select District</option></select> <label for='distCode'>District</label></div></div>";
						str6 = str6 + "<div class='col-4'><div class='form-group floating-label'><select class='form-select' name='mandalCode' id='mandalCode'><option value=''>Select Mandal</option></select> <label for='mandalCode'>Mandal</label></div></div>";
						str6 = str6 + "<div class='col-4'><div class='form-group floating-label'><select class='form-select' name='villCode' id='villCode'><option value=''>Select Village</option></select> <label for='villCode'>Village</label></div></div>";
					} else if (dtype.equalsIgnoreCase("time")) {
						JsonObject = (JSONObject) array.get(i);
						str6 = str6
								+ "<div class='col-4'><div class='floating-label'><input type='text' name='time' id='time_"
								+ i
								+ "' minlength='5' maxlength='5' onkeypress='return isNumberwithColan(event)' onblur='return ValidateTime(this);'  AUTOCOMPLETE='OFF' label='FormName' class='form-control' placeholder='Enter Time format like hh:mm(24 Hours format) '/> <label for='time_"
								+ i + "'>" + label + "</label></div>"
								+ "</div>";
//-------------------------------SMS-Code------------------------------
					} else if (dtype.equalsIgnoreCase("otp")) {
						JsonObject = (JSONObject) array.get(i);
						str6 = str6
								+ "<div class='col-4'><div class='floating-label'><input type='text' name='otp' id='otp_"
								+ i
								+ "' minlength='6' maxlength='6' onkeypress='return isNumber(event)' onblur='validateOtp()' AUTOCOMPLETE='OFF' label='FormName' class='form-control' placeholder='Please enter 6 digit OTP'/> <label for='otp"
								+ i + "'>" + label + "</label></div>"								
								+ "</div>"								
								+ "<div class='col-4'><div class='floating-label'>"
								+ "<label for='Click here to Generate OTP' id='otphider1'>Click here to Generate OTP</label>&nbsp;&nbsp;"
								+ "<i onclick='generateOTP()' id='otphider'> <input type='button' name='Generate OTP' value='Generate OTP' class='btn btn-primary'/></i>"								
								+ "<i onclick='generateOTP()' id='reotphider' style='display:none;'> <input type='button' name='Re-Generate OTP' value='Re-Generate OTP' class='btn btn-primary'/></i>"								
								+ "<div id='timer'></div>"
								+ "</div></div>";
//---------------------------------------------------------------------						
					} else {}
					if (i % 3 == 2) {					
						str6 = str6 + "</div>";
					}
				}
				if (array.size() % 2 == 1) {					
					str6 = str6 + "</div>";
				}
//-----------------------Captcha-Img-------------------------------------
//				if (bean.getCaptcha_key().trim().equalsIgnoreCase("Y")) {
//					str6 = str6 + "<div class='mx-auto mt-5 mb-2 col-4'>\r\n" + "<div class='input-group mb-3'>\r\n"
//							+ " <div class='form-group floating-label'><input type='text' class='form-control' name='captcha'  id='captcha' AUTOCOMPLETE='OFF' placeholder='Enter Captcha' aria-label='Enter Captcha' onkeypress='return ValidateAlphaNumeric(event)' style='text-transform:uppercase;'><label for='captcha'>Enter Captcha</label></div>\r\n"
//							+ " <img id='ssss' src='captcha'/><span class='input-group-text'><i class='fas fa-arrows-rotate' onclick='DrawCaptcha()'></i></span>\r\n"
//							+ "</div></div>";
//				} 			  <!-- Display the captcha image using Thymeleaf -->
//			    <img th:src="'data:image/jpeg;base64,' + ${bean.captchaImage}" alt="Captcha Image">

//				<div class='captchaHolder'><img id='captchaImage' th:src=\"@{'data:image/jpeg;base64,' + ${bean.captchaImage}}\" alt='CAPTCHA Image' /> "			    
				if (bean.getCaptcha_key().trim().equalsIgnoreCase("Y")) {
					/*
					 * str6 = str6 +
					 * "<div class='row'><div class='mx-auto mt-3 col-3'><div class='captchaHolder'><img id='captchaImage' th:src=\"@{'data:image/jpeg;base64,' + '"
					 * +bean.getCaptchaImage()+"'}\" " +
					 * "alt='CAPTCHA Image' /><i class='fas fa-volume-high cptIcons' onclick='getAudioCaptcha()'></i><i class='fas fa-arrows-rotate cptIcons' onclick='DrawCaptcha()'></i></div></div></div>"
					 * +
					 * "<div class='mx-auto mt-3 mb-2 col-3'><div class='form-group floating-label'><input type='text' class='form-control' name='captcha'  id='captcha' AUTOCOMPLETE='OFF' placeholder='Enter Captcha' aria-label='Enter Captcha' onkeypress='return ValidateAlphaNumeric(event)' style='text-transform:uppercase;' onkeyup='return wordCapital(this)'><label for='captcha'>Enter Captcha</label></div>\r\n"
					 * + "</div></div>";																																	  th:src=\"@{'data:image/jpeg;base64,' + ${bean.getCaptchaImage()}}\"			
					 */

					 str6 = str6 + "<div class='row'><div class='mx-auto mt-3 col-3'><div class='captchaHolder'><img id='captchaImage' src=\"data:image/jpeg;base64, "+bean.getCaptchaImage()+" \" alt='CAPTCHA Image' /><i class='fas fa-volume-high cptIcons' onclick='getAudioCaptcha()'></i><i class='fas fa-arrows-rotate cptIcons' onclick='DrawCaptcha()'></i></div></div></div>" +
						    "<div class='mx-auto mt-3 mb-2 col-3'><div class='form-group floating-label'><input type='text' class='form-control' name='captcha'  id='captcha' minlength='6' maxlength='6' AUTOCOMPLETE='OFF' placeholder='Enter Captcha' aria-label='Enter Captcha' onkeypress='return ValidateAlphaNumeric(event)' style='text-transform:uppercase;' onkeyup='return wordCapital(this)'><label for='captcha'>Enter Captcha</label></div>\r\n" +
						    "</div></div>";


				}
//--------------------------------------------------------------- 
				str6 = str6 + "<div th:if=\"${#fields.hasErrors('global')}\">\r\n"
						+ "    <p th:each=\"err : ${#fields.errors('global')}\" th:text=\"${err}\" class=\"error\" />\r\n"
						+ "</div>";
//-------------------------------SMS-Code------------------------------	
				if (bean.getSms_key().trim().equalsIgnoreCase("Y")) {
				str6 = str6 + "<p><span class='text-decoration-underline  text-danger'>NOTE:</span> The OTP is valid for 10 minutes</p>";
				}
//---------------------------------------------------------------
				str6 = str6 + "<div class='mx-auto col-1 mt-3 mb-3'>"
						+ "<input type='submit' name='Submit' value='Submit'  class='btn btn-primary'/>" + "</div>"
						+ "</div>";

				str = str1 + str6 + str5;
			} else {
				str6 = str6
						+ "<p><span style=\"font-size: 12pt; font-family: 'Arial','sans-serif';\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color='red'><strong>Form Doesn't contain Fields..</strong></font></span></p><br/>";
				str = str1 + str6 + str5;
			}
		} catch (Exception e) {
			logger.error("Error in Retrieving_Json_Object::getJsonTable_Fields2::" + e.getMessage());
	        logger.error("Error in Retrieving_Json_Object::getJsonTable_Fields2::" + e.getCause());
	        logger.error("Error in Retrieving_Json_Object::getJsonTable_Fields2::" + e.getClass() + "::" + e.getLocalizedMessage());
	    }

		return str;
	}

    public static String getJsonTable_Fields4(JSONArray array, List list5, JsonBean form) {
		String str = "", str6 = "";
		int ab = 1, z = 0, x = 0, jj = 0;
		String str1 =  "<html><head>"
                + "</head><body>"                               
                + "<div class='container'>";                                                        

   String str5 =  "</div></body></html>"; 
		try {
			if (array.size() > 0) {
				for (int i = 0; i < array.size(); i++) {
					JsonBean bean = (JsonBean) list5.get(i);
					JSONObject JsonObject = (JSONObject) array.get(i);
					String label = (String) JsonObject.get("label");
					String dtype = (String) JsonObject.get("dtype");
					String value = "";
					try {
					value = bean.getColumn_value().toString();
					}catch(Exception e) {}
					if (i % 3 == 0) {
						str6 = str6 + "<div class='row my-3 g-2 d-flex'>";
					}
//             if(ab==1){
//             str6 = str6 + "<tr>";
//             }
					if(dtype.equalsIgnoreCase("radiobutton") && label.equalsIgnoreCase(bean.getColumn_name()))  {           
		            	 ArrayList<String> list1 = (ArrayList<String>) JsonObject.get("radiovalues");
		              String array2[] = new String[list1.size()];
		             for (int j = 0; j < list1.size(); j++){array2[j] = list1.get(j);}
		        str6 =  str6 + "<div class='col-4'>"+ label + "";
		            for (String n : array2) {
		            	if(value.equalsIgnoreCase(n)) {
		            		str6 =  str6 +"<div class='form-check'><input class='form-check-input' type='radio' name='radio_button_"+ i +"' checked='true' readonly='true' id='radio_button_"+ i +"' value='"+ n +"'><label class='form-check-label' for='radio_button_"+ i +"'>"+ n + "</label></div>";
		            	} else {
		            		str6 =  str6 +"<input type='radio' name='radio_button_"+ i +"' id='radio_button_"+ i +"' value='"+ n +"'/><b>"+ n + "</b>";	
		            	} 
		            }
		        str6 =  str6 + "</div>"; 
		             } else if(dtype.equalsIgnoreCase("dropdown") && label.equalsIgnoreCase(bean.getColumn_name())){                    
		            	 JsonObject = (JSONObject) array.get(i);
		                ArrayList<String> list = (ArrayList<String>) JsonObject.get("dropvalues");                
		                String array1[] = new String[list.size()];
		                for (int j = 0; j < list.size(); j++){array1[j] = list.get(j);}                     
		        str6 = str6 + "<div class='col-4'><div class='form-group floating-label'><select class='form-select' name='dropdown' id='dropdown_"+ i +"'><option value=''>Select</option>";
		                for (String k : array1) {
		                	if(value.equalsIgnoreCase(k)) {
		        str6 =  str6	+ "<option value='"+bean.getColumn_value()+"' selected>"+bean.getColumn_value()+"</option>";
		                	}else {
		        str6 =  str6 + "<option value='"+ k +"'>"+ k + "</option>";
		                	}
		                }
		                str6 =  str6 + "</select> <label for='dropdown_"+ i +"'>"+ label + "</label></div></div>";
		                } else if(dtype.equalsIgnoreCase("checkbox") && label.equalsIgnoreCase(bean.getColumn_name())){            
		                	JsonObject = (JSONObject) array.get(i);
		            ArrayList<String> list2 = (ArrayList<String>) JsonObject.get("checkvalues");
		            String array3[] = new String[list2.size()];
		            for (int j = 0; j < list2.size(); j++){array3[j] = list2.get(j);}
		            str6 = str6 +"<div class='col-4'>"+ label + "";
		                for (String m : array3) {
		                if(value.equalsIgnoreCase(m)) {
		                	str6 =  str6 + "<div class='form-check'>\r\n"
		                    		+ "  <input class='form-check-input' type='checkbox' name='check_box_"+ i +"' value='"+ m +"' id='check_box_"+ i +"' checked='true'><label class='form-check-label' for='check_box_"+ i +"'>"+ m + "</label>\r\n"
		                    		+ "</div>\r\n";       
		                }else {
		                	str6 =  str6 + "<div class='form-check'>\r\n"
		                    		+ "  <input class='form-check-input' type='checkbox' name='check_box_"+ i +"' value='"+ m +" id='check_box_"+ i +"' ><label class='form-check-label' for='check_box_"+ i +"'>"+ m + "</label>\r\n"
		                    		+ "</div>\r\n";
		                }
		                }
		        str6 =  str6 + "</div>";   
		            } else if(dtype.equalsIgnoreCase("textarea") && label.equalsIgnoreCase(bean.getColumn_name())){                           
		            	JsonObject = (JSONObject) array.get(i);
		            	str6= str6 +"<div class='col-4'><div class='form-group floating-label'>"
		            			+ "<textarea name='textarea' id='textarea_"+ i +"' onblur='return textarea(this, '"+ label + "');' maxlength='1000' size='50px' class='form-control' placeholder=''>" +bean.getColumn_value()+ "</textarea>"
		            			+ "<label for='textarea_"+ i +"'>"+ label + "</label></div></div>";
		            } else if(dtype.equalsIgnoreCase("password") && label.equalsIgnoreCase(bean.getColumn_name())){                                            
		            	  JsonObject = (JSONObject) array.get(i);
		            	  str6 =  str6 + "<div class='col-4'><div class='floating-label'><input type='password' name='password' id='password_"+ i +"'   AUTOCOMPLETE='OFF'  label='FormName' class='form-control' placeholder='' value='"+bean.getColumn_value()+"'/> <label for='password_"+ i +"'>"+ label + "</label></div></div>";
		            } else if(dtype.equalsIgnoreCase("email") && label.equalsIgnoreCase(bean.getColumn_name())){                                                          
		            	   JsonObject = (JSONObject) array.get(i);
		            	   str6 =  str6 + "<div class='col-4'><div class='floating-label'><input type='email' name='email' id='email_"+ i +"' maxlength='50' onblur='return ValidateEmail(this);'  AUTOCOMPLETE='OFF'  label='FormName' class='form-control' placeholder='' value='"+bean.getColumn_value()+"'/> <label for='email_"+ i +"'>"+ label + "</label></div></div>";
		            } else if(dtype.equalsIgnoreCase("date") && label.equalsIgnoreCase(bean.getColumn_name())){               
		                JsonObject = (JSONObject) array.get(i);
		                str6 =  str6 + "<div class='col-4'><div class='floating-label'><input type='date' name='date"+ x +"' id='date"+ x +"' minlength='10' maxlength='10'  AUTOCOMPLETE='OFF' label='FormName' class='form-control' placeholder='' value='"+bean.getColumn_value()+"'/> <label for='date"+ x +"'>"+ label + "</label></div></div>";
		        		x++;
		               } else if(dtype.equalsIgnoreCase("file") && label.equalsIgnoreCase(bean.getColumn_name())){                                            
		            	   JsonObject = (JSONObject) array.get(i);
						   String encodedString = "";
						   if(!value.equalsIgnoreCase("") && !value.equalsIgnoreCase("null")) {
						   Path path = Paths.get(bean.getColumn_value());
						   byte[] data = Files.readAllBytes(path);            	               	            	  
						   encodedString = Base64.getEncoder().encodeToString(data);            	               	               	   
						   MultipartFile img = new MockMultipartFile("123456789.jpg", path.getFileName().toString(), "image/jpg", data);             	   
						   }
						   String file_type = (String)JsonObject.get("file_type");
            	   
		            	   str6 =  str6 + "<div class='col-4 d-flex justify-content-between'><div class='floating-label'><input type='file' name='file"+ z +"' id='file"+ z +"' accept='JPG/JPEG/PNG/PDF/*   label='FormName'  class='form-control' /> <label for='file"+ z +"'>"+ label + "</label></div>"      		                   
		            			   		+ "<input type='hidden' name='base64' id='base64_"+ i +"' />";							 
							if(!value.equalsIgnoreCase("") && !value.equalsIgnoreCase("null")) {															      
								if(file_type.equalsIgnoreCase("pdf")) {
							str6 =  str6 + "<a href='ViewFile?form_id="+bean.getForm_id()+"&record_id="+bean.getRecord_id()+"&column_id="+bean.getColumn_id()+"'  target='_blank'>View</a></div>"; 			
								}else {
									str6 = str6  + "<img src='data:image/jpg;base64,"+encodedString+"' id='profile-img-tag"+z+"' style='width:75px; height:75px;' data-toggle='modal' data-target='#myModal' onclick='getImageDisplay(this.src)'/></div>";
								}
							}							 
		        		z++;
               } else if(dtype.equalsIgnoreCase("text") && label.equalsIgnoreCase(bean.getColumn_name())){
		            	   JsonObject = (JSONObject) array.get(i);							
		        str6 =  str6 + "<div class='col-4'><div class='floating-label'><input type='text' name='text' id='text_"+ i +"'value='"+bean.getColumn_value()+"' onkeypress='return ValidateAlpha(event)' onblur='return checkString(this, '"+ label + "');' autocomplete='off' class='form-control' placeholder='' /> <label for='text_"+ i +"'>"+ label +"</label> </div></div>";
		       } else if(dtype.equalsIgnoreCase("number") && label.equalsIgnoreCase(bean.getColumn_name())){                                            
		             	  JsonObject = (JSONObject) array.get(i);
		             	 str6 =  str6 + "<div class='col-4'><div class='floating-label'><input type='text' name='number' id='number_"+ i +"' minlength='1' maxlength='10' onkeypress='return isNumber(event)'  AUTOCOMPLETE='OFF' label='FormName' value='"+bean.getColumn_value()+"' class='form-control' placeholder=''/> <label for='number_"+ i +"'>"+ label + "</label></div></div>";
		       } else if(dtype.equalsIgnoreCase("mobile") && label.equalsIgnoreCase(bean.getColumn_name())){                                            
		      	  JsonObject = (JSONObject) array.get(i);
		      	 str6 =  str6 + "<div class='col-4'><div class='floating-label'><input type='text' name='mobile' id='mobile_"+ i +"'  value='"+bean.getColumn_value()+"' onkeypress='return isNumber(event)' onblur='return checkmobile(this);' minlength='10' maxlength='10'  AUTOCOMPLETE='OFF'  label='FormName' class='form-control' placeholder=''/> <label for='mobile_"+ i + "'>" + label + "</label></div></div>";
		       } else if(dtype.equalsIgnoreCase("aadhaarno") && label.equalsIgnoreCase(bean.getColumn_name())){                                            
		      	  JsonObject = (JSONObject) array.get(i);
		      	  str6 =  str6 + "<div class='col-4'><div class='floating-label'><input type='text' name='aadhaarno' id='aadhaarno_"+ i +"'  value='"+bean.getColumn_value()+"' minlength='12' maxlength='12' onkeypress='return isNumber(event)' onchange='return aadharValid(this)'  AUTOCOMPLETE='OFF' label='FormName' class='form-control' placeholder=''/> <label for='aadhaarno_"+ i +"'>" + label + "</label></div></div>";
		       } else if(dtype.equalsIgnoreCase("panno") && label.equalsIgnoreCase(bean.getColumn_name())){                                            
			  JsonObject = (JSONObject) array.get(i);
			  	 str6 =  str6 + "<div class='col-4'><div class='floating-label'><input type='text' name='panno' id='panno_"+ i +"'  value='"+bean.getColumn_value()+"' minlength='10' maxlength='10' style='text-transform:uppercase;' AUTOCOMPLETE='OFF'  label='FormName' class='form-control' placeholder=''/> <label for='panno_"+ i +"'>" + label + "</label></div></div>";
		       } else if(dtype.equalsIgnoreCase("specialcombo") && bean.getColumn_name().equalsIgnoreCase("specialcombo")){               
		            	   JsonObject = (JSONObject) array.get(i);
		            	   str6 = str6 + "<div class='col-4'><div class='form-group floating-label'><select class='form-select' name='stateCode' id='stateCode'>";
						   if(!bean.getStateCode().equalsIgnoreCase("")) {
		                        str6 =  str6  + "<option value='"+bean.getStateCode()+"'>"+bean.getStateName()+"</option>";
						    }else {
								str6 =  str6  + "<option value=''>Select State</option>";   
							}
		          			for(int u =0;u<bean.getStateList().size();u++) {           				
		          			JsonBean cb = (JsonBean)bean.getStateList().get(u);
		          str6 =  str6    + "<option value='"+cb.getStateCode()+"'>"+cb.getStateName()+"</option>";
		          			}
		          str6 =  str6 + "</select><label for='stateCode'>State</label></div></div>";    
		           str6 =  str6 + "<div class='col-4'><div class='form-group floating-label'><select class='form-select' name='distCode' id='distCode'>";
						  if(!bean.getDistCode().equalsIgnoreCase("")) {
		                        str6 =  str6 + "<option value='"+bean.getDistCode()+"'>"+bean.getDistName()+"</option>" ;                                                                   
								}else {
								str6 =  str6  + "<option value=''>Select District</option>" ;   
								}
		           for(int u =0;u<bean.getDistList().size();u++) {           				
		     			JsonBean cb = bean.getDistList().get(u);
		     str6 =  str6    + "<option value='"+cb.getDistCode()+"'>"+cb.getDistName()+"</option>";
		     			}
		     str6 =  str6 + "</select><label for='distCode'>District</label></div></div>";  
		           str6 =  str6 + "<div class='col-4'><div class='form-group floating-label'><select class='form-select' name='mandalCode' id='mandalCode'>";
				   if(!bean.getMandalCode().equalsIgnoreCase("")) {
		                   str6 =  str6  + "<option value='"+bean.getMandalCode()+"'>"+bean.getMandalName()+"</option>";
						   }else {
							str6 =  str6 + "<option value=''>Select Mandal</option>";
						  }
		                   for(int u =0;u<bean.getMandalList().size();u++) {           				
		            			JsonBean cb = bean.getMandalList().get(u);
		            str6 =  str6    + "<option value='"+cb.getMandalCode()+"'>"+cb.getMandalName()+"</option>";
		            			}
		            str6 =  str6 + "</select><label for='mandalCode'>Mandal</label></div></div>";  
		        	  str6 =  str6 + "<div class='col-4'><div class='form-group floating-label'><select class='form-select' name='villCode' id='villCode'>"; 
					  if(!bean.getVillCode().equalsIgnoreCase("")) {
		                   str6 =  str6 + "<option value='"+bean.getVillCode()+"'>"+bean.getVillName()+"</option>";
						   }else {
								str6 =  str6 + "<option value=''>Select Village</option>";
							}
		                   for(int u =0;u<bean.getVillList().size();u++) {           				
		            			JsonBean cb = bean.getVillList().get(u);
		            str6 =  str6    + "<option value='"+cb.getVillCode()+"'>"+cb.getVillName()+"</option>";
		            			}
		            str6 =  str6 + "</select><label for='villCode'>Village</label></div></div>";
		       } else if(dtype.equalsIgnoreCase("time") && label.equalsIgnoreCase(bean.getColumn_name())){                                            
					  JsonObject = (JSONObject) array.get(i);
					  	 str6 =  str6 + "<div class='col-4'><div class='floating-label'><input type='text' name='time' id='time_"+ i +"'  value='"+bean.getColumn_value()+"' minlength='5' maxlength='5' onkeypress='return isNumberwithColan(event)' onblur='return ValidateTime(this);' AUTOCOMPLETE='OFF'  label='FormName' class='form-control' placeholder='Enter Time format like hh:mm(24 Hours format)'/> <label for='time_"+ i +"'>" + label + "</label></div></div>";				       
					  	//-------------------------------SMS-Code------------------------------
				} else if (dtype.equalsIgnoreCase("otp")) {
					JsonObject = (JSONObject) array.get(i);
					str6 = str6
							+ "<div class='col-4'><div class='floating-label'><input type='text' name='otp' id='otp_"
							+ i
							+ "' minlength='6' maxlength='6' onkeypress='return isNumber(event)' onblur='validateOtp()' AUTOCOMPLETE='OFF' label='FormName' class='form-control' placeholder='Please enter 6 digit OTP'/> <label for='otp"
							+ i + "'>" + label + "</label></div>"								
							+ "</div>"								
							+ "<div class='col-4'><div class='floating-label'>"
							+ "<label for='Click here to Generate OTP' id='otphider1'>Click here to Generate OTP</label>&nbsp;&nbsp;"
							+ "<i onclick='generateOTP()' id='otphider'> <input type='button' name='Generate OTP' value='Generate OTP' class='btn btn-primary'/></i>"								
							+ "<i onclick='generateOTP()' id='reotphider' style='display:none;'> <input type='button' name='Re-Generate OTP' value='Re-Generate OTP' class='btn btn-primary'/></i>"								
							+ "<div id='timer'></div>"
							+ "</div></div>";
//---------------------------------------------------------------------						
				} else {}

		              if(i%3==2){
		        str6 =  str6 + "</div>";           
		              }
				}
				if (array.size() % 2 == 1) {
					str6 = str6 + "</div>";
				}
//-----------------------Captcha-Img------------------------------------- 
//				if (form.getCaptcha_key().trim().equalsIgnoreCase("Y")) {
//					str6 = str6 + "<div class='col-4'>\r\n" + "<div class='input-group mb-3'>\r\n"
//								+ " <div class='form-group floating-label'><input type='text' class='form-control' name='captcha'  id='captcha' AUTOCOMPLETE='OFF' placeholder='Enter Captcha' aria-label='Enter Captcha' onkeypress='return ValidateAlphaNumeric(event)' style='text-transform:uppercase;' onkeyup='return wordCapital(this)' ><label for='captcha'>Enter Captcha</label></div>\r\n"
//								+ " <img id='ssss' src='captcha'/><span class='input-group-text'><i class='fas fa-arrows-rotate' onclick='DrawCaptcha()'></i></span>\r\n"
//								+ "</div></div>";<img id='captchaImage' src=\"data:image/jpeg;base64, "+bean.getCaptchaImage()+" \" alt='CAPTCHA Image' />
//				}
				if (form.getCaptcha_key().trim().equalsIgnoreCase("Y")) {
					str6 = str6 + "<div class='row'><div class='mx-auto mt-3 col-3'><div class='captchaHolder'><img id='captchaImage' src=\"data:image/jpeg;base64, "+form.getCaptchaImage()+" \" alt='CAPTCHA Image' /><i class='fas fa-volume-high cptIcons' onclick='getAudioCaptcha()'></i><i class='fas fa-arrows-rotate cptIcons' onclick='DrawCaptcha()'></i></div></div></div>"
							+ "<div class='mx-auto mt-3 mb-2 col-3'><div class='form-group floating-label'><input type='text' class='form-control' name='captcha'  id='captcha' AUTOCOMPLETE='OFF' placeholder='Enter Captcha' aria-label='Enter Captcha' minlength='6' maxlength='6' onkeypress='return ValidateAlphaNumeric(event)' style='text-transform:uppercase;' onkeyup='return wordCapital(this)'><label for='captcha'>Enter Captcha</label></div>\r\n"
							+ "</div></div>";
				}
//--------------------------------------------------------------- 
				str6 = str6 + "<div th:if=\"${#fields.hasErrors('global')}\">\r\n"
						+ "    <p th:each=\"err : ${#fields.errors('global')}\" th:text=\"${err}\" class=\"error\" />\r\n"
						+ "</div>";
//-------------------------------SMS-Code------------------------------	
				if (form.getSms_key().trim().equalsIgnoreCase("Y")) {
				str6 = str6 + "<p><span class='text-decoration-underline m-3  text-danger'>NOTE:</span>The OTP is valid for 10 minutes</p>";
							
				}
//---------------------------------------------------------------				
				str6 = str6 + "<div class='mx-auto col-1 mt-3 mb-3'>"
							+ "<input type='submit' name='Submit' value='Submit' class='btn btn-primary'/>" + "</div>" + "</div>";
				str = str1 + str6 + str5;
			} 
		} catch (Exception e) {
			logger.error("Exception in Retrieving_Json_Object::getJsonTable_Fields4::" + e.getMessage());
	        logger.error("Exception in Retrieving_Json_Object::getJsonTable_Fields4::" + e.getCause());
	        logger.error("Exception in Retrieving_Json_Object::getJsonTable_Fields4::" + e.getClass() + "::" + e.getLocalizedMessage());
	    }

		return str;
	}          

    public static String getJsonTable_Data_Preview(JSONArray array, List list5, JsonBean form) {
		String str = "", str6 = "";
		int ab = 1, z = 0, x = 0, jj = 0;
		String str1 =  "<html><head>"
                + "</head><body>"                               
                + "<div class='container'>";                                                        

   String str5 =  "</div></body></html>"; 
		try {
			if (array.size() > 0) {
				for (int i = 0; i < array.size(); i++) {
					JsonBean bean = (JsonBean) list5.get(i);
					JSONObject JsonObject = (JSONObject) array.get(i);
					String label = (String) JsonObject.get("label");
					String dtype = (String) JsonObject.get("dtype");
					String value = "";
					try {
					value = bean.getColumn_value().toString();
					}catch(Exception e) {}
					if (i % 3 == 0) {
						str6 = str6 + "<div class='row my-3 g-2 d-flex'>";
					}
//             if(ab==1){
//             str6 = str6 + "<tr>";
//             }
					if(dtype.equalsIgnoreCase("radiobutton") && label.equalsIgnoreCase(bean.getColumn_name()))  {           
		            	 ArrayList<String> list1 = (ArrayList<String>) JsonObject.get("radiovalues");
		              String array2[] = new String[list1.size()];
		             for (int j = 0; j < list1.size(); j++){array2[j] = list1.get(j);}
		        str6 =  str6 + "<div class='col-4'>"+ label + "";
		            for (String n : array2) {
		            	if(value.equalsIgnoreCase(n)) {
		            		str6 =  str6 +"<div class='form-check'><input class='form-check-input' type='radio' name='radio_button_"+ i +"' checked='true' readonly='true' id='radio_button_"+ i +"' value='"+ n +"'><label class='form-check-label' for='radio_button_"+ i +"'>"+ n + "</label></div>";
		            	} else {
		            		str6 =  str6 +"<input type='radio' name='radio_button_"+ i +"' id='radio_button_"+ i +"' value='"+ n +"'/><b>"+ n + "</b>";	
		            	} 
		            }
		        str6 =  str6 + "</div>"; 
		             } else if(dtype.equalsIgnoreCase("dropdown") && label.equalsIgnoreCase(bean.getColumn_name())){                    
		            	 JsonObject = (JSONObject) array.get(i);
		                ArrayList<String> list = (ArrayList<String>) JsonObject.get("dropvalues");                
		                String array1[] = new String[list.size()];
		                for (int j = 0; j < list.size(); j++){array1[j] = list.get(j);}                     
		        str6 = str6 + "<div class='col-4'><div class='form-group floating-label'><select class='form-select' name='dropdown' id='dropdown_"+ i +"'><option value=''>Select</option>";
		                for (String k : array1) {
		                	if(value.equalsIgnoreCase(k)) {
		        str6 =  str6	+ "<option value='"+bean.getColumn_value()+"' selected>"+bean.getColumn_value()+"</option>";
		                	}else {
		        str6 =  str6 + "<option value='"+ k +"'>"+ k + "</option>";
		                	}
		                }
		                str6 =  str6 + "</select> <label for='dropdown_"+ i +"'>"+ label + "</label></div></div>";
		                } else if(dtype.equalsIgnoreCase("checkbox") && label.equalsIgnoreCase(bean.getColumn_name())){            
		                	JsonObject = (JSONObject) array.get(i);
		            ArrayList<String> list2 = (ArrayList<String>) JsonObject.get("checkvalues");
		            String array3[] = new String[list2.size()];
		            for (int j = 0; j < list2.size(); j++){array3[j] = list2.get(j);}
		            str6 = str6 +"<div class='col-4'>"+ label + "";
		                for (String m : array3) {
		                if(value.equalsIgnoreCase(m)) {
		                	str6 =  str6 + "<div class='form-check'>\r\n"
		                    		+ "  <input class='form-check-input' type='checkbox' name='check_box_"+ i +"' value='"+ m +"' id='check_box_"+ i +"' checked='true'><label class='form-check-label' for='check_box_"+ i +"'>"+ m + "</label>\r\n"
		                    		+ "</div>\r\n";       
		                }else {
		                	str6 =  str6 + "<div class='form-check'>\r\n"
		                    		+ "  <input class='form-check-input' type='checkbox' name='check_box_"+ i +"' value='"+ m +" id='check_box_"+ i +"' ><label class='form-check-label' for='check_box_"+ i +"'>"+ m + "</label>\r\n"
		                    		+ "</div>\r\n";
		                }
		                }
		        str6 =  str6 + "</div>";   
		            } else if(dtype.equalsIgnoreCase("textarea") && label.equalsIgnoreCase(bean.getColumn_name())){                           
		            	JsonObject = (JSONObject) array.get(i);
		            	str6= str6 +"<div class='col-4'><div class='form-group floating-label'>"
		            			+ "<textarea name='textarea' id='textarea_"+ i +"' onblur='return textarea(this, '"+ label + "');' readonly='true' maxlength='1000' size='50px' class='form-control' placeholder=''>" +bean.getColumn_value()+ "</textarea>"
		            			+ "<label for='textarea_"+ i +"'>"+ label + "</label></div></div>";
		            } else if(dtype.equalsIgnoreCase("password") && label.equalsIgnoreCase(bean.getColumn_name())){                                            
		            	  JsonObject = (JSONObject) array.get(i);
		            	  str6 =  str6 + "<div class='col-4'><div class='floating-label'><input type='password' name='password' id='password_"+ i +"' readonly='true'  AUTOCOMPLETE='OFF'  label='FormName' class='form-control' placeholder='' value='"+bean.getColumn_value()+"'/> <label for='password_"+ i +"'>"+ label + "</label></div></div>";
		            } else if(dtype.equalsIgnoreCase("email") && label.equalsIgnoreCase(bean.getColumn_name())){                                                          
		            	   JsonObject = (JSONObject) array.get(i);
		            	   str6 =  str6 + "<div class='col-4'><div class='floating-label'><input type='email' name='email' id='email_"+ i +"' readonly='true' maxlength='50' onblur='return ValidateEmail(this);'  AUTOCOMPLETE='OFF'  label='FormName' class='form-control' placeholder='' value='"+bean.getColumn_value()+"'/> <label for='email_"+ i +"'>"+ label + "</label></div></div>";
		            } else if(dtype.equalsIgnoreCase("date") && label.equalsIgnoreCase(bean.getColumn_name())){               
		                JsonObject = (JSONObject) array.get(i);
		                str6 =  str6 + "<div class='col-4'><div class='floating-label'><input type='date' name='date"+ x +"' id='date"+ x +"' readonly='true' minlength='10' maxlength='10'  AUTOCOMPLETE='OFF' label='FormName' class='form-control' placeholder='' value='"+bean.getColumn_value()+"'/> <label for='date"+ x +"'>"+ label + "</label></div></div>";
		        		x++;
		               } else if(dtype.equalsIgnoreCase("file") && label.equalsIgnoreCase(bean.getColumn_name())){                                            
		            	   JsonObject = (JSONObject) array.get(i);
						   String encodedString = "";
						   if(!value.equalsIgnoreCase("") && !value.equalsIgnoreCase("null")) {
						   Path path = Paths.get(bean.getColumn_value());
						   byte[] data = Files.readAllBytes(path);            	               	            	  
						   encodedString = Base64.getEncoder().encodeToString(data);            	               	               	   
						   MultipartFile img = new MockMultipartFile("123456789.jpg", path.getFileName().toString(), "image/jpg", data);             	   
						   }
						   String file_type = (String)JsonObject.get("file_type");
            	   
		            	   str6 =  str6 + "<div class='col-4 d-flex justify-content-between'><div class='floating-label'><input type='file' name='file"+ z +"' id='file"+ z +"' accept='JPG/JPEG/PNG/PDF/*   label='FormName'  class='form-control' /> <label for='file"+ z +"'>"+ label + "</label></div>"      		                   
		            			   		+ "<input type='hidden' name='base64' id='base64_"+ i +"' />";							 
							if(!value.equalsIgnoreCase("") && !value.equalsIgnoreCase("null")) {															      
								if(file_type.equalsIgnoreCase("pdf")) {
							str6 =  str6 + "<a href='ViewFile?form_id="+bean.getForm_id()+"&record_id="+bean.getRecord_id()+"&column_id="+bean.getColumn_id()+"'  target='_blank'>View</a></div>"; 			
								}else {
									str6 = str6  + "<img src='data:image/jpg;base64,"+encodedString+"' id='profile-img-tag"+z+"' style='width:75px; height:75px;' data-toggle='modal' data-target='#myModal' onclick='getImageDisplay(this.src)'/></div>";
								}
							}							 
		        		z++;
		               } else if(dtype.equalsIgnoreCase("text") && label.equalsIgnoreCase(bean.getColumn_name())){
		            	   JsonObject = (JSONObject) array.get(i);							
		        str6 =  str6 + "<div class='col-4'><div class='floating-label'><input type='text' name='text' id='text_"+ i +"' readonly='true'  value='"+bean.getColumn_value()+"' onkeypress='return ValidateAlpha(event)' onblur='return checkString(this, '"+ label + "');' autocomplete='off' class='form-control' placeholder='' /> <label for='text_"+ i +"'>"+ label +"</label> </div></div>";
		       } else if(dtype.equalsIgnoreCase("number") && label.equalsIgnoreCase(bean.getColumn_name())){                                            
		             	  JsonObject = (JSONObject) array.get(i);
		             	 str6 =  str6 + "<div class='col-4'><div class='floating-label'><input type='text' name='number' id='number_"+ i +"' readonly='true' minlength='1' maxlength='10' onkeypress='return isNumber(event)'  AUTOCOMPLETE='OFF' label='FormName' value='"+bean.getColumn_value()+"' class='form-control' placeholder=''/> <label for='number_"+ i +"'>"+ label + "</label></div></div>";
		       } else if(dtype.equalsIgnoreCase("mobile") && label.equalsIgnoreCase(bean.getColumn_name())){                                            
		      	  JsonObject = (JSONObject) array.get(i);
		      	 str6 =  str6 + "<div class='col-4'><div class='floating-label'><input type='text' name='mobile' id='mobile_"+ i +"' readonly='true'  value='"+bean.getColumn_value()+"' onkeypress='return isNumber(event)' onblur='return checkmobile(this);' minlength='10' maxlength='10'  AUTOCOMPLETE='OFF'  label='FormName' class='form-control' placeholder=''/> <label for='mobile_"+ i + "'>" + label + "</label></div></div>";
		       } else if(dtype.equalsIgnoreCase("aadhaarno") && label.equalsIgnoreCase(bean.getColumn_name())){                                            
		      	  JsonObject = (JSONObject) array.get(i);
		  str6 =  str6 + "<div class='col-4'><div class='floating-label'><input type='text' name='aadhaarno' id='aadhaarno_"+ i +"' readonly='true' value='"+bean.getColumn_value()+"' minlength='12' maxlength='12' onkeypress='return isNumber(event)' onchange='return aadharValid(this)'  AUTOCOMPLETE='OFF' label='FormName' class='form-control' placeholder=''/> <label for='aadhaarno_"+ i +"'>" + label + "</label></div></div>";
		       } else if(dtype.equalsIgnoreCase("panno") && label.equalsIgnoreCase(bean.getColumn_name())){                                            
			  JsonObject = (JSONObject) array.get(i);
			 str6 =  str6 + "<div class='col-4'><div class='floating-label'><input type='text' name='panno' id='panno_"+ i +"' readonly='true' value='"+bean.getColumn_value()+"' minlength='10' maxlength='10' style='text-transform:uppercase;' AUTOCOMPLETE='OFF'  label='FormName' class='form-control' placeholder=''/> <label for='panno_"+ i +"'>" + label + "</label></div></div>";
		       } else if(dtype.equalsIgnoreCase("specialcombo") && bean.getColumn_name().equalsIgnoreCase("specialcombo")){               
		            	   JsonObject = (JSONObject) array.get(i);
		            	   str6 = str6 + "<div class='col-4'><div class='form-group floating-label'><select class='form-select' name='stateCode' id='stateCode'>";
						   if(!bean.getStateCode().equalsIgnoreCase("")) {
		                        str6 =  str6  + "<option value='"+bean.getStateCode()+"'>"+bean.getStateName()+"</option>";
						    }else {
								str6 =  str6  + "<option value=''>Select State</option>";   
							}
		          			for(int u =0;u<bean.getStateList().size();u++) {           				
		          			JsonBean cb = (JsonBean)bean.getStateList().get(u);
		          str6 =  str6    + "<option value='"+cb.getStateCode()+"'>"+cb.getStateName()+"</option>";
		          			}
		          str6 =  str6 + "</select><label for='stateCode'>State</label></div></div>";    
		           str6 =  str6 + "<div class='col-4'><div class='form-group floating-label'><select class='form-select' name='distCode' id='distCode'>";
						  if(!bean.getDistCode().equalsIgnoreCase("")) {
		                        str6 =  str6 + "<option value='"+bean.getDistCode()+"'>"+bean.getDistName()+"</option>" ;                                                                   
								}else {
								str6 =  str6  + "<option value=''>Select District</option>" ;   
								}
		           for(int u =0;u<bean.getDistList().size();u++) {           				
		     			JsonBean cb = bean.getDistList().get(u);
		     str6 =  str6    + "<option value='"+cb.getDistCode()+"'>"+cb.getDistName()+"</option>";
		     			}
		     str6 =  str6 + "</select><label for='distCode'>District</label></div></div>";  
		           str6 =  str6 + "<div class='col-4'><div class='form-group floating-label'><select class='form-select' name='mandalCode' id='mandalCode'>";
				   if(!bean.getMandalCode().equalsIgnoreCase("")) {
		                   str6 =  str6  + "<option value='"+bean.getMandalCode()+"'>"+bean.getMandalName()+"</option>";
						   }else {
							str6 =  str6 + "<option value=''>Select Mandal</option>";
						  }
		                   for(int u =0;u<bean.getMandalList().size();u++) {           				
		            			JsonBean cb = bean.getMandalList().get(u);
		            str6 =  str6    + "<option value='"+cb.getMandalCode()+"'>"+cb.getMandalName()+"</option>";
		            			}
		            str6 =  str6 + "</select><label for='mandalCode'>Mandal</label></div></div>";  
		        	  str6 =  str6 + "<div class='col-4'><div class='form-group floating-label'><select class='form-select' name='villCode' id='villCode'>"; 
					  if(!bean.getVillCode().equalsIgnoreCase("")) {
		                   str6 =  str6 + "<option value='"+bean.getVillCode()+"'>"+bean.getVillName()+"</option>";
						   }else {
								str6 =  str6 + "<option value=''>Select Village</option>";
							}
		                   for(int u =0;u<bean.getVillList().size();u++) {           				
		            			JsonBean cb = bean.getVillList().get(u);
		            str6 =  str6    + "<option value='"+cb.getVillCode()+"'>"+cb.getVillName()+"</option>";
		            			}
		            str6 =  str6 + "</select><label for='villCode'>Village</label></div></div>";  
		       } else if(dtype.equalsIgnoreCase("time") && label.equalsIgnoreCase(bean.getColumn_name())){                                            
	             	  JsonObject = (JSONObject) array.get(i);
	             	 str6 =  str6 + "<div class='col-4'><div class='floating-label'><input type='text' name='time' id='time_"+ i +"' readonly='true' minlength='5' maxlength='5' onkeypress='return isNumberwithColan(event)' onblur='return ValidateTime(this);'  AUTOCOMPLETE='OFF' label='FormName' value='"+bean.getColumn_value()+"' class='form-control' placeholder=''/> <label for='time_"+ i +"'>"+ label + "</label></div></div>";	       
		       } else { 
		               }

		              if(i%3==2){
		        str6 =  str6 + "</div>";           
		              }
				}
				if (array.size() % 2 == 1) {
					str6 = str6 + "</div>";
				}
				str = str1 + str6 + str5;
			} 
		} catch (Exception e) {
			logger.error("Exception in Retrieving_Json_Object::getJsonTable_Data_Preview::" + e.getMessage());
	        logger.error("Exception in Retrieving_Json_Object::getJsonTable_Data_Preview::" + e.getCause());
	        logger.error("Exception in Retrieving_Json_Object::getJsonTable_Data_Preview::" + e.getClass() + "::" + e.getLocalizedMessage());
	    }

		return str;
	}          

}

