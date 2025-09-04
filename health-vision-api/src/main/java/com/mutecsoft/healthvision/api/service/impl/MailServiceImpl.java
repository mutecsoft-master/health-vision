package com.mutecsoft.healthvision.api.service.impl;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.mutecsoft.healthvision.api.service.MailService;
import com.mutecsoft.healthvision.common.constant.MailTypeCdEnum;
import com.mutecsoft.healthvision.common.mapper.MailSendAttemptMapper;
import com.mutecsoft.healthvision.common.model.MailSendAttempt;
import com.mutecsoft.healthvision.common.util.RequestUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

//TODO[csm] 추후 메일 발송 이력 추가 검토

@Service
@RequiredArgsConstructor
@Slf4j
public class MailServiceImpl implements MailService {
	
	private final JavaMailSender javaMailSender;
	private final MailSendAttemptMapper mailSendAttemptMapper;
	
	@Override
	public void sendMail(String to, String subject, String content) throws MessagingException {
		MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(to);// 보내려는 대상의 mail 주소
        helper.setSubject(subject);// 보내는 mail의 제목
        helper.setText(content, true);// 보내는 mail의 내용 (html)

        javaMailSender.send(message);
	}

	
	//무분별한 메일 발송을 제한하기 위해 mailSendAttempt 관리 
	@Override
	public MailSendAttempt selectMailSendAttempt(String email, MailTypeCdEnum mailType) {
		MailSendAttempt mailSendAttempt = new MailSendAttempt();
    	mailSendAttempt.setEmail(email);
    	mailSendAttempt.setIp(RequestUtil.getRemoteIp());
    	mailSendAttempt.setMailTypeCd(mailType.getValue());
    	
    	return mailSendAttemptMapper.selectLastMailSendAttempt(mailSendAttempt);
	}

	@Override
	public void saveMailSendAttempt(String email, MailTypeCdEnum mailType) {
		MailSendAttempt mailSendAttempt = new MailSendAttempt();
    	mailSendAttempt.setEmail(email);
    	mailSendAttempt.setIp(RequestUtil.getRemoteIp());
    	mailSendAttempt.setMailTypeCd(mailType.getValue());
    	
    	MailSendAttempt lastMailSendAttempt = mailSendAttemptMapper.selectLastMailSendAttempt(mailSendAttempt);
    	
    	if(lastMailSendAttempt != null) {
    		mailSendAttemptMapper.updateReqCnt(lastMailSendAttempt);
    	}else {
    		mailSendAttemptMapper.insertMailSendAttempt(mailSendAttempt);
    	}
	}

	@Override
	public void initMailSendAttempt(String email, MailTypeCdEnum mailType) {
		MailSendAttempt mailSendAttempt = new MailSendAttempt();
    	mailSendAttempt.setEmail(email);
    	mailSendAttempt.setIp(RequestUtil.getRemoteIp());
    	mailSendAttempt.setMailTypeCd(mailType.getValue());
    	
    	MailSendAttempt lastMailSendAttempt = mailSendAttemptMapper.selectLastMailSendAttempt(mailSendAttempt);
    	if(lastMailSendAttempt != null) {
    		mailSendAttemptMapper.initMailSendAttempt(lastMailSendAttempt);
    	}
	}

	
	
	    
	    

   
    
    
}
