package com.mutecsoft.healthvision.admin.service;

import java.util.List;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.mutecsoft.healthvision.common.dto.admin.AdminTermsDto.RegisterTerms;
import com.mutecsoft.healthvision.common.dto.admin.AdminTermsDto.SearchTerms;
import com.mutecsoft.healthvision.common.dto.admin.AdminTermsDto.TermsResponse;
import com.mutecsoft.healthvision.common.dto.admin.AdminTermsDto.UpdateTerms;

public interface TermsService {

	PageImpl<TermsResponse> selectTermsListPage(SearchTerms searchParam, Pageable pageable);

	void registerTerms(RegisterTerms registerTerms);

	void updateTerms(UpdateTerms updateTerms);

	void deleteTermsList(List<Long> termsIdList);
	
}
