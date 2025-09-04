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
import com.mutecsoft.healthvision.api.service.ReportV2Service;
import com.mutecsoft.healthvision.common.constant.Const;
import com.mutecsoft.healthvision.common.constant.ReportDownloadTypeEnum;
import com.mutecsoft.healthvision.common.dto.ResponseDto;
import com.mutecsoft.healthvision.common.dto.ReportV2Dto.PurchaseReportAppleRequest;
import com.mutecsoft.healthvision.common.dto.ReportV2Dto.PurchaseReportGoogleRequest;
import com.mutecsoft.healthvision.common.dto.ReportV2Dto.ReportApplyRequest;
import com.mutecsoft.healthvision.common.dto.ReportV2Dto.ReportResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

//V2 : 리포트 신청 -> 리포트 등록(관리자) -> preview 제공 -> 사용자가 다운로드 원할 시 결제 -> 다운로드 가능
@Tag(name = "Report V2", description = "리포트 V2")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/report/v2")
public class ReportV2Controller {
	
	private final ReportV2Service reportService;
	private final FileService fileService;
	
	
	@Operation(summary = "리포트 신청", description = "리포트 신청")
	@PostMapping("/apply")
	public ResponseEntity<ResponseDto> applyReport(@RequestBody @Valid ReportApplyRequest applyReq) {
		
		ResponseDto responseDto = reportService.applyReport(applyReq);
		
		return ResponseEntity.ok(responseDto);
	}
	
	@Operation(summary = "리포트 결제(구글)", description = "리포트 결제(구글)")
	@PostMapping("/purchase/google")
	public ResponseEntity<ResponseDto> purchaseReportForGoogle(@RequestBody @Valid PurchaseReportGoogleRequest purchaseReq) {
		
		ResponseDto responseDto = reportService.purchaseReportForGoogle(purchaseReq);
		
		return ResponseEntity.ok(responseDto);
	}
	
	@Operation(summary = "리포트 결제(애플)", description = "리포트 결제(애플)")
	@PostMapping("/purchase/apple")
	public ResponseEntity<ResponseDto> purchaseReportForApple(@RequestBody @Valid PurchaseReportAppleRequest purchaseReq) {
		
		ResponseDto responseDto = reportService.purchaseReportForApple(purchaseReq);
		
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
	@Operation(summary = "리포트 출력(preview)", description = "application/octet-stream 응답")
	@GetMapping("/preview/{reportFileId}")
    public ResponseEntity<?> getReportForPreview(
    		@PathVariable("reportFileId") Long fileId
    		, HttpServletRequest request
            , HttpServletResponse response
    		) {
		
		return fileService.getReport(fileId, ReportDownloadTypeEnum.PREVIEW, response);

    }
	
	@Operation(summary = "리포트 출력(download)", description = "application/octet-stream 응답")
	@GetMapping("/download/{reportFileId}")
    public ResponseEntity<?> getReportForDownload(
    		@PathVariable("reportFileId") Long fileId
    		, HttpServletRequest request
            , HttpServletResponse response
    		) {
		
		return fileService.getReport(fileId, ReportDownloadTypeEnum.DOWNLOAD, response);

    }
	
}
