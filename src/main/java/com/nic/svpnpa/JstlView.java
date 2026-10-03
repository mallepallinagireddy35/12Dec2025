package com.nic.svpnpa;

import java.util.Map;


import org.springframework.web.servlet.view.InternalResourceView;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.*;

public class JstlView extends InternalResourceView {

    @Override
    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        // Determine the path for the request dispatcher.
        String dispatcherPath = prepareForRendering(request, response);

        // set original view being asked for as a request parameter
        request.setAttribute("partial", dispatcherPath);
        request.setAttribute("title", model.get("title"));
        request.setAttribute("view", model.get("view"));
        request.setAttribute("msg", model.get("msg"));
        request.setAttribute("userType", model.get("userType"));
        request.setAttribute("form", model.get("form"));
        request.setAttribute("userlogin", model.get("userlogin"));
        request.setAttribute("userId", model.get("userId"));
        request.setAttribute("masterlist", model.get("masterlist"));       
        request.setAttribute("menuList", model.get("menuList"));
        request.setAttribute("levelOneList", model.get("levelOneList"));
        request.setAttribute("levelTwoList", model.get("levelTwoList"));
        request.setAttribute("mainList", model.get("mainList"));
        request.setAttribute("mContentList", model.get("mContentList"));
        request.setAttribute("userId", model.get("userId"));
        request.setAttribute("label", model.get("label"));
        response.addHeader("X-Content-Type-Options", "'nosniff'");
        response.addHeader("Set-Cookie", "key=value; HttpOnly; SameSite='strict'");

        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/WEB-INF/views/template.jsp");
        requestDispatcher.include(request, response);

    }

}
