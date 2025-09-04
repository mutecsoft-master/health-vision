package com.mutecsoft.healthvision.api.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.mutecsoft.healthvision.api.service.FileService;
import com.mutecsoft.healthvision.api.service.ReportService;
import com.mutecsoft.healthvision.api.util.UserUtil;
import com.mutecsoft.healthvision.common.constant.FileCateCdEnum;
import com.mutecsoft.healthvision.common.constant.ReportDownloadTypeEnum;
import com.mutecsoft.healthvision.common.constant.ReportV2StatusCdEnum;
import com.mutecsoft.healthvision.common.constant.ResultCdEnum;
import com.mutecsoft.healthvision.common.dto.UserDto.UserInfo;
import com.mutecsoft.healthvision.common.exception.CustomException;
import com.mutecsoft.healthvision.common.mapper.FileMapper;
import com.mutecsoft.healthvision.common.model.FileModel;
import com.mutecsoft.healthvision.common.model.Report;
import com.mutecsoft.healthvision.common.util.FileUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService{
	
	private final FileMapper fileMapper;
	private final UserUtil userUtil;
	private final FileUtil fileUtil;
	
	private final ReportService reportService;
	
	@Value("${file.base-path}")
    private String basePath;
	
	    
	@Override
	public void insertFile(FileModel fileModel) {
		fileMapper.insertFile(fileModel);
	}
	
	@Override
	public FileModel selectFile(Long fileId) {
		return fileMapper.selectFile(fileId);
	}
	
	@Override
	public ResponseEntity<?> getFile(Long fileId, HttpServletResponse response) {
		FileModel fileModel = fileMapper.selectFile(fileId);
		UserInfo userInfo = userUtil.getUserInfo();
		
		if(fileModel != null) {
			//권한 체크
			if(StringUtils.hasText(fileModel.getFileCateCd())) {
				
				FileCateCdEnum fileCateCdEnum = FileCateCdEnum.fromValue(fileModel.getFileCateCd());
				
				switch (fileCateCdEnum) {
					case FOOD: case PRESET: case PROFILE: {
						//소유자 체크
						if(!userInfo.getUserId().equals(fileModel.getRegId())) {
							throw new CustomException(ResultCdEnum.E002.getValue());
						}
						break;
					}
					
					//TODO[csm] V2 정리되면 삭제
					case REPORT: {
						Report report = reportService.getReportByReportFileId(fileId);
						//신청자 체크
						if(!userInfo.getUserId().equals(report.getAplyId())) {
							throw new CustomException(ResultCdEnum.E002.getValue());
						}
						break;
					}
					default: 
						break;
				}
			}
			
			File file = new File(basePath + "/" + fileModel.getFilePath());
		    if (!file.exists()) {
		    	throw new CustomException(ResultCdEnum.E003.getValue());
		    }

		    try {
		        Resource resource = new FileSystemResource(file);
		        
		        // 파일의 MIME 타입을 동적으로 설정
		        String contentType = Files.probeContentType(file.toPath());
		        if (contentType == null) {
		            contentType = "application/octet-stream"; // 알 수 없는 경우 기본값 설정
		        }

		        return ResponseEntity.ok()
		                .contentType(MediaType.parseMediaType(contentType))
		                .body(resource);
		    } catch (IOException e) {
		        throw new CustomException(ResultCdEnum.E004.getValue());
		    }
		}else {
			throw new CustomException(ResultCdEnum.E003.getValue());
		}
		
	}
	
	@Override
	public ResponseEntity<?> getReport(Long reportFileId, ReportDownloadTypeEnum downloadTypeEnum, HttpServletResponse response) {
		FileModel fileModel = fileMapper.selectFile(reportFileId);
		UserInfo userInfo = userUtil.getUserInfo();
		
		if(fileModel != null) {
			//권한 체크
			if(StringUtils.hasText(fileModel.getFileCateCd())) {
				
				FileCateCdEnum fileCateCdEnum = FileCateCdEnum.fromValue(fileModel.getFileCateCd());
				
				switch (fileCateCdEnum) {
					case REPORT: {
						Report report = reportService.getReportByReportFileId(reportFileId);
						//신청자 체크
						if(!userInfo.getUserId().equals(report.getAplyId())) {
							throw new CustomException(ResultCdEnum.E002.getValue());
						}
						
						//결제여부 확인
						if(downloadTypeEnum == ReportDownloadTypeEnum.DOWNLOAD) {
							if(!report.getStatusCd().equals(ReportV2StatusCdEnum.COMPLETE.getValue())) {
								throw new CustomException(ResultCdEnum.E002.getValue()); 
							}
						}
						
						break;
					}
					default: 
						break;
				}
			}
			
			File file = new File(basePath + "/" + fileModel.getFilePath());
		    if (!file.exists()) {
		    	throw new CustomException(ResultCdEnum.E003.getValue());
		    }

		    try {
		        Resource resource = new FileSystemResource(file);
		        
		        // 파일의 MIME 타입을 동적으로 설정
		        String contentType = Files.probeContentType(file.toPath());
		        if (contentType == null) {
		            contentType = "application/octet-stream"; // 알 수 없는 경우 기본값 설정
		        }

		        return ResponseEntity.ok()
		                .contentType(MediaType.parseMediaType(contentType))
		                .body(resource);
		    } catch (IOException e) {
		        throw new CustomException(ResultCdEnum.E004.getValue());
		    }
		}else {
			throw new CustomException(ResultCdEnum.E003.getValue());
		}
		
	}

	@Override
	public void deleteFile(Long fileId) throws IOException {
		
		FileModel fileModel = fileMapper.selectFile(fileId);
		
		if(fileModel != null) {
			//1.실제 파일 삭제
			if(StringUtils.hasText(fileModel.getFilePath())) {
				fileUtil.deleteFile(fileModel.getFilePath());
			}
			
		    //2. DB 데이터 삭제
		    fileMapper.deleteFile(fileId);
		}
		
	}
	
	
	
	


}
