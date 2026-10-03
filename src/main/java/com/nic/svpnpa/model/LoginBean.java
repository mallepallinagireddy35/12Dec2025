/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nic.svpnpa.model;


public class LoginBean {

    private String user_id;
    private String password;
    private String user_desc;
    private String user_role;
    private String user_type;
    private String ofc_name;
    private String ofc_code;
    private String captcha;
    private String new_pwd;
    private String cur_pwd;
    private String con_pwd;
    private String img_captcha;
    private String status;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUser_desc() {
        return user_desc;
    }

    public void setUser_desc(String user_desc) {
        this.user_desc = user_desc;
    }

    public String getUser_role() {
        return user_role;
    }

    public void setUser_role(String user_role) {
        this.user_role = user_role;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getOfc_name() {
        return ofc_name;
    }

    public void setOfc_name(String ofc_name) {
        this.ofc_name = ofc_name;
    }

    public String getOfc_code() {
        return ofc_code;
    }

    public void setOfc_code(String ofc_code) {
        this.ofc_code = ofc_code;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public String getNew_pwd() {
        return new_pwd;
    }

    public void setNew_pwd(String new_pwd) {
        this.new_pwd = new_pwd;
    }

    public String getCur_pwd() {
        return cur_pwd;
    }

    public void setCur_pwd(String cur_pwd) {
        this.cur_pwd = cur_pwd;
    }

    public String getCon_pwd() {
        return con_pwd;
    }

    public void setCon_pwd(String con_pwd) {
        this.con_pwd = con_pwd;
    }

    public String getImg_captcha() {
        return img_captcha;
    }

    public void setImg_captcha(String img_captcha) {
        this.img_captcha = img_captcha;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
