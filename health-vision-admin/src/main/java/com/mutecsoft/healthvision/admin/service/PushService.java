package com.mutecsoft.healthvision.admin.service;

import java.util.List;

import com.mutecsoft.healthvision.common.constant.PushTypeEnum;
import com.mutecsoft.healthvision.common.dto.PushDto;

public interface PushService {
	
	void initFirebase();
	
	void sendPush(Long userId, PushTypeEnum pushTypeEnum, PushDto pushDto);
	void sendPushMulticast(List<Long> userIdList, PushTypeEnum pushTypeEnum, PushDto pushDto);
	
}
