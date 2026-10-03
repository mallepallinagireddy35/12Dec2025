/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nic.svpnpa.dao;



import com.nic.svpnpa.model.LoginBean;
import com.nic.svpnpa.model.WebMenuBean;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;


public interface LoginDao {

    public LoginBean getLoginAuthentication(LoginBean login, HttpServletRequest request);

    public int getLoginPwdUpdate(LoginBean login, HttpServletRequest request);

    public int audit_Trails(LoginBean login, HttpServletRequest request, String log_type, String status, String process);

    public int createPWDLog(LoginBean login, HttpServletRequest request);

    public List<WebMenuBean> getDynamicMenuList(String userid,String lang);

}
