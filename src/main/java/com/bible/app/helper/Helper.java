package com.bible.app.helper;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

public class Helper {

    public static String getRemoteAddrAndRequestURL() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        return "Remote address " + request.getRemoteAddr() + " requested " + request.getRequestURL();
    }
}
