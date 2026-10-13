package com.nic.svpnpa.service;

import org.springframework.ui.Model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nic.svpnpa.util.AESEncryption;
import com.nic.svpnpa.util.DBUtils;
import org.json.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import org.springframework.web.multipart.MultipartFile;
import javax.sql.DataSource;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;
import java.util.UUID;
import java.util.stream.Collectors;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.imageio.ImageIO;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Service
public class SVPNPAService {

	private static final Logger logger = LogManager.getLogger(SVPNPAService.class);

	@Value("${galleryPath}")
	String galleryPath;

	@Value("${galleryAbsolutePath}")
	String galleryAbsolutePath;

	private String[][] tableData;
	private int page_id;
	private String object_title;
	private StringBuilder viewObject;
	private StringBuilder output[];
	private String admin;
	@Autowired
	public DBUtils dbutils;
	@Autowired
	private DataSource dataSource;
	public String lang = "E";

	public String role;
	
	@Autowired
	private JdbcTemplate JdbcWrite;

	@Autowired
	private JdbcTemplate JdbcRead;

	public String LoginValidation(String username, String password, HttpServletResponse response) {
		System.out.println("LoginValidation====");
		if (username.equals("NPA") && password.equals("Test@123")) {
			Cookie cookieLogin = new Cookie("status", "Logout");
			cookieLogin.setSecure(true);
			cookieLogin.setPath("/svpnpacms");
			response.addCookie(cookieLogin);
			return "redirect:/view";
		}
		return "redirect:/sign-in";
	}

	public String saveTemplate(Map<String, Object> typeMap) {
		try {
			int count = dbutils.insertOrUpdate(
					"INSERT INTO templates(template_id,template_name,template_data) VALUES (nextval('template_seq'),?,?);",
					new Object[] { typeMap.get("template_name"), typeMap.get("template_data") });
			if (count <= 0)
				throw new Exception("Failed to insert");
		} catch (Exception e) {
			logger.error("Exception in SVPNPAService::saveTemplate::" + e.getMessage());
			logger.error("Exception in SVPNPAService::saveTemplate::" + e.getCause());
			logger.error("Exception in SVPNPAService::saveTemplate::" + e.getClass() + "::" + e.getLocalizedMessage());
		}
		return "Success";
	}

	public String template(Model model) {
		try {
			ArrayList<HashMap<String, Object>> templates = dbutils
					.getListMap("SELECT template_data,template_name FROM templates order by template_id", null);
			for (Map<String, Object> template : templates) {
				for (Map.Entry<String, Object> entry : template.entrySet()) {
					String key = entry.getKey();
					String value = (String) entry.getValue();
					value = value.replaceAll("@object", "");
					template.put(key, value);
				}
			}
			model.addAttribute("templates", templates);
		} catch (Exception e) {
			logger.error("Exception in SVPNPAService::template::" + e.getMessage());
			logger.error("Exception in SVPNPAService::template::" + e.getCause());
			logger.error("Exception in SVPNPAService::template::" + e.getClass() + "::" + e.getLocalizedMessage());
		} finally {
		}
		return "template";
	}

	public String view(Model model) {
		try {
			String condition = "";
			String published = "";
			if (role.equals("2")) {				
				condition = " where verified=false and archive=false ";
//				published = " ,published as published ";
				published = " ,verified as published ";

			} else if (role.equals("3")) {				
				condition = " where published=false and archive=false";				
//				published = " ,verified as published ";
				published = " ,published as published";

			} else {				
				published = " ,published as published ";
//---------------------01-11-2024-------------------------------
				condition = " where verified=true and published=true and archive=false";
//---------------------------------------------------------------------				

			}
			ArrayList<HashMap<String, Object>> pages = dbutils
					.getListMap("SELECT page_id,page_name,description,keywords" + published + " FROM pages_names "
							+ condition + " order by page_id", null);
			for (Map<String, Object> page : pages) {
				page.put("page_data", viewPageContents((int) page.get("page_id")));
			}					
			model.addAttribute("optionsNew", pages);
		} catch (Exception e) {
			logger.error("Exception in SVPNPAService::view::" + e.getMessage());
			logger.error("Exception in SVPNPAService::view::" + e.getCause());
			logger.error("Exception in SVPNPAService::view::" + e.getClass() + "::" + e.getLocalizedMessage());
		}
		return "view";
	}
	
	public String View_NewPages(Model model) {
		try {			
			ArrayList<HashMap<String, Object>> pages = dbutils
					.getListMap("SELECT page_id,page_name,description,keywords,verified,published FROM pages_names where (verified=false or published=false) and archive=false order by page_id", null);
			for (Map<String, Object> page : pages) {
				page.put("page_data", viewPageContents((int) page.get("page_id")));
			}					
			model.addAttribute("optionsNew", pages);
		} catch (Exception e) {
			logger.error("Exception in SVPNPAService::View_NewPages::" + e.getMessage());
			logger.error("Exception in SVPNPAService::View_NewPages::" + e.getCause());
			logger.error("Exception in SVPNPAService::View_NewPages::" + e.getClass() + "::" + e.getLocalizedMessage());
		}
		return "view";
	}
	
	public String View_ArchivedPages(Model model) {
		try {			
			ArrayList<HashMap<String, Object>> pages = dbutils
					.getListMap("SELECT page_id,page_name,description,keywords,verified,published FROM pages_names where  archive=true order by page_id", null);
			for (Map<String, Object> page : pages) {
				page.put("page_data", viewPageContents((int) page.get("page_id")));
			}					
			model.addAttribute("optionsNew", pages);
		} catch (Exception e) {
			logger.error("Exception in SVPNPAService::View_ArchivedPages::" + e.getMessage());
			logger.error("Exception in SVPNPAService::View_ArchivedPages::" + e.getCause());
			logger.error("Exception in SVPNPAService::View_ArchivedPages::" + e.getClass() + "::" + e.getLocalizedMessage());
		}
		return "view";
	}


	public String pageIndex(Model model) {
		try {
			ArrayList<HashMap<String, Object>> templates = dbutils
					.getListMap("SELECT template_id,template_name FROM templates", null);
			model.addAttribute("options", templates);
		} catch (Exception e) {
			logger.error("Exception in SVPNPAService::pageIndex::" + e.getMessage());
			logger.error("Exception in SVPNPAService::pageIndex::" + e.getCause());
			logger.error("Exception in SVPNPAService::pageIndex::" + e.getClass() + "::" + e.getLocalizedMessage());			
		}
		return "PageDesigner";
	}

	public String page(Model model, String page_name, String template_id, String page_id) {
		String innerHTML = null;
		try {
			ArrayList<HashMap<String, Object>> templates = dbutils
					.getListMap("SELECT template_id,template_name FROM templates", null);
			model.addAttribute("options", templates);
			if (page_id == null) {
				String template_data = (String) dbutils.getDBValue(
						"SELECT template_data FROM templates where template_id=?",
						new Object[] { Integer.parseInt(template_id) });
				model.addAttribute("template_id", template_id);
				model.addAttribute("page_name", page_name);
				model.addAttribute("page_id", page_id);

				innerHTML = parsePage(template_data);
			} else {
				ArrayList<HashMap<String, Object>> pages_names = dbutils.getListMap(
						"SELECT * FROM pages_names where page_id=?", new Object[] { Integer.parseInt(page_id) });
				innerHTML = viewPageContentsEditable(Integer.parseInt(page_id));
				model.addAttribute("page_name", pages_names.get(0).get("page_name"));
				model.addAttribute("template_id", pages_names.get(0).get("template_id"));
				model.addAttribute("page_id", page_id);
				model.addAttribute("description", pages_names.get(0).get("description"));
				model.addAttribute("keywords", pages_names.get(0).get("keywords"));
				String json = (String) dbutils.getDBValue(
						"SELECT string_agg(object_id::varchar,',' order by order_no) json FROM pages where page_id=?",
						new Object[] { Integer.parseInt(page_id) });
				model.addAttribute("existingJson", json);
			}
			model.addAttribute("templateBody", innerHTML);
		} catch (Exception e) {
			logger.error("Exception in SVPNPAService::page::" + e.getMessage());
			logger.error("Exception in SVPNPAService::page::" + e.getCause());
			logger.error("Exception in SVPNPAService::page::" + e.getClass() + "::" + e.getLocalizedMessage());			
		} finally {
		}
		return "PageDesigner";
	}

	private String parsePage(String data) {
		String modifiedString = data.replace("@object",
				"<img src=\"static/add_obj_page.png\" data-bs-target=\"#selectionPopup\" onclick=\"getid(this);\" data-bs-toggle=\"modal\" width=\"100px\" height=\"70px\" style=\"cursor:pointer;\" >");
		return modifiedString;
	}

	public String design(Model model, int rowCount, int colCount, String object_title, String description,
			String help_text, String alternate_text, String keywords) {
		try {
			Map propertiesJson = new HashMap<String, String>();
			propertiesJson.put("title", object_title);
			propertiesJson.put("description", description);
			propertiesJson.put("help_text", help_text);
			propertiesJson.put("alt_text", alternate_text);
			propertiesJson.put("keywords", keywords);
			String newJson = new Gson().toJson(propertiesJson);
			tableData = new String[rowCount][colCount];
			model.addAttribute("newJson", newJson);
			model.addAttribute("tableData", tableData);
			model.addAttribute("object_title", object_title);
		} catch (Exception e) {
			logger.error("Exception in SVPNPAService::design::" + e.getMessage());
			logger.error("Exception in SVPNPAService::design::" + e.getCause());
			logger.error("Exception in SVPNPAService::design::" + e.getClass() + "::" + e.getLocalizedMessage());			
		}
		return "index";
	}

	public String designList(Model model, int rowCount) {
		try {
			String[] listData = new String[rowCount];
			model.addAttribute("link_items", listData);
			model.addAttribute("list_items", listData);
			model.addAttribute("exp_items", listData);
		} catch (Exception e) {
			logger.error("Exception in SVPNPAService::designList::" + e.getMessage());
			logger.error("Exception in SVPNPAService::designList::" + e.getCause());
			logger.error("Exception in SVPNPAService::designList::" + e.getClass() + "::" + e.getLocalizedMessage());			
		} finally {
		}
		return "ListDesigner";
	}

	public String designGalleryObjects(Model model, String object_title, String type, String object_id) {
		try {
			String[] userArray = null;
			if (object_id != null && !object_id.equals("")) {
				String data = (String) dbutils.getDBValue("SELECT data FROM objects where object_id=?",new Object[] { object_id });
				Gson gson = new Gson();
				data = data.replaceAll("mvnr", "'");
				userArray = gson.fromJson(data.substring(1, data.length() - 1), String[].class);
			}
			model.addAttribute("data", userArray);
			model.addAttribute("galleryGroups", groups(type));
			model.addAttribute("object_title", object_title);
			model.addAttribute("type", type);
		} catch (Exception e) {
			logger.error("Exception in SVPNPAService::designGalleryObjects::" + e.getMessage());
			logger.error("Exception in SVPNPAService::designGalleryObjects::" + e.getCause());
			logger.error("Exception in SVPNPAService::designGalleryObjects::" + e.getClass() + "::"+ e.getLocalizedMessage());
			
		} finally {
		}
		return "GalleryDesigner";
	}

	public String designTable(int object_id, Model model, String newRow, String newCol) {
		try {
			ArrayList<HashMap<String, Object>> object = dbutils.getListMap(
			 "select *,object_meta->>'keywords' as keywords,object_meta->>'alt_text' as alt_text,object_meta->>'description' as description,object_meta->>'title' as title,object_meta->>'help_text' as help_text from objects where  object_id=? order by lang",
			  new Object[] { object_id });
			String data = String.valueOf(object.get(0).get("data"));
			Gson gson = new Gson();
			model.addAttribute("marquee", String.valueOf(object.get(0).get("properties")));
			model.addAttribute("styles", String.valueOf(object.get(0).get("styles")));
			model.addAttribute("classes", String.valueOf(object.get(0).get("classes")));
			this.page_id = object_id;	
			String template_name = String.valueOf(object.get(0).get("object_category"));	
			
			if (template_name.equals("1")) {
				String[][] userArray = gson.fromJson(data.substring(1, data.length() - 1), String[][].class);
				this.tableData = userArray;
				String[][] tempArray;
				if (newRow != null) {
					tempArray = new String[Integer.parseInt(newRow)][Integer.parseInt(newCol)];
//					System.out.println("tableData==="+tableData.length);
					for (int i = 0; i < tableData.length; i++) {
						for (int j = 0; j < tableData[i].length; j++) {
							try {
							tableData[i][j] = tableData[i][j].replaceAll("mvnr", "'");
							tempArray[i][j] = new String(
									URLDecoder.decode(tableData[i][j], StandardCharsets.UTF_8.name()));
							}catch(Exception e) {
								//tempArray[i][j] = "";	
							}
						}
					}
				} else {
//					System.out.println("tableData==="+tableData.length);
					tempArray = new String[tableData.length][tableData[0].length];
					for (int i = 0; i < tableData.length; i++) {
						for (int j = 0; j < tableData[i].length; j++) {
							try {
							tableData[i][j] = tableData[i][j].replaceAll("mvnr", "'");
							tempArray[i][j] = new String(
									URLDecoder.decode(tableData[i][j], StandardCharsets.UTF_8.name()));
							}catch(Exception e) {
								//tempArray[i][j] = "";	
							}
						}
					}
				}
				model.addAttribute("tableData", tempArray);
				model.addAttribute("object_id", object_id);
				model.addAttribute("object_title", String.valueOf(object.get(0).get("object_title")));
				model.addAttribute("keywords", String.valueOf(object.get(0).get("keywords")));
				model.addAttribute("alternate_text", String.valueOf(object.get(0).get("alt_text")));
				model.addAttribute("description", String.valueOf(object.get(0).get("description")));
				model.addAttribute("help_text", String.valueOf(object.get(0).get("help_text")));
// Hindi Code
				
				if (object.get(1) != null) {
					data = String.valueOf(object.get(1).get("data"));					
					this.page_id = object_id;
					template_name = String.valueOf(object.get(1).get("object_category"));
					try {
					userArray = gson.fromJson(data.substring(1, data.length() - 1), String[][].class);
					this.tableData = userArray;
					}catch(Exception e) {
						userArray = null;
					}
					
					if (newRow != null) {
						tempArray = new String[Integer.parseInt(newRow)][Integer.parseInt(newCol)];
						for (int i = 0; i < tableData.length; i++) {
							for (int j = 0; j < tableData[i].length; j++) {
								try {
								tableData[i][j] = tableData[i][j].replaceAll("mvnr", "'");
								tempArray[i][j] = new String(
										URLDecoder.decode(tableData[i][j], StandardCharsets.UTF_8.name()));
							}catch(Exception e) {
								//tempArray[i][j] = "";	
							}
							}
						}
					} else {
						tempArray = new String[tableData.length][tableData[0].length];
						for (int i = 0; i < tableData.length; i++)
							for (int j = 0; j < tableData[i].length; j++) {
								try {
								tableData[i][j] = tableData[i][j].replaceAll("mvnr", "'");
								tempArray[i][j] = new String(
										URLDecoder.decode(tableData[i][j], StandardCharsets.UTF_8.name()));
								}catch(Exception e) {
									//tempArray[i][j]="";
								}
							}
						}
					model.addAttribute("tableData_hi", tempArray);
					model.addAttribute("object_id", object_id);
					model.addAttribute("object_title_hi", String.valueOf(object.get(1).get("object_title")));
					model.addAttribute("keywords_hi", String.valueOf(object.get(1).get("keywords")));
					model.addAttribute("alternate_text_hi", String.valueOf(object.get(1).get("alt_text")));
					model.addAttribute("description_hi", String.valueOf(object.get(1).get("description")));
					model.addAttribute("help_text_hi", String.valueOf(object.get(1).get("help_text")));
					model.addAttribute("hideDivs", "show");
// Hindi ends
				}
				logger.info("index page=====1=============");
				return "index";
			} else if (template_name.equals("3")) {
				data = data.replaceAll("mvnr", "'");
				String[] userArray = gson.fromJson(data.substring(1, data.length() - 1), String[].class);
				String[] tempArray, expArray = null;
				String links = String.valueOf(object.get(0).get("hyperlinks"));
				String exp = String.valueOf(object.get(0).get("expiration"));
				String[] userArrayLinks = null;
				if (links != null)
					userArrayLinks = gson.fromJson(links.substring(1, links.length() - 1), String[].class);
				if (exp != null)
					expArray = gson.fromJson(exp.substring(1, exp.length() - 1), String[].class);				
				String[] tempArrayLinks = new String[userArray.length];
//				String[] tempExpLinks = new String[expArray.length];
				String[] tempExpLinks = new String[userArray.length];
				if (newRow != null) {
					tempArray = new String[Integer.parseInt(newRow)];
					tempArrayLinks = new String[Integer.parseInt(newRow)];
					tempExpLinks = new String[Integer.parseInt(newRow)];
					for (int i = 0; i < userArray.length; i++) {
						try {
						tempArray[i] = new String(URLDecoder.decode(userArray[i], StandardCharsets.UTF_8.name()));
						}catch(Exception e) {
						tempArray[i] = "";	
						}
						try {
						if (links != null) {
							tempArrayLinks[i] = new String(
									URLDecoder.decode(userArrayLinks[i], StandardCharsets.UTF_8.name()));
								}
						}catch(Exception e) {
							tempArrayLinks[i] = null;
						}
						try {
						if (exp != null) {
							tempExpLinks[i] = new String(URLDecoder.decode(expArray[i], StandardCharsets.UTF_8.name()));
						}
						}catch(Exception e) {
							tempExpLinks[i] = null;
						}
					}
				} else {
					tempArray = new String[userArray.length];
					if (links != null)
						tempArrayLinks = new String[userArrayLinks.length];
					for (int i = 0; i < userArray.length; i++) {
						try {
						tempArray[i] = new String(URLDecoder.decode(userArray[i], StandardCharsets.UTF_8.name()));
						}catch(Exception e) {							
							tempArray[i] = "";	
						}
						try {
						if (links != null) {
							tempArrayLinks[i] = new String(
									URLDecoder.decode(userArrayLinks[i], StandardCharsets.UTF_8.name()));							
						}
					}catch(Exception e) {
						tempArrayLinks[i] = null;
					}
						try {
						if (exp != null && expArray[i] != null) {								
							tempExpLinks[i] = new String(URLDecoder.decode(expArray[i], StandardCharsets.UTF_8.name()));							
						}
						}catch(Exception e) {
							tempExpLinks[i] = null;
						}
					}
				}
				model.addAttribute("link_items", tempArrayLinks);
				model.addAttribute("list_items", tempArray);
				model.addAttribute("exp_items", tempExpLinks);
				model.addAttribute("object_id", object_id);
				model.addAttribute("type", template_name);
				model.addAttribute("object_title", (String) object.get(0).get("object_title"));
				model.addAttribute("keywords", (String) object.get(0).get("keywords"));
				model.addAttribute("alternate_text", (String) object.get(0).get("alt_text"));
				model.addAttribute("description", (String) object.get(0).get("description"));
				model.addAttribute("help_text", (String) object.get(0).get("help_text"));
				if (object.get(1) != null) {
					data = String.valueOf(object.get(1).get("data"));
					data = data.replaceAll("mvnr", "'");
					this.page_id = object_id;
					try {
					userArray = gson.fromJson(data.substring(1, data.length() - 1), String[].class);
					}catch(Exception e) {
					}
					System.out.println("userArray===="+userArray.toString());

					String[] tempArray_hi = new String[userArray.length];
					System.out.println("tempArray_hi===="+tempArray_hi.toString());

					for (int i = 0; i < userArray.length; i++) {
						if (userArray[i] != null) {
							try {
							tempArray_hi[i] = new String(URLDecoder.decode(userArray[i], StandardCharsets.UTF_8.name()));
							System.out.println("tempArray_hi==="+i+"===="+tempArray_hi[i]);
							}catch(Exception e) {
								//tempArray_hi[i] = "";	
							}
						}else {
							tempArray_hi[i] = "";
					    }
					}
					model.addAttribute("list_items_hi", tempArray_hi);
					model.addAttribute("object_title_hi", (String) object.get(1).get("object_title"));
					model.addAttribute("keywords_hi", (String) object.get(1).get("keywords"));
					model.addAttribute("alternate_text_hi", (String) object.get(1).get("alt_text"));
					model.addAttribute("description_hi", (String) object.get(1).get("description"));
					model.addAttribute("help_text_hi", (String) object.get(1).get("help_text"));
				}
				logger.info("ListDesigner page=====3=============");
				return "ListDesigner";
			} else if (template_name.equals("6")) {
				data = data.replaceAll("mvnr", "'");
				String encd_message = data;				
				data = new String(URLDecoder.decode(encd_message.substring(2, encd_message.length() - 2),
						StandardCharsets.UTF_8.name()));
				model.addAttribute("data", data);
				model.addAttribute("object_id", object_id);
				model.addAttribute("object_title", (String) object.get(0).get("object_title"));
				if (object.get(1) != null) {
					String data_hi = String.valueOf(object.get(1).get("data"));
					data_hi = data_hi.replace("mvnr", "'");
					model.addAttribute("data_hi", new String(URLDecoder
							.decode(data_hi.substring(2, data_hi.length() - 2), StandardCharsets.UTF_8.name())));
					model.addAttribute("object_title_hi", (String) object.get(1).get("object_title"));
				}
				logger.info("paragraph page========6==========");
				return "paragraph";
			}else if (template_name.equals("10")) {
//				System.out.println("1==B=="+data.toString());
				data = data.replaceAll("mvnr", "'");
//				System.out.println("1==A=="+data.toString());
				String encd_message = data;
				try {
				data = new String(URLDecoder.decode(encd_message.substring(2, encd_message.length() - 2),
						StandardCharsets.UTF_8.name()));
				}catch(Exception e) {					
					data = "";	
				}
				model.addAttribute("data", data);
				model.addAttribute("object_id", object_id);
				model.addAttribute("object_title", (String) object.get(0).get("object_title"));
				if (object.get(1) != null) {
					String data_hi = "";
					try {
					 data_hi = String.valueOf(object.get(1).get("data"));
//					 System.out.println("2==B=="+data_hi.toString());
					 data_hi = data_hi.replaceAll("mvnr", "'");
//					 System.out.println("2==A=="+data_hi.toString());
					}catch(Exception e) {
						
						data_hi = "";	
					}
					model.addAttribute("data_hi", new String(URLDecoder
							.decode(data_hi.substring(2, data_hi.length() - 2), StandardCharsets.UTF_8.name())));
					model.addAttribute("object_title_hi", (String) object.get(1).get("object_title"));
				}
				logger.info("embed page====Custom HTML====10==========");
				return "embed";
			}else if (template_name.equals("2") || template_name.equals("5")) {
				String userArray[] = null;
				try {
					data = data.replaceAll("mvnr", "'");
				userArray = gson.fromJson(data.substring(1, data.length() - 1), String[].class);
				}catch(Exception e) {
										
				}
				
				String property[] = String.valueOf(object.get(0).get("properties")).toString().split(",");
				model.addAttribute("data", userArray);
				model.addAttribute("object_id", object_id);
				model.addAttribute("galleryGroups", groups(template_name));
				model.addAttribute("type", template_name);
				model.addAttribute("object_title", (String) object.get(0).get("object_title"));
				if(template_name.equals("5")) {
				model.addAttribute("random", property[0]);
				}
				logger.info("GalleryDesigner page========2 or 5====Image--Carousel======");
				return "GalleryDesigner";
			} else if (template_name.equals("7") || template_name.equals("8") ) {
				String[] userArray = null ;
				try {
					data = data.replaceAll("mvnr", "'");
				userArray = gson.fromJson(data.substring(1, data.length() - 1), String[].class);
				}catch(Exception e) {
						
				}
				model.addAttribute("data", userArray);
				model.addAttribute("object_id", object_id);
				model.addAttribute("galleryGroups", groups(template_name));
				model.addAttribute("type", template_name);
				model.addAttribute("object_title", (String) object.get(0).get("object_title"));
				logger.info("GalleryDesigner page=====7=====Vedio========");
				return "GalleryDesigner";
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception in SVPNPAService::designTable::" + e.getMessage());
	        logger.error("Exception in SVPNPAService::designTable::" + e.getCause());
	        logger.error("Exception in SVPNPAService::designTable::" + e.getClass() + "::" + e.getLocalizedMessage());	        
		}
		return "home";
	}

	public String updateItem(Map<String, Object> typeMap) {
		String temp = null;
		try {
			int updatedCount = dbutils.insertOrUpdate("update pages set object_id=? where page_id=? and order_no=?",
					new Object[] { typeMap.get("value"), typeMap.get("page_id"),
							((String) typeMap.get("id")).substring(1) });
			ArrayList<HashMap<String, Object>> objects = dbutils.getListMap(
					"select object_category,data,properties,object_title,hyperlinks,expiration,styles,classes from objects where object_id=?",
					new Object[] { Integer.parseInt((String) typeMap.get("value")) });
			Gson gson = new Gson();
			int i = 0;
			StringBuilder marquee = null;
			for (Map object : objects) {
				String type = String.valueOf(object.get("object_category"));
				if (type.equals("1")) {
					String data = String.valueOf(object.get("data"));
					data = data.replace("mvnr", "'");
					String[][] userArray = gson.fromJson(data.substring(1, data.length() - 1), String[][].class);
					output[i] = parseTableData(String.valueOf(object.get("object_title")), userArray,
							String.valueOf(object.get("styles")), String.valueOf(object.get("classes")));
				} else if (type.equals("3")) {
					String data = String.valueOf(object.get("data"));
					data = data.replace("mvnr", "'");
					String[] userArray = gson.fromJson(data.substring(1, data.length() - 1), String[].class);
					String linksData = String.valueOf(object.get("hyperlinks"));
					String[] linksArray = null;
					if (linksData != null) {
						linksArray = gson.fromJson(linksData.substring(1, linksData.length() - 1), String[].class);
					}
					String expData = String.valueOf(object.get("expiration"));
					String[] expArray = null;
					if (linksData != null) {
						expArray = gson.fromJson(expData.substring(1, expData.length() - 1), String[].class);
					}
					String property[] = (String.valueOf(object.get("properties")).toString().split(","));
					if (property.length > 1) {
						if (property[1].equals("marquee"))
							marquee = new StringBuilder("<h5 class='text-danger fw-bold text-center'>"
									+ (String) object.get("object_title")
									+ "</h5>                  <div class=\"card-body border border-2 border-success rounded-5 important_links\">\r\n"
									+ "<marquee scrollamount=3 onmouseover='this.stop();' onmouseout='this.start();' direction='"
									+ property[2] + "'>"
									+ parseListData(userArray, linksArray, expArray, property[0],
											String.valueOf(object.get("styles")),
											String.valueOf(object.get("classes")) + "</marquee>\r\n" + "</div>"));
						else
							marquee = new StringBuilder(new StringBuffer(
									"<h5  class='text-danger fw-bold text-center'>")
									.append((String) object.get("object_title"))
									.append("</h5>                  <div class=\"card-body border border-2 border-success rounded-5 important_links\">\r\n"
											+ ""))
									.append(parseListData(userArray, linksArray, expArray, property[0],
											(String) object.get("styles"), (String) object.get("classes"))
											.append("\r\n" + "                  </div>"));
					} else
						marquee = new StringBuilder(
								"<div class=\"card-body border border-2 border-success rounded-5 important_links\">")
								.append(parseListData(userArray, linksArray, expArray, property[0],
										String.valueOf(object.get("styles")), String.valueOf(object.get("classes"))))
								.append("\r\n" + "                  </div>");
					output[i] = marquee;
				} else if (type.equals("6") || type.equals("10")) {
					String data;
					String encd_message = String.valueOf(object.get("data"));
					encd_message = encd_message.replace("mvnr", "'");
					data = new String(URLDecoder.decode(encd_message.substring(3, encd_message.length() - 3),
							StandardCharsets.UTF_8.name()));
					output[i] = new StringBuilder(data);
				} else if (type.equals("5")) {
					String data = String.valueOf(object.get("data"));
					data = data.replace("mvnr", "'");
					String[] userArray = gson.fromJson(data.substring(1, data.length() - 1), String[].class);
					String property[] = String.valueOf(object.get("properties")).toString().split(",");
					output[i] = parseImageCarousel(userArray, i, property[0]);
				} else if (type.equals("2")) {
					String data = String.valueOf(object.get("data"));
					data = data.replace("mvnr", "'");
					String[] userArray = gson.fromJson(data.substring(1, data.length() - 1), String[].class);
					output[i] = parseImages(userArray, i);
				} else if (type.equals("7")) {
					String data = String.valueOf(object.get("data"));
					data = data.replace("mvnr", "'");
					String[] userArray = gson.fromJson(data.substring(1, data.length() - 1), String[].class);
					output[i] = parseVideos(userArray, i);
				} else if (type.equals("8")) {
					String data = String.valueOf(object.get("data"));
					data = data.replace("mvnr", "'");
					String[] userArray = gson.fromJson(data.substring(1, data.length() - 1), String[].class);
					output[i] = parseAudios(userArray, i);
				} else if (type.equals("9")) {
					String data = String.valueOf(object.get("data"));
					data = data.replace("mvnr", "'");
					String[] userArray = gson.fromJson(data.substring(1, data.length() - 1), String[].class);
					output[i] = parseDocs(userArray, i);
				}
				i++;
				viewObject = output[i];
				temp = viewObject.toString();
			}
		} catch (Exception e) {
			logger.error("Exception in SVPNPAService::updateItem::" + e.getMessage());
			logger.error("Exception in SVPNPAService::updateItem::" + e.getCause());
			logger.error("Exception in SVPNPAService::updateItem::" + e.getClass() + "::" + e.getLocalizedMessage());			
		}
		return temp;
	}

	public ArrayList<HashMap<String, Object>> objectsList() {
		ArrayList<HashMap<String, Object>> selectData = null;
		try {
			selectData = dbutils.getListMap(
					"SELECT object_id,concat((select category_name from object_categories where category_id=object_category), '- ',object_title) as name FROM objects order by concat",
					null);
		} catch (Exception e) {
			logger.error("Exception in SVPNPAService::objectsList::" + e.getMessage());
			logger.error("Exception in SVPNPAService::objectsList::" + e.getCause());
			logger.error("Exception in SVPNPAService::objectsList::" + e.getClass() + "::" + e.getLocalizedMessage());
			}
		return selectData;
	}

	public String viewObject(int page_id, String admin, Model model) {
		this.viewObject = new StringBuilder(viewPageContents(page_id));
		model.addAttribute("viewObject", viewObject);
		model.addAttribute("page_id", page_id);
		return "view";
	}

	public String viewPageContents(int page_id) {
		try {
			Long countLong = (Long) dbutils.getDBValue("SELECT coalesce(count(page_id),0) FROM pages where page_id=?",
					new Object[] { (page_id) });
			int count = Integer.parseInt(countLong.toString());
			String template_data = (String) dbutils.getDBValue(
					"SELECT template_data FROM templates where template_id=(select template_id from pages_names where page_id=?)",
					new Object[] { (page_id) });
			StringBuilder viewObject = new StringBuilder(template_data);
			output = new StringBuilder[count];
			ArrayList<HashMap<String, Object>> objects = dbutils.getListMap(
					"select object_category,data,o.properties,o.object_title,o.hyperlinks,o.expiration,o.styles,o.classes from pages p,objects o where p.object_id=o.object_id and p.page_id=? and o.lang=? order by order_no",
					new Object[] { page_id, lang });
			Gson gson = new Gson();
			int i = 0;
			StringBuilder marquee = null;
			for (Map map : objects) {
				String type = String.valueOf((Integer) map.get("object_category"));
				if (type.equals("1")) {
					String data = String.valueOf(map.get("data"));
					data = data.replace("mvnr", "'");
					String[][] userArray = gson.fromJson(data.substring(1, data.length() - 1), String[][].class);
					output[i] = parseTableData(String.valueOf(map.get("object_title")), userArray,
							(String) map.get("styles"), (String) map.get("classes"));
				} else if (type.equals("3")) {
					String data = String.valueOf(map.get("data"));
					data = data.replace("mvnr", "'");
					String[] userArray = gson.fromJson(data.substring(1, data.length() - 1), String[].class);
					String linksData = String.valueOf(map.get("hyperlinks"));
					String[] linksArray = null;
					if (linksData != null) {
						linksArray = gson.fromJson(linksData.substring(1, linksData.length() - 1), String[].class);
					}
					String expData = String.valueOf(map.get("expiration"));
					String[] expArray = null;
					if (expData != null) {
						expArray = gson.fromJson(expData.substring(1, expData.length() - 1), String[].class);
					}
					String property[] = ((String) map.get("properties")).toString().split(",");
					if (property.length > 1) {
						if (property[1].equals("marquee"))
							marquee = new StringBuilder("<h5  class='text-danger fw-bold text-center'>"
									+ (String) map.get("object_title")
									+ "</h5><marquee scrollamount=3 onmouseover='this.stop();' onmouseout='this.start();' direction='"
									+ property[2] + "'>" + parseListData(userArray, linksArray, expArray, property[0],
											(String) map.get("styles"), (String) map.get("classes"))
									+ "</marquee>");
						else if (property[1].equals("marquee"))
							marquee = new StringBuilder("<h5  class='text-danger fw-bold text-center'>"
									+ (String) map.get("object_title")
									+ "</h5><div class=\"card-body border border-2 border-success rounded-5 important_links\"><marquee scrollamount=3 onmouseover='this.stop();' onmouseout='this.start();' direction='"
									+ property[2] + "'>"
									+ parseListData(userArray, linksArray, expArray, property[0],
											(String) map.get("styles"), (String) map.get("classes"))
									+ "</marquee>").append("" + "                  </div>");
						else
							marquee = new StringBuilder(new StringBuffer(
									"<h5  class='text-danger fw-bold text-center'>")
									.append((String) map.get("object_title"))
									.append("</h5><div class=\"card-body border border-2 border-success rounded-5 important_links\">"))
									.append(parseListData(userArray, linksArray, expArray, property[0],
											(String) map.get("styles"), (String) map.get("classes")))
									.append("\r\n" + "                  </div>");
						;
					} else
						marquee = new StringBuilder(
								"<div class=\"card-body border border-2 border-success rounded-5 important_links\">")
								.append(parseListData(userArray, linksArray, expArray, property[0],
										String.valueOf(map.get("styles")), String.valueOf(map.get("classes"))))
								.append("\r\n" + "                  </div>");
					output[i] = marquee;
				} else if (type.equals("6") || type.equals("10")) {
					String data;
					String encd_message = String.valueOf(map.get("data"));
					encd_message = encd_message.replace("mvnr", "'");
					data = new String(URLDecoder.decode(encd_message.substring(2, encd_message.length() - 2),
							StandardCharsets.UTF_8.name()));
					output[i] = new StringBuilder(data);
				} else if (type.equals("5")) {
					String data = String.valueOf(map.get("data"));
					data = data.replace("mvnr", "'");
					String[] userArray = gson.fromJson(data.substring(1, data.length() - 1), String[].class);
					String property[] = String.valueOf(map.get("properties")).toString().split(",");
					output[i] = parseImageCarousel(userArray, i, property[0]);
				} else if (type.equals("2")) {
					String data = String.valueOf(map.get("data"));
					data = data.replace("mvnr", "'");
					String[] userArray = gson.fromJson(data.substring(1, data.length() - 1), String[].class);
					output[i] = parseImages(userArray, i);
				} else if (type.equals("7")) {
					String data = String.valueOf(map.get("data"));
					data = data.replace("mvnr", "'");
					String[] userArray = gson.fromJson(data.substring(1, data.length() - 1), String[].class);
					output[i] = parseVideos(userArray, i);
				} else if (type.equals("8")) {
					String data = String.valueOf(map.get("data"));
					data = data.replace("mvnr", "'");
					String[] userArray = gson.fromJson(data.substring(1, data.length() - 1), String[].class);
					output[i] = parseAudios(userArray, i);
				} else if (type.equals("9")) {
					String data = String.valueOf(map.get("data"));
					data = data.replace("mvnr", "'");
					String[] userArray = gson.fromJson(data.substring(1, data.length() - 1), String[].class);
					output[i] = parseDocs(userArray, i);
				}
				int index = viewObject.indexOf("@object");
				viewObject = new StringBuilder(viewObject.substring(0, index) + output[i]
						+ viewObject.substring(index + 7, viewObject.length()));
				i++;
			}
			String tempObject = viewObject.toString();
			tempObject = tempObject.replaceAll("@object", "");
			viewObject = new StringBuilder(tempObject);
			this.viewObject = viewObject;
			this.page_id = page_id;
		} catch (Exception e) {
			logger.error("Exception in SVPNPAService::viewPageContents::" + e.getMessage());
			logger.error("Exception in SVPNPAService::viewPageContents::" + e.getCause());
			logger.error("Exception in SVPNPAService::viewPageContents::" + e.getClass() + "::" + e.getLocalizedMessage());
			}
		return viewObject.toString();
	}

	public StringBuilder object_retrieval(Map map) throws UnsupportedEncodingException {
		String type = String.valueOf((Integer) map.get("object_category"));
		Gson gson = new Gson();
		StringBuilder marquee = null;
		try {
			if (type.equals("1")) {
				String data = String.valueOf(map.get("data"));
				data = data.replace("mvnr", "'");
				String[][] userArray = gson.fromJson(data.substring(1, data.length() - 1), String[][].class);
				return parseTableData(String.valueOf(map.get("object_title")), userArray, (String) map.get("styles"),
						(String) map.get("classes"));
			} else if (type.equals("3")) {
				String data = String.valueOf(map.get("data"));
				data = data.replace("mvnr", "'");
				String[] userArray = gson.fromJson(data.substring(1, data.length() - 1), String[].class);
				String linksData = String.valueOf(map.get("hyperlinks"));
				String[] linksArray = null;
				if (linksData != null) {
					linksArray = gson.fromJson(linksData.substring(1, linksData.length() - 1), String[].class);
				}
				String expData = String.valueOf(map.get("expiration"));
				String[] expArray = null;
				if (expData != null) {
					expArray = gson.fromJson(expData.substring(1, expData.length() - 1), String[].class);
				}
				String property[] = ((String) map.get("properties")).toString().split(",");
				if (property.length > 1) {
					if (property[1].equals("marquee"))
						marquee = new StringBuilder("<h5 class='text-danger fw-bold text-center'>"
								+ (String) map.get("object_title")
								+ "</h5><marquee scrollamount=3 onmouseover='this.stop();' onmouseout='this.start();' direction='"
								+ property[2] + "'>" + parseListData(userArray, linksArray, expArray, property[0],
										(String) map.get("styles"), (String) map.get("classes"))
								+ "</marquee>");
					else if (property[1].equals("marquee"))
						marquee = new StringBuilder("<h5 class='text-danger fw-bold text-center'>"
								+ (String) map.get("object_title")
								+ "</h5><marquee scrollamount=3 onmouseover='this.stop();' onmouseout='this.start();' direction='"
								+ property[2] + "'>" + parseListData(userArray, linksArray, expArray, property[0],
										(String) map.get("styles"), (String) map.get("classes"))
								+ "</marquee>");
					else
						marquee = new StringBuilder(new StringBuffer("<h5 class='text-danger fw-bold text-center'>")
								.append((String) map.get("object_title")).append("</h5>"))
								.append(parseListData(userArray, linksArray, expArray, property[0],
										(String) map.get("styles"), (String) map.get("classes")));
				} else
					marquee = new StringBuilder(
							"<div class=\"card-body border border-2 border-success rounded-5 important_links\">")
							.append(parseListData(userArray, linksArray, expArray, property[0],
									String.valueOf(map.get("styles")), String.valueOf(map.get("classes"))))
							.append("\r\n" + "                  </div>");
				return marquee;
			} else if (type.equals("6") || type.equals("10")) {
				String data;
				String encd_message = String.valueOf(map.get("data"));
				encd_message = encd_message.replace("mvnr", "'");
				data = new String(URLDecoder.decode(encd_message.substring(2, encd_message.length() - 2),
						StandardCharsets.UTF_8.name()));
				return new StringBuilder(data);
			} else if (type.equals("5")) {
				String data = String.valueOf(map.get("data"));
				data = data.replace("mvnr", "'");
				String[] userArray = gson.fromJson(data.substring(1, data.length() - 1), String[].class);
				String property[] = String.valueOf(map.get("properties")).toString().split(",");
				return parseImageCarousel(userArray, 0, property[0]);
			} else if (type.equals("2")) {
				String data = String.valueOf(map.get("data"));
				data = data.replace("mvnr", "'");
				String[] userArray = gson.fromJson(data.substring(1, data.length() - 1), String[].class);
				return parseImages(userArray, 0);
			} else if (type.equals("7")) {
				String data = String.valueOf(map.get("data"));
				data = data.replace("mvnr", "'");
				String[] userArray = gson.fromJson(data.substring(1, data.length() - 1), String[].class);
				return parseVideos(userArray, 0);
			} else if (type.equals("8")) {
				String data = String.valueOf(map.get("data"));
				data = data.replace("mvnr", "'");
				String[] userArray = gson.fromJson(data.substring(1, data.length() - 1), String[].class);
				return parseAudios(userArray, 0);
			} else if (type.equals("9")) {
				String data = String.valueOf(map.get("data"));
				data = data.replace("mvnr", "'");
				String[] userArray = gson.fromJson(data.substring(1, data.length() - 1), String[].class);
				return parseDocs(userArray, 0);
			}
		} catch (Exception e) {
			logger.error("Exception in SVPNPAService::object_retrieval::" + e.getMessage());
			logger.error("Exception in SVPNPAService::object_retrieval::" + e.getCause());
			logger.error("Exception in SVPNPAService::object_retrieval::" + e.getClass() + "::" + e.getLocalizedMessage());
			}
		return null;
	}

	public String viewPageContentsEditable(int page_id) {
		try {
			int count = Integer.parseInt(String
					.valueOf((long) dbutils.getDBValue("SELECT coalesce(count(page_id),0) FROM pages where page_id=?",
							new Object[] { (page_id) })));
			String template_data = (String) dbutils.getDBValue(
					"SELECT template_data FROM templates where template_id=(select template_id from pages_names where page_id=?)",
					new Object[] { (page_id) });
			StringBuilder viewObject = new StringBuilder(template_data);
			output = new StringBuilder[count];
			ArrayList<HashMap<String, Object>> objects = dbutils.getListMap(
					"select object_category,data,o.properties,o.object_title,o.hyperlinks,o.expiration,o.styles,o.classes from pages p,objects o where p.object_id=o.object_id and p.page_id=? and o.lang=? order by order_no",
					new Object[] { page_id, lang });
			Gson gson = new Gson();
			int i = 0;
			StringBuilder marquee = null;
			for (Map map : objects) {
				String type = String.valueOf(map.get("object_category"));
				if (type.equals("1")) {
					String data = String.valueOf(map.get("data"));
					data = data.replace("mvnr", "'");
					String[][] userArray = gson.fromJson(data.substring(1, data.length() - 1), String[][].class);
					output[i] = parseTableData(String.valueOf(map.get("object_title")), userArray,
							String.valueOf(map.get("styles")), String.valueOf(map.get("classes")));
				} else if (type.equals("3")) {
					String data = String.valueOf(map.get("data"));
					data = data.replace("mvnr", "'");
					String[] userArray = gson.fromJson(data.substring(1, data.length() - 1), String[].class);
					String linksData = String.valueOf(map.get("hyperlinks"));
					String[] linksArray = null;
					if (linksData != null) {
						linksArray = gson.fromJson(linksData.substring(1, linksData.length() - 1), String[].class);
					}
					String expData = String.valueOf(map.get("expiration"));
					String[] expArray = null;
					if (expData != null) {
						expArray = gson.fromJson(expData.substring(1, expData.length() - 1), String[].class);
					}
					String property[] = (String.valueOf(map.get("properties"))).toString().split(",");
					if (property.length > 1) {
						if (property[1].equals("marquee"))
							marquee = new StringBuilder("<h5  class='text-danger fw-bold text-center'>"
									+ String.valueOf(map.get("object_title"))
									+ "</h5><marquee scrollamount=3 onmouseover='this.stop();' onmouseout='this.start();' direction='"
									+ property[2] + "'>"
									+ parseListData(userArray, linksArray, expArray, property[0],
											String.valueOf(map.get("styles")), String.valueOf(map.get("classes")))
									+ "</marquee>");
						else if (property[1].equals("marquee"))
							marquee = new StringBuilder("<h5  class='text-danger fw-bold text-center'>"
									+ String.valueOf(map.get("object_title"))
									+ "</h5><marquee scrollamount=3 onmouseover='this.stop();' onmouseout='this.start();' direction='"
									+ property[2] + "'>"
									+ parseListData(userArray, linksArray, expArray, property[0],
											String.valueOf(map.get("styles")), String.valueOf(map.get("classes")))
									+ "</marquee>");
						else
							marquee = new StringBuilder(new StringBuffer("<h5 class='text-danger fw-bold text-center'>")
									.append(String.valueOf(map.get("object_title"))).append("</h5>"))
									.append(parseListData(userArray, linksArray, expArray, property[0],
											String.valueOf(map.get("styles")), String.valueOf(map.get("classes"))));
					} else
						marquee = new StringBuilder(
								"<div class=\"card-body border border-2 border-success rounded-5 important_links\">")
								.append(parseListData(userArray, linksArray, expArray, property[0],
										String.valueOf(map.get("styles")), String.valueOf(map.get("classes"))))
								.append("\r\n" + "                  </div>");
					output[i] = marquee;
				} else if (type.equals("6") || type.equals("10")) {
					String data;
					String encd_message = String.valueOf(map.get("data"));
					encd_message = encd_message.replace("mvnr", "'");
					data = new String(URLDecoder.decode(encd_message.substring(2, encd_message.length() - 2),
							StandardCharsets.UTF_8.name()));
					output[i] = new StringBuilder(data);
				} else if (type.equals("5")) {
					String data = String.valueOf(map.get("data"));
					data = data.replace("mvnr", "'");
					String[] userArray = gson.fromJson(data.substring(1, data.length() - 1), String[].class);
					String property[] = String.valueOf(map.get("properties")).toString().split(",");
					output[i] = parseImageCarousel(userArray, i, property[0]);
				} else if (type.equals("2")) {
					String data = String.valueOf(map.get("data"));
					data = data.replace("mvnr", "'");
					String[] userArray = gson.fromJson(data.substring(1, data.length() - 1), String[].class);
					output[i] = parseImages(userArray, i);
				} else if (type.equals("7")) {
					String data = String.valueOf(map.get("data"));
					data = data.replace("mvnr", "'");
					String[] userArray = gson.fromJson(data.substring(1, data.length() - 1), String[].class);
					output[i] = parseVideos(userArray, i);
				} else if (type.equals("8")) {
					String data = String.valueOf(map.get("data"));
					data = data.replace("mvnr", "'");
					String[] userArray = gson.fromJson(data.substring(1, data.length() - 1), String[].class);
					output[i] = parseAudios(userArray, i);
				} else if (type.equals("9")) {
					String data = String.valueOf(map.get("data"));
					data = data.replace("mvnr", "'");
					String[] userArray = gson.fromJson(data.substring(1, data.length() - 1), String[].class);
					output[i] = parseDocs(userArray, i);
				}

				int index = viewObject.indexOf("@object");
				viewObject = new StringBuilder(viewObject.substring(0, index)
						+ "<div data-bs-target=\"#selectionPopup\" onclick=\"getid(this);\" data-bs-toggle=\"modal\">"
						+ output[i] + "</div>" + viewObject.substring(index + 7, viewObject.length()));
				i++;
			}
			String tempObject = viewObject.toString();
			tempObject = tempObject.replaceAll("@object",
					"<img src=\"add_obj_page.png\" data-bs-target=\"#selectionPopup\" onclick=\"getid(this);\" data-bs-toggle=\"modal\" width=\"100px\" height=\"70px\" style=\"cursor:pointer;\" >");
			viewObject = new StringBuilder(tempObject);
			this.viewObject = viewObject;
			this.page_id = page_id;
		} catch (Exception e) {
			logger.error("Exception in SVPNPAService::viewPageContentsEditable::" + e.getMessage());
			logger.error("Exception in SVPNPAService::viewPageContentsEditable::" + e.getCause());
			logger.error("Exception in SVPNPAService::viewPageContentsEditable::" + e.getClass() + "::"+ e.getLocalizedMessage());
			}
		return viewObject.toString();
	}

	public List groups(String object) {
		List items = null;
		try {
			if (object.equals("5"))
				object = "2";
			items = dbutils.getListMap("SELECT distinct group_id FROM gallery where media_type=?",
					new Object[] { Integer.parseInt(object) });
		} catch (Exception e) {
			logger.error("Exception in SVPNPAService::groups::" + e.getMessage());
			logger.error("Exception in SVPNPAService::groups::" + e.getCause());
			logger.error("Exception in SVPNPAService::groups::" + e.getClass() + "::" + e.getLocalizedMessage());
			}
		return items;
	}

	public ArrayList<HashMap<String, Object>> getGalleryItems(Map<String, Object> typeMap) {
		ArrayList<HashMap<String, Object>> items = null;
		try {
			String type = "";
			if (typeMap.get("type").equals("5"))
				type = "2";
			else
				type = (String) typeMap.get("type");
			items = dbutils.getListMap(
					"select file_name,thumbnail_filename,properties ->> 'description' as description,original_filename,properties,seq_id from gallery where group_id=? and media_type=?",
					new Object[] { typeMap.get("group"), Integer.parseInt(type) });
		} catch (Exception e) {
			logger.error("Exception in SVPNPAService::getGalleryItems::" + e.getMessage());
			logger.error("Exception in SVPNPAService::getGalleryItems::" + e.getCause());
			logger.error("Exception in SVPNPAService::getGalleryItems::" + e.getClass() + "::" + e.getLocalizedMessage());
			}
		return items;
	}

	public ArrayList<HashMap<String, Object>> getObjectTypeWise(Map<String, Object> typeMap) {
		ArrayList<HashMap<String, Object>> items = null;
		try {

			String verified = "";
			if (typeMap.get("source").equals("home"))
				verified = "";
			else if (typeMap.get("source").equals("design"))
				verified = "and verified=true";
//System.out.println("====typeMap==="+typeMap.get("type")+"====lang==="+lang);
			Long count = (Long) dbutils.getDBValue(
					"SELECT coalesce(count(object_id),0) FROM objects where object_category::varchar=? and lang=?;",
					new Object[] { (String) typeMap.get("type"), lang });
			items = dbutils.getListMap(
					"SELECT *,object_meta ->> 'description' as description,object_meta ->> 'title' as title,object_meta ->> 'help_text' as help_text FROM objects where object_category::varchar=? and lang=? order by object_id desc"
							+ verified,
					new Object[] { typeMap.get("type"), lang });
			StringBuilder[] output = new StringBuilder[Integer.parseInt(count.toString())];
			Gson gson = new Gson();
			int i = 0;
			StringBuilder marquee = null;
			String type = "";
			for (Map<String, Object> item : items) {
				try {
//System.out.println("------------------------------------------------------"+(String) typeMap.get("type"));					
				type = (String) typeMap.get("type");				
				}catch(Exception e) {
					e.printStackTrace();
				}
				if (type.equals("1")) {				
					String data = String.valueOf(item.get("data"));
					data = data.replaceAll("mvnr", "'");
//System.out.println("------------------------------------------------------"+data);	
					String[][] userArray = gson.fromJson(data.substring(1, data.length() - 1), String[][].class);
					output[i] = parseTableData(String.valueOf(item.get("title")), userArray,
							(String) item.get("styles"), (String) item.get("classes"));
				} else if (type.equals("3")) {
					String data = String.valueOf(item.get("data"));
					data = data.replaceAll("mvnr", "'");
					String[] userArray = gson.fromJson(data.substring(1, data.length() - 1), String[].class);
					String linksData = String.valueOf(item.get("hyperlinks"));
					String[] linksArray = null;
					if (linksData != null) {
						linksArray = gson.fromJson(linksData.substring(1, linksData.length() - 1), String[].class);
					}
					String expData = String.valueOf(item.get("expiration"));
					String[] expArray = null;
					if (expData != null) {
						expArray = gson.fromJson(expData.substring(1, expData.length() - 1), String[].class);
					}
					String property[] = ((String) item.get("properties")).toString().split(",");
					if (property.length > 1) {
						if (property[1].equals("marquee"))
					
							marquee = new StringBuilder("<h5  class='text-danger fw-bold text-center'>"
									+ (String) item.get("object_title")
									+ "</h5><marquee scrollamount=3 onmouseover='this.stop();' onmouseout='this.start();' direction='"
									+ property[2] + "'>" + parseListData(userArray, linksArray, expArray, property[0],
											(String) item.get("styles"), (String) item.get("classes"))
									+ "</marquee>");
						else
							marquee = new StringBuilder(
									new StringBuffer("<h5  class='text-danger fw-bold text-center'>")
											.append((String) item.get("object_title")).append("</h5>"))
									.append(parseListData(userArray, linksArray, expArray, property[0],
											(String) item.get("styles"), (String) item.get("classes")));
					} else
						marquee = new StringBuilder(
								"<div class=\"card-body border border-2 border-success rounded-5 important_links\">")
								.append(parseListData(userArray, linksArray, expArray, property[0],
										String.valueOf(item.get("styles")), String.valueOf(item.get("classes"))))
								.append("\r\n" + "                  </div>");
					output[i] = marquee;
				} else if (type.equals("6") || type.equals("10")) {
					String data;
					String encd_message = String.valueOf(item.get("data"));
					encd_message = encd_message.replaceAll("mvnr", "'");
					data = URLDecoder.decode(encd_message.substring(2, encd_message.length() - 2),
							StandardCharsets.UTF_8.name());
					output[i] = new StringBuilder(data);
				} else if (type.equals("5")) {
					String data = String.valueOf(item.get("data"));
					data = data.replaceAll("mvnr", "'");
					String[] userArray = gson.fromJson(data.substring(1, data.length() - 1), String[].class);
					String property[] = String.valueOf(item.get("properties")).toString().split(",");
					output[i] = parseImageCarousel(userArray, i, property[0]);
				} else if (type.equals("2")) {
					String data = String.valueOf(item.get("data"));
					data = data.replaceAll("mvnr", "'");
//					System.out.println("2===="+data.toString());
					String[] userArray = gson.fromJson(data.substring(1, data.length() - 1), String[].class);
					output[i] = parseImages(userArray, i);
				} else if (type.equals("7")) {
					String data = String.valueOf(item.get("data"));
					data = data.replaceAll("mvnr", "'");
					String[] userArray = gson.fromJson(data.substring(1, data.length() - 1), String[].class);
					output[i] = parseVideos(userArray, i);
				} else if (type.equals("8")) {
					String data = String.valueOf(item.get("data"));
					data = data.replaceAll("mvnr", "'");
					String[] userArray = gson.fromJson(data.substring(1, data.length() - 1), String[].class);
					output[i] = parseAudios(userArray, i);
				} else if (type.equals("9")) {
					String data = String.valueOf(item.get("data"));
					data = data.replaceAll("mvnr", "'");
					String[] userArray = gson.fromJson(data.substring(1, data.length() - 1), String[].class);
					output[i] = parseDocs(userArray, i);
				}
				item.put("card_data", "<div class=\"card border-danger border border-1\">\r\n" + "	<div\r\n"
						+ "class=\"card-header\">\r\n" + "	<div class=\"d-flex justify-content-between\"><div>"
						+ " <a class=\"dropdown dropdown-hover\">\r\n"
						+ "<a class=\"\" href=\"#\" role=\"button\" data-bs-toggle=\"dropdown\"\r\n"
						+ "aria-expanded=\"false\"> <i class=\"fas fa-bars p-1 border fa-fw fa-2xs rounded text-white bg-info\" data-bs-toggle=\"tooltip\" data-bs-placement=\"bottom\"\r\n"
						+ "title=\"Options\"></i>\r\n" + "</a>\r\n"
						+ "<ul class=\"dropdown-menu dropdown-menu-end dropdown-menu-theme pr-0\">\r\n"
						+ "	<li><a class=\"dropdown-item\" href=\"fetch?object_id="
						+ (int) item.get("object_id") + "\"><i\r\n" + "class=\"fas fa-edit\"></i>Edit</a></li>\r\n"
						+ "<li><a class=\"dropdown-item\"  onClick=\"deleteObject(" + (int) item.get("object_id")
						+ ",this);\"><i\r\n" + "class=\"fas fa-trash\"></i>Delete</a></li>\r\n"
						+ "	<li><a class=\"dropdown-item\" href=\"approveObject?object_id="
						+ (int) item.get("object_id") + "\"><i\r\n" + "class=\"fas fa-check\"></i>Approve</a></li>\r\n"
						+ "</ul>\r\n" + "</a> \r\n" + "\r\n" + "<i\r\n"
						+ "class=\"fas fa-t p-1 border fa-fw fa-2xs rounded text-white bg-primary\" data-bs-toggle=\"tooltip\" data-bs-placement=\"bottom\" title="
						+ (String) item.get("lang") + "></i>\r\n"
						+ "<i class=\"fas fa-d p-1 border fa-fw fa-2xs rounded bg-success\" data-bs-toggle=\"tooltip\" data-bs-placement=\"bottom\"  title="
						+ (String) item.get("styles") + "></i>\r\n" + "<i\r\n"
						+ "class=\"fas fa-s p-1 border fa-fw fa-2xs rounded text-white bg-warning\" data-bs-toggle=\"tooltip\" data-bs-placement=\"bottom\"  title="
						+ (String) item.get("classes") + "></i>\r\n" + "						"
						+ "<i class=\"svg-inline--fa langIconBg langIcon English\"\r\n"
						+ "data-bs-toggle=\"tooltip\" data-bs-placement=\"bottom\"\r\n"
						+ "title=\"English available\"></i> <i\r\n"
						+ "class=\"svg-inline--fa langIconBg langIcon Hindi NA\"\r\n"
						+ "data-bs-toggle=\"tooltip\" data-bs-placement=\"bottom\"\r\n"
						+ "title=\"Hindi available\"></i></div>\r\n"
						+ "<div><i class=\"fa fa-remove\" onClick=\"removeObj(this);\"></i></div>" + "</div></div>"
						+ "<div class=\"card-body\">" + output[i].toString() + "</div></div>\r\n");

			}
		} catch (Exception e) {
			logger.error("Exception in SVPNPAService::getObjectTypeWise::" + e.getMessage());
			logger.error("Exception in SVPNPAService::getObjectTypeWise::" + e.getCause());
			logger.error("Exception in SVPNPAService::getObjectTypeWise::" + e.getClass() + "::" + e.getLocalizedMessage());
			}
		return items;
	}

	public String PreviewPage(int page_id, Model model) {
		StringBuilder viewObject = null,marquee = null;
		try {
			String ct = String.valueOf(dbutils.getDBValue("select count(*) from pages_names where page_id=? and published=true and verified=true and archive=false",new Object[] { (page_id) }));
			if(Integer.parseInt(ct)>0) {
			Long countLong = (long) dbutils.getDBValue("SELECT coalesce(count(page_id),0) FROM pages where page_id=?",
					new Object[] { (page_id) });
			String page_title = String.valueOf(dbutils.getDBValue(
					"select page_name from pages_names where page_id=? and published=true and verified=true",
					new Object[] { (page_id) }));
			String page_description = String.valueOf(dbutils.getDBValue(
	                "select description from pages_names where page_id=? and published=true and verified=true",
	                new Object[] { (page_id) }));
	        
	        String page_keywords = String.valueOf(dbutils.getDBValue(
	                "select keywords from pages_names where page_id=? and published=true and verified=true",
	                new Object[] { (page_id) }));
	        
	        model.addAttribute("page_title", page_title);
	        model.addAttribute("page_description", page_description);
	        model.addAttribute("page_keywords", page_keywords);
			int count = Integer.parseInt(countLong.toString());
			String template_data = String.valueOf(dbutils.getDBValue(
					"SELECT template_data FROM templates where template_id=(select template_id from pages_names where page_id=? and published=true and verified=true)",
					new Object[] { (page_id) }));
			viewObject = new StringBuilder(template_data);
			output = new StringBuilder[count];
			System.out.println("page_id===="+page_id+"====lang==="+lang);				
			ArrayList<HashMap<String, Object>> objects = dbutils.getListMap(
					"select o.object_id,object_category,data,o.properties,o.object_title,o.hyperlinks,o.expiration,o.styles,o.classes from pages p,objects o where p.object_id=o.object_id  and p.page_id=? and o.lang=? order by order_no",
					new Object[] { page_id, lang });
			Gson gson = new Gson();
			int i = 0;			
System.out.println("objects===="+objects);			
			for (Map map : objects) {
		String type = String.valueOf(map.get("object_category"));	
		System.out.println("type==="+type);
		if (type.equals("1")) {
			String data = String.valueOf(map.get("data"));
			data = data.replace("mvnr", "'");					
//-------------------------------18-11-2024-------------check date with Current Date(Expiry or Not)----------------------------------------	        	        
//	        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			sdf.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
	        String curDate = sdf.format(new Date());
	        Date dateB = sdf.parse(curDate);
	        int cc = 0;
//			String data1 = URLDecoder.decode(data.substring(2, data.length() - 2),StandardCharsets.UTF_8.name());			
	        String data1 = data.substring(2, data.length() - 2);
	        String[] subarrays = data1.split("\\],\\[");	        
	        subarrays[0] = subarrays[0].replace("[[", "");
	        subarrays[subarrays.length-1] = subarrays[subarrays.length-1].replace("]]", "");
	        // List of lists to store the parsed result
	        List<List<String>> resultList = new ArrayList<>();	        	        
	        // Process each subarray
	        for (String subarray : subarrays) {	        	
	            List<String> innerList = new ArrayList<>();
	            // Split each subarray by commas to get individual items
	            subarray = subarray.replace("[", "");
	            subarray = subarray.replace("]", "");
	            String[] items = subarray.split(",");
	            int k = 0,count1 = 0;
	            for (String item : items) {
	            	String trimmedItem = item.trim();
	            	Date dateA = null;
	            	if(trimmedItem.length() == 10) {
	            	try {
	                 dateA = sdf.parse(trimmedItem);	
	                if (dateA.equals(dateB)) {
	                } else if (dateB.before(dateA)) {
	                } else if (dateA.before(dateB)) {
	                    count1++;		                    	                
	                } else {}
	            	}catch(Exception e) {
	            		e.printStackTrace();	            		
	            	}
	            	}
	                //// Remove leading/trailing spaces and add the item to the inner list
////	            	item = URLEncoder.encode(item.toString(),StandardCharsets.UTF_8.name());
	                innerList.add(item.trim());	
	                k++;
	                
	            }
	            
	            String object_id = String.valueOf(map.get("object_id"));
	            if(Integer.parseInt(object_id) == 218) {
	            	if(count1>1) {
	    	            // Add the inner list to the result list
	    	           resultList.add(innerList);
	    	        }else if(count1==0) {
	    	            // Add the inner list to the result list
	   	             resultList.add(innerList);
	   	            }	
	            }else if(count1<2) {
	            // Add the inner list to the result list
	             resultList.add(innerList);
	            }
	        }	
	        
//---------------------------------------------------------------------------------------------------------------
//			String[][] userArray = gson.fromJson(data.substring(1, data.length() - 1), String[][].class);			
	        String[][] userArray = gson.fromJson(resultList.toString(), String[][].class);
			output[i] = parseTableData(String.valueOf(map.get("object_title")), userArray,
					(String) map.get("styles"), (String) map.get("classes"));
		} else if (type.equals("3")) {
			String data = String.valueOf(map.get("data"));
			// 1. Decode URL-encoded string	
			data = data.replace("mvnr", "'");
			String[] userArray = null;
			System.out.println("object_id======"+String.valueOf(map.get("object_id"))+"===title===="+String.valueOf(map.get("object_title")));
			System.out.println("data--3==="+data.toString());
			try {
			userArray = gson.fromJson(data.substring(1, data.length() - 1), String[].class);
			System.out.println("userArray=="+userArray.length+"==="+userArray.toString());
//-------------------18-11-2024-------------------------------------------------------------------------			
////			String subarray = gson.fromJson(data.substring(1, data.length() - 1), String.class);
//			String subarray = java.net.URLDecoder.decode(data.substring(1, data.length() - 1), java.nio.charset.StandardCharsets.UTF_8);
//			List<List<String>> resultList = new ArrayList<>();
//			int count1 = 0;
//            List<String> innerList = new ArrayList<>();
//            subarray = subarray.replace("[", "");
//            subarray = subarray.replace("]", "");
//            String[] phrases = subarray.split(",");
//            
//            Arrays.sort(phrases, new Comparator<String>() {
//                @Override
//                public int compare(String phrase1, String phrase2) {
//                    // Get the first word of each phrase
//                    String firstWord1 = phrase1.split("\\s+")[0].toLowerCase();
//                    String firstWord2 = phrase2.split("\\s+")[0].toLowerCase();
//
//                    // Compare based on the first letter of the first word (case-insensitive)
//                    return firstWord1.charAt(0) - firstWord2.charAt(0);
//                }
//            });
//
//            // Rebuild the string after sorting and join them with commas
//            String sortedString = String.join(", ", phrases);
//
//            // Print the sorted string
//            System.out.println("Sorted string *****: " + sortedString);
//            
////            for (String item : items) {
////            	String trimmedItem = item.trim();         		            	            		            
////                //item = URLEncoder.encode(item.toString(),StandardCharsets.UTF_8.name());
////                innerList.add(item.trim());	                
////            }
////            if(count1<1) {
////            // Add the inner list to the result list
////            resultList.add(innerList);
////            }
//            
//            String item = URLEncoder.encode(sortedString.toString(),StandardCharsets.UTF_8.name());
//            userArray[0] = item;
//            System.out.println("item : " + item);
//            System.out.println("userArray *****: " + userArray[0]);
//---------------------------------------------------------------------------------------------------------			
			}catch(Exception e) {e.printStackTrace();}
			String linksData = String.valueOf(map.get("hyperlinks"));
			String[] linksArray = null;
			if (linksData != null) {
				linksArray = gson.fromJson(linksData.substring(1, linksData.length() - 1), String[].class);
			}
			String expData = String.valueOf(map.get("expiration"));
			String[] expArray = null;
			if (expData != null) {
				expArray = gson.fromJson(expData.substring(1, expData.length() - 1), String[].class);
			}
			String property[] = String.valueOf(map.get("properties")).toString().split(",");
			String view_more = "";
			String list_class = "";
			if (String.valueOf(map.get("classes")).contains("view-more")) {
				view_more = "<a href=\"/Preview?page_id=196\" style=\"bottom:10px;left:10px;position:absolute;\" title=\"View all\" onClick=\"marqueePopup(this);\" class=\"btn btn-info\" target=\"_blank\" onClick=\"marqueePopup(this);\" style=\"margin-top: 10px;\">View more</a>";
				list_class = " border border-2 border-success rounded-5 ";
			}
	if (property.length > 1) {
		if (property[1].equals("marquee"))
			if (property[2].equals("up"))
				marquee = new StringBuilder("<h5 class='text-danger fw-bold text-center'>"
						+ String.valueOf(map.get("object_title")) + "</h5><div class=\"card-body "
						+ list_class
						+ " important_links\"><marquee scrollamount=3 onmouseover='this.stop();' onmouseout='this.start();' direction='"
						+ property[2] + "'>"
						+ parseListData(userArray, linksArray, expArray, property[0],String.valueOf(map.get("styles")), String.valueOf(map.get("classes")))
						+ "</marquee>" + view_more + "   </div>               ");
			else if (property[2].equals("left"))
				marquee = new StringBuilder(
						"<div class=\"ticker1 modern-ticker mt-round mt-scroll border border-1\">\r\n"
						+ "                     <div class=\"latest-update\">"
						+ String.valueOf(map.get("object_title")) + "</div>\r\n"
						+ "                     <div class=\"mt-news newsof-widht\">"
	
						+ parseListData(userArray, linksArray, expArray, property[0],
								String.valueOf(map.get("styles")),
								String.valueOf(map.get("classes")))
						+ "<a href=\"/Preview?page_id=196\" target=\"_blank\" onClick=\"marqueePopup(this);\"><i class=\"fa-solid fa-up-right-from-square mx-2\"></i></a></div>\r\n"
						+ "                     <div class=\"mt-controls\">\r\n"
						+ "                        <a href=\"#mt-prev\" class=\"mt-prev\" title=\"prev\"><span class=\"hidethis\">Previous</span></a>\r\n"
						+ "                        <a href=\"#mt-pause\" class=\"mt-play mt-pause\" title=\"Pause\"><span class=\"hidethis\">Pause</span></a>\r\n"
						+ "                        <a href=\"#mt-next\" class=\"mt-next\" title=\"next\"><span class=\"hidethis\">Next</span></a>\r\n"
						+ "                     </div>");
			else
				marquee = new StringBuilder(
						new StringBuffer("<h5 class='text-danger fw-bold text-center'>")
								.append(String.valueOf(map.get("object_title")))
								.append("</h5>                  <div class=\"card-body " + list_class
										+ " important_links\">\r\n" + ""))
						.append(parseListData(userArray, linksArray, expArray, property[0],
								String.valueOf(map.get("styles")), String.valueOf(map.get("classes")))
								.append(view_more + "                  </div>"));
						} else {
							marquee = new StringBuilder(
									"<h5 class='text-danger fw-bold text-center'>" + String.valueOf(map.get("object_title"))
											+ "</h5><div class=\"card-body " + list_class + " important_links\">")
									.append(parseListData(userArray, linksArray, expArray, property[0],
											String.valueOf(map.get("styles")), String.valueOf(map.get("classes")))
											.append(view_more + "                  </div>"));
						}
						output[i] = marquee;
						
						String object_id = String.valueOf(map.get("object_id"));
						/*
						 * if(object_id.equalsIgnoreCase("7")) { //
						 * System.out.println("marquee==7==="+marquee.toString()); }else { //
						 * System.out.println("marquee==="+marquee.toString()); }
						 */
			} else if (type.equals("6") || type.equals("10")) {
				String data;
				String encd_message = String.valueOf(map.get("data"));
				encd_message = encd_message.replace("mvnr", "'");
				data = URLDecoder.decode(encd_message.substring(2, encd_message.length() - 2),StandardCharsets.UTF_8.name());
				output[i] = new StringBuilder(data);
			} else if (type.equals("5")) {
				try {
				String data = String.valueOf(map.get("data"));
				data = data.replaceAll("mvnr", "'");
//System.out.println("data===="+data.toString());				
				String[] userArray = gson.fromJson(data.substring(1, data.length() - 1), String[].class);
//				System.out.println("userArray===="+userArray.toString());
				String property[] = String.valueOf(map.get("properties")).toString().split(",");
				output[i] = parseImageCarousel(userArray, i, property[0]);
				}catch(Exception e) {
					e.printStackTrace();
				}
			} else if (type.equals("2")) {
				String data = String.valueOf(map.get("data"));
				data = data.replaceAll("mvnr", "'");
				String[] userArray = gson.fromJson(data.substring(1, data.length() - 1), String[].class);
				output[i] = parseImages(userArray, i);
			} else if (type.equals("7")) {
				String data = String.valueOf(map.get("data"));
				data = data.replaceAll("mvnr", "'");
				String[] userArray = gson.fromJson(data.substring(1, data.length() - 1), String[].class);
				output[i] = parseVideos(userArray, i);
			} else if (type.equals("8")) {
				String data = String.valueOf(map.get("data"));
				data = data.replaceAll("mvnr", "'");
				String[] userArray = gson.fromJson(data.substring(1, data.length() - 1), String[].class);
				output[i] = parseAudios(userArray, i);
			} else if (type.equals("9")) {
				String data = String.valueOf(map.get("data"));
				data = data.replaceAll("mvnr", "'");
				String[] userArray = gson.fromJson(data.substring(1, data.length() - 1), String[].class);
				output[i] = parseDocs(userArray, i);
			}
			int index = viewObject.indexOf("@object");
			viewObject = new StringBuilder(viewObject.substring(0, index) + output[i]
					+ viewObject.substring(index + 7, viewObject.length()));
			i++;
		}
		}else {
			viewObject = new StringBuilder("</br></br><h3 style='text-align:center;color:red;'>Page was Archived</h3></br></br></br></br>");
		}
		this.viewObject = viewObject;
		model.addAttribute("viewObject", viewObject);
	} catch (Exception e) {
		e.printStackTrace();
		logger.error("Exception in SVPNPAService::PreviewPage::" + e.getMessage());
		logger.error("Exception in SVPNPAService::PreviewPage::" + e.getCause());
		logger.error("Exception in SVPNPAService::PreviewPage::" + e.getClass() + "::" + e.getLocalizedMessage());
		}
	return "Preview";
	}

	
	private StringBuilder parseImageCarousel(String[] array, int i, String random) {
		StringBuilder temp = null;
		String str = "";
		try {
			StringBuilder sliders = new StringBuilder("  <div class=\"carousel-indicators\">\r\n" + "");
			for (int j = 0; j < array.length; j++) {
				if (j == 0) {
					sliders.append("<button type=\"button\" data-bs-target=\"#carouselExample" + i
							+ "\" data-bs-slide-to=\"" + j
							+ "\" class=\"active\" aria-current=\"true\" aria-label=\"Slide" + j + "\"></button>");
				} else
					sliders.append("<button type=\"button\" data-bs-target=\"#carouselExample" + i
							+ "\" data-bs-slide-to=\"" + j + "\" aria-label=\"Slide" + j + "\"></button>");
			}
			sliders.append("</div>");
//			temp = new StringBuilder("<div id=\"carouselExample" + i
//					+ "\" class=\"carousel carousel-dark slide\" data-bs-ride=\"carousel\"\r\n" + ">\r\n" + sliders
//					+ "  <div class=\"carousel-inner\" style=\" width:auto; height: auto !important; overflow:hidden;\">");
			if(random.equalsIgnoreCase("") || random.equalsIgnoreCase("false")) {
			str = "<div id=\"carouselExample" + i
					+ "\" class=\"carousel carousel-dark slide\" data-bs-ride=\"carousel\"\r\n" + ">\r\n" + sliders
					+ "  <div class=\"carousel-inner\" style=\" width:auto; height: auto !important; overflow:hidden;\">";
			}else {
				str = "<div id=\"carouselExample" + i
						+ "\" class=\"carousel carousel-dark slide\" data-bs-ride=\"carousel\"\r\n" + ">\r\n" + sliders
						+ "  <div class=\"carousel-inner random-slides\" style=\" width:auto; height: auto !important; overflow:hidden;\">";
			}
			temp = new StringBuilder(str);
			for (int j = 0; j < array.length; j++) {
				if (j == 0)
					temp.append(					
							"<div class=\"carousel-item active\">\r\n" + "      <img src=\"" + galleryPath + "\\image\\"
									+ array[j] + "\" class=\"d-block  img-fluid \"  alt=\"...\">\r\n" + "    </div>");
//					temp.append(
//							"<div class=\"carousel-item active\">\r\n" + "   <img src=\"/getImg/" 
//									+ array[j] + "\"  class=\"d-block  img-fluid \"  alt=\"...\">\r\n" + "    </div>");
				else
					temp.append("<div class=\"carousel-item\">\r\n" + "      <img src=\"" + galleryPath + "\\image\\"
							+ array[j] + "\" class=\"d-block img-fluid\"  alt=\"...\">\r\n" + "    </div>");
//					temp.append("<div class=\"carousel-item\">\r\n" + "  <img src=\"/getImg/" 
//							+ array[j] + "\" class=\"d-block img-fluid\"  alt=\"...\">\r\n" + "    </div>");
			}
			
			temp.append("</div>\r\n"
					+ "  <button class=\"carousel-control-prev\" type=\"button\" data-bs-target=\"#carouselExample" + i
					+ "\" data-bs-slide=\"prev\">\r\n"
					+ "    <span class=\"carousel-control-prev-icon\" aria-hidden=\"true\"></span>\r\n"
					+ "    <span class=\"visually-hidden\">Previous</span>\r\n" + "  </button>\r\n"
					+ "  <button class=\"carousel-control-next\" type=\"button\" data-bs-target=\"#carouselExample" + i
					+ "\" data-bs-slide=\"next\">\r\n"
					+ "    <span class=\"carousel-control-next-icon\" aria-hidden=\"true\"></span>\r\n"
					+ "    <span class=\"visually-hidden\">Next</span>\r\n" + "  </button>\r\n" + "</div>");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception in SVPNPAService::parseImageCarousel::" + e.getMessage());
			logger.error("Exception in SVPNPAService::parseImageCarousel::" + e.getCause());
			logger.error("Exception in SVPNPAService::parseImageCarousel::" + e.getClass() + "::" + e.getLocalizedMessage());
			}
		return temp;
	}

	private StringBuilder parseImages(String[] array, int i) {
		StringBuilder temp = null;
		String width = "100%";
		try {
			temp = new StringBuilder();
			if (array.length > 10) {				
				temp.append("<div class=\"docs-galley mb-3\"><ul class=\"docs-pictures clearfix\">");
				for (int j = 0; j < array.length; j++) {
					Map<String, Object> imageDesc = dbutils.getMap(
							"select upper(properties->>'description') as description,coalesce(properties->>'title',properties->>'description')  as title from gallery where file_name=?",
							new Object[] { array[j] });
					temp.append("<li class=\"ImgGallery\"><img data-original=\""
							+ "" + galleryPath + "\\image\\" + array[j] + "\" width=\"145\" height=\"300\"  class=\" p-3\" id=\"img-" + j
							+ "\" src=\"" + galleryPath + "\\image\\" + array[j] + "\"  alt=\"" + imageDesc.get("title")
							+ "\" title=\"" + imageDesc.get("title")
							+ "\" onClick=\"titleSelected(this);\"><p class=\"mx-auto text-center fw-bold\">"
							+ imageDesc.get("description") + "</p></li>");
				}
				temp.append("</ul></div>");

			} else if (array.length < 10) {				
				for (int j = 0; j < array.length; j++) {
					String imageDesc = String.valueOf(
							dbutils.getDBValue("select properties->>'description' from gallery where file_name=?",
									new Object[] { array[j] }));
					temp.append("<img class=\"img-fluid mb-3 fadein\" id=\"img-" + j + "\" src=\"" + galleryPath
//------------Newly-Added------------------------------------------							
							+ "\\image\\"
//------------End------------------------------------------							
							+ array[j] + "\" style=\"width:" + width + "\" alt=\"SVPNPA\" title=\""
							+ imageDesc + "\">");
				} 
		}

		} catch (Exception e) {
			logger.error("Exception in SVPNPAService::parseImages::" + e.getMessage());
			logger.error("Exception in SVPNPAService::parseImages::" + e.getCause());
			logger.error("Exception in SVPNPAService::parseImages::" + e.getClass() + "::" + e.getLocalizedMessage());
			}
		return temp;
	}

	private StringBuilder parseVideos(String[] array, int i) {
		StringBuilder temp = null;
		try {
			temp = new StringBuilder();
			for (int j = 0; j < array.length; j++) {
				temp.append("<video id=\"video-" + j
						+ "\" controls=\"\" width=\"100%\" height=\"100%\" class=\"embed-responsive-item\"> <source src=\""
						+ galleryPath + "\\video\\" + array[j] + "\" type=\"video/mp4\"></video>");
//						+ "/getVedio/" + array[j] + "\" type=\"video/mp4\"></video>");
			}
		} catch (Exception e) {
			logger.error("Exception in SVPNPAService::parseVideos::" + e.getMessage());
			logger.error("Exception in SVPNPAService::parseVideos::" + e.getCause());
			logger.error("Exception in SVPNPAService::parseVideos::" + e.getClass() + "::" + e.getLocalizedMessage());
			}
		return temp;
	}

	private StringBuilder parseAudios(String[] array, int i) {
		StringBuilder temp = null;
		try {
			temp = new StringBuilder();
			for (int j = 0; j < array.length; j++) {
				temp.append("<audio id=\"audio-" + j
						+ "\" controls=\"\" width=\"100%\" height=\"100%\" class=\"embed-responsive-item\"> <source src=\""
						+ galleryPath + "\\audio\\" + array[j] + "\" type=\"audio/mp3\"></audio>");
//						+ "/getAudio/" + array[j] + "\"  type=\"audio/mp3\"></audio>");
			}
		} catch (Exception e) {
			logger.error("Exception in SVPNPAService::parseAudios::" + e.getMessage());
			logger.error("Exception in SVPNPAService::parseAudios::" + e.getCause());
			logger.error("Exception in SVPNPAService::parseAudios::" + e.getClass() + "::" + e.getLocalizedMessage());
			}
		return temp;
	}

	private StringBuilder parseDocs(String[] array, int i) {
		StringBuilder temp = null;
		try {
			temp = new StringBuilder();
			for (int j = 0; j < array.length; j++) {
//				temp.append("<a href=\"" + galleryPath + "\\docs\\" + array[j]+ "\" target=\"_blank\" ><i class=\"far fa-2xl fa-file-pdf\"></i></a>");
				// temp.append("<iframe src=\"" + galleryPath + "\\docs\\" + array[j]+ "\"
				// width=\"100%\" height=\"100%\"></iframe>");
				temp.append(
						"<embed src=\"" + galleryPath + "\\docs\\" + array[j] + "\" width=\"100%\" height=\"1000px\">");											
//						"<embed src=\"" + "/getDoc/" + array[j] + "\"  type=\"application/pdf\" width=\"100%\" height=\"1000px\">");
				/*
				 * <embed src="http://example.com/the.pdf" width="500" height="375"
				 * type="application/pdf">
				 */
			}
		} catch (Exception e) {
			logger.error("Exception in SVPNPAService::parseDocs::" + e.getMessage());
			logger.error("Exception in SVPNPAService::parseDocs::" + e.getCause());
			logger.error("Exception in SVPNPAService::parseDocs::" + e.getClass() + "::" + e.getLocalizedMessage());
			}
		return temp;
	}

	private StringBuilder parseHTML(String[] array) {
		StringBuilder temp = null;
		try {
			temp = new StringBuilder();
			for (int j = 0; j < array.length; j++) {
				temp.append(array[j]);
			}
		} catch (Exception e) {
			logger.error("Exception in SVPNPAService::parseHTML::" + e.getMessage());
			logger.error("Exception in SVPNPAService::parseHTML::" + e.getCause());
			logger.error("Exception in SVPNPAService::parseHTML::" + e.getClass() + "::" + e.getLocalizedMessage());
			}
		return temp;
	}

	public int saveTableData(String object_id, String imgType, String jsonString, String type, String marquee,
			String linksString, String newJson, String list_type, String expString, String object_title,
			String object_title_hi, String styles, String classes, String description, String help_text,
			String alternate_text, String keywords, String jsonString_hi, String meta_hi,String random) {
			String object_id_from_db = null;
		int i = 0, count = 0, count_hi = 0;
		logger.info("saveTableData=========");
		try {
			if (type.equals("2") || type.equals("5") || type.equals("7") || type.equals("8") || type.equals("9")) {
				meta_hi = newJson;
				object_title_hi = object_title;
				jsonString_hi = jsonString;
			}
			if(type.equals("5")) {
				list_type = random;	
			}
			Integer countLong = (int) dbutils.getDBValue("SELECT coalesce(max(object_id)+1,0) FROM objects", null);
			object_id_from_db = countLong.toString();
			boolean check = false;
			if (object_id != null && !object_id.equals("")) {
				check = (boolean) dbutils.getDBValue("SELECT true FROM objects where  object_id=?",
						new Object[] { Integer.parseInt(object_id) });
			}
			if (newJson == null || newJson.length() == 0) {
				Map propertiesJson = new HashMap<String, String>();
				propertiesJson.put("title", object_title);
				propertiesJson.put("description", description);
				propertiesJson.put("help_text", help_text);
				propertiesJson.put("alt_text", alternate_text);
				propertiesJson.put("keywords", keywords);
				newJson = new Gson().toJson(propertiesJson);
			}
			
			String linksStringqry = "", expStringqry = "";
			jsonString = jsonString.replace("'", "mvnr");
//			jsonString_hi = jsonString_hi.replace("\"", "mnr");
			jsonString_hi = jsonString_hi.replace("'", "mvnr");
//			jsonString_hi = jsonString_hi.replace(",", "mnr");
			
			System.out.println("jsonString===="+jsonString);
			
			System.out.println("jsonString_hi===="+jsonString_hi);
				try {
					if(linksString.trim().equalsIgnoreCase("")|| linksString.trim().equalsIgnoreCase(null)) {
						linksString = null;					
					}else {}
					if(expString.trim().equalsIgnoreCase("")|| expString.trim().equalsIgnoreCase(null)) {
						expString = null;					
					}else {}
					if(meta_hi.trim().equalsIgnoreCase("")|| meta_hi.trim().equalsIgnoreCase(null)) {
						meta_hi = null;					
					}else {}				
				
					object_title = (object_title.trim() != null) ? object_title.trim() : "";
					marquee = (marquee != null) ? marquee.trim() : "";				
					newJson = (newJson != null) ? newJson.trim() : "";
					styles = (styles != null) ? styles.trim() : "";
					classes = (classes != null) ? classes.trim() : "";
					jsonString_hi = (jsonString_hi != null) ? jsonString_hi.trim() : "";
					object_title_hi = (object_title_hi != null) ? object_title_hi.trim() : "";
					//meta_hi = (meta_hi != null) ? meta_hi.trim() : "";
				}catch(Exception e) {
					e.printStackTrace();
				}
				
				
			if (!check) {
				System.out.println("----------");
					count = dbutils.insertOrUpdate(
						"INSERT INTO objects(object_id,object_category,data,object_title,properties,hyperlinks,expiration,object_meta,lang,styles,classes) VALUES(?,?,?::json,?,?,?::json,?::json,?::json,?,?,?)",
//						"INSERT INTO objects(object_id,object_category,data,object_title,properties,hyperlinks,expiration,object_meta,lang,styles,classes,verified) VALUES(?,?,?::json,?,?,?::json,?::json,?::json,?,?,?,true)",											
						new Object[] { Integer.parseInt(object_id_from_db), Integer.parseInt(type), jsonString,
								object_title, list_type + "," + marquee, linksString, expString, newJson, 'E', styles,
								classes });
				if (jsonString_hi != null && jsonString_hi.length() > 0) {
					count_hi = dbutils.insertOrUpdate(
							"INSERT INTO objects(object_id,object_category,data,object_title,properties,hyperlinks,expiration,object_meta,lang,styles,classes) VALUES(?,?,?::json,?,?,?::json,?::json,?::json,?,?,?)",
//							"INSERT INTO objects(object_id,object_category,data,object_title,properties,hyperlinks,expiration,object_meta,lang,styles,classes,verified) VALUES(?,?,?::json,?,?,?::json,?::json,?::json,?,?,?,true)",
							new Object[] { Integer.parseInt(object_id_from_db), Integer.parseInt(type), jsonString_hi,
									object_title_hi, list_type + "," + marquee, linksString, expString, meta_hi, 'H',
									styles, classes });
				}
			} else {	
				System.out.println("----1-----");
				count = dbutils.insertOrUpdate(
						"update objects set data=?::json,object_title=?,properties=?,hyperlinks='"+linksString+"'::json,expiration='"+expString+"'::json,object_meta=?::json,styles=?,classes=? where object_id=? and lang='E'",						
						new Object[] { jsonString, object_title, list_type + "," + marquee,
								newJson, styles, classes, Integer.parseInt(object_id) });
				count_hi = dbutils.insertOrUpdate(
						"update objects set data=?::json,object_title=?,properties=?,hyperlinks='"+linksString+"'::json,expiration='"+expString+"'::json,object_meta=?::json,styles=?,classes=? where object_id=? and lang='H'",
						new Object[] { jsonString_hi, object_title_hi, list_type + "," + marquee,
								 meta_hi, styles, classes, Integer.parseInt(object_id) });
				if(Integer.parseInt(object_id)== 217) {
					int count1 = dbutils.insertOrUpdate(
							"update objects set data=?::json,object_title=?,properties=?,hyperlinks='"+linksString+"'::json,expiration='"+expString+"'::json,object_meta=?::json,styles=?,classes=? where object_id='218' and lang='E'",						
							new Object[] { jsonString, object_title, list_type + "," + marquee,
									newJson, styles, classes});
					int count_hi1 = dbutils.insertOrUpdate(
							"update objects set data=?::json,object_title=?,properties=?,hyperlinks='"+linksString+"'::json,expiration='"+expString+"'::json,object_meta=?::json,styles=?,classes=? where object_id='218' and lang='H'",
							new Object[] { jsonString_hi, object_title_hi, list_type + "," + marquee,
									 meta_hi, styles, classes});
					System.out.println("count1==="+count1+"====count_hi1==="+count_hi1);
				}
			}
			logger.info("count==="+count+"====count_hi==="+count_hi);
			if(count>0 || count_hi>0) {
				i = 1;
			}
		} catch (Exception e) {
		 	e.printStackTrace();
			logger.error("Exception in SVPNPAService::saveTableData::" + e.getMessage());
			logger.error("Exception in SVPNPAService::saveTableData::" + e.getCause());
			logger.error("Exception in SVPNPAService::saveTableData::" + e.getClass() + "::" + e.getLocalizedMessage());
		}
		logger.info("i====="+i);
//		return "redirect:/pageObjects";
		return i;
	}

	public int savePageData(String jsonString, int template_id, String page_id_given, String page_name,
			String description, String keywords) {
//		System.out.println("page_name===="+page_name);
		int k = 0;
		try {
			int page_id = 0;
			if (page_id_given != null && page_id_given.length() != 0)
				page_id = Integer.parseInt(page_id_given);
			Gson gson = new Gson();
			String[] userArray = gson.fromJson(jsonString.substring(1, jsonString.length() - 1), String[].class);
			int i = 1;
			int page_id_max = Integer.valueOf(
					String.valueOf(dbutils.getDBValue("SELECT coalesce(max(page_id),0)+1 FROM pages_names", null)));
			if (page_id_given == null)
				page_id = page_id_max;
//			System.out.println("page_id===="+page_id);
			boolean check2 = Boolean.valueOf(String.valueOf(
					dbutils.getDBValue("SELECT true FROM pages_names where page_id=?", new Object[] { page_id })));
			if (!check2) {
				k = dbutils.insertOrUpdate("INSERT INTO pages_names VALUES(?,?,?,?,?);",
						new Object[] { page_id_max, page_name, template_id, description, keywords });

//				int k = dbutils.insertOrUpdate("INSERT INTO pages_names VALUES(?,?,?,?,?,?,?);",
//						new Object[] { page_id_max, page_name, template_id, description, keywords, true, true });
//				System.out.println("k==if=="+k);
			} else {
				k = dbutils.insertOrUpdate(
						"update pages_names set page_name=?,description=?,keywords=? where page_id=?",
						new Object[] { page_name, description, keywords, page_id });
				
//				System.out.println("k==else=="+k);
			}
			if (page_id == 0)
				page_id = page_id_max;
			for (String val : userArray) {
				boolean check = Boolean.valueOf(String.valueOf(dbutils.getDBValue(
						"SELECT true FROM pages where page_id=? and order_no=?", new Object[] { page_id, i })));
				if (!check) {
					k = dbutils.insertOrUpdate("INSERT INTO pages VALUES(?,?,?);",
							new Object[] { page_id, Integer.parseInt(val), i });
//					System.out.println("k==Loop==if=="+k);
				} else {
					k = dbutils.insertOrUpdate("update pages set object_id=? where page_id=? and order_no=?",
							new Object[] { Integer.parseInt(val), page_id, i });
//					System.out.println("k=Loop=else=="+k);
				}
				i++;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception in SVPNPAService::savePageData::" + e.getMessage());
			logger.error("Exception in SVPNPAService::savePageData::" + e.getCause());
			logger.error("Exception in SVPNPAService::savePageData::" + e.getClass() + "::" + e.getLocalizedMessage());
			}
//		return "redirect:/view";
		return k;
	}


	public StringBuilder parseTableData(String tableTitle, String[][] array, String styles, String classes) {
		StringBuilder temp = null;
		try {
			temp = new StringBuilder("<h5>" + tableTitle + "</h5><table styles='" + styles
					+ "'  class=\"w-100 h-100 customTable border border-2 " + classes + " \">");
			for (int i = 0; i < array.length; i++) {
				if (i == 0)
					temp.append("<thead>");
				else if (i == 1)
					temp.append("<tbody>");
				temp.append("<tr>");
				for (int j = 0; j < array[i].length; j++) {
					if (i == 0)
						temp.append("<th class=\"bg-light text-center border border-1\">"
								+ new StringBuilder(URLDecoder.decode(array[i][j].replaceAll("MNR", "\\'"), StandardCharsets.UTF_8.name()))
								+ "</th>");
					else
						temp.append("<td class=\"border border-1 \" style=\"word-wrap:word-break;\">"
								+ new StringBuilder(URLDecoder.decode(array[i][j].replaceAll("MNR", "\\'"), StandardCharsets.UTF_8.name()))
								+ "</td>");
				}
				temp.append("</tr>");
				if (i == 0)
					temp.append("</thead>");
			}
			temp.append("</tbody></table>");
		} catch (Exception e) {
			logger.error("Exception in SVPNPAService::parseTableData::" + e.getMessage());
			logger.error("Exception in SVPNPAService::parseTableData::" + e.getCause());
			logger.error("Exception in SVPNPAService::parseTableData::" + e.getClass() + "::" + e.getLocalizedMessage());
			}
		return temp;
	}

	public StringBuilder parseListData(String[] array, String[] linksArray, String[] expArray, String type,
			String styles, String classes) {
		StringBuilder temp = null;
		String tagName = "";
		tagName = type;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
			System.out.println("linksArray===="+linksArray.length);
			System.out.println("Array-Length===="+array.length);
			if (linksArray == null) {
				temp = new StringBuilder("<" + tagName + " style='" + styles + "' class='" + classes + "'>");
				if (array.length > 0) {
				for (int i = 0; i < array.length; i++) {
//					System.out.println("i===="+i);
					if (expArray[i].equals("")) {
						try {
						temp.append(
								"<li>" + new StringBuilder(URLDecoder.decode(array[i], StandardCharsets.UTF_8.name()))
									+ "</li>");
						}catch(Exception e) {}
					}else if (expArray == null || expArray[i] == null || expArray[i].equals("")
						|| sdf.parse(new String(URLDecoder.decode(expArray[i], StandardCharsets.UTF_8.name())))
							.before(sdf.parse(sdf.format(new java.util.Date())))) {
						try {
						temp.append(
								"<li>" + new StringBuilder(URLDecoder.decode(array[i], StandardCharsets.UTF_8.name()))
									+ "</li>");
						}catch(Exception e) {}
					}
				}
			}
			} else {
				temp = new StringBuilder("<" + tagName + " style='" + styles + "' class='" + classes + "'>");
				if (array.length > 0) {
//					System.out.println("array===="+array.length);
					for (int i = 0; i < array.length; i++) {
						if (expArray[i] == null || expArray[i].equals("")) {
							try {
							temp.append("<li><a href="
									+ new StringBuilder(URLDecoder.decode(linksArray[i], StandardCharsets.UTF_8.name()))
									+ ">"
									+ new StringBuilder(URLDecoder.decode(array[i], StandardCharsets.UTF_8.name()))
									+ "</a></li>");
						}catch(Exception e) {}
					}else if (expArray == null || expArray[i] == null
						|| sdf.parse(new String(URLDecoder.decode(expArray[i], StandardCharsets.UTF_8.name())))
							.after(sdf.parse(sdf.format(new java.util.Date())))) {
						try {	
						temp.append("<li><a href="
							+ new StringBuilder(URLDecoder.decode(linksArray[i], StandardCharsets.UTF_8.name()))
							+ ">"
							+ new StringBuilder(URLDecoder.decode(array[i], StandardCharsets.UTF_8.name()))
							+ "</a></li>");
						}catch(Exception e) {}
						
					}
					}
				}
			}
			temp.append("</" + tagName + ">");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception in SVPNPAService::parseListData::" + e.getMessage());
			logger.error("Exception in SVPNPAService::parseListData::" + e.getCause());
			logger.error("Exception in SVPNPAService::parseListData::" + e.getClass() + "::" + e.getLocalizedMessage());
		}
		System.out.println("temp===="+temp.toString());
		return temp;
	}

	public String getImage(Model model) {
		try {
			String filename = "";
			String folderPath = galleryPath + "\\image\\";
			File folder = new File(folderPath);
			File[] listOfFiles = folder.listFiles();
			List<String> imageNames = new ArrayList<>();
			if (listOfFiles != null) {
				for (File file : listOfFiles) {
					if (file.isFile()) {
						filename = file.getName();
						if (filename.startsWith("t_")) {
							imageNames.add(file.getName());
						}
					}
				}
			}
			model.addAttribute("imageNames", imageNames);
		} catch (Exception e) {
			logger.error("Exception in SVPNPAService::getImage::" + e.getMessage());
			logger.error("Exception in SVPNPAService::getImage::" + e.getCause());
			logger.error("Exception in SVPNPAService::getImage::" + e.getClass() + "::" + e.getLocalizedMessage());			
		}
		return "imageUploader";
	}

	public String uploadImage(Model model, MultipartFile file[], List<String> file_desc, String group_id)
			throws IOException {
//		String UPLOAD_DIRECTORY = galleryAbsolutePath + "/image"; // Production
//		String UPLOAD_DIRECTORY = galleryAbsolutePath + "\\image"; // Staging
		String UPLOAD_DIRECTORY = galleryAbsolutePath + File.separator + "image"; // Staging
		String user_identity = "usr", original_filename = "";		
		try {
			StringBuilder fileNames = new StringBuilder();
			int length = file.length;
			JSONObject reqJson = new JSONObject();
			for (int i = 0; i < file.length; i++) {
				original_filename = file[i].getOriginalFilename();
				String fileExtension = getFileExtension(original_filename); // Get the file extension
// Generate a random UUID as the new file name
				String randomFileName = generateRandomFileName();
// Create the new file name with the random UUID and the original extension
				String newFileName = randomFileName + fileExtension;
				String file_name_temp = "";
//				int dotIndex = original_filename.lastIndexOf('.');
//				if (dotIndex > 0) {
//					file_name_temp = original_filename.substring(0, dotIndex);
//				}
				Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, newFileName);
//				System.out.println(fileNameAndPath + "--" + newFileName + "Path");

				fileNames.append(file[i].getOriginalFilename());
				Files.write(fileNameAndPath, file[i].getBytes());
//				File inputFile = new File(UPLOAD_DIRECTORY + "/" + newFileName); // Replace with the path to your input
				File inputFile = new File(UPLOAD_DIRECTORY + File.separator + newFileName); // image

				BufferedImage inputImage = ImageIO.read(inputFile);
				int thumbnailWidth = 100; // Width of the thumbnail image
				int thumbnailHeight = 100; // Height of the thumbnail image
				Image thumbnail = inputImage.getScaledInstance(thumbnailWidth, thumbnailHeight, Image.SCALE_DEFAULT);
				BufferedImage bufferedThumbnail = new BufferedImage(thumbnailWidth, thumbnailHeight,
						BufferedImage.TYPE_INT_RGB);
				Graphics2D g2d = bufferedThumbnail.createGraphics();
				g2d.drawImage(thumbnail, 0, 0, null);
				g2d.dispose();
				String thumbnail_fname = "t_" + newFileName;
//				File outputFile = new File(UPLOAD_DIRECTORY + "/" + thumbnail_fname);
				File outputFile = new File(UPLOAD_DIRECTORY + File.separator + thumbnail_fname);
				ImageIO.write(bufferedThumbnail, "PNG", outputFile);
//				reqJson.put("description", file_desc.get(i));
//				String rq2 = reqJson.toString();	
//------------Newly-Added-Code------------------------------------------				
				String fdc = "",rq2 = "";
				String[] mnr;				
				try {
					fdc = file_desc.get(i);
					fdc = fdc.replace("\\", "");
					mnr = fdc.split("&");
					reqJson.put("description", mnr[0]);
					reqJson.put("title", mnr[1]);					
					
					ObjectNode newJsonObject = null;
					ObjectMapper objectMapper = new ObjectMapper();
					
					newJsonObject = (ObjectNode) objectMapper.readTree(reqJson.toString());
		            rq2 = newJsonObject.toString();
					
				}catch(Exception e) {	
					e.printStackTrace();
				 reqJson.put("description", file_desc.get(i));
				 rq2 = reqJson.toString();
				}										 
//-----------------------------------------------------------				
				int k = dbutils.insertOrUpdate(
						"INSERT INTO gallery(media_type,file_name,path,thumbnail_filename,group_id,user_identity,original_filename,properties) values(?,?,?,?,?,?,?,to_json(?::json));",
						new Object[] { 2, newFileName, UPLOAD_DIRECTORY, thumbnail_fname, group_id, user_identity,original_filename, rq2 });
				
			}
			model.addAttribute("msg", "Uploaded images: " + fileNames.toString());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception in SVPNPAService::uploadImage::" + e.getMessage());
			logger.error("Exception in SVPNPAService::uploadImage::" + e.getCause());
			logger.error("Exception in SVPNPAService::uploadImage::" + e.getClass() + "::" + e.getLocalizedMessage());
				}
		return "gallery";
	}

	public String uploadImage1(Model model, MultipartFile file[]) throws IOException {
//		String uploadPath = galleryAbsolutePath + "/image"; //Production
//		String uploadPath = galleryAbsolutePath + "\\image"; // Staging
		String uploadPath = galleryAbsolutePath + File.separator + "image"; // Staging
		try {
			StringBuilder fileNames = new StringBuilder();
			for (int i = 0; i < file.length; i++) {
				Path fileNameAndPath = Paths.get(uploadPath, file[i].getOriginalFilename());
				fileNames.append(file[i].getOriginalFilename());
				Files.write(fileNameAndPath, file[i].getBytes());
			}
			model.addAttribute("msg", "Uploaded images: " + fileNames.toString());
		} catch (Exception e) {
			logger.error("Exception in SVPNPAService::uploadImage1::" + e.getMessage());
			logger.error("Exception in SVPNPAService::uploadImage1::" + e.getCause());
			logger.error("Exception in SVPNPAService::uploadImage1::" + e.getClass() + "::" + e.getLocalizedMessage());
		} finally {
		}
		return "image_test";
	}

	public String getAudio(Model model) {
		try {
			String filename = "";
//		String folderPath = galleryPath + "\\audio\\";
			String folderPath = galleryPath + File.separator + "audio" + File.separator;
			logger.error("SVPNPAService::getAudio::" + folderPath);
			logger.error("SVPNPAService::getAudio::" + folderPath);
			logger.error("SVPNPAService::getAudio::" + folderPath);
			File folder = new File(folderPath);
			File[] listOfFiles = folder.listFiles();
			List<String> audioNames = new ArrayList<>();
			if (listOfFiles != null) {
				for (File file : listOfFiles) {
					if (file.isFile()) {
						filename = file.getName();
						audioNames.add(file.getName());
					}
				}
			}
			model.addAttribute("audioNames", audioNames);
		} catch (Exception e) {
			logger.error("Exception in SVPNPAService::getAudio::" + e.getMessage());
			logger.error("Exception in SVPNPAService::getAudio::" + e.getCause());
			logger.error("Exception in SVPNPAService::getAudio::" + e.getClass() + "::" + e.getLocalizedMessage());
			}

		return "audioUpload";
	}

	public String uploadAudio(Model model, MultipartFile file[], String group_id) throws IOException {
//		String UPLOAD_DIRECTORY = galleryAbsolutePath + "/audio/"; // Production
//		String UPLOAD_DIRECTORY = galleryAbsolutePath + "\\audio\\"; // Staging
		
		String UPLOAD_DIRECTORY = galleryAbsolutePath + File.separator + "audio" + File.separator; // Staging
		String user_identity = "usr", original_filename = "";
		try {
			StringBuilder fileNames = new StringBuilder();
			for (int i = 0; i < file.length; i++) {
				original_filename = file[i].getOriginalFilename();
				String fileExtension = getFileExtension(original_filename); // Get the file extension
// Generate a random UUID as the new file name
				String randomFileName = generateRandomFileName();
// Create the new file name with the random UUID and the original extension
				String newFileName = randomFileName + fileExtension;
				Path filePath = Paths.get(UPLOAD_DIRECTORY + newFileName);
//				System.out.println(filePath + "Path");
				Files.copy(file[i].getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
				int k = dbutils.insertOrUpdate(
						"INSERT INTO gallery(media_type,file_name,thumbnail_filename,path,group_id,user_identity,original_filename) values(?,?,?,?,?,?,?);",
						new Object[] { 8, newFileName, newFileName, UPLOAD_DIRECTORY, group_id, user_identity,
								original_filename });
				}
			
			model.addAttribute("msg", "Uploaded audio: " + fileNames.toString());
		} catch (Exception e) {
			logger.error("Exception in SVPNPAService::uploadAudio::" + e.getMessage());
			logger.error("Exception in SVPNPAService::uploadAudio::" + e.getCause());
			logger.error("Exception in SVPNPAService::uploadAudio::" + e.getClass() + "::" + e.getLocalizedMessage());
			}
		return "redirect:/gallery";
	}

	public String getVideo(Model model) {
		try {
			String filename = "";
//		String folderPath = galleryPath + "\\video\\";
			String folderPath = galleryPath + File.separator + "video" + File.separator;
			logger.error("SVPNPAService::getVideo::" + folderPath);
			File folder = new File(folderPath);
			File[] listOfFiles = folder.listFiles();
			List<String> videoFiles = new ArrayList<>();
			if (listOfFiles != null) {
				for (File file : listOfFiles) {
					if (file.isFile()) {
						filename = file.getName();
						videoFiles.add(file.getName());
					}
				}
			}
			model.addAttribute("videoFiles", videoFiles);
		} catch (Exception e) {
			logger.error("Exception in SVPNPAService::getVideo::" + e.getMessage());
			logger.error("Exception in SVPNPAService::getVideo::" + e.getCause());
			logger.error("Exception in SVPNPAService::getVideo::" + e.getClass() + "::" + e.getLocalizedMessage());
			}
		return "videoUpload";
	}

	public String deleteObject(Map<String, Object> object_json) {
		try {
			dbutils.insertOrUpdate("INSERT INTO deleted_objects select *,now() from objects where object_id=?",
					new Object[] { Integer.parseInt(String.valueOf(object_json.get("object_id"))) });
			dbutils.insertOrUpdate("delete from pages where object_id=?",
					new Object[] { Integer.parseInt(String.valueOf(object_json.get("object_id"))) });
			dbutils.insertOrUpdate("delete from objects where object_id=?",
					new Object[] { Integer.parseInt(String.valueOf(object_json.get("object_id"))) });
		} catch (Exception e) {
			logger.error("Exception in SVPNPAService::deleteObject::" + e.getMessage());
			logger.error("Exception in SVPNPAService::deleteObject::" + e.getCause());
			logger.error("Exception in SVPNPAService::deleteObject::" + e.getClass() + "::" + e.getLocalizedMessage());
			}
		return "success";
	}

	public String publishPage(int page_id) {
		try {
			String condition = "";
			if (role.equals("2")) {
				condition = " verified ";
			} else if (role.equals("3")) {
				//condition = " published ";
				condition = " verified =true, published ";
			} else {
//				condition = " published ";
				condition = " verified =true, published ";
			}
			int k = dbutils.insertOrUpdate("update pages_names set " + condition + "=true where page_id=?",
					new Object[] { page_id });
			String page_name = (String) dbutils.getDBValue("select page_name from pages_names where page_id=?",
					new Object[] { page_id });
//			if (k > 0) {
//				RestTemplate restTemplate = new RestTemplate();
//				String url = "http://localhost:8088/send_email";
//				HttpHeaders headers = new HttpHeaders();
//				headers.setContentType(MediaType.APPLICATION_JSON);
//				String validate = AESEncryption.encrypt("sVPNPA-" + System.currentTimeMillis());
//				String jsonString = "{\"id\":\"" + 1 + "\",\"text\":\"" + page_name + "\",\"validate\":\"" + validate
//						+ "\"}";
//				System.out.println(jsonString);
//				HttpEntity<String> requestEntity = new HttpEntity<>(jsonString, headers);
//				ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity,
//						String.class);
//				String response = responseEntity.getBody();
//				System.out.println("Response from the web service: " + response);
//			}
		} catch (Exception e) {
			logger.error("Exception in SVPNPAService::publishPage::" + e.getMessage());
			logger.error("Exception in SVPNPAService::publishPage::" + e.getCause());
			logger.error("Exception in SVPNPAService::publishPage::" + e.getClass() + "::" + e.getLocalizedMessage());
			}
		return "success";
	}

	public String verifyPage(int page_id) {
		try {
			int k = dbutils.insertOrUpdate("update pages_names set verified=true where page_id=?",
					new Object[] { page_id });
		} catch (Exception e) {
			logger.error("Exception in SVPNPAService::verifyPage::" + e.getMessage());
			logger.error("Exception in SVPNPAService::verifyPage::" + e.getCause());
			logger.error("Exception in SVPNPAService::verifyPage::" + e.getClass() + "::" + e.getLocalizedMessage());
			} finally {

		}
		return "Success";
	}

	public String deletePage(Map<String, Object> page_json) {
		String success = null;
		try {
			dbutils.insertOrUpdate("INSERT INTO deleted_pages select *,now() from pages_names where page_id=?",
					new Object[] { Integer.parseInt(String.valueOf(page_json.get("page_id"))) });
			dbutils.insertOrUpdate("delete from pages where page_id=?",
					new Object[] { Integer.parseInt(String.valueOf(page_json.get("page_id"))) });
			dbutils.insertOrUpdate("delete from pages_names where page_id=?",
					new Object[] { Integer.parseInt(String.valueOf(page_json.get("page_id"))) });
			success = "success";
		} catch (Exception e) {
			logger.error("Exception in SVPNPAService::deletePage::" + e.getMessage());
			logger.error("Exception in SVPNPAService::deletePage::" + e.getCause());
			logger.error("Exception in SVPNPAService::deletePage::" + e.getClass() + "::" + e.getLocalizedMessage());
			}
		return success;
	}

	public String uploadVideo(Model model, MultipartFile file[], String group_id) throws IOException {
//		String UPLOAD_DIRECTORY = galleryAbsolutePath + "/video/"; // Production
//		String UPLOAD_DIRECTORY = galleryAbsolutePath + "\\video\\"; // Staging
		String UPLOAD_DIRECTORY = galleryAbsolutePath + File.separator + "video" + File.separator; // Staging
		
		Connection connection = null;
		String user_identity = "usr", original_filename = "";
		try {
			connection = dataSource.getConnection();
			StringBuilder fileNames = new StringBuilder();
			for (int i = 0; i < file.length; i++) {
				original_filename = file[i].getOriginalFilename();
				String fileExtension = getFileExtension(original_filename); // Get the file extension
// Generate a random UUID as the new file name
				String randomFileName = generateRandomFileName();
// Create the new file name with the random UUID and the original extension
				String newFileName = randomFileName + fileExtension;
				Path filePath = Paths.get(UPLOAD_DIRECTORY + newFileName);
				Files.copy(file[i].getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
				int k = dbutils.insertOrUpdate(
						"INSERT INTO gallery(media_type,file_name,thumbnail_filename,path,group_id,user_identity,original_filename) values(?,?,?,?,?,?,?);",
						new Object[] { 7, newFileName, newFileName, UPLOAD_DIRECTORY, group_id, user_identity,
								original_filename });
				}
			model.addAttribute("msg", "Uploaded videos: " + fileNames.toString());
		} catch (Exception e) {
			logger.error("Exception in SVPNPAService::uploadVideo::" + e.getMessage());
			logger.error("Exception in SVPNPAService::uploadVideo::" + e.getCause());
			logger.error("Exception in SVPNPAService::uploadVideo::" + e.getClass() + "::" + e.getLocalizedMessage());
			} finally {
			try {
				connection.close();
			} catch (SQLException e) {				
			}
		}
		return "redirect:/gallery";
	}

	private static String generateRandomFileName() {
		String uuid = UUID.randomUUID().toString().replace("-", "");
		return uuid;
	}

	private static String getFileExtension(String fileName) {
		try {
			int dotIndex = fileName.lastIndexOf(".");
			if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
				return fileName.substring(dotIndex);
			}
		} catch (Exception e) {
			logger.error("Exception in SVPNPAService::getFileExtension::" + e.getMessage());
			logger.error("Exception in SVPNPAService::getFileExtension::" + e.getCause());
			logger.error("Exception in SVPNPAService::getFileExtension::" + e.getClass() + "::" + e.getLocalizedMessage());
			logger.info("Exception in SVPNPAService::getFileExtension::" + e.getMessage());
			logger.info("Exception in SVPNPAService::getFileExtension::" + e.getCause());
			logger.info("Exception in SVPNPAService::getFileExtension::" + e.getClass() + "::" + e.getLocalizedMessage());
			}
		return "";
	}

	public String uploadDoc(Model model, MultipartFile file[], String[] file_desc, String group_id) throws IOException {
//		String UPLOAD_DIRECTORY = galleryAbsolutePath + "/docs/"; //  Production
//		String UPLOAD_DIRECTORY = galleryAbsolutePath + "\\docs\\"; // Local System
		String UPLOAD_DIRECTORY = galleryAbsolutePath + File.separator + "docs" + File.separator; // Local System
		
		logger.error("SVPNPAService::uploadDoc::" + UPLOAD_DIRECTORY);
		
		Connection connection = null;
		String user_identity = "usr", original_filename = "";
		try {
			connection = dataSource.getConnection();
			StringBuilder fileNames = new StringBuilder();
			int length = file.length;
			JSONObject reqJson = new JSONObject();
			for (int i = 0; i < file.length; i++) {
				original_filename = file[i].getOriginalFilename();
				String fileExtension = getFileExtension(original_filename); // Get the file extension
				String randomFileName = generateRandomFileName();
				String newFileName = randomFileName + fileExtension;
				Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, newFileName);
				fileNames.append(file[i].getOriginalFilename());
				Files.write(fileNameAndPath, file[i].getBytes());
				reqJson.put("description", file_desc[i]);
				String rq2 = reqJson.toString();
				int k = dbutils.insertOrUpdate(
						"INSERT INTO gallery(media_type,file_name,thumbnail_filename,path,group_id,user_identity,original_filename,properties) values(?,?,?,?,?,?,?,to_json(?::json));",
						new Object[] { 9, newFileName, newFileName, UPLOAD_DIRECTORY, group_id, user_identity,
								original_filename, rq2 });
				
				logger.error("SVPNPAService::uploadDoc::" + k);
				}
			model.addAttribute("msg", "Uploaded docs: " + fileNames.toString());
		} catch (Exception e) {			
			logger.error("Exception in SVPNPAService::uploadDoc::" + e.getMessage());
			logger.error("Exception in SVPNPAService::uploadDoc::" + e.getCause());
			logger.error("Exception in SVPNPAService::uploadDoc::" + e.getClass() + "::" + e.getLocalizedMessage());
			e.printStackTrace();
			} finally {
			try {
				connection.close();
			} catch (SQLException e) {
			}
		}
		return "gallery";
	}

	public String[][] getindividualobject(Map<String, Object> typeMap) {
		String[][] items = null;
		try {
			Long countLong = (long) dbutils.getDBValue(
					"SELECT coalesce(count(object_id),0) FROM objects where  object_id=?",
					new Object[] { (Integer.parseInt((String) typeMap.get("objectid"))) });
			int count = Integer.parseInt(countLong.toString());
			String template_data = String.valueOf(dbutils.getDBValue(
					"SELECT template_data FROM templates where template_id=(select template_id from pages_names where page_id=?)",
					new Object[] { (page_id) }));
			StringBuilder viewObject = new StringBuilder(template_data);
			output = new StringBuilder[count];
			items = new String[count][2];
			ArrayList<HashMap<String, Object>> objects = dbutils.getListMap(
					"select * from objects where object_id=? and lang=? order by lang",
					new Object[] { Integer.parseInt((String) typeMap.get("objectid")), lang });
			Gson gson = new Gson();
			int i = 0;
			StringBuilder marquee = null;
			for (Map map : objects) {
				String type = Integer.toString((int) map.get("object_category"));
				if (type.equals("1")) {
					String data = String.valueOf(map.get("data"));
					data = data.replaceAll("mvnr", "'");
					String[][] userArray = gson.fromJson(data.substring(1, data.length() - 1), String[][].class);
					output[i] = parseTableData(String.valueOf(map.get("object_title")), userArray,
							(String) map.get("styles"), (String) map.get("classes"));
				} else if (type.equals("3")) {
					String data = String.valueOf(map.get("data"));
					data = data.replaceAll("mvnr", "'");
					String[] userArray = gson.fromJson(data.substring(1, data.length() - 1), String[].class);
					String linksData = String.valueOf(map.get("hyperlinks"));
					String[] linksArray = null;
					if (linksData != null) {
						linksArray = gson.fromJson(linksData.substring(1, linksData.length() - 1), String[].class);
					}
					String expData = String.valueOf(map.get("expiration"));
					String[] expArray = null;
					if (expData != null) {
						expArray = gson.fromJson(expData.substring(1, expData.length() - 1), String[].class);
					}
					String property[] = ((String) map.get("properties")).toString().split(",");
					if (property.length > 1) {
						if (property[1].equals("marquee"))
							marquee = new StringBuilder(
									"<h5 class='text-danger fw-bold text-center'>" + (String) map.get("object_title")
											+ "</h5><marquee direction='" + property[2] + "'>"
											+ parseListData(userArray, linksArray, expArray, property[0],
													(String) map.get("styles"), (String) map.get("classes"))
											+ "</marquee>");
						else if (property[1].equals("marquee"))
							marquee = new StringBuilder(
									"<h5  class='text-danger fw-bold text-center'>" + (String) map.get("object_title")
											+ "</h5><marquee direction='" + property[2] + "'>"
											+ parseListData(userArray, linksArray, expArray, property[0],
													(String) map.get("styles"), (String) map.get("classes"))
											+ "</marquee>");
						else
							marquee = new StringBuilder(
									new StringBuffer("<h5  class='text-danger fw-bold text-center'>")
											.append((String) map.get("object_title")).append("</h5>"))
									.append(parseListData(userArray, linksArray, expArray, property[0],
											(String) map.get("styles"), (String) map.get("classes")));
					} else
						marquee = new StringBuilder(
								"<div class=\"card-body border border-2 border-success rounded-5 important_links\">")
								.append(parseListData(userArray, linksArray, expArray, property[0],
										String.valueOf(map.get("styles")), String.valueOf(map.get("classes"))))
								.append("" + "                  </div>");
					output[i] = marquee;
				} else if (type.equals("6") || type.equals("10")) {
					String data;
					String encd_message = String.valueOf(map.get("data"));
					encd_message = encd_message.replaceAll("mvnr", "'");					
					data = new String(URLDecoder.decode(encd_message.substring(2, encd_message.length() - 2),
							StandardCharsets.UTF_8.name()));
					output[i] = new StringBuilder(data);
				} else if (type.equals("5")) {
					String data = String.valueOf(map.get("data"));
					data = data.replaceAll("mvnr", "'");
					String[] userArray = gson.fromJson(data.substring(1, data.length() - 1), String[].class);
					String property[] = String.valueOf(map.get("properties")).toString().split(",");
					output[i] = parseImageCarousel(userArray, i, userArray[0]);
				} else if (type.equals("2")) {
					String data = String.valueOf(map.get("data"));
					data = data.replaceAll("mvnr", "'");
					String[] userArray = gson.fromJson(data.substring(1, data.length() - 1), String[].class);
					output[i] = parseImages(userArray, i);
				} else if (type.equals("7")) {
					String data = String.valueOf(map.get("data"));
					data = data.replaceAll("mvnr", "'");
					String[] userArray = gson.fromJson(data.substring(1, data.length() - 1), String[].class);
					output[i] = parseVideos(userArray, i);
				} else if (type.equals("8")) {
					String data = String.valueOf(map.get("data"));
					data = data.replaceAll("mvnr", "'");
					String[] userArray = gson.fromJson(data.substring(1, data.length() - 1), String[].class);
					output[i] = parseAudios(userArray, i);
				} else if (type.equals("9")) {
					String data = String.valueOf(map.get("data"));
					data = data.replaceAll("mvnr", "'");
					String[] userArray = gson.fromJson(data.substring(1, data.length() - 1), String[].class);
					output[i] = parseDocs(userArray, i);
				}
				items[i][1] = Integer.toString((int) map.get("object_id"));
				items[i][0] = output[i].toString();
				i++;
			}
		} catch (Exception e) {
			logger.error("Exception in SVPNPAService::getindividualobject::" + e.getMessage());
			logger.error("Exception in SVPNPAService::getindividualobject::" + e.getCause());
			logger.error("Exception in SVPNPAService::getindividualobject::" + e.getClass() + "::"+ e.getLocalizedMessage());
			}
		return items;
	}

	public ArrayList<HashMap<String, Object>> getListPages() {
		ArrayList<HashMap<String, Object>> pages = dbutils.getListMap(
				"select page_id,page_name,case when published then 'Published' else 'Not Published' end as status,case when published then concat('/Preview?page_id=',page_id) else '' end as Link from pages_names order by page_id",
				null);
		return pages;
	}

	public ArrayList<HashMap<String, Object>> searchUniversal(String value) {
		ArrayList<HashMap<String, Object>> pages = null;
		try {
			pages = dbutils.getListMap("SELECT objects.*,regexp_replace(urldecode_arr(data::varchar),\r\n"
					+ "        '(\\y" + value + "\\y)',\r\n" + "        '<span class=\"highlight\">\\1</span>',\r\n"
					+ "        'gi'\r\n"
					+ "    ) AS highlighted_content,pages_names.page_name,concat('Preview?page_id=',pages_names.page_id) as link\r\n"
					+ "FROM\r\n" + "    objects,pages,pages_names\r\n"
					+ "WHERE objects.object_id=pages.object_id and pages_names.page_id=pages.page_id and pages_names.published=true and\r\n"
					+ "    urldecode_arr(data::varchar) ILIKE '%" + value + "%';", null);
			for (HashMap<String, Object> list : pages) {
//				list.put("data", list.get("highlighted_content"));
				String retrieved = object_retrieval(list).toString();
				list.put("highlighted_content",
						retrieved.replaceAll(value, "<span class=\"highlight\">" + value + "</span>"));
			}
		} catch (Exception e) {
			logger.error("Exception in SVPNPAService::getListPages::" + e.getMessage());
			logger.error("Exception in SVPNPAService::getListPages::" + e.getCause());
			logger.error("Exception in SVPNPAService::getListPages::" + e.getClass() + "::" + e.getLocalizedMessage());
			}

		return pages;
	}

	public ArrayList<HashMap<String, Object>> getListForms() {
		ArrayList<HashMap<String, Object>> forms = null;
		try {
			forms = dbutils.getListMap(
//					"select form_id,form_name,case when (approve_status='A' and verify_status='A') then 'Approved' else 'Not Approved' end as verify_status,case when verify_status='A' then concat('/getDynamicForm?form_id=',form_id) else '' end as Link from generic_forms order by form_id",
					"select form_id,form_name,case when (approve_status='A' and verify_status='A') then 'Approved' else 'Not Approved' end as verify_status,case when verify_status='A' then concat('/getForm?form_id=',form_id) else '' end as Link from generic_forms order by form_id",
					null);
		} catch (Exception e) {
			logger.error("Exception in SVPNPAService::getListForms::" + e.getMessage());
			logger.error("Exception in SVPNPAService::getListForms::" + e.getCause());
			logger.error("Exception in SVPNPAService::getListForms::" + e.getClass() + "::" + e.getLocalizedMessage());
			}
		return forms;
	}

	public ArrayList<HashMap<String, Object>> getListDocs() {
		ArrayList<HashMap<String, Object>> docs = null;
		try {
			docs = dbutils.getListMap(
					"select concat('/gallery/docs/',file_name) as file,original_filename,properties->>'description' as description from gallery where media_type='9'",
					null);
		} catch (Exception e) {
			logger.error("Exception in SVPNPAService::getListDocs::" + e.getMessage());
			logger.error("Exception in SVPNPAService::getListDocs::" + e.getCause());
			logger.error("Exception in SVPNPAService::getListDocs::" + e.getClass() + "::" + e.getLocalizedMessage());
			}
		return docs;
	}

	public String getRole(String userName) {
		String role = "";
		try {
			role = String.valueOf(dbutils.getDBValue(
					"select distinct b.role_id from users a left join user_roles b on a.id=b.user_id where a.username=?",
					new Object[] { userName }));
		} catch (Exception e) {
			logger.error("Exception in SVPNPAService::getRole::" + e.getMessage());
			logger.error("Exception in SVPNPAService::getRole::" + e.getCause());
			logger.error("Exception in SVPNPAService::getRole::" + e.getClass() + "::" + e.getLocalizedMessage());
			}
		return role;
	}

	public int approveObject(int object_id, String role) {
		int updatedCount = 0;
		try {
//			if (role.equals("2")) {
//				updatedCount = dbutils.insertOrUpdate("update objects set verified=true where object_id=?",
//						new Object[] { object_id });
//			} else {
//				return 0;
//			}
		updatedCount = dbutils.insertOrUpdate("update objects set verified=true where object_id=?",new Object[] { object_id });
		} catch (Exception e) {
			logger.error("Exception in SVPNPAService::approveObject::" + e.getMessage());
			logger.error("Exception in SVPNPAService::approveObject::" + e.getCause());
			logger.error("Exception in SVPNPAService::approveObject::" + e.getClass() + "::" + e.getLocalizedMessage());
			return 0;
			}
		System.out.println("updatedCount==="+updatedCount);
		return updatedCount;
	}
	
	public ArrayList<HashMap<String, String>> getFileNames(Map<String, Object> fileType) {
		String folderPath = "",folderPath1 = "",ssr = "";
		ArrayList<HashMap<String, String>> files = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> itemsList = new HashMap<String, String>();
		try {
			if(fileType.get("filetype").equals("4")) {
				  folderPath = galleryAbsolutePath + File.separator + "video" + File.separator;
				  folderPath1 = galleryPath + File.separator + "video" + File.separator;
				  ssr = "fas fa-file-video";
				}else if(fileType.get("filetype").equals("3")) {
				  folderPath = galleryAbsolutePath + File.separator + "audio" + File.separator;
				  folderPath1 = galleryPath + File.separator + "audio" + File.separator;
				  ssr = "fas fa-file-audio";
				}else if(fileType.get("filetype").equals("2")) {
				  folderPath = galleryAbsolutePath + File.separator + "docs" + File.separator;
				  folderPath1 = galleryPath + File.separator + "docs" + File.separator;
				  ssr = "fas fa-file-pdf";
				}else if(fileType.get("filetype").equals("1")) {
				  folderPath = galleryAbsolutePath + File.separator + "image" + File.separator;
				  folderPath1 = galleryPath + File.separator + "image" + File.separator;
				  ssr = "fas fa-image";
				}
		
			String table = "";int i = 1;
			try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(folderPath))) {
	            for (Path path : directoryStream) {
				    table = table + "<div class='row g-1'>"
						          + "<div class='col-2'>"+i+"</div>"
						          + "<div class='col-1 fileIcon'><a href='"+folderPath1+path.getFileName().toString()+"' target='_blank' title='Click to view'><i class='"+ssr+"' style='color:red;'></i></a></div>"
						          + "<div class='col-8 fileName'>"+path.getFileName().toString()+"</div>"
						          + "<div class='col-1'>"
						          + "<i class='fas fa-copy' onclick='CopyFileName(this)' data-bs-toggle='tooltip' data-bs-placement='top' title='Copy to Clipboard'></i>"
						          + "</a>"
						          + "</div></div>\n";
	            i++;
	            }
	            itemsList.put("filename",table);
                files.add(itemsList);	
	            
	        } catch (IOException e) {
	        }
		} catch (Exception e) {
			logger.error("Exception in SVPNPAService::getFileNames::" + e.getMessage());
			logger.error("Exception in SVPNPAService::getFileNames::" + e.getCause());
			logger.error("Exception in SVPNPAService::getFileNames::" + e.getClass() + "::" + e.getLocalizedMessage());
		}
		return files;
	} 

	
	public int updateImageDes(String[] jsonString, String description) {
			String object_id_from_db = null;
		int i = 0, count = 0, count_hi = 0;
		JSONObject reqJson = new JSONObject();
		logger.info("updateImageDes=========");
				try {
				//String imgarray[] = jsonString.split(",");
				String desc[] = description.split("##");
				for(int j = 0;j<jsonString.length;j++) {
					String ab[] = jsonString[j].split("##");
					
//------------Newly-Added-Code------------------------------------------				
					String fdc = "",rq2 = "";
					String[] mnr;				
					try {
						fdc = desc[j];
						fdc = fdc.replace("\\", "");
						mnr = fdc.split("&");
						reqJson.put("description", mnr[0]);
						reqJson.put("title", mnr[1]);					
						
						ObjectNode newJsonObject = null;
						ObjectMapper objectMapper = new ObjectMapper();
						
						newJsonObject = (ObjectNode) objectMapper.readTree(reqJson.toString());
			            rq2 = newJsonObject.toString();
						
					}catch(Exception e) {					
					 reqJson.put("description", desc[j]);
					 rq2 = reqJson.toString();
					}		
					System.out.println("rq2==="+rq2.toString());
//-----------------------------------------------------------					
				count = JdbcWrite.update("update gallery set properties=?::json where seq_id=? and file_name=?",new Object[] { rq2 ,Integer.parseInt(ab[1]), ab[0] });				
				System.out.println("count==="+count);
			
			logger.info("count==="+count);
				}
			if(count>0) {
				i = 1;
			}
		} catch (Exception e) {
		 	e.printStackTrace();
			logger.error("Exception in SVPNPAService::updateImageDes::" + e.getMessage());
			logger.error("Exception in SVPNPAService::updateImageDes::" + e.getCause());
			logger.error("Exception in SVPNPAService::updateImageDes::" + e.getClass() + "::" + e.getLocalizedMessage());
		}
		logger.info("updateImageDes===i====="+i);
		return i;
	}
	
	public String archivePage(Map<String, Object> page_json) {
		String success = null;
		try {			
			dbutils.insertOrUpdate("update pages_names set archive=true where page_id=?",
					new Object[] { Integer.parseInt(String.valueOf(page_json.get("page_id"))) });
			success = "success";
		} catch (Exception e) {
			logger.error("Exception in SVPNPAService::archivePage::" + e.getMessage());
			logger.error("Exception in SVPNPAService::archivePage::" + e.getCause());
			logger.error("Exception in SVPNPAService::archivePage::" + e.getClass() + "::" + e.getLocalizedMessage());
			}
		return success;
	}
	public String unarchivePage(Map<String, Object> page_json) {
		String success = null;
		try {			
			dbutils.insertOrUpdate("update pages_names set archive=false where page_id=?",
					new Object[] { Integer.parseInt(String.valueOf(page_json.get("page_id"))) });
			success = "success";
		} catch (Exception e) {
			logger.error("Exception in SVPNPAService::unarchivePage::" + e.getMessage());
			logger.error("Exception in SVPNPAService::unarchivePage::" + e.getCause());
			logger.error("Exception in SVPNPAService::unarchivePage::" + e.getClass() + "::" + e.getLocalizedMessage());
			}
		return success;
	}

	public int deleteUser(String username, String id) {
		int count = 0;
		try {
			count = dbutils.insertOrUpdate("update users set status='D' where TRIM(username) = ? and id = ?",new Object[] { username.trim(),Integer.parseInt(id) });
		} catch (Exception e) {
			logger.error("Exception in SVPNPAService::deleteUser::" + e.getMessage());
			logger.error("Exception in SVPNPAService::deleteUser::" + e.getCause());
			logger.error("Exception in SVPNPAService::deleteUser::" + e.getClass() + "::" + e.getLocalizedMessage());
			return 0;
			}
		System.out.println("count==="+count);
		return count;
	}

	public Map<String, Object> checkGalleryFolderUsage(String groupId, Integer mediaType) {
		Map<String, Object> result = new HashMap<>();
		List<Map<String, Object>> usedInPages = new ArrayList<>();
		List<String> usedFiles = new ArrayList<>();
		boolean hasUsage = false;

		try {
			ArrayList<HashMap<String, Object>> galleryFiles = dbutils.getListMap(
				"SELECT file_name FROM gallery WHERE group_id = ? AND media_type = ?",
				new Object[] { groupId, mediaType }
			);

			if (galleryFiles.isEmpty()) {
				result.put("hasUsage", false);
				result.put("message", "No files found in this folder");
				result.put("usedInPages", usedInPages);
				result.put("usedFiles", usedFiles);
				return result;
			}

			List<String> fileNames = galleryFiles.stream()
				.map(f -> (String) f.get("file_name"))
				.filter(Objects::nonNull)
				.collect(Collectors.toList());

			if (fileNames.isEmpty()) {
				result.put("hasUsage", false);
				result.put("message", "No valid filenames in folder");
				result.put("usedInPages", usedInPages);
				result.put("usedFiles", usedFiles);
				return result;
			}

			String placeholders = fileNames.stream().map(f -> "?").collect(Collectors.joining(","));
			String objectCategory = (mediaType == 2) ? "2" : (mediaType == 4 ? "7" : (mediaType == 3 ? "8" : "9"));
			if (mediaType == 2) objectCategory = "2,5";

			String sql = "SELECT o.object_id, o.object_title, o.object_category, o.data, o.lang " +
				"FROM objects o " +
				"WHERE o.object_category IN (" + objectCategory + ") " +
				"AND o.data IS NOT NULL";

			ArrayList<HashMap<String, Object>> objects = dbutils.getListMap(sql, null);

			Gson gson = new Gson();
			for (Map<String, Object> obj : objects) {
				String data = String.valueOf(obj.get("data"));
				if (data == null || data.trim().isEmpty()) continue;

				data = data.replace("mvnr", "'");
				try {
					String[] fileArray = gson.fromJson(data.substring(1, data.length() - 1), String[].class);
					for (String fileName : fileArray) {
						if (fileNames.contains(fileName)) {
							hasUsage = true;
							if (!usedFiles.contains(fileName)) {
								usedFiles.add(fileName);
							}

							Integer objId = (Integer) obj.get("object_id");
							ArrayList<HashMap<String, Object>> pages = dbutils.getListMap(
								"SELECT p.page_id, pn.page_name, pn.template_id " +
								"FROM pages p " +
								"JOIN pages_names pn ON p.page_id = pn.page_id " +
								"WHERE p.object_id = ?",
								new Object[] { objId }
							);

							for (Map<String, Object> page : pages) {
								Map<String, Object> pageInfo = new HashMap<>();
								pageInfo.put("page_id", page.get("page_id"));
								pageInfo.put("page_name", page.get("page_name"));
								pageInfo.put("object_id", objId);
								pageInfo.put("object_title", obj.get("object_title"));
								pageInfo.put("object_category", obj.get("object_category"));
								pageInfo.put("lang", obj.get("lang"));
								pageInfo.put("file_name", fileName);
								usedInPages.add(pageInfo);
							}
						}
					}
				} catch (Exception e) {
					logger.error("Error parsing object data: " + e.getMessage());
				}
			}

			result.put("hasUsage", hasUsage);
			result.put("usedInPages", usedInPages);
			result.put("usedFiles", usedFiles);
			result.put("totalFilesInFolder", fileNames.size());
			result.put("message", hasUsage ? 
				"Folder contains " + usedFiles.size() + " file(s) used in " + usedInPages.size() + " page placement(s)" :
				"Folder is not used in any pages");

		} catch (Exception e) {
			logger.error("Exception in SVPNPAService::checkGalleryFolderUsage::" + e.getMessage());
			logger.error("Exception in SVPNPAService::checkGalleryFolderUsage::" + e.getCause());
			logger.error("Exception in SVPNPAService::checkGalleryFolderUsage::" + e.getClass() + "::" + e.getLocalizedMessage());
			result.put("hasUsage", false);
			result.put("error", e.getMessage());
		}
		return result;
	}

	public Map<String, Object> deleteGalleryFolder(String groupId, Integer mediaType, String username, String clientIp) {
		Map<String, Object> result = new HashMap<>();
		try {
			Map<String, Object> usageCheck = checkGalleryFolderUsage(groupId, mediaType);
			if ((Boolean) usageCheck.get("hasUsage")) {
				result.put("success", false);
				result.put("message", "Cannot delete: folder is in use");
				result.put("usageDetails", usageCheck);
				return result;
			}
			
			// 1. Fetch files to be deleted for logging
			ArrayList<HashMap<String, Object>> filesToDelete = dbutils.getListMap(
				"SELECT media_type, file_name, path, thumbnail_filename, group_id, original_filename " +
				"FROM gallery WHERE group_id = ? AND media_type = ?",
				new Object[] { groupId, mediaType }
			);
			
			// 2. Log each file to gallery_deleted_log before deletion
			if (!filesToDelete.isEmpty()) {
				for (Map<String, Object> file : filesToDelete) {
					dbutils.insertOrUpdate(
						"INSERT INTO gallery_deleted_log (media_type, file_name, path, thumbnail_filename, group_id, original_filename, deleted_timestamp, deleted_by, deleted_ip) " +
						"VALUES (?, ?, ?, ?, ?, ?, NOW(), ?, ?)",
						new Object[] {
							file.get("media_type"),
							file.get("file_name"),
							file.get("path"),
							file.get("thumbnail_filename"),
							file.get("group_id"),
							file.get("original_filename"),
							username != null && !username.isEmpty() ? username : "system",
							clientIp != null ? clientIp : "unknown"
						}
					);
				}
			}
			
			// 3. Delete from gallery table
			int deletedFiles = dbutils.insertOrUpdate(
				"DELETE FROM gallery WHERE group_id = ? AND media_type = ?",
				new Object[] { groupId, mediaType }
			);

			String folderPath = "";
			if (mediaType == 2) folderPath = galleryAbsolutePath + File.separator + "image" + File.separator;
			else if (mediaType == 3) folderPath = galleryAbsolutePath + File.separator + "audio" + File.separator;
			else if (mediaType == 4) folderPath = galleryAbsolutePath + File.separator + "video" + File.separator;
			else if (mediaType == 5) folderPath = galleryAbsolutePath + File.separator + "docs" + File.separator;

			File folder = new File(folderPath);
			if (folder.exists() && folder.isDirectory()) {
				File[] files = folder.listFiles();
				if (files != null) {
					for (File file : files) {
						file.delete();
					}
				}
			}

			result.put("success", true);
			result.put("message", "Folder deleted successfully. " + deletedFiles + " database records removed and logged.");
			result.put("deletedRecords", deletedFiles);
			result.put("loggedRecords", filesToDelete.size());

		} catch (Exception e) {
			logger.error("Exception in SVPNPAService::deleteGalleryFolder::" + e.getMessage());
			logger.error("Exception in SVPNPAService::deleteGalleryFolder::" + e.getCause());
			logger.error("Exception in SVPNPAService::deleteGalleryFolder::" + e.getClass() + "::" + e.getLocalizedMessage());
			result.put("success", false);
			result.put("message", "Error deleting folder: " + e.getMessage());
		}
		return result;
	}

}