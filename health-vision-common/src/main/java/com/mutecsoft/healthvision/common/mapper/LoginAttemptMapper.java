package com.mutecsoft.healthvision.common.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.mutecsoft.healthvision.common.model.LoginAttempt;

@Mapper
public interface LoginAttemptMapper {
	
	void initLoginAttempt(LoginAttempt loginAttempt);
	LoginAttempt selectLastLoginAttempt(LoginAttempt loginAttempt);
    void insertLoginAttempt(LoginAttempt loginAttempt);
    void updateFailCnt(LoginAttempt loginAttempt);
	

	
}
