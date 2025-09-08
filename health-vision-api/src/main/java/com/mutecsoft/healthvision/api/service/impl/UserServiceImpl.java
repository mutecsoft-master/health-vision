package com.mutecsoft.healthvision.api.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mutecsoft.healthvision.api.service.UserService;
import com.mutecsoft.healthvision.api.util.UserUtil;
import com.mutecsoft.healthvision.common.dto.ResponseDto;
import com.mutecsoft.healthvision.common.dto.UserDto.SignupRequest;
import com.mutecsoft.healthvision.common.dto.UserDto.UserInfo;
import com.mutecsoft.healthvision.common.mapper.UserMapper;
import com.mutecsoft.healthvision.common.model.User;
import com.mutecsoft.healthvision.common.util.CommonUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserUtil userUtil;

    @Override
    public User selectUserByEmail(String email) {
    	User user = userMapper.selectUserByEmail(email);
    	
        return user;
    }

    @Override
    public User selectUserByUserId(Long userId) {
    	User user = userMapper.selectUserByUserId(userId);
    	
        return user;
    }

    @Transactional
    @Override
    public void insertUser(User user) {
        userMapper.insertUser(user);
    }
    
    @Override
    public void updateLastLoginDt(Long userId) {
        userMapper.updateLastLoginDt(userId);
    }

    
    @Transactional
    @Override
	public ResponseDto signup(SignupRequest signupReq) {

    	//가입된 계정인지 확인
    	User user = selectUserByEmail(signupReq.getEmail());
		if(user != null) {
			return new ResponseDto(false, "이미 가입된 계정입니다.");
		}
		
		//회원가입
		user = new User();
	    user.setEmail(signupReq.getEmail());
        user.setUserPw(passwordEncoder.encode(signupReq.getUserPw()));
	    insertUser(user);
    	
	    return new ResponseDto(true);
		
	}

    @Override
    public void deleteUser() {
    	UserInfo userInfo = userUtil.getUserInfo();
    	userMapper.deleteUser(userInfo.getUserId(), CommonUtil.makeDeleteUserSuffix());
    }
    
}
