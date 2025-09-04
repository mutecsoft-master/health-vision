package com.mutecsoft.healthvision.admin.service;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.mutecsoft.healthvision.common.dto.ReportV2Dto.ReportResponse;
import com.mutecsoft.healthvision.common.dto.ReportV2Dto.ReportUploadRequest;
import com.mutecsoft.healthvision.common.dto.admin.AdminReportDto.SearchReport;

public interface ReportService {

	PageImpl<ReportResponse> selectReportListPage(SearchReport searchParam, Pageable pageable);

	void uploadReport(ReportUploadRequest uploadReq) throws IOException;

	void downloadReport(Long reportFileId, HttpServletResponse response);

	void deleteReport(Long reportId) throws IOException;

}
