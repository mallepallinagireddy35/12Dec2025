/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nic.svpnpa.dao;



import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.nic.svpnpa.controller.LoginUserDetailsController;
import com.nic.svpnpa.model.CommonBean;
import com.nic.svpnpa.model.Role;
import com.nic.svpnpa.model.WebMenuBean;

import jakarta.servlet.http.*;


@Repository
public class AdminDaoImpl implements AdminDao {

	private static final Logger logger = LogManager.getLogger(AdminDaoImpl.class);
	@Autowired
	private JdbcTemplate JdbcWrite;

	@Autowired
	private JdbcTemplate JdbcRead;
	
	@Autowired
	LoginUserDetailsController loginUSerController;

	@Override
	public List<WebMenuBean> getDynamicMenuList(String userid, HttpServletRequest request) {
        List<WebMenuBean> menulist = new ArrayList<WebMenuBean>();
        WebMenuBean menuBean = null;
        WebMenuBean subbean = null;
        WebMenuBean subsubbean = null;
        int role_id = 4;
        try {
        if(!userid.equalsIgnoreCase("") && !userid.equalsIgnoreCase("null")) {
         role_id = JdbcRead.queryForObject("select distinct b.role_id from users a left join user_roles b on a.id=b.user_id where a.username='"+userid+"'",Integer.class);
        }
        String main_sql = "select DISTINCT a.mainlevel,a.main_desc from t04_menu_web  a left join t04_menu_service b on(a.mainlevel=b.mainlevel) where b.role_id='" + role_id + "' and a.status='A' order by a.mainlevel";
       // logger.info("main query--" + main_sql);
        List<WebMenuBean> mainlist = JdbcRead.query(main_sql, new RowMapper<WebMenuBean>() {
            @Override
            public WebMenuBean mapRow(ResultSet rs, int i) throws SQLException {
                WebMenuBean menu = new WebMenuBean();
                menu.setMainlevel(rs.getInt("mainlevel"));
                menu.setMainlevel_desc(rs.getString("main_desc"));  
                return menu;
            }
        });
        for (int i = 0; i < mainlist.size(); i++) {
            WebMenuBean bean = mainlist.get(i);
            menuBean = new WebMenuBean();
            menuBean.setMainlevel(bean.getMainlevel());
            menuBean.setMainlevel_desc(bean.getMainlevel_desc());
          
            String sql = "select DISTINCT a.mainlevel, a.level1, a.level1_desc from t04_menu_web a left join t04_menu_service b on(a.mainlevel=b.mainlevel and a.level1=b.level1)"
                    + " where b.role_id='" + role_id + "' and a.status='A' and a.mainlevel="+bean.getMainlevel()+" and a.level1 !=0 order by a.mainlevel, a.level1";
            List<WebMenuBean> list = JdbcRead.query(sql, new RowMapper<WebMenuBean>() {
                @Override
                public WebMenuBean mapRow(ResultSet rs, int i) throws SQLException {
                    WebMenuBean menu = new WebMenuBean();
                    menu.setMainlevel(rs.getInt("mainlevel"));
                    menu.setLevel1(rs.getInt("level1"));
                    menu.setLevel1_desc(rs.getString("level1_desc"));

                    return menu;
                }
            });

            List<WebMenuBean> submenulist = new ArrayList<WebMenuBean>();
            if(list.size()>0) {
            for (int j = 0; j < list.size(); j++) {
                WebMenuBean subMenu = list.get(j);
                subbean = new WebMenuBean();
                subbean.setMainlevel(subMenu.getMainlevel());
                subbean.setLevel1(subMenu.getLevel1());
                subbean.setLevel1_desc(subMenu.getLevel1_desc());
                
                String sql1 = "select DISTINCT a.mainlevel, a.level1, a.level2, a.level2_desc from t04_menu_web a left join t04_menu_service b on(a.mainlevel=b.mainlevel and a.level1=b.level1 and a.level2=b.level2) where b.role_id='" + role_id + "' and a.status='A' and a.mainlevel='" + subMenu.getMainlevel() + "' and a.level1='" + subMenu.getLevel1() + "' and a.level2 !=0 order by a.mainlevel, a.level1, a.level2";
                List<WebMenuBean> sublist = JdbcRead.query(sql1, new RowMapper<WebMenuBean>() {
                    @Override
                    public WebMenuBean mapRow(ResultSet rs, int i) throws SQLException {
                        WebMenuBean menu = new WebMenuBean();
                        menu.setMainlevel(rs.getInt("mainlevel"));
                        menu.setLevel1(rs.getInt("level1"));

                        menu.setLevel2(rs.getInt("level2"));
                        menu.setLevel2_desc(rs.getString("level2_desc"));

                        return menu;
                    }
                });
//                ----------------------
                List<WebMenuBean> submenu3 = new ArrayList<WebMenuBean>();
                if(sublist.size()>0) {
                for (int k = 0; k < sublist.size(); k++) {
                    String menu_url = "";
                    WebMenuBean subsubMenu = sublist.get(k);
                    subsubbean = new WebMenuBean();
                    subsubbean.setMainlevel(subsubMenu.getMainlevel());
                    subsubbean.setLevel1(subsubMenu.getLevel1());
                    subsubbean.setLevel2(subsubMenu.getLevel2());
                    subsubbean.setLevel2_desc(subsubMenu.getLevel2_desc());
//                ---------------------------
//                subsubbean.setLevel3(0);
//                --------------------------                    
                    String sql2 = "select DISTINCT a.mainlevel, a.level1, a.level2, A.level2_desc, a.level3, a.level3_desc, a.menu_url from t04_menu_web a left join t04_menu_service b on (a.mainlevel=b.mainlevel and a.level1=b.level1 and a.level2=b.level2 and a.level3=b.level3) where b.role_id='" + role_id + "' and a.status='A' and a.mainlevel='" + subsubMenu.getMainlevel() + "' and a.level1='" + subsubMenu.getLevel1() + "' and a.level2='" + subsubMenu.getLevel2() + "' and a.level3 !=0 order by a.level2, a.level3";
                    List<WebMenuBean> subsubMenulist = JdbcRead.query(sql2, new RowMapper<WebMenuBean>() {
                        @Override
                        public WebMenuBean mapRow(ResultSet rs, int i) throws SQLException {
                            WebMenuBean menu = new WebMenuBean();
                            menu.setMainlevel(rs.getInt("mainlevel"));
                            menu.setLevel1(rs.getInt("level1"));
                            menu.setLevel2(rs.getInt("level2"));
                            menu.setLevel2_desc(rs.getString("level2_desc"));
                            menu.setLevel3(rs.getInt("level3"));
                            menu.setLevel3_desc(rs.getString("level3_desc"));
                            menu.setMenu_url(rs.getString("menu_url"));

                            return menu;
                        }
                    });
                    //logger.info("list size---" + subsubMenulist.size());
                    if (subsubMenulist.size() == 0) {

                        
                        String sql3 = "select DISTINCT a.mainlevel, a.level1, a.level2, a.level2_desc, a.menu_url from t04_menu_web a left join t04_menu_service b on(a.mainlevel=b.mainlevel and a.level1=b.level1 and a.level2=b.level2) where b.role_id='" + role_id + "' and a.status='A' and  a.mainlevel='" + subsubMenu.getMainlevel() + "' and a.level1='" + subsubMenu.getLevel1() + "' and a.level2='" + subsubMenu.getLevel2() + "' order by a.level2";
                    //    logger.info("level23---" + sql3);
                        List<WebMenuBean> subsubMenulist1 = JdbcRead.query(sql3, new RowMapper<WebMenuBean>() {
                            //int j=1;
                            @Override
                            public WebMenuBean mapRow(ResultSet rs, int i) throws SQLException {
                                //logger.info("j::"+j);
                                WebMenuBean menu = new WebMenuBean();
                                menu.setMainlevel(rs.getInt("mainlevel"));
                                menu.setLevel1(rs.getInt("level1"));
                                menu.setLevel2(rs.getInt("level2"));
                                menu.setLevel2_desc(rs.getString("level2_desc"));
                                menu.setMenu_url(rs.getString("menu_url"));
                                //++j;
                                return menu;

                            }

                        });
                        //logger.info("size--" + subsubMenulist1.size());
                        if (subsubMenulist1.size() > 0) {
                            WebMenuBean lbean = subsubMenulist1.get(0);
                            subsubbean.setMenu_url(lbean.getMenu_url());
                        }

                    }

                    //logger.info("list size---" + subsubMenulist.size());
                    subsubbean.setMenuList(subsubMenulist);
                    submenu3.add(subsubbean);

                }
//                ------------------
                }else {
                	String sql33 = "select DISTINCT a.mainlevel, a.level1, a.level1_desc,a.menu_url from t04_menu_web a left join t04_menu_service b on(a.mainlevel=b.mainlevel and a.level1=b.level1)"
                            + " where b.role_id='" + role_id + "' and a.status='A' and a.mainlevel='" + subMenu.getMainlevel() + "' and a.level1='" + subMenu.getLevel1() + "'  order by a.mainlevel, a.level1";
                    // String sql = "select DISTINCT a.mainlevel,A.LEVEL1,A.LEVEL1_DESC from t04_menu_web a left join SROUSER.IGRS_MENU_SERVICE b on(a.mainlevel=b.mainlevel and a.level1=b.level1) where b.AGENCY_NAME='" + userid.trim() + "' and   a.mainlevel='" + bean.getMainlevel() + "' order by a.mainlevel,A.LEVEL1";
                   // logger.info("sql----::"+sql);
                    List<WebMenuBean> list1 = JdbcRead.query(sql33, new RowMapper<WebMenuBean>() {
                        @Override
                        public WebMenuBean mapRow(ResultSet rs, int i) throws SQLException {
                            WebMenuBean menu = new WebMenuBean();
                            menu.setMainlevel(rs.getInt("mainlevel"));
                            menu.setLevel1(rs.getInt("level1"));
                            menu.setLevel1_desc(rs.getString("level1_desc"));
                            menu.setMenu_url(rs.getString("menu_url"));

                            return menu;
                        }
                    });
                    
                    if(list1.size()>0) {
                    	WebMenuBean lbean = list1.get(0);
                    	subbean.setMenu_url(lbean.getMenu_url());	
                    }

                }
                subbean.setSubMenu(submenu3);
                submenulist.add(subbean);

            }

//----------------
            }else {
            	String main_sqll = "select DISTINCT a.mainlevel,a.main_desc,a.menu_url from t04_menu_web  a left join t04_menu_service b on(a.mainlevel=b.mainlevel) where b.role_id='" + role_id + "' and a.status='A' and a.mainlevel="+bean.getMainlevel()+" order by a.mainlevel";
            	List<WebMenuBean> mainlistt = JdbcRead.query(main_sqll, new RowMapper<WebMenuBean>() {
                    @Override
                    public WebMenuBean mapRow(ResultSet rs, int i) throws SQLException {
                        WebMenuBean menu = new WebMenuBean();
                        menu.setMainlevel(rs.getInt("mainlevel"));
                        menu.setMainlevel_desc(rs.getString("main_desc")); 
                        menu.setMenu_url(rs.getString("menu_url"));
                        return menu;
                    }
                });
            	if(mainlistt.size()>0) {
            		WebMenuBean lbean = mainlistt.get(0);
            		menuBean.setMenu_url(lbean.getMenu_url());	
            	}

            }
            menuBean.setMenuList(submenulist);
            menulist.add(menuBean);
            
          //  System.out.println(menulist);
        }
        } catch (Exception e) {
			logger.info("Exception in AdminDaoImpl::getDynamicMenuList::" + e.getMessage());
            logger.info("Exception in AdminDaoImpl::getDynamicMenuList::" + e.getCause());
            logger.info("Exception in AdminDaoImpl::getDynamicMenuList::" + e.getClass() + "::" + e.getLocalizedMessage());
        }
        return menulist;
        
	}

	@Override
	public List<WebMenuBean> getMainMenuList(String userid, HttpServletRequest request) {		
		List<WebMenuBean> menulist = new ArrayList<WebMenuBean>();
		try {
			String sql = "select DISTINCT a.mainlevel,a.main_desc from t04_menu_web a left join t04_menu_service b on \r\n"
					+ "(a.mainlevel=b.mainlevel) where a.mainlevel !='0' and a.status = 'A' order by a.mainlevel";
			menulist = JdbcRead.query(sql, new RowMapper<WebMenuBean>() {
				public WebMenuBean mapRow(ResultSet rs, int row) throws SQLException {
					WebMenuBean bean = new WebMenuBean();
					bean.setMainlevel(rs.getInt("mainlevel"));
					bean.setMainlevel_desc(rs.getString("main_desc"));
					return bean;
				}
			});
		} catch (DataAccessException de) {
			menulist = new ArrayList<WebMenuBean>();
			logger.info("DataAccessException in AdminDaoImpl::getDynamicMenuList::" + de.getMessage());
            logger.info("DataAccessException in AdminDaoImpl::getDynamicMenuList::" + de.getCause());
            logger.info("DataAccessException in AdminDaoImpl::getDynamicMenuList::" + de.getClass() + "::" + de.getLocalizedMessage());
		} catch (Exception e) {
			menulist = new ArrayList<WebMenuBean>();
			logger.info("Exception in AdminDaoImpl::getDynamicMenuList::" + e.getMessage());
            logger.info("Exception in AdminDaoImpl::getDynamicMenuList::" + e.getCause());
            logger.info("Exception in AdminDaoImpl::getDynamicMenuList::" + e.getClass() + "::" + e.getLocalizedMessage());
        }
		return menulist;
	}

	@Override
	public List<WebMenuBean> getLevelOneList(String userid, String mainLevel, HttpServletRequest request) {		
		List<WebMenuBean> menulist = new ArrayList<WebMenuBean>();
		try {
			String sql = "select DISTINCT a.mainlevel, a.main_desc, a.level1, a.level1_desc from t04_menu_web a left join t04_menu_service b on \r\n"
					+ "(a.mainlevel=b.mainlevel) where b.agency_name='Admin' and a.mainlevel = '" + mainLevel
					+ "' and a.status = 'A' order by a.mainlevel, a.level1";
			menulist = JdbcRead.query(sql, new RowMapper<WebMenuBean>() {
				public WebMenuBean mapRow(ResultSet rs, int row) throws SQLException {
					WebMenuBean bean = new WebMenuBean();
					bean.setMainlevel(rs.getInt("mainlevel"));
					bean.setMainlevel_desc(rs.getString("main_desc"));
					bean.setLevel1(rs.getInt("level1"));
					bean.setLevel1_desc(rs.getString("level1_desc"));
					return bean;
				}
			});
		} catch (DataAccessException de) {
			menulist = new ArrayList<WebMenuBean>();
			logger.info("DataAccessException in AdminDaoImpl::getLevelOneList::" + de.getMessage());
            logger.info("DataAccessException in AdminDaoImpl::getLevelOneList::" + de.getCause());
            logger.info("DataAccessException in AdminDaoImpl::getLevelOneList::" + de.getClass() + "::" + de.getLocalizedMessage());
		} catch (Exception e) {
			menulist = new ArrayList<WebMenuBean>();
			logger.info("Exception in AdminDaoImpl::getLevelOneList::" + e.getMessage());
            logger.info("Exception in AdminDaoImpl::getLevelOneList::" + e.getCause());
            logger.info("Exception in AdminDaoImpl::getLevelOneList::" + e.getClass() + "::" + e.getLocalizedMessage());
        }
		return menulist;
	}

	@Override
	public List<WebMenuBean> getLevelTwoList(String userid, String mainLevel, String level1,
			HttpServletRequest request) {		
		List<WebMenuBean> menulist = new ArrayList<WebMenuBean>();
		try {
			String sql = "select DISTINCT a.mainlevel,a.main_desc, a.level1, a.level1_desc, a.level2, a.level2_desc  from t04_menu_web a left join t04_menu_service b on \r\n"
					+ "(a.mainlevel=b.mainlevel)  where  b.agency_name='Admin' and a.mainlevel = '" + mainLevel
					+ "' and a.level1 = '" + level1 + "'  and a.status = 'A' order by a.mainlevel, a.level1, level2";
			menulist = JdbcRead.query(sql, new RowMapper<WebMenuBean>() {
				public WebMenuBean mapRow(ResultSet rs, int row) throws SQLException {
					WebMenuBean bean = new WebMenuBean();
					bean.setMainlevel(rs.getInt("mainlevel"));
					bean.setMainlevel_desc(rs.getString("main_desc"));
					bean.setLevel1(rs.getInt("level1"));
					bean.setLevel1_desc(rs.getString("level1_desc"));
					bean.setLevel2(rs.getInt("level2"));
					bean.setLevel2_desc(rs.getString("level2_desc"));
					return bean;
				}
			});
		} catch (DataAccessException de) {
			menulist = new ArrayList<WebMenuBean>();
			logger.info("DataAccessException in AdminDaoImpl::getLevelTwoList::" + de.getMessage());
            logger.info("DataAccessException in AdminDaoImpl::getLevelTwoList::" + de.getCause());
            logger.info("DataAccessException in AdminDaoImpl::getLevelTwoList::" + de.getClass() + "::" + de.getLocalizedMessage());
		} catch (Exception e) {
			menulist = new ArrayList<WebMenuBean>();
			logger.info("Exception in AdminDaoImpl::getLevelTwoList::" + e.getMessage());
            logger.info("Exception in AdminDaoImpl::getLevelTwoList::" + e.getCause());
            logger.info("Exception in AdminDaoImpl::getLevelTwoList::" + e.getClass() + "::" + e.getLocalizedMessage());
		}
		return menulist;
	}

	@Override
	public WebMenuBean getMLevel(String userid, int mainLevel, HttpServletRequest request) {		
		WebMenuBean wmbean = new WebMenuBean();
		try {			
			wmbean = JdbcRead.queryForObject(
					"select DISTINCT a.mainlevel, a.main_desc from t04_menu_web a left join t04_menu_service b on (a.mainlevel=b.mainlevel) where b.agency_name=? and a.mainlevel = ?",
					new Object[] { userid, mainLevel }, new RowMapper<WebMenuBean>() {
						public WebMenuBean mapRow(ResultSet rs, int row) throws SQLException {
							WebMenuBean ml = new WebMenuBean();
							ml.setMainlevel(rs.getInt("mainlevel"));
							ml.setMainlevel_desc(rs.getString("main_desc"));
							return ml;
						}
					});
		} catch (DataAccessException de) {
			logger.info("DataAccessException in AdminDaoImpl::getMLevel::" + de.getMessage());
            logger.info("DataAccessException in AdminDaoImpl::getMLevel::" + de.getCause());
            logger.info("DataAccessException in AdminDaoImpl::getMLevel::" + de.getClass() + "::" + de.getLocalizedMessage());
		} catch (Exception e) {
			wmbean = new WebMenuBean();
			logger.info("Exception in AdminDaoImpl::getMLevel::" + e.getMessage());
            logger.info("Exception in AdminDaoImpl::getMLevel::" + e.getCause());
            logger.info("Exception in AdminDaoImpl::getMLevel::" + e.getClass() + "::" + e.getLocalizedMessage());
		}
		return wmbean;
	}

	@Override
	public WebMenuBean getLOne(String userid, int mainLevel, int level1, HttpServletRequest request) {		
		WebMenuBean wmbean = new WebMenuBean();
		try {			
			wmbean = JdbcRead.queryForObject(
					"select DISTINCT a.mainlevel, a.main_desc, a.level1, a.level1_desc from t04_menu_web a left join t04_menu_service b on (a.mainlevel=b.mainlevel) where b.agency_name=? and a.mainlevel =? and a.level1 = ?",
					new Object[] { userid, mainLevel, level1 }, new RowMapper<WebMenuBean>() {
						public WebMenuBean mapRow(ResultSet rs, int row) throws SQLException {
							WebMenuBean ml = new WebMenuBean();
							ml.setMainlevel(rs.getInt("mainlevel"));
							ml.setMainlevel_desc(rs.getString("main_desc"));
							ml.setLevel1(rs.getInt("level1"));
							ml.setLevel1_desc(rs.getString("level1_desc"));
							return ml;
						}
					});
		} catch (DataAccessException de) {
			logger.info("DataAccessException in AdminDaoImpl::getLOne::" + de.getMessage());
            logger.info("DataAccessException in AdminDaoImpl::getLOne::" + de.getCause());
            logger.info("DataAccessException in AdminDaoImpl::getLOne::" + de.getClass() + "::" + de.getLocalizedMessage());
		} catch (Exception e) {
			wmbean = new WebMenuBean();
			logger.info("Exception in AdminDaoImpl::getLOne::" + e.getMessage());
            logger.info("Exception in AdminDaoImpl::getLOne::" + e.getCause());
            logger.info("Exception in AdminDaoImpl::getLOne::" + e.getClass() + "::" + e.getLocalizedMessage());
		}
		return wmbean;
	}

	@Override
	public WebMenuBean getLTwo(String userid, int mainLevel, int Level1, int Level2, HttpServletRequest request) {
		WebMenuBean wmbean = new WebMenuBean();
		String sql = "";
		try {
		    sql = "select DISTINCT a.mainlevel, a.main_desc, a.level1, a.level1_desc, a.level2, a.level2_desc from t04_menu_web a left join t04_menu_service b on (a.mainlevel=b.mainlevel) where b.agency_name= '"+userid+"' and a.mainlevel = '"+mainLevel +"' and a.level1 = '"+Level1+"' and a.level2 = '"+Level2+"'";
			wmbean = JdbcRead.queryForObject(sql, new Object[] {}, new RowMapper<WebMenuBean>() {
						public WebMenuBean mapRow(ResultSet rs, int row) throws SQLException {
							WebMenuBean ml = new WebMenuBean();
							ml.setMainlevel(rs.getInt("mainlevel"));
							ml.setMainlevel_desc(rs.getString("main_desc"));
							ml.setLevel1(rs.getInt("level1"));
							ml.setLevel1_desc(rs.getString("level1_desc"));
							ml.setLevel2(rs.getInt("level2"));
							ml.setLevel2_desc(rs.getString("level2_desc"));

							return ml;
						}
					});			
		} catch (DataAccessException de) {
			logger.info("DataAccessException in AdminDaoImpl::getLTwo::" + de.getMessage());
            logger.info("DataAccessException in AdminDaoImpl::getLTwo::" + de.getCause());
            logger.info("DataAccessException in AdminDaoImpl::getLTwo::" + de.getClass() + "::" + de.getLocalizedMessage());
		} catch (Exception e) {
			wmbean = new WebMenuBean();
			logger.info("Exception in AdminDaoImpl::getLTwo::" + e.getMessage());
            logger.info("Exception in AdminDaoImpl::getLTwo::" + e.getCause());
            logger.info("Exception in AdminDaoImpl::getLTwo::" + e.getClass() + "::" + e.getLocalizedMessage());
		}
		return wmbean;
	}
	
	@Override
	public int getAddMenuContent(String userid, WebMenuBean cf, HttpServletRequest request) {       
        int i = 0;
        try {
        	SimpleDateFormat dff = new SimpleDateFormat("yyyy-MM-dd");
            String s = dff.format(new Date());
            Date cur_date = dff.parse(s);
            List<MultipartFile> uploadFiles = cf.getContent_image();    		
            String[] sql_query = new String[cf.getHeading().length];
            for (int k = 0; k < cf.getHeading().length; k++) {
            	byte[] data = Base64.encodeBase64(uploadFiles.get(k).getBytes());
            
              //  sql_query[k] = "insert into t05_add_heading_content (mainlevel, main_desc, level1, level1_desc, level2, level2_desc, level3, level3_desc, heading, sub_heading, content, content_image, cur_date, content_image_name) values "
                //		+ "('"+cf.getMainlevel()+"', '"+cf.getMainlevel_desc()+"', '"+cf.getLevel1()+"', '"+cf.getLevel1_desc()+"', '"+cf.getLevel2()+"', '"+cf.getLevel2_desc()+"', '"+cf.getLevel3()+"', '"+cf.getLevel3_desc()+"', '"+cf.getHeading()[k]+"', '"+cf.getSub_heading()[k]+"', '"+cf.getJsonContent().get(k)+"', '"+data+"', '"+cur_date+"', '"+uploadFiles.get(k).getOriginalFilename()+"')";
                    
                sql_query[k] = "insert into t05_add_heading_content (mainlevel, main_desc, level1, level1_desc, level2, level2_desc, level3, level3_desc, heading, sub_heading, content, content_image, cur_date, content_image_name) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                		        
                i = this.JdbcRead.update(sql_query[k], new Object[]{cf.getMainlevel(),cf.getMainlevel_desc(), cf.getLevel1(), cf.getLevel1_desc(), cf.getLevel2(), cf.getLevel2_desc(), cf.getLevel3(), cf.getLevel3_desc(), cf.getHeading()[k], cf.getSub_heading()[k], cf.getJsonContent().get(k).toString(), data, cur_date,  uploadFiles.get(k).getOriginalFilename() });
                    // System.out.println("kkkk---" +k+"  --- "+ cf.getContent_image().get(k)+"  --- "+ cf.getContent_image().get(k).getSize());
            }
            
        } catch (Exception e) {
            i = 0;
            logger.info("Exception in AdminDaoImpl::getAddMenuContent::" + e.getMessage());
            logger.info("Exception in AdminDaoImpl::getAddMenuContent::" + e.getCause());
            logger.info("Exception in AdminDaoImpl::getAddMenuContent::" + e.getClass() + "::" + e.getLocalizedMessage());
		}

        return i;		
	}

	
//	@Override
//	public int getAddMenuContent(String userid, WebMenuBean cf, HttpServletRequest request) {
//        System.out.println("getAddMenuContent---");
//        int i = 0;
//        try {
//        	SimpleDateFormat dff = new SimpleDateFormat("yyyy-MM-dd");
//            String s = dff.format(new Date());
//            Date cur_date = dff.parse(s);
//            List<MultipartFile> uploadFiles = cf.getContent_image();
//    		System.out.println("getAddMenuContent---"+cf.getJsonContent().get(0)+"  --- "+cf.getContent_image()+"   --- "+cf.getContent_image().size()+"  --- "+cf.getContent_image().get(0).getSize());
//            String[] sql_query = new String[cf.getHeading().length];
//            for (int k = 0; k < cf.getHeading().length; k++) {
//            	System.out.println("uploadFiles=== "+uploadFiles.get(k).getOriginalFilename()+" ------ "+uploadFiles.get(k).getBytes());
//            	byte[] data = Base64.encodeBase64(uploadFiles.get(k).getBytes());
//            	//data = IOUtils.toByteArray(uploadFiles.get(k).getInputStream());
//            	String resut1 = new String(data);
//    	        System.out.println("resut777777777777771------------  "+resut1);
//            	System.out.println("data+++++++=== "+data); // cf.getContent_image().get(k)
//                sql_query[k] = "insert into t05_add_heading_content (mainlevel, main_desc, level1, level1_desc, level2, level2_desc, level3, level3_desc, heading, sub_heading, content, content_image, cur_date, content_image_name) values "
//                		+ "('"+cf.getMainlevel()+"', '"+cf.getMainlevel_desc()+"', '"+cf.getLevel1()+"', '"+cf.getLevel1_desc()+"', '"+cf.getLevel2()+"', '"+cf.getLevel2_desc()+"', '"+cf.getLevel3()+"', '"+cf.getLevel3_desc()+"', '"+cf.getHeading()[k]+"', '"+cf.getSub_heading()[k]+"', '"+cf.getJsonContent().get(k)+"', '"+data+"', '"+cur_date+"', '"+uploadFiles.get(k).getOriginalFilename()+"')";
//                     System.out.println("len---" + sql_query[k]);
//                    // System.out.println("kkkk---" +k+"  --- "+ cf.getContent_image().get(k)+"  --- "+ cf.getContent_image().get(k).getSize());
//            }
//            int[] j = JdbcRead.batchUpdate(sql_query);                                   
//            if (j.length > 0) {
//                System.out.println("Success..");
//            } else {
//                System.out.println("Fail..");
//            }
//            System.out.println("sql-11-" + sql_query);
//        } catch (Exception e) {
//            i = 0;
//            System.out.println("Exception in AdminDaoImpl.getAddMenuContent() ::" + e.fillInStackTrace());
//        }
////      JsonArray jsonArray = new JsonArray();
////		for(int r = 0; r < cf.getContent().length; r++) {
////			JsonObject x = new JsonObject();
////			x.addProperty("content", cf.getContent()[r]);
////			jsonArray.add(x);
////		}
////		cf.setContent_menu(jsonArray.toString());
//        return i;		
//	}
	
	

	@Override
	public List<WebMenuBean> getMenuContent(String userid, int mainLevel, int Level1, int Level2, HttpServletRequest request){		
		List<WebMenuBean> menulist = new ArrayList<WebMenuBean>();
		String sql = "";
		String where = "";
		try {
			if(mainLevel != 0) {
				where = "where mainlevel = '"+mainLevel+"' and level1 = '"+Level1+"'  and level2 = '"+Level2+"' order by sl_no";				
			}
			sql = "select mainlevel, main_desc, level1, level1_desc, level2, level2_desc, level3, level3_desc, heading, sub_heading, content, content_image, cur_date from t05_add_heading_content "+where+"";
			menulist = JdbcRead.query(sql, new RowMapper<WebMenuBean>() {
				public WebMenuBean mapRow(ResultSet rs, int row) throws SQLException {
					WebMenuBean bean = new WebMenuBean();
					bean.setMainlevel(rs.getInt("mainlevel"));
					bean.setMainlevel_desc(rs.getString("main_desc"));
					bean.setLevel1(rs.getInt("level1"));
					bean.setLevel1_desc(rs.getString("level1_desc"));
					bean.setLevel2(rs.getInt("level2"));
					bean.setLevel2_desc(rs.getString("level2_desc"));
					bean.setHead(rs.getString("heading"));
					bean.setSub_head(rs.getString("sub_heading"));
					bean.setContent_menu(rs.getString("content"));
					bean.setImagefile(rs.getBytes(12));					
					String resut1 = new String(bean.getImagefile());	
					return bean;
				}
			});
		} catch (DataAccessException de) {
			menulist = new ArrayList<WebMenuBean>();
			logger.info("DataAccessException in AdminDaoImpl::getMenuContent::" + de.getMessage());
            logger.info("DataAccessException in AdminDaoImpl::getMenuContent::" + de.getCause());
            logger.info("DataAccessException in AdminDaoImpl::getMenuContent::" + de.getClass() + "::" + de.getLocalizedMessage());
		} catch (Exception e) {
			menulist = new ArrayList<WebMenuBean>();
				logger.info("Exception in AdminDaoImpl::getMenuContent::" + e.getMessage());
	            logger.info("Exception in AdminDaoImpl::getMenuContent::" + e.getCause());
	            logger.info("Exception in AdminDaoImpl::getMenuContent::" + e.getClass() + "::" + e.getLocalizedMessage());
			}
		return menulist;
		
	}
	
	
	@Override
	public List<WebMenuBean> getMainLevelSize(String userid, WebMenuBean cf, HttpServletRequest request) {
		List<WebMenuBean> menulist = new ArrayList<WebMenuBean>();
		String sql = "";
		try {
			sql = "select mainlevel, main_desc, level1, level1_desc, level2, level2_desc, level3, level3_desc, heading, sub_heading, content, content_image, cur_date from t05_add_heading_content";
			menulist = JdbcRead.query(sql, new RowMapper<WebMenuBean>() {
				public WebMenuBean mapRow(ResultSet rs, int row) throws SQLException {
					WebMenuBean bean = new WebMenuBean();
					bean.setMainlevel(rs.getInt("mainlevel"));
					bean.setMainlevel_desc(rs.getString("main_desc"));
					bean.setLevel1(rs.getInt("level1"));
					bean.setLevel1_desc(rs.getString("level1_desc"));
					bean.setLevel2(rs.getInt("level2"));
					bean.setLevel2_desc(rs.getString("level2_desc"));
					bean.setHead(rs.getString("heading"));
					bean.setSub_head(rs.getString("sub_heading"));
					bean.setContent_menu(rs.getString("content"));
					
					return bean;
				}
			});
		} catch (DataAccessException de) {
			menulist = new ArrayList<WebMenuBean>();
			logger.info("DataAccessException in AdminDaoImpl::getMainLevelSize::" + de.getMessage());
            logger.info("DataAccessException in AdminDaoImpl::getMainLevelSize::" + de.getCause());
            logger.info("DataAccessException in AdminDaoImpl::getMainLevelSize::" + de.getClass() + "::" + de.getLocalizedMessage());
		} catch (Exception e) {
			menulist = new ArrayList<WebMenuBean>();
				logger.info("Exception in AdminDaoImpl::getMainLevelSize::" + e.getMessage());
	            logger.info("Exception in AdminDaoImpl::getMainLevelSize::" + e.getCause());
	            logger.info("Exception in AdminDaoImpl::getMainLevelSize::" + e.getClass() + "::" + e.getLocalizedMessage());
			}
		return menulist;				
	}

	@Override
	public WebMenuBean searchMainLevel(String userid, String maindesc, HttpServletRequest request) {		
//		String sql = "select DISTINCT a.mainlevel, a.main_desc from t04_menu_web a left join t04_menu_service b on (a.mainlevel=b.mainlevel) where LOWER(trim(b.agency_name))='"+userid.toLowerCase().trim()+"' and LOWER(trim(a.main_desc)) = '"+maindesc.toLowerCase().trim()+"' and a.status = 'A'";
		String sql = "select DISTINCT a.mainlevel, a.main_desc from t04_menu_web a left join t04_menu_service b on (a.mainlevel=b.mainlevel) where  LOWER(trim(a.main_desc)) = '"+maindesc.toLowerCase().trim()+"' and a.status = 'A'";
		try {					
			WebMenuBean	wmbean = JdbcRead.query(sql, new Object[]{}, new ResultSetExtractor<WebMenuBean>() {
	            @Override
	            public WebMenuBean extractData(ResultSet rs) throws SQLException, DataAccessException {
	                if (rs.next()) {
	                	WebMenuBean ml = new WebMenuBean();
						ml.setMainlevel(rs.getInt("mainlevel"));
						ml.setMainlevel_desc(rs.getString("main_desc"));
						return ml;
	                }
	                return null;
	            }
	        });				
			return wmbean;
		} catch (DataAccessException de) {
			logger.info("DataAccessException in AdminDaoImpl::searchMainLevel::" + de.getMessage());
            logger.info("DataAccessException in AdminDaoImpl::searchMainLevel::" + de.getCause());
            logger.info("DataAccessException in AdminDaoImpl::searchMainLevel::" + de.getClass() + "::" + de.getLocalizedMessage());
			return null;
		} catch (Exception e) {
			logger.info("Exception in AdminDaoImpl::searchMainLevel::" + e.getMessage());
            logger.info("Exception in AdminDaoImpl::searchMainLevel::" + e.getCause());
            logger.info("Exception in AdminDaoImpl::searchMainLevel::" + e.getClass() + "::" + e.getLocalizedMessage());
			return null;
		}	
	}
	
	@Override
	public WebMenuBean searchLevelOne(String userid, WebMenuBean cf, HttpServletRequest request) {		
		try {
			String sql = "select DISTINCT a.mainlevel, a.main_desc, a.level1, a.level1_desc from t04_menu_web a left join t04_menu_service b on \r\n"
					+ "(a.mainlevel=b.mainlevel) where  a.mainlevel = '" + cf.getMainlevel() + "' and LOWER(a.level1_desc) = '" + cf.getLevel1_desc().toLowerCase().trim() + "' and a.status = 'A' order by a.mainlevel, a.level1";
			System.out.println("sql==1===="+sql);
			WebMenuBean	wmbean = JdbcRead.query(sql, new Object[]{}, new ResultSetExtractor<WebMenuBean>() {
	            @Override
	            public WebMenuBean extractData(ResultSet rs) throws SQLException, DataAccessException {
	                if (rs.next()) {
					WebMenuBean bean = new WebMenuBean();
					bean.setMainlevel(rs.getInt("mainlevel"));
					bean.setMainlevel_desc(rs.getString("main_desc"));
					bean.setLevel1(rs.getInt("level1"));
					bean.setLevel1_desc(rs.getString("level1_desc"));
					return bean;
				}
	               
	                return null;
	            }
			});			
			return wmbean;
		} catch (DataAccessException de) {
			logger.info("DataAccessException in AdminDaoImpl::searchLevelOne::" + de.getMessage());
            logger.info("DataAccessException in AdminDaoImpl::searchLevelOne::" + de.getCause());
            logger.info("DataAccessException in AdminDaoImpl::searchLevelOne::" + de.getClass() + "::" + de.getLocalizedMessage());
			return null;
		} catch (Exception e) {
			logger.info("Exception in AdminDaoImpl::searchLevelOne::" + e.getMessage());
            logger.info("Exception in AdminDaoImpl::searchLevelOne::" + e.getCause());
            logger.info("Exception in AdminDaoImpl::searchLevelOne::" + e.getClass() + "::" + e.getLocalizedMessage());
			return null;
		}	
	}
	
	@Override
	public List<WebMenuBean> getLevelOneSize(String userid, WebMenuBean cf, HttpServletRequest request) {
		List<WebMenuBean> menulist = new ArrayList<WebMenuBean>();
		try {
//			String sql = "select DISTINCT a.mainlevel, a.main_desc, a.level1, a.level1_desc from t04_menu_web a left join t04_menu_service b on \r\n"
//					+ "(a.mainlevel=b.mainlevel) where  a.mainlevel = '" + cf.getMainlevel() + "' and a.level1 != '0' order by a.mainlevel, a.level1";
			String sql = "select COALESCE(max(a.level1),0)level1 from t04_menu_web a left join t04_menu_service b on \r\n"
					+ "(a.mainlevel=b.mainlevel) where  a.mainlevel = '" + cf.getMainlevel() + "' and a.level1 != '0'";
			System.out.println("sql===="+sql);
			menulist = JdbcRead.query(sql,	new Object[] {}, new RowMapper<WebMenuBean>() {
				public WebMenuBean mapRow(ResultSet rs, int row) throws SQLException {
					WebMenuBean bean = new WebMenuBean();
//					bean.setMainlevel(rs.getInt("mainlevel"));
//					bean.setMainlevel_desc(rs.getString("main_desc"));
					bean.setLevel1(rs.getInt("level1"));
//					bean.setLevel1_desc(rs.getString("level1_desc"));
					return bean;
				}
			});
		} catch (DataAccessException de) {
			menulist = new ArrayList<WebMenuBean>();
			logger.info("DataAccessException in AdminDaoImpl::getLevelOneSize::" + de.getMessage());
            logger.info("DataAccessException in AdminDaoImpl::getLevelOneSize::" + de.getCause());
            logger.info("DataAccessException in AdminDaoImpl::getLevelOneSize::" + de.getClass() + "::" + de.getLocalizedMessage());
			
		} catch (Exception e) {
			menulist = new ArrayList<WebMenuBean>();
			logger.info("Exception in AdminDaoImpl::getLevelOneSize::" + e.getMessage());
            logger.info("Exception in AdminDaoImpl::getLevelOneSize::" + e.getCause());
            logger.info("Exception in AdminDaoImpl::getLevelOneSize::" + e.getClass() + "::" + e.getLocalizedMessage());
			
		}	
		return menulist;
	}
	
	@Override
	public WebMenuBean searchLevelTwo(String userid, WebMenuBean cf, HttpServletRequest request) {
		try {
			String sql = "select DISTINCT a.mainlevel, a.main_desc, a.level1, a.level1_desc, a.level2, a.level2_desc from t04_menu_web a left join t04_menu_service b on \r\n"
					+ "(a.mainlevel=b.mainlevel) where  a.mainlevel = '" + cf.getMainlevel() + "' and LOWER(a.level1_desc) = '" + cf.getLevel1_desc().toLowerCase().trim() + "'"
					+ "and a.level1 = '" + cf.getLevel1() + "' and LOWER(a.level2_desc) = '" + cf.getLevel2_desc().toLowerCase().trim() + "' and a.status = 'A' order by a.mainlevel, a.level1";
			WebMenuBean	wmbean = JdbcRead.query(sql, new Object[]{}, new ResultSetExtractor<WebMenuBean>() {
	            @Override
	            public WebMenuBean extractData(ResultSet rs) throws SQLException, DataAccessException {
	                if (rs.next()) {
	                WebMenuBean bean = new WebMenuBean();	
					bean.setMainlevel(rs.getInt("mainlevel"));
					bean.setMainlevel_desc(rs.getString("main_desc"));
					bean.setLevel1(rs.getInt("level1"));
					bean.setLevel1_desc(rs.getString("level1_desc"));
					bean.setLevel2(rs.getInt("level2"));
					bean.setLevel2_desc(rs.getString("level2_desc"));
					return bean;
				}
	                return null;
	            }
			});
			return wmbean;
		} catch (DataAccessException de) {
			logger.info("DataAccessException in AdminDaoImpl::searchLevelTwo::" + de.getMessage());
            logger.info("DataAccessException in AdminDaoImpl::searchLevelTwo::" + de.getCause());
            logger.info("DataAccessException in AdminDaoImpl::searchLevelTwo::" + de.getClass() + "::" + de.getLocalizedMessage());
			return null;
		} catch (Exception e) {
			logger.info("Exception in AdminDaoImpl::searchLevelTwo::" + e.getMessage());
            logger.info("Exception in AdminDaoImpl::searchLevelTwo::" + e.getCause());
            logger.info("Exception in AdminDaoImpl::searchLevelTwo::" + e.getClass() + "::" + e.getLocalizedMessage());
			return null;
		}	
	}
	
	@Override
	public List<WebMenuBean> getLevelTwoSize(String userid, WebMenuBean cf, HttpServletRequest request) {
		List<WebMenuBean> menulist = new ArrayList<WebMenuBean>();
		try {
//			String sql = "select DISTINCT a.mainlevel, a.main_desc, a.level1, a.level1_desc, a.level2, a.level2_desc from t04_menu_web a left join t04_menu_service b on \r\n"
//					+ "(a.mainlevel=b.mainlevel) where  a.mainlevel = '" + cf.getMainlevel() + "'and a.level1 = '" + cf.getLevel1() + "' and a.level2!= '0' order by a.mainlevel, a.level1, a.level2";
			String sql = "select COALESCE(max(a.level2),0)level2 from t04_menu_web a left join t04_menu_service b on \r\n"
					+ "(a.mainlevel=b.mainlevel) where  a.mainlevel = '" + cf.getMainlevel() + "'and a.level1 = '" + cf.getLevel1() + "' and a.level2!= '0'";
			menulist = JdbcRead.query(sql, new RowMapper<WebMenuBean>() {
				public WebMenuBean mapRow(ResultSet rs, int row) throws SQLException {
					WebMenuBean bean = new WebMenuBean();
//					bean.setMainlevel(rs.getInt("mainlevel"));
//					bean.setMainlevel_desc(rs.getString("main_desc"));
//					bean.setLevel1(rs.getInt("level1"));
//					bean.setLevel1_desc(rs.getString("level1_desc"));
					bean.setLevel2(rs.getInt("level2"));
//					bean.setLevel2_desc(rs.getString("level2_desc"));
					return bean;
				}
			});
		} catch (DataAccessException de) {
			menulist = new ArrayList<WebMenuBean>();
			logger.info("DataAccessException in AdminDaoImpl::getLevelTwoSize::" + de.getMessage());
            logger.info("DataAccessException in AdminDaoImpl::getLevelTwoSize::" + de.getCause());
            logger.info("DataAccessException in AdminDaoImpl::getLevelTwoSize::" + de.getClass() + "::" + de.getLocalizedMessage());
			
		} catch (Exception e) {
			menulist = new ArrayList<WebMenuBean>();
			logger.info("Exception in AdminDaoImpl::getLevelTwoSize::" + e.getMessage());
            logger.info("Exception in AdminDaoImpl::getLevelTwoSize::" + e.getCause());
            logger.info("Exception in AdminDaoImpl::getLevelTwoSize::" + e.getClass() + "::" + e.getLocalizedMessage());
			
		}	
		return menulist;
	}
	
	@Override
    public int createMenu(String user_id, WebMenuBean cf, HttpServletRequest request) {
        int i = 0;
        try {
            String query = "insert into t04_menu_web (mainlevel, main_desc, level1, level1_desc, level2, level2_desc, level3, level3_desc, menu_url, status,lang) values (?,?,?,?,?,?,?,?,?,?,?)";
            i = this.JdbcWrite.update(query, new Object[]{cf.getMainlevel(), cf.getMainlevel_desc().trim(), cf.getLevel1(), cf.getLevel1_desc().trim(), cf.getLevel2(), cf.getLevel2_desc().trim(), cf.getLevel3(), cf.getLevel3_desc(), cf.getMenu_url(), 'A', 'E'});
            if (cf.getMainlevel_desc_hi().trim() != null && cf.getMainlevel_desc_hi().trim().length() > 0) {
            	String query1 = "insert into t04_menu_web (mainlevel, main_desc, level1, level1_desc, level2, level2_desc, level3, level3_desc, menu_url, status,lang) values (?,?,?,?,?,?,?,?,?,?,?)";
                int j = this.JdbcWrite.update(query1, new Object[]{cf.getMainlevel(), cf.getMainlevel_desc_hi().trim(), cf.getLevel1(), cf.getLevel1_desc_hi().trim(), cf.getLevel2(), cf.getLevel2_desc_hi().trim(), cf.getLevel3(), cf.getLevel3_desc_hi(), cf.getMenu_url(), 'A', 'H'});
            }
            
         
        } catch (DataAccessException de) {
        	i = 0;
        	de.printStackTrace();
        	logger.info("DataAccessException in AdminDaoImpl::createMenu::" + de.getMessage());
            logger.info("DataAccessException in AdminDaoImpl::createMenu::" + de.getCause());
            logger.info("DataAccessException in AdminDaoImpl::createMenu::" + de.getClass() + "::" + de.getLocalizedMessage());
			
		} catch (Exception e) {
			i = 0;
			e.printStackTrace();
			logger.info("Exception in AdminDaoImpl::createMenu::" + e.getMessage());
            logger.info("Exception in AdminDaoImpl::createMenu::" + e.getCause());
            logger.info("Exception in AdminDaoImpl::createMenu::" + e.getClass() + "::" + e.getLocalizedMessage());
			
		}	
        System.out.println("i==="+i);   
        return i;
    }
	
	@Override
	public int createMenu_Service(String role_id, WebMenuBean cf, HttpServletRequest request) {
        int i = 0;
        try {
            String query = "insert into t04_menu_service (role_id, mainlevel, level1, level2, level3, remarks) values (?,?,?,?,?,?)";

            i = this.JdbcWrite.update(query, new Object[]{role_id, cf.getMainlevel(), cf.getLevel1(), cf.getLevel2(), 0, ""});

        } catch (DataAccessException de) {            
            logger.info("DataAccessException in AdminDaoImpl::createMenu_Service::" + de.getMessage());
            logger.info("DataAccessException in AdminDaoImpl::createMenu_Service::" + de.getCause());
            logger.info("DataAccessException in AdminDaoImpl::createMenu_Service::" + de.getClass() + "::" + de.getLocalizedMessage());
			
		} catch (Exception e) {			
			logger.info("Exception in AdminDaoImpl::createMenu_Service::" + e.getMessage());
            logger.info("Exception in AdminDaoImpl::createMenu_Service::" + e.getCause());
            logger.info("Exception in AdminDaoImpl::createMenu_Service::" + e.getClass() + "::" + e.getLocalizedMessage());
			
		}	
        return i;
		
	}
	

	@Override
	public List<WebMenuBean> getEditMenu(String userid, HttpServletRequest request) {		
		List<WebMenuBean> menulist = new ArrayList<WebMenuBean>();
		String sql = "";
		try {
			sql = "select mainlevel, main_desc, level1, level1_desc, level2, level2_desc, level3, level3_desc, menu_url, status from t04_menu_web where lang='E' order by mainlevel, level1, level2";			
			menulist = JdbcRead.query(sql, new RowMapper<WebMenuBean>() {
				public WebMenuBean mapRow(ResultSet rs, int row) throws SQLException {
					WebMenuBean bean = new WebMenuBean();
					bean.setMainlevel(rs.getInt("mainlevel"));
					bean.setMainlevel_desc(rs.getString("main_desc"));
					bean.setLevel1(rs.getInt("level1"));
					bean.setLevel1_desc(rs.getString("level1_desc"));
					bean.setLevel2(rs.getInt("level2"));
					bean.setLevel2_desc(rs.getString("level2_desc"));
					bean.setLevel3(rs.getInt("level3"));
					bean.setLevel3_desc(rs.getString("level3_desc"));
					bean.setMenu_url(rs.getString("menu_url"));
					bean.setStatus(rs.getString("status"));
					
					return bean;
				}
			});
		} catch (DataAccessException de) {
			menulist = new ArrayList<WebMenuBean>();
			 	logger.info("DataAccessException in AdminDaoImpl::getEditMenu::" + de.getMessage());
	            logger.info("DataAccessException in AdminDaoImpl::getEditMenu::" + de.getCause());
	            logger.info("DataAccessException in AdminDaoImpl::getEditMenu::" + de.getClass() + "::" + de.getLocalizedMessage());
				
			} catch (Exception e) {			
				menulist = new ArrayList<WebMenuBean>();
				logger.info("Exception in AdminDaoImpl::getEditMenu::" + e.getMessage());
	            logger.info("Exception in AdminDaoImpl::getEditMenu::" + e.getCause());
	            logger.info("Exception in AdminDaoImpl::getEditMenu::" + e.getClass() + "::" + e.getLocalizedMessage());
				
			}	
		return menulist;				
	}
	
	
	@Override
	public WebMenuBean getFillDetails(String userid, WebMenuBean cf, HttpServletRequest request) {		
		String sql = "select mainlevel, main_desc, level1, level1_desc, level2, level2_desc, level3, level3_desc, menu_url, status from t04_menu_web where mainlevel = ? and level1 = ? and level2 = ? and level3 = ?";
		try {					
			WebMenuBean	wmbean = JdbcRead.query(sql, new Object[]{cf.getMainlevel(), cf.getLevel1(), cf.getLevel2(), cf.getLevel3()}, new ResultSetExtractor<WebMenuBean>() {
	            @Override
	            public WebMenuBean extractData(ResultSet rs) throws SQLException, DataAccessException {
	                if (rs.next()) {
	                	WebMenuBean ml = new WebMenuBean();
	                	ml.setMainlevel(rs.getInt("mainlevel"));
	                	ml.setMainlevel_desc(rs.getString("main_desc"));
	                	ml.setLevel1(rs.getInt("level1"));
	                	ml.setLevel1_desc(rs.getString("level1_desc"));
	                	ml.setLevel2(rs.getInt("level2"));
	                	ml.setLevel2_desc(rs.getString("level2_desc"));
	                	ml.setLevel3(rs.getInt("level3"));
	                	ml.setLevel3_desc(rs.getString("level3_desc"));
	                	ml.setMenu_url(rs.getString("menu_url"));
	                	ml.setStatus(rs.getString("status"));
						return ml;
	                }
	                return null;
	            }
	        });	
			return wmbean;
		} catch (DataAccessException de) {
			logger.info("DataAccessException in AdminDaoImpl::getFillDetails::" + de.getMessage());
            logger.info("DataAccessException in AdminDaoImpl::getFillDetails::" + de.getCause());
            logger.info("DataAccessException in AdminDaoImpl::getFillDetails::" + de.getClass() + "::" + de.getLocalizedMessage());
			return null;
		} catch (Exception e) {						
			logger.info("Exception in AdminDaoImpl::getFillDetails::" + e.getMessage());
            logger.info("Exception in AdminDaoImpl::getFillDetails::" + e.getCause());
            logger.info("Exception in AdminDaoImpl::getFillDetails::" + e.getClass() + "::" + e.getLocalizedMessage());
			return null;		
		}	
	}
	
	@Override
    public int editMenu(String user_id, WebMenuBean cf, HttpServletRequest request) {
        int i = 0, count = 0;
        try {
            String query = "update t04_menu_web set  main_desc = ?,  level1_desc = ?, level2_desc = ?,  level3_desc = ?,  status = ?,menu_url=? where mainlevel = ? and level1 = ? and level2 = ? and level3 =? and lang='E'";
            i = JdbcWrite.update(query, new Object[]{ cf.getMainlevel_desc().trim(), cf.getLevel1_desc().trim(),  cf.getLevel2_desc().trim(), cf.getLevel3_desc(), cf.getStatus(), cf.getMenu_url(), cf.getMainlevel(), cf.getLevel1(), cf.getLevel2(), cf.getLevel3()});
            if (cf.getMainlevel_desc_hi().trim() != null && cf.getMainlevel_desc_hi().trim().length() > 0) {
            	count = JdbcWrite.queryForObject("select count(*) from t04_menu_web where mainlevel = ? and level1 = ? and level2 = ? and level3 =? and lang='H'", new Object[] {cf.getMainlevel(), cf.getLevel1(), cf.getLevel2(), cf.getLevel3()},Integer.class);
            	if(count>0) {
            	String query1 = "update t04_menu_web set  main_desc = ?,  level1_desc = ?, level2_desc = ?,  level3_desc = ?,  status = ?,menu_url=? where mainlevel = ? and level1 = ? and level2 = ? and level3 =? and lang='H'";
                int j = JdbcWrite.update(query1, new Object[]{ cf.getMainlevel_desc_hi().trim(), cf.getLevel1_desc_hi().trim(),  cf.getLevel2_desc_hi().trim(), cf.getLevel3_desc_hi(), cf.getStatus(), cf.getMenu_url(), cf.getMainlevel(), cf.getLevel1(), cf.getLevel2(), cf.getLevel3()});
            	}else {
            		String query1 = "insert into t04_menu_web (mainlevel, main_desc, level1, level1_desc, level2, level2_desc, level3, level3_desc, menu_url, status,lang) values (?,?,?,?,?,?,?,?,?,?,?)";
                    int j = this.JdbcWrite.update(query1, new Object[]{cf.getMainlevel(), cf.getMainlevel_desc_hi().trim(), cf.getLevel1(), cf.getLevel1_desc_hi().trim(), cf.getLevel2(), cf.getLevel2_desc_hi().trim(), cf.getLevel3(), cf.getLevel3_desc_hi(), cf.getMenu_url(), 'A', 'H'});	
            	}
            }
        } catch (DataAccessException de) {
        	de.printStackTrace();
        	logger.info("DataAccessException in AdminDaoImpl::editMenu::" + de.getMessage());
            logger.info("DataAccessException in AdminDaoImpl::editMenu::" + de.getCause());
            logger.info("DataAccessException in AdminDaoImpl::editMenu::" + de.getClass() + "::" + de.getLocalizedMessage());
		} catch (Exception e) {	
			e.printStackTrace();
			logger.info("Exception in AdminDaoImpl::editMenu::" + e.getMessage());
            logger.info("Exception in AdminDaoImpl::editMenu::" + e.getCause());
            logger.info("Exception in AdminDaoImpl::editMenu::" + e.getClass() + "::" + e.getLocalizedMessage());
		}	     
        return i;
    }
	
	  @Override
	    public List<WebMenuBean> getStatusList() {
	        List<WebMenuBean> list = new ArrayList<WebMenuBean>();
	        try {
	            String qry = "select status, status_key from t01_user_status order by slno";
	            list = JdbcRead.query(qry, new Object[]{}, new RowMapper<WebMenuBean>() {
	                public WebMenuBean mapRow(ResultSet rs, int rowNum) throws SQLException {
	                	WebMenuBean bean = new WebMenuBean();
	                    bean.setStatus(rs.getString("status"));
	                    bean.setStatus_key(rs.getString("status_key"));
	                    return bean;
	                }
	            });

	        } catch (DataAccessException e) {
	            list = new ArrayList<>();
	            logger.info("Exception in AdminDaoImpl::getStatusList::" + e.getMessage());
	            logger.info("Exception in AdminDaoImpl::getStatusList::" + e.getCause());
	            logger.info("Exception in AdminDaoImpl::getStatusList::" + e.getClass() + "::" + e.getLocalizedMessage());
			}	
	        return list;
	    }
	  
	  @Override
	    public int deleteMenu(String user_id, WebMenuBean cf, HttpServletRequest request) {
	        int i = 0;
	        try {
	            String sql = "delete from t04_menu_web where mainlevel = ? and level1 = ? and level2 = ? and level3 = ?";
	            i = JdbcWrite.update(sql, new Object[]{cf.getMainlevel(), cf.getLevel1(), cf.getLevel2(), cf.getLevel3()});
	        } catch (DataAccessException de) {
	        	logger.info("DataAccessException in AdminDaoImpl::deleteMenu::" + de.getMessage());
	            logger.info("DataAccessException in AdminDaoImpl::deleteMenu::" + de.getCause());
	            logger.info("DataAccessException in AdminDaoImpl::deleteMenu::" + de.getClass() + "::" + de.getLocalizedMessage());
			} catch (Exception e) {			
				logger.info("Exception in AdminDaoImpl::deleteMenu::" + e.getMessage());
	            logger.info("Exception in AdminDaoImpl::deleteMenu::" + e.getCause());
	            logger.info("Exception in AdminDaoImpl::deleteMenu::" + e.getClass() + "::" + e.getLocalizedMessage());
			}	
	        return i;
	    }

	@Override
	public List<WebMenuBean> getDynamicMenuServicesList(HttpServletRequest request) {
        List<WebMenuBean> menulist = new ArrayList<WebMenuBean>();
        WebMenuBean menuBean = null;
        WebMenuBean subbean = null;
        WebMenuBean subsubbean = null; 
        try {
        String main_sql = "select DISTINCT a.mainlevel,a.main_desc from t04_menu_web  a left join t04_menu_service b on(a.mainlevel=b.mainlevel)  order by a.mainlevel";
        List<WebMenuBean> mainlist = JdbcRead.query(main_sql, new RowMapper<WebMenuBean>() {
            @Override
            public WebMenuBean mapRow(ResultSet rs, int i) throws SQLException {
                WebMenuBean menu = new WebMenuBean();
                menu.setMainlevel(rs.getInt("mainlevel"));
                menu.setMainlevel_desc(rs.getString("main_desc"));  
                return menu;
            }
        });
        for (int i = 0; i < mainlist.size(); i++) {
            WebMenuBean bean = mainlist.get(i);
            menuBean = new WebMenuBean();
            menuBean.setMainlevel(bean.getMainlevel());
            menuBean.setMainlevel_desc(bean.getMainlevel_desc());
          
            String sql = "select DISTINCT a.mainlevel, a.level1, a.level1_desc from t04_menu_web a left join t04_menu_service b on(a.mainlevel=b.mainlevel and a.level1=b.level1)"
                    + " where  a.mainlevel="+bean.getMainlevel()+" and a.level1 !=0 order by a.mainlevel, a.level1";
            List<WebMenuBean> list = JdbcRead.query(sql, new RowMapper<WebMenuBean>() {
                @Override
                public WebMenuBean mapRow(ResultSet rs, int i) throws SQLException {
                    WebMenuBean menu = new WebMenuBean();
                    menu.setMainlevel(rs.getInt("mainlevel"));
                    menu.setLevel1(rs.getInt("level1"));
                    menu.setLevel1_desc(rs.getString("level1_desc"));

                    return menu;
                }
            });

            List<WebMenuBean> submenulist = new ArrayList<WebMenuBean>();
            if(list.size()>0) {
            for (int j = 0; j < list.size(); j++) {
                WebMenuBean subMenu = list.get(j);
                subbean = new WebMenuBean();
                subbean.setMainlevel(subMenu.getMainlevel());
                subbean.setLevel1(subMenu.getLevel1());
                subbean.setLevel1_desc(subMenu.getLevel1_desc());
                
                String sql1 = "select DISTINCT a.mainlevel, a.level1, a.level2, a.level2_desc from t04_menu_web a left join t04_menu_service b on(a.mainlevel=b.mainlevel and a.level1=b.level1 and a.level2=b.level2) where  a.mainlevel='" + subMenu.getMainlevel() + "' and a.level1='" + subMenu.getLevel1() + "' and a.level2 !=0 order by a.mainlevel, a.level1, a.level2";
                List<WebMenuBean> sublist = JdbcRead.query(sql1, new RowMapper<WebMenuBean>() {
                    @Override
                    public WebMenuBean mapRow(ResultSet rs, int i) throws SQLException {
                        WebMenuBean menu = new WebMenuBean();
                        menu.setMainlevel(rs.getInt("mainlevel"));
                        menu.setLevel1(rs.getInt("level1"));
                        menu.setLevel2(rs.getInt("level2"));
                        menu.setLevel2_desc(rs.getString("level2_desc"));
                        return menu;
                    }
                });
                List<WebMenuBean> submenu3 = new ArrayList<WebMenuBean>();
                if(sublist.size()>0) {
                for (int k = 0; k < sublist.size(); k++) {
                    String menu_url = "";
                    WebMenuBean subsubMenu = sublist.get(k);
                    subsubbean = new WebMenuBean();
                    subsubbean.setMainlevel(subsubMenu.getMainlevel());
                    subsubbean.setLevel1(subsubMenu.getLevel1());
                    subsubbean.setLevel2(subsubMenu.getLevel2());
                    subsubbean.setLevel2_desc(subsubMenu.getLevel2_desc());                  
                    String sql2 = "select DISTINCT a.mainlevel, a.level1, a.level2, A.level2_desc, a.level3, a.level3_desc, a.menu_url from t04_menu_web a left join t04_menu_service b on (a.mainlevel=b.mainlevel and a.level1=b.level1 and a.level2=b.level2 and a.level3=b.level3) where a.mainlevel='" + subsubMenu.getMainlevel() + "' and a.level1='" + subsubMenu.getLevel1() + "' and a.level2='" + subsubMenu.getLevel2() + "' and a.level3 !=0 order by a.level2, a.level3";
                    List<WebMenuBean> subsubMenulist = JdbcRead.query(sql2, new RowMapper<WebMenuBean>() {
                        @Override
                        public WebMenuBean mapRow(ResultSet rs, int i) throws SQLException {
                            WebMenuBean menu = new WebMenuBean();
                            menu.setMainlevel(rs.getInt("mainlevel"));
                            menu.setLevel1(rs.getInt("level1"));
                            menu.setLevel2(rs.getInt("level2"));
                            menu.setLevel2_desc(rs.getString("level2_desc"));
                            menu.setLevel3(rs.getInt("level3"));
                            menu.setLevel3_desc(rs.getString("level3_desc"));
                            menu.setMenu_url(rs.getString("menu_url"));

                            return menu;
                        }
                    });
                    if (subsubMenulist.size() == 0) {                        
                        String sql3 = "select DISTINCT a.mainlevel, a.level1, a.level2, a.level2_desc, a.menu_url from t04_menu_web a left join t04_menu_service b on(a.mainlevel=b.mainlevel and a.level1=b.level1 and a.level2=b.level2) where a.mainlevel='" + subsubMenu.getMainlevel() + "' and a.level1='" + subsubMenu.getLevel1() + "' and a.level2='" + subsubMenu.getLevel2() + "' order by a.level2";
                        List<WebMenuBean> subsubMenulist1 = JdbcRead.query(sql3, new RowMapper<WebMenuBean>() {
                            @Override
                            public WebMenuBean mapRow(ResultSet rs, int i) throws SQLException {
                                WebMenuBean menu = new WebMenuBean();
                                menu.setMainlevel(rs.getInt("mainlevel"));
                                menu.setLevel1(rs.getInt("level1"));
                                menu.setLevel2(rs.getInt("level2"));
                                menu.setLevel2_desc(rs.getString("level2_desc"));
                                menu.setMenu_url(rs.getString("menu_url"));
                                return menu;
                            }
                        });
                        if (subsubMenulist1.size() > 0) {
                            WebMenuBean lbean = subsubMenulist1.get(0);
                            subsubbean.setMenu_url(lbean.getMenu_url());
                        }
                    }
                    subsubbean.setMenuList(subsubMenulist);
                    submenu3.add(subsubbean);
                }
                }else {
                	String sql33 = "select DISTINCT a.mainlevel, a.level1, a.level1_desc,a.menu_url from t04_menu_web a left join t04_menu_service b on(a.mainlevel=b.mainlevel and a.level1=b.level1)"
                            + " where a.mainlevel='" + subMenu.getMainlevel() + "' and a.level1='" + subMenu.getLevel1() + "'  order by a.mainlevel, a.level1";
                    List<WebMenuBean> list1 = JdbcRead.query(sql33, new RowMapper<WebMenuBean>() {
                        @Override
                        public WebMenuBean mapRow(ResultSet rs, int i) throws SQLException {
                            WebMenuBean menu = new WebMenuBean();
                            menu.setMainlevel(rs.getInt("mainlevel"));
                            menu.setLevel1(rs.getInt("level1"));
                            menu.setLevel1_desc(rs.getString("level1_desc"));
                            menu.setMenu_url(rs.getString("menu_url"));
                            return menu;
                        }
                    });                    
                    if(list1.size()>0) {
                    	WebMenuBean lbean = list1.get(0);
                    	subbean.setMenu_url(lbean.getMenu_url());	
                    }
                }
                subbean.setSubMenu(submenu3);
                submenulist.add(subbean);
            }
            }else {
            	String main_sqll = "select DISTINCT a.mainlevel,a.main_desc,a.menu_url from t04_menu_web  a left join t04_menu_service b on(a.mainlevel=b.mainlevel) where a.mainlevel="+bean.getMainlevel()+" order by a.mainlevel";
            	List<WebMenuBean> mainlistt = JdbcRead.query(main_sqll, new RowMapper<WebMenuBean>() {
                    @Override
                    public WebMenuBean mapRow(ResultSet rs, int i) throws SQLException {
                        WebMenuBean menu = new WebMenuBean();
                        menu.setMainlevel(rs.getInt("mainlevel"));
                        menu.setMainlevel_desc(rs.getString("main_desc")); 
                        menu.setMenu_url(rs.getString("menu_url"));
                        return menu;
                    }
                });
            	if(mainlistt.size()>0) {
            		WebMenuBean lbean = mainlistt.get(0);
            		menuBean.setMenu_url(lbean.getMenu_url());	
            	}
            }
            menuBean.setMenuList(submenulist);
            menulist.add(menuBean);            
        }
        } catch (Exception e) {			
			logger.info("Exception in AdminDaoImpl::getDynamicMenuServicesList::" + e.getMessage());
            logger.info("Exception in AdminDaoImpl::getDynamicMenuServicesList::" + e.getCause());
            logger.info("Exception in AdminDaoImpl::getDynamicMenuServicesList::" + e.getClass() + "::" + e.getLocalizedMessage());
		}
        return menulist;
        
	}

	@Override
	public List<WebMenuBean> getDynamicMenuList2() {
		List<WebMenuBean> menulist = new ArrayList<WebMenuBean>();
		List<WebMenuBean> mainlist = null;
		WebMenuBean menuBean = null;
		WebMenuBean subbean = null;
        final WebMenuBean subsubbean = null;
        try {
//        String main_sql = "select DISTINCT mainlevel,MAIN_DESC from t04_menu_web where mainlevel not in(12) order by mainlevel";
        String main_sql = "select DISTINCT mainlevel,MAIN_DESC from t04_menu_web  order by mainlevel";
        	mainlist = JdbcRead.query(main_sql, new RowMapper<WebMenuBean>() {
            @Override
            public WebMenuBean mapRow(final ResultSet rs, int i) throws SQLException {
                WebMenuBean menu = new WebMenuBean();
                menu.setMainlevel(rs.getInt("mainlevel"));
                menu.setMainlevel_desc(rs.getString("MAIN_DESC"));
                menu.setLevel1(0);
                menu.setLevel2(0);
                menu.setLevel3(0);
                String sql = "";
                sql = "select DISTINCT mainlevel,LEVEL1,LEVEL1_DESC from t04_menu_web  where  mainlevel='" + rs.getInt("mainlevel") + "' order by mainlevel,LEVEL1";
                List<WebMenuBean> list = JdbcRead.query(sql, new RowMapper<WebMenuBean>() {
                    @Override
                    public WebMenuBean mapRow(final ResultSet rs1, int i) throws SQLException {
                    	WebMenuBean menu1 = new WebMenuBean();
                        menu1.setMainlevel(rs1.getInt("mainlevel"));
                        menu1.setLevel1(rs1.getInt("LEVEL1"));
                        menu1.setLevel1_desc(rs1.getString("LEVEL1_DESC"));
                        menu1.setLevel2(0);
                        menu1.setLevel3(0);
                        
                        String sql1 = "select DISTINCT mainlevel,LEVEL1,LEVEL2,LEVEL2_DESC from t04_menu_web where mainlevel='" + rs.getInt("mainlevel") + "' and level1='" + rs1.getInt("LEVEL1") + "'  order by mainlevel,LEVEL1,LEVEL2";
                        List<WebMenuBean> sublist = JdbcRead.query(sql1, new RowMapper<WebMenuBean>() {
                            @Override
                            public WebMenuBean mapRow(final ResultSet rs2, int i) throws SQLException {
                            	WebMenuBean menu2 = new WebMenuBean();
                                menu2.setMainlevel(rs2.getInt("mainlevel"));
                                menu2.setLevel1(rs2.getInt("LEVEL1"));
                                menu2.setLevel2(rs2.getInt("LEVEL2"));
                                menu2.setLevel2_desc(rs2.getString("LEVEL2_DESC"));
                                menu2.setLevel3(0);
                                String sql2 = "select DISTINCT mainlevel,MAIN_DESC,LEVEL1,LEVEL1_DESC,LEVEL2,LEVEL2_DESC,LEVEL3,LEVEL3_DESC,MENU_URL from t04_menu_web  where mainlevel='" + rs.getInt("mainlevel") + "' and level1='" + rs1.getInt("LEVEL1") + "' and level2='" + rs2.getInt("LEVEL2") + "'  order by mainlevel,LEVEL1,LEVEL2,LEVEL3";
                                List<WebMenuBean> subsubMenulist = JdbcRead.query(sql2, new RowMapper<WebMenuBean>() {
                                    @Override
                                    public WebMenuBean mapRow(ResultSet rs3, int i) throws SQLException {
                                    	WebMenuBean menu3 = new WebMenuBean();
                                        menu3.setMainlevel(rs3.getInt("mainlevel"));
                                        menu3.setLevel1(rs3.getInt("LEVEL1"));
                                        menu3.setLevel2(rs3.getInt("LEVEL2"));
                                        menu3.setLevel3(rs3.getInt("LEVEL3"));
                                        menu3.setLevel3_desc(rs3.getString("LEVEL3_DESC"));
                                        return menu3;
                                    }
                                });     
                                menu2.setMenuList(subsubMenulist);
                                return menu2;
                            }
                        });     
                        menu1.setMenuList(sublist);
                        return menu1;
                    }
                });     
                menu.setMenuList(list);
                return menu;
            }
        });  
        } catch (Exception e) {			
			logger.info("Exception in AdminDaoImpl::getDynamicMenuList2::" + e.getMessage());
            logger.info("Exception in AdminDaoImpl::getDynamicMenuList2::" + e.getCause());
            logger.info("Exception in AdminDaoImpl::getDynamicMenuList2::" + e.getClass() + "::" + e.getLocalizedMessage());
		}
        return mainlist;
	}

	@Override
	public int insertMenuServices(WebMenuBean bean,String[] checkedSerivesArray) {
		int i = 0;
        String serviceQry = "";
        try {  
        	UserDetails userDetails = loginUSerController.getUserDetails();
			String username = userDetails.getUsername();
            for (int k = 0; k < checkedSerivesArray.length; k++) {            	
            serviceQry = "insert into t04_menu_service(role_id,mainlevel,level1,level2,level3,remarks,login_name) values(?,?,?,?,?,?,?)";
            String menulinks = checkedSerivesArray[k]; 
            String menulinks1[] = menulinks.split("#");
            i = JdbcRead.update(serviceQry, new Object[]{Integer.parseInt(bean.getRole()),Integer.parseInt(menulinks1[0]),Integer.parseInt(menulinks1[1]),Integer.parseInt(menulinks1[2]),Integer.parseInt(menulinks1[3]),"Department instructions",username});
            }                            
        } catch (Exception e) {
        	logger.info("Exception in AdminDaoImpl::insertMenuServices::" + e.getMessage());
            logger.info("Exception in AdminDaoImpl::insertMenuServices::" + e.getCause());
            logger.info("Exception in AdminDaoImpl::insertMenuServices::" + e.getClass() + "::" + e.getLocalizedMessage());
		}
        return i;
	}

	@Override
	public String getServices(WebMenuBean bean) {
		List ServicesList = new ArrayList();
        String services=""; 
       try {        
        String main_sql = "select *from t04_menu_web a, t04_menu_service b where a.MAINLEVEL=b.MAINLEVEL and a.LEVEL1=b.LEVEL1 and a.LEVEL2=b.LEVEL2 and a.LEVEL3=b.LEVEL3 and b.role_id='"+bean.getRole()+"' order by a.MAINLEVEL, a.LEVEL1,a.LEVEL2,a.LEVEL3";
        ServicesList = JdbcRead.query(main_sql, new RowMapper<WebMenuBean>() {
            @Override
            public WebMenuBean mapRow(final ResultSet rs, int i) throws SQLException {
            	WebMenuBean form = new WebMenuBean();                
                form.setService(rs.getString("MAINLEVEL")+"#"+rs.getString("LEVEL1")+"#"+rs.getString("LEVEL2")+"#"+rs.getString("LEVEL3"));       
                return form;
            }        
        });
        	WebMenuBean af=new WebMenuBean();   
            for(int y=0;y<ServicesList.size();y++){
            af=(WebMenuBean)ServicesList.get(y);
            if(services=="")
            services=services+af.getService();     
            else    
            services=services+"~"+af.getService(); 
            }
       }catch(Exception e){          
    		logger.info("Exception in AdminDaoImpl::getServices::" + e.getMessage());
            logger.info("Exception in AdminDaoImpl::getServices::" + e.getCause());
            logger.info("Exception in AdminDaoImpl::getServices::" + e.getClass() + "::" + e.getLocalizedMessage());
		}
       return services;
	}

	@Override
	public int updateMenuServices(WebMenuBean bean, String[] checkedSerivesArray) {
		int i = 0;
        String serviceQry = "";
        try { 
           String qry ="delete from t04_menu_service where role_id=?";
          int j = JdbcRead.update(qry, new Object[]{Integer.parseInt(bean.getRole())});   
            for (int k = 0; k < checkedSerivesArray.length; k++) {
            serviceQry = "insert into t04_menu_service(role_id,MAINLEVEL,LEVEL1,LEVEL2,LEVEL3,remarks) values(?,?,?,?,?,?)";
            String menulinks = checkedSerivesArray[k]; 
            String menulinks1[] = menulinks.split("#");            
            i = JdbcRead.update(serviceQry, new Object[]{Integer.parseInt(bean.getRole()),Integer.parseInt(menulinks1[0]),Integer.parseInt(menulinks1[1]),Integer.parseInt(menulinks1[2]),Integer.parseInt(menulinks1[3]),"Department instructions"});    
           
            }                        
        }catch(Exception e){          
    		logger.info("Exception in AdminDaoImpl::updateMenuServices::" + e.getMessage());
            logger.info("Exception in AdminDaoImpl::updateMenuServices::" + e.getCause());
            logger.info("Exception in AdminDaoImpl::updateMenuServices::" + e.getClass() + "::" + e.getLocalizedMessage());
		}
        return i;
	}

	@Override
	public WebMenuBean searchLevelThree(String userid, WebMenuBean cf, HttpServletRequest request) {		
		try {
			String sql = "select DISTINCT a.mainlevel, a.main_desc, a.level1, a.level1_desc, a.level2, a.level2_desc, a.level3, a.level3_desc from t04_menu_web a left join t04_menu_service b on \r\n"
					+ "(a.mainlevel=b.mainlevel) where  a.mainlevel = '" + cf.getMainlevel() + "'"
					+ "and a.level1 = '" + cf.getLevel1() + "' and LOWER(a.level2_desc) = '" + cf.getLevel2_desc().toLowerCase().trim() + "' and a.level2 = '" + cf.getLevel2() + "' and LOWER(a.level3_desc) = '" + cf.getLevel3_desc().toLowerCase().trim() + "' and a.status = 'A' order by a.mainlevel, a.level1, a.level2";
			WebMenuBean	wmbean = JdbcRead.query(sql, new Object[]{}, new ResultSetExtractor<WebMenuBean>() {
	            @Override
	            public WebMenuBean extractData(ResultSet rs) throws SQLException, DataAccessException {
	                if (rs.next()) {
	                WebMenuBean bean = new WebMenuBean();	
					bean.setMainlevel(rs.getInt("mainlevel"));
					bean.setMainlevel_desc(rs.getString("main_desc"));
					bean.setLevel1(rs.getInt("level1"));
					bean.setLevel1_desc(rs.getString("level1_desc"));
					bean.setLevel2(rs.getInt("level2"));
					bean.setLevel2_desc(rs.getString("level2_desc"));
					bean.setLevel3(rs.getInt("level3"));
					bean.setLevel3_desc(rs.getString("level3_desc"));
					return bean;
				}
	                return null;
	            }
			});
			return wmbean;
		} catch (DataAccessException de) {
			logger.info("DataAccessException in AdminDaoImpl::searchLevelThree::" + de.getMessage());
            logger.info("DataAccessException in AdminDaoImpl::searchLevelThree::" + de.getCause());
            logger.info("DataAccessException in AdminDaoImpl::searchLevelThree::" + de.getClass() + "::" + de.getLocalizedMessage());
            return null;
		} catch (Exception e) {			
			logger.info("Exception in AdminDaoImpl::searchLevelThree::" + e.getMessage());
            logger.info("Exception in AdminDaoImpl::searchLevelThree::" + e.getCause());
            logger.info("Exception in AdminDaoImpl::searchLevelThree::" + e.getClass() + "::" + e.getLocalizedMessage());
            return null;
		}	
	}

	@Override
	public List<WebMenuBean> getLevelThreeSize(String userid, WebMenuBean cf, HttpServletRequest request) {
		List<WebMenuBean> menulist = new ArrayList<WebMenuBean>();
		try {
//			String sql = "select DISTINCT a.mainlevel, a.main_desc, a.level1, a.level1_desc, a.level2, a.level2_desc, a.level3, a.level3_desc from t04_menu_web a left join t04_menu_service b on \r\n"
//					+ "(a.mainlevel=b.mainlevel) where  a.mainlevel = '" + cf.getMainlevel() + "' and a.level1 = '" + cf.getLevel1() + "' and a.level2 = '" + cf.getLevel2() + "' and a.level3!= '0' order by a.mainlevel, a.level1, a.level2, a.level3";
			String sql = "select COALESCE(max(a.level3),0)level3 from t04_menu_web a left join t04_menu_service b on \r\n"
					+ "(a.mainlevel=b.mainlevel) where  a.mainlevel = '" + cf.getMainlevel() + "' and a.level1 = '" + cf.getLevel1() + "' and a.level2 = '" + cf.getLevel2() + "' and a.level3!= '0'";
			menulist = JdbcRead.query(sql, new RowMapper<WebMenuBean>() {
				public WebMenuBean mapRow(ResultSet rs, int row) throws SQLException {
					WebMenuBean bean = new WebMenuBean();
//					bean.setMainlevel(rs.getInt("mainlevel"));
//					bean.setMainlevel_desc(rs.getString("main_desc"));
//					bean.setLevel1(rs.getInt("level1"));
//					bean.setLevel1_desc(rs.getString("level1_desc"));
//					bean.setLevel2(rs.getInt("level2"));
//					bean.setLevel2_desc(rs.getString("level2_desc"));
					bean.setLevel3(rs.getInt("level3"));
//					bean.setLevel3_desc(rs.getString("level3_desc"));
					return bean;
				}
			});
			
		} catch (DataAccessException de) {
			menulist = new ArrayList<WebMenuBean>();
			logger.info("DataAccessException in AdminDaoImpl::getLevelThreeSize::" + de.getMessage());
            logger.info("DataAccessException in AdminDaoImpl::getLevelThreeSize::" + de.getCause());
            logger.info("DataAccessException in AdminDaoImpl::getLevelThreeSize::" + de.getClass() + "::" + de.getLocalizedMessage());
		} catch (Exception e) {			
			menulist = new ArrayList<WebMenuBean>();
			logger.info("Exception in AdminDaoImpl::getLevelThreeSize::" + e.getMessage());
            logger.info("Exception in AdminDaoImpl::getLevelThreeSize::" + e.getCause());
            logger.info("Exception in AdminDaoImpl::getLevelThreeSize::" + e.getClass() + "::" + e.getLocalizedMessage());
		}	
		return menulist;
	}

	@Override
	public List<Role> getRolesList(String username) {
		List<Role> roleslist = new ArrayList<Role>();
		String sql = "";
		try {
			if(!username.equalsIgnoreCase("") && !username.equalsIgnoreCase("null")) {			
//			    sql = "select distinct id,role_name,role_desc from roles where id !='4'";
				sql = "select distinct id,role_name,role_desc from roles";
			}else {
				sql = "select distinct id,role_name,role_desc from roles where id ='4'";
			}
			
			roleslist = JdbcRead.query(sql,	new Object[] {}, new RowMapper<Role>() {
				public Role mapRow(ResultSet rs, int row) throws SQLException {
					Role bean = new Role();
					bean.setRole_name(rs.getString("role_name"));					
					return bean;
				}
			});
		} catch (DataAccessException de) {
			roleslist = new ArrayList<Role>();
			logger.info("DataAccessException in AdminDaoImpl::getRolesList::" + de.getMessage());
            logger.info("DataAccessException in AdminDaoImpl::getRolesList::" + de.getCause());
            logger.info("DataAccessException in AdminDaoImpl::getRolesList::" + de.getClass() + "::" + de.getLocalizedMessage());
			
		} catch (Exception e) {
			roleslist = new ArrayList<Role>();
			logger.info("Exception in AdminDaoImpl::getRolesList::" + e.getMessage());
            logger.info("Exception in AdminDaoImpl::getRolesList::" + e.getCause());
            logger.info("Exception in AdminDaoImpl::getRolesList::" + e.getClass() + "::" + e.getLocalizedMessage());
			
		}	
		return roleslist;
	}
	
	
}
