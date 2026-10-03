/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nic.svpnpa.dao;


import java.util.List;

import com.nic.svpnpa.model.Role;
import com.nic.svpnpa.model.WebMenuBean;

import jakarta.servlet.http.*;


public interface AdminDao {
	public List<WebMenuBean> getDynamicMenuList(String userid, HttpServletRequest request);

	public List<WebMenuBean> getMainMenuList(String userid, HttpServletRequest request);

	public List<WebMenuBean> getLevelOneList(String userid, String mainLevel, HttpServletRequest request);

	public List<WebMenuBean> getLevelTwoList(String userid, String mainLevel, String Level1, HttpServletRequest request);

	public WebMenuBean getMLevel(String userid, int mainLevel, HttpServletRequest request);

	public WebMenuBean getLOne(String userid, int mainLevel, int Level1, HttpServletRequest request);

	public WebMenuBean getLTwo(String userid, int mainLevel, int Level1, int Level2, HttpServletRequest request);
	
	public  int  getAddMenuContent(String userid, WebMenuBean cf, HttpServletRequest request);
	
	public List<WebMenuBean> getMenuContent(String userid, int mainLevel, int Level1, int Level2, HttpServletRequest request);
	
	public List<WebMenuBean> getMainLevelSize(String userid, WebMenuBean cf, HttpServletRequest request);
	
	public WebMenuBean searchLevelOne(String userid, WebMenuBean cf, HttpServletRequest request);
	
	public WebMenuBean searchLevelTwo(String userid, WebMenuBean cf, HttpServletRequest request);
	
	public WebMenuBean searchLevelThree(String userid, WebMenuBean cf, HttpServletRequest request);
	
	public List<WebMenuBean> getLevelOneSize(String userid, WebMenuBean cf, HttpServletRequest request);
	
	public List<WebMenuBean> getLevelTwoSize(String userid, WebMenuBean cf, HttpServletRequest request);
	
	public List<WebMenuBean> getLevelThreeSize(String userid, WebMenuBean cf, HttpServletRequest request);
	
	public WebMenuBean searchMainLevel(String userid, String maindesc, HttpServletRequest request);
	
	public int createMenu(String userid, WebMenuBean cf, HttpServletRequest request);
	
	public int createMenu_Service(String userid, WebMenuBean cf, HttpServletRequest request);
	
	public List<WebMenuBean> getEditMenu(String userid, HttpServletRequest request);
	
	public WebMenuBean getFillDetails(String userid, WebMenuBean cf, HttpServletRequest request);
	
	public int editMenu(String userid, WebMenuBean cf, HttpServletRequest request);
	
	public List<WebMenuBean> getStatusList();
	
	public int deleteMenu(String user_id, WebMenuBean cf, HttpServletRequest request);
	
	public List<WebMenuBean> getDynamicMenuServicesList(HttpServletRequest request);
	
	public List<WebMenuBean> getDynamicMenuList2();
	
	public int insertMenuServices(WebMenuBean bean,String[] checkedSerivesArray);
	
	public String getServices(WebMenuBean bean);
	
	public int updateMenuServices(WebMenuBean bean,String[] checkedSerivesArray);
	
	public List<Role> getRolesList(String username);

}
