package com.nic.svpnpa.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public class JsonBean {

	private String label ,dtype ,minsize ,maxsize ,mandatory ,format ;
	private String data ,multicolumn ,radiobutton ,checkbox ,image ,password ,text,date,email,fileupload,textarea;
	
	private MultipartFile file0,file1,file2,file3,file4,file5,file6,file7,file8,file9,file10;
	
//	ArrayList<MultipartFile> file;
	
//	MultipartFile file;
	
	ArrayList<String> dropvalues;
	

	
	private String form_id ,record_id ,column_id,column_name,column_value,jsonFields;		
	private String jsonstring ,jsonData ,jsonCheck ,form_name,fields ,no1 ,no2 ,no3 ,base64,specialcombo ;
	private String date0,date1,date2,stateCode,stateName,distCode,distName,mandalCode,mandalName,villCode,villName;
	private String mobile,aadhaarno,panno,number,con_check,con_value,from_date,to_date,captcha,captcha_key,file_type,login_name;
	
	private List<JsonBean> stateList,distList,mandalList,villList;
	
	private String remark,approve,flag,captchaImage,sms,sms_key,phone;
	
	private int otp;
	
	
	

	public int getOtp() {
		return otp;
	}

	public void setOtp(int otp) {
		this.otp = otp;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getSms() {
		return sms;
	}

	public void setSms(String sms) {
		this.sms = sms;
	}

	public String getSms_key() {
		return sms_key;
	}

	public void setSms_key(String sms_key) {
		this.sms_key = sms_key;
	}

	public String getCaptchaImage() {
		return captchaImage;
	}

	public void setCaptchaImage(String captchaImage) {
		this.captchaImage = captchaImage;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	
	
	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getDtype() {
		return dtype;
	}

	public void setDtype(String dtype) {
		this.dtype = dtype;
	}

	public String getMinsize() {
		return minsize;
	}

	public void setMinsize(String minsize) {
		this.minsize = minsize;
	}

	public String getMaxsize() {
		return maxsize;
	}

	public void setMaxsize(String maxsize) {
		this.maxsize = maxsize;
	}

	public String getMandatory() {
		return mandatory;
	}

	public void setMandatory(String mandatory) {
		this.mandatory = mandatory;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getMulticolumn() {
		return multicolumn;
	}

	public void setMulticolumn(String multicolumn) {
		this.multicolumn = multicolumn;
	}

	public String getRadiobutton() {
		return radiobutton;
	}

	public void setRadiobutton(String radiobutton) {
		this.radiobutton = radiobutton;
	}

	public String getCheckbox() {
		return checkbox;
	}

	public void setCheckbox(String checkbox) {
		this.checkbox = checkbox;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFileupload() {
		return fileupload;
	}

	public void setFileupload(String fileupload) {
		this.fileupload = fileupload;
	}

	public String getTextarea() {
		return textarea;
	}

	public void setTextarea(String textarea) {
		this.textarea = textarea;
	}

	public ArrayList<String> getDropvalues() {
		return dropvalues;
	}

	public void setDropvalues(ArrayList<String> dropvalues) {
		this.dropvalues = dropvalues;
	}

	public String getJsonData() {
		return jsonData;
	}

	public void setJsonData(String jsonData) {
		this.jsonData = jsonData;
	}

	public String getJsonCheck() {
		return jsonCheck;
	}

	public void setJsonCheck(String jsonCheck) {
		this.jsonCheck = jsonCheck;
	}

	public String getForm_name() {
		return form_name;
	}

	public void setForm_name(String form_name) {
		this.form_name = form_name;
	}

	public String getFields() {
		return fields;
	}

	public void setFields(String fields) {
		this.fields = fields;
	}

	public MultipartFile getFile0() {
		return file0;
	}

	public void setFile0(MultipartFile file0) {
		this.file0 = file0;
	}

	public MultipartFile getFile1() {
		return file1;
	}

	public void setFile1(MultipartFile file1) {
		this.file1 = file1;
	}

	public MultipartFile getFile2() {
		return file2;
	}

	public void setFile2(MultipartFile file2) {
		this.file2 = file2;
	}

	public MultipartFile getFile3() {
		return file3;
	}

	public void setFile3(MultipartFile file3) {
		this.file3 = file3;
	}

	public MultipartFile getFile4() {
		return file4;
	}

	public void setFile4(MultipartFile file4) {
		this.file4 = file4;
	}

	public MultipartFile getFile5() {
		return file5;
	}

	public void setFile5(MultipartFile file5) {
		this.file5 = file5;
	}

	public MultipartFile getFile6() {
		return file6;
	}

	public void setFile6(MultipartFile file6) {
		this.file6 = file6;
	}

	public MultipartFile getFile7() {
		return file7;
	}

	public void setFile7(MultipartFile file7) {
		this.file7 = file7;
	}

	public MultipartFile getFile8() {
		return file8;
	}

	public void setFile8(MultipartFile file8) {
		this.file8 = file8;
	}

	public MultipartFile getFile9() {
		return file9;
	}

	public void setFile9(MultipartFile file9) {
		this.file9 = file9;
	}

	public MultipartFile getFile10() {
		return file10;
	}

	public void setFile10(MultipartFile file10) {
		this.file10 = file10;
	}

	public String getRecord_id() {
		return record_id;
	}

	public void setRecord_id(String record_id) {
		this.record_id = record_id;
	}

	public String getColumn_id() {
		return column_id;
	}

	public void setColumn_id(String column_id) {
		this.column_id = column_id;
	}

	public String getForm_id() {
		return form_id;
	}

	public void setForm_id(String form_id) {
		this.form_id = form_id;
	}

	public String getJsonstring() {
		return jsonstring;
	}

	public void setJsonstring(String jsonstring) {
		this.jsonstring = jsonstring;
	}

//	public MultipartFile getFile() {
//		return file;
//	}
//
//	public void setFile(MultipartFile file) {
//		this.file = file;
//	}

	
	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getJsonFields() {
		return jsonFields;
	}

	public void setJsonFields(String jsonFields) {
		this.jsonFields = jsonFields;
	}

	public String getBase64() {
		return base64;
	}

	public void setBase64(String base64) {
		this.base64 = base64;
	}

	public String getDate0() {
		return date0;
	}

	public void setDate0(String date0) {
		this.date0 = date0;
	}

	public String getDate1() {
		return date1;
	}

	public void setDate1(String date1) {
		this.date1 = date1;
	}

	public String getDate2() {
		return date2;
	}

	public void setDate2(String date2) {
		this.date2 = date2;
	}

	public String getColumn_name() {
		return column_name;
	}

	public void setColumn_name(String column_name) {
		this.column_name = column_name;
	}

	public String getColumn_value() {
		return column_value;
	}

	public void setColumn_value(String column_value) {
		this.column_value = column_value;
	}

	public String getSpecialcombo() {
		return specialcombo;
	}

	public void setSpecialcombo(String specialcombo) {
		this.specialcombo = specialcombo;
	}

		

	public String getStateCode() {
		return stateCode;
	}

	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public String getDistCode() {
		return distCode;
	}

	public void setDistCode(String distCode) {
		this.distCode = distCode;
	}

	public String getMandalCode() {
		return mandalCode;
	}

	public void setMandalCode(String mandalCode) {
		this.mandalCode = mandalCode;
	}

	public String getVillCode() {
		return villCode;
	}

	public void setVillCode(String villCode) {
		this.villCode = villCode;
	}

	public String getDistName() {
		return distName;
	}

	public void setDistName(String distName) {
		this.distName = distName;
	}

	public String getMandalName() {
		return mandalName;
	}

	public void setMandalName(String mandalName) {
		this.mandalName = mandalName;
	}

	public String getVillName() {
		return villName;
	}

	public void setVillName(String villName) {
		this.villName = villName;
	}

	public String getNo1() {
		return no1;
	}

	public void setNo1(String no1) {
		this.no1 = no1;
	}

	public String getNo2() {
		return no2;
	}

	public void setNo2(String no2) {
		this.no2 = no2;
	}

	public String getNo3() {
		return no3;
	}

	public void setNo3(String no3) {
		this.no3 = no3;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getAadhaarno() {
		return aadhaarno;
	}

	public void setAadhaarno(String aadhaarno) {
		this.aadhaarno = aadhaarno;
	}

	public String getPanno() {
		return panno;
	}

	public void setPanno(String panno) {
		this.panno = panno;
	}

	public String getCon_check() {
		return con_check;
	}

	public void setCon_check(String con_check) {
		this.con_check = con_check;
	}

	public String getFrom_date() {
		return from_date;
	}

	public void setFrom_date(String from_date) {
		this.from_date = from_date;
	}

	public String getTo_date() {
		return to_date;
	}

	public void setTo_date(String to_date) {
		this.to_date = to_date;
	}

	public String getCaptcha() {
		return captcha;
	}

	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}

	public List<JsonBean> getDistList() {
		return distList;
	}

	public void setDistList(List<JsonBean> distList) {
		this.distList = distList;
	}

	public List<JsonBean> getMandalList() {
		return mandalList;
	}

	public void setMandalList(List<JsonBean> mandalList) {
		this.mandalList = mandalList;
	}

	public List<JsonBean> getVillList() {
		return villList;
	}

	public void setVillList(List<JsonBean> villList) {
		this.villList = villList;
	}

	public List<JsonBean> getStateList() {
		return stateList;
	}

	public void setStateList(List<JsonBean> stateList) {
		this.stateList = stateList;
	}

	public String getCaptcha_key() {
		return captcha_key;
	}

	public void setCaptcha_key(String captcha_key) {
		this.captcha_key = captcha_key;
	}

	public String getCon_value() {
		return con_value;
	}

	public void setCon_value(String con_value) {
		this.con_value = con_value;
	}

	public String getFile_type() {
		return file_type;
	}

	public void setFile_type(String file_type) {
		this.file_type = file_type;
	}

	public String getLogin_name() {
		return login_name;
	}

	public void setLogin_name(String login_name) {
		this.login_name = login_name;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getApprove() {
		return approve;
	}

	public void setApprove(String approve) {
		this.approve = approve;
	}



	



	
}
