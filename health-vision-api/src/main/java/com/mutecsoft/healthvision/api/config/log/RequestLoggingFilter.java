package com.mutecsoft.healthvision.api.config.log;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class RequestLoggingFilter extends OncePerRequestFilter {

	//logback.xml 에 logger 설정을 위해 RequestLogger 네이밍 설정
	private static final Logger log = LoggerFactory.getLogger("RequestLogger");
	
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String method = request.getMethod();
        String uri = request.getRequestURI();
        String ip = request.getRemoteAddr();

        log.info("## request - method: {}, uri: {}, ip: {}", method, uri, ip);

        filterChain.doFilter(request, response);
    }
}