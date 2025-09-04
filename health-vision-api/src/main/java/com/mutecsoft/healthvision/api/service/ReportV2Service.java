package com.mutecsoft.healthvision.api.service;

import java.util.List;

import org.json.JSONObject;

import com.mutecsoft.healthvision.common.dto.AppleRtdnDto;
import com.mutecsoft.healthvision.common.dto.ResponseDto;
import com.mutecsoft.healthvision.common.dto.ReportV2Dto.PurchaseReportAppleRequest;
import com.mutecsoft.healthvision.common.dto.ReportV2Dto.PurchaseReportGoogleRequest;
import com.mutecsoft.healthvision.common.dto.ReportV2Dto.ReportApplyRequest;
import com.mutecsoft.healthvision.common.dto.ReportV2Dto.ReportResponse;
import com.mutecsoft.healthvision.common.model.Report;

public interface ReportV2Service {

	ResponseDto applyReport(ReportApplyRequest applyReq);
	ResponseDto purchaseReportForGoogle(PurchaseReportGoogleRequest purchaseReq);
	ResponseDto purchaseReportForApple(PurchaseReportAppleRequest purchaseReq);
	

	List<ReportResponse> getReportList(Integer limitMonth);
	void updateReportByGoogleRtdn(JSONObject json);
	void updateReportByAppleRtdn(AppleRtdnDto payloadDto);
	
	Report getReportByReportFileId(Long fileId);

	
}
