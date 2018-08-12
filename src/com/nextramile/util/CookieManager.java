package com.nextramile.util;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieManager {
    public static void addCookie(ExternalContext context, String name, String value, int maxAge) {
    	Cookie referer = new Cookie(name, value);
		referer.setMaxAge(maxAge);
		HttpServletResponse response = (HttpServletResponse) context.getResponse();
		response.addCookie(referer);
    }
    
    public static String getCookie(ExternalContext context, String name) {
    	HttpServletRequest request = (HttpServletRequest) context.getRequest();
    	Cookie[] cookies = request.getCookies();
    	String value = null;
    	for(Cookie cookie : cookies) {
    		if(cookie.getName().equals(name)) {
    			value = cookie.getValue();
    		}
    		break;
    	}
    	return value;
    }
}
