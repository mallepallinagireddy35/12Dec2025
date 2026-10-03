package com.nic.svpnpa.dao;

import java.awt.image.BufferedImage;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;

import jakarta.servlet.http.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.JsonObject;
import com.nic.svpnpa.controller.LoginUserDetailsController;
import com.nic.svpnpa.model.CommonBean;
import com.nic.svpnpa.model.JsonBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Repository
public class GenericDaoImpl implements GenericDao {

	private static final Logger logger = LogManager.getLogger(GenericDaoImpl.class);
	@Autowired
	private JdbcTemplate JdbcWrite;

	@Autowired
	private JdbcTemplate JdbcRead;

	@Autowired
	LoginUserDetailsController loginUSerController;

	@Value("${formPath}")
	String formPath;

	String stateCode = "", distCode = "", mandalCode = "", villCode = "";

	@Override
	public int generateForm(JsonBean bean) {
		int i = 0, j = 0, form_id = 0;
		String form_name = bean.getForm_name().replaceAll("\\s", "");
		String jsonString = bean.getJsonData().replace("\\", "");
		SimpleDateFormat dff = new SimpleDateFormat("yyyy-MM-dd");

		try {
			UserDetails userDetails = loginUSerController.getUserDetails();
			String username = userDetails.getUsername();

			String tab = "CREATE TABLE IF NOT EXISTS Generic_Forms (form_id integer,form_name varchar(30),fields integer,jsonString varchar(5000),from_date date,to_date date,con_check varchar(100),approve_status character varying(10),captcha char(1),otp_required char(1) DEFAULT 'N',stucture_role character varying(20),timestamp timestamptz  NOT NULL DEFAULT NOW(),update_structure_role character varying(20),update_timestamp timestamp,verify_role character varying(20),verify_status character varying(10),verify_timestamp timestamp,approve_role character varying(20),remark character varying(100),approve_timestamp timestamp,expiry_role character varying(20),expiry_timestamp timestamp,del_role character varying(20),del_timestamp timestamp)";
			j = JdbcWrite.update(tab);
			Date from_date = dff.parse(bean.getFrom_date());
			Date to_date = dff.parse(bean.getTo_date());
			form_id = JdbcWrite.queryForObject("select distinct COALESCE (max(form_id+1),'1') from generic_forms",
					Integer.class);
			String sql = "insert into Generic_Forms (form_id,form_name,fields,jsonString,verify_status,from_date,to_date,captcha,stucture_role,con_check,approve_status,otp_required) values(?,?,?,?,'P',?,?,?,?,?,'P',?)";
			i = JdbcWrite.update(sql,
					new Object[] { form_id, form_name.toUpperCase().trim(), Integer.parseInt(bean.getFields().trim()),
							jsonString.toString().trim(), from_date, to_date, bean.getCaptcha(), username,
							bean.getCon_value(),bean.getSms() });
		} catch (Exception e) {
			logger.error("Exception in GenericDaoImpl::generateForm::" + e.getMessage());
			logger.error("Exception in GenericDaoImpl::generateForm::" + e.getCause());
			logger.error("Exception in GenericDaoImpl::generateForm::" + e.getClass() + "::" + e.getLocalizedMessage());
		}
		return i;
	}

	@Override
	public List Retrieving_Json_Values(String form_name) throws Exception {
		List list = new ArrayList();
		try {

			list = (List) JdbcRead.queryForObject("select *from generic_table where text=?", new Object[] { form_name },
					new RowMapper<JsonBean>() {
						public JsonBean mapRow(ResultSet rs, int row) throws SQLException {
							JsonBean jb = new JsonBean();
							jb.setText(rs.getString("text"));
							jb.setMulticolumn(rs.getString("multicolumn"));
							jb.setRadiobutton(rs.getString("radiobutton"));
							jb.setCheckbox(rs.getString("checkbox"));
							jb.setDate(rs.getString("date"));
							jb.setPassword(rs.getString("password"));
							jb.setEmail(rs.getString("email"));
							jb.setTextarea(rs.getString("textarea"));
							jb.setFileupload(rs.getString("fileupload").toString());
							return jb;
						}
					});
		} catch (Exception e) {
			logger.error("Exception in GenericDaoImpl::Retrieving_Json_Values::" + e.getMessage());
			logger.error("Exception in GenericDaoImpl::Retrieving_Json_Values::" + e.getCause());
			logger.error("Exception in GenericDaoImpl::Retrieving_Json_Values::" + e.getClass() + "::"
					+ e.getLocalizedMessage());
		}
		return list;

	}

	@Override
	public int insertFormData(JSONArray array, JSONObject obj, String form_name, JsonBean bean) throws Exception {
		int m = 0, j = 0, kk = 0, form_id = 0, record_id = 0, column_id = 1;
		String username = "";
		try {
			String val = "";
			UserDetails userDetails = loginUSerController.getUserDetails();
			try {
				username = userDetails.getUsername();
				}catch(Exception ex) {}
			List<Object[]> inputList = new ArrayList<Object[]>();
			String tab = "CREATE TABLE IF NOT EXISTS generic_data (form_id integer,record_id integer,column_id integer,column_name VARCHAR(50),column_value varchar(5000),login_name varchar(50),timestamp timestamptz  NOT NULL DEFAULT NOW(),update_login_role character varying(20),update_timestamp timestamp,del_role character varying(20),del_timestamp timestamp,status char(1))";
			j = JdbcWrite.update(tab);

			String tab1 = "CREATE TABLE IF NOT EXISTS generic_data_log (form_id integer,record_id integer,jsonData varchar(5000),login_name varchar(50),timestamp timestamptz  NOT NULL DEFAULT NOW(),update_jsonData varchar(5000),update_login_role character varying(20),update_timestamp timestamp,del_role character varying(20),del_timestamp timestamp)";
			kk = JdbcWrite.update(tab1);

			record_id = JdbcWrite
					.queryForObject("select distinct COALESCE (max(record_id+1),'1') from generic_data where form_id = "
							+ bean.getForm_id() + "", Integer.class);

			String con_check = JdbcWrite.queryForObject(
					"select distinct con_check from generic_forms where form_id = " + bean.getForm_id() + "",
					String.class);
			StringTokenizer st = new StringTokenizer(con_check, "||");
			String ccheck[] = new String[st.countTokens()];
			int yy = 0;
			while (st.hasMoreTokens()) {
				ccheck[yy] = st.nextToken();
				yy++;
			}
			int count = 0;
			for (int i = 0; i < array.size(); i++) {
				JSONObject jsonObject1 = (JSONObject) array.get(i);
				String dtype = (String) jsonObject1.get("dtype");
				for (int jj = 0; jj < ccheck.length; jj++) {
					int cnt = 0;
					if (dtype.equalsIgnoreCase(ccheck[jj])) {
						cnt = JdbcRead.queryForObject("select count(*) from generic_data where form_id="
								+ bean.getForm_id() + " and column_value='" + (String) obj.get((String) jsonObject1.get("label")) + "'", Integer.class);
						if (cnt > 0) {
							count++;
						}
					}
				}
			}
			if (count == ccheck.length) {
				m = 200;
			} else {
				String qry = "";
				int y = 0;
				for (int i = 0; i < array.size(); i++) {
					JSONObject jsonObject1 = (JSONObject) array.get(i);
					String dtype = (String) jsonObject1.get("dtype");
					qry = "insert into generic_data(form_id,record_id,column_id,column_name,column_value,login_name,status)values("
							+ bean.getForm_id() + "," + record_id + ",?,?,?,'" + username + "','A')";
					if (dtype.equalsIgnoreCase("file")) {
						String tempfile = "test";
						MultipartFile multipartFile = null;
						if (y == 0) {
							multipartFile = bean.getFile0();
						} else if (y == 1) {
							multipartFile = bean.getFile1();
						} else if (y == 2) {
							multipartFile = bean.getFile2();
						} else if (y == 3) {
							multipartFile = bean.getFile3();
						}
//                	List <MultipartFile> list = bean.getFile0();
//                	multipartFile = list.get(y);
						String filename = multipartFile.getOriginalFilename();
						if (!filename.equalsIgnoreCase("")) {
							int index = filename.lastIndexOf('.');
							String extension = "";
							if (index > 0) {
								extension = filename.substring(index + 1);
							}
							File path = null;
							BufferedImage image = null;
							path = new File(formPath + File.separator + form_name);
//                	path = new File("C:\\u01\\app\\" + File.separator + tempfile + File.separator + form_name + File.separator + System.nanoTime() + "." + extension);
							if (path.exists()) {
//                    	path.delete();
//                    	path.mkdir();
							} else {
								path.mkdir();
							}
							File path1 = new File(formPath + File.separator + form_name + File.separator
									+ System.nanoTime() + "." + extension);
//                  ImageIO.write(image, "jpg", path);                	
							Object[] tmp1 = { column_id, (String) jsonObject1.get("label"), path1.toString() };
							inputList.add(tmp1);
							if (y == 0) {
								bean.getFile0().transferTo(path1);
								
//								String path2 = formPath + File.separator + form_name;
//								String newFileName = System.nanoTime() + "." + extension;
//								Path fileNameAndPath = Paths.get(path2, newFileName);
//								Files.write(fileNameAndPath, bean.getFile0().getBytes());
								y++;
							} else if (y == 1) {
								bean.getFile1().transferTo(path1);
								y++;
							} else if (y == 2) {
								bean.getFile2().transferTo(path1);
								y++;
							} else if (y == 3) {
								bean.getFile3().transferTo(path1);
								y++;
							}
						} else {
							Object[] tmp1 = { column_id, (String) jsonObject1.get("label"), "" };
							inputList.add(tmp1);
							y++;
						}
					} else if (dtype.equalsIgnoreCase("specialcombo")) {
						String combo = "";
						combo = "stateCode";
//-------------------------------------------------------------
						String code = "";
						code = (String) obj.get("stateCode") + "|" + (String) obj.get("distCode") + "|"
								+ (String) obj.get("mandalCode") + "|" + (String) obj.get("villCode");
						Object[] tmp2 = { column_id, "specialcombo", code };
						inputList.add(tmp2);										
					} else {
						String value = "";
						try {
							value = (String) obj.get((String) jsonObject1.get("label"));
						} catch (Exception e) {
							value = "";
						}

						Object[] tmp6 = { column_id, (String) jsonObject1.get("label"), value };
						inputList.add(tmp6);
					}
					column_id++;
				}
				int[] k = JdbcWrite.batchUpdate(qry, inputList);
				m = k.length;
				if (k.length > 0) {
					String qryy = "insert into generic_data_log(form_id,record_id,jsonData,login_name)values(?,?,?,?)";
					int ii = JdbcWrite.update(qryy, new Object[] { Integer.parseInt(bean.getForm_id()), record_id,
							obj.toString().trim(), username });
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception in GenericDaoImpl::insertFormData::" + e.getMessage());
			logger.error("Exception in GenericDaoImpl::insertFormData::" + e.getCause());
			logger.error("Exception in GenericDaoImpl::insertFormData::" + e.getClass() + "::" + e.getLocalizedMessage());
		}

		return m;
	}

	@Override
	public List<JsonBean> getList() {
		List<JsonBean> list = new ArrayList<JsonBean>();
		String qry = "";
		try {
			boolean con = JdbcRead.queryForObject(
					"select exists (select 1 from pg_tables where tablename = 'generic_data')", Boolean.class);
			if (con == true) {
				qry = "select distinct form_id,form_name,fields,jsonstring from generic_forms where form_id not in(select distinct form_id from generic_data) and verify_status='P' and approve_status='P'";
			} else {
				qry = "select distinct form_id,form_name,fields,jsonstring from generic_forms where verify_status='P' and approve_status='P'";
			}
			list = JdbcRead.query(qry, new Object[] {}, new RowMapper<JsonBean>() {
				public JsonBean mapRow(ResultSet rs, int row) throws SQLException {
					JsonBean jb = new JsonBean();
					jb.setForm_id(rs.getString("form_id"));
					jb.setForm_name(rs.getString("form_name"));
					return jb;
				}
			});
		} catch (Exception e) {
			logger.error("Exception in GenericDaoImpl::getList::" + e.getMessage());
			logger.error("Exception in GenericDaoImpl::getList::" + e.getCause());
			logger.error("Exception in GenericDaoImpl::getList::" + e.getClass() + "::" + e.getLocalizedMessage());
		}

		return list;
	}

	@Override
	public List<JsonBean> getFormStructure(String form_id, HttpSession session) {
		List<JsonBean> list = new ArrayList<JsonBean>();
		String qry = "";
		try {
			boolean con = JdbcRead.queryForObject(
					"select exists (select 1 from pg_tables where tablename = 'generic_data')", Boolean.class);
			if (con == true) {
				qry = "select distinct form_id,form_name,fields,jsonstring,to_char(from_date,'yyyy-mm-dd')from_date,to_char(to_date,'yyyy-mm-dd')to_date,captcha,con_check,otp_required from generic_forms where  form_id ='"
						+ form_id + "'  and form_id not in(select distinct form_id from generic_data)";
			} else {
				qry = "select distinct form_id,form_name,fields,jsonstring,to_char(from_date,'yyyy-mm-dd')from_date,to_char(to_date,'yyyy-mm-dd')to_date,captcha,con_check,otp_required from generic_forms where  form_id ='"
						+ form_id + "'";
			}
			list = JdbcRead.query(qry, new Object[] {}, new RowMapper<JsonBean>() {
				public JsonBean mapRow(ResultSet rs, int row) throws SQLException {
					JsonBean jb = new JsonBean();
					jb.setForm_id(rs.getString("form_id"));
					jb.setForm_name(rs.getString("form_name"));
					jb.setFields(rs.getString("fields"));
					jb.setJsonstring(rs.getString("jsonstring"));
					jb.setFrom_date(rs.getString("from_date"));
					jb.setTo_date(rs.getString("to_date"));
					jb.setCaptcha_key(rs.getString("captcha"));
					jb.setCon_value(rs.getString("con_check"));
					jb.setSms_key(rs.getString("otp_required"));
					return jb;
				}
			});
		} catch (Exception e) {
			logger.error("Exception in GenericDaoImpl::getFormStructure::" + e.getMessage());
			logger.error("Exception in GenericDaoImpl::getFormStructure::" + e.getCause());
			logger.error(
					"Exception in GenericDaoImpl::getFormStructure::" + e.getClass() + "::" + e.getLocalizedMessage());
		}

		return list;
	}

	@Override
	public int updateFormStructure(JsonBean form, HttpSession session) {
		int i = 0;
		SimpleDateFormat dff = new SimpleDateFormat("yyyy-MM-dd");
		try {
			UserDetails userDetails = loginUSerController.getUserDetails();
			String username = userDetails.getUsername();

			Date from_date = dff.parse(form.getFrom_date());
			Date to_date = dff.parse(form.getTo_date());			

			String sql = "update Generic_Forms set fields=?,jsonString=?,from_date=?,to_date=?,captcha=?,update_timestamp=now(),con_check=?,update_structure_role=?,otp_required=? where form_id=?";
			i = JdbcWrite.update(sql,
					new Object[] { Integer.parseInt(form.getFields().trim()), form.getJsonData().toString().trim(),
							from_date, to_date, form.getCaptcha(), form.getCon_value(), username,form.getSms(),
							Integer.parseInt(form.getForm_id()) });
		} catch (Exception e) {
			logger.error("Exception in GenericDaoImpl::updateFormStructure::" + e.getMessage());
			logger.error("Exception in GenericDaoImpl::updateFormStructure::" + e.getCause());
			logger.error("Exception in GenericDaoImpl::updateFormStructure::" + e.getClass() + "::"
					+ e.getLocalizedMessage());
		}

		return i;
	}

	@Override
	public List<JsonBean> getFormsList() {
		List<JsonBean> list = new ArrayList<JsonBean>();
		try {
			list = JdbcRead.query(
					"select distinct form_id,form_name,fields,jsonstring from generic_forms where verify_status='A' and approve_status='A' order by form_name",
					new Object[] {}, new RowMapper<JsonBean>() {
						public JsonBean mapRow(ResultSet rs, int row) throws SQLException {
							JsonBean jb = new JsonBean();
							jb.setForm_id(rs.getString("form_id"));
							jb.setForm_name(rs.getString("form_name"));
							return jb;
						}
					});
		} catch (Exception e) {
			logger.error("Exception in GenericDaoImpl::getFormsList::" + e.getMessage());
			logger.error("Exception in GenericDaoImpl::getFormsList::" + e.getCause());
			logger.error("Exception in GenericDaoImpl::getFormsList::" + e.getClass() + "::" + e.getLocalizedMessage());
		}

		return list;

	}

	@Override
	public List<JsonBean> getForm(String form_id, HttpSession session) {
		List<JsonBean> list = new ArrayList<JsonBean>();
		try {
			list = JdbcRead.query(
					"select distinct form_id,form_name,fields,jsonstring,from_date,to_date,captcha,otp_required from generic_forms where  form_id ='" + form_id + "' and from_date<=current_date and to_date>=current_date and verify_status='A' and approve_status='A'",
					new Object[] {}, new RowMapper<JsonBean>() {
						public JsonBean mapRow(ResultSet rs, int row) throws SQLException {
							JsonBean jb = new JsonBean();
							jb.setForm_id(rs.getString("form_id"));
							jb.setForm_name(rs.getString("form_name"));
							jb.setFields(rs.getString("fields"));
							jb.setJsonstring(rs.getString("jsonstring"));
							jb.setFrom_date(rs.getString("from_date"));
							jb.setTo_date(rs.getString("to_date"));
							jb.setCaptcha_key(rs.getString("captcha"));
							jb.setSms_key(rs.getString("otp_required"));
							return jb;
						}
					});
		} catch (Exception e) {
			logger.error("Exception in GenericDaoImpl::getForm::" + e.getMessage());
			logger.error("Exception in GenericDaoImpl::getForm::" + e.getCause());
			logger.error("Exception in GenericDaoImpl::getForm::" + e.getClass() + "::" + e.getLocalizedMessage());
		}

		return list;
	}

	@Override
	public List<JsonBean> getFormsRecordList() {
		List<JsonBean> list = new ArrayList<JsonBean>();
		try {
			list = JdbcRead.query(
					"select distinct form_id,form_name,fields,jsonstring from generic_forms where  form_id  in(select distinct form_id from generic_data where status='A') and verify_status='A' and approve_status='A' order by form_name",
					new Object[] {}, new RowMapper<JsonBean>() {
						public JsonBean mapRow(ResultSet rs, int row) throws SQLException {
							JsonBean jb = new JsonBean();
							jb.setForm_id(rs.getString("form_id"));
							jb.setForm_name(rs.getString("form_name"));
							return jb;
						}
					});
		} catch (Exception e) {
			logger.error("Exception in GenericDaoImpl::getFormsRecordList::" + e.getMessage());
			logger.error("Exception in GenericDaoImpl::getFormsRecordList::" + e.getCause());
			logger.error("Exception in GenericDaoImpl::getFormsRecordList::" + e.getClass() + "::"
					+ e.getLocalizedMessage());
		}

		return list;

	}

	@Override
	public List<JsonBean> getListOfRecords(String form_id, HttpSession session) {
		List<JsonBean> list = new ArrayList<JsonBean>();
		try {
			UserDetails userDetails = loginUSerController.getUserDetails();
			String username = userDetails.getUsername();
			list = JdbcRead.query(
					"select distinct a.form_id,b.record_id from generic_forms a left join generic_data b on a.form_id=b.form_id where  a.form_id ='"
							+ form_id + "'  and b.form_id='" + form_id + "' and (a.verify_status='A' and a.approve_status='A')  and b.status='A' order by record_id",
					new Object[] {}, new RowMapper<JsonBean>() {
						public JsonBean mapRow(ResultSet rs, int row) throws SQLException {
							JsonBean jb = new JsonBean();
							jb.setForm_id(rs.getString("form_id"));
							jb.setRecord_id(rs.getString("record_id"));
							return jb;
						}
					});

		} catch (Exception e) {
			logger.error("Exception in GenericDaoImpl::getListOfRecords::" + e.getMessage());
			logger.error("Exception in GenericDaoImpl::getListOfRecords::" + e.getCause());
			logger.error(
					"Exception in GenericDaoImpl::getListOfRecords::" + e.getClass() + "::" + e.getLocalizedMessage());
		}

		return list;
	}

	@Override
	public List<JsonBean> getFormData(String form_id, String record_id, HttpSession session) {
		List<JsonBean> list = new ArrayList<JsonBean>();
		try {
			list = JdbcRead.query(
					"select distinct form_id,record_id,column_id,column_name,column_value from generic_data where form_id='"
							+ form_id + "' and record_id='" + record_id + "' and status='A' order by column_id",
					new Object[] {}, new RowMapper<JsonBean>() {
						public JsonBean mapRow(ResultSet rs, int row) throws SQLException {
							JsonBean jb = new JsonBean();
							jb.setForm_id(rs.getString("form_id"));
							jb.setRecord_id(rs.getString("record_id"));
							jb.setColumn_id(rs.getString("column_id"));
							jb.setColumn_name(rs.getString("column_name"));

							if (rs.getString("column_name").equalsIgnoreCase("specialcombo")) {
								StringTokenizer st = new StringTokenizer(rs.getString("column_value"), "|");
								while (st.hasMoreTokens()) {
									stateCode = st.nextToken();
									distCode = st.nextToken();
									mandalCode = st.nextToken();
									villCode = st.nextToken();
								}
								List stateList = new ArrayList();
								if (!stateCode.equalsIgnoreCase("") && !stateCode.equalsIgnoreCase("null")) {
									String stateName = JdbcRead.queryForObject(
											"select distinct statename from state_mast where statecode='" + stateCode
													+ "'",
											String.class);
									jb.setStateCode(stateCode);
									jb.setStateName(stateName);
								} else {
									jb.setStateCode(stateCode);
									jb.setStateName("");
								}
								stateList = JdbcRead.query(
										"select distinct statecode,statename from state_mast order by statecode",
										new RowMapper<JsonBean>() {
											public JsonBean mapRow(ResultSet rs, int row) throws SQLException {
												JsonBean bean = new JsonBean();
												bean.setStateName(rs.getString("statename"));
												bean.setStateCode(rs.getString("statecode"));
												return bean;
											}
										});

								jb.setStateList(stateList);
								List distList = new ArrayList();
								if (!distCode.equalsIgnoreCase("") && !distCode.equalsIgnoreCase("null")) {
									String distName = JdbcRead
											.queryForObject(
													"select distinct dist_name from dist_mst where statecode='"
															+ stateCode + "' and dist_code ='" + distCode + "' ",
													String.class);
									jb.setDistName(distName);
									jb.setDistCode(distCode);
									distList = JdbcRead.query(
											"select distinct dist_code,dist_name from dist_mst where statecode=?",
											new Object[] { stateCode }, new RowMapper<JsonBean>() {
												public JsonBean mapRow(ResultSet rs, int row) throws SQLException {
													JsonBean jb = new JsonBean();
													jb.setDistCode(rs.getString("dist_code"));
													jb.setDistName(rs.getString("dist_name"));
													return jb;
												}
											});
								} else {
									jb.setDistName("");
									jb.setDistCode(distCode);
								}
								jb.setDistList(distList);
								List mandalList = new ArrayList();
								if (!mandalCode.equalsIgnoreCase("") && !mandalCode.equalsIgnoreCase("null")) {
									String mandalName = JdbcRead.queryForObject(
											"select distinct mandal_name from mandal_master where state_code='"
													+ stateCode + "' and district_code='" + distCode
													+ "' and mandal_code='" + mandalCode + "'",
											String.class);
									jb.setMandalName(mandalName);
									jb.setMandalCode(mandalCode);
									mandalList = JdbcRead.query(
											"select distinct  mandal_code,mandal_name from mandal_master where state_code=? and district_code=? ",
											new Object[] { stateCode, distCode }, new RowMapper<JsonBean>() {
												public JsonBean mapRow(ResultSet rs, int row) throws SQLException {
													JsonBean bean = new JsonBean();
													bean.setMandalCode(rs.getString("mandal_code"));
													bean.setMandalName(rs.getString("mandal_name"));
													return bean;
												}
											});
								} else {
									jb.setMandalName("");
									jb.setMandalCode(mandalCode);
								}
								jb.setMandalList(mandalList);
								List villList = new ArrayList();
								if (!villCode.equalsIgnoreCase("") && !villCode.equalsIgnoreCase("null")) {
									String villName = JdbcRead.queryForObject(
											"select distinct village_name from village_mst where statecode='"
													+ stateCode + "' and dist_code='" + distCode + "' and mandal_code='"
													+ mandalCode + "' and village_code='" + villCode + "'",
											String.class);
									jb.setVillName(villName);
									jb.setVillCode(villCode);
									villList = JdbcRead.query(
											"select distinct village_code,village_name from village_mst where statecode=? and dist_code=? and mandal_code=? ",
											new Object[] { stateCode, distCode, mandalCode },
											new RowMapper<JsonBean>() {
												public JsonBean mapRow(ResultSet rs, int row) throws SQLException {
													JsonBean bean = new JsonBean();
													bean.setVillCode(rs.getString("village_code"));
													bean.setVillName(rs.getString("village_name"));
													return bean;
												}
											});
								} else {
									jb.setVillName("");
									jb.setVillCode(villCode);
								}
								jb.setVillList(villList);
							}

							jb.setColumn_value(rs.getString("column_value"));
							return jb;
						}
					});
		} catch (Exception e) {
			logger.error("Exception in GenericDaoImpl::getFormData::" + e.getMessage());
			logger.error("Exception in GenericDaoImpl::getFormData::" + e.getCause());
			logger.error("Exception in GenericDaoImpl::getFormData::" + e.getClass() + "::" + e.getLocalizedMessage());
		}

		return list;
	}

	@Override
	public int updateFormData(JSONArray array, JSONObject obj, JsonBean bean) throws Exception {
		int m = 0, j = 0, kk = 0, form_id = 0, record_id = 0, column_id = 1;
		String qry = "";
		try {

			UserDetails userDetails = loginUSerController.getUserDetails();
			String username = userDetails.getUsername();
			String val = "";
			List<Object[]> inputList = new ArrayList<Object[]>();
			String con_check = JdbcWrite.queryForObject(
					"select distinct con_check from generic_forms where form_id = " + bean.getForm_id() + "",
					String.class);
			StringTokenizer st = new StringTokenizer(con_check, "||");
			String ccheck[] = new String[st.countTokens()];
			int yy = 0;
			while (st.hasMoreTokens()) {
				ccheck[yy] = st.nextToken();
				yy++;
			}
			int count = 0;
			for (int i = 0; i < array.size(); i++) {
				JSONObject jsonObject1 = (JSONObject) array.get(i);
				String dtype = (String) jsonObject1.get("dtype");
				for (int jj = 0; jj < ccheck.length; jj++) {
					int cnt = 0;
					if (dtype.equalsIgnoreCase(ccheck[jj])) {
						cnt = JdbcRead
								.queryForObject(
										"select count(*) from generic_data where form_id=" + bean.getForm_id()
												+ " and record_id !='" + bean.getRecord_id() + "' and column_value='"
												+ (String) obj.get((String) jsonObject1.get("label")) + "'",
										Integer.class);
						if (cnt > 0) {
							count++;
						}
					}
				}
			}
			if (count > 0 && count == ccheck.length) {
				m = 200;
			} else {
				qry = "update generic_data set column_value =?,update_login_role=?,update_timestamp=now() where  form_id='"
						+ bean.getForm_id() + "' and record_id='" + bean.getRecord_id() + "' and column_name=?";
				int y = 0;
				for (int i = 0; i < array.size(); i++) {
					JSONObject jsonObject1 = (JSONObject) array.get(i);
					String dtype = (String) jsonObject1.get("dtype");
					if (dtype.equalsIgnoreCase("file")) {
						String tempfile = "test";
						MultipartFile multipartFile = null;
						if (y == 0) {
							multipartFile = bean.getFile0();
						} else if (y == 1) {
							multipartFile = bean.getFile1();
						}
						if (y == 2) {
							multipartFile = bean.getFile2();
						}
						if (y == 3) {
							multipartFile = bean.getFile3();
						}
//                	List <MultipartFile> list = bean.getFile();
//                	multipartFile = list.get(y);
						String filename = multipartFile.getOriginalFilename();
						if (!filename.equalsIgnoreCase("")) {

							int index = filename.lastIndexOf('.');
							String extension = "";
							if (index > 0) {

								extension = filename.substring(index + 1);

								File path = null;
								BufferedImage image = null;
//                	path = new File("C:\\u01\\app\\" + File.separator + tempfile + File.separator + bean.getForm_name() + File.separator + System.nanoTime() + "." + extension);
								path = new File(formPath + File.separator + bean.getForm_name());
								if (path.exists()) {
//                    	path.delete();
//                    	path.mkdir();
								} else {
									path.mkdir();
								}
								File path1 = new File(formPath + File.separator + bean.getForm_name() + File.separator
										+ System.nanoTime() + "." + extension);
//                  ImageIO.write(image, "jpg", path);

//                Object[] tmp = new Object[]{path.toString(),bean.getForm_id(),bean.getRecord_id(),(String) jsonObject1.get("label")};
								Object[] tmp = new Object[] { path1.toString(), username,
										(String) jsonObject1.get("label") };
								inputList.add(tmp);
								if (y == 0) {
									bean.getFile0().transferTo(path1);
									y++;
								} else if (y == 1) {
									bean.getFile1().transferTo(path1);
									y++;
								} else if (y == 2) {
									bean.getFile2().transferTo(path1);
									y++;
								} else if (y == 3) {
									bean.getFile3().transferTo(path1);
									y++;
								}
							} else {

								y++;
							}
						} else {

							y++;
						}
					} else if (dtype.equalsIgnoreCase("specialcombo")) {
//-------------------------------------------------------------  
						String code = "";
						code = (String) obj.get("stateCode") + "|" + (String) obj.get("distCode") + "|"
								+ (String) obj.get("mandalCode") + "|" + (String) obj.get("villCode");
						Object[] tmp2 = { code, username, "specialcombo" };
						inputList.add(tmp2);
					} else {
						String value = "";
						try {
							value = (String) obj.get((String) jsonObject1.get("label"));
						} catch (Exception e) {
							value = "";
						}
						Object[] tmp = new Object[] { value, username, (String) jsonObject1.get("label") };
						inputList.add(tmp);
					}
					column_id++;
				}
				for (int ff = 0; ff < inputList.size(); ff++) {
					Object[] tmp = inputList.get(ff);
				}
				int[] k = JdbcWrite.batchUpdate(qry, inputList);
				m = k.length;
				if (k.length > 0) {
					String qryy = "update generic_data_log set update_jsonData =?,update_login_role=?,update_timestamp=now()  where form_id='"
							+ bean.getForm_id() + "' and record_id='" + bean.getRecord_id() + "'";
					int ii = JdbcWrite.update(qryy, new Object[] { obj.toString().trim(), username });
				}
			}
		} catch (DataAccessException e) {
			logger.error("Exception in GenericDaoImpl::updateFormData::" + e.getMessage());
			logger.error("Exception in GenericDaoImpl::updateFormData::" + e.getCause());
			logger.error("Exception in GenericDaoImpl::updateFormData::" + e.getClass() + "::" + e.getLocalizedMessage());
		}

		return m;

	}

	@Override
	public List<CommonBean> getStateList() {
		List stateList = new ArrayList();
		String qry = "select distinct statecode,statename from state_mast order by statecode";
		stateList = JdbcRead.query(qry, new RowMapper<CommonBean>() {
			public CommonBean mapRow(ResultSet rs, int row) throws SQLException {
				CommonBean bean = new CommonBean();
				bean.setStateName(rs.getString("statename"));
				bean.setStateCode(rs.getString("statecode"));
				return bean;
			}
		});
		return stateList;
	}

	@Override
	public List getDistrictList(String stateCode) {
		String qry = "select distinct dist_code,dist_name from dist_mst where statecode='" + stateCode
				+ "' order by dist_code";
		List distList = JdbcRead.query(qry, new RowMapper<Object>() {
			public Object mapRow(ResultSet rs, int row) throws SQLException {
				return rs.getString("dist_name") + "/" + rs.getString("dist_code");
			}
		});
		return distList;
	}

	@Override
	public List getMandalList(String stateCode, String distCode) {
		String qry = "select distinct  mandal_code,mandal_name from mandal_master where district_code='" + distCode
				+ "' and state_code='" + stateCode + "'";
		List mandalList = JdbcRead.query(qry, new RowMapper<Object>() {
			public Object mapRow(ResultSet rs, int row) throws SQLException {
				return rs.getString("mandal_name") + "/" + rs.getString("mandal_code");
			}
		});
		return mandalList;
	}

	@Override
	public List getVillageList(String stateCode, String distCode, String mandalCode) {
		String qry = "select distinct village_code,village_name from village_mst where statecode='" + stateCode
				+ "' and dist_code='" + distCode + "' and mandal_code='" + mandalCode + "'";
		List villList = JdbcRead.query(qry, new RowMapper<Object>() {
			public Object mapRow(ResultSet rs, int row) throws SQLException {
				return rs.getString("village_name") + "/" + rs.getString("village_code");
			}
		});
		return villList;
	}

	@Override
	public String getFilePath(String form_id, String record_id, String column_id) {
		String filePath = "";
		try {
			filePath = JdbcRead.queryForObject("select distinct column_value from generic_data where form_id='"
					+ form_id + "' and record_id='" + record_id + "' and column_id='" + column_id + "'", String.class);
		} catch (Exception e) {
			logger.error("Exception in GenericDaoImpl::getFilePath::" + e.getMessage());
			logger.error("Exception in GenericDaoImpl::getFilePath::" + e.getCause());
			logger.error("Exception in GenericDaoImpl::getFilePath::" + e.getClass() + "::" + e.getLocalizedMessage());
		}

		return filePath;
	}

	@Override

	public List<JsonBean> getFormsReport(String form_id, HttpSession session) {

		final List list1 = new ArrayList();

		try {

			int count = JdbcRead.queryForObject(
					"select distinct fields from generic_forms where form_id=" + form_id + "", Integer.class);

			String columns = JdbcRead.queryForObject(
					"select string_agg(concat,',') as columns from (select distinct column_name,column_id,concat('\"',column_id,'\"',' as ','\"',column_name,'\"') from generic_data where form_id="
							+ form_id + " and record_id=1 order by column_id) as inn",
					String.class);

			String crosstabColumns = JdbcRead.queryForObject(
					"select  string_agg(concat('\"',column_id,'\"',' text'),',') as concatt from (select distinct column_id from generic_data where form_id="
							+ form_id + " and record_id=1  order by column_id) as inn",
					String.class);

			List list2 = JdbcRead.query("select record_id," + columns
					+ " from crosstab($$ select record_id,column_id,column_value from generic_data where form_id="
					+ form_id
					+ " and status='A' order by 1,2$$,$$select distinct column_id from generic_data where form_id="
					+ form_id + " and status='A' order by 1 $$) as ct (record_id text," + crosstabColumns + ")",
					new Object[] {}, new RowMapper<JsonBean>() {

						public JsonBean mapRow(ResultSet rs, int row) throws SQLException {

							JsonBean jb = new JsonBean();

							List<JsonBean> list = new ArrayList<JsonBean>();

							for (int i = 1; i <= count + 1; i++) {

								jb = new JsonBean();

								try {

									StringTokenizer st = new StringTokenizer(rs.getString(i), "|");

									while (st.hasMoreTokens()) {

										stateCode = st.nextToken();

										distCode = st.nextToken();

										mandalCode = st.nextToken();

										villCode = st.nextToken();

									}

									String value = JdbcRead.queryForObject(
											"select distinct concat(a.statename,' || ',b.dist_name,' || ',c.mandal_name,' || ',d.village_name)cc from state_mast a left join dist_mst b on a.statecode=b.statecode \r\n"

													+ "left join mandal_master c on a.statecode=c.state_code and b.dist_code=c.district_code\r\n"

													+ "left join village_mst d on a.statecode=d.statecode and b.dist_code=d.dist_code and c.mandal_code=d.mandal_code \r\n"

													+ "where a.statecode='" + stateCode + "' and b.statecode='"
													+ stateCode + "' and c.state_code='" + stateCode
													+ "' and d.statecode='" + stateCode + "' and b.dist_code='"
													+ distCode + "' and c.district_code='" + distCode
													+ "' and d.dist_code='" + distCode + "' \r\n"

													+ "and c.mandal_code='" + mandalCode + "' and d.mandal_code='"
													+ mandalCode + "' and d.village_code='" + villCode + "'",
											String.class);

									jb.setColumn_value(value);

								} catch (Exception e) {

									jb.setColumn_value(rs.getString(i));

								}

								list.add(jb);

							}

							list1.add(list);

							return jb;

						}

					});

		} catch (Exception e) {

			logger.error("Exception in GenericDaoImpl::getFormsReport::" + e.getMessage());

			logger.error("Exception in GenericDaoImpl::getFormsReport::" + e.getCause());

			logger.error(
					"Exception in GenericDaoImpl::getFormsReport::" + e.getClass() + "::" + e.getLocalizedMessage());

		}

		return list1;

	}

	@Override
	public List<JsonBean> getColumnsList(String form_id, HttpSession session) {
		List<JsonBean> list = new ArrayList<JsonBean>();
		String columns = "";
		try {
			columns = JdbcRead.queryForObject(
					"select string_agg(concat,',') as columns from (select distinct column_name,column_id,concat(column_name) from generic_data where form_id="
							+ form_id + " and record_id=1 order by column_id) as inn",
					String.class);
			if (!columns.equalsIgnoreCase("null") && !columns.equalsIgnoreCase("")) {
				String[] abc = columns.split(",");
				JsonBean jb1 = new JsonBean();
				jb1.setColumn_name("slno");
				list.add(jb1);
				for (int i = 0; i < abc.length; i++) {
					JsonBean jb = new JsonBean();
					jb.setColumn_name(abc[i]);
					list.add(jb);
				}
			} else {
				list = new ArrayList<JsonBean>();
			}
		} catch (Exception e) {
			list = new ArrayList<JsonBean>();
			logger.error("Exception in GenericDaoImpl::getFormsReport::" + e.getMessage());
			logger.error("Exception in GenericDaoImpl::getFormsReport::" + e.getCause());
			logger.error(
					"Exception in GenericDaoImpl::getFormsReport::" + e.getClass() + "::" + e.getLocalizedMessage());
		}

		return list;
	}

	@Override
	public List<JsonBean> getFormExpiryDetails(String form_id, HttpSession session) {
		List<JsonBean> list = new ArrayList<JsonBean>();
		try {
			list = JdbcRead.query(
					"select distinct form_id,from_date,to_date from generic_forms where form_id ='" + form_id + "' ",
					new Object[] {}, new RowMapper<JsonBean>() {
						public JsonBean mapRow(ResultSet rs, int row) throws SQLException {
							JsonBean jb = new JsonBean();
							jb.setForm_id(rs.getString("form_id"));
							jb.setFrom_date(rs.getString("from_date"));
							jb.setTo_date(rs.getString("to_date"));
							return jb;
						}
					});
		} catch (Exception e) {
			logger.error("Exception in GenericDaoImpl::getFormExpiryDetails::" + e.getMessage());
			logger.error("Exception in GenericDaoImpl::getFormExpiryDetails::" + e.getCause());
			logger.error("Exception in GenericDaoImpl::getFormExpiryDetails::" + e.getClass() + "::"
					+ e.getLocalizedMessage());
		}

		return list;
	}

	@Override
	public int updateFormExpiryDate(JsonBean form, HttpSession session) {
		int i = 0;
		SimpleDateFormat dff = new SimpleDateFormat("yyyy-MM-dd");
		try {
			UserDetails userDetails = loginUSerController.getUserDetails();
			String username = userDetails.getUsername();
			Date from_date = dff.parse(form.getFrom_date());
			Date to_date = dff.parse(form.getTo_date());

			String sql = "update Generic_Forms set to_date=?,expiry_role=?,expiry_timestamp=now() where form_id=?";
			i = JdbcWrite.update(sql, new Object[] { to_date, username, Integer.parseInt(form.getForm_name()) });
		} catch (Exception e) {
			logger.error("Exception in GenericDaoImpl::updateFormExpiryDate::" + e.getMessage());
			logger.error("Exception in GenericDaoImpl::updateFormExpiryDate::" + e.getCause());
			logger.error("Exception in GenericDaoImpl::updateFormExpiryDate::" + e.getClass() + "::"
					+ e.getLocalizedMessage());
		}

		return i;

	}

	@Override
	public int updateFormApprove(JsonBean form, HttpSession session) {
		int i = 0;
		try {
			UserDetails userDetails = loginUSerController.getUserDetails();
			String username = userDetails.getUsername();
			String sql = "update Generic_Forms set approve_status=?,approve_role=?,remark=?,approve_timestamp=now() where form_id=?";
			i = JdbcWrite.update(sql, new Object[] { form.getApprove(), username, form.getRemark(),
					Integer.parseInt(form.getForm_id()) });
		} catch (Exception e) {
			logger.error("Exception in GenericDaoImpl::updateFormExpiryDate::" + e.getMessage());
			logger.error("Exception in GenericDaoImpl::updateFormApprove::" + e.getCause());
			logger.error(
					"Exception in GenericDaoImpl::updateFormApprove::" + e.getClass() + "::" + e.getLocalizedMessage());
		}

		return i;
	}

	@Override
	public int getForm_Check(String form_name) {
		int status = 0;
		try {
			status = JdbcWrite.queryForObject(
					"select count(*) from generic_forms where form_name='" + form_name.toUpperCase().replaceAll("\\s","") + "'",
					Integer.class);
		} catch (Exception e) {
			logger.error("Exception in GenericDaoImpl::getForm_Check::" + e.getMessage());
			logger.error("Exception in GenericDaoImpl::getForm_Check::" + e.getCause());
			logger.error("Exception in GenericDaoImpl::getForm_Check::" + e.getClass() + "::" + e.getLocalizedMessage());
		}

		return status;
	}

	@Override
	public int deleteFormData(JsonBean form, HttpSession session) {
		int i = 0;int[] j = null;
		try {
			UserDetails userDetails = loginUSerController.getUserDetails();
			String username = userDetails.getUsername();
			System.out.println("deleteFormData=========="+Integer.parseInt(form.getRecord_id()));
			if(Integer.parseInt(form.getRecord_id()) == 0) {
				j = JdbcWrite.batchUpdate(new String[] {
						"update Generic_data set status='D',del_role='" + username + "',del_timestamp=now() where form_id=" + Integer.parseInt(form.getForm_id()) + "",
						"update Generic_data_log set del_role='" + username + "',del_timestamp=now() where form_id=" + Integer.parseInt(form.getForm_id()) + "" });	
			}else {
			j = JdbcWrite.batchUpdate(new String[] {
					"update Generic_data set status='D',del_role='" + username + "',del_timestamp=now() where form_id="
							+ Integer.parseInt(form.getForm_id()) + " and record_id="
							+ Integer.parseInt(form.getRecord_id()) + "",
					"update Generic_data_log set del_role='" + username + "',del_timestamp=now() where form_id="
							+ Integer.parseInt(form.getForm_id()) + " and record_id="
							+ Integer.parseInt(form.getRecord_id()) + "" });
			}
			i = j.length;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception in GenericDaoImpl::deleteFormData::" + e.getMessage());
			logger.error("Exception in GenericDaoImpl::deleteFormData::" + e.getCause());
			logger.error("Exception in GenericDaoImpl::deleteFormData::" + e.getClass() + "::" + e.getLocalizedMessage());
		}

		return i;
	}

	@Override
	public int deleteFormStructure(JsonBean form, HttpSession session) {
		int i = 0;
		try {
			UserDetails userDetails = loginUSerController.getUserDetails();
			String username = userDetails.getUsername();
			String sql = "update Generic_forms set verify_status='D',approve_status='D',del_role=?,del_timestamp=now() where form_id=?";
			i = JdbcWrite.update(sql, new Object[] { username, Integer.parseInt(form.getForm_id()) });
		} catch (Exception e) {
			logger.error("Exception in GenericDaoImpl::deleteFormStructure::" + e.getMessage());
			logger.error("Exception in GenericDaoImpl::deleteFormStructure::" + e.getCause());
			logger.error("Exception in GenericDaoImpl::deleteFormStructure::" + e.getClass() + "::"+ e.getLocalizedMessage());
		}

		return i;

	}

	@Override
	public int updateFormVerify(JsonBean form, HttpSession session) {
		int i = 0;
		try {
			UserDetails userDetails = loginUSerController.getUserDetails();
			String username = userDetails.getUsername();
//---------------------------New-Code For(SMS)-------------------------------------
			JsonBean jb1 = new JsonBean();
			List<JsonBean> list = new ArrayList<JsonBean>();			
			String qry = "select distinct form_name,form_id,fields,jsonstring,otp_required from generic_forms where form_id="+form.getForm_id()+"";
			list = JdbcRead.query(qry, new Object[] {}, new RowMapper<JsonBean>() {
				public JsonBean mapRow(ResultSet rs, int row) throws SQLException {
					JsonBean jb = new JsonBean();
					jb.setForm_name(rs.getString("form_name"));
					jb.setForm_id(rs.getString("form_id"));
					jb.setFields(rs.getString("fields"));
					jb.setJsonData(rs.getString("jsonstring"));
					jb.setSms_key(rs.getString("otp_required"));
					return jb;
				}
			});
			if(list.size()>0) {
				jb1 = (JsonBean)list.get(0);	
			}
			String jsonString = jb1.getJsonData();
			if(jb1.getSms_key().equalsIgnoreCase("Y")) {							
	        String newJsonString = "{\"label\":\"OTP Verify\",\"dtype\":\"otp\",\"minsize\":\"6\",\"maxsize\":\"6\",\"mandatory\":\"true\"}";	        
	        ObjectMapper objectMapper = new ObjectMapper();
	        ArrayNode existingJsonArray = null;
	        try {
	            existingJsonArray = (ArrayNode) objectMapper.readTree(jsonString);
	        } catch (Exception e) {}
	        ObjectNode newJsonObject = null,newJsonObject1 = null;
	        try {
	            newJsonObject = (ObjectNode) objectMapper.readTree(newJsonString);
	            newJsonObject1 = objectMapper.createObjectNode();
	        } catch (Exception e) {}
	        int fields = Integer.parseInt(jb1.getFields());
	        fields = fields + 1;
	        newJsonObject1.put("formname", jb1.getForm_name());
	        newJsonObject1.put("fields", fields);

	        existingJsonArray.set(0, newJsonObject1);
	        existingJsonArray.add(newJsonObject);			
			
			String sql = "update Generic_Forms set verify_status=?,verify_role=?,remark=?,verify_timestamp=now(),fields=?,jsonstring=? where form_id=?";
			i = JdbcWrite.update(sql, new Object[] { form.getApprove(), username, form.getRemark(),
					fields,existingJsonArray.toString(),Integer.parseInt(form.getForm_id()) });
//--------------------------------------------------------------------------------------			
			}else {
				String sql = "update Generic_Forms set verify_status=?,verify_role=?,remark=?,verify_timestamp=now() where form_id=?";
				i = JdbcWrite.update(sql, new Object[] { form.getApprove(), username, form.getRemark(),
						Integer.parseInt(form.getForm_id()) });
			}
		} catch (Exception e) {
			logger.error("Exception in GenlericDaoImpl::updateFormVerify::" + e.getMessage());
			logger.error("Exception in GenericDaoImpl::updateFormVerify::" + e.getCause());
			logger.error(
					"Exception in GenericDaoImpl::updateFormVerify::" + e.getClass() + "::" + e.getLocalizedMessage());
		}

		return i;

	}

	@Override
	public List<JsonBean> getList1() {
		List<JsonBean> list = new ArrayList<JsonBean>();
		String qry = "";
		try {
			boolean con = JdbcRead.queryForObject(
					"select exists (select 1 from pg_tables where tablename = 'generic_data')", Boolean.class);
			if (con == true) {
				qry = "select distinct form_id,form_name,fields,jsonstring from generic_forms where form_id not in(select distinct form_id from generic_data) ";
			} else {
				qry = "select distinct form_id,form_name,fields,jsonstring from generic_forms ";
			}
			list = JdbcRead.query(qry, new Object[] {}, new RowMapper<JsonBean>() {
				public JsonBean mapRow(ResultSet rs, int row) throws SQLException {
					JsonBean jb = new JsonBean();
					jb.setForm_id(rs.getString("form_id"));
					jb.setForm_name(rs.getString("form_name"));
					return jb;
				}
			});
		} catch (Exception e) {
			logger.error("Exception in GenericDaoImpl::getList::" + e.getMessage());
			logger.error("Exception in GenericDaoImpl::getList::" + e.getCause());
			logger.error("Exception in GenericDaoImpl::getList::" + e.getClass() + "::" + e.getLocalizedMessage());
		}

		return list;
	}

	@Override
	public List<JsonBean> getList2() {
		List<JsonBean> list = new ArrayList<JsonBean>();
		String qry = "";
		try {
			boolean con = JdbcRead.queryForObject(
					"select exists (select 1 from pg_tables where tablename = 'generic_data')", Boolean.class);
			if (con == true) {
				qry = "select distinct form_id,form_name,fields,jsonstring from generic_forms where form_id not in(select distinct form_id from generic_data) and verify_status='A'";
			} else {
				qry = "select distinct form_id,form_name,fields,jsonstring from generic_forms where verify_status='A'";
			}
			list = JdbcRead.query(qry, new Object[] {}, new RowMapper<JsonBean>() {
				public JsonBean mapRow(ResultSet rs, int row) throws SQLException {
					JsonBean jb = new JsonBean();
					jb.setForm_id(rs.getString("form_id"));
					jb.setForm_name(rs.getString("form_name"));
					return jb;
				}
			});
		} catch (Exception e) {
			logger.error("Exception in GenericDaoImpl::getList::" + e.getMessage());
			logger.error("Exception in GenericDaoImpl::getList::" + e.getCause());
			logger.error("Exception in GenericDaoImpl::getList::" + e.getClass() + "::" + e.getLocalizedMessage());
		}

		return list;
	}

	@Override
	public List<JsonBean> getList3() {
		List<JsonBean> list = new ArrayList<JsonBean>();
		String qry = "";
		try {
			boolean con = JdbcRead.queryForObject(
					"select exists (select 1 from pg_tables where tablename = 'generic_data')", Boolean.class);
			if (con == true) {
//				qry = "select distinct form_id,form_name,fields,jsonstring from generic_forms where form_id not in(select distinct form_id from generic_data) and verify_status='P' and approve_status='P'";
				
				qry = "select distinct form_id,form_name,fields,jsonstring from generic_forms where verify_status !='D' and approve_status !='D'";
			} else {
				qry = "select distinct form_id,form_name,fields,jsonstring from generic_forms where verify_status='P' and approve_status='P'";
			}
			list = JdbcRead.query(qry, new Object[] {}, new RowMapper<JsonBean>() {
				public JsonBean mapRow(ResultSet rs, int row) throws SQLException {
					JsonBean jb = new JsonBean();
					jb.setForm_id(rs.getString("form_id"));
					jb.setForm_name(rs.getString("form_name"));
					return jb;
				}
			});
		} catch (Exception e) {
			logger.error("Exception in GenericDaoImpl::getList::" + e.getMessage());
			logger.error("Exception in GenericDaoImpl::getList::" + e.getCause());
			logger.error("Exception in GenericDaoImpl::getList::" + e.getClass() + "::" + e.getLocalizedMessage());
		}

		return list;
	}

	@Override
	public int checkFormExpiryDate(String form_id, HttpSession session) {
		int count = 0,count1 = 0;
		try {
			
			count1 = JdbcRead.queryForObject("select count(*) from generic_forms where  form_id ='" + form_id + "' ", Integer.class);
			if(count1>0) {
			count = JdbcRead.queryForObject("select count(*) from generic_forms where  form_id ='" + form_id
					+ "' and from_date<=current_date and to_date>=current_date", Integer.class);
			}else {
			count = 200;
			}
		} catch (Exception e) {
			logger.error("Exception in GenericDaoImpl::checkFormExpiryDate::" + e.getMessage());
			logger.error("Exception in GenericDaoImpl::checkFormExpiryDate::" + e.getCause());
			logger.error("Exception in GenericDaoImpl::checkFormExpiryDate::" + e.getClass() + "::"+ e.getLocalizedMessage());
		}
		return count;
	}

}
