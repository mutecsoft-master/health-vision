package com.mutecsoft.healthvision.api.controller.api;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mutecsoft.healthvision.api.service.AuthService;
import com.mutecsoft.healthvision.api.service.UserService;
import com.mutecsoft.healthvision.api.util.UserUtil;
import com.mutecsoft.healthvision.common.constant.ResultCdEnum;
import com.mutecsoft.healthvision.common.dto.ResponseDto;
import com.mutecsoft.healthvision.common.dto.UserDto.EmailRequest;
import com.mutecsoft.healthvision.common.dto.UserDto.LoginRequest;
import com.mutecsoft.healthvision.common.dto.UserDto.NicknameRequest;
import com.mutecsoft.healthvision.common.dto.UserDto.SignupRequest;
import com.mutecsoft.healthvision.common.dto.UserDto.UserInfo;
import com.mutecsoft.healthvision.common.model.User;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "User", description = "회원관리")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
	
	private final AuthService authService;
	private final UserService userService;
	private final UserUtil userUtil;

	//이메일 중복 확인
	@Operation(summary = "이메일 중복 확인", description = "이메일 중복 확인")
	@PostMapping("/email/check")
	public ResponseEntity<ResponseDto> checkEmail(HttpServletRequest request
            , HttpServletResponse response, @RequestBody @Valid EmailRequest emailReq) {
		
		User user = userService.selectUserByEmail(emailReq.getEmail());
		if(user == null) {
			return ResponseEntity.ok(new ResponseDto(true, "사용 가능한 메일 주소입니다."));
		}else {
			return ResponseEntity.ok(new ResponseDto(false, "이미 가입된 메일 주소입니다."));
		}
		
	}
	
	//회원 가입
	@Operation(summary = "회원가입", description = "회원가입")
	@PostMapping
	public ResponseEntity<ResponseDto> signUp(@Valid @RequestBody SignupRequest signupReq) {
		
		//회원 가입
		ResponseDto signupResult = userService.signup(signupReq);
		
		if(signupResult.isSuccess()) {
			//자동 로그인 처리
		    LoginRequest loginReq = new LoginRequest();
		    loginReq.setEmail(signupReq.getEmail());
		    loginReq.setUserPw(signupReq.getUserPw());
		    ResponseDto loginResult = authService.login(loginReq);
		    return ResponseEntity.ok(loginResult);
		}else {
			return ResponseEntity.ok(signupResult);
		}
		
    }
	
	//회원정보 조회
	@Operation(summary = "회원정보 조회", description = "회원정보 조회")
	@GetMapping
	public ResponseEntity<ResponseDto> getUser(HttpServletRequest request) {
		
		UserInfo userInfo = userUtil.getUserInfo();
		
		return ResponseEntity.ok(new ResponseDto(true, userInfo));
	}
	
	//회원 탈퇴
	@Operation(summary = "회원탈퇴", description = "회원탈퇴")
	@DeleteMapping
	public ResponseEntity<ResponseDto> deleteUser(HttpServletRequest request) {
		
		userService.deleteUser();
		return ResponseEntity.ok(new ResponseDto(true));
	}
	
}
