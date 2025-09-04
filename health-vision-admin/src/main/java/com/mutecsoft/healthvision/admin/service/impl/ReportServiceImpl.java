package com.mutecsoft.healthvision.admin.service.impl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.mutecsoft.healthvision.admin.service.FileService;
import com.mutecsoft.healthvision.admin.service.PushService;
import com.mutecsoft.healthvision.admin.service.ReportService;
import com.mutecsoft.healthvision.admin.util.UserUtil;
import com.mutecsoft.healthvision.common.constant.FileCateCdEnum;
import com.mutecsoft.healthvision.common.constant.PushTypeEnum;
import com.mutecsoft.healthvision.common.constant.ReportPurchaseStatusCdEnum;
import com.mutecsoft.healthvision.common.constant.ReportStatusCdEnum;
import com.mutecsoft.healthvision.common.constant.ReportV2StatusCdEnum;
import com.mutecsoft.healthvision.common.dto.PushDto;
import com.mutecsoft.healthvision.common.dto.FileDto.FileInsertDto;
import com.mutecsoft.healthvision.common.dto.ReportV2Dto.ReportResponse;
import com.mutecsoft.healthvision.common.dto.ReportV2Dto.ReportUploadRequest;
import com.mutecsoft.healthvision.common.dto.UserDto.UserInfo;
import com.mutecsoft.healthvision.common.dto.admin.AdminReportDto.SearchReport;
import com.mutecsoft.healthvision.common.mapper.AReportMapper;
import com.mutecsoft.healthvision.common.model.FileModel;
import com.mutecsoft.healthvision.common.model.Report;
import com.mutecsoft.healthvision.common.util.FileUtil;
import com.mutecsoft.healthvision.common.util.MessageUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ReportServiceImpl implements ReportService {
	
	private final AReportMapper aReportMapper;
	private final FileService fileService;
	private final FileUtil fileUtil;
	private final UserUtil userUtil;
	private final PushService pushService;
	private final MessageUtil messageUtil;
	
	@Override
	public PageImpl<ReportResponse> selectReportListPage(SearchReport searchParam, Pageable pageable) {
		
		List<ReportResponse> reportList = aReportMapper.selectReportList(searchParam, pageable);
        int totalCnt = aReportMapper.selectReportListCnt(searchParam, pageable);
        
        reportList.forEach(report -> {
            ReportV2StatusCdEnum statusEnum = ReportV2StatusCdEnum.fromValue(report.getStatusCd());
            report.setStatusNm(statusEnum.getName());
            
            ReportPurchaseStatusCdEnum purchaseStatusEnum = ReportPurchaseStatusCdEnum.fromValue(report.getPurchaseStatusCd());
            if(purchaseStatusEnum != null) {
            	report.setPurchaseStatusNm(purchaseStatusEnum.getName());
            }
            
        });
        
        return new PageImpl<>(reportList, pageable, totalCnt);
	}

	@Override
	public void uploadReport(ReportUploadRequest uploadReq) throws IOException {
		
		UserInfo userInfo = userUtil.getUserInfo();
		Report report = aReportMapper.selectReportById(uploadReq.getReportId());
		
		if(report != null) {
			if(report.getReportFileId() != null) {
				//기존 파일 삭제
				fileService.deleteFile(report.getReportFileId());
			}
			
			//1. 파일 저장
			FileInsertDto fileDto = new FileInsertDto(0, uploadReq.getFile(), FileCateCdEnum.REPORT.getValue());
			FileModel fileModel = fileUtil.saveFile(fileDto);

			//2. 파일데이터 DB 저장
			if(StringUtils.hasText(fileModel.getFilePath())) {
				fileService.insertFile(fileModel);
				report.setReportFileId(fileModel.getFileId());
			}
			
			report.setStatusCd(ReportV2StatusCdEnum.COMPLETE.getValue());
			report.setAnlysDt(LocalDateTime.now());
			report.setAnlysId(userInfo.getUserId());
			aReportMapper.updateReportFile(report);
			
		}
		
		PushDto pushDto = new PushDto();
		pushDto.setTitle(messageUtil.getMessage("admin.push.reportComplete.title"));
		pushDto.setBody(messageUtil.getMessage("admin.push.reportComplete.body"));
		pushService.sendPush(report.getAplyId(), PushTypeEnum.REPORT_COMPLETE, pushDto);
		
	}

	@Override
	public void downloadReport(Long reportFileId, HttpServletResponse response) {
		FileModel fm = fileService.selectFile(reportFileId);
		
		fileUtil.downloadFileByFileModel(fm, response);
	}

	@Override
	public void deleteReport(Long reportId) throws IOException {
		Report report = aReportMapper.selectReportById(reportId);
		
		if(report != null) {
			if(report.getReportFileId() != null) {
				//기존 파일 삭제
				fileService.deleteFile(report.getReportFileId());
			}
			
			report.setStatusCd(ReportStatusCdEnum.APPLY.getValue());
			aReportMapper.deleteReportFile(report);
		}
	}

}
