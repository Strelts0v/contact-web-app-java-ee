package com.itechart.app.controller.utils;

import javax.servlet.http.HttpServletRequest;

/**
 * Encapsulates HttpServletRequest object and provides interface for extracting
 * request params and inserting attributes
 */
public class RequestContent {

    private HttpServletRequest request;

    public RequestContent(HttpServletRequest request){
        this.request = request;
    }

    public String getParameter(final String param){
        return request.getParameter(param);
    }

    public String[] getParameterValues(final String param){
        return request.getParameterValues(param);
    }

    public void insertAttribute(final String name, final Object attribute){
        request.setAttribute(name, attribute);
    }

    public Object getAttribute(final String name){
        return request.getAttribute(name);
    }

    public HttpServletRequest getHttpServletRequest(){
        return request;
    }
}
