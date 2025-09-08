package com.mutecsoft.healthvision.api.service.impl;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.mutecsoft.healthvision.api.service.MedicationService;
import com.mutecsoft.healthvision.api.util.UserUtil;
import com.mutecsoft.healthvision.common.dto.MedicationInfoDto.MedicationInfoSearchRequest;
import com.mutecsoft.healthvision.common.dto.MedicationLogDto.MedicationLogResponse;
import com.mutecsoft.healthvision.common.dto.UserDto.UserInfo;
import com.mutecsoft.healthvision.common.exception.CustomException;
import com.mutecsoft.healthvision.common.mapper.MedicationMapper;
import com.mutecsoft.healthvision.common.model.MedicationInfo;
import com.mutecsoft.healthvision.common.model.MedicationLog;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MedicationServiceImpl implements MedicationService {

    private final MedicationMapper medicationMapper;
    private final UserUtil userUtil;

	@Override
	public void insertMedicationInfo(MedicationInfo medInfo) {
		medicationMapper.insertMedicationInfo(medInfo);
	}

	@Override
	public void insertMedicationLog(MedicationLog medLog) {
		medicationMapper.insertMedicationLog(medLog);
	}

	@Override
	public List<MedicationInfo> selectMedicationInfoList(Long userId) {
		return medicationMapper.selectMedicationInfoList(userId);
	}

	@Override
	public List<MedicationLogResponse> selectMedicationLogList(Long userId) {
		return medicationMapper.selectMedicationLogList(userId);
	}

	@Override
	public void deleteMedicationInfo(Long medInfoId) {
		UserInfo userInfo = userUtil.getUserInfo();
		
		MedicationInfoSearchRequest searchReq = new MedicationInfoSearchRequest();
		searchReq.setMedInfoId(medInfoId);
		searchReq.setUserId(userInfo.getUserId());
		
		MedicationInfo medInfo = medicationMapper.selectMedicationInfo(searchReq);
		if(medInfo == null) {
			throw new CustomException("복약정보를 찾을 수 없습니다.", HttpStatus.BAD_REQUEST);
		}
		
		medicationMapper.deleteMedicationInfo(medInfoId);
		
	}
    
}
