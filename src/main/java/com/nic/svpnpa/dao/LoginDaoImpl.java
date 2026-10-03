/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nic.svpnpa.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;


import java.util.ArrayList;
import java.util.List;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.nic.svpnpa.model.LoginBean;
import com.nic.svpnpa.model.WebMenuBean;

import jakarta.servlet.http.*;


@Repository
public class LoginDaoImpl implements LoginDao {

    private static final Logger logger = LogManager.getLogger(LoginDaoImpl.class);
    @Autowired
    private JdbcTemplate JdbcWrite;

    @Autowired
    private JdbcTemplate JdbcRead;

	/*
	 * @Override public LoginBean getLoginAuthentication(LoginBean login,
	 * HttpServletRequest request) { logger.
	 * info("----------- inside LoginDaoImpl.getLoginAuthentication------------- ");
	 * LoginBean lbean = new LoginBean(); try { System.out.println("JdbcDMSRead--" +
	 * JdbcRead); lbean = JdbcRead.
	 * queryForObject("select trim(user_name)as user_id,trim(encpwd)as pwd, trim(user_desc)as user_des,trim(user_role)as user_role, status from login_users where user_name=?"
	 * , new Object[]{login.getUser_id()}, new RowMapper<LoginBean>() { public
	 * LoginBean mapRow(ResultSet rs, int row) throws SQLException { LoginBean login
	 * = new LoginBean(); login.setUser_id(rs.getString("user_id"));
	 * login.setPassword(rs.getString("pwd"));
	 * login.setUser_desc(rs.getString("user_des"));
	 * login.setUser_role(rs.getString("user_role"));
	 * login.setStatus(rs.getString("status"));
	 * 
	 * return login; } }); } catch (DataAccessException de) { de.fillInStackTrace();
	 * System.out.println("DataAccessException--" + de.getMostSpecificCause()); }
	 * catch (Exception e) { lbean = new LoginBean(); logger.
	 * error("Exception in  LoginDaoImpl.getLoginAuthentication------------- ", e);
	 * }
	 * 
	 * return lbean; }
	 * 
	 * @Override public int getLoginPwdUpdate(LoginBean login, HttpServletRequest
	 * request) { System.out.println("getLoginPwdUpdate---" + login.getUser_id());
	 * int k = 0; try { String sql_upd = "update login_users set encpwd = '" +
	 * login.getNew_pwd() + "' where user_name='" + login.getUser_id() + "' ";
	 * System.out.println("getLoginPwdUpdate=upd====" + k + " -- " + sql_upd); k =
	 * JdbcRead.update(sql_upd); } catch (Exception e) {
	 * System.out.println("Exception getLoginPwdUpdate in ::::" +
	 * e.fillInStackTrace()); logger.error("Exception in Slot Dashboard :::: ", e);
	 * } return k; }
	 * 
	 * @Override public int audit_Trails(LoginBean login, HttpServletRequest
	 * request, String log_type, String status, String process) {
	 * System.out.println("getLoginPwdUpdate---" + login.getUser_id()); int i = 0;
	 * try { String ipAddress = request.getHeader("X-FORWARDED-FOR"); if (ipAddress
	 * == null) { ipAddress = request.getRemoteAddr(); } SimpleDateFormat dff = new
	 * SimpleDateFormat("yyyy-MM-dd"); String x = dff.format(new Date()); Date
	 * cur_date = dff.parse(x);
	 * 
	 * SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH.mm.ss"); String
	 * cur_time_stamp = df.format(new Date()); int tenent_id = 1;
	 * 
	 * String sql =
	 * "insert into audit_trails (cur_date, cur_time_stamp, ip_address, log_type, login_name, status, process, tenent_id) values (?,?,?,?,?,?,?,?)"
	 * ; int j = JdbcWrite.update(sql, new Object[]{cur_date, cur_time_stamp,
	 * ipAddress, log_type, login.getUser_id(), status, process, tenent_id});
	 * 
	 * if (j > 0) { System.out.println("audit_Trails Success.."); } else {
	 * System.out.println("audit_Trails Fail.."); }
	 * 
	 * } catch (Exception e) { i = 0;
	 * System.out.println("Exception in LoginDaoImpl.audit_Trails() ::" +
	 * e.fillInStackTrace()); } return i; }
	 * 
	 * @Override public int createPWDLog(LoginBean login, HttpServletRequest
	 * request) { int i = 0; try { String ipAddress =
	 * request.getHeader("X-FORWARDED-FOR"); if (ipAddress == null) { ipAddress =
	 * request.getRemoteAddr(); } SimpleDateFormat dff = new
	 * SimpleDateFormat("yyyy-MM-dd"); String x = dff.format(new Date()); Date
	 * cur_date = dff.parse(x);
	 * 
	 * SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH.mm.ss"); String
	 * cur_time_stamp = df.format(new Date()); int tenent_id = 1; String sql =
	 * "insert into password_log (cur_date, cur_time_stamp, ip_address, password, login_name, tenent_id) values (?,?,?,?,?,?)"
	 * ; int j = JdbcRead.update(sql, new Object[]{cur_date, cur_time_stamp,
	 * ipAddress, login.getNew_pwd(), login.getUser_id(), tenent_id});
	 * 
	 * if (j > 0) { System.out.println("createPWDLog Success.."); } else {
	 * System.out.println("createPWDLog Fail.."); }
	 * 
	 * } catch (Exception e) { i = 0;
	 * System.out.println("Exception in LoginDaoImpl.createPWDLog() ::" +
	 * e.fillInStackTrace()); } return i; }
	 */

//    public List<WebMenuBean> getDynamicMenuList_old(String userid) {
//        List<WebMenuBean> menulist = new ArrayList<WebMenuBean>();
//        WebMenuBean menuBean = null;
//        WebMenuBean subbean = null;
//        WebMenuBean subsubbean = null;
//        
//      //  int role_id = JdbcRead.queryForObject("select distinct b.role_id from users a left join user_roles b on a.id=b.user_id where a.username='"+userid+"'",Integer.class);
//        
//        String main_sql = "select DISTINCT a.mainlevel,a.main_desc from t04_menu_web  a left join t04_menu_service b on(a.mainlevel=b.mainlevel) where b.agency_name='" + userid.trim() + "' order by a.mainlevel";
//        // String main_sql = "select DISTINCT a.mainlevel,A.MAIN_DESC from SROUSER.IGRS_MENU_WEB a left join SROUSER.IGRS_MENU_SERVICE b on(a.mainlevel=b.mainlevel) where b.AGENCY_NAME='" + userid.trim() + "' order by a.mainlevel";
//       // logger.info("main query--" + main_sql);
//        System.out.println("main sql---" + main_sql);
//        List<WebMenuBean> mainlist = JdbcRead.query(main_sql, new RowMapper<WebMenuBean>() {
//            @Override
//            public WebMenuBean mapRow(ResultSet rs, int i) throws SQLException {
//                WebMenuBean menu = new WebMenuBean();
//                menu.setMainlevel(rs.getInt("mainlevel"));
//                menu.setMainlevel_desc(rs.getString("main_desc"));  
//                return menu;
//            }
//        });
//        System.out.println("size of mainlist..........."+mainlist.size());
//        for (int i = 0; i < mainlist.size(); i++) {
//            WebMenuBean bean = mainlist.get(i);
//            menuBean = new WebMenuBean();
//            menuBean.setMainlevel(bean.getMainlevel());
//            menuBean.setMainlevel_desc(bean.getMainlevel_desc());
//          
//            String sql = "select DISTINCT a.mainlevel, a.level1, a.level1_desc from t04_menu_web a left join t04_menu_service b on(a.mainlevel=b.mainlevel and a.level1=b.level1)"
//                    + " where b.agency_name='" + userid.trim() + "' and a.mainlevel="+bean.getMainlevel()+" and a.level1 !=0 order by a.mainlevel, a.level1";
//            // String sql = "select DISTINCT a.mainlevel,A.LEVEL1,A.LEVEL1_DESC from SROUSER.IGRS_MENU_WEB a left join SROUSER.IGRS_MENU_SERVICE b on(a.mainlevel=b.mainlevel and a.level1=b.level1) where b.AGENCY_NAME='" + userid.trim() + "' and   a.mainlevel='" + bean.getMainlevel() + "' order by a.mainlevel,A.LEVEL1";
//           // logger.info("sql----::"+sql);
//            System.out.println("sql sql---" + sql);
//            List<WebMenuBean> list = JdbcRead.query(sql, new RowMapper<WebMenuBean>() {
//                @Override
//                public WebMenuBean mapRow(ResultSet rs, int i) throws SQLException {
//                    WebMenuBean menu = new WebMenuBean();
//                    menu.setMainlevel(rs.getInt("mainlevel"));
//                    menu.setLevel1(rs.getInt("level1"));
//                    menu.setLevel1_desc(rs.getString("level1_desc"));
//
//                    return menu;
//                }
//            });
//
//            List<WebMenuBean> submenulist = new ArrayList<WebMenuBean>();
//            if(list.size()>0) {
//            for (int j = 0; j < list.size(); j++) {
//                WebMenuBean subMenu = list.get(j);
//                subbean = new WebMenuBean();
//                subbean.setMainlevel(subMenu.getMainlevel());
//                subbean.setLevel1(subMenu.getLevel1());
//                subbean.setLevel1_desc(subMenu.getLevel1_desc());
//                
//                String sql1 = "select DISTINCT a.mainlevel, a.level1, a.level2, a.level2_desc from t04_menu_web a left join t04_menu_service b on(a.mainlevel=b.mainlevel and a.level1=b.level1 and a.level2=b.level2) where b.agency_name='" + userid.trim() + "' and a.mainlevel='" + subMenu.getMainlevel() + "' and a.level1='" + subMenu.getLevel1() + "' and a.level2 !=0 order by a.mainlevel, a.level1, a.level2";
//                //String sql1 = "select DISTINCT a.mainlevel,A.LEVEL1,A.LEVEL2,A.LEVEL2_DESC from SROUSER.IGRS_MENU_WEB a left join SROUSER.IGRS_MENU_SERVICE b on(a.mainlevel=b.mainlevel and a.level1=b.level1 and a.level2=b.level2) where b.AGENCY_NAME='" + userid.trim() + "' and a.mainlevel='" + subMenu.getMainlevel() + "' and a.level1='" + subMenu.getLevel1() + "' order by a.mainlevel,A.LEVEL1, A.LEVEL2";
//               // logger.info("sql1-------::"+sql1);
//                System.out.println("sql1 sql1---" + sql1);
//                List<WebMenuBean> sublist = JdbcRead.query(sql1, new RowMapper<WebMenuBean>() {
//                    @Override
//                    public WebMenuBean mapRow(ResultSet rs, int i) throws SQLException {
//                        WebMenuBean menu = new WebMenuBean();
//                        menu.setMainlevel(rs.getInt("mainlevel"));
//                        menu.setLevel1(rs.getInt("level1"));
//
//                        menu.setLevel2(rs.getInt("level2"));
//                        menu.setLevel2_desc(rs.getString("level2_desc"));
//
//                        return menu;
//                    }
//                });
////                ----------------------
//                List<WebMenuBean> submenu3 = new ArrayList<WebMenuBean>();
//                if(sublist.size()>0) {
//                for (int k = 0; k < sublist.size(); k++) {
//                    String menu_url = "";
//                    WebMenuBean subsubMenu = sublist.get(k);
//                    subsubbean = new WebMenuBean();
//                    subsubbean.setMainlevel(subsubMenu.getMainlevel());
//                    subsubbean.setLevel1(subsubMenu.getLevel1());
//                    subsubbean.setLevel2(subsubMenu.getLevel2());
//                    subsubbean.setLevel2_desc(subsubMenu.getLevel2_desc());
////                ---------------------------
////                subsubbean.setLevel3(0);
////                --------------------------                    
//                    String sql2 = "select DISTINCT a.mainlevel, a.level1, a.level2, A.level2_desc, a.level3, a.level3_desc, a.menu_url from t04_menu_web a left join t04_menu_service b on (a.mainlevel=b.mainlevel and a.level1=b.level1 and a.level2=b.level2 and a.level3=b.level3) where b.agency_name='" + userid.trim() + "' and a.mainlevel='" + subsubMenu.getMainlevel() + "' and a.level1='" + subsubMenu.getLevel1() + "' and a.level2='" + subsubMenu.getLevel2() + "' and a.level3 !=0 order by a.level2, a.level3";
//
//                    //  String sql2 = "select DISTINCT a.mainlevel,A.LEVEL1,A.LEVEL2,A.LEVEL2_DESC,A.LEVEL3,A.LEVEL3_DESC,A.MENU_URL from SROUSER.IGRS_MENU_WEB a left join SROUSER.IGRS_MENU_SERVICE b on(a.mainlevel=b.mainlevel and a.level1=b.level1 and a.level2=b.level2 and a.level3=b.level3) where b.AGENCY_NAME='" + userid.trim() + "' and a.mainlevel='" + subsubMenu.getMainlevel() + "' and a.level1='" + subsubMenu.getLevel1() + "' and a.level2='" + subsubMenu.getLevel2() + "' and a.level3 !=0 order by A.LEVEL2,A.LEVEL3";
//                    //String sql2 = "select DISTINCT a.mainlevel,A.LEVEL1,A.LEVEL2,A.LEVEL2_DESC,A.LEVEL3,A.LEVEL3_DESC,A.MENU_URL from SROUSER.IGRS_MENU_WEB a left join SROUSER.IGRS_MENU_SERVICE b on(a.mainlevel=b.mainlevel and a.level1=b.level1 and a.level2=b.level2) where b.AGENCY_NAME='" + username.trim() + "' and a.mainlevel='" + subsubMenu.getMainlevel() + "' and a.level1='" + subsubMenu.getLevel1() + "' and a.level2='" + subsubMenu.getLevel2() + "' and a.level3 =0 order by A.LEVEL2,A.LEVEL3";
//                //    logger.info("level3---" + sql2);
//                    System.out.println("level3 size---" + sql2);
//                    List<WebMenuBean> subsubMenulist = JdbcRead.query(sql2, new RowMapper<WebMenuBean>() {
//                        @Override
//                        public WebMenuBean mapRow(ResultSet rs, int i) throws SQLException {
//                            WebMenuBean menu = new WebMenuBean();
//                            menu.setMainlevel(rs.getInt("mainlevel"));
//                            menu.setLevel1(rs.getInt("level1"));
//                            menu.setLevel2(rs.getInt("level2"));
//                            menu.setLevel2_desc(rs.getString("level2_desc"));
//                            menu.setLevel3(rs.getInt("level3"));
//                            menu.setLevel3_desc(rs.getString("level3_desc"));
//                            menu.setMenu_url(rs.getString("menu_url"));
//
//                            return menu;
//                        }
//                    });
//                    System.out.println("list size---" + subsubMenulist.size());
//                    //logger.info("list size---" + subsubMenulist.size());
//                    if (subsubMenulist.size() == 0) {
//
//                        
//                        String sql3 = "select DISTINCT a.mainlevel, a.level1, a.level2, a.level2_desc, a.menu_url from t04_menu_web a left join t04_menu_service b on(a.mainlevel=b.mainlevel and a.level1=b.level1 and a.level2=b.level2) where b.agency_name='" + userid.trim() + "' and  a.mainlevel='" + subsubMenu.getMainlevel() + "' and a.level1='" + subsubMenu.getLevel1() + "' and a.level2='" + subsubMenu.getLevel2() + "' order by a.level2";
//                    //    logger.info("level23---" + sql3);
//                        System.out.println("level23---" + sql3);
//                        List<WebMenuBean> subsubMenulist1 = JdbcRead.query(sql3, new RowMapper<WebMenuBean>() {
//                            //int j=1;
//                            @Override
//                            public WebMenuBean mapRow(ResultSet rs, int i) throws SQLException {
//                                //logger.info("j::"+j);
//                                WebMenuBean menu = new WebMenuBean();
//                                menu.setMainlevel(rs.getInt("mainlevel"));
//                                menu.setLevel1(rs.getInt("level1"));
//                                menu.setLevel2(rs.getInt("level2"));
//                                menu.setLevel2_desc(rs.getString("level2_desc"));
//                                menu.setMenu_url(rs.getString("menu_url"));
//                                //++j;
//                                return menu;
//
//                            }
//
//                        });
//                        //logger.info("size--" + subsubMenulist1.size());
//                        System.out.println("size--" + subsubMenulist1.size());
//                        if (subsubMenulist1.size() > 0) {
//                            WebMenuBean lbean = subsubMenulist1.get(0);
//                            subsubbean.setMenu_url(lbean.getMenu_url());
//                        }
//
//                    }
//
//                    //logger.info("list size---" + subsubMenulist.size());
//                    System.out.println("list size---" + subsubMenulist.size());
//                    subsubbean.setMenuList(subsubMenulist);
//                    submenu3.add(subsubbean);
//
//                }
////                ------------------
//                }else {
//                	String sql33 = "select DISTINCT a.mainlevel, a.level1, a.level1_desc,a.menu_url from t04_menu_web a left join t04_menu_service b on(a.mainlevel=b.mainlevel and a.level1=b.level1)"
//                            + " where b.agency_name='" + userid.trim() + "' and a.mainlevel='" + subMenu.getMainlevel() + "' and a.level1='" + subMenu.getLevel1() + "'  order by a.mainlevel, a.level1";
//                    // String sql = "select DISTINCT a.mainlevel,A.LEVEL1,A.LEVEL1_DESC from SROUSER.IGRS_MENU_WEB a left join SROUSER.IGRS_MENU_SERVICE b on(a.mainlevel=b.mainlevel and a.level1=b.level1) where b.AGENCY_NAME='" + userid.trim() + "' and   a.mainlevel='" + bean.getMainlevel() + "' order by a.mainlevel,A.LEVEL1";
//                   // logger.info("sql----::"+sql);
//                    System.out.println("sql sql---" + sql33);
//                    List<WebMenuBean> list1 = JdbcRead.query(sql33, new RowMapper<WebMenuBean>() {
//                        @Override
//                        public WebMenuBean mapRow(ResultSet rs, int i) throws SQLException {
//                            WebMenuBean menu = new WebMenuBean();
//                            menu.setMainlevel(rs.getInt("mainlevel"));
//                            menu.setLevel1(rs.getInt("level1"));
//                            menu.setLevel1_desc(rs.getString("level1_desc"));
//                            menu.setMenu_url(rs.getString("menu_url"));
//
//                            return menu;
//                        }
//                    });
//                    
//                    if(list1.size()>0) {
//                    	WebMenuBean lbean = list1.get(0);
//                    	subbean.setMenu_url(lbean.getMenu_url());	
//                    }
//
//                }
//                subbean.setSubMenu(submenu3);
//                submenulist.add(subbean);
//
//            }
//
////----------------
//            }else {
//            	String main_sqll = "select DISTINCT a.mainlevel,a.main_desc,a.menu_url from t04_menu_web  a left join t04_menu_service b on(a.mainlevel=b.mainlevel) where b.agency_name='" + userid.trim() + "' and a.mainlevel="+bean.getMainlevel()+" order by a.mainlevel";
//            	List<WebMenuBean> mainlistt = JdbcRead.query(main_sqll, new RowMapper<WebMenuBean>() {
//                    @Override
//                    public WebMenuBean mapRow(ResultSet rs, int i) throws SQLException {
//                        WebMenuBean menu = new WebMenuBean();
//                        menu.setMainlevel(rs.getInt("mainlevel"));
//                        menu.setMainlevel_desc(rs.getString("main_desc")); 
//                        menu.setMenu_url(rs.getString("menu_url"));
//                        return menu;
//                    }
//                });
//            	if(mainlistt.size()>0) {
//            		WebMenuBean lbean = mainlistt.get(0);
//            		menuBean.setMenu_url(lbean.getMenu_url());	
//            	}
//
//            }
//            menuBean.setMenuList(submenulist);
//            menulist.add(menuBean);
//            
//          //  System.out.println(menulist);
//        }
//
//        return menulist;
//
//    }

    
    
    @Override
    public List<WebMenuBean> getDynamicMenuList(String userid,String lang) {
        List<WebMenuBean> menulist = new ArrayList<WebMenuBean>();
        WebMenuBean menuBean = null;
        WebMenuBean subbean = null;
        WebMenuBean subsubbean = null;
        try {
        int role_id = 4;
        if(!userid.equalsIgnoreCase("") && !userid.equalsIgnoreCase("null")) {
         role_id = JdbcRead.queryForObject("select distinct b.role_id from users a left join user_roles b on a.id=b.user_id where a.username='"+userid+"'",Integer.class);
        }
        String main_sql = "select DISTINCT a.mainlevel,a.main_desc from t04_menu_web  a left join t04_menu_service b on(a.mainlevel=b.mainlevel) where b.role_id='" + role_id + "' and a.status='A' and a.lang='"+lang+"' order by a.mainlevel";
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
                    + " where b.role_id='" + role_id + "' and a.mainlevel="+bean.getMainlevel()+" and a.level1 !=0 and a.lang='"+lang+"' order by a.mainlevel, a.level1";
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
                
                String sql1 = "select DISTINCT a.mainlevel, a.level1, a.level2, a.level2_desc from t04_menu_web a left join t04_menu_service b on(a.mainlevel=b.mainlevel and a.level1=b.level1 and a.level2=b.level2) where b.role_id='" + role_id + "' and a.mainlevel='" + subMenu.getMainlevel() + "' and a.level1='" + subMenu.getLevel1() + "' and a.level2 !=0 and a.lang='"+lang+"' order by a.mainlevel, a.level1, a.level2";
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
                    String sql2 = "select DISTINCT a.mainlevel, a.level1, a.level2, A.level2_desc, a.level3, a.level3_desc, a.menu_url from t04_menu_web a left join t04_menu_service b on (a.mainlevel=b.mainlevel and a.level1=b.level1 and a.level2=b.level2 and a.level3=b.level3) where b.role_id='" + role_id + "' and a.mainlevel='" + subsubMenu.getMainlevel() + "' and a.level1='" + subsubMenu.getLevel1() + "' and a.level2='" + subsubMenu.getLevel2() + "' and a.level3 !=0 and a.lang='"+lang+"' order by a.level2, a.level3";
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

                        
                        String sql3 = "select DISTINCT a.mainlevel, a.level1, a.level2, a.level2_desc, a.menu_url from t04_menu_web a left join t04_menu_service b on(a.mainlevel=b.mainlevel and a.level1=b.level1 and a.level2=b.level2) where b.role_id='" + role_id + "' and  a.mainlevel='" + subsubMenu.getMainlevel() + "' and a.level1='" + subsubMenu.getLevel1() + "' and a.level2='" + subsubMenu.getLevel2() + "' and a.lang='"+lang+"' order by a.level2";
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
                            + " where b.role_id='" + role_id + "' and a.mainlevel='" + subMenu.getMainlevel() + "' and a.level1='" + subMenu.getLevel1() + "'  and a.lang='"+lang+"' order by a.mainlevel, a.level1";
                    // String sql = "select DISTINCT a.mainlevel,A.LEVEL1,A.LEVEL1_DESC from SROUSER.IGRS_MENU_WEB a left join SROUSER.IGRS_MENU_SERVICE b on(a.mainlevel=b.mainlevel and a.level1=b.level1) where b.AGENCY_NAME='" + userid.trim() + "' and   a.mainlevel='" + bean.getMainlevel() + "' order by a.mainlevel,A.LEVEL1";
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
            	String main_sqll = "select DISTINCT a.mainlevel,a.main_desc,a.menu_url from t04_menu_web  a left join t04_menu_service b on(a.mainlevel=b.mainlevel) where b.role_id='" + role_id + "' and a.mainlevel="+bean.getMainlevel()+" and a.lang='"+lang+"' order by a.mainlevel";
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
        }catch(Exception e) {
        	logger.error("Exception in LoginDaoImpl::getDynamicMenuList::" + e.getMessage());
            logger.error("Exception in LoginDaoImpl::getDynamicMenuList::" + e.getCause());
            logger.error("Exception in LoginDaoImpl::getDynamicMenuList::" + e.getClass() + "::" + e.getLocalizedMessage());
        }

        return menulist;

    }

	@Override
	public LoginBean getLoginAuthentication(LoginBean login, jakarta.servlet.http.HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getLoginPwdUpdate(LoginBean login, jakarta.servlet.http.HttpServletRequest request) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int audit_Trails(LoginBean login, jakarta.servlet.http.HttpServletRequest request, String log_type,
			String status, String process) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int createPWDLog(LoginBean login, jakarta.servlet.http.HttpServletRequest request) {
		// TODO Auto-generated method stub
		return 0;
	}





}
