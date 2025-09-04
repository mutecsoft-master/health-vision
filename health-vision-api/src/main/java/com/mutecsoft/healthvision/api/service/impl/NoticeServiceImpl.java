package com.mutecsoft.healthvision.api.service.impl;

import java.util.List;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import com.mutecsoft.healthvision.api.service.NoticeService;
import com.mutecsoft.healthvision.common.dto.NoticeDto.NoticeSearchRequest;
import com.mutecsoft.healthvision.common.mapper.NoticeMapper;
import com.mutecsoft.healthvision.common.model.Notice;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class NoticeServiceImpl implements NoticeService {
	
	private final NoticeMapper noticeMapper;
	
	@Override
	public List<Notice> selectNoticeList(int size, Long lastNoticeId) {
		String lang = LocaleContextHolder.getLocale().getLanguage();
		NoticeSearchRequest searchReq = new NoticeSearchRequest(lastNoticeId, size, lang);
		
		List<Notice> noticeList = noticeMapper.selectNoticeList(searchReq);
		return noticeList;
	}

    

}
