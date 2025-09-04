package com.mutecsoft.healthvision.admin.util;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.mutecsoft.healthvision.common.constant.ResultCdEnum;
import com.mutecsoft.healthvision.common.dto.UserDto.UserInfo;
import com.mutecsoft.healthvision.common.exception.CustomException;
import com.mutecsoft.healthvision.common.mapper.AUserMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class UserUtil {

	private final ModelMapper modelMapper;
	private final AUserMapper aUserMapper;

    public UserInfo getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserInfo) {
        	UserInfo userInfo = (UserInfo) principal;
        	return userInfo;
        }else {
        	throw new CustomException(ResultCdEnum.E001.getValue());
        }
        
        
    }
}
