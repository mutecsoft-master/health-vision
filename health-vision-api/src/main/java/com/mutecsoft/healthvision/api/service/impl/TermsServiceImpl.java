package com.mutecsoft.healthvision.api.service.impl;

import java.util.List;
import java.util.Locale;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import com.mutecsoft.healthvision.api.service.TermsService;
import com.mutecsoft.healthvision.common.mapper.TermsMapper;
import com.mutecsoft.healthvision.common.model.Terms;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TermsServiceImpl implements TermsService {
	
	private final TermsMapper termsMapper;
	
	@Override
	public List<Terms> selectLastVerTemrsList(String lang) {
		
		List<Terms> termsList = termsMapper.selectLastVerTemrsListByLang(lang);
		
		return termsList;
	}

    

}
