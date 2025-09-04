package com.mutecsoft.healthvision.api.controller.api;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.mutecsoft.healthvision.api.service.TokenService;
import com.mutecsoft.healthvision.api.service.UserService;
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

@Tag(name = "Auth Google", description = "구글 로그인")
@RestController
@RequestMapping("/api/auth/google")
@RequiredArgsConstructor
public class AuthGoogleController {
	
	private final ClientRegistrationRepository clientRegistrationRepository;
    private final UserService userService;
    private final TokenService tokenService;
    private final JwtTokenUtil jwtTokenUtil;

    //App 에서 자체적으로 발급받은 id_token을 서버에서 검증하여 로그인 처리
    @Operation(summary = "구글 로그인", description = "App에서 발급받은 id_token 으로 인증")
    @PostMapping("/login")
    public ResponseEntity<ResponseDto> googleLogin(@RequestBody @Valid UserDto.OauthLoginRequest loginReq) {
    	
    	//application.yml 에서 google oauth 관련 정보 불러옴
    	ClientRegistration registration = clientRegistrationRepository.findByRegistrationId("google");
    	String clientId = registration.getClientId();
    	
    	// id_token 검증
    	GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance())
                .setAudience(Collections.singletonList(clientId))
                .build();
    	
    	String email = "";
        String sub = ""; 
        GoogleIdToken googleIdToken;
		try {
			googleIdToken = verifier.verify(loginReq.getIdToken());
			
			if (googleIdToken != null) {
	            Payload payload = googleIdToken.getPayload();
	            email = payload.getEmail();
	            sub = payload.getSubject();
	    	
				//회원가입
				User user = userService.selectUserByEmail(email);
				if (user == null) {
				    String snsUserId = sub; // snsUserId
				    user = new User();
				    user.setEmail(email);
				    user.setSnsProvider("google");
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
				throw new CustomException(ResultCdEnum.L101.getValue());
			}
		} catch (GeneralSecurityException | IOException e) {
			throw new CustomException(ResultCdEnum.L101.getValue());
		}
    }
    
}
