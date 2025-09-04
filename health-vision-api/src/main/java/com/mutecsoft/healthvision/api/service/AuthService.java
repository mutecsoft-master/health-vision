package com.mutecsoft.healthvision.api.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;

import com.mutecsoft.healthvision.common.dto.ResponseDto;
import com.mutecsoft.healthvision.common.dto.UserDto.LoginRequest;

public interface AuthService {

    ResponseDto login(LoginRequest loginReq);
    
    Map<String, String> refreshToken(HttpServletRequest request);
    
}
