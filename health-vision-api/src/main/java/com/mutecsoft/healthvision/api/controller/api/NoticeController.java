package com.mutecsoft.healthvision.api.controller.api;

import java.util.List;

import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mutecsoft.healthvision.api.service.NoticeService;
import com.mutecsoft.healthvision.common.dto.ResponseDto;
import com.mutecsoft.healthvision.common.model.Notice;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;


@Tag(name = "Notice", description = "공지사항")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notice")
public class NoticeController {
	
	private final NoticeService noticeService;
	
	//공지사항 목록 조회(커서 기반 페이징)
	@Operation(summary = "공지사항 목록 조회", description = "공지사항 목록 조회(커서 기반 페이징)")
	@GetMapping("/list")
	public ResponseEntity<ResponseDto> getNoticeList(
			@RequestParam(required = false, defaultValue = "20") int size,
			@RequestParam(required = false) Long lastNoticeId,
			@Parameter(name = "Accept-Language", 
				description = "언어 코드 (예: ko, en)", 
				in = ParameterIn.HEADER,
			    required = false,
			    example = "ko"
		    )
		    @RequestHeader(value = "Accept-Language", required = false) String acceptLanguage) {
		
		List<Notice> noticeList = noticeService.selectNoticeList(size, lastNoticeId);
		
		return ResponseEntity.ok(new ResponseDto(true, noticeList));
	}
	
}
