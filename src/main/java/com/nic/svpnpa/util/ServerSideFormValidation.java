package com.nic.svpnpa.util;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.Enumeration;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nic.svpnpa.model.LoginBean;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ServerSideFormValidation {

//    private static final Logger logger = LoggerFactory.getLogger(ServerSideFormValidation.class);
//    public static final String DATE_VALID = "^[19|20|21]{2}[0-9]{2}[-/.][0-9]{1,2}[-/.][0-9]{1,2}$";
//    private static final String SPECIALCHAR_PATTERN = "^[a-zA-Z0-9\\s]*$";
//    private static final String SOMESPECIALCHAR_PATTERN = "^[a-zA-Z0-9./\\s\\w]*$";
//    private static final String DECIMAL = "[0-9]+([.][0-9]{1,2})?";
//    private static final String DECIMALMOREDIGITS = "[0-9]+([.][0-9]{1,3})?";
//    static final String ONLY09_PATTEREN = "^[0-9]*$";
//    // It matches 1 or more digits, followed by optional: comma or full stop, followed by 1 or 2 digits
//    final String regExp = "[0-9]+([,.][0-9]{1,2})?";
//    private static final String DataYear_Petern = "^[0-9-/\\s\\w]*$";
//    public static final String negative_value_pattern = "^-?[0-9]\\d*(\\.\\d+)?$";
//    private static final String negative_DECIMAL = "^-?[0-9]+([.][0-9]{1,2})?";
//    private static final String coma_separated_integer = "^[0-9](,[0-9])*$";
//    private static final String coma_separated_numbers = "^[0-9](,[0-9])*$";
//
//    Properties prop = null;
//
//    public ServerSideFormValidation() {
//        prop = new Properties();
//    }
//
//    public void clearCache(HttpServletResponse response) {
//        response.setHeader("Cache-Control", "no-cache, no-store");
//        response.setHeader("Pragma", "no-cache");
//        response.setDateHeader("Expires", -1);
//        response.setHeader("Vary", "*");
//    }
//
//    public String isInteger(String str) {
//        try {
//            boolean checkFile = this.loadPropertirs();
//            if (!checkFile) {
//                return "";
//            }
//            Integer min = Integer.parseInt(prop.getProperty("int_min_len").trim());
//            Integer max = Integer.parseInt(prop.getProperty("int_max_len").trim());
//
//            Pattern pattern1 = Pattern.compile(SOMESPECIALCHAR_PATTERN);
//            Matcher matcher1 = pattern1.matcher(str);
//            if (!matcher1.matches()) {
//                return "Special Characters are not allowed";
//            }
//
//            Pattern pattern2 = Pattern.compile(ONLY09_PATTEREN);
//            Matcher matcher2 = pattern2.matcher(str);
//            if (!matcher2.matches()) {
//                return "Only Numbers are allowed";
//            }
//
//            if (str.length() >= min && str.length() <= max) {
//            } else {
//                return "Field length must be greater then 1 and less then 10";
//            }
//            Integer.parseInt(str);
//            return "Success";
//        } catch (NumberFormatException ex) {
//            return "Only Numbers are allowed";
//        }
//    }
//
//    public String isInteger2(String str) {
//        try {
//            boolean checkFile = this.loadPropertirs();
//            if (!checkFile) {
//                return "";
//            }
//            Integer min = Integer.parseInt(prop.getProperty("int_min_len").trim());
//            Integer max = Integer.parseInt(prop.getProperty("int_max_len2").trim());
//
//            Pattern pattern1 = Pattern.compile(SOMESPECIALCHAR_PATTERN);
//            Matcher matcher1 = pattern1.matcher(str);
//            if (!matcher1.matches()) {
//                return "Special Characters are not allowed";
//            }
//
//            Pattern pattern2 = Pattern.compile(ONLY09_PATTEREN);
//            Matcher matcher2 = pattern2.matcher(str);
//            if (!matcher2.matches()) {
//                return "Only Numbers are allowed";
//            }
//
//            if (str.length() >= min && str.length() <= max) {
//            } else {
//                return "Field length must be greater than 1 and less than 4";
//            }
//            Integer.parseInt(str);
//            return "Success";
//        } catch (NumberFormatException ex) {
//            return "Only Numbers are allowed";
//        }
//    }
//
//    public String isDoubleWithNegative(String str) {
//        try {
//            boolean checkFile = this.loadPropertirs();
//            if (!checkFile) {
//                return "";
//            }
//            Integer min = Integer.parseInt(prop.getProperty("int_min_len").trim());
//            Integer max = Integer.parseInt(prop.getProperty("int_max_len").trim());
//
//            Pattern pattern1 = Pattern.compile(negative_value_pattern);
//            Matcher matcher1 = pattern1.matcher(str);
//            if (!matcher1.matches()) {
//                return "Special Characters are not allowed";
//            }
//
//            Pattern pattern2 = Pattern.compile(negative_DECIMAL);
//            Matcher matcher2 = pattern2.matcher(str);
//            if (!matcher2.matches()) {
//                return "Only Numbers are allowed";
//            }
//
//            if (str.length() >= min && str.length() <= max) {
//            } else {
//                return "Field length must be greater then 1 and less then 10";
//            }
//            Double.parseDouble(str);
//            return "Success";
//        } catch (NumberFormatException ex) {
//            return "Numbers are allowed";
//        }
//    }
//
//    public String isDouble(String str) {
//        try {
//            boolean checkFile = this.loadPropertirs();
//            if (!checkFile) {
//                return "";
//            }
//            Integer min = Integer.parseInt(prop.getProperty("int_min_len").trim());
//            Integer max = Integer.parseInt(prop.getProperty("int_max_len").trim());
//
//            Pattern pattern1 = Pattern.compile(SOMESPECIALCHAR_PATTERN);
//            Matcher matcher1 = pattern1.matcher(str);
//            if (!matcher1.matches()) {
//                return "Special Characters are not allowed";
//            }
//
//            Pattern pattern2 = Pattern.compile(DECIMAL);
//            Matcher matcher2 = pattern2.matcher(str);
//            if (!matcher2.matches()) {
//                return "Only Numbers are allowed";
//            }
//
//            if (str.length() >= min && str.length() <= max) {
//            } else {
//                return "Field length must be greater then 1 and less then 10";
//            }
//            Double.parseDouble(str);
//            return "Success";
//        } catch (NumberFormatException ex) {
//            return "Numbers are allowed";
//        }
//    }
//
//    public String isDouble_14_Digits(String str) {
//        try {
//            boolean checkFile = this.loadPropertirs();
//            if (!checkFile) {
//                return "";
//            }
//            Integer min = Integer.parseInt(prop.getProperty("int_min_len").trim());
//            Integer max = Integer.parseInt(prop.getProperty("double_14").trim());
//
//            Pattern pattern1 = Pattern.compile(SOMESPECIALCHAR_PATTERN);
//            Matcher matcher1 = pattern1.matcher(str);
//            if (!matcher1.matches()) {
//                return "Special Characters are not allowed";
//            }
//
//            Pattern pattern2 = Pattern.compile(DECIMAL);
//            Matcher matcher2 = pattern2.matcher(str);
//            if (!matcher2.matches()) {
//                return "Only Numbers are allowed";
//            }
//
//            if (str.length() >= min && str.length() <= max) {
//            } else {
//                return "Field length must be greater then 1 and less then 14";
//            }
//            Double.parseDouble(str);
//            return "Success";
//        } catch (NumberFormatException ex) {
//
//            return "Numbers are allowed";
//        }
//    }
//
//    public String isDouble_18_Digits(String str) {
//        try {
//            boolean checkFile = this.loadPropertirs();
//            if (!checkFile) {
//                return "";
//            }
//            Integer min = Integer.parseInt(prop.getProperty("int_min_len").trim());
//            Integer max = Integer.parseInt(prop.getProperty("double_18").trim());
//
//            Pattern pattern1 = Pattern.compile(SOMESPECIALCHAR_PATTERN);
//            Matcher matcher1 = pattern1.matcher(str);
//            if (!matcher1.matches()) {
//                return "Special Characters are not allowed";
//            }
//
//            Pattern pattern2 = Pattern.compile(DECIMAL);
//            Matcher matcher2 = pattern2.matcher(str);
//            if (!matcher2.matches()) {
//                return "Only Numbers are allowed";
//            }
//
//            if (str.length() >= min && str.length() <= max) {
//            } else {
//                return "Field length must be greater then 1 and less then 10";
//            }
//            Double.parseDouble(str);
//            return "Success";
//        } catch (NumberFormatException ex) {
//            return "Numbers are allowed";
//        }
//    }
//
//    public String isDouble_fecility(String str) {
//        try {
//            boolean checkFile = this.loadPropertirs();
//            if (!checkFile) {
//                return "";
//            }
//            Integer min = Integer.parseInt(prop.getProperty("int_min_len").trim());
//            Integer max = Integer.parseInt(prop.getProperty("feclity_max_len").trim());
//
//            Pattern pattern1 = Pattern.compile(SOMESPECIALCHAR_PATTERN);
//            Matcher matcher1 = pattern1.matcher(str);
//            if (!matcher1.matches()) {
//                return "Special Characters are not allowed";
//            }
//
//            Pattern pattern2 = Pattern.compile(DECIMAL);
//            Matcher matcher2 = pattern2.matcher(str);
//            if (!matcher2.matches()) {
//                return "Only Numbers are allowed";
//            }
//
//            if (str.length() >= min && str.length() <= max) {
//            } else {
//                return "Field length must be greater then 1 and less then 10";
//            }
//            Double.parseDouble(str);
//            return "Success";
//        } catch (NumberFormatException ex) {
//            return "Numbers are allowed";
//        }
//    }
//
//    public String isDecimalThreeDigits(String str) {
//        //(10,3)
//        try {
//            boolean checkFile = this.loadPropertirs();
//            if (!checkFile) {
//                return "";
//            }
//            Integer min = Integer.parseInt(prop.getProperty("int_min_len").trim());
//            Integer max = Integer.parseInt(prop.getProperty("double_three_digits").trim());
//
//            Pattern pattern1 = Pattern.compile(SOMESPECIALCHAR_PATTERN);
//            Matcher matcher1 = pattern1.matcher(str);
//            if (!matcher1.matches()) {
//                return "Special Characters are not allowed";
//            }
//
//            Pattern pattern2 = Pattern.compile(DECIMALMOREDIGITS);
//            Matcher matcher2 = pattern2.matcher(str);
//            if (!matcher2.matches()) {
//                return "Only Numbers are allowed";
//            }
//
//            if (str.length() >= min && str.length() <= max) {
//            } else {
//                return "Field length must be greater then 1 and less then 10";
//            }
//            Double.parseDouble(str);
//            return "Success";
//        } catch (NumberFormatException ex) {
//            return "Numbers are allowed";
//        }
//    }
//
//    public String isDecimalMoreDigits1(String str) {
//        //(10,3)
//        try {
//            boolean checkFile = this.loadPropertirs();
//            if (!checkFile) {
//                return "";
//            }
//            Integer min = Integer.parseInt(prop.getProperty("int_min_len").trim());
//            Integer max = Integer.parseInt(prop.getProperty("int_max_len").trim());
//
//            Pattern pattern1 = Pattern.compile(SOMESPECIALCHAR_PATTERN);
//            Matcher matcher1 = pattern1.matcher(str);
//            if (!matcher1.matches()) {
//                return "Special Characters are not allowed";
//            }
//
//            Pattern pattern2 = Pattern.compile(DECIMALMOREDIGITS);
//            Matcher matcher2 = pattern2.matcher(str);
//            if (!matcher2.matches()) {
//                return "Only Numbers are allowed";
//            }
//
//            if (str.length() >= min && str.length() <= max) {
//            } else {
//                return "Field length must be greater then 1 and less then 10";
//            }
//            Double.parseDouble(str);
//            return "Success";
//        } catch (NumberFormatException ex) {
//            return "Numbers are allowed";
//        }
//    }
//
//    public String isDecimalMoreDigits2(String str) {
//        try {
//            //(13,3)
//            boolean checkFile = this.loadPropertirs();
//            if (!checkFile) {
//                return "";
//            }
//            Integer min = Integer.parseInt(prop.getProperty("int_min_len").trim());
//            Integer max = Integer.parseInt(prop.getProperty("int_max_len3").trim());
//
//            Pattern pattern1 = Pattern.compile(SOMESPECIALCHAR_PATTERN);
//            Matcher matcher1 = pattern1.matcher(str);
//            if (!matcher1.matches()) {
//                return "Special Characters are not allowed";
//            }
//
//            Pattern pattern2 = Pattern.compile(DECIMALMOREDIGITS);
//            Matcher matcher2 = pattern2.matcher(str);
//            if (!matcher2.matches()) {
//                return "Only Numbers are allowed";
//            }
//
//            if (str.length() >= min && str.length() <= max) {
//            } else {
//                return "Field length must be greater then 1 and less then 13";
//            }
//            Double.parseDouble(str);
//            return "Success";
//        } catch (NumberFormatException ex) {
//            return "Numbers are allowed";
//        }
//    }
//
//    public String isInteger_with_coma(String str) {
//        try {
//            boolean checkFile = this.loadPropertirs();
//            if (!checkFile) {
//                return "";
//            }
//
//            Integer min = Integer.parseInt(prop.getProperty("int_min_len").trim());
//            Integer max = Integer.parseInt(prop.getProperty("int_max_len").trim());
//
//            Pattern pattern1 = Pattern.compile(coma_separated_integer);
//            Matcher matcher1 = pattern1.matcher(str);
//            if (!matcher1.matches()) {
//                return "Special Characters are not allowed";
//            }
//
//            Pattern pattern2 = Pattern.compile(coma_separated_numbers);
//            Matcher matcher2 = pattern2.matcher(str);
//            if (!matcher2.matches()) {
//                return "Only Numbers are allowed";
//            }
//
//            if (str.length() >= min && str.length() <= max) {
//            } else {
//                return "Field length must be greater than 1 and less than 4";
//            }
//            return "Success";
//        } catch (NumberFormatException ex) {
//
//            return "Only Numbers are allowed";
//
//        }
//    }
//
//    public String ValidName_with_specialchar(String name) {
//
//        Pattern p = Pattern.compile("^(?!\\d+$)(?:[a-zA-Z0-9 ,.-][a-zA-Z0-9 ,.-]*)?$");
//        Matcher m = p.matcher(name);
//
//        if (!m.matches()) {
//            return "false";
//        }
//        return "true";
//    }
//
//    public boolean specialCharValidator(String input) {
//        Pattern pattern = Pattern.compile(SPECIALCHAR_PATTERN);
//        Matcher matcher = pattern.matcher(input);
//        if (!matcher.matches()) {
//            return false;
//        }
//        return true;
//    }
//
//    public boolean referrerValidation(HttpServletRequest req) {
//        try {
//            HttpServletRequest request = req;
//            String referrer = request.getHeader("referer");
//            StringBuffer url = request.getRequestURL();
//            if (referrer == null || referrer.equals("")) {
//                return false;
//            }
//            String reqBaseURL = url.substring(0, url.length() - request.getRequestURI().length()) + request.getContextPath() + "/";
//            String reqReferrer = referrer.substring(0, reqBaseURL.length());
//            if (reqBaseURL.equals(reqReferrer)) {
//            } else {
//                return false;
//            }
//            return true;
//        } catch (Exception ex) {
//            logger.error("Exception--" + ex);
//            return false;
//        }
//    }
//
//    public boolean getReferralSession(HttpServletRequest request, HttpServletResponse response) {
//        boolean condition = true;
//        if (condition) {
//            boolean checkFile = this.loadPropertirs();
//            if (!checkFile) {
//                return false;
//            }
//            String hostid = prop.getProperty("hostid").trim();
//
//            Enumeration headerNames = request.getHeaderNames();
//
//            while (headerNames.hasMoreElements()) {
//                String paramName = (String) headerNames.nextElement();
//                System.out.print(" Name..." + paramName);
//                String paramValue = request.getHeader(paramName);
//
//                if (paramName.equals("host")) {
//                    if (paramValue.equals(hostid)) {
//
//                    } else {
//
//                        condition = false;
//                        return condition;
//                    }
//                }
//            }
//            response.addHeader("X-Frame-Options", "DENY");
//            response.addHeader("X-Frame-Options", "SAMEORIGIN");
//            response.setHeader("X-XSS-Protection", "1; mode=block");
//            response.setHeader("Content-Security-Policy", "default-src  http:; script-src 'self' 'unsafe-inline' 'unsafe-eval'  http:; style-src http:  'unsafe-inline'; img-src 'self' data: http:; connect-src http:  ws:;");
//            // response.setContentType("text/plain");
//            response.addHeader("X-Content-Type-Options", "nosniff");
//            response.addHeader("Access-Control-Allow-Methods", "GET, POST");
//            response.addHeader("Access-Control-Allow-Origin", hostid);
//
//        }
//        
//        return condition;
//    }
//
//    public boolean checkIPAddress(String ip) {
//        Pattern p = Pattern.compile("[0-9.]*");
//        Matcher m = p.matcher(ip);
//        boolean b = m.matches();
//        if (b) {
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//    public String ValidName(String name) {
//        Pattern p = Pattern.compile("[a-zA-Z. ]*");
//        Matcher m = p.matcher(name);
//
//        if (!m.matches()) {
//            return "false";
//        }
//        return "true";
//    }
//
//    public boolean checkValidName(String name) {
//        Pattern p = Pattern.compile("[a-zA-Z0-9. ]*");
//        Matcher m = p.matcher(name);
//        boolean b = m.matches();
//        if (b) {
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//    public boolean loadPropertirs() {
//        InputStream input = null;
//        try {
//            String filename = "npa.properties";
//            input = this.getClass().getClassLoader().getResourceAsStream(filename);
//            if (input == null) {
//
//                return false;
//            }
//            prop.load(input);
//        } catch (IOException ie) {
//
//            return false;
//        } finally {
//            if (input != null) {
//                try {
//                    input.close();
//                } catch (IOException ex) {
//
//                }
//            }
//        }
//        return true;
//    }
//
//    public boolean checkRegister_under(String ip) {
//
//        if (Integer.parseInt(ip) < 1 || Integer.parseInt(ip) > 3) {
//
//            return false;
//        } else {
//            return true;
//        }
//
//    }
//
//    public boolean check_Yes_No(String ss) {
//
//        if (ss.trim().equalsIgnoreCase("Yes") || ss.trim().equalsIgnoreCase("No") || ss.trim().equalsIgnoreCase("Y") || ss.trim().equalsIgnoreCase("N")) {
//            return true;
//        } else {
//
//            return false;
//        }
//
//    }
//
//    public boolean checkSanc_ForPublic(String ss) {
//        if (Integer.parseInt(ss) < 1 || Integer.parseInt(ss) > 5) {
//
//            return false;
//        } else {
//            return true;
//        }
//
//    }
//
//    public boolean checkLevelCol(String ss) {
//        if (Integer.parseInt(ss) < 1 || Integer.parseInt(ss) > 5) {
//
//            return false;
//        } else {
//            return true;
//        }
//    }
//
//    public boolean checkManage(String ss) {
//
//        if (Integer.parseInt(ss) < 1 || Integer.parseInt(ss) > 3) {
//
//            return false;
//        } else {
//            return true;
//        }
//    }
//
//    public boolean checkBuilding(String ss) {
//
//        if (Integer.parseInt(ss) < 1 || Integer.parseInt(ss) > 3) {
//
//            return false;
//        } else {
//            return true;
//        }
//    }
//
//    public boolean checkCourse(String ss) {
//        if (Integer.parseInt(ss) < 1 || Integer.parseInt(ss) > 9) {
//
//            return false;
//        } else {
//            return true;
//        }
//    }
//
//    public boolean checkSanct_ForEdu(String ss) {
//        if (Integer.parseInt(ss) < 1 || Integer.parseInt(ss) > 7) {
//
//            return false;
//        } else {
//            return true;
//        }
//    }
//
//    public boolean checkFunctin(String ss) {
//        if (Integer.parseInt(ss) < 1 || Integer.parseInt(ss) > 2) {
//
//            return false;
//        } else {
//            return true;
//        }
//    }
//
//    public String findAllreplacements(String str) {
//        String newSTr = "";
//        newSTr = str.replaceAll("&", "");
//        newSTr = newSTr.replaceAll("<", "");
//        newSTr = newSTr.replaceAll(">", "");
//        newSTr = newSTr.replaceAll("select", "");
//        newSTr = newSTr.replaceAll("insert", "");
//        newSTr = newSTr.replaceAll("script", "");
//        newSTr = newSTr.replaceAll("alert", "");
//        newSTr = newSTr.replaceAll("href", "");
//        newSTr = newSTr.replaceAll("iframe", "");
//        newSTr = newSTr.replaceAll("http://", "");
//        newSTr = newSTr.replaceAll("html", "");
//        newSTr = newSTr.replaceAll("htm", "");
//        newSTr = newSTr.replaceAll("hidden", "");
//        newSTr = newSTr.replaceAll("\"", "");
//        newSTr = newSTr.replaceAll("\\(", "");
//        newSTr = newSTr.replaceAll("\\)", "");
//        newSTr = newSTr.replaceAll("\\+", "");
//        return newSTr;
//    }
//
//    public String RegYear(String str) {
//        try {
//            boolean checkFile = this.loadPropertirs();
//            if (!checkFile) {
//                return "";
//            }
//            Integer min = Integer.parseInt(prop.getProperty("int_max_len4").trim());
//            Integer max = Integer.parseInt(prop.getProperty("int_max_len4").trim());
//            String patternString = ".*javascript:.*|.*//.*|.*<.*|.*>.*";
//            Pattern pattern = Pattern.compile(patternString);
//            Matcher matcher = pattern.matcher(str);
//            if (matcher.matches()) {
//                return "Special Characters are not allowed.";
//            }
//
//            Pattern pattern1 = Pattern.compile(DataYear_Petern);
//            Matcher matcher1 = pattern1.matcher(str);
//            if (!matcher1.matches()) {
//                return "Special Characters are not allowed";
//            }
//
//            if (str.length() >= min && str.length() <= max) {
//            } else {
//                return "Field length must be 9";
//            }
//            return "Success";
//        } catch (NumberFormatException ex) {
//            return "Given Data Format Wrong";
//        }
//    }
//
//    public String bookYear(String str) {
//        try {
//            boolean checkFile = this.loadPropertirs();
//            if (!checkFile) {
//                return "";
//            }
//            Integer min = Integer.parseInt(prop.getProperty("int_max_len2").trim());
//            Integer max = Integer.parseInt(prop.getProperty("int_max_len2").trim());
//
//            String patternString = ".*javascript:.*|.*//.*|.*<.*|.*>.*";
//            Pattern pattern = Pattern.compile(patternString);
//            Matcher matcher = pattern.matcher(str);
//            if (matcher.matches()) {
//                return "Special Characters are not allowed.";
//            }
//
//            Pattern pattern1 = Pattern.compile(ONLY09_PATTEREN);
//            Matcher matcher1 = pattern1.matcher(str);
//            if (!matcher1.matches()) {
//                return "Special Characters are not allowed";
//            }
//
//            if (str.length() >= min && str.length() <= max) {
//            } else {
//                return "Field length must be 4";
//            }
//            Integer.parseInt(str);
//            return "Success";
//        } catch (NumberFormatException ex) {
//            return "Given Data Format Wrong";
//        }
//    }
//
//    public String distValid(String str) {
//        try {
//            boolean checkFile = this.loadPropertirs();
//            if (!checkFile) {
//                return "";
//            }
//            Integer min = Integer.parseInt(prop.getProperty("int_min_len").trim());
//            Integer max = Integer.parseInt(prop.getProperty("int_mix_dist").trim());
//
//            Pattern pattern1 = Pattern.compile(ONLY09_PATTEREN);
//            Matcher matcher1 = pattern1.matcher(str);
//            if (!matcher1.matches()) {
//                return "Special Characters are not allowed";
//            }
//
//            if (str.length() >= min && str.length() <= max) {
//            } else {
//                return "Field length must be 2";
//            }
//            Integer.parseInt(str);
//            return "Success";
//        } catch (NumberFormatException ex) {
//            return "Given Data Format Wrong";
//        }
//    }
//
//    public String villageValid(String str) {
//        try {
//            boolean checkFile = this.loadPropertirs();
//            if (!checkFile) {
//                return "";
//            }
//            Integer min = Integer.parseInt(prop.getProperty("vill_int_min").trim());
//            Integer max = Integer.parseInt(prop.getProperty("vill_int_max").trim());
//
//            Pattern pattern1 = Pattern.compile(ONLY09_PATTEREN);
//            Matcher matcher1 = pattern1.matcher(str);
//            if (!matcher1.matches()) {
//                return "Special Characters are not allowed";
//            }
//
//            if (str.length() >= min && str.length() <= max) {
//            } else {
//                return "Please Select Proper Village Name";
//            }
//            Integer.parseInt(str);
//            return "Success";
//        } catch (NumberFormatException ex) {
//            return "Given Data Format Wrong";
//        }
//    }
//
//    public boolean SessionStillLive(HttpServletRequest request, HttpServletResponse response) {
//        boolean sessionExists = true;
//        LoginBean lb;
//        clear_Cache(request, response);
//
//        lb = (LoginBean) request.getSession().getAttribute("loginbean");
//        if (lb == null || lb.equals("") || lb.equals(null)) {
//            sessionExists = false;
//            return false;
//        }
//        String username = lb.getUser_id().trim();
//        if (username == null) {
//            sessionExists = false;
//            return false;
//        }
//        getHeaderMethods(request, response);
//        return sessionExists;
//    }
//
//    public void getHeaderMethods(HttpServletRequest request, HttpServletResponse response) {
//        String method = request.getMethod();
//        String uri = request.getRequestURI();
//        String proto = request.getProtocol();
//        Enumeration headerNames = request.getHeaderNames();
//        while (headerNames.hasMoreElements()) {
//            String headerName = (String) headerNames.nextElement();
//            String sid = (String) request.getSession().getId();
//            if (headerName.equals("JSESSIONID")) {
//                if (request.getHeader(headerName).equals(sid)) {
//                }
//            }
//        }
//    }
//
//    public void clear_Cache(HttpServletRequest request, HttpServletResponse response) {
//        response.setHeader("Cache-Control", "no-cache, no-store");
//        response.setHeader("Pragma", "no-cache");
//        response.setDateHeader("Expires", -1);
//        response.setHeader("Vary", "*");
//        response.setDateHeader("Expires", 0);
//        response.setHeader("Pragma", "no-cache,no-store");
//        if (request.getProtocol().equals("HTTP/1.1")) {
//            response.setHeader("Cache-Control", "no-cache");
//        }
//    }
//
//    public String isLang(String str) {
//        try {
//            Pattern pattern1 = Pattern.compile(SOMESPECIALCHAR_PATTERN);
//            Matcher matcher1 = pattern1.matcher(str);
//            if (!matcher1.matches()) {
//                return "Special Characters are not allowed";
//            }
//            return "Success";
//        } catch (NumberFormatException ex) {
//            return "Invalid Data";
//        }
//    }
//
//    public String isSpecialChar(String str) {
//        try {
//            str = findAllreplacements(str);
//            Pattern pattern1 = Pattern.compile(SPECIALCHAR_PATTERN);
//            Matcher matcher1 = pattern1.matcher(str);
//            if (!matcher1.matches()) {
//                return "Special Characters are not allowed";
//            }
//            return "Success";
//        } catch (NumberFormatException ex) {
//            return "Invalid Data";
//        }
//    }
//
//    public static String IndianNumberFormat(double value) {
//        String stringValue = new Double(value).toString();
//        DecimalFormat df = new DecimalFormat("0.00");
//        stringValue = df.format(value);
//
//        // Seperating Integral and Fractional parts
//        int dotLocation = stringValue.indexOf(".");
//        String integralPart = stringValue.substring(0, dotLocation);
//        String fractionalPart = stringValue.substring(dotLocation + 1);
//
//        //Logic to insert comma(s)
//        int i = 3;
//
//        StringBuffer sb = new StringBuffer(integralPart);
//        sb = sb.reverse();
//        if (integralPart.length() > 3) {
//            sb.insert(i, ',');
//        }
//        while (i < integralPart.length() && integralPart.length() > 5) {
//            i = i + 3;
//            sb.insert(i, ',');
//        }
//
//        sb = sb.reverse();
//        integralPart = sb.toString();
//
//        if (integralPart.startsWith(",")) {
//            integralPart = integralPart.substring(1);
//        }
//        // Combining Integral and Fractional parts
//        // stringValue = integralPart + "." + fractionalPart;(COMMENTED BY PD)
//        stringValue = integralPart;
//
//        // Returning the result
//        return stringValue;
//    }
//
//    public String ValidName_with_spaces(String name) {
//        Pattern p = Pattern.compile("^\\p{L}+[\\p{L}\\p{Z}\\p{P}]{0,}");
//        Matcher m = p.matcher(name);
//
//        if (!m.matches()) {
//            return "false";
//        }
//        return "true";
//    }
}
