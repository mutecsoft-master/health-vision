package com.mutecsoft.healthvision.api.controller.api;

import java.io.IOException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mutecsoft.healthvision.api.service.MedicationService;
import com.mutecsoft.healthvision.api.util.UserUtil;
import com.mutecsoft.healthvision.common.dto.MedicationInfoDto.MedicationInfoResponse;
import com.mutecsoft.healthvision.common.dto.MedicationLogDto.MedicationLogResponse;
import com.mutecsoft.healthvision.common.dto.ResponseDto;
import com.mutecsoft.healthvision.common.dto.UserDto.UserInfo;
import com.mutecsoft.healthvision.common.model.MedicationInfo;
import com.mutecsoft.healthvision.common.model.MedicationLog;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Medication", description = "복약관리")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/medication")
public class MedicationController {
	
	private final MedicationService medicationService;
	private final UserUtil userUtil;

	@Operation(summary = "복약정보 등록", description = "복약정보 등록")
	@PostMapping("/info")
	public ResponseEntity<ResponseDto> insertMedicationInfo(@ModelAttribute MedicationInfo medInfo) throws IOException {
		
		medicationService.insertMedicationInfo(medInfo);
		
		return ResponseEntity.ok(new ResponseDto(true));
	}
	
	@Operation(summary = "복약기록 등록", description = "복약기록 등록")
	@PostMapping("/log")
	public ResponseEntity<ResponseDto> insertMedicationLog(@RequestBody MedicationLog medLog) {
		
		medicationService.insertMedicationLog(medLog);
		
		return ResponseEntity.ok(new ResponseDto(true));
	}
	
	@Operation(summary = "복약정보 목록 조회", description = "복약정보 목록 조회")
	@GetMapping("/info/list")
	public ResponseEntity<ResponseDto> selectMedicationInfoList() {
		
		UserInfo userInfo = userUtil.getUserInfo();
		List<MedicationInfoResponse> list = medicationService.selectMedicationInfoList(userInfo.getUserId());
		
		return ResponseEntity.ok(new ResponseDto(true, list));
	}
	
	@Operation(summary = "복약기록 목록 조회", description = "복약기록 목록 조회")
	@GetMapping("/log/list")
	public ResponseEntity<ResponseDto> selectMedicationLogList() {
		
		UserInfo userInfo = userUtil.getUserInfo();
		List<MedicationLogResponse> list = medicationService.selectMedicationLogList(userInfo.getUserId());
		
		return ResponseEntity.ok(new ResponseDto(true, list));
	}
	
	
	@Operation(summary = "복약정보 삭제", description = "복약정보 삭제")
	@DeleteMapping("/info/{medInfoId}")
	public ResponseEntity<ResponseDto> deleteMedicationInfo(@PathVariable("medInfoId") String medInfoId) {
		
		medicationService.deleteMedicationInfo(Long.parseLong(medInfoId));
		
		return ResponseEntity.ok(new ResponseDto(true));
	}
	
}
