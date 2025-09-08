package com.mutecsoft.healthvision.api.controller.api;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mutecsoft.healthvision.api.service.AuthService;
import com.mutecsoft.healthvision.common.dto.ResponseDto;
import com.mutecsoft.healthvision.common.dto.UserDto.LoginRequest;
import com.mutecsoft.healthvision.common.exception.CustomException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Auth", description = "일반 로그인")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    /**
     * 로그인
     *
     * @param request
     * @param response
     * @param userMap
     * @return
     */
    @Operation(summary = "일반 로그인", description = "Email / Password 기반 로그인")
    @PostMapping("/login")
    public ResponseEntity<ResponseDto> login(HttpServletRequest request
            , HttpServletResponse response, @RequestBody @Valid LoginRequest loginReq) {

        ResponseDto responseDto = authService.login(loginReq);
        return ResponseEntity.ok(responseDto);

    }
    
    
    //토큰 refresh
    @Operation(summary = "Refresh 토큰 재발급", description = "Refresh 토큰 재발급")
    @PostMapping("/refreshToken")
    public ResponseEntity<ResponseDto> refreshToken(HttpServletRequest request
            , HttpServletResponse respons) {

        Map<String, String> tokenMap = authService.refreshToken(request);
        
        if(tokenMap != null) {
        	return ResponseEntity.ok(new ResponseDto(true, tokenMap));
        }else {
        	throw new CustomException("유효하지 않은 토큰입니다.", HttpStatus.UNAUTHORIZED);
        }
    }

    
}
