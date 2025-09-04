package com.mutecsoft.healthvision.api.service;

import javax.mail.MessagingException;

import com.mutecsoft.healthvision.common.constant.MailTypeCdEnum;
import com.mutecsoft.healthvision.common.model.MailSendAttempt;

public interface MailService {

	void sendMail(String to, String subject, String content) throws MessagingException;
	
	MailSendAttempt selectMailSendAttempt(String email, MailTypeCdEnum mailType);
	void saveMailSendAttempt(String email, MailTypeCdEnum mailType);
	void initMailSendAttempt(String email, MailTypeCdEnum mailType);
	
}
