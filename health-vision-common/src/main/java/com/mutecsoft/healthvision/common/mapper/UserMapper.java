package com.mutecsoft.healthvision.common.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.mutecsoft.healthvision.common.annotation.LoginId;
import com.mutecsoft.healthvision.common.model.User;

@Mapper
public interface UserMapper {
	
	User selectUserByEmail(String email);

	User selectUserByUserId(Long userId);
	
    void insertUser(User user);

    @LoginId
    void updateProfile(User user);
    
    @LoginId
    void updateBodyInfo(User user);

    void deleteUser(@Param("userId") Long userId, @Param("emailSuffix")String emailSuffix);

    void updateLastLoginDt(Long userId);

	void updateTempPw(User user);

	@LoginId
	void changePw(User user);

	//User selectUserByNickname(String nickname);
	User selectUserByNicknameAndNotUserId(@Param("nickname") String nickname, @Param("excludeUserId") Long excludeUserId);

	Integer selectNextNicknameIndex(String nicknamePrefix);

	

	

	
}
