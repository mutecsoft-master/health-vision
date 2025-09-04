package com.mutecsoft.healthvision.api.controller.api;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mutecsoft.healthvision.api.service.AuthService;
import com.mutecsoft.healthvision.api.service.UserService;
import com.mutecsoft.healthvision.api.util.UserUtil;
import com.mutecsoft.healthvision.common.constant.ResultCdEnum;
import com.mutecsoft.healthvision.common.dto.ResponseDto;
import com.mutecsoft.healthvision.common.dto.TokenDto.FcmTokenInsertRequest;
import com.mutecsoft.healthvision.common.dto.TokenDto.FcmTokenUpdateRequest;
import com.mutecsoft.healthvision.common.dto.UserDto.EmailAuthRequest;
import com.mutecsoft.healthvision.common.dto.UserDto.EmailRequest;
import com.mutecsoft.healthvision.common.dto.UserDto.LoginRequest;
import com.mutecsoft.healthvision.common.dto.UserDto.NicknameRequest;
import com.mutecsoft.healthvision.common.dto.UserDto.PwRequest;
import com.mutecsoft.healthvision.common.dto.UserDto.SignupRequest;
import com.mutecsoft.healthvision.common.dto.UserDto.UserInfo;
import com.mutecsoft.healthvision.common.dto.UserDto.UserUpdateRequest;
import com.mutecsoft.healthvision.common.model.Token;
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
			return ResponseEntity.ok(new ResponseDto(true, "Email available"));
		}else {
			return ResponseEntity.ok(new ResponseDto(false, null, ResultCdEnum.S001.getValue()));
		}
		
	}
	
	//이메일 인증 요청
	@Operation(summary = "이메일 인증 요청", description = "이메일 인증 요청 메일 발송")
	@PostMapping("/email/send")
	public ResponseEntity<ResponseDto> sendAuthEmail(HttpServletRequest request
            , HttpServletResponse response, @RequestBody @Valid EmailRequest emailReq) {
		
		ResponseDto responseDto = userService.sendAuthEmail(emailReq.getEmail());
		return ResponseEntity.ok(responseDto);
		
	}
	
	
	//이메일 인증 확인
	@Operation(summary = "이메일 인증 확인", description = "이메일 인증 확인")
	@PostMapping("/email/verify")
	public ResponseEntity<ResponseDto> verifyAuthEmail(HttpServletRequest request
            , HttpServletResponse response, @RequestBody @Valid EmailAuthRequest emailAuthReq) {
		
		ResponseDto responseDto = userService.verifyAuthEmail(emailAuthReq);
		return ResponseEntity.ok(responseDto);
		
		
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
	
	//프로필 수정
	@Operation(summary = "프로필 수정", description = "프로필 이미지, 닉네임")
	@PatchMapping("/profile")
	public ResponseEntity<ResponseDto> updateProfile(@ModelAttribute UserUpdateRequest updateReq) throws IOException {
		
		ResponseDto responseDto = userService.updateProfile(updateReq);
		return ResponseEntity.ok(responseDto);
		
    }
	
	//신체정보 수정
	@Operation(summary = "신체정보 수정", description = "생년월일, 성별, 키, 몸무게")
	@PatchMapping("/bodyInfo")
	public ResponseEntity<ResponseDto> updateBodyInfo(@RequestBody UserUpdateRequest updateReq) throws IOException {
		
		userService.updateBodyInfo(updateReq);
		return ResponseEntity.ok(new ResponseDto(true));
		
    }
	
	//닉네임 중복 확인
	@Operation(summary = "닉네임 중복 확인", description = "닉네임 중복 확인")
	@PostMapping("/nickname/check")
	public ResponseEntity<ResponseDto> checkNickname(@RequestBody @Valid NicknameRequest nicknameReq) {
		
		UserInfo userInfo = userUtil.getUserInfo();
		
		User user = userService.selectUserByNicknameAndNotUserId(nicknameReq.getNickname(), userInfo.getUserId());
		if(user == null) {
			return ResponseEntity.ok(new ResponseDto(true, "Nickname available"));
		}else {
			return ResponseEntity.ok(new ResponseDto(false, null, ResultCdEnum.S006.getValue()));
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
	
	//비밀번호 찾기
	@Operation(summary = "비밀번호 찾기", description = "임시 비밀번호 메일 발송")
	@PostMapping("/findPw")
	public ResponseEntity<ResponseDto> findPw(@Valid @RequestBody EmailRequest emailReq) {
		
		ResponseDto responseDto = userService.findPw(emailReq.getEmail());
		return ResponseEntity.ok(responseDto);
    }
	
	//비밀번호 변경
	@Operation(summary = "비밀번호 변경", description = "비밀번호 변경")
	@PatchMapping("/changePw")
	public ResponseEntity<ResponseDto> changePw(@Valid @RequestBody PwRequest pwReq) {
		
		ResponseDto responseDto = userService.changePw(pwReq);
		return ResponseEntity.ok(responseDto);
    }
	
	
	//FCM 토큰 등록
	@Operation(summary = "FCM Token 등록", description = "FCM Token 등록")
	@PostMapping("/fcmToken")
	public ResponseEntity<ResponseDto> insertFcmToken(@Valid @RequestBody FcmTokenInsertRequest insertReq) {
		
		userService.insertFcmToken(insertReq);
		return ResponseEntity.ok(new ResponseDto(true));
    }
	
	@Operation(summary = "FCM Token 갱신", description = "FCM Token 갱신")
	@PatchMapping("/fcmToken")
	public ResponseEntity<ResponseDto> updateFcmToken(@Valid @RequestBody FcmTokenUpdateRequest updateReq) {
		
		userService.updateFcmToken(updateReq);
		return ResponseEntity.ok(new ResponseDto(true));
    }
	
	@Operation(summary = "FCM Token 조회", description = "FCM Token 조회")
	@GetMapping("/fcmTokenList")
	public ResponseEntity<ResponseDto> getFcmTokenList() {
		
		List<Token> tokenList = userService.selectFcmTokenList();
		return ResponseEntity.ok(new ResponseDto(true, tokenList));
    }
	
}
