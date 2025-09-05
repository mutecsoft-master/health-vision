package com.mutecsoft.healthvision.common.util;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class RequestUtil {
	
    public static HttpServletRequest getCurrentRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
        	throw new RuntimeException("잘못된 요청입니다.");
        }
        return attributes.getRequest();
    }
    
    public static String getRemoteIp() {
    	HttpServletRequest request = getCurrentRequest();
    	return request.getRemoteAddr();
    }
}
