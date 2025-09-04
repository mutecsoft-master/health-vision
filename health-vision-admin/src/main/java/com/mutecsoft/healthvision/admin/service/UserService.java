package com.mutecsoft.healthvision.admin.service;

import java.util.List;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.mutecsoft.healthvision.common.dto.ResponseDto;
import com.mutecsoft.healthvision.common.dto.UserDto.UserInfo;
import com.mutecsoft.healthvision.common.dto.admin.AdminUserDto.RegisterAnalystRequest;
import com.mutecsoft.healthvision.common.dto.admin.AdminUserDto.SearchUser;
import com.mutecsoft.healthvision.common.dto.admin.AdminUserDto.UserUpdateRequest;
import com.mutecsoft.healthvision.common.model.User;

public interface UserService {

	User selectUserByEmail(String email);
	
    User selectUserByUserId(Long userId);

    void updateLastLoginDt(Long userId);
    
    void updateUser(UserUpdateRequest updateReq);

    void deleteUserList(List<Long> userIdList);

	PageImpl<UserInfo> selectUserListPage(SearchUser searchParam, Pageable pageable);

    PageImpl<UserInfo> selectAnalystListPage(SearchUser searchParam, Pageable pageable);

	ResponseDto registerAnalyst(RegisterAnalystRequest registerAnlystReq);

	void deleteAnalystList(List<Long> userIdList);

	
}
