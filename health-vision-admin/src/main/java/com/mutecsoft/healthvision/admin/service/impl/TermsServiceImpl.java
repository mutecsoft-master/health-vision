package com.mutecsoft.healthvision.admin.service.impl;

import java.util.List;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mutecsoft.healthvision.admin.service.TermsService;
import com.mutecsoft.healthvision.admin.util.UserUtil;
import com.mutecsoft.healthvision.common.constant.Const;
import com.mutecsoft.healthvision.common.dto.UserDto.UserInfo;
import com.mutecsoft.healthvision.common.dto.admin.AdminTermsDto.RegisterTerms;
import com.mutecsoft.healthvision.common.dto.admin.AdminTermsDto.SearchTerms;
import com.mutecsoft.healthvision.common.dto.admin.AdminTermsDto.TermsResponse;
import com.mutecsoft.healthvision.common.dto.admin.AdminTermsDto.UpdateTerms;
import com.mutecsoft.healthvision.common.mapper.ATermsMapper;
import com.mutecsoft.healthvision.common.util.CommonUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class TermsServiceImpl implements TermsService {
	
	private final UserUtil userUtil;
	private final ATermsMapper aTermsMapper;
	
	
	@Override
	public PageImpl<TermsResponse> selectTermsListPage(SearchTerms searchParam, Pageable pageable) {
		List<TermsResponse> termsList = aTermsMapper.selectTermsList(searchParam, pageable);
        int totalCnt = aTermsMapper.selectTermsListCnt(searchParam, pageable);
        
        for(TermsResponse terms : termsList) {
        	String plainText = CommonUtil.htmlToPlainText(terms.getContent());
        	plainText = CommonUtil.truncateWithEllipsis(plainText, Const.TERMS_DISPLAY_MAX_LENGTH);
        	terms.setPlainContent(plainText);
        }
        
        return new PageImpl<>(termsList, pageable, totalCnt);
	}


	@Override
	public void registerTerms(RegisterTerms registerTerms) {
		UserInfo userInfo = userUtil.getUserInfo();
		registerTerms.setRegId(userInfo.getUserId());
		
		aTermsMapper.registerTerms(registerTerms);
	}


	@Override
	public void updateTerms(UpdateTerms updateTerms) {
		UserInfo userInfo = userUtil.getUserInfo();
		updateTerms.setUpdId(userInfo.getUserId());
		
		aTermsMapper.updateTerms(updateTerms);
		
	}


	@Override
	public void deleteTermsList(List<Long> termsIdList) {
		aTermsMapper.deleteNotices(termsIdList);
	}
	
}
