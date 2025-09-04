package com.mutecsoft.healthvision.api.service;

import java.util.List;

import com.mutecsoft.healthvision.common.constant.MultiTokenTypeEnum;
import com.mutecsoft.healthvision.common.constant.SingleTokenTypeEnum;
import com.mutecsoft.healthvision.common.model.Token;

public interface TokenService {

    void insertToken(Token tokenModel);

    void updateToken(Token tokenModel);

    boolean isVerifyTokenInDB(SingleTokenTypeEnum singleTokenTypeEnum, String token);

    Token selectToken(Long userId, SingleTokenTypeEnum singleTokenTypeEnum);
    List<Token> selectTokenList(Long userId, MultiTokenTypeEnum multiTokenTypeEnum);
    Token selectTokenByToken(Long userId, String tokenType, String token);
}
