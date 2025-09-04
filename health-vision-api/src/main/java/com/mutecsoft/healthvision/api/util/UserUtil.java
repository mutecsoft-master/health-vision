package com.mutecsoft.healthvision.api.util;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.mutecsoft.healthvision.api.security.CustomOAuth2User;
import com.mutecsoft.healthvision.common.constant.ResultCdEnum;
import com.mutecsoft.healthvision.common.dto.UserDto.UserInfo;
import com.mutecsoft.healthvision.common.exception.CustomException;
import com.mutecsoft.healthvision.common.model.User;
import com.mutecsoft.healthvision.common.util.FileUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class UserUtil {
	
	private final ModelMapper modelMapper;
	private final FileUtil fileUtil;
	
	public UserInfo getUserInfo() {

		//Principal에서 추출
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
        	throw new CustomException(ResultCdEnum.L001.getValue());
        }
        
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserInfo) {
        	UserInfo userInfo = (UserInfo) principal;
        	userInfo.setProfileImgUrl(fileUtil.makeImgApiUrl(userInfo.getProfileFileId()));
        	return userInfo;
        }else if(principal instanceof CustomOAuth2User) { //구글 로그인 시
        	CustomOAuth2User customUser = (CustomOAuth2User) principal;
            UserInfo userInfo = customUser.getUserInfo();
            userInfo.setProfileImgUrl(fileUtil.makeImgApiUrl(userInfo.getProfileFileId()));
            return userInfo;
        }else {
        	throw new CustomException(ResultCdEnum.E001.getValue());
        }
        
	}
	
	//시스템 내부적으로 User 클래스를 사용하기 위해 변환
	public User getUserFromUserInfo() {
		UserInfo userInfo = getUserInfo();
		User user = modelMapper.map(userInfo, User.class);
		return user;
	}
	
}
