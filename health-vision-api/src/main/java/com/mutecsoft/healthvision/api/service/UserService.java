package com.mutecsoft.healthvision.api.service;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.mutecsoft.healthvision.common.dto.ResponseDto;
import com.mutecsoft.healthvision.common.dto.TokenDto.FcmTokenInsertRequest;
import com.mutecsoft.healthvision.common.dto.TokenDto.FcmTokenUpdateRequest;
import com.mutecsoft.healthvision.common.dto.UserDto.EmailAuthRequest;
import com.mutecsoft.healthvision.common.dto.UserDto.PwRequest;
import com.mutecsoft.healthvision.common.dto.UserDto.SignupRequest;
import com.mutecsoft.healthvision.common.dto.UserDto.UserUpdateRequest;
import com.mutecsoft.healthvision.common.model.Token;
import com.mutecsoft.healthvision.common.model.User;

public interface UserService {

	User selectUserByEmail(String email);
	
    User selectUserByUserId(Long userId);

    void insertUser(User user);

    void updateLastLoginDt(Long userId);

	ResponseDto sendAuthEmail(String email);

	ResponseDto verifyAuthEmail(EmailAuthRequest emailAuthReq);

	ResponseDto signup(SignupRequest signupReq);

	ResponseDto updateProfile(UserUpdateRequest updateReq) throws IOException;
	void updateBodyInfo(UserUpdateRequest updateReq);
	
	void deleteUser();

	ResponseDto findPw(String email);

	ResponseDto changePw(PwRequest pwReq);

	User selectUserByNicknameAndNotUserId(String nickname, Long userId);

	void insertFcmToken(FcmTokenInsertRequest insertReq);

	void updateFcmToken(FcmTokenUpdateRequest updateReq);

	List<Token> selectFcmTokenList();

	

	

}
