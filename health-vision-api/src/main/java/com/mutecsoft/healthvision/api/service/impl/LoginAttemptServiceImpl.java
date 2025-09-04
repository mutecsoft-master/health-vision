package com.mutecsoft.healthvision.api.service.impl;

import org.springframework.stereotype.Service;

import com.mutecsoft.healthvision.api.service.LoginAttemptService;
import com.mutecsoft.healthvision.common.mapper.LoginAttemptMapper;
import com.mutecsoft.healthvision.common.model.LoginAttempt;
import com.mutecsoft.healthvision.common.util.RequestUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoginAttemptServiceImpl implements LoginAttemptService {

	private final LoginAttemptMapper loginAttemptMapper;
	
	@Override
	public LoginAttempt getLoginAttempt(String email) {
    	
    	LoginAttempt reqLoginAttempt = new LoginAttempt();
    	reqLoginAttempt.setEmail(email);
    	reqLoginAttempt.setIp(RequestUtil.getRemoteIp());
    	
    	return loginAttemptMapper.selectLastLoginAttempt(reqLoginAttempt);
	}
    
	@Override
	public void saveFailLoginAttempt(String email) {
    	
    	LoginAttempt loginAttempt = new LoginAttempt();
    	loginAttempt.setEmail(email);
    	loginAttempt.setIp(RequestUtil.getRemoteIp());
    	
    	LoginAttempt lastLoginAttempt = loginAttemptMapper.selectLastLoginAttempt(loginAttempt);
    	
    	if(lastLoginAttempt != null) {
    		loginAttemptMapper.updateFailCnt(lastLoginAttempt);
    	}else {
    		loginAttemptMapper.insertLoginAttempt(loginAttempt);
    	}
    	
	}
    
	@Override
	public void initLoginAttempt(String email) {
    	LoginAttempt loginAttempt = new LoginAttempt();
    	loginAttempt.setEmail(email);
    	loginAttempt.setIp(RequestUtil.getRemoteIp());
    	
    	LoginAttempt lastLoginAttempt = loginAttemptMapper.selectLastLoginAttempt(loginAttempt);
    	if(lastLoginAttempt != null) {
    		loginAttemptMapper.initLoginAttempt(lastLoginAttempt);
    	}
    	
	}
	

}
