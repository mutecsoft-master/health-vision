package com.mutecsoft.healthvision.admin.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mutecsoft.healthvision.admin.service.TokenService;
import com.mutecsoft.healthvision.common.constant.MultiTokenTypeEnum;
import com.mutecsoft.healthvision.common.mapper.TokenMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

	private final TokenMapper tokenMapper;
	
	@Override
	public List<String> selectFcmTokenList(Long userId) {
		return selectFcmTokenList(List.of(userId));
	}
	
	
	@Override
	public List<String> selectFcmTokenList(List<Long> userIdList) {
		return tokenMapper.selectTokenListByUserIdList(userIdList, MultiTokenTypeEnum.FCM.getValue());
	}


	@Override
	public void deleteFcmToken(String token) {
		tokenMapper.deleteToken(MultiTokenTypeEnum.FCM.getValue(), token);
	}


}
