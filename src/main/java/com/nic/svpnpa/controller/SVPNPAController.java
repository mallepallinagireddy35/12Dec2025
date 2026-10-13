package com.nic.svpnpa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.nic.svpnpa.dao.LoginDao;
import com.nic.svpnpa.model.ObjectBean;
import com.nic.svpnpa.model.WebMenuBean;
import com.nic.svpnpa.model.WebsiteHits;
import com.nic.svpnpa.service.SVPNPAService;
import com.nic.svpnpa.service.WebsiteHitsService;
import com.nic.svpnpa.util.DBUtils;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.sql.DataSource;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64.Decoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;

import javax.imageio.ImageIO;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class SVPNPAController {
	
	private static final Logger logger = LogManager.getLogger(SVPNPAController.class);
	
	@Autowired
    private CacheManager cacheManager;
	
	@Scheduled(fixedRateString = "${clear.all.cache.fixed.rate}", initialDelayString = "${clear.all.cache.init.delay}") // reset cache every hr, with delay of 1hr after app start
    public void reportCurrentTime() {
       cacheManager.getCacheNames().parallelStream().forEach(name -> cacheManager.getCache(name).clear());
    }
	
	@Value("${galleryPath}")
	String galleryPath;
	String role;
	private StringBuilder viewObject;
	private String page_title;
	private String page_description;
	private String page_keywords;

	@Autowired
	private SVPNPAService service;

	@Autowired
	private LoginDao loginDaoImpl;

	@Autowired
	LoginUserDetailsController loginController;

	private String lang = "E";	
	
	private final WebsiteHitsService hitsService;

    public SVPNPAController(WebsiteHitsService hitsService) {
        this.hitsService = hitsService;
    }

	@GetMapping("/error")
	public String error(Model model) {
		model.addAttribute("errorMessage", "Request Resource is not available.");
		return "custom_error";
	}
	
	@GetMapping("/")
	public String index(Model model,HttpSession session,HttpServletRequest request, HttpServletResponse response) {
//		return "redirect:/Preview?page_id=195";		
		clearTempDirectory();
		int page_id=195;
		String username = "";	
		try {
			UserDetails userDetails = loginController.getUserDetails();
			try {
			username = userDetails.getUsername();
			}catch(Exception ex) {}
			logger.error("SVPNPAController::index::session===" + session);
			logger.error("SVPNPAController::index::lang===" + lang);
			
		List<WebMenuBean> list = loginDaoImpl.getDynamicMenuList(username,lang);
		logger.error("SVPNPAController::index::list==size===" + list.size());
		session.setAttribute("menulist", list);
		if (!username.equalsIgnoreCase("") && !username.equalsIgnoreCase("null")) {
			session.setAttribute("username", username);
		} else {
			session.setAttribute("username", "");
		}
		session.setAttribute("lang",lang.trim());
		this.role=service.getRole(username);
		session.setAttribute("role", service.getRole(username));
		service.PreviewPage(page_id, model);
		service.role=role;		
		viewObject = (StringBuilder) model.getAttribute("viewObject");		
		this.page_title=(String) model.getAttribute("page_title");
		model.addAttribute("viewObject", viewObject.toString());
		model.addAttribute("page_title",this.page_title);
		viewObject = null;
//--------------------------------------Newly-Added----------------------		
		session.setAttribute("page_id", page_id);
//----------------------------------------------------------------------
				
		String ipAddress = request.getRemoteAddr();
		hitsService.recordHit(ipAddress, "E");
		WebsiteHits hits = hitsService.getHits();
		session.setAttribute("EnglishhitCount", hits.getEnglishHits());

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception in SVPNPAController::index::" + e.getMessage());
            logger.error("Exception in SVPNPAController::index::" + e.getCause());
            logger.error("Exception in SVPNPAController::index::" + e.getClass() + "::" + e.getLocalizedMessage());
		} 
		return "Preview";
	}
	
	@GetMapping("/Preview")
	public String PreviewPage(@RequestParam int page_id, Model model, HttpSession session,HttpServletRequest request) {
		String username = "";	
		try {
			UserDetails userDetails = loginController.getUserDetails();
			try {
			username = userDetails.getUsername();
			}catch(Exception ex) {}
			logger.error("SVPNPAController::PreviewPage::session===" + session);
			logger.error("SVPNPAController::PreviewPage::lang===" + lang);
		List<WebMenuBean> list = loginDaoImpl.getDynamicMenuList(username,lang);
		System.out.println("list==="+list.size());
		logger.error("SVPNPAController::PreviewPage::list==size===" + list.size());
		session.setAttribute("menulist", list);
		if (!username.equalsIgnoreCase("") && !username.equalsIgnoreCase("null")) {
			session.setAttribute("username", username);
		} else {
			session.setAttribute("username", "");
		}
		session.setAttribute("lang",lang);
		this.role=service.getRole(username);
		session.setAttribute("role", service.getRole(username));
		service.PreviewPage(page_id, model);
		service.role=role;		
		viewObject = (StringBuilder) model.getAttribute("viewObject");		
		this.page_title=(String) model.getAttribute("page_title");
		this.page_description=(String) model.getAttribute("page_description");
		this.page_keywords=(String) model.getAttribute("page_keywords");
		
//--------------------------------------Newly-Added----------------------
        // Store page_id in session for JavaScript access
        session.setAttribute("page_id", page_id);
        
        // Also store in model for immediate access
        model.addAttribute("page_id", page_id);
        
        // Store the original request URL for navigation tracking
        session.setAttribute("original_url", "/Preview?page_id=" + page_id);
        
        String ipAddress = request.getRemoteAddr();
		hitsService.recordHit(ipAddress, lang);
		WebsiteHits hits = hitsService.getHits();
		session.setAttribute("EnglishhitCount", hits.getEnglishHits());
//----------------------------------------------------------------------		
		} catch (Exception e) {			
			logger.error("Exception in SVPNPAController::PreviewPage::" + e.getMessage());
            logger.error("Exception in SVPNPAController::PreviewPage::" + e.getCause());
            logger.error("Exception in SVPNPAController::PreviewPage::" + e.getClass() + "::" + e.getLocalizedMessage());
		}
		//return "redirect:/home";
		return "Preview";
	}

	
	@GetMapping("/home")
	public String home(Model model, HttpSession session,HttpServletRequest request) {
		try {
			if (viewObject == null) {
				return "redirect:/Preview?page_id=195";
			}
			
			model.addAttribute("viewObject", viewObject.toString());
			model.addAttribute("page_title",this.page_title);
			model.addAttribute("page_description",this.page_description);
			model.addAttribute("page_keywords",this.page_keywords);
			// Pass page_id to the view
            Object pageId = session.getAttribute("page_id");
            if (pageId != null) {
                model.addAttribute("page_id", pageId);
            }
            
            // Pass original URL to the view
            Object originalUrl = session.getAttribute("original_url");
            if (originalUrl != null) {
                model.addAttribute("original_url", originalUrl);
            }
			viewObject = null;
			
			
			String ipAddress = request.getRemoteAddr();
			hitsService.recordHit(ipAddress, lang);
			WebsiteHits hits = hitsService.getHits();
			session.setAttribute("EnglishhitCount", hits.getEnglishHits());
		} catch (Exception e) {			
			logger.error("Exception in SVPNPAController::home::" + e.getMessage());
            logger.error("Exception in SVPNPAController::home::" + e.getCause());
            logger.error("Exception in SVPNPAController::home::" + e.getClass() + "::" + e.getLocalizedMessage());
		}
		return "Preview";
	}
	
	@GetMapping("/api/navigation-history")
    @ResponseBody
    public Map<String, Object> getNavigationHistory(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        response.put("current_page_id", session.getAttribute("page_id"));
        response.put("original_url", session.getAttribute("original_url"));
        response.put("username", session.getAttribute("username"));
        return response;
    }


	@GetMapping("/designTemplate")
	public String designTemplate() {
		return "templateDesigner";
	}

	@GetMapping("/logoutStatic")
	public String loggedOut(HttpServletResponse response) {
		Cookie cookieLogin = new Cookie("status", "Login");
		cookieLogin.setMaxAge(0);
		cookieLogin.setSecure(true);
		response.addCookie(cookieLogin);
		return "redirect:/";
	}
 
	@ResponseBody
	@GetMapping("/updateCookie")
	public String setCookie(@CookieValue(value = "lang", defaultValue = "E") String lang, HttpServletRequest request, HttpServletResponse response,HttpSession session,
			@RequestParam(name = "langSelected", required = false) String langSelected, Model model) {
		String username = "";
		try {
			UserDetails userDetails = loginController.getUserDetails();
			try {
			username = userDetails.getUsername();
			}catch(Exception ex) {}
			
//		    String ipAddress = request.getRemoteAddr();
//			if(langSelected.equalsIgnoreCase("H")) {
////			hitsService.incrementHindiHits();
////	        WebsiteHits hits = hitsService.getHits();
////	        session.setAttribute("HindihitCount", hits.getHindiHits());
//				
//				hitsService.recordHit(ipAddress, "H");
//				WebsiteHits hits = hitsService.getHits();
//				session.setAttribute("HindihitCount", hits.getHindiHits());
//	        }if(langSelected.equalsIgnoreCase("E")) {
//			hitsService.recordHit(ipAddress, "E");
//			WebsiteHits hits = hitsService.getHits();
//			session.setAttribute("EnglishhitCount", hits.getEnglishHits());
//		}
			
		List<WebMenuBean> list = loginDaoImpl.getDynamicMenuList(username, langSelected);
		if (langSelected == null || langSelected.isEmpty())
			langSelected = lang;
		if (langSelected.equals("E") || langSelected.equals("H")) {
//			Cookie cookie = new Cookie("lang", langSelected);
//			cookie.setMaxAge(Integer.MAX_VALUE);
//			cookie.setHttpOnly(true);
//			cookie.setSecure(true);
//			response.addCookie(cookie);
			session.setAttribute("lang", langSelected);
			this.lang = langSelected;
			service.lang = langSelected;
			return "Success";
		} else {
			return null;
		}		
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception in SVPNPAController::setCookie::" + e.getMessage());
            logger.error("Exception in SVPNPAController::setCookie::" + e.getCause());
            logger.error("Exception in SVPNPAController::setCookie::" + e.getClass() + "::" + e.getLocalizedMessage());
		}
		return null;
	}

	@PostMapping("/saveTemplate")
	@ResponseBody
	public String saveTemplate(@RequestBody Map<String, Object> typeMap) {
		try {
		service.saveTemplate(typeMap);
		} catch (Exception e) {
			logger.error("Exception in SVPNPAController::saveTemplate::" + e.getMessage());
            logger.error("Exception in SVPNPAController::saveTemplate::" + e.getCause());
            logger.error("Exception in SVPNPAController::saveTemplate::" + e.getClass() + "::" + e.getLocalizedMessage());
		}
		return "Success";
	}

	
	
	@GetMapping("/gallery")
	public String gallery() {
		return "gallery";
	}

	

	@GetMapping("/galleryObjects")
	public String galleryObjects(Model model, @RequestParam String type) {
		try {
		model.addAttribute("type", type);
		} catch (Exception e) {
			logger.error("Exception in SVPNPAController::galleryObjects::" + e.getMessage());
            logger.error("Exception in SVPNPAController::galleryObjects::" + e.getCause());
            logger.error("Exception in SVPNPAController::galleryObjects::" + e.getClass() + "::" + e.getLocalizedMessage());
		}
		return "GalleryDesigner";
	}

	@GetMapping("/pageObjects")
	public String pageObjects() {
		return "home";
	}

	@GetMapping("/templates")
	public String template(Model model) {
		try {
		service.template(model);
		} catch (Exception e) {
			logger.error("Exception in SVPNPAController::template::" + e.getMessage());
            logger.error("Exception in SVPNPAController::template::" + e.getCause());
            logger.error("Exception in SVPNPAController::template::" + e.getClass() + "::" + e.getLocalizedMessage());
		}
		return "template";
	}

	@GetMapping("/table")
	public String indexTable() {
		return "index";
	}

	@GetMapping("/image")
	public String imageUploader() {
		return "imageUploader";
	}

	@GetMapping("/paragraph")
	public String indexParagraph(Model model) {
		return "paragraph";
	}

	@GetMapping("/embed")
	public String indexEmbed(Model model) {
		return "embed";
	}
	
	@GetMapping("/view")
	public String view(Model model) {
		try {
		service.view(model);
		} catch (Exception e) {
			logger.error("Exception in SVPNPAController::view::" + e.getMessage());
            logger.error("Exception in SVPNPAController::view::" + e.getCause());
            logger.error("Exception in SVPNPAController::view::" + e.getClass() + "::" + e.getLocalizedMessage());
		}
		return "view";
	}
	
	@GetMapping("/View_NewPages")
	public String View_NewPages(Model model) {
		try {
		service.View_NewPages(model);
		} catch (Exception e) {
			logger.error("Exception in SVPNPAController::View_NewPages::" + e.getMessage());
            logger.error("Exception in SVPNPAController::View_NewPages::" + e.getCause());
            logger.error("Exception in SVPNPAController::View_NewPages::" + e.getClass() + "::" + e.getLocalizedMessage());
		}
		return "view";
	}
	
	@GetMapping("/View_ArchivedPages")
	public String View_ArchivedPages(Model model) {
		try {
		service.View_ArchivedPages(model);
		} catch (Exception e) {
			logger.error("Exception in SVPNPAController::View_NewPages::" + e.getMessage());
            logger.error("Exception in SVPNPAController::View_NewPages::" + e.getCause());
            logger.error("Exception in SVPNPAController::View_NewPages::" + e.getClass() + "::" + e.getLocalizedMessage());
		}
		return "view";
	}

	@GetMapping("/getPage")
	public String pageIndex(Model model) {
		try {
		service.pageIndex(model);
		} catch (Exception e) {
			logger.error("Exception in SVPNPAController::pageIndex::" + e.getMessage());
            logger.error("Exception in SVPNPAController::pageIndex::" + e.getCause());
            logger.error("Exception in SVPNPAController::pageIndex::" + e.getClass() + "::" + e.getLocalizedMessage());
		}
		return "PageDesigner";

	}

	@GetMapping("/designPage")
	public String page(Model model, @RequestParam(name = "page_name", required = false) String page_name,
			@RequestParam(name = "template_id", required = false) String template_id,
			@RequestParam(name = "page_id", required = false) String page_id) {
		try {
		service.page(model, page_name, template_id, page_id);
		} catch (Exception e) {
			logger.error("Exception in SVPNPAController::page::" + e.getMessage());
            logger.error("Exception in SVPNPAController::page::" + e.getCause());
            logger.error("Exception in SVPNPAController::page::" + e.getClass() + "::" + e.getLocalizedMessage());
		}
		return "PageDesigner";
	}

	@PostMapping("/design")
	public String design(Model model, @RequestParam int rowCount, @RequestParam int colCount,
			@RequestParam String object_title, @RequestParam String description, @RequestParam String help_text,
			@RequestParam String alternate_text, @RequestParam String keywords) {
		try {
		service.design(model, rowCount, colCount, object_title, description, help_text, alternate_text, keywords);
		} catch (Exception e) {
			logger.error("Exception in SVPNPAController::design::" + e.getMessage());
            logger.error("Exception in SVPNPAController::design::" + e.getCause());
            logger.error("Exception in SVPNPAController::design::" + e.getClass() + "::" + e.getLocalizedMessage());
		}
		return "index";
	}

	@PostMapping("/designList")
	public String designList(Model model, @RequestParam int rowCount) {
		try {
		service.designList(model, rowCount);
		} catch (Exception e) {
			logger.error("Exception in SVPNPAController::designList::" + e.getMessage());
            logger.error("Exception in SVPNPAController::designList::" + e.getCause());
            logger.error("Exception in SVPNPAController::designList::" + e.getClass() + "::" + e.getLocalizedMessage());
		}
		return "ListDesigner";
	}

	@PostMapping("/designGalleryObjects")
	public String designGalleryObjects(Model model, @RequestParam String object_title, @RequestParam String type,
			@RequestParam(name = "object_id", required = false) String object_id) {
		try {
		service.designGalleryObjects(model, object_title, type, object_id);
		} catch (Exception e) {
			logger.error("Exception in SVPNPAController::designGalleryObjects::" + e.getMessage());
            logger.error("Exception in SVPNPAController::designGalleryObjects::" + e.getCause());
            logger.error("Exception in SVPNPAController::designGalleryObjects::" + e.getClass() + "::" + e.getLocalizedMessage());
		}
		return "GalleryDesigner";
	}

	@GetMapping("/fetch")
	public String designTable(@RequestParam int object_id, Model model,
			@RequestParam(name = "newRow", required = false) String newRow,
			@RequestParam(name = "newCol", required = false) String newCol) {	
		String returnPage = service.designTable(object_id, model, newRow, newCol);
		try {
		} catch (Exception e) {	
			e.printStackTrace();
			logger.error("Exception in SVPNPAController::designTable::" + e.getMessage());
            logger.error("Exception in SVPNPAController::designTable::" + e.getCause());
            logger.error("Exception in SVPNPAController::designTable::" + e.getClass() + "::" + e.getLocalizedMessage());
		}
		return returnPage;
	}

	@GetMapping("/getObjects")
	@ResponseBody
	public ArrayList<HashMap<String, Object>> getObjects() {		
		return service.objectsList();		
	}

	@GetMapping("/viewObjects")
	public String viewObjects(Model model) {
		try {
			model.addAttribute("options", service.objectsList());
		} catch (Exception e) {
			logger.error("Exception in SVPNPAController::viewObjects::" + e.getMessage());
            logger.error("Exception in SVPNPAController::viewObjects::" + e.getCause());
            logger.error("Exception in SVPNPAController::viewObjects::" + e.getClass() + "::" + e.getLocalizedMessage());
		}
		return "viewObjects";
	}

	@PostMapping("/updateItem")
	@ResponseBody
	public String updateItem(@RequestBody Map<String, Object> typeMap) {
		String temp = service.updateItem(typeMap);
		return temp;
	}

	public ArrayList<HashMap<String, Object>> objectsList() {
		ArrayList<HashMap<String, Object>> selectData2 = null;
		selectData2 = service.objectsList();
		return selectData2;
	}

	@GetMapping("/viewPage")
	public String viewObject(@RequestParam int page_id, @RequestParam(name = "admin", required = false) String admin,
			Model model) {
		try {
		this.viewObject = new StringBuilder(service.viewPageContents(page_id));
		model.addAttribute("viewObject", viewObject);
		model.addAttribute("page_id", page_id);
		} catch (Exception e) {
			logger.error("Exception in SVPNPAController::viewObject::" + e.getMessage());
            logger.error("Exception in SVPNPAController::viewObject::" + e.getCause());
            logger.error("Exception in SVPNPAController::viewObject::" + e.getClass() + "::" + e.getLocalizedMessage());
		}
		return "view";
	}

	@PostMapping("/getGalleryGroups")
	@ResponseBody
	public List getGalleryGroups(@RequestBody Map<String, Object> typeMap) {
		return service.groups((String) typeMap.get("type"));
	}

	@PostMapping("/getGalleryItems")
	@ResponseBody
	public ArrayList<HashMap<String, Object>> getGalleryItems(@RequestBody Map<String, Object> typeMap) {
		ArrayList<HashMap<String, Object>> items = null;
		items = service.getGalleryItems(typeMap);		
		return items;

	}

	@PostMapping("/getObjectsTypeWise")
	@ResponseBody
	public ArrayList<HashMap<String, Object>> getObjectTypeWise(@RequestBody Map<String, Object> typeMap,@RequestHeader(name = "X-XSRF-TOKEN") String csrfToken,
	          @RequestHeader(name = "X-XSRF-HEADER") String csrfHeader) {		
		ArrayList<HashMap<String, Object>> items = null;
		try {			
		items = service.getObjectTypeWise(typeMap);
		}catch(Exception e){
			logger.error("Exception in SVPNPAController::getObjectTypeWise::" + e.getMessage());
	        logger.error("Exception in SVPNPAController::getObjectTypeWise::" + e.getCause());
	        logger.error("Exception in SVPNPAController::getObjectTypeWise::" + e.getClass() + "::" + e.getLocalizedMessage());
	    }
		return items;
	}

		@GetMapping("/approveObject")
	public String approveObject(@RequestParam int object_id) {
		service.approveObject(object_id,role);
		return "home";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)	
	@ResponseBody
	public ResponseEntity<?>  saveTableData(
			@RequestHeader(name = "X-XSRF-TOKEN") String csrfToken,
	        @RequestHeader(name = "X-XSRF-HEADER") String csrfHeader,
	        @RequestBody Map<String, Object> jsonData
			) {
		logger.info("save====");		
		Map<String, String> response = new HashMap<>();
		ObjectBean bean = new ObjectBean();		
		try {
			if (jsonData == null) {
				bean = new ObjectBean();					
				response.put("success", "Can not Insert/Update Empty Data");
			} else {
				ObjectMapper objectMapper = new ObjectMapper();
				// Convert map to JSON string
				String jsonString1 = objectMapper.writeValueAsString(jsonData);	
				bean = objectMapper.readValue(jsonString1, ObjectBean.class);
				
				System.out.println("Object_title---"+bean.getObject_title()+"===="+bean.getRandom());	
				
	        int i =  service.saveTableData(bean.getObject_id(), bean.getImgType(), bean.getJsonString(), bean.getType(), bean.getMarquee(), bean.getLinksString(), bean.getNewJson(), bean.getList_type(), bean.getExpString(),
			bean.getObject_title(), bean.getObject_title_hi(), bean.getStyles(), bean.getClasses(), bean.getDescription(), bean.getHelp_text(), bean.getAlternate_text(), bean.getKeywords(),bean.getJsonString_hi(), bean.getMeta_hi(),bean.getRandom());	        
	        if(i>0) {	        	
	        	System.out.println("if---"+i);	        	
	        	response.put("success", "Successfully Stored/Update the Data");
	        	return ResponseEntity.ok(response);
			} else {
				response.put("success", "Data Insertion/Updation Fail");
				return ResponseEntity.ok(response);
		    }
		 }
		} catch (Exception e) {	
			e.printStackTrace();
			logger.error("Exception in SVPNPAController::saveTableData::" + e.getMessage());
            logger.error("Exception in SVPNPAController::saveTableData::" + e.getCause());
            logger.error("Exception in SVPNPAController::saveTableData::" + e.getClass() + "::" + e.getLocalizedMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Object Data Insertion/Updation Fail");
		}
		return ResponseEntity.ok(response);
	}

	@RequestMapping(value = "/savePage", method = RequestMethod.POST)	
	@ResponseBody
	public ResponseEntity<?>  savePage(
			@RequestHeader(name = "X-XSRF-TOKEN") String csrfToken,
	        @RequestHeader(name = "X-XSRF-HEADER") String csrfHeader,
	        @RequestBody Map<String, Object> jsonData) {
		Map<String, String> response = new HashMap<>();
		ObjectBean bean = new ObjectBean();	
		int i = 0;
		try {
			if (jsonData == null) {
				bean = new ObjectBean();					
				response.put("success", "Can not Insert/Update Empty Data");
			} else {
				ObjectMapper objectMapper = new ObjectMapper();
				// Convert map to JSON string
				String jsonString1 = objectMapper.writeValueAsString(jsonData);	
				bean = objectMapper.readValue(jsonString1, ObjectBean.class);
				System.out.println("jsonString==="+bean.getJsonString());
				System.out.println("template_id==="+bean.getTemplate_id()+"====page_id_given==="+bean.getPage_id_given()+"===page_name==="+bean.getPage_name()+"====description==="+bean.getDescription()+"===keywords===="+bean.getKeywords());
				
			i = service.savePageData(bean.getJsonString(), Integer.parseInt(bean.getTemplate_id()), bean.getPage_id_given(), bean.getPage_name(), bean.getDescription(), bean.getKeywords());
			System.out.println("i==="+i);
			if(i>0) {	        	
	        	System.out.println("if---"+i);	        	
	        	response.put("success", "Successfully Stored/Update the Page Data");
	        	return ResponseEntity.ok(response);
			} else {
				System.out.println("else---"+i);
				response.put("success", "Page Data Insertion/Updation Fail");
				return ResponseEntity.ok(response);
		    }
		 }
	} catch (Exception e) {	
		e.printStackTrace();
		logger.error("Exception in SVPNPAController::savePageData::" + e.getMessage());
        logger.error("Exception in SVPNPAController::savePageData::" + e.getCause());
        logger.error("Exception in SVPNPAController::savePageData::" + e.getClass() + "::" + e.getLocalizedMessage());
	}
	return ResponseEntity.ok(response);
	}

	@GetMapping("/list")
	public String listDesigner(Model model) {
		return "ListDesigner";
	}

	@GetMapping("/getImage")
	public String getImage(Model model) {
		service.getImage(model);
		return "imageUploader";

	}

	@PostMapping("/uploadImage")
	public String uploadImage(Model model, MultipartFile file[], @RequestParam List<String> file_desc,
			@RequestParam String group_id) throws IOException {
		service.uploadImage(model, file, file_desc, group_id);
		return "gallery";
	}

	@PostMapping("/uploadImage1")
	public String uploadImage1(Model model, MultipartFile file[]) throws IOException {
		service.uploadImage1(model, file);
		return "image_test";
	}

	@GetMapping("/audio")
	public String getAudio(Model model) {
		service.getAudio(model);
		return "audioUpload";

	}

	@PostMapping("/uploadAudio")
	public String uploadAudio(Model model, MultipartFile file[], @RequestParam String group_id) throws IOException {

		service.uploadAudio(model, file, group_id);
		return "redirect:/gallery";
	}

	@GetMapping("/video")
	public String getVideo(Model model) {

		service.getVideo(model);
		return "videoUpload";

	}

	@PostMapping("/deleteObject")
	@ResponseBody
	public String deleteObject(@RequestBody Map<String, Object> object_json,@RequestHeader(name = "X-XSRF-TOKEN") String csrfToken,
	          @RequestHeader(name = "X-XSRF-HEADER") String csrfHeader) {
		service.deleteObject(object_json);
		return "success";

	}

	@PostMapping("/publishPage")
	@ResponseBody
	public String publishPage(@RequestParam int page_id) {
		service.publishPage(page_id);
		return "success";

	}

	@PostMapping("/deletePage")
	@ResponseBody
	public String deletePage(@RequestBody Map<String, Object> page_json) {
		service.deletePage(page_json);
		return "success";
	}

	@PostMapping("/uploadVideo")
	public String uploadVideo(Model model, MultipartFile file[], @RequestParam String group_id) throws IOException {

		service.uploadVideo(model, file, group_id);
		return "redirect:/gallery";
	}

	@GetMapping("/doc")
	public String getDoc(Model model) {
		return "docupload";
	}

	@PostMapping("/uploadDoc")
	public String uploadDoc(Model model, MultipartFile file[], @RequestParam("file_desc[]") String[] file_desc,
			@RequestParam String group_id) throws IOException {

		service.uploadDoc(model, file, file_desc, group_id);
		return "gallery";
	}

	@PostMapping("/getindividualobject")
	@ResponseBody
	public String[][] getindividualobject(@RequestBody Map<String, Object> typeMap) {
		String[][] items = null;
		try {
		items = service.getindividualobject(typeMap);
		} catch (Exception e) {
			logger.error("Exception in SVPNPAController::getindividualobject::" + e.getMessage());
            logger.error("Exception in SVPNPAController::getindividualobject::" + e.getCause());
            logger.error("Exception in SVPNPAController::getindividualobject::" + e.getClass() + "::" + e.getLocalizedMessage());
		}
		return items;
	}
	@GetMapping("/getListPages")
	@ResponseBody
	public ArrayList<HashMap<String, Object>> getListPages(Model model) {
		ArrayList<HashMap<String, Object>> items = null;
		try {
		items = service.getListPages();
		} catch (Exception e) {
			logger.error("Exception in SVPNPAController::getListPages::" + e.getMessage());
            logger.error("Exception in SVPNPAController::getListPages::" + e.getCause());
            logger.error("Exception in SVPNPAController::getListPages::" + e.getClass() + "::" + e.getLocalizedMessage());
		}
		return items;
	}
	@GetMapping("/universalSearch")
	public String searchUniversal(@RequestParam String value,Model model) {
		ArrayList<HashMap<String, Object>> items = null;
		try {
		items = service.searchUniversal(value);
		model.addAttribute("queryReturned",items);
		} catch (Exception e) {
			logger.error("Exception in SVPNPAController::searchUniversal::" + e.getMessage());
            logger.error("Exception in SVPNPAController::searchUniversal::" + e.getCause());
            logger.error("Exception in SVPNPAController::searchUniversal::" + e.getClass() + "::" + e.getLocalizedMessage());
		}
		return "UniversalSearch";
	}
	@GetMapping("/getListForms")
	@ResponseBody
	public ArrayList<HashMap<String, Object>> getListForms(Model model) {
		ArrayList<HashMap<String, Object>> items = null;
		try {
		items = service.getListForms();
		} catch (Exception e) {
			logger.error("Exception in SVPNPAController::getListForms::" + e.getMessage());
            logger.error("Exception in SVPNPAController::getListForms::" + e.getCause());
            logger.error("Exception in SVPNPAController::getListForms::" + e.getClass() + "::" + e.getLocalizedMessage());
		}
		return items;
	}
	@GetMapping("/getListDocs")
	@ResponseBody
	public ArrayList<HashMap<String, Object>> getListDocs(Model model) {
		ArrayList<HashMap<String, Object>> items = null;
		try {
		items = service.getListDocs();
		} catch (Exception e) {
			logger.error("Exception in SVPNPAController::getListDocs::" + e.getMessage());
            logger.error("Exception in SVPNPAController::getListDocs::" + e.getCause());
            logger.error("Exception in SVPNPAController::getListDocs::" + e.getClass() + "::" + e.getLocalizedMessage());
		}
		return items;
	}
	
//	@PostMapping("/getFileNames")
//	@ResponseBody
//	public String[][] getFileNames(@RequestBody Map<String, Object> typeMap) {
//		String[][] items = null;
//		try {
//		items = service.getindividualobject(typeMap);
//		} catch (Exception e) {
//			logger.error("Exception in SVPNPAController::getFileNames::" + e.getMessage());
//            logger.error("Exception in SVPNPAController::getFileNames::" + e.getCause());
//            logger.error("Exception in SVPNPAController::getFileNames::" + e.getClass() + "::" + e.getLocalizedMessage());
//		}
//		return items;
//	}
	
	@PostMapping("/getFileNames")
	@ResponseBody
	public ArrayList<HashMap<String, String>> getFileNames(@RequestBody Map<String, Object> filetype,
			  @RequestHeader(name = "X-XSRF-TOKEN") String csrfToken,
	          @RequestHeader(name = "X-XSRF-HEADER") String csrfHeader) {	
		return service.getFileNames(filetype);
		
	}
	
	@RequestMapping(value = "/updateImageDes", method = RequestMethod.POST)	
	@ResponseBody
	public ResponseEntity<?>  updateImageDes(
			@RequestHeader(name = "X-XSRF-TOKEN") String csrfToken,
	        @RequestHeader(name = "X-XSRF-HEADER") String csrfHeader,
	        @RequestBody Map<String, Object> jsonData) {
		System.out.println("updateImageDes===="+jsonData.toString());	
		logger.info("updateImageDes====");
		Map<String, String> response = new HashMap<>();
		ObjectBean bean = new ObjectBean();		
		try {
			if (jsonData == null) {
				bean = new ObjectBean();					
				response.put("success", "Can not Update Empty Data");
			} else {
				ObjectMapper objectMapper = new ObjectMapper();
				// Convert map to JSON string
				String jsonStr = objectMapper.writeValueAsString(jsonData);	
				bean = objectMapper.readValue(jsonStr, ObjectBean.class);
				System.out.println("===="+bean.getJsonString1().toString()+"==="+bean.getDescription());
		        int i =  service.updateImageDes(bean.getJsonString1(), bean.getDescription());	        
	        if(i>0) {	        	
	        	System.out.println("if---"+i);	        	
	        	response.put("success", "Successfully Updated the Image Description");
	        	return ResponseEntity.ok(response);
			} else {
				response.put("success", "Image Description Updation Fail");
				return ResponseEntity.ok(response);
		    }
		 }
		} catch (Exception e) {	
			e.printStackTrace();
			logger.error("Exception in SVPNPAController::updateImageDes::" + e.getMessage());
            logger.error("Exception in SVPNPAController::updateImageDes::" + e.getCause());
            logger.error("Exception in SVPNPAController::updateImageDes::" + e.getClass() + "::" + e.getLocalizedMessage());
		}
		return ResponseEntity.ok(response);
	}
	
//-------------------------04-01-2025---------------------------------------------------------
			@PostMapping("/archivePage")
			@ResponseBody
			public String archivePage(@RequestBody Map<String, Object> page_json) {
				System.out.println("Archive Page=====");
				service.archivePage(page_json);
				return "success";
			}
			
			@PostMapping("/unarchivePage")
			@ResponseBody
			public String unarchivePage(@RequestBody Map<String, Object> page_json) {
				System.out.println("Unarchive Page=====");
				service.unarchivePage(page_json);
				return "success";
			}
	
//------------------------25-09-2026--------------------------------------------------------------
			@RequestMapping(value = "/deleteUser", method = RequestMethod.POST)	
			@ResponseBody
			public ResponseEntity<?>  deleteUser(
					@RequestHeader(name = "X-XSRF-TOKEN") String csrfToken,
			        @RequestHeader(name = "X-XSRF-HEADER") String csrfHeader,
			        @RequestBody Map<String, Object> jsonData
					) {
				logger.info("deleteUser====");		
				Map<String, String> response = new HashMap<>();
				ObjectBean bean = new ObjectBean();		
				try {
					if (jsonData == null) {
						bean = new ObjectBean();					
						response.put("success", "Can not Delete Empty User");
					} else {
						ObjectMapper objectMapper = new ObjectMapper();
						// Convert map to JSON string
						String jsonString1 = objectMapper.writeValueAsString(jsonData);	
						bean = objectMapper.readValue(jsonString1, ObjectBean.class);
						
						System.out.println("username---"+bean.getUsername()+"===id==="+bean.getId());	
						
			        int i =  service.deleteUser(bean.getUsername(), bean.getId());	        
			        if(i>0) {	        	
			        	System.out.println("if---"+i);	        	
			        	response.put("success", "Successfully Deleted the User");
			        	return ResponseEntity.ok(response);
					} else {
						response.put("success", "User Deletion Fail");
						return ResponseEntity.ok(response);
				    }
				 }
				} catch (Exception e) {	
					e.printStackTrace();
					logger.error("Exception in SVPNPAController::deleteUser::" + e.getMessage());
		            logger.error("Exception in SVPNPAController::deleteUser::" + e.getCause());
		            logger.error("Exception in SVPNPAController::deleteUser::" + e.getClass() + "::" + e.getLocalizedMessage());
				}
				return ResponseEntity.ok(response);
			}
//-------------------------------------------------------------------------------------------------------------
	
	@GetMapping("/translation")
	public ModelAndView translation(Model model,HttpServletRequest request, HttpServletResponse response,HttpSession session) {		
   		String username = "";		
		try {
			UserDetails userDetails = loginController.getUserDetails();
			try {
			username = userDetails.getUsername();
			}catch(Exception ex) {}
		List<WebMenuBean> list = loginDaoImpl.getDynamicMenuList(username,lang);
		session.setAttribute("menulist", list);
		if (!username.equalsIgnoreCase("") && !username.equalsIgnoreCase("null")) {
			session.setAttribute("username", username);
		} else {
			session.setAttribute("username", "");
		}
		session.setAttribute("lang",lang);
		this.role=service.getRole(username);
		session.setAttribute("role", service.getRole(username));
		service.role=role;				
		} catch (Exception e) {			
			logger.error("Exception in SVPNPAController::translation::" + e.getMessage());
            logger.error("Exception in SVPNPAController::translation::" + e.getCause());
            logger.error("Exception in SVPNPAController::translation::" + e.getClass() + "::" + e.getLocalizedMessage());
		}
		return new ModelAndView("paragraph_trans");
	}
	
	@PostMapping("/translateContent")
    @ResponseBody
    public String translateContent(@RequestParam("_csrf")String csrfToken,@RequestParam ("englishContent")String englishContent,HttpServletRequest request) {
    String response = "";
		try {
//		response = smsService.sendSms1(englishContent);
			HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_JSON);

	        String apiUrl = "https://panini.ai.nic.in/translation" ;   
	        
	        String sourceLanguage = "eng_Latn";
	        String targetLanguage = "hin_Deva";
	     // Request body parameters
	        String requestBody = String.format("{\"text\": \"%s\", \"src_lan\": \"%s\", \"tar_lan\": \"%s\"}",
	        		englishContent, sourceLanguage, targetLanguage);

	        System.out.println("requestBody==="+requestBody);
	        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
	        
	        RestTemplate restTemplate = new RestTemplate();
//	        ResponseEntity<String> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, String.class);
	        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
	        requestFactory.setConnectTimeout(60000); // 60 seconds -- 5000 -- 5 seconds
	        requestFactory.setReadTimeout(60000);    // 60 seconds
	        restTemplate.setRequestFactory(requestFactory);

	        RequestEntity req = RequestEntity
	        	     .post(URI.create("https://panini.ai.nic.in/translation"))
	        	     .accept(MediaType.APPLICATION_JSON)
	                 .body(requestBody);
	        	 ResponseEntity<String> responseEntity = restTemplate.exchange(req, String.class);
	        	 
	        	 if (responseEntity.getStatusCode().is2xxSuccessful()) {
	        		 System.out.println("1==translateContent======="+responseEntity.getBody());
	        		 logger.error("SVPNPAController::translateContent::====Successful==="+responseEntity.getBody());
	                 return responseEntity.getBody();
	             } else {
	                 // Handle error response
	            	 System.out.println("2==translateContent======="+responseEntity.getStatusCodeValue());
	            	 logger.error("SVPNPAController::translateContent::====Not Successful==="+responseEntity.getStatusCodeValue());
	                 return "" + responseEntity.getStatusCodeValue();
	             }      


		} catch (Exception e) {			
			logger.error("Exception in SVPNPAController::translateContent::" + e.getMessage());
            logger.error("Exception in SVPNPAController::translateContent::" + e.getCause());
            logger.error("Exception in SVPNPAController::translateContent::" + e.getClass() + "::" + e.getLocalizedMessage());
            return e.getMessage();
		}
		
		}
	
	private void clearTempDirectory() {
		int count = 0;
    	try {
        // Get the path to the temporary directory
        String tempDirPath = System.getProperty("java.io.tmpdir");

        // Create a File object representing the temporary directory
        File tempDir = new File(tempDirPath);

        // Check if the directory exists
        if (tempDir.exists() && tempDir.isDirectory()) {
            // Get the list of files in the temporary directory
            File[] files = tempDir.listFiles();

            // Delete each file in the temporary directory
            if (files != null) {            	
            	logger.error("SVPNPAController::clearTempDirectory::Files in the Temporary Directory");
                for (File file : files) {
                	logger.error("SVPNPAController::clearTempDirectory::File Name:::"+file.getName());
                    boolean con = file.delete();
                    if(con == true) {
                     count++;	
                    }
                }
            }else {
            	logger.error("SVPNPAController::clearTempDirectory::No Files in the Temporary Directory");
            }
            
            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");            
            logger.error("No of Cache Files Deleted::"+count+"===Date==="+formatter.format(date));
            logger.error("SVPNPAController::clearTempDirectory::Temporary directory cleared");
        } else {
            logger.error("SVPNPAController::clearTempDirectory::Temporary directory does not exist");
        }
        
} catch (Exception e) {			
			logger.error("Exception in SVPNPAController::clearTempDirectory::" + e.getMessage());
            logger.error("Exception in SVPNPAController::clearTempDirectory::" + e.getCause());
            logger.error("Exception in SVPNPAController::clearTempDirectory::" + e.getClass() + "::" + e.getLocalizedMessage());
		}
    }
	
//--------------------------------------12-10-2026-------------------------------------------------
	@GetMapping("/galleryFolderManager")
	public String galleryFolderManager(Model model) {
		return "galleryFolderManager";
	}
	
	@PostMapping("/checkGalleryFolderUsage")
	@ResponseBody
	public Map<String, Object> checkGalleryFolderUsage(@RequestBody Map<String, Object> request) {
		Map<String, Object> result = new HashMap<>();
		try {
			String groupId = request.get("groupId") != null ? request.get("groupId").toString() : null;
			Integer mediaType = request.get("mediaType") != null ? Integer.parseInt(request.get("mediaType").toString()) : null;

			if (groupId == null || groupId.trim().isEmpty() || mediaType == null) {
				result.put("success", false);
				result.put("message", "groupId and mediaType are required");
				return result;
			}

			result = service.checkGalleryFolderUsage(groupId, mediaType);
			result.put("success", true);
		} catch (Exception e) {
			logger.error("Exception in SVPNPAController::checkGalleryFolderUsage::" + e.getMessage());
			logger.error("Exception in SVPNPAController::checkGalleryFolderUsage::" + e.getCause());
			logger.error("Exception in SVPNPAController::checkGalleryFolderUsage::" + e.getClass() + "::" + e.getLocalizedMessage());
			result.put("success", false);
			result.put("message", "Error checking folder usage: " + e.getMessage());
		}
		return result;
	}

	@PostMapping("/deleteGalleryFolder")
	@ResponseBody
	public Map<String, Object> deleteGalleryFolder(@RequestBody Map<String, Object> request,
			@RequestHeader(name = "X-XSRF-TOKEN", required = false) String csrfToken,
			@RequestHeader(name = "X-XSRF-HEADER", required = false) String csrfHeader,
			jakarta.servlet.http.HttpServletRequest httpRequest) {
		Map<String, Object> result = new HashMap<>();
		try {
			String groupId = request.get("groupId") != null ? request.get("groupId").toString() : null;
			Integer mediaType = request.get("mediaType") != null ? Integer.parseInt(request.get("mediaType").toString()) : null;
			Boolean forceDelete = request.get("forceDelete") != null ? Boolean.parseBoolean(request.get("forceDelete").toString()) : false;

			if (groupId == null || groupId.trim().isEmpty() || mediaType == null) {
				result.put("success", false);
				result.put("message", "groupId and mediaType are required");
				return result;
			}

			String username = "";
			try {
				UserDetails userDetails = loginController.getUserDetails();
				username = userDetails.getUsername();
			} catch (Exception ex) {}

			// Get client IP address
			String clientIp = httpRequest.getHeader("X-Forwarded-For");
			if (clientIp == null || clientIp.isEmpty() || "unknown".equalsIgnoreCase(clientIp)) {
				clientIp = httpRequest.getHeader("Proxy-Client-IP");
			}
			if (clientIp == null || clientIp.isEmpty() || "unknown".equalsIgnoreCase(clientIp)) {
				clientIp = httpRequest.getHeader("WL-Proxy-Client-IP");
			}
			if (clientIp == null || clientIp.isEmpty() || "unknown".equalsIgnoreCase(clientIp)) {
				clientIp = httpRequest.getRemoteAddr();
			}
			// Handle multiple IPs in X-Forwarded-For (take first one)
			if (clientIp != null && clientIp.contains(",")) {
				clientIp = clientIp.split(",")[0].trim();
			}

			if (forceDelete) {
				result = service.deleteGalleryFolder(groupId, mediaType, username, clientIp);
			} else {
				result = service.checkGalleryFolderUsage(groupId, mediaType);
				if ((Boolean) result.get("hasUsage")) {
					result.put("success", false);
					result.put("message", "Folder is in use. Use forceDelete=true to override.");
					result.put("requiresConfirmation", true);
				} else {
					result = service.deleteGalleryFolder(groupId, mediaType, username, clientIp);
				}
			}
		} catch (Exception e) {
			logger.error("Exception in SVPNPAController::deleteGalleryFolder::" + e.getMessage());
			logger.error("Exception in SVPNPAController::deleteGalleryFolder::" + e.getCause());
			logger.error("Exception in SVPNPAController::deleteGalleryFolder::" + e.getClass() + "::" + e.getLocalizedMessage());
			result.put("success", false);
			result.put("message", "Error deleting folder: " + e.getMessage());
		}
		return result;
	}


	
	}

