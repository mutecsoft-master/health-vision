package com.mutecsoft.healthvision.api.controller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mutecsoft.healthvision.api.util.UserUtil;
import com.mutecsoft.healthvision.common.dto.ResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Medication", description = "복약관리")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/medication")
public class MedicationController {
	
	private final UserUtil userUtil;

	//TODO[csm]
	@Operation(summary = "약물정보 등록", description = "약물정보 등록")
	@PostMapping
	public ResponseEntity<ResponseDto> insertMedication() {
		
		return ResponseEntity.ok(new ResponseDto(true));
	}
	
	
	
	@Operation(summary = "복약 등록", description = "복약 등록")
	@PostMapping("/log")
	public ResponseEntity<ResponseDto> insertMedicationLog() {
		
		return ResponseEntity.ok(new ResponseDto(true));
	}
	
}
