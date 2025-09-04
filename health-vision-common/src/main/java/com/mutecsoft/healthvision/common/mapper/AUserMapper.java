package com.mutecsoft.healthvision.common.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import com.mutecsoft.healthvision.common.dto.UserDto.UserInfo;
import com.mutecsoft.healthvision.common.dto.admin.AdminUserDto.SearchUser;
import com.mutecsoft.healthvision.common.model.User;

@Mapper
public interface AUserMapper {

	User selectUserByEmail(String email);
    
	User selectUserByUserId(Long userId);
	
	void updateLastLoginDt(Long userId);

	List<UserInfo> selectUserList(SearchUser searchParam, Pageable pageable);
	int selectUserListCnt(SearchUser searchParam, Pageable pageable);
	
	
   

    void deleteUserList(@Param("userIds") List<Long> userIds, @Param("emailSuffix") String emailSuffix);

	void insertUser(User user);

	void deleteUser(@Param("userId") Long userId, @Param("emailSuffix") String emailSuffix);

	void updateUser(User user);

	
}
