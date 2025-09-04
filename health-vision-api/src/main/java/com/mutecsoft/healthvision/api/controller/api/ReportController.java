package com.mutecsoft.healthvision.api.controller.api;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mutecsoft.healthvision.api.service.FileService;
import com.mutecsoft.healthvision.api.service.ReportService;
import com.mutecsoft.healthvision.common.constant.Const;
import com.mutecsoft.healthvision.common.dto.ResponseDto;
import com.mutecsoft.healthvision.common.dto.ReportDto.ReportApplyAppleRequest;
import com.mutecsoft.healthvision.common.dto.ReportDto.ReportApplyFreeRequest;
import com.mutecsoft.healthvision.common.dto.ReportDto.ReportApplyGoogleRequest;
import com.mutecsoft.healthvision.common.dto.ReportDto.ReportResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

//V1 : 리포트 신청시 결제하는 구조
@Tag(name = "Report", description = "리포트")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/report")
@Deprecated
public class ReportController {
	
	private final ReportService reportService;
	private final FileService fileService;

	@Operation(summary = "리포트 신청(구글)", description = "리포트 신청(구글)")
	@PostMapping("/google")
	public ResponseEntity<ResponseDto> applyReportForGoogle(@RequestBody @Valid ReportApplyGoogleRequest applyReq) {
		
		ResponseDto responseDto = reportService.applyReportForGoogle(applyReq);
		
		return ResponseEntity.ok(responseDto);
	}
	
	@Operation(summary = "리포트 신청(애플)", description = "리포트 신청(애플)")
	@PostMapping("/apple")
	public ResponseEntity<ResponseDto> applyReportForApple(@RequestBody @Valid ReportApplyAppleRequest applyReq) {
		
		ResponseDto responseDto = reportService.applyReportForApple(applyReq);
		
		return ResponseEntity.ok(responseDto);
	}
	
	@Operation(summary = "리포트 신청(무료) - 베타서비스용", description = "리포트 신청(무료) - 베타서비스용")
	@PostMapping("/free")
	public ResponseEntity<ResponseDto> applyReportForFree(@RequestBody @Valid ReportApplyFreeRequest applyReq) {
		
		ResponseDto responseDto = reportService.applyReportForFree(applyReq);
		
		return ResponseEntity.ok(responseDto);
	}
	
	@Operation(summary = "리포트 목록 조회", description = "리포트 목록 조회(최근)")
	@GetMapping("/list/recent")
	public ResponseEntity<ResponseDto> getReportListRecent() {
		
		List<ReportResponse> reportList = reportService.getReportList(Const.RECENT_REPORT_DURATION_IN_MONTHS);
		
		return ResponseEntity.ok(new ResponseDto(true, reportList));
	}
	
	
	@Operation(summary = "리포트 목록 조회", description = "리포트 목록 조회(전체)")
	@GetMapping("/list")
	public ResponseEntity<ResponseDto> getReportList() {
		
		List<ReportResponse> reportList = reportService.getReportList(null);
		
		return ResponseEntity.ok(new ResponseDto(true, reportList));
	}
	
	//해당 URL이 application.yml에 정의되어 있어야 햠
	@Operation(summary = "리포트 출력", description = "application/octet-stream 응답")
	@GetMapping("/file/{reportFileId}")
    public ResponseEntity<?> getReport(
    		@PathVariable("reportFileId") Long fileId
    		, HttpServletRequest request
            , HttpServletResponse response
    		) {
		
		return fileService.getFile(fileId, response);

    }
	
}
