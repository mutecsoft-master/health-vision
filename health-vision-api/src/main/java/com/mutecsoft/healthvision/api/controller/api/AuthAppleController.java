package com.mutecsoft.healthvision.api.controller.api;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mutecsoft.healthvision.api.service.TokenService;
import com.mutecsoft.healthvision.api.service.UserService;
import com.mutecsoft.healthvision.api.util.AppleUtil;
import com.mutecsoft.healthvision.api.util.JwtTokenUtil;
import com.mutecsoft.healthvision.common.constant.ResultCdEnum;
import com.mutecsoft.healthvision.common.constant.SingleTokenTypeEnum;
import com.mutecsoft.healthvision.common.dto.ResponseDto;
import com.mutecsoft.healthvision.common.dto.UserDto;
import com.mutecsoft.healthvision.common.exception.CustomException;
import com.mutecsoft.healthvision.common.model.Token;
import com.mutecsoft.healthvision.common.model.User;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Auth Apple", description = "애플 로그인")
@RestController
@RequestMapping("/api/auth/apple")
@RequiredArgsConstructor
public class AuthAppleController {

    private final UserService userService;
    private final TokenService tokenService;
    private final AppleUtil appleUtil;
    private final JwtTokenUtil jwtTokenUtil;

    //App 에서 자체적으로 발급받은 id_token을 서버에서 검증하여 로그인 처리
    @Operation(summary = "애플 로그인", description = "App에서 발급받은 id_token 으로 인증")
    @PostMapping("/login")
    public ResponseEntity<ResponseDto> appleLogin(@RequestBody @Valid UserDto.OauthLoginRequest loginReq) {
    	
        // id_token 검증
        JSONObject claims = appleUtil.decodeAndValidateIdToken(loginReq.getIdToken());
        
        if(claims != null) {
        	String email = claims.get("email") == null ? "" : claims.get("email").toString();
            String sub = claims.get("sub") == null ? "" : claims.get("sub").toString(); 
        	
    		//회원가입
    		User user = userService.selectUserByEmail(email);
    		if (user == null) {
    		    String snsUserId = sub; // snsUserId
    		    user = new User();
    		    user.setEmail(email);
    		    user.setSnsProvider("apple");
    		    user.setSnsUserId(snsUserId);
    		    userService.insertUser(user);
    		}
    		
    		String accessToken = jwtTokenUtil.generateToken(SingleTokenTypeEnum.ACCESS.getValue(), String.valueOf(user.getUserId()));
    		String refreshToken = jwtTokenUtil.generateToken(SingleTokenTypeEnum.REFRESH.getValue(), String.valueOf(user.getUserId()));
    		
    		Token tokenModel = tokenService.selectToken(user.getUserId(), SingleTokenTypeEnum.REFRESH);
    		if (tokenModel == null) {
    			tokenModel = new Token();
    			tokenModel.setUserId(user.getUserId());
    			tokenModel.setTokenType(SingleTokenTypeEnum.REFRESH.getValue());
    			tokenModel.setToken(refreshToken);
    			tokenService.insertToken(tokenModel);
    		} else {
    		    tokenModel.setToken(refreshToken);
    		    tokenService.updateToken(tokenModel);
    		}

            Map<String, Object> result = new HashMap<>();
            result.put("accessToken", accessToken);
            result.put("refreshToken", refreshToken);

            return ResponseEntity.ok(new ResponseDto(true, result));
        }else {
        	throw new CustomException(ResultCdEnum.L102.getValue());
        }
        
        
    }
    
}
