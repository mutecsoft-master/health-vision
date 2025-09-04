package com.mutecsoft.healthvision.common.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.mutecsoft.healthvision.common.model.TermsAgree;

@Mapper
public interface TermsAgreeMapper {

	void insertTermsAgree(TermsAgree termsAgree);

	
}
