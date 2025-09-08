package com.mutecsoft.healthvision.common.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.mutecsoft.healthvision.common.model.User;

@Mapper
public interface UserMapper {
	
	User selectUserByEmail(String email);

	User selectUserByUserId(Long userId);
	
    void insertUser(User user);

    void deleteUser(@Param("userId") Long userId, @Param("emailSuffix")String emailSuffix);

    void updateLastLoginDt(Long userId);

}
