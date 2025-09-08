package com.mutecsoft.healthvision.api.service.impl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mutecsoft.healthvision.api.service.AuthService;
import com.mutecsoft.healthvision.api.service.UserService;
import com.mutecsoft.healthvision.api.util.JwtTokenUtil;
import com.mutecsoft.healthvision.common.constant.ResultCdEnum;
import com.mutecsoft.healthvision.common.constant.SingleTokenTypeEnum;
import com.mutecsoft.healthvision.common.dto.ResponseDto;
import com.mutecsoft.healthvision.common.dto.UserDto.LoginRequest;
import com.mutecsoft.healthvision.common.model.User;
import com.mutecsoft.healthvision.common.util.MessageUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    
    @Transactional
    public ResponseDto login(LoginRequest loginReq) {

        String email = loginReq.getEmail();
        String userPw = loginReq.getUserPw();
        
        User user = userService.selectUserByEmail(email);
        if (user == null) {
        	return new ResponseDto(false, "사용자 정보를 찾을 수 없습니다.");
        }

        boolean isNormalPwValid = passwordEncoder.matches(userPw, user.getUserPw());
        
        if (!isNormalPwValid) {
        	return new ResponseDto(false, "비밀번호가 일치하지 않습니다.");
        }

        userService.updateLastLoginDt(user.getUserId());
        
        // Token 발급
        Map<String,String> tokenMap = generateTokens(user);
        return new ResponseDto(true, tokenMap);


    }
    
	@Override
	public Map<String, String> refreshToken(HttpServletRequest request) {
    	String token = jwtTokenUtil.getJwtFromRequest(request);
    	
    	//refresh token 을 받았을 경우만 재발급
    	String tokenType = jwtTokenUtil.getTokenType(token);
    	if(!tokenType.equals(SingleTokenTypeEnum.REFRESH.getValue())) {
    		return null;
    	}
    	
    	if(!jwtTokenUtil.isValidToken(token)) {
    		return null;
    	}else {
    		String userId = jwtTokenUtil.getUserIdFromToken(token);
    		User user = userService.selectUserByUserId(Long.parseLong(userId));
    		
    		return generateTokens(user);
    	}
	}
    

    private Map<String,String> generateTokens(User user) {
        Map<String, String> result = new HashMap<>();

        String accessToken = jwtTokenUtil.generateToken(SingleTokenTypeEnum.ACCESS.getValue(), String.valueOf(user.getUserId()));
        String refreshToken = jwtTokenUtil.generateToken(SingleTokenTypeEnum.REFRESH.getValue(), String.valueOf(user.getUserId()));

        result.put("accessToken", accessToken);
        result.put("refreshToken", refreshToken);

        return result;
    }


    

	
}
