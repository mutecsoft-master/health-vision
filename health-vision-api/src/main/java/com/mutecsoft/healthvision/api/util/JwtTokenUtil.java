package com.mutecsoft.healthvision.api.util;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.mutecsoft.healthvision.common.constant.Const;
import com.mutecsoft.healthvision.common.constant.SingleTokenTypeEnum;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtTokenUtil {

    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @Value("${jwt.base64.secret}")
    private String secret;

    @Value("${jwt.access-expire-time}")
    private String accessExpireTime;

    @Value("${jwt.refresh-expire-day}")
    private String refreshExpireDay;
    
    @Value("${jwt.password-expire-minute}")
    private String passwordExpireMinute;

    private Key createKey() {

        //secret 길이가 짧아서 sha256 사용. secret 자체를 늘려도 됨
        String sha256hex = DigestUtils.sha256Hex(secret);
        byte[] apiKeySecretBytes = Base64.getDecoder().decode(sha256hex);

        return new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
    }

    public String getUserIdFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getIssuedAtDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getIssuedAt);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(createKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String getCustomClaimsFromToken(String token, String key) {
        Claims claims = getAllClaimsFromToken(token);
        return String.valueOf(claims.get(key));
    }

    public boolean isTokenExpired(String token) {
        try {
            Date exp = getExpirationDateFromToken(token);
            return exp.before(new Date());
        } catch (ExpiredJwtException e) {
            log.error("## Token Expired : {}", token);
            return true;
        }
    }
    
    public boolean isValidToken(String token) {
    	return StringUtils.hasText(token) && !isTokenExpired(token);
    }

    public String generateToken(String tokenType, String userId) {
        Map<String, Object> claims = new HashMap<>();
        if (tokenType.equals(SingleTokenTypeEnum.ACCESS.getValue())) {
            claims.put("type", SingleTokenTypeEnum.ACCESS.getValue());
        } else if(tokenType.equals(SingleTokenTypeEnum.REFRESH.getValue())) {
            claims.put("type", SingleTokenTypeEnum.REFRESH.getValue());
        } 

        //필요한 클레임은 여기에 추가
        return doGenerateToken(tokenType, claims, userId);
    }

    private String doGenerateToken(String tokenType, Map<String, Object> claims, String subject) {
        Map<String, Object> headerMap = new HashMap<String, Object>();
        headerMap.put("typ", "JWT");

        return Jwts.builder()
                .setHeader(headerMap)
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + getExpireTimeMills(tokenType)))
                .signWith(createKey(), signatureAlgorithm).compact();
    }

    private long getExpireTimeMills(String tokenType) {
        if (tokenType.equals(SingleTokenTypeEnum.ACCESS.getValue())) {
            return (long) Integer.parseInt(accessExpireTime) * 60 * 60 * 1000; //시간
        } else if(tokenType.equals(SingleTokenTypeEnum.REFRESH.getValue())){
            return (long) Integer.parseInt(refreshExpireDay) * 24 * 60 * 60 * 1000; //일
        }
		return 0;

    }

    public String getTokenType(String token) {
        return getCustomClaimsFromToken(token, "type");
    }
    
    public String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(Const.TOKEN_REQUEST_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(Const.TOKEN_REQUEST_HEADER_PREFIX + " ")) {

            bearerToken = bearerToken.replace(Const.TOKEN_REQUEST_HEADER_PREFIX + " ", "");
            return bearerToken.replace(Const.TOKEN_REQUEST_HEADER_PREFIX + " ", "");
        }
        return null;
    }

}
