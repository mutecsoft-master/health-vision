package com.mutecsoft.healthvision.common.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.domain.Pageable;

import com.mutecsoft.healthvision.common.dto.admin.AdminTermsDto.RegisterTerms;
import com.mutecsoft.healthvision.common.dto.admin.AdminTermsDto.SearchTerms;
import com.mutecsoft.healthvision.common.dto.admin.AdminTermsDto.TermsResponse;
import com.mutecsoft.healthvision.common.dto.admin.AdminTermsDto.UpdateTerms;

@Mapper
public interface ATermsMapper {

	List<TermsResponse> selectTermsList(SearchTerms searchParam, Pageable pageable);
	int selectTermsListCnt(SearchTerms searchParam, Pageable pageable);
	void registerTerms(RegisterTerms registerTerms);
	void updateTerms(UpdateTerms updateTerms);
	void deleteNotices(List<Long> termsIdList);

	
}
