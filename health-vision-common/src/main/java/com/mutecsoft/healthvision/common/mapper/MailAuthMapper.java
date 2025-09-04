package com.mutecsoft.healthvision.common.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.mutecsoft.healthvision.common.model.MailAuth;

@Mapper
public interface MailAuthMapper {

	void insertMailAuth(MailAuth mailAuth);
	
	MailAuth selectMailAuthByEmail(String email);

	void verifyMailAuth(MailAuth mailAuth);

	
	
}
