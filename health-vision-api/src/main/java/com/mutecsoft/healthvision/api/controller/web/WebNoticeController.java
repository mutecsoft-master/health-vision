package com.mutecsoft.healthvision.api.controller.web;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mutecsoft.healthvision.api.service.NoticeService;
import com.mutecsoft.healthvision.common.model.Notice;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/web/notice")
public class WebNoticeController {
	
	private final NoticeService noticeService;
	
	//공지사항 목록 조회(커서 기반 페이징)
	@GetMapping("/list")
	public String getNoticeList(
			Model model,
			@RequestParam(required = false, defaultValue = "20") int size,
			@RequestParam(required = false) Long lastNoticeId) {
		
		List<Notice> noticeList = noticeService.selectNoticeList(size, lastNoticeId);
		
		model.addAttribute("noticeList", noticeList);
		
		if (lastNoticeId != null) {
	        return "web/notice/list :: noticeListFragment";
	    }
		
		return "web/notice/list";
	}
	
}
