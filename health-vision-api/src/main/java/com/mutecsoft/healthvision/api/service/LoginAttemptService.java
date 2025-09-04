package com.mutecsoft.healthvision.api.service;

import com.mutecsoft.healthvision.common.model.LoginAttempt;

public interface LoginAttemptService {
	
	LoginAttempt getLoginAttempt(String email);
    
    void saveFailLoginAttempt(String email);
    
    void initLoginAttempt(String email);
}
