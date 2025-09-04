package com.mutecsoft.healthvision.common.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.mutecsoft.healthvision.common.dto.ReportV2Dto.ReportResponse;
import com.mutecsoft.healthvision.common.dto.ReportV2Dto.ReportSearchRequest;
import com.mutecsoft.healthvision.common.model.Report;

@Mapper
public interface ReportV2Mapper {
	
    void insertReport(Report report);

    ReportResponse selectReportByBgFileId(Long bgFileId);
    
	List<ReportResponse> selectReportList(ReportSearchRequest searchReq);

	Report selectReportByPurchaseToken(String purchaseToken);

	void updateReportStatus(Report report);
	void updateReportPurchaseStatus(Report report);

	void deleteReport(Long reportId);

	Report selectReportById(Long reportId);

	Report selectReportByReportId(Long fileId);

    
}
