package com.mutecsoft.healthvision.api.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mutecsoft.healthvision.api.service.MedicationService;
import com.mutecsoft.healthvision.common.dto.MedicationLogDto.MedicationLogResponse;
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
    
}
