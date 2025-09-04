package com.mutecsoft.healthvision.api.controller.api;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mutecsoft.healthvision.api.service.BgFileService;
import com.mutecsoft.healthvision.common.constant.Const;
import com.mutecsoft.healthvision.common.dto.ResponseDto;
import com.mutecsoft.healthvision.common.dto.web.BgFileDto.BgFileResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Blood Glucose File", description = "혈당 파일")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bg")
public class BgFileController {
	
	private final BgFileService bgFileService;

	@Operation(summary = "혈당기록 조회", description = "혈당기록 조회(최근)")
	@GetMapping("/bgFileList/recent")
	public ResponseEntity<ResponseDto> getBgFileListRecent() {
		
		List<BgFileResponse> bgFileList = bgFileService.getBgFileListForApi(Const.RECENT_BG_FILE_LIST_CNT);
		
		return ResponseEntity.ok(new ResponseDto(true, bgFileList));
	}
	
	
	@Operation(summary = "혈당기록 조회", description = "혈당기록 조회(전체)")
	@GetMapping("/bgFileList")
	public ResponseEntity<ResponseDto> getBgFileList() {
		
		List<BgFileResponse> bgFileList = bgFileService.getBgFileListForApi(null);
		
		return ResponseEntity.ok(new ResponseDto(true, bgFileList));
	}
	
}
