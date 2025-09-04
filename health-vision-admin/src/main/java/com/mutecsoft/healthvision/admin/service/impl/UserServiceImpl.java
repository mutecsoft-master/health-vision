package com.mutecsoft.healthvision.admin.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.mutecsoft.healthvision.admin.service.UserService;
import com.mutecsoft.healthvision.admin.util.UserUtil;
import com.mutecsoft.healthvision.common.constant.Const;
import com.mutecsoft.healthvision.common.dto.ResponseDto;
import com.mutecsoft.healthvision.common.dto.UserDto.UserInfo;
import com.mutecsoft.healthvision.common.dto.admin.AdminUserDto.RegisterAnalystRequest;
import com.mutecsoft.healthvision.common.dto.admin.AdminUserDto.SearchUser;
import com.mutecsoft.healthvision.common.dto.admin.AdminUserDto.UserUpdateRequest;
import com.mutecsoft.healthvision.common.mapper.ARoleMapper;
import com.mutecsoft.healthvision.common.mapper.AUserMapper;
import com.mutecsoft.healthvision.common.mapper.AUserRoleMapper;
import com.mutecsoft.healthvision.common.mapper.UserRoleMapper;
import com.mutecsoft.healthvision.common.model.Role;
import com.mutecsoft.healthvision.common.model.User;
import com.mutecsoft.healthvision.common.model.UserRole;
import com.mutecsoft.healthvision.common.util.CommonUtil;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final AUserMapper aUserMapper;
    private final ARoleMapper aRoleMapper;
    private final AUserRoleMapper aUserRoleMapper;
    private final UserRoleMapper userRoleMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserUtil userUtil;

    @Override
    public User selectUserByEmail(String email) {
        User user = aUserMapper.selectUserByEmail(email);
    	if(user != null) {
	    	List<Role> roleList = aRoleMapper.selectRoleListByUserId(user.getUserId());
	    	user.setRoleNmList(roleList.stream().map(r -> r.getRoleNm()).collect(Collectors.toList()));
    	}
    	
        return user;
    }
    
    @Override
    public User selectUserByUserId(Long userId) {
        User user = aUserMapper.selectUserByUserId(userId);
        if(user != null) {
	    	List<Role> roleList = aRoleMapper.selectRoleListByUserId(user.getUserId());
	    	user.setRoleNmList(roleList.stream().map(r -> r.getRoleNm()).collect(Collectors.toList()));
    	}
        
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
		for(UserInfo userInfo : userList) {
			if(userInfo != null) {
		    	List<Role> roleList = aRoleMapper.selectRoleListByUserId(userInfo.getUserId());
		    	userInfo.setRoleNmList(roleList.stream().map(r -> r.getRoleNm()).collect(Collectors.toList()));
	    	}
		}
		
        int totalCnt = aUserMapper.selectUserListCnt(searchParam, pageable);

        return new PageImpl<>(userList, pageable, totalCnt);
		
	}

    @Override
    public PageImpl<UserInfo> selectAnalystListPage(SearchUser searchParam, Pageable pageable) {

        searchParam.setSearchRoleNm(Const.ROLE_ANALYST);
        List<UserInfo> userList = aUserMapper.selectUserList(searchParam, pageable);
        for(UserInfo userInfo : userList) {
			if(userInfo != null) {
		    	List<Role> roleList = aRoleMapper.selectRoleListByUserId(userInfo.getUserId());
		    	userInfo.setRoleNmList(roleList.stream().map(r -> r.getRoleNm()).collect(Collectors.toList()));
	    	}
		}
        
        int totalCnt = aUserMapper.selectUserListCnt(searchParam, pageable);

        return new PageImpl<>(userList, pageable, totalCnt);
    }

	@Override
	public ResponseDto registerAnalyst(RegisterAnalystRequest registerAnlystReq) {
		
		User user = selectUserByEmail(registerAnlystReq.getEmail());
		
		if(user == null) {
			user = new User();
		    user.setEmail(registerAnlystReq.getEmail());
	        user.setUserPw(passwordEncoder.encode(registerAnlystReq.getUserPw()));
	        aUserMapper.insertUser(user);
	        
	        Role role = aRoleMapper.selectRoleByRoleNm(Const.ROLE_ANALYST);
	        UserRole userRole = new UserRole();
	        userRole.setRoleId(role.getRoleId());
	        userRole.setUserId(user.getUserId());
	        userRoleMapper.insertUserRole(userRole);
	        
		}else {
			if(!user.getRoleNmList().contains(Const.ROLE_ANALYST)) {
				Role role = aRoleMapper.selectRoleByRoleNm(Const.ROLE_ANALYST);
		        UserRole userRole = new UserRole();
		        userRole.setRoleId(role.getRoleId());
		        userRole.setUserId(user.getUserId());
		        userRoleMapper.insertUserRole(userRole);
		        
		        return new ResponseDto(true, "기존 회원정보에 분석가 권한을 추가했습니다.", null);
			}else {
				return new ResponseDto(true, "이미 등록된 분석가입니다.", null);
			}
		}
		
		return new ResponseDto(true);
	}

	@Override
	public void deleteAnalystList(List<Long> userIdList) {
		
		Role analystRole = aRoleMapper.selectRoleByRoleNm(Const.ROLE_ANALYST);
		
		//분석가 Role만 갖는 경우 user 도 삭제, 분석가 외 Role이 있으면 user_role 만 삭제
		for(Long userId : userIdList) {
			User user = selectUserByUserId(userId);
			
			List<String> roleNmList = user.getRoleNmList();
			boolean hasOtherRole = roleNmList.stream()
			        .anyMatch(role -> !Const.ROLE_ANALYST.equals(role));
			
			aUserRoleMapper.deleteRoleByUserIdAndRoleId(userId, analystRole.getRoleId());
			if(!hasOtherRole) {
				aUserMapper.deleteUser(userId, CommonUtil.makeDeleteUserSuffix());
			}
		}
		
	}

	@Override
	public void updateUser(UserUpdateRequest updateReq) {
		UserInfo adminInfo = userUtil.getUserInfo();
		
		User user = new User();
		user.setUserId(updateReq.getUserId());
		user.setNickname(updateReq.getNickname());
		if(StringUtils.hasText(updateReq.getUserPw())) {
			user.setUserPw(passwordEncoder.encode(updateReq.getUserPw()));
		}
		user.setUpdId(adminInfo.getUserId());
		
		aUserMapper.updateUser(user);
	}
}
