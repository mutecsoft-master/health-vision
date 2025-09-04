package com.mutecsoft.healthvision.common.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.mutecsoft.healthvision.common.model.Token;

@Mapper
public interface TokenMapper {
    void insertToken(Token token);
    void updateToken(Token token);
    
    Token selectToken(Long userId, String tokenType);
    List<Token> selectTokenList(Long userId, String tokenType);
    Token selectTokenByToken(Long userId, String tokenType, String token);
    
	List<String> selectTokenListByUserIdList(@Param("userIdList") List<Long> userIdList, @Param("tokenType") String tokenType);
	void deleteToken(@Param("tokenType") String tokenType, @Param("token") String token);
}
