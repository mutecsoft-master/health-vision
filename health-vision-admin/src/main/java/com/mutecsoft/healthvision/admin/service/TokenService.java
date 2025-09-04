package com.mutecsoft.healthvision.admin.service;

import java.util.List;

public interface TokenService {

	List<String> selectFcmTokenList(Long userId);
	List<String> selectFcmTokenList(List<Long> userIdList);
	void deleteFcmToken(String fcmToken);
	
}
