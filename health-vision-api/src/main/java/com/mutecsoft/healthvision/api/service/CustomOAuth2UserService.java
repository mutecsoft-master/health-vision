package com.mutecsoft.healthvision.api.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.mutecsoft.healthvision.api.security.CustomOAuth2User;
import com.mutecsoft.healthvision.common.constant.Const;
import com.mutecsoft.healthvision.common.constant.ResultCdEnum;
import com.mutecsoft.healthvision.common.dto.UserDto.UserInfo;
import com.mutecsoft.healthvision.common.exception.CustomException;
import com.mutecsoft.healthvision.common.model.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserService userService;
    private final ModelMapper modelMapper;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest); //인터페이스에 userRequest를 받아 로드

        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        Map<String, Object> attributes = null;
        if (StringUtils.hasText(registrationId)) {
        	if(registrationId.contains("google")) { //google
        		attributes = oAuth2User.getAttributes();
        	}
        }
        
        if(attributes != null) {
        	// attributes의 pk값(user id 번호)를 value로 가지고 있는 key name
            String nameAttributeKey = userRequest.getClientRegistration().getProviderDetails()
                    .getUserInfoEndpoint().getUserNameAttributeName();

            // value example) google, apple, naver, kakao
            String snsProvider = userRequest.getClientRegistration().getRegistrationId();
            String snsUserId = attributes.get(nameAttributeKey).toString();
            String email = attributes.get("email").toString();
            String userNm = attributes.get("family_name").toString() + attributes.get("given_name").toString();

            User user = userService.selectUserByEmail(email);

            if (user == null) { // 신규
                user = new User();
                user.setEmail(email);
                user.setSnsProvider(snsProvider);
                user.setSnsUserId(snsUserId);

                userService.insertUser(user);

                user = userService.selectUserByEmail(email);
            } 

            List<GrantedAuthority> authorities = new ArrayList<>();
            if(user.getRoleNmList() != null) {
            	for(String roleNm : user.getRoleNmList()) {
            		authorities.add(new SimpleGrantedAuthority(Const.ROLE_PREFIX + roleNm));
            	}
            }
            
            UserInfo userInfo = modelMapper.map(user, UserInfo.class);
            CustomOAuth2User customOAuth2User = new CustomOAuth2User(userInfo, attributes, authorities);
            return customOAuth2User;
            
        }else {

        	ResultCdEnum resultCdEnum = ResultCdEnum.E001;
        	switch (registrationId) {
			case "google":
				resultCdEnum = ResultCdEnum.L101;
				break;
			default:
				break;
			}
        	
        	throw new CustomException(resultCdEnum.getValue());
        }
        
    }

}
