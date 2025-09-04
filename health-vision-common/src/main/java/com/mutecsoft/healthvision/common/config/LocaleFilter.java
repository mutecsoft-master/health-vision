package com.mutecsoft.healthvision.common.config;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.LocaleResolver;


//로그인 실패시 메시지 다국어 처리를 위해 사용
//스프링 시큐리티 필터 체인이 localeInterceptor 보다 먼저 처리됨 
public class LocaleFilter extends OncePerRequestFilter {

    private LocaleResolver localeResolver;

    public LocaleFilter(LocaleResolver localeResolver) {
        this.localeResolver = localeResolver;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        String newLocale = request.getParameter("lang");
        if (newLocale != null) {
            Locale locale = StringUtils.parseLocaleString(newLocale);
            localeResolver.setLocale(request, response, locale);
        }
        
        filterChain.doFilter(request, response);
    }
}