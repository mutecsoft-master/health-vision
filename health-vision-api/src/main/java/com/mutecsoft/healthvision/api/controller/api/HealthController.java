package com.mutecsoft.healthvision.api.controller.api;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mutecsoft.healthvision.api.service.HealthService;
import com.mutecsoft.healthvision.common.dto.ResponseDto;
import com.mutecsoft.healthvision.common.dto.HealthDto.HealthInfoRequest;
import com.mutecsoft.healthvision.common.dto.HealthDto.HealthInfoResponse;
import com.mutecsoft.healthvision.common.dto.HealthDto.HealthSaveDto;
import com.mutecsoft.healthvision.common.model.FinalUpload;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Health", description = "건강정보")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/health")
public class HealthController {
	
	private final HealthService healthService;

	@Operation(summary = "건강정보 업로드(파일)", description = "Json 파일 업로드")
	@PostMapping
	public ResponseEntity<ResponseDto> saveHealth(@Valid HealthSaveDto healthDto
			, HttpServletRequest request
			, HttpServletResponse response) {
		
		healthService.saveHealth(healthDto);
		
		return ResponseEntity.ok(new ResponseDto(true));
	}
	
	@Operation(summary = "건강정보 마지막 업로드 조회", description = "중복 업로드 방지 목적")
	@GetMapping("/finalUpload")
	public ResponseEntity<ResponseDto> getFinalUpload() {
		
		FinalUpload finalUpload = healthService.getFinalUpload();
		
		return ResponseEntity.ok(new ResponseDto(true, finalUpload));
	}
	
	@Operation(summary = "건강정보 유형별 수집 데이터량 조회", description = "리포트 신청시 건강정보 유형별 수집 데이터량(%) 조회")
	@GetMapping("/info")
	public ResponseEntity<ResponseDto> getHealthInfo(HealthInfoRequest healthInfoReq) {

		HealthInfoResponse healthInfo = healthService.getHealthInfo(healthInfoReq);
		
		return ResponseEntity.ok(new ResponseDto(true, healthInfo));
	}
}
