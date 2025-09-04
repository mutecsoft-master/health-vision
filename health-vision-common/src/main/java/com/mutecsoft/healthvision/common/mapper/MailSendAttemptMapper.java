package com.mutecsoft.healthvision.common.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.mutecsoft.healthvision.common.model.MailSendAttempt;

@Mapper
public interface MailSendAttemptMapper {
	
	void initMailSendAttempt(MailSendAttempt mailSendAttempt);
	MailSendAttempt selectLastMailSendAttempt(MailSendAttempt mailSendAttempt);
    void insertMailSendAttempt(MailSendAttempt mailSendAttempt);
	void updateReqCnt(MailSendAttempt mailSendAttempt);
	

	
}
