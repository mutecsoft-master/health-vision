package com.mutecsoft.healthvision.api.service.impl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.mutecsoft.healthvision.api.service.FileService;
import com.mutecsoft.healthvision.api.service.MailService;
import com.mutecsoft.healthvision.api.service.TermsService;
import com.mutecsoft.healthvision.api.service.TokenService;
import com.mutecsoft.healthvision.api.service.UserService;
import com.mutecsoft.healthvision.api.util.JwtTokenUtil;
import com.mutecsoft.healthvision.api.util.UserUtil;
import com.mutecsoft.healthvision.common.constant.Const;
import com.mutecsoft.healthvision.common.constant.FileCateCdEnum;
import com.mutecsoft.healthvision.common.constant.MailTypeCdEnum;
import com.mutecsoft.healthvision.common.constant.MultiTokenTypeEnum;
import com.mutecsoft.healthvision.common.constant.ResultCdEnum;
import com.mutecsoft.healthvision.common.dto.ResponseDto;
import com.mutecsoft.healthvision.common.dto.FileDto.FileInsertDto;
import com.mutecsoft.healthvision.common.dto.TokenDto.FcmTokenInsertRequest;
import com.mutecsoft.healthvision.common.dto.TokenDto.FcmTokenUpdateRequest;
import com.mutecsoft.healthvision.common.dto.UserDto.EmailAuthRequest;
import com.mutecsoft.healthvision.common.dto.UserDto.PwRequest;
import com.mutecsoft.healthvision.common.dto.UserDto.SignupRequest;
import com.mutecsoft.healthvision.common.dto.UserDto.UserInfo;
import com.mutecsoft.healthvision.common.dto.UserDto.UserUpdateRequest;
import com.mutecsoft.healthvision.common.exception.CustomException;
import com.mutecsoft.healthvision.common.mapper.MailAuthMapper;
import com.mutecsoft.healthvision.common.mapper.RoleMapper;
import com.mutecsoft.healthvision.common.mapper.TermsAgreeMapper;
import com.mutecsoft.healthvision.common.mapper.UserMapper;
import com.mutecsoft.healthvision.common.mapper.UserRoleMapper;
import com.mutecsoft.healthvision.common.model.FileModel;
import com.mutecsoft.healthvision.common.model.MailAuth;
import com.mutecsoft.healthvision.common.model.MailSendAttempt;
import com.mutecsoft.healthvision.common.model.Role;
import com.mutecsoft.healthvision.common.model.TermsAgree;
import com.mutecsoft.healthvision.common.model.Token;
import com.mutecsoft.healthvision.common.model.User;
import com.mutecsoft.healthvision.common.model.UserRole;
import com.mutecsoft.healthvision.common.util.CommonUtil;
import com.mutecsoft.healthvision.common.util.FileUtil;
import com.mutecsoft.healthvision.common.util.MessageUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final TokenService tokenService;
    private final UserMapper userMapper;
    private final RoleMapper roleMapper;
    private final UserRoleMapper userRoleMapper;
    private final MailAuthMapper mailAuthMapper;
    private final FileService fileService;
    private final TermsAgreeMapper termsAgreeMapper;
    private final TermsService termsService;
    
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final MailService mailService;
    private final MessageUtil messageUtil;
    private final CommonUtil commonUtil;
    private final UserUtil userUtil;
    private final FileUtil fileUtil;

    @Value("${jwt.password-expire-minute}")
    private String passwordExpireMinute;

    @Override
    public User selectUserByEmail(String email) {
    	User user = userMapper.selectUserByEmail(email);
    	if(user != null) {
	    	List<Role> roleList = roleMapper.selectRoleListByUserId(user.getUserId());
	    	user.setRoleNmList(roleList.stream().map(r -> r.getRoleNm()).collect(Collectors.toList()));
    	}
    	
        return user;
    }

    @Override
    public User selectUserByUserId(Long userId) {
    	User user = userMapper.selectUserByUserId(userId);
    	if(user != null) {
	    	List<Role> roleList = roleMapper.selectRoleListByUserId(user.getUserId());
	    	user.setRoleNmList(roleList.stream().map(r -> r.getRoleNm()).collect(Collectors.toList()));
    	}
    	
        return user;
    }

    

    @Transactional
    @Override
    public void insertUser(User user) {
        userMapper.insertUser(user);

        Role role = roleMapper.selectRoleByRoleNm(Const.ROLE_USER);

        if (role == null) {
            return;
        }

        Long roleId = role.getRoleId();
        Long userId = user.getUserId();

        UserRole userRole = new UserRole();
        userRole.setRoleId(roleId);
        userRole.setUserId(userId);

        userRoleMapper.insertUserRole(userRole);
    }
    
    @Override
    public void updateLastLoginDt(Long userId) {
        userMapper.updateLastLoginDt(userId);
    }

    
    @Override
	public ResponseDto sendAuthEmail(String email) {
    	//가입된 계정인지 확인
    	User user = selectUserByEmail(email);
		if(user != null) {
			return new ResponseDto(false, null, ResultCdEnum.S001.getValue());
		}
    	
		//메일 발송 시도 확인
		MailSendAttempt mailSendAttempt = mailService.selectMailSendAttempt(email, MailTypeCdEnum.AUTH);
        if(mailSendAttempt != null && mailSendAttempt.getReqCnt() >= MailTypeCdEnum.AUTH.getLimitReqCnt()) {
        	//2시간 이내에 재시도 불가
        	if(!mailSendAttempt.getLastAttemptDt().isBefore(LocalDateTime.now().minusHours(MailTypeCdEnum.AUTH.getWaitHours()))) {
        		return new ResponseDto(false, null, ResultCdEnum.L005.getValue());
        	}else {
        		//2시간 지났으면 초기화
        		mailService.initMailSendAttempt(email, MailTypeCdEnum.AUTH);
        	}
        }
        
        mailService.saveMailSendAttempt(email, MailTypeCdEnum.AUTH);
		
    	//인증번호 발송
    	String code = commonUtil.generateNumberCode(Const.MAIL_AUTH_CODE_LENGTH);
    	String subject = messageUtil.getMessage("auth.mail.subject");
        String content = messageUtil.getMessage("auth.mail.content", code);

        try {
            mailService.sendMail(email, subject, content);
            
            //발송 결과 저장
            MailAuth mailAuth = new MailAuth();
            mailAuth.setEmail(email);
            mailAuth.setCode(code);
            mailAuth.setCodeExpiredDt(commonUtil.calcExpiryTime(Const.MAIL_AUTH_CODE_EXPIRED_TIME));
            mailAuthMapper.insertMailAuth(mailAuth);
            
            return new ResponseDto(true);
        } catch (MessagingException e) {
            log.error("## Error : Mail 발송 실패 {}", e.getMessage());
            throw new CustomException(ResultCdEnum.E005.getValue());
        }
    	
	}
    
    @Transactional
    @Override
	public ResponseDto verifyAuthEmail(EmailAuthRequest emailAuthReq) {
    	//가입된 계정인지 확인
    	User user = selectUserByEmail(emailAuthReq.getEmail());
		if(user != null) {
			return new ResponseDto(false, null, ResultCdEnum.S001.getValue());
		}
    	
    	//인증번호 확인
    	MailAuth mailAuth = mailAuthMapper.selectMailAuthByEmail(emailAuthReq.getEmail());
    	if(mailAuth != null) {
    		//만료여부 확인
    		if(!commonUtil.isExpired(mailAuth.getCodeExpiredDt())) {
    			//일치여부 확인
    			if(mailAuth.getCode().equals(emailAuthReq.getCode())) {
    	            mailAuth.setVerifiedYn("Y");
    	            mailAuth.setSignupExpiredDt(commonUtil.calcExpiryTime(Const.MAIL_AUTH_SIGNUP_EXPIRED_TIME));
    				mailAuthMapper.verifyMailAuth(mailAuth);
    				return new ResponseDto(true);
    			}else {
    				return new ResponseDto(false, null, ResultCdEnum.S002.getValue());
    			}
    		}else {
				return new ResponseDto(false, null, ResultCdEnum.S003.getValue());
			}
    	}else {
    		return new ResponseDto(false, null, ResultCdEnum.S002.getValue());
    	}
	}
    
    @Transactional
    @Override
	public ResponseDto signup(SignupRequest signupReq) {

    	//가입된 계정인지 확인
    	User user = selectUserByEmail(signupReq.getEmail());
		if(user != null) {
			return new ResponseDto(false, null, ResultCdEnum.S001.getValue());
		}
    	
		//인증여부 확인
    	MailAuth mailAuth = mailAuthMapper.selectMailAuthByEmail(signupReq.getEmail());
    	if(mailAuth != null && mailAuth.getVerifiedYn().equals("Y")) {
    		//만료여부 확인
    		if(!commonUtil.isExpired(mailAuth.getSignupExpiredDt())) {
    			//회원가입
    			user = new User();
    		    user.setEmail(signupReq.getEmail());
    	        user.setUserPw(passwordEncoder.encode(signupReq.getUserPw()));
    		    insertUser(user);
    		    
    		    //약관 동의
    		    for(Long termsId : signupReq.getAgreeTermsIdList()) {
    		    	TermsAgree agree = new TermsAgree();
    		    	agree.setTermsId(termsId);
    		    	agree.setUserId(user.getUserId());
    		    	termsAgreeMapper.insertTermsAgree(agree);
    		    }
	            
				return new ResponseDto(true);
    		}else {
				return new ResponseDto(false, null, ResultCdEnum.S004.getValue());
			}
    	}else {
    		throw new CustomException(ResultCdEnum.E007.getValue());
    	}
		
	}

    @Transactional
    @Override
	public ResponseDto updateProfile(UserUpdateRequest updateReq) throws IOException {
    	
    	User user = userUtil.getUserFromUserInfo();
    	
    	if(StringUtils.hasText(updateReq.getNickname())) {
			user.setNickname(updateReq.getNickname());
		}else {
			user.setNickname(generateNickname());
		}
    	
    	if(updateReq.getProfileFile() != null 
				&& !updateReq.getProfileFile().isEmpty() 
				&& StringUtils.hasText(updateReq.getProfileFile().getOriginalFilename())) {
		
	    	//기존 프로필 삭제
			if(user.getProfileFileId() != null) {
				fileService.deleteFile(user.getProfileFileId());
			}
			
			//1. 파일 저장
			FileInsertDto fileDto = new FileInsertDto(0, updateReq.getProfileFile(), FileCateCdEnum.PROFILE.getValue());
			FileModel fileModel = fileUtil.saveFile(fileDto);
	
			//2. 파일데이터 DB 저장
			if(StringUtils.hasText(fileModel.getFilePath())) {
				fileService.insertFile(fileModel);
				user.setProfileFileId(fileModel.getFileId());
			}
			
    	}else {
    		user.setProfileFileId(user.getProfileFileId());
    	}
		
		try {
			userMapper.updateProfile(user);
		} catch (DuplicateKeyException e) {
			log.error("## nickname duplicated");
			throw new CustomException(ResultCdEnum.E001.getValue());
		}
		 
		return new ResponseDto(true);
    	
	}
    
	private String generateNickname() {
    	
		StringBuilder sb = new StringBuilder();
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String formattedDate = LocalDateTime.now().format(formatter);
        
        String nicknamePrefix = sb.append(Const.DEFAULT_NICKNAME_PREFIX)
        .append(formattedDate)
        .append("*")
        .toString();
        
        Integer maxIndex = userMapper.selectNextNicknameIndex(nicknamePrefix);
        if(maxIndex == null) {
        	maxIndex = 1;
        }else {
        	maxIndex++;
        }
        
        return nicknamePrefix + String.format("%04d", maxIndex);
    	
	}

	@Override
	public void updateBodyInfo(UserUpdateRequest updateReq) {
    	User user = userUtil.getUserFromUserInfo();
		user.setBirthDate(updateReq.getBirthDate());
		user.setGender(updateReq.getGender());
		user.setHeight(updateReq.getHeight());
		user.setWeight(updateReq.getWeight());
		userMapper.updateBodyInfo(user);
    	
	}
    
    
    @Override
    public void deleteUser() {
    	UserInfo userInfo = userUtil.getUserInfo();
    	userMapper.deleteUser(userInfo.getUserId(), CommonUtil.makeDeleteUserSuffix());
    }
    
    @Transactional
    @Override
	public ResponseDto findPw(String email) {
    	//가입된 계정인지 확인
    	User user = selectUserByEmail(email);
		if(user == null) {
			return new ResponseDto(false, null, ResultCdEnum.S005.getValue());
		}
		
		//메일 발송 시도 확인
		MailSendAttempt mailSendAttempt = mailService.selectMailSendAttempt(email, MailTypeCdEnum.FIND_PW);
        if(mailSendAttempt != null && mailSendAttempt.getReqCnt() >= MailTypeCdEnum.FIND_PW.getLimitReqCnt()) {
        	//2시간 이내에 재시도 불가
        	if(!mailSendAttempt.getLastAttemptDt().isBefore(LocalDateTime.now().minusHours(MailTypeCdEnum.FIND_PW.getWaitHours()))) {
        		return new ResponseDto(false, null, ResultCdEnum.L004.getValue());
        	}else {
        		//2시간 지났으면 초기화
        		mailService.initMailSendAttempt(email, MailTypeCdEnum.FIND_PW);
        	}
        }
        
        mailService.saveMailSendAttempt(email, MailTypeCdEnum.FIND_PW);
		
		//임시 비밀번호 발송
    	String tempPw = commonUtil.generateStringCode(Const.MAIL_FIND_PW_CODE_LENGTH);
    	String subject = messageUtil.getMessage("find.pw.mail.subject");
        String content = messageUtil.getMessage("find.pw.mail.content", tempPw);
        
        try {
            mailService.sendMail(email, subject, content);
            
            //회원정보 업데이트
            user.setTempPw(passwordEncoder.encode(tempPw));
            user.setTempPwExpireDt(commonUtil.calcExpiryTime(Const.TEMP_PW_EXPIRED_TIME));
            userMapper.updateTempPw(user);
            
            return new ResponseDto(true);
        } catch (MessagingException e) {
            log.error("## Error : Mail 발송 실패 {}", e.getMessage());
            throw new CustomException(ResultCdEnum.E005.getValue());
        }
        
	}
    
    
    @Override
	public User selectUserByNicknameAndNotUserId(String nickname, Long userId) {
    	return  userMapper.selectUserByNicknameAndNotUserId(nickname, userId);
	}
    
    @Override
	public ResponseDto changePw(PwRequest pwReq) {
    	UserInfo userInfo = userUtil.getUserInfo();
    	User user = userMapper.selectUserByUserId(userInfo.getUserId());
    	
    	boolean isNormalPwValid = passwordEncoder.matches(pwReq.getCurrentPw(), user.getUserPw());
        boolean isTempPwValid = passwordEncoder.matches(pwReq.getCurrentPw(), user.getTempPw()) 
                                && user.getTempPwExpireDt().isAfter(LocalDateTime.now());
        
		if(isNormalPwValid || isTempPwValid) {
			user.setUserPw(passwordEncoder.encode(pwReq.getNewPw()));
			userMapper.changePw(user);
			return new ResponseDto(true);
		}else {
			return new ResponseDto(false, null, ResultCdEnum.L001.getValue());
		}
	}
    
    @Override
	public void insertFcmToken(FcmTokenInsertRequest insertReq) {
		UserInfo userInfo = userUtil.getUserInfo();
		
    	Token tokenModel = new Token();
        tokenModel.setUserId(userInfo.getUserId());
        tokenModel.setTokenType(MultiTokenTypeEnum.FCM.getValue());
        tokenModel.setToken(insertReq.getFcmToken());
        tokenService.insertToken(tokenModel);
	}

	@Override
	public void updateFcmToken(FcmTokenUpdateRequest updateReq) {
		UserInfo userInfo = userUtil.getUserInfo();
		
		Token tokenModel = tokenService.selectTokenByToken(userInfo.getUserId(), MultiTokenTypeEnum.FCM.getValue(), updateReq.getOldFcmToken());
		if(tokenModel != null) {
			tokenModel.setToken(updateReq.getNewFcmToken());
			tokenService.updateToken(tokenModel);
		}else {
			tokenModel = new Token();
	        tokenModel.setUserId(userInfo.getUserId());
	        tokenModel.setTokenType(MultiTokenTypeEnum.FCM.getValue());
	        tokenModel.setToken(updateReq.getNewFcmToken());
	        tokenService.insertToken(tokenModel);
		}
		
	}

	@Override
	public List<Token> selectFcmTokenList() {
		UserInfo userInfo = userUtil.getUserInfo();
		
		return tokenService.selectTokenList(userInfo.getUserId(), MultiTokenTypeEnum.FCM);
	}
    

}
