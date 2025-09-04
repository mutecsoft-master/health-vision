package com.mutecsoft.healthvision.api.security;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.mutecsoft.healthvision.api.service.LoginAttemptService;
import com.mutecsoft.healthvision.api.service.UserService;
import com.mutecsoft.healthvision.common.constant.Const;
import com.mutecsoft.healthvision.common.dto.UserDto.UserInfo;
import com.mutecsoft.healthvision.common.model.LoginAttempt;
import com.mutecsoft.healthvision.common.model.User;
import com.mutecsoft.healthvision.common.util.MessageUtil;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final LoginAttemptService loginAttemptService;
    private final ModelMapper modelMapper;
    private final MessageUtil messageUtil;
    
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String userPw = (String) authentication.getCredentials();

        // 로그인 횟수제한 확인
        LoginAttempt attempt = loginAttemptService.getLoginAttempt(email);
        if (attempt != null && attempt.getFailCnt() >= Const.MAX_LOGIN_ATTEMPT_CNT) {
            if (!attempt.getLastAttemptDt().isBefore(LocalDateTime.now().minusHours(Const.LOGIN_ATTEMPT_WAIT_HOURS))) {
            	throw new LockedException(messageUtil.getMessage("web.login.attemptLimitExceeded", Const.LOGIN_ATTEMPT_WAIT_HOURS));
            } else {
                loginAttemptService.initLoginAttempt(email);
            }
        }

        User user = userService.selectUserByEmail(email);
        if (user == null) {
            loginAttemptService.saveFailLoginAttempt(email);
            throw new BadCredentialsException(messageUtil.getMessage("web.login.authenticationFailed"));
        }

        boolean isNormalPwValid = passwordEncoder.matches(userPw, user.getUserPw());
        boolean isTempPwValid = passwordEncoder.matches(userPw, user.getTempPw()) 
                                && user.getTempPwExpireDt().isAfter(LocalDateTime.now());
        
        if (!(isNormalPwValid || isTempPwValid)) {
        	loginAttemptService.saveFailLoginAttempt(email);
        	throw new BadCredentialsException(messageUtil.getMessage("web.login.authenticationFailed"));
        }

        loginAttemptService.initLoginAttempt(email);
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