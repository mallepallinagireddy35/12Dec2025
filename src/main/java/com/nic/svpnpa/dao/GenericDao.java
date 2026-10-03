package com.nic.svpnpa.dao;

import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.http.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import com.nic.svpnpa.model.CommonBean;
import com.nic.svpnpa.model.JsonBean;

public interface GenericDao {
	
	public int generateForm(JsonBean bean);

	public List Retrieving_Json_Values(String form_name) throws Exception;
	
	public int insertFormData(JSONArray array,JSONObject obj,String form_name,JsonBean bean) throws Exception;

	public List<JsonBean> getList();
	
	public List<JsonBean> getFormStructure(String form_id,HttpSession session);
	
	public int updateFormStructure(JsonBean form,HttpSession session);
	
	public List<JsonBean> getFormsList();
	
	public List<JsonBean> getForm(String form_id,HttpSession session);
	
	public List<JsonBean> getFormsRecordList();
	
	public List<JsonBean> getListOfRecords(String form_id,HttpSession session);
	
	public List<JsonBean> getFormData(String form_id,String record_id,HttpSession session);

	public int updateFormData(JSONArray array,JSONObject obj,JsonBean bean) throws Exception;
	
	public List<CommonBean> getStateList();
	
	public List getDistrictList(String stateCode);
	
	public List getMandalList(String stateCode, String distCode);
	
	public List getVillageList(String stateCode, String distCode,String mandalCode);
	
	public String getFilePath(String form_id,String record_id,String column_id);
	
	public List<JsonBean> getFormsReport(String form_id,HttpSession session);
	
	public List<JsonBean> getColumnsList(String form_id,HttpSession session);
	
	public List<JsonBean> getFormExpiryDetails(String form_id,HttpSession session);
	
	public int updateFormExpiryDate(JsonBean form,HttpSession session);
	
	public int updateFormApprove(JsonBean form,HttpSession session);
	
	public int getForm_Check(String form_name);
	
	public int deleteFormData(JsonBean form,HttpSession session);

	public int deleteFormStructure(JsonBean form, HttpSession session);
	
	public int updateFormVerify(JsonBean form,HttpSession session);
	
	public List<JsonBean> getList1();
	
	public List<JsonBean> getList2();
	
	public List<JsonBean> getList3();
	
	public int checkFormExpiryDate(String form_id,HttpSession session);
	
}
