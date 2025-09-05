package com.mutecsoft.healthvision.api.util;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.mutecsoft.healthvision.common.dto.UserDto.UserInfo;
import com.mutecsoft.healthvision.common.exception.CustomException;
import com.mutecsoft.healthvision.common.model.User;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class UserUtil {
	
	private final ModelMapper modelMapper;
	
	public UserInfo getUserInfo() {

		//Principal에서 추출
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
        	throw new CustomException("인증 정보를 찾을 수 없습니다.", HttpStatus.UNAUTHORIZED);
        }
        
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserInfo) {
        	UserInfo userInfo = (UserInfo) principal;
        	return userInfo;
        }else {
        	throw new CustomException("사용자 정보 변환에 실패했습니다.", HttpStatus.FORBIDDEN);
        }
        
	}
	
	//시스템 내부적으로 User 클래스를 사용하기 위해 변환
	public User getUserFromUserInfo() {
		UserInfo userInfo = getUserInfo();
		User user = modelMapper.map(userInfo, User.class);
		return user;
	}
	
}
