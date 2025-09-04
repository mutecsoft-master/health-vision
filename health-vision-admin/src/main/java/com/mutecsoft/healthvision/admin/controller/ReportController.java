package com.mutecsoft.healthvision.admin.controller;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.mutecsoft.healthvision.admin.service.ReportService;
import com.mutecsoft.healthvision.common.dto.ResponseDto;
import com.mutecsoft.healthvision.common.dto.ReportV2Dto.ReportResponse;
import com.mutecsoft.healthvision.common.dto.ReportV2Dto.ReportUploadRequest;
import com.mutecsoft.healthvision.common.dto.admin.AdminReportDto.SearchReport;
import com.mutecsoft.healthvision.common.util.DatatableUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/report")
public class ReportController {

    private final ReportService reportService;
    private final DatatableUtil datatableUtil;

    //리포트관리
    @GetMapping("/reportList")
    public ModelAndView reportList(ModelAndView mav) {
    	
        mav.setViewName("report/reportList");
        
        return mav;
    }
    
    @GetMapping("/reportList/data")
    public Map<String, Object> getReportList(Pageable pageable,
    		@ModelAttribute SearchReport searchParam) {
    	
    	PageImpl<ReportResponse> reportListPage = reportService.selectReportListPage(searchParam, pageable);
    	Map<String, Object> resultMap = datatableUtil.makeDatatablePagingMap(reportListPage, searchParam.getDraw());
    	
        return resultMap;
    }
    
    
    @PostMapping("/upload")
    public ResponseEntity<ResponseDto> uploadReport(@ModelAttribute ReportUploadRequest uploadReq) throws IOException {
    	
    	reportService.uploadReport(uploadReq);
    	
    	return ResponseEntity.ok(new ResponseDto(true));
    }
    
    
    @GetMapping("/download")
    public void downloadReport(@RequestParam("reportFileId") Long reportFileId, HttpServletResponse response) throws IOException {
    	
    	reportService.downloadReport(reportFileId, response);
    	
    }
    
    @DeleteMapping("/delete")
    public void deleteReport(@RequestParam("reportId") Long reportId) throws IOException {
    	
    	reportService.deleteReport(reportId);
    	
    }
    
}
