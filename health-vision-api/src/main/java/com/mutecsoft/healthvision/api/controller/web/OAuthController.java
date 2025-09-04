package com.mutecsoft.healthvision.api.controller.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.mutecsoft.healthvision.api.security.CustomOAuth2User;
import com.mutecsoft.healthvision.api.service.UserService;
import com.mutecsoft.healthvision.api.util.AppleUtil;
import com.mutecsoft.healthvision.common.constant.Const;
import com.mutecsoft.healthvision.common.constant.ResultCdEnum;
import com.mutecsoft.healthvision.common.dto.ResponseDto;
import com.mutecsoft.healthvision.common.dto.UserDto.UserInfo;
import com.mutecsoft.healthvision.common.exception.CustomException;
import com.mutecsoft.healthvision.common.model.User;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;

//Web apple 로그인

@Hidden
@RestController
@RequestMapping("/web/oauth")
@RequiredArgsConstructor
public class OAuthController {

    private final UserService userService;
    private final AppleUtil appleUtil;
    private final ModelMapper modelMapper;

    @Value("${apple.login.team-id}")
    private String appleTeamId;

    @Value("${apple.login.client-id}")
    private String appleClientId;

    @Value("${apple.login.key-id}")
    private String appleKeyId;

    @Value("${apple.login.redirect-url}")
    private String appleRedirectUrl;

    @Value("${apple.login.key-path}")
    private String appleKeyPath;

    @Value("${apple.login.auth-url}")
    private String appleAuthUrl;

    @GetMapping("/appleAuthUrl")
    public @ResponseBody Map<String, Object> getAppleAuthUrl(
            HttpServletRequest request) {

        String authUrl = appleAuthUrl + "/auth/authorize?client_id=" + appleClientId + "&redirect_uri="
                + appleRedirectUrl + "&response_type=code id_token&scope=name email&response_mode=form_post";

        Map<String, Object> map = new HashMap<>();
        map.put("url", authUrl);

        return map;
    }

    @PostMapping(value = "/apple/return", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<ResponseDto> appleRedirect(String code, HttpServletResponse response) {

        /*
         * 첫 로그인 연결시 email 공유 여부 No를 선택시 xxxx@privaterelay.appleid.com 형식의 이메일을 리턴해줌.
         * 따라서 이용자가 No를 선택하면 실제 이메일주소를 알아낼 수 있는 방법은 없음.
         * name 은 제공해주지 않으므로 회원가입시 email 외에는 받을 수 있는 정보가 없음.
         * apple 로그인시 필수적으로 이메일을 제외한 모든 정보를 추가로 제공받아야함.
         * email 을 어떻게 처리할지에 대해서도 논의 필요. ex) 프로필에 보여지는 이메일을 어떻게 할 것인가.
         */
    	
        if (code == null) {
            return null;
        }

        //client secret 생성
        Map<String, String> appleAuthMap = new HashMap<>();
        appleAuthMap.put("appleTeamId", appleTeamId);
        appleAuthMap.put("appleClientId", appleClientId);
        appleAuthMap.put("appleKeyId", appleKeyId);
        appleAuthMap.put("appleKeyPath", appleKeyPath);
        appleAuthMap.put("appleAuthUrl", appleAuthUrl);
        String clientSecret = appleUtil.createClientSecret(appleAuthMap);

        //apple token 요청
        String appleTokenUrl = appleAuthUrl + "/auth/token";
        Map<String, String> tokenReqMap = new HashMap<>();
        tokenReqMap.put("client_id", appleClientId);
        tokenReqMap.put("client_secret", clientSecret);
        tokenReqMap.put("code", code);
        tokenReqMap.put("grant_type", "authorization_code");

        String tokenResStr = appleUtil.getAppleToken(appleTokenUrl, tokenReqMap);
        JSONObject tokenRes = new JSONObject(tokenResStr);
        JSONObject appleInfo = appleUtil.decodeFromIdToken(tokenRes.getString("id_token"));
        String email = appleInfo.get("email").toString();

        //회원가입
        User user = userService.selectUserByEmail(email);
        if (user == null) {
            String snsUserId = appleInfo.get("sub").toString(); // snsUserId
            user = new User();
            user.setEmail(email);
            user.setSnsProvider("apple");
            user.setSnsUserId(snsUserId);
            userService.insertUser(user);
        }
        
        UserInfo userInfo = modelMapper.map(user, UserInfo.class);
        List<GrantedAuthority> authorities = new ArrayList<>();
        if(user.getRoleNmList() != null) {
        	for(String roleNm : user.getRoleNmList()) {
        		authorities.add(new SimpleGrantedAuthority(Const.ROLE_PREFIX + roleNm));
        	}
        }
        
        CustomOAuth2User customOAuth2User = new CustomOAuth2User(userInfo, Collections.emptyMap(), authorities);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
    	    customOAuth2User, null, customOAuth2User.getAuthorities()
    	);

        //인증 정보 securityContextHolder에 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        try {
            response.sendRedirect("/web/main");
            return null;
        } catch (IOException e) {
        	throw new CustomException(ResultCdEnum.E001.getValue());
        }

    }

}
