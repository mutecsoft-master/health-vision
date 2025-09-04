package com.mutecsoft.healthvision.admin.security;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.mutecsoft.healthvision.admin.service.UserService;
import com.mutecsoft.healthvision.common.constant.Const;
import com.mutecsoft.healthvision.common.dto.UserDto.UserInfo;
import com.mutecsoft.healthvision.common.model.User;
import com.mutecsoft.healthvision.common.util.MessageUtil;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final MessageUtil messageUtil;
    
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String userPw = (String) authentication.getCredentials();

        User user = userService.selectUserByEmail(email);
        if (user == null) {
            throw new BadCredentialsException(messageUtil.getMessage("web.login.authenticationFailed"));
        }

        boolean isNormalPwValid = passwordEncoder.matches(userPw, user.getUserPw());
        
        if (!isNormalPwValid) {
        	throw new BadCredentialsException(messageUtil.getMessage("web.login.authenticationFailed"));
        }
        
        if(!user.getRoleNmList().contains(Const.ROLE_ADMIN) && !user.getRoleNmList().contains(Const.ROLE_ANALYST)) {
        	throw new BadCredentialsException(messageUtil.getMessage("web.accessDenied"));
        }

        userService.updateLastLoginDt(user.getUserId());

        UserInfo userInfo = modelMapper.map(user, UserInfo.class);
        List<GrantedAuthority> authorities = userInfo.getRoleNmList().stream()
    	    .map(roleNm -> new SimpleGrantedAuthority(Const.ROLE_PREFIX + roleNm))
    	    .collect(Collectors.toList());

        return new UsernamePasswordAuthenticationToken(userInfo, null, authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}