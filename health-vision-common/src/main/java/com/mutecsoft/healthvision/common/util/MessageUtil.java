package com.mutecsoft.healthvision.common.util;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class MessageUtil {

    private final MessageSource messageSource;

    public String getMessage(String code) {
    	return getMessage(code, null, null);
    }
    
    public String getMessage(String code, Object... args) {
    	return getMessage(code, args, null);
    }
    
    public String getMessage(String code, Object[] args, Locale locale) {
    	
    	HttpServletRequest request = RequestUtil.getCurrentRequest();
        HttpSession session = request.getSession(false);
        if (session != null) {
            Object localeObj = session.getAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME);
            if (localeObj instanceof Locale) {
            	locale = (Locale) localeObj;
            }
        }
    	
    	String msg = "";
    	try {
    		if(locale == null) {
    			locale = LocaleContextHolder.getLocale();
    		}
    		
    		msg = messageSource.getMessage(code, args, locale);
		} catch (NoSuchMessageException e) {
			log.error("## Error : Message '{}'를 찾을 수 없습니다.", code);
		}
    	return msg;
    }
}
