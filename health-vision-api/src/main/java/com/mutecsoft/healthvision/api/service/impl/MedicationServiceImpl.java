package com.mutecsoft.healthvision.api.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mutecsoft.healthvision.api.service.FileService;
import com.mutecsoft.healthvision.api.service.MedicationService;
import com.mutecsoft.healthvision.api.util.UserUtil;
import com.mutecsoft.healthvision.common.constant.FileCateCdEnum;
import com.mutecsoft.healthvision.common.dto.FileDto.FileInsertDto;
import com.mutecsoft.healthvision.common.dto.MedicationInfoDto.MedicationInfoRequest;
import com.mutecsoft.healthvision.common.dto.MedicationLogDto.MedicationLogRequest;
import com.mutecsoft.healthvision.common.dto.MedicationLogDto.MedicationLogResponse;
import com.mutecsoft.healthvision.common.dto.MedicationLogDto.MedicationLogSearchRequest;
import com.mutecsoft.healthvision.common.dto.UserDto.UserInfo;
import com.mutecsoft.healthvision.common.exception.CustomException;
import com.mutecsoft.healthvision.common.mapper.MedicationMapper;
import com.mutecsoft.healthvision.common.model.FileModel;
import com.mutecsoft.healthvision.common.model.MedicationInfo;
import com.mutecsoft.healthvision.common.model.MedicationLog;
import com.mutecsoft.healthvision.common.util.FileUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MedicationServiceImpl implements MedicationService {

    private final MedicationMapper medicationMapper;
    private final ModelMapper modelMapper;
    private final FileService fileService;
    private final FileUtil fileUtil;
    private final UserUtil userUtil;
	
    @Transactional
    @Override
	public void insertMedicationLog(MedicationLogRequest medLogDto) throws IOException {
		
    	if(medLogDto.getFile() != null) {
			//1. 파일저장
			FileInsertDto fileDto = new FileInsertDto(medLogDto.getFile(), FileCateCdEnum.MEDICATION.getValue());
			FileModel fileModel = fileUtil.saveFile(fileDto);
			
			//2. 파일데이터 DB 저장
			fileService.insertFile(fileModel);
			
			medLogDto.setFileId(fileModel.getFileId());
		}
    	
    	List<MedicationInfoRequest> medInfoList = medLogDto.getMedInfoList();
    	for(MedicationInfoRequest medInfoDto : medInfoList) {
    		//1. 복약정보 저장
    		
    		MedicationInfo medInfo = modelMapper.map(medInfoDto, MedicationInfo.class);
    		medInfo.setFileId(medLogDto.getFileId());
    		medicationMapper.insertMedicationInfo(medInfo);
    		
    		log.info("@@ medInfoId : {}", medInfo.getMedInfoId()); //PK 저장 확인
    		
    		//2. 복약기록 저장
    		if(medInfoDto.getDoseYn().equals("Y")) {
    			
    			MedicationLog medLog = new MedicationLog();
    			medLog.setMedInfoId(medInfo.getMedInfoId());
    			medLog.setDoseDt(medInfoDto.getDoseDt());
    			medicationMapper.insertMedicationLog(medLog);
    		}
    	}
	}

    @Override
	public List<MedicationLogResponse> selectMedicationLogList(Long userId) {
		
		List<MedicationLogResponse> list = medicationMapper.selectMedicationLogList(userId);
		List<MedicationLogResponse> dtoList = list.stream()
				.map(m -> {
					MedicationLogResponse dto = modelMapper.map(m, MedicationLogResponse.class);
		            dto.setImgUrl(fileUtil.makeImgApiUrl(dto.getFileId()));
		            return dto;
		        })
				.collect(Collectors.toList());
		return dtoList;
	}
    
    
    @Override
	public void deleteMedicationLog(Long medLogId) {
		UserInfo userInfo = userUtil.getUserInfo();
		
		MedicationLogSearchRequest searchReq = new MedicationLogSearchRequest();
		searchReq.setMedLogId(medLogId);
		searchReq.setUserId(userInfo.getUserId());
		
		MedicationLog medLog = medicationMapper.selectMedicationLog(searchReq);
		if(medLog == null) {
			throw new CustomException("복약기록을 찾을 수 없습니다.", HttpStatus.BAD_REQUEST);
		}
		
		medicationMapper.deleteMedicationLog(medLogId);
	}

	
    
    
    //기획문서(한글파일) 토대로 작업한 내용. 뮤텍 내부 회의를 거쳐 API 변경 - 25-09-11
//  @Transactional
//	@Override
//	public void insertMedicationInfo(MedicationInfo medInfo) throws IOException {
//    	
//    	if(medInfo.getFile() != null) {
//			//1. 파일저장
//			FileInsertDto fileDto = new FileInsertDto(medInfo.getFile(), FileCateCdEnum.MEAL.getValue());
//			FileModel fileModel = fileUtil.saveFile(fileDto);
//			
//			//2. 파일데이터 DB 저장
//			fileService.insertFile(fileModel);
//			
//			medInfo.setFileId(fileModel.getFileId());
//		}
//
//		medicationMapper.insertMedicationInfo(medInfo);
//	}
//
//	@Override
//	public void insertMedicationLog(MedicationLog medLog) {
//		
//		UserInfo userInfo = userUtil.getUserInfo();
//		
//		MedicationInfoSearchRequest searchReq = new MedicationInfoSearchRequest();
//		searchReq.setMedInfoId(medLog.getMedInfoId());
//		searchReq.setUserId(userInfo.getUserId());
//		
//		MedicationInfo medInfo = medicationMapper.selectMedicationInfo(searchReq);
//		if(medInfo == null) {
//			throw new CustomException("복약정보를 찾을 수 없습니다.", HttpStatus.BAD_REQUEST);
//		}
//		
//		medicationMapper.insertMedicationLog(medLog);
//	}
//
//	@Override
//	public List<MedicationInfoResponse> selectMedicationInfoList(Long userId) {
//		
//		List<MedicationInfo> list = medicationMapper.selectMedicationInfoList(userId);
//		List<MedicationInfoResponse> dtoList = list.stream()
//				.map(m -> {
//					MedicationInfoResponse dto = modelMapper.map(m, MedicationInfoResponse.class);
//		            dto.setImgUrl(fileUtil.makeImgApiUrl(dto.getFileId()));
//		            return dto;
//		        })
//				.collect(Collectors.toList());
//				
//		return dtoList;
//	}
//
//	@Override
//	public List<MedicationLogResponse> selectMedicationLogList(Long userId) {
//		
//		List<MedicationLogResponse> list = medicationMapper.selectMedicationLogList(userId);
//		List<MedicationLogResponse> dtoList = list.stream()
//				.map(m -> {
//					MedicationLogResponse dto = modelMapper.map(m, MedicationLogResponse.class);
//		            dto.setImgUrl(fileUtil.makeImgApiUrl(dto.getFileId()));
//		            return dto;
//		        })
//				.collect(Collectors.toList());
//		return dtoList;
//	}
//
//	@Override
//	public void deleteMedicationInfo(Long medInfoId) {
//		UserInfo userInfo = userUtil.getUserInfo();
//		
//		MedicationInfoSearchRequest searchReq = new MedicationInfoSearchRequest();
//		searchReq.setMedInfoId(medInfoId);
//		searchReq.setUserId(userInfo.getUserId());
//		
//		MedicationInfo medInfo = medicationMapper.selectMedicationInfo(searchReq);
//		if(medInfo == null) {
//			throw new CustomException("복약정보를 찾을 수 없습니다.", HttpStatus.BAD_REQUEST);
//		}
//		
//		medicationMapper.deleteMedicationInfo(medInfoId);
//		
//	}
    
}
