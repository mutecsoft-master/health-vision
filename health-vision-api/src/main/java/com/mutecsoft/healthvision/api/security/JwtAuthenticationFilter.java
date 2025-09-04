package com.mutecsoft.healthvision.api.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mutecsoft.healthvision.api.service.UserService;
import com.mutecsoft.healthvision.api.util.JwtTokenUtil;
import com.mutecsoft.healthvision.common.constant.Const;
import com.mutecsoft.healthvision.common.constant.ResultCdEnum;
import com.mutecsoft.healthvision.common.constant.SingleTokenTypeEnum;
import com.mutecsoft.healthvision.common.dto.ResponseDto;
import com.mutecsoft.healthvision.common.dto.UserDto.UserInfo;
import com.mutecsoft.healthvision.common.model.User;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
    JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	UserService userService;
	
	@Autowired
    ModelMapper modelMapper;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String token = jwtTokenUtil.getJwtFromRequest(request);

            // 1. Access Token이 유효한 지 체크
            if (jwtTokenUtil.isValidToken(token)) {

                if (jwtTokenUtil.getTokenType(token).equals(SingleTokenTypeEnum.ACCESS.getValue())) {
                    String userId = jwtTokenUtil.getUserIdFromToken(token);

                    User user = userService.selectUserByUserId(Long.parseLong(userId));
                    
                    List<GrantedAuthority> authorities = new ArrayList<>();
                    if(user.getRoleNmList() != null) {
                    	for(String roleNm : user.getRoleNmList()) {
                    		authorities.add(new SimpleGrantedAuthority(Const.ROLE_PREFIX + roleNm));
                    	}
                    }
                    
                    UserInfo userInfo = modelMapper.map(user, UserInfo.class);
                    
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                            		userInfo //user.getEmail()
                                    , null //인증 완료 시 password 대신 null 권장
                                    , authorities
                            );
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }

            }
        } catch (Exception ex) {
        	response.setContentType("application/json;charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            ResponseDto responseDto = new ResponseDto(false, null, ResultCdEnum.L001.getValue());
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonResponse = objectMapper.writeValueAsString(responseDto);

            response.getWriter().write(jsonResponse);
            response.getWriter().flush();
            
            log.error("## Authentication failed");
            return;
        }

        filterChain.doFilter(request, response);
    }

    

}
