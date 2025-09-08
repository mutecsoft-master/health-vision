package com.mutecsoft.healthvision.admin.service.impl;

import java.util.List;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.mutecsoft.healthvision.admin.service.UserService;
import com.mutecsoft.healthvision.admin.util.UserUtil;
import com.mutecsoft.healthvision.common.dto.UserDto.UserInfo;
import com.mutecsoft.healthvision.common.dto.admin.AdminUserDto.SearchUser;
import com.mutecsoft.healthvision.common.dto.admin.AdminUserDto.UserUpdateRequest;
import com.mutecsoft.healthvision.common.mapper.AUserMapper;
import com.mutecsoft.healthvision.common.model.User;
import com.mutecsoft.healthvision.common.util.CommonUtil;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final AUserMapper aUserMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserUtil userUtil;

    @Override
    public User selectUserByEmail(String email) {
        User user = aUserMapper.selectUserByEmail(email);
    	
        return user;
    }
    
    @Override
    public User selectUserByUserId(Long userId) {
        User user = aUserMapper.selectUserByUserId(userId);
        
        return user;
    }

    @Override
    public void updateLastLoginDt(Long userId) {
        aUserMapper.updateLastLoginDt(userId);
    }

    @Override
    public void deleteUserList(List<Long> userIdList) {
        aUserMapper.deleteUserList(userIdList, CommonUtil.makeDeleteUserSuffix());
    }

	@Override
	public PageImpl<UserInfo> selectUserListPage(SearchUser searchParam, Pageable pageable) {
		
		List<UserInfo> userList = aUserMapper.selectUserList(searchParam, pageable);
        int totalCnt = aUserMapper.selectUserListCnt(searchParam, pageable);

        return new PageImpl<>(userList, pageable, totalCnt);
		
	}

	@Override
	public void updateUser(UserUpdateRequest updateReq) {
		UserInfo adminInfo = userUtil.getUserInfo();
		
		User user = new User();
		user.setUserId(updateReq.getUserId());
		if(StringUtils.hasText(updateReq.getUserPw())) {
			user.setUserPw(passwordEncoder.encode(updateReq.getUserPw()));
		}
		user.setUpdId(adminInfo.getUserId());
		
		aUserMapper.updateUser(user);
	}
}
