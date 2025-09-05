package com.mutecsoft.healthvision.api.service;

import java.io.IOException;

import com.mutecsoft.healthvision.common.dto.ResponseDto;
import com.mutecsoft.healthvision.common.dto.UserDto.SignupRequest;
import com.mutecsoft.healthvision.common.dto.UserDto.UserUpdateRequest;
import com.mutecsoft.healthvision.common.model.User;

public interface UserService {

	User selectUserByEmail(String email);
	
    User selectUserByUserId(Long userId);

    void insertUser(User user);

    void updateLastLoginDt(Long userId);

	ResponseDto signup(SignupRequest signupReq);

	void deleteUser();

}
