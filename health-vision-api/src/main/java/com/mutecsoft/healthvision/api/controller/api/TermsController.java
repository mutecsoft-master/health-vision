package com.mutecsoft.healthvision.api.controller.api;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mutecsoft.healthvision.api.service.TermsService;
import com.mutecsoft.healthvision.common.dto.ResponseDto;
import com.mutecsoft.healthvision.common.model.Terms;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;


@Tag(name = "Terms", description = "약관")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/terms")
public class TermsController {
	
	private final TermsService termsService;
	
	//약관 목록 조회
	@Operation(summary = "약관 목록 조회", description = "약관 목록 조회")
	@GetMapping("/list")
	public ResponseEntity<ResponseDto> getTermsList(@RequestParam("lang") String lang) {
		
		List<Terms> termsList = termsService.selectLastVerTemrsList(lang);
		
		return ResponseEntity.ok(new ResponseDto(true, termsList));
	}
	
}
