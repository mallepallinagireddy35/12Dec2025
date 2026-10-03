package com.nic.svpnpa.model;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.JsonArray;

/**
 *
 * @author Krishna
 */
public class WebMenuBean implements Serializable {

	private int mainlevel, level1, level2, level3;
	private String mainlevel_desc, level1_desc, level2_desc, level3_desc, menu_url, lvl_url, user_id,mainlevel_desc_hi,level1_desc_hi,level2_desc_hi,level3_desc_hi;
	private List<WebMenuBean> menuList;
	private List<WebMenuBean> subMenu;

	private String[] heading, sub_heading, content, content1, content2, content3;
	//private MultipartFile content_image;
	private List<MultipartFile> content_image;
	private List<MultipartFile> uploadFiles;
	private String head, sub_head, content_menu;
	private String status, status_key;
	
    private byte[] imagefile;
	
    private String role,service;
    
    
	JsonArray jsonContent = new JsonArray();

	
	
	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public int getMainlevel() {
		return mainlevel;
	}

	public void setMainlevel(int mainlevel) {
		this.mainlevel = mainlevel;
	}

	public int getLevel1() {
		return level1;
	}

	public void setLevel1(int level1) {
		this.level1 = level1;
	}

	public int getLevel2() {
		return level2;
	}

	public void setLevel2(int level2) {
		this.level2 = level2;
	}

	public int getLevel3() {
		return level3;
	}

	public void setLevel3(int level3) {
		this.level3 = level3;
	}

	public String getMainlevel_desc() {
		return mainlevel_desc;
	}

	public void setMainlevel_desc(String mainlevel_desc) {
		this.mainlevel_desc = mainlevel_desc;
	}

	public String getLevel1_desc() {
		return level1_desc;
	}

	public void setLevel1_desc(String level1_desc) {
		this.level1_desc = level1_desc;
	}

	public String getLevel2_desc() {
		return level2_desc;
	}

	public void setLevel2_desc(String level2_desc) {
		this.level2_desc = level2_desc;
	}

	public String getLevel3_desc() {
		return level3_desc;
	}

	public void setLevel3_desc(String level3_desc) {
		this.level3_desc = level3_desc;
	}

	public String getMenu_url() {
		return menu_url;
	}

	public void setMenu_url(String menu_url) {
		this.menu_url = menu_url;
	}

	public List<WebMenuBean> getMenuList() {
		return menuList;
	}

	public void setMenuList(List<WebMenuBean> menuList) {
		this.menuList = menuList;
	}

	public List<WebMenuBean> getSubMenu() {
		return subMenu;
	}

	public void setSubMenu(List<WebMenuBean> subMenu) {
		this.subMenu = subMenu;
	}

	public String getLvl_url() {
		return lvl_url;
	}

	public void setLvl_url(String lvl_url) {
		this.lvl_url = lvl_url;
	}

	public String[] getHeading() {
		return heading;
	}

	public void setHeading(String[] heading) {
		this.heading = heading;
	}

	public String[] getSub_heading() {
		return sub_heading;
	}

	public void setSub_heading(String[] sub_heading) {
		this.sub_heading = sub_heading;
	}

	public String[] getContent() {
		return content;
	}

	public void setContent(String[] content) {
		this.content = content;
	}

	

	public List<MultipartFile> getContent_image() {
		return content_image;
	}

	public void setContent_image(List<MultipartFile> content_image) {
		this.content_image = content_image;
	}

	public String getHead() {
		return head;
	}

	public void setHead(String head) {
		this.head = head;
	}

	public String getSub_head() {
		return sub_head;
	}

	public void setSub_head(String sub_head) {
		this.sub_head = sub_head;
	}

	public String getContent_menu() {
		return content_menu;
	}

	public void setContent_menu(String content_menu) {
		this.content_menu = content_menu;
	}

	public String[] getContent1() {
		return content1;
	}

	public void setContent1(String[] content1) {
		this.content1 = content1;
	}

	public String[] getContent2() {
		return content2;
	}

	public void setContent2(String[] content2) {
		this.content2 = content2;
	}

	public String[] getContent3() {
		return content3;
	}

	public void setContent3(String[] content3) {
		this.content3 = content3;
	}

	public JsonArray getJsonContent() {
		return jsonContent;
	}

	public void setJsonContent(JsonArray jsonContent) {
		this.jsonContent = jsonContent;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getStatus_key() {
		return status_key;
	}

	public void setStatus_key(String status_key) {
		this.status_key = status_key;
	}

	public List<MultipartFile> getUploadFiles() {
		return uploadFiles;
	}

	public void setUploadFiles(List<MultipartFile> uploadFiles) {
		this.uploadFiles = uploadFiles;
	}

	public byte[] getImagefile() {
		return imagefile;
	}

	public void setImagefile(byte[] imagefile) {
		this.imagefile = imagefile;
	}
	

	public String getMainlevel_desc_hi() {
		return mainlevel_desc_hi;
	}

	public void setMainlevel_desc_hi(String mainlevel_desc_hi) {
		this.mainlevel_desc_hi = mainlevel_desc_hi;
	}

	public String getLevel1_desc_hi() {
		return level1_desc_hi;
	}

	public void setLevel1_desc_hi(String level1_desc_hi) {
		this.level1_desc_hi = level1_desc_hi;
	}

	public String getLevel2_desc_hi() {
		return level2_desc_hi;
	}

	public void setLevel2_desc_hi(String level2_desc_hi) {
		this.level2_desc_hi = level2_desc_hi;
	}

	public String getLevel3_desc_hi() {
		return level3_desc_hi;
	}

	public void setLevel3_desc_hi(String level3_desc_hi) {
		this.level3_desc_hi = level3_desc_hi;
	}

	@Override
	public String toString() {
		return "WebMenuBean [mainlevel=" + mainlevel + ", level1=" + level1 + ", level2=" + level2 + ", level3="
				+ level3 + ", mainlevel_desc=" + mainlevel_desc + ", level1_desc=" + level1_desc + ", level2_desc="
				+ level2_desc + ", level3_desc=" + level3_desc + ", mainlevel_desc_hi=" + mainlevel_desc_hi + ", level1_desc_hi=" + level1_desc_hi + ", level2_desc_hi=" + level2_desc_hi + ", level3_desc_hi=" + level3_desc_hi + ","
				+ "menu_url=" + menu_url + ", lvl_url=" + lvl_url
				+ ", user_id=" + user_id + ", menuList=" + menuList + ", subMenu=" + subMenu + ", heading="
				+ Arrays.toString(heading) + ", sub_heading=" + Arrays.toString(sub_heading) + ", content="
				+ Arrays.toString(content) + ", content1=" + Arrays.toString(content1) + ", content2="
				+ Arrays.toString(content2) + ", content3=" + Arrays.toString(content3) + ", content_image="
				+ content_image + ", uploadFiles=" + uploadFiles + ", head=" + head + ", sub_head=" + sub_head
				+ ", content_menu=" + content_menu + ", status=" + status + ", status_key=" + status_key
				+ ", imagefile=" + Arrays.toString(imagefile) + ", jsonContent=" + jsonContent + "]";
	}

	
	
	

}
