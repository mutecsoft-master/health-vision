package com.mutecsoft.healthvision.admin.util;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.mutecsoft.healthvision.common.dto.UserDto.UserInfo;
import com.mutecsoft.healthvision.common.exception.CustomException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class UserUtil {

    public UserInfo getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserInfo) {
        	UserInfo userInfo = (UserInfo) principal;
        	return userInfo;
        }else {
        	throw new CustomException("사용자 정보 변환에 실패했습니다.", HttpStatus.FORBIDDEN);
        }
        
        
    }
}
