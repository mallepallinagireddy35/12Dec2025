/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nic.svpnpa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.nic.svpnpa.dao.AdminDao;
import com.nic.svpnpa.dao.LoginDao;
import com.nic.svpnpa.dao.RoleRepository;
import com.nic.svpnpa.model.LoginBean;
import com.nic.svpnpa.model.Role;
import com.nic.svpnpa.model.WebMenuBean;


import jakarta.servlet.http.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;

@Controller
public class AdminController {
	@Autowired
	AdminDao AdminDaoImpl;
	
	@Autowired
	LoginDao LoginDaoImpl;
	
	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	LoginUserDetailsController loginUSerController;

	private static final Logger logger = LogManager.getLogger(AdminController.class);


	 @RequestMapping(value = "/admin_home", method = RequestMethod.GET)
		public ModelAndView admin_home(ModelMap model, Model m, HttpServletRequest request) {
			m.addAttribute("form", new LoginBean());
			HttpSession session = request.getSession();
						
			UserDetails userDetails = null;
			ModelAndView modelAndView = null;
			try {

				 userDetails = loginUSerController.getUserDetails();
				 
				 for (GrantedAuthority authority : userDetails.getAuthorities()) {
					    
					    // For example, you can access methods and properties of the GrantedAuthority interface
					    String authorityName = authority.getAuthority();
					}
				 
				 AdminDaoImpl.getDynamicMenuList("",request); 
				modelAndView = new ModelAndView("admin/admin_home");
				modelAndView.addObject("title", "Admin Home");

				return modelAndView; 
			} catch (Exception e) {
				logger.error("Exception in AdminController::admin_home::" + e.getMessage());
	            logger.error("Exception in AdminController::admin_home::" + e.getCause());
	            logger.error("Exception in AdminController::admin_home::" + e.getClass() + "::" + e.getLocalizedMessage());
				
				modelAndView = new ModelAndView("admin/admin_home");
				modelAndView.addObject("title", "Admin Home");
				modelAndView.addObject("msg", "Technical Exception. Please contact to Admin");
				
				return modelAndView;
			}
		}
	
	 
	    @RequestMapping(value = "/index", method = RequestMethod.GET)
	    public ModelAndView index(ModelMap model,Model m, HttpServletRequest request, HttpSession session) {
	            List<WebMenuBean> menuList = new ArrayList<WebMenuBean>();
	            ModelAndView modelAndView = new ModelAndView("index1");
	            try {
	            menuList = AdminDaoImpl.getDynamicMenuList("", request);
	            model.addAttribute("menulist", menuList);
	            session.setAttribute("menulist", menuList);	           	        
	            modelAndView.addObject("title", "Home");
	            }catch(Exception e) {
					logger.error("Exception in AdminController::index::" + e.getMessage());
		            logger.error("Exception in AdminController::index::" + e.getCause());
		            logger.error("Exception in AdminController::index::" + e.getClass() + "::" + e.getLocalizedMessage());
	            }
	        
	        return modelAndView;
	    }
		
	@RequestMapping(value = "/addmenucontent", method = RequestMethod.GET)
	public ModelAndView addmenucontent(Model m, HttpServletRequest request, HttpServletResponse response,
			HttpSession session,Model model,@RequestParam(name = "contentMsg", required = false) String contentMsg) {
		ModelAndView modelAndView = new ModelAndView("admin/addmemucontent2");
		try {
		modelAndView.addObject("title", "AddMenuContent");
		modelAndView.addObject("contentMsg", contentMsg);

		List<WebMenuBean> mainList = new ArrayList<WebMenuBean>();
		mainList = AdminDaoImpl.getMainMenuList("", request);
		 modelAndView.addObject("mainList", mainList);		 	
		 model.addAttribute(mainList);
		}catch(Exception e) {
			logger.error("Exception in AdminController::addmenucontent::" + e.getMessage());
            logger.error("Exception in AdminController::addmenucontent::" + e.getCause());
            logger.error("Exception in AdminController::addmenucontent::" + e.getClass() + "::" + e.getLocalizedMessage());
        }
		 
		return modelAndView;
	}
	
	

	@RequestMapping("/getLevelOneData")
	public void getLevelOneData(HttpServletRequest request, HttpServletResponse response,Model model) throws IOException {
		String mainlevel = request.getParameter("mainlevel");		
		String jsonArray = "";
		try {
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			HashMap<String, Object> regDatails = new HashMap<>();
			List<WebMenuBean> levelOneList = AdminDaoImpl.getLevelOneList("", mainlevel, request);
			if (levelOneList.size() > 0) {
				 model.addAttribute(levelOneList);
				regDatails.put("message", "success");
				regDatails.put("levelOneList", levelOneList);
			} else {
				regDatails.put("message", "fail");
			}
			jsonArray = gson.toJson(regDatails);
			response.getWriter().println(jsonArray);
		}catch(Exception e) {
			logger.error("Exception in AdminController::getLevelOneData::" + e.getMessage());
            logger.error("Exception in AdminController::getLevelOneData::" + e.getCause());
            logger.error("Exception in AdminController::getLevelOneData::" + e.getClass() + "::" + e.getLocalizedMessage());
        }
	}

	@RequestMapping("/getLevelTwoData")
	public void getLevelTwoData(HttpServletRequest request, HttpServletResponse response,Model model) throws IOException {
		String mainlevel = request.getParameter("mainlevel");
		String level1 = request.getParameter("level1");		
		String jsonArray = "";
		try {
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			HashMap<String, Object> regDatails = new HashMap<>();
			List<WebMenuBean> levelTwoList = AdminDaoImpl.getLevelTwoList("", mainlevel, level1, request);
			if (levelTwoList.size() > 0) {
				regDatails.put("message", "success");
				 model.addAttribute(levelTwoList);
				regDatails.put("levelTwoList", levelTwoList);
				regDatails.put("levelTwoSize", levelTwoList.size());
			} else {
				regDatails.put("message", "fail");
			}
			jsonArray = gson.toJson(regDatails);
			response.getWriter().println(jsonArray);
		}catch(Exception e) {
			logger.error("Exception in AdminController::getLevelTwoData::" + e.getMessage());
            logger.error("Exception in AdminController::getLevelTwoData::" + e.getCause());
            logger.error("Exception in AdminController::getLevelTwoData::" + e.getClass() + "::" + e.getLocalizedMessage());
        }
	}

	@RequestMapping(value = "/add_content", method = RequestMethod.POST)
	public ModelAndView add_content(@ModelAttribute(value = "WebMenuBean") @Validated WebMenuBean cf,
			BindingResult result, ModelMap mm,Model model, HttpSession session, HttpServletRequest request) {

		ModelAndView modelAndView = new ModelAndView("redirect:/addmenucontent");
		try {
			JsonArray json = new JsonArray();
			JsonArray json1 = new JsonArray();
			JsonArray json2 = new JsonArray();
			JsonArray json3 = new JsonArray();

			if (cf.getContent1() != null) {
				for (int s = 0; s < cf.getContent1().length; s++) {
					JsonObject x = new JsonObject();
					x.addProperty("content", cf.getContent1()[s]);
					json1.add(x);

				}
				json.add(json1);
			}
			if (cf.getContent2() != null) {
				for (int t = 0; t < cf.getContent2().length; t++) {
					JsonObject y = new JsonObject();
					y.addProperty("content", cf.getContent2()[t]);
					json2.add(y);
				}
				json.add(json2);
			}

			if (cf.getContent3() != null) {
				for (int u = 0; u < cf.getContent3().length; u++) {
					JsonObject f = new JsonObject();
					f.addProperty("content", cf.getContent3()[u]);
					json3.add(f);
				}
				json.add(json3);
			}

			cf.setJsonContent(json);
		
			FileInputStream fileInputStream = null;
			 byte[] data = null;
			String fileName = "";
			 String realPath = request.getServletContext().getRealPath("/");
			List<MultipartFile> uploadFiles = cf.getContent_image();
			byte[] image1 = Base64.encodeBase64(uploadFiles.get(0).getBytes());		
	        String resut1 = new String(image1);	        
			if (null != uploadFiles && uploadFiles.size() > 0) {
				for (MultipartFile multipartFile : uploadFiles) {
					fileName = multipartFile.getOriginalFilename();
					byte[] bytes = IOUtils.toByteArray(multipartFile.getInputStream());
					FileCopyUtils.copy(multipartFile.getBytes(), new File(realPath+""+multipartFile.getOriginalFilename()));
			        
					byte[] image = Base64.encodeBase64(multipartFile.getBytes());					
			        String resut = new String(image);					
					 if (fileName != null || !fileName.isEmpty()) {
	                        if (!"".equalsIgnoreCase(fileName)) {
	                            String mimeType = request.getServletContext().getMimeType(fileName);
	                            
	                            if (mimeType.equalsIgnoreCase("image/jpeg") || mimeType.equalsIgnoreCase("image/jpg") || mimeType.equalsIgnoreCase("audio/mpeg")) {
	                            } else {
	                                modelAndView.addObject("msg", "Wrong File Format");
	                                return modelAndView;
	                            }	                          
	                            if (multipartFile.getContentType().equalsIgnoreCase("image/jpeg") || multipartFile.getContentType().equalsIgnoreCase("image/jpg")  || multipartFile.getContentType().equalsIgnoreCase("audio/mpeg")) {
	                            } else {
	                                modelAndView.addObject("msg", "Wrong File Format");
	                                return modelAndView;
	                            }

	                            long fileSizeInBytes = multipartFile.getBytes().length;
	                            // Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
	                            long fileSizeInKB = fileSizeInBytes / 1024;
	                            // Convert the KB to MegaBytes (1 MB = 1024 KBytes)
	                            long fileSizeInMB = fileSizeInKB / 1024;	                           
	                            
	                            data = new byte[(int) multipartFile.getBytes().length];	                           
	                        }
	                    }
					 
					 
				}
			}

			WebMenuBean m_level = AdminDaoImpl.getMLevel("", cf.getMainlevel(), request);
			WebMenuBean l_one = AdminDaoImpl.getLOne("", cf.getMainlevel(), cf.getLevel1(), request);
			WebMenuBean l_two = AdminDaoImpl.getLTwo("", cf.getMainlevel(), cf.getLevel1(), cf.getLevel2(),
					request);
			cf.setMainlevel_desc(m_level.getMainlevel_desc());
			cf.setLevel1_desc(l_one.getLevel1_desc());
			cf.setLevel2_desc(l_two.getLevel2_desc());

			Integer k = AdminDaoImpl.getAddMenuContent("", cf, request);

			if(k>0)
				 modelAndView.addObject("contentMsg", "Menu Contenet Added Successfully");

				
			
			AdminDaoImpl.getMenuContent("", cf.getMainlevel(), cf.getLevel1(), cf.getLevel2(), request);
			return modelAndView;
		}catch(Exception e) {
			logger.error("Exception in AdminController::add_content::" + e.getMessage());
            logger.error("Exception in AdminController::add_content::" + e.getCause());
            logger.error("Exception in AdminController::add_content::" + e.getClass() + "::" + e.getLocalizedMessage());
        }
		return modelAndView;
	}
	
	@RequestMapping(value = "/academy_song", method = RequestMethod.GET)
	public ModelAndView academy_song(@ModelAttribute(value = "WebMenuBean") @Validated WebMenuBean cf,
			BindingResult result, ModelMap mm, HttpSession session, HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView("academy_song");
		try {
		List<WebMenuBean> menuList = new ArrayList<WebMenuBean>();
		menuList = AdminDaoImpl.getDynamicMenuList("", request);
		session.setAttribute("menuList", menuList);
		List<WebMenuBean> mContent = AdminDaoImpl.getMenuContent("", cf.getMainlevel(), cf.getLevel1(),cf.getLevel2(), request);			
		session.setAttribute("mContentList", mContent);
		}catch(Exception e) {
			logger.error("Exception in AdminController::academy_song::" + e.getMessage());
            logger.error("Exception in AdminController::academy_song::" + e.getCause());
            logger.error("Exception in AdminController::academy_song::" + e.getClass() + "::" + e.getLocalizedMessage());
        }
		return modelAndView;
	}

	@RequestMapping(value = "/addmenu", method = RequestMethod.GET)
	public ModelAndView addmenu(Model m, HttpServletRequest request, HttpServletResponse response,
			HttpSession session) {	
		ModelAndView modelAndView = new ModelAndView("admin/addmenu");		
		modelAndView.addObject("title", "AddMenu");
		return modelAndView;
	}
	
	

	@RequestMapping(value = "/add_menu", method = RequestMethod.POST)
	public ModelAndView createMenu(@ModelAttribute(value = "WebMenuBean") @Validated WebMenuBean cf,
			BindingResult result, Model m, HttpServletRequest request, HttpServletResponse response,
			HttpSession session) {
			ModelAndView modelAndView = new ModelAndView("admin/addmenu");
		modelAndView.addObject("title", "AddMenu");

		try {
//			if (cf.getLevel2_desc() != null || cf.getLevel2_desc() != "") {
//				cf.setMenu_url(cf.getLevel2_desc().replace(' ', '_').toLowerCase());
//			} else if (cf.getLevel1_desc() != null || cf.getLevel1_desc() != "") {
//				cf.setMenu_url(cf.getLevel1_desc().replace(' ', '_').toLowerCase());
//			} else if (cf.getMainlevel_desc() != null || cf.getMainlevel_desc() != "") {
//				cf.setMenu_url(cf.getMainlevel_desc().replace(' ', '_').toLowerCase());
//			}

			int k = AdminDaoImpl.createMenu("", cf, request);
//			int h = AdminDaoImpl.createMenu_Service("", cf, request);
			if(k>0) {
				m.addAttribute("msg","Menu  Added Successfully");
			}else {
				m.addAttribute("msg1","Menu Add Fail");
			}
			
			List<WebMenuBean> mainList = new ArrayList<WebMenuBean>();
			mainList = AdminDaoImpl.getMainMenuList("", request);		
			return modelAndView;
		}catch(Exception e) {
			logger.error("Exception in AdminController::createMenu::" + e.getMessage());
            logger.error("Exception in AdminController::createMenu::" + e.getCause());
            logger.error("Exception in AdminController::createMenu::" + e.getClass() + "::" + e.getLocalizedMessage());
        }
		return modelAndView;
	}

//	@RequestMapping("/searchMainLevel")
//	public void searchMainLevel(HttpServletRequest request, HttpServletResponse response) throws IOException {
//		String mainlevel = request.getParameter("mainlevel");
//		System.out.println("searchMainLevel===== " + mainlevel);
//		try {
//			WebMenuBean mlevel = null;
//			WebMenuBean cf = new WebMenuBean();
//			cf.setMainlevel_desc(mainlevel);
//			mlevel = AdminDaoImpl.searchMainLevel("", mainlevel, request);
//			List<WebMenuBean> d = AdminDaoImpl.getMainMenuList("", request);
//
//			System.out.println("searchMainLevel=ddd==== " + d.size());
//			if (mlevel != null) {
//				response.getWriter().println("success : " + mlevel.getMainlevel());
//			} else {
//				int size = d.size() + 1;
//				response.getWriter().println("failure : " + size);
//			}
//		} catch (Exception e) {
//			System.out.println("searchMainLevel--5-- " + e.getMessage());
//		}
//	}
//	@RequestMapping("/searchLevelOne")
//	public void searchLevelOne(HttpServletRequest request, HttpServletResponse response) throws IOException {
//		System.out.println("==================");
//		String level1_desc = request.getParameter("level1_desc");
//		String main_desc = request.getParameter("mainlevel_desc");
//		String mainlevel = request.getParameter("mainlevel");
//		System.out.println("searchLevelOne=====11 " + level1_desc + "  --  " + main_desc + "  -- " + mainlevel);
//		try {
//			WebMenuBean cf = new WebMenuBean();
//			
//			cf.setLevel1_desc(level1_desc);
//			cf.setMainlevel_desc(main_desc);
//			cf.setMainlevel(Integer.parseInt(mainlevel));
//
//			WebMenuBean levelOne = AdminDaoImpl.searchLevelOne("", cf, request);
//			List<WebMenuBean> d = AdminDaoImpl.getLevelOneSize("", cf, request);
//
//			if (levelOne != null) {
//				response.getWriter().println("success : " + levelOne.getLevel1());
//			} else {
//				int size = d.size() + 1;
//				response.getWriter().println("failure : " + size);
//			}
//		} catch (Exception e) {
//		}
//	}
//	@RequestMapping("/searchLevelTwo")
//	public void searchLevelTwo(HttpServletRequest request, HttpServletResponse response) throws IOException {
//		String level2_desc = request.getParameter("level2_desc");
//		String level1_desc = request.getParameter("level1_desc");
//		String level1 = request.getParameter("level1");
//		String main_desc = request.getParameter("mainlevel_desc");
//		String mainlevel = request.getParameter("mainlevel");
//		System.out.println("searchLevelTwo===== " + level2_desc);
//		try {
//			WebMenuBean levelTwo = null;
//			WebMenuBean cf = new WebMenuBean();
//			cf.setLevel2_desc(level2_desc);
//			cf.setLevel1_desc(level1_desc);
//			cf.setMainlevel_desc(main_desc);
//			cf.setMainlevel(Integer.parseInt(mainlevel));
//			cf.setLevel1(Integer.parseInt(level1));
//
//			levelTwo = AdminDaoImpl.searchLevelTwo("", cf, request);
//
//			List<WebMenuBean> d = AdminDaoImpl.getLevelTwoSize("", cf, request);
//			if (levelTwo != null) {
//				response.getWriter().println("success : " + levelTwo.getLevel2());
//			} else {
//				int size = d.size() + 1;
//				response.getWriter().println("failure : " + size);
//			}
//		} catch (Exception e) {
//		}
//	}


	@RequestMapping("/searchMainLevel")
	public ModelAndView searchMainLevel(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String mainlevel = request.getParameter("mainlevel");	
		String stringBuffer = "";
	    PrintWriter out = null;
		try {
			out = response.getWriter();
			WebMenuBean mlevel = null;
			mlevel = AdminDaoImpl.searchMainLevel("", mainlevel, request);
			List<WebMenuBean> d = AdminDaoImpl.getMainMenuList("", request);			
			if (mlevel != null) {
//				response.getWriter().println("success : " + mlevel.getMainlevel());
				stringBuffer = "success" + "##" + mlevel.getMainlevel();
			} else {
				int size = d.size() + 1;
//				response.getWriter().println("failure : " + size);
				stringBuffer = "failure" + "##" + size;
			}
			
	         response.setContentType("text/plain");
	         response.setHeader("Pragma", "no-cache");//for HTTP1.0
	         response.setHeader("cache-control", "no-cache");// for HTTP/1.1.
	         out.write(stringBuffer.toString());
		}catch(Exception e) {
			logger.error("Exception in AdminController::searchMainLevel::" + e.getMessage());
            logger.error("Exception in AdminController::searchMainLevel::" + e.getCause());
            logger.error("Exception in AdminController::searchMainLevel::" + e.getClass() + "::" + e.getLocalizedMessage());
        }
		return null;
	}

	
	@RequestMapping("/searchLevelOne")
	public ModelAndView searchLevelOne(HttpServletRequest request, HttpServletResponse response) throws IOException {		
		String level1_desc = request.getParameter("level1_desc");
		String main_desc = request.getParameter("mainlevel_desc");
		String mainlevel = request.getParameter("mainlevel");		
		String stringBuffer = "";
	    PrintWriter out = null;
	    System.out.println("searchLevelOne===");
		try {
			out = response.getWriter();
			WebMenuBean cf = new WebMenuBean();			
			cf.setLevel1_desc(level1_desc);
			cf.setMainlevel_desc(main_desc);
			cf.setMainlevel(Integer.parseInt(mainlevel));
			WebMenuBean levelOne = AdminDaoImpl.searchLevelOne("", cf, request);
			List<WebMenuBean> d = AdminDaoImpl.getLevelOneSize("", cf, request);
			if (levelOne != null) {
//				response.getWriter().println("success : " + levelOne.getLevel1());
				stringBuffer = "success" + "##" + levelOne.getLevel1();
			} else {
				WebMenuBean bean = (WebMenuBean) d.get(0);
				int size = bean.getLevel1() + 1;				
//				response.getWriter().println("failure : " + size);
				stringBuffer = "failure" + "##" + size;				
			}
	         response.setContentType("text/plain");
	         response.setHeader("Pragma", "no-cache");//for HTTP1.0
	         response.setHeader("cache-control", "no-cache");// for HTTP/1.1.
	         out.write(stringBuffer.toString());
		}catch(Exception e) {
			logger.error("Exception in AdminController::searchLevelOne::" + e.getMessage());
            logger.error("Exception in AdminController::searchLevelOne::" + e.getCause());
            logger.error("Exception in AdminController::searchLevelOne::" + e.getClass() + "::" + e.getLocalizedMessage());
        }
		return null;
	}

	@RequestMapping("/searchLevelTwo")
	public ModelAndView searchLevelTwo(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String level2_desc = request.getParameter("level2_desc");
		String level1_desc = request.getParameter("level1_desc");
		String level1 = request.getParameter("level1");
		String main_desc = request.getParameter("mainlevel_desc");
		String mainlevel = request.getParameter("mainlevel");		
		String stringBuffer = "";
	    PrintWriter out = null;
		try {
			out = response.getWriter();
			WebMenuBean levelTwo = null;
			WebMenuBean cf = new WebMenuBean();
			cf.setLevel2_desc(level2_desc);
			cf.setLevel1_desc(level1_desc);
			cf.setMainlevel_desc(main_desc);
			cf.setMainlevel(Integer.parseInt(mainlevel));
			cf.setLevel1(Integer.parseInt(level1));

			levelTwo = AdminDaoImpl.searchLevelTwo("", cf, request);
			List<WebMenuBean> d = AdminDaoImpl.getLevelTwoSize("", cf, request);
			if (levelTwo != null) {
//				response.getWriter().println("success : " + levelTwo.getLevel2());
				stringBuffer = "success" + "##" + levelTwo.getLevel2();
			} else {
				WebMenuBean bean = (WebMenuBean) d.get(0);
				int size = bean.getLevel2() + 1;
//				response.getWriter().println("failure : " + size);
				stringBuffer = "failure" + "##" + size;
			}
	         response.setContentType("text/plain");
	         response.setHeader("Pragma", "no-cache");//for HTTP1.0
	         response.setHeader("cache-control", "no-cache");// for HTTP/1.1.
	         out.write(stringBuffer.toString());
		}catch(Exception e) {
			logger.error("Exception in AdminController::searchLevelTwo::" + e.getMessage());
            logger.error("Exception in AdminController::searchLevelTwo::" + e.getCause());
            logger.error("Exception in AdminController::searchLevelTwo::" + e.getClass() + "::" + e.getLocalizedMessage());
        }
		return null;
	}
	
	@RequestMapping("/searchLevelThree")
	public ModelAndView searchLevelThree(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String level3_desc = request.getParameter("level3_desc");
		String level2 = request.getParameter("level2");
		String level2_desc = request.getParameter("level2_desc");
		String level1_desc = request.getParameter("level1_desc");
		String level1 = request.getParameter("level1");
		String main_desc = request.getParameter("mainlevel_desc");
		String mainlevel = request.getParameter("mainlevel");		
		String stringBuffer = "";
	    PrintWriter out = null;
		try {
			out = response.getWriter();
			WebMenuBean levelThree = null;
			WebMenuBean cf = new WebMenuBean();
			cf.setLevel3_desc(level3_desc);
			cf.setLevel2_desc(level2_desc);
			cf.setLevel1_desc(level1_desc);
			cf.setMainlevel_desc(main_desc);
			cf.setMainlevel(Integer.parseInt(mainlevel));
			cf.setLevel1(Integer.parseInt(level1));
			cf.setLevel2(Integer.parseInt(level2));

			levelThree = AdminDaoImpl.searchLevelThree("", cf, request);
			List<WebMenuBean> d = AdminDaoImpl.getLevelThreeSize("", cf, request);
			if (levelThree != null) {
//				response.getWriter().println("success : " + levelThree.getLevel3());
				stringBuffer = "success" + "##" + levelThree.getLevel3();
			} else {
				WebMenuBean bean = (WebMenuBean) d.get(0);
				int size = bean.getLevel3() + 1;
				response.getWriter().println("failure : " + size);
				stringBuffer = "failure" + "##" + size;
			}			
	         response.setContentType("text/plain");
	         response.setHeader("Pragma", "no-cache");//for HTTP1.0
	         response.setHeader("cache-control", "no-cache");// for HTTP/1.1.
	         out.write(stringBuffer.toString());
		}catch(Exception e) {
			logger.error("Exception in AdminController::searchLevelThree::" + e.getMessage());
            logger.error("Exception in AdminController::searchLevelThree::" + e.getCause());
            logger.error("Exception in AdminController::searchLevelThree::" + e.getClass() + "::" + e.getLocalizedMessage());
        }
		return null;
	}


	@RequestMapping(value = "/editmenu", method = RequestMethod.GET)
	public ModelAndView editmenu(Model m, HttpServletRequest request, HttpServletResponse response,
			HttpSession session) {
		ModelAndView modelAndView = new ModelAndView("admin/edit_menu");
		try {
		modelAndView.addObject("title", "Edit Menu");
		List<WebMenuBean> reportList = AdminDaoImpl.getEditMenu("", request);

		List<WebMenuBean> statusList = AdminDaoImpl.getStatusList();
//		session.setAttribute("reportList", reportList);
		//session.setAttribute("statusList", statusList);
		 modelAndView.addObject("reportList", reportList);
		 modelAndView.addObject("statusList", statusList);
		m.addAttribute(reportList);
		m.addAttribute(statusList);
		}catch(Exception e) {
			logger.error("Exception in AdminController::editmenu::" + e.getMessage());
            logger.error("Exception in AdminController::editmenu::" + e.getCause());
            logger.error("Exception in AdminController::editmenu::" + e.getClass() + "::" + e.getLocalizedMessage());
        }
		return modelAndView;
	}


	@RequestMapping(value = "/getFormFillValues", method = RequestMethod.POST)
	public ModelAndView getFormFillValues(Model m, HttpServletRequest request, HttpServletResponse response,
			HttpSession session) {
						
		String mainlevel = request.getParameter("mainlevel");
		String level2 = request.getParameter("level2");
		String level1 = request.getParameter("level1");
		String level3 = request.getParameter("level3");

		WebMenuBean cf = new WebMenuBean();
		try {
			cf.setMainlevel(Integer.parseInt(mainlevel));
			cf.setLevel1(Integer.parseInt(level1));
			cf.setLevel2(Integer.parseInt(level2));
			cf.setLevel3(Integer.parseInt(level3));
			
			WebMenuBean fillDetails = AdminDaoImpl.getFillDetails("", cf, request);			
			String jsonArray = "";

			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			HashMap<String, Object> regDatails = new HashMap<>();
			if (fillDetails != null) {
				
				regDatails.put("message", "success");
				regDatails.put("fillDetails", fillDetails);
				m.addAttribute(fillDetails);
				m.addAttribute(regDatails);
			} else {
				regDatails.put("message", "fail");
			}
			jsonArray = gson.toJson(regDatails);
			response.getWriter().println(jsonArray);
		}catch(Exception e) {
			logger.error("Exception in AdminController::getFormFillValues::" + e.getMessage());
            logger.error("Exception in AdminController::getFormFillValues::" + e.getCause());
            logger.error("Exception in AdminController::getFormFillValues::" + e.getClass() + "::" + e.getLocalizedMessage());
        }
		return null;
	}

	@RequestMapping(value = "/edit_menu", method = RequestMethod.POST)
	public ModelAndView editMenu(@ModelAttribute(value = "WebMenuBean") @Validated WebMenuBean cf, BindingResult result,
			Model m, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		ModelAndView modelAndView = new ModelAndView("admin/edit_menu");
		modelAndView.addObject("title", "AddMenu");
		try {

			int k = AdminDaoImpl.editMenu("", cf, request);
			// int h = AdminDaoImpl.createMenu_Service("", cf, request);
			List<WebMenuBean> reportList = AdminDaoImpl.getEditMenu("", request);
			List<WebMenuBean> statusList = AdminDaoImpl.getStatusList();
			 modelAndView.addObject("reportList", reportList);
			 modelAndView.addObject("statusList", statusList);
			 m.addAttribute(reportList);
			 m.addAttribute(statusList);
			/*
			 * session.setAttribute("reportList", reportList);
			 * session.setAttribute("statusList", statusList);
			 */
			return modelAndView;
		}catch(Exception e) {
			logger.error("Exception in AdminController::editMenu::" + e.getMessage());
            logger.error("Exception in AdminController::editMenu::" + e.getCause());
            logger.error("Exception in AdminController::editMenu::" + e.getClass() + "::" + e.getLocalizedMessage());
        }
		return modelAndView;
	}

	@RequestMapping(value = "/delete_menu", method = RequestMethod.POST)
	public ModelAndView deleteMenu(@ModelAttribute(value = "WebMenuBean") @Validated WebMenuBean cf,
			BindingResult result, Model m, HttpServletRequest request, HttpServletResponse response,
			HttpSession session) {
		
		ModelAndView modelAndView = new ModelAndView("admin/edit_menu");
		modelAndView.addObject("title", "AddMenu");
		try {
			int k = AdminDaoImpl.deleteMenu("", cf, request);		
			List<WebMenuBean> reportList = AdminDaoImpl.getEditMenu("", request);
			List<WebMenuBean> statusList = AdminDaoImpl.getStatusList();
			 modelAndView.addObject("reportList", reportList);
			 modelAndView.addObject("statusList", statusList);
			return modelAndView;
		}catch(Exception e) {
			logger.error("Exception in AdminController::deleteMenu::" + e.getMessage());
            logger.error("Exception in AdminController::deleteMenu::" + e.getCause());
            logger.error("Exception in AdminController::deleteMenu::" + e.getClass() + "::" + e.getLocalizedMessage());
        }
		return modelAndView;
	}

	@RequestMapping(value = "/missionstat", method = RequestMethod.GET)
	public ModelAndView missionstatement(@ModelAttribute(value = "WebMenuBean") @Validated WebMenuBean cf,
			BindingResult result, ModelMap mm, HttpSession session, HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView("missionstatement");
		try {
		// Integer x = AdminDaoImpl.getAddMenuContent("", cf, request);
		List<WebMenuBean> menuList = new ArrayList<WebMenuBean>();

		menuList = AdminDaoImpl.getDynamicMenuList("", request);
		session.setAttribute("menuList", menuList);

		List<WebMenuBean> mContent = AdminDaoImpl.getMenuContent("", cf.getMainlevel(), cf.getLevel1(),
				cf.getLevel2(), request);
		session.setAttribute("mContentList", mContent);
		}catch(Exception e) {
			logger.error("Exception in AdminController::missionstatement::" + e.getMessage());
            logger.error("Exception in AdminController::missionstatement::" + e.getCause());
            logger.error("Exception in AdminController::missionstatement::" + e.getClass() + "::" + e.getLocalizedMessage());
        }
		return modelAndView;
	}

	

	@RequestMapping(value = "/location_map", method = RequestMethod.GET)
	public ModelAndView location_map(@ModelAttribute(value = "WebMenuBean") @Validated WebMenuBean cf,
			BindingResult result, ModelMap mm, HttpSession session, HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView("location_map");
		try {
		List<WebMenuBean> menuList = new ArrayList<WebMenuBean>();

		menuList = AdminDaoImpl.getDynamicMenuList("", request);
		session.setAttribute("menuList", menuList);

		List<WebMenuBean> mContent = AdminDaoImpl.getMenuContent("", cf.getMainlevel(), cf.getLevel1(),
				cf.getLevel2(), request);
		session.setAttribute("mContentList", mContent);
		}catch(Exception e) {
			logger.error("Exception in AdminController::location_map::" + e.getMessage());
            logger.error("Exception in AdminController::location_map::" + e.getCause());
            logger.error("Exception in AdminController::location_map::" + e.getClass() + "::" + e.getLocalizedMessage());
        }
		return modelAndView;
	}

	@RequestMapping(value = "/history_academy", method = RequestMethod.GET)
	public ModelAndView history_academy(@ModelAttribute(value = "WebMenuBean") @Validated WebMenuBean cf,
			BindingResult result, ModelMap mm, HttpSession session, HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView("history_academy");
		try {
		List<WebMenuBean> menuList = new ArrayList<WebMenuBean>();
		menuList = AdminDaoImpl.getDynamicMenuList("", request);
		session.setAttribute("menuList", menuList);

		List<WebMenuBean> mContent = AdminDaoImpl.getMenuContent("", cf.getMainlevel(), cf.getLevel1(),
				cf.getLevel2(), request);
		session.setAttribute("mContentList", mContent);
		}catch(Exception e) {
			logger.error("Exception in AdminController::history_academy::" + e.getMessage());
            logger.error("Exception in AdminController::history_academy::" + e.getCause());
            logger.error("Exception in AdminController::history_academy::" + e.getClass() + "::" + e.getLocalizedMessage());
        }
		return modelAndView;
	}

	@RequestMapping(value = "/commemorative_stamp", method = RequestMethod.GET)
	public ModelAndView commemorative_stamp(@ModelAttribute(value = "WebMenuBean") @Validated WebMenuBean cf,
			BindingResult result, ModelMap mm, HttpSession session, HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView("commemorative_stamp");
		try {
		List<WebMenuBean> menuList = new ArrayList<WebMenuBean>();
		menuList = AdminDaoImpl.getDynamicMenuList("", request);
		session.setAttribute("menuList", menuList);

		List<WebMenuBean> mContent = AdminDaoImpl.getMenuContent("", cf.getMainlevel(), cf.getLevel1(),
				cf.getLevel2(), request);
		session.setAttribute("mContentList", mContent);
		}catch(Exception e) {
			logger.error("Exception in AdminController::commemorative_stamp::" + e.getMessage());
            logger.error("Exception in AdminController::commemorative_stamp::" + e.getCause());
            logger.error("Exception in AdminController::commemorative_stamp::" + e.getClass() + "::" + e.getLocalizedMessage());
        }
		return modelAndView;
	}

	@RequestMapping(value = "/organisation_chart", method = RequestMethod.GET)
	public ModelAndView organisation_chart(@ModelAttribute(value = "WebMenuBean") @Validated WebMenuBean cf,
			BindingResult result, ModelMap mm, HttpSession session, HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView("organisation_chart");
		try {
		List<WebMenuBean> menuList = new ArrayList<WebMenuBean>();
		menuList = AdminDaoImpl.getDynamicMenuList("", request);
		session.setAttribute("menuList", menuList);
		List<WebMenuBean> mContent = AdminDaoImpl.getMenuContent("", cf.getMainlevel(), cf.getLevel1(),
				cf.getLevel2(), request);
		session.setAttribute("mContentList", mContent);
		}catch(Exception e) {
			logger.error("Exception in AdminController::organisation_chart::" + e.getMessage());
            logger.error("Exception in AdminController::organisation_chart::" + e.getCause());
            logger.error("Exception in AdminController::organisation_chart::" + e.getClass() + "::" + e.getLocalizedMessage());
        }
		return modelAndView;
	}

	@RequestMapping(value = "/academy_guesthouse", method = RequestMethod.GET)
	public ModelAndView academy_guesthouse(@ModelAttribute(value = "WebMenuBean") @Validated WebMenuBean cf,
			BindingResult result, ModelMap mm, HttpSession session, HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView("academy_guesthouse");
		try {
		List<WebMenuBean> menuList = new ArrayList<WebMenuBean>();
		menuList = AdminDaoImpl.getDynamicMenuList("", request);
		session.setAttribute("menuList", menuList);
		List<WebMenuBean> mContent = AdminDaoImpl.getMenuContent("", cf.getMainlevel(), cf.getLevel1(),
				cf.getLevel2(), request);
		session.setAttribute("mContentList", mContent);
		}catch(Exception e) {
			logger.error("Exception in AdminController::academy_guesthouse::" + e.getMessage());
            logger.error("Exception in AdminController::academy_guesthouse::" + e.getCause());
            logger.error("Exception in AdminController::academy_guesthouse::" + e.getClass() + "::" + e.getLocalizedMessage());
        }
		return modelAndView;
	}

	@RequestMapping(value = "/academy_monuments", method = RequestMethod.GET)
	public ModelAndView academy_monuments(@ModelAttribute(value = "WebMenuBean") @Validated WebMenuBean cf,
			BindingResult result, ModelMap mm, HttpSession session, HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView("academy_monuments");
		try {
		List<WebMenuBean> menuList = new ArrayList<WebMenuBean>();
		menuList = AdminDaoImpl.getDynamicMenuList("", request);
		session.setAttribute("menuList", menuList);
		List<WebMenuBean> mContent = AdminDaoImpl.getMenuContent("", cf.getMainlevel(), cf.getLevel1(),
				cf.getLevel2(), request);
		session.setAttribute("mContentList", mContent);
		}catch(Exception e) {
			logger.error("Exception in AdminController::academy_monuments::" + e.getMessage());
            logger.error("Exception in AdminController::academy_monuments::" + e.getCause());
            logger.error("Exception in AdminController::academy_monuments::" + e.getClass() + "::" + e.getLocalizedMessage());
        }
		return modelAndView;
	}

	@RequestMapping(value = "/faculty", method = RequestMethod.GET)
	public ModelAndView faculty(@ModelAttribute(value = "WebMenuBean") @Validated WebMenuBean cf, BindingResult result,
			ModelMap mm, HttpSession session, HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView("faculty");
		try {
		List<WebMenuBean> menuList = new ArrayList<WebMenuBean>();
		menuList = AdminDaoImpl.getDynamicMenuList("", request);
		session.setAttribute("menuList", menuList);
		List<WebMenuBean> mContent = AdminDaoImpl.getMenuContent("", cf.getMainlevel(), cf.getLevel1(),
				cf.getLevel2(), request);
		session.setAttribute("mContentList", mContent);
		}catch(Exception e) {
			logger.error("Exception in AdminController::faculty::" + e.getMessage());
            logger.error("Exception in AdminController::faculty::" + e.getCause());
            logger.error("Exception in AdminController::faculty::" + e.getClass() + "::" + e.getLocalizedMessage());
        }
		return modelAndView;
	}

	@RequestMapping(value = "/mou_institutes", method = RequestMethod.GET)
	public ModelAndView mou_institutes(@ModelAttribute(value = "WebMenuBean") @Validated WebMenuBean cf,
			BindingResult result, ModelMap mm, HttpSession session, HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView("mou_institutes");
		try {
		List<WebMenuBean> menuList = new ArrayList<WebMenuBean>();
		menuList = AdminDaoImpl.getDynamicMenuList("", request);
		session.setAttribute("menuList", menuList);
		List<WebMenuBean> mContent = AdminDaoImpl.getMenuContent("", cf.getMainlevel(), cf.getLevel1(),
				cf.getLevel2(), request);
		session.setAttribute("mContentList", mContent);
		}catch(Exception e) {
			logger.error("Exception in AdminController::mou_institutes::" + e.getMessage());
            logger.error("Exception in AdminController::mou_institutes::" + e.getCause());
            logger.error("Exception in AdminController::mou_institutes::" + e.getClass() + "::" + e.getLocalizedMessage());
        }
		return modelAndView;
	}

	@RequestMapping(value = "/npa_movie", method = RequestMethod.GET)
	public ModelAndView npa_movie(@ModelAttribute(value = "WebMenuBean") @Validated WebMenuBean cf,
			BindingResult result, ModelMap mm, HttpSession session, HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView("npa_movie");
		try {
		List<WebMenuBean> menuList = new ArrayList<WebMenuBean>();
		menuList = AdminDaoImpl.getDynamicMenuList("", request);
		session.setAttribute("menuList", menuList);
		List<WebMenuBean> mContent = AdminDaoImpl.getMenuContent("", cf.getMainlevel(), cf.getLevel1(),
				cf.getLevel2(), request);
		session.setAttribute("mContentList", mContent);
		}catch(Exception e) {
			logger.error("Exception in AdminController::npa_movie::" + e.getMessage());
            logger.error("Exception in AdminController::npa_movie::" + e.getCause());
            logger.error("Exception in AdminController::npa_movie::" + e.getClass() + "::" + e.getLocalizedMessage());
        }
		return modelAndView;
	}

	@RequestMapping(value = "/short_film_training", method = RequestMethod.GET)
	public ModelAndView short_film_training(@ModelAttribute(value = "WebMenuBean") @Validated WebMenuBean cf,
			BindingResult result, ModelMap mm, HttpSession session, HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView("short_film_training");
		try {
		List<WebMenuBean> menuList = new ArrayList<WebMenuBean>();
		menuList = AdminDaoImpl.getDynamicMenuList("", request);
		session.setAttribute("menuList", menuList);
		List<WebMenuBean> mContent = AdminDaoImpl.getMenuContent("", cf.getMainlevel(), cf.getLevel1(),
				cf.getLevel2(), request);		
		session.setAttribute("mContentList", mContent);
		}catch(Exception e) {
			logger.error("Exception in AdminController::short_film_training::" + e.getMessage());
            logger.error("Exception in AdminController::short_film_training::" + e.getCause());
            logger.error("Exception in AdminController::short_film_training::" + e.getClass() + "::" + e.getLocalizedMessage());
        }
		return modelAndView;
	}

	@RequestMapping(value = "/academy_infra", method = RequestMethod.GET)
	public ModelAndView academyInfra(@ModelAttribute(value = "WebMenuBean") @Validated WebMenuBean cf,
			BindingResult result, ModelMap mm, HttpSession session, HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView("academy_infrastucture");
		try {
		List<WebMenuBean> menuList = new ArrayList<WebMenuBean>();
		menuList = AdminDaoImpl.getDynamicMenuList("", request);		
		session.setAttribute("menuList", menuList);
		List<WebMenuBean> mContent = AdminDaoImpl.getMenuContent("", cf.getMainlevel(), cf.getLevel1(),
				cf.getLevel2(), request);
		session.setAttribute("mContentList", mContent);
		}catch(Exception e) {
			logger.error("Exception in AdminController::academyInfra::" + e.getMessage());
            logger.error("Exception in AdminController::academyInfra::" + e.getCause());
            logger.error("Exception in AdminController::academyInfra::" + e.getClass() + "::" + e.getLocalizedMessage());
        }
		return modelAndView;
	}
	
	@RequestMapping(value = "/getmenuServices", method = RequestMethod.GET)
	public ModelAndView getmenuServices(Model model, HttpServletRequest request, HttpServletResponse response,HttpSession session) {
		try {
		List<WebMenuBean> menuList = new ArrayList<WebMenuBean>();
		menuList = AdminDaoImpl.getDynamicMenuList2();
		session.setAttribute("menuList", menuList);
		model.addAttribute("roleList", roleRepository.findAll());
		}catch(Exception e) {
			logger.error("Exception in AdminController::getmenuServices::" + e.getMessage());
            logger.error("Exception in AdminController::getmenuServices::" + e.getCause());
            logger.error("Exception in AdminController::getmenuServices::" + e.getClass() + "::" + e.getLocalizedMessage());
        }
		return new ModelAndView("admin/menu_services");		
	}	
	
	   @RequestMapping(value = "/addMenuServices", method = RequestMethod.POST)
	    public ModelAndView addMenuServices(@ModelAttribute("form") WebMenuBean bean, Model model, HttpServletRequest request, HttpSession session) {
	        ModelAndView mv = null;
	    try{
	    	String checkedSerives=request.getParameter("checkedSerives");
	        String[] checkedSerivesArray=checkedSerives.split("~");	    
	         if(checkedSerives!=null && !checkedSerives.equals("")) 
	        {
	        int i=AdminDaoImpl.insertMenuServices(bean,checkedSerivesArray); 	        
	        if(i>0){
	        	request.setAttribute("msg1", "Menu Links Successfully Inserted");
//	            request.setAttribute("msg", "User  Successfully Created with Menu Links");	                       
	        }
	        else{
	           request.setAttribute("msg1", "Menu Links Not Enabled Successfully");
	        }	               
	         }else {
	        	 request.setAttribute("msg1", "Menu Links Not Enabled Successfully");
	         }     
	    }catch(Exception e) {
			logger.error("Exception in AdminController::addMenuServices::" + e.getMessage());
            logger.error("Exception in AdminController::addMenuServices::" + e.getCause());
            logger.error("Exception in AdminController::addMenuServices::" + e.getClass() + "::" + e.getLocalizedMessage());
        }
	     return new ModelAndView("admin/menu_services");
	    }
	   
	   @RequestMapping(value = "/editMenuServices", method = RequestMethod.GET)
		public ModelAndView editMenuServices(Model model, HttpServletRequest request, HttpServletResponse response,HttpSession session) {			
			model.addAttribute("roleList", roleRepository.findAll());
			return new ModelAndView("admin/editmenu_services");		
		}
	   
	   @RequestMapping(value = "/getDynamicMenuLinks", method = RequestMethod.POST)
		public ModelAndView getDynamicMenuLinks(@ModelAttribute("form") WebMenuBean bean,Model model, HttpServletRequest request, HttpServletResponse response,HttpSession session) {
		try {
			List<WebMenuBean> menuList = new ArrayList<WebMenuBean>();
			menuList = AdminDaoImpl.getDynamicMenuList2();
			session.setAttribute("menuList", menuList);
			String services = AdminDaoImpl.getServices(bean);
			request.setAttribute("services",services);
			model.addAttribute("role_id", bean.getRole());
			model.addAttribute("role_name" , request.getParameter("role_name"));
			model.addAttribute("roleList", roleRepository.findAll());
//			model.addAttribute("services", services);
		}catch(Exception e) {
			logger.error("Exception in AdminController::getDynamicMenuLinks::" + e.getMessage());
            logger.error("Exception in AdminController::getDynamicMenuLinks::" + e.getCause());
            logger.error("Exception in AdminController::getDynamicMenuLinks::" + e.getClass() + "::" + e.getLocalizedMessage());
        }
			return new ModelAndView("admin/editmenu_links");		
		}
	   
	   @RequestMapping(value = "/updateMenuServices", method = RequestMethod.POST)
	    public ModelAndView updateMenuServices(@ModelAttribute("form") WebMenuBean bean, Model model, HttpServletRequest request, HttpSession session) {
	        ModelAndView mv = null;
	    try{	    	
	    	String checkedSerives=request.getParameter("checkedSerives");
	        String[] checkedSerivesArray=checkedSerives.split("~");	    
	         if(checkedSerives!=null && !checkedSerives.equals("")) 
	        {
	        int i=AdminDaoImpl.updateMenuServices(bean,checkedSerivesArray); 	        
	        if(i>0){
	        	request.setAttribute("msg1", "Menu Links Successfully Updated");	                       
	        }
	        else{
	           request.setAttribute("msg1", "Menu Links Not Updated Successfully");
	        }	               
	         }else {
	        	 request.setAttribute("msg1", "Menu Links Not Updated Successfully");
	         }
	         model.addAttribute("roleList", roleRepository.findAll());
	    }catch(Exception e) {
			logger.error("Exception in AdminController::updateMenuServices::" + e.getMessage());
            logger.error("Exception in AdminController::updateMenuServices::" + e.getCause());
            logger.error("Exception in AdminController::updateMenuServices::" + e.getClass() + "::" + e.getLocalizedMessage());
        }
	     return new ModelAndView("admin/editmenu_services");
	    }

}
