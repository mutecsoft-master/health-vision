package com.mutecsoft.healthvision.api.service;

import java.util.List;

import org.json.JSONObject;

import com.mutecsoft.healthvision.common.dto.AppleRtdnDto;
import com.mutecsoft.healthvision.common.dto.ResponseDto;
import com.mutecsoft.healthvision.common.dto.ReportDto.ReportApplyAppleRequest;
import com.mutecsoft.healthvision.common.dto.ReportDto.ReportApplyFreeRequest;
import com.mutecsoft.healthvision.common.dto.ReportDto.ReportApplyGoogleRequest;
import com.mutecsoft.healthvision.common.dto.ReportDto.ReportResponse;
import com.mutecsoft.healthvision.common.model.Report;

public interface ReportService {

	ResponseDto applyReportForGoogle(ReportApplyGoogleRequest applyReq);
	ResponseDto applyReportForApple(ReportApplyAppleRequest applyReq);
	ResponseDto applyReportForFree(ReportApplyFreeRequest applyReq);

	List<ReportResponse> getReportList(Integer limitMonth);
	void updateReportByGoogleRtdn(JSONObject json);
	void updateReportByAppleRtdn(AppleRtdnDto payloadDto);
	
	Report getReportByReportFileId(Long fileId);

	
}
