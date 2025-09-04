package com.mutecsoft.healthvision.common.util;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.mutecsoft.healthvision.common.constant.ResultCdEnum;
import com.mutecsoft.healthvision.common.exception.CustomException;

public class RequestUtil {
	
    public static HttpServletRequest getCurrentRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            throw new CustomException(ResultCdEnum.E001.getValue());
        }
        return attributes.getRequest();
    }
    
    public static String getRemoteIp() {
    	HttpServletRequest request = getCurrentRequest();
    	return request.getRemoteAddr();
    }
}
