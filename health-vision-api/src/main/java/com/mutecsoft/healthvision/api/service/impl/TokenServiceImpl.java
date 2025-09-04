package com.mutecsoft.healthvision.api.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mutecsoft.healthvision.api.service.TokenService;
import com.mutecsoft.healthvision.api.util.JwtTokenUtil;
import com.mutecsoft.healthvision.common.constant.MultiTokenTypeEnum;
import com.mutecsoft.healthvision.common.constant.SingleTokenTypeEnum;
import com.mutecsoft.healthvision.common.mapper.TokenMapper;
import com.mutecsoft.healthvision.common.model.Token;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TokenServiceImpl implements TokenService {

    private final TokenMapper tokenMapper;
    private final JwtTokenUtil jwtTokenUtil;

    public void insertToken(Token tokenModel) {
        tokenMapper.insertToken(tokenModel);
    }

    public void updateToken(Token tokenModel) {
        tokenMapper.updateToken(tokenModel);
    }

	@Override
	public Token selectToken(Long userId, SingleTokenTypeEnum singleTokenTypeEnum) {
		return tokenMapper.selectToken(userId, singleTokenTypeEnum.getValue());
	}

	@Override
	public List<Token> selectTokenList(Long userId, MultiTokenTypeEnum multiTokenTypeEnum) {
		return tokenMapper.selectTokenList(userId, multiTokenTypeEnum.getValue());
	}
	
	@Override
	public Token selectTokenByToken(Long userId, String tokenType, String token) {
		return tokenMapper.selectTokenByToken(userId, tokenType, token);
	}
	
	
	
	public boolean isVerifyTokenInDB(SingleTokenTypeEnum singleTokenTypeEnum, String token) {

        Long userId = Long.parseLong(jwtTokenUtil.getUserIdFromToken(token));

        Token tokenModel = selectToken(userId, singleTokenTypeEnum);

        return tokenModel.getToken().equals(token);
    }

}
