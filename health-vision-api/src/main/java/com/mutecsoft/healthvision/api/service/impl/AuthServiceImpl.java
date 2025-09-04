package com.mutecsoft.healthvision.api.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mutecsoft.healthvision.api.service.AuthService;
import com.mutecsoft.healthvision.api.service.LoginAttemptService;
import com.mutecsoft.healthvision.api.service.TokenService;
import com.mutecsoft.healthvision.api.service.UserService;
import com.mutecsoft.healthvision.api.util.JwtTokenUtil;
import com.mutecsoft.healthvision.common.constant.Const;
import com.mutecsoft.healthvision.common.constant.ResultCdEnum;
import com.mutecsoft.healthvision.common.constant.SingleTokenTypeEnum;
import com.mutecsoft.healthvision.common.dto.ResponseDto;
import com.mutecsoft.healthvision.common.dto.UserDto.LoginRequest;
import com.mutecsoft.healthvision.common.model.LoginAttempt;
import com.mutecsoft.healthvision.common.model.Token;
import com.mutecsoft.healthvision.common.model.User;
import com.mutecsoft.healthvision.common.util.MessageUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final ModelMapper modelMapper;
    private final MessageUtil messageUtil;
    
    private final LoginAttemptService loginAttemptService;

    @Transactional
    public ResponseDto login(LoginRequest loginReq) {

        String email = loginReq.getEmail();
        String userPw = loginReq.getUserPw();
        
        //로그인 횟수제한 확인
        LoginAttempt loginAttempt = loginAttemptService.getLoginAttempt(email);
        if(loginAttempt != null && loginAttempt.getFailCnt() >= Const.MAX_LOGIN_ATTEMPT_CNT) {
        	//2시간 이내에 재시도 불가
        	if(!loginAttempt.getLastAttemptDt().isBefore(LocalDateTime.now().minusHours(Const.LOGIN_ATTEMPT_WAIT_HOURS))) {
        		return new ResponseDto(false, null, ResultCdEnum.L002.getValue());
        	}else {
        		//2시간 지났으면 초기화
        		loginAttemptService.initLoginAttempt(email);
        	}
        }
        
        User user = userService.selectUserByEmail(email);
        if (user == null) {
        	loginAttemptService.saveFailLoginAttempt(email);
        	return new ResponseDto(false, null, ResultCdEnum.L001.getValue());
        }

        boolean isNormalPwValid = passwordEncoder.matches(userPw, user.getUserPw());
        boolean isTempPwValid = passwordEncoder.matches(userPw, user.getTempPw()) 
                                && user.getTempPwExpireDt().isAfter(LocalDateTime.now());
        
        if (!(isNormalPwValid || isTempPwValid)) {
        	loginAttemptService.saveFailLoginAttempt(email);
        	return new ResponseDto(false, null, ResultCdEnum.L001.getValue());
        }

        //로그인 성공시 초기화
        loginAttemptService.initLoginAttempt(email);
        userService.updateLastLoginDt(user.getUserId());
        
        List<GrantedAuthority> authorities = new ArrayList<>();
        if(user.getRoleNmList() != null) {
        	for(String roleNm : user.getRoleNmList()) {
        		authorities.add(new SimpleGrantedAuthority(Const.ROLE_PREFIX + roleNm));
        	}
        }

        // Token 발급
        Map<String,String> tokenMap = generateAndStoreTokens(user);
        return new ResponseDto(true, tokenMap);


    }
    
	@Override
	public Map<String, String> refreshToken(HttpServletRequest request) {
    	String token = jwtTokenUtil.getJwtFromRequest(request);
    	
    	if(!jwtTokenUtil.isValidToken(token)) {
    		return null;
    	}else {
    		String userId = jwtTokenUtil.getUserIdFromToken(token);
    		User user = userService.selectUserByUserId(Long.parseLong(userId));
    		
    		return generateAndStoreTokens(user);
    	}
	}
    

    private Map<String,String> generateAndStoreTokens(User user) {
        Map<String, String> result = new HashMap<>();

        String accessToken = jwtTokenUtil.generateToken(SingleTokenTypeEnum.ACCESS.getValue(), String.valueOf(user.getUserId()));
        String refreshToken = jwtTokenUtil.generateToken(SingleTokenTypeEnum.REFRESH.getValue(), String.valueOf(user.getUserId()));

        Token tokenModel = tokenService.selectToken(user.getUserId(), SingleTokenTypeEnum.REFRESH);
        if (tokenModel == null) {
            tokenModel = new Token();
            tokenModel.setUserId(user.getUserId());
            tokenModel.setTokenType(SingleTokenTypeEnum.REFRESH.getValue());
            tokenModel.setToken(refreshToken);
            tokenService.insertToken(tokenModel);
        } else {
            tokenModel.setToken(refreshToken);
            tokenService.updateToken(tokenModel);
        }

        result.put("accessToken", accessToken);
        result.put("refreshToken", refreshToken);

        return result;
    }


    

	
}
