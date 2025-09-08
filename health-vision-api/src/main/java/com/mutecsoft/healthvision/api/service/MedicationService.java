package com.mutecsoft.healthvision.api.service;

import java.util.List;

import com.mutecsoft.healthvision.common.dto.MedicationLogDto.MedicationLogResponse;
import com.mutecsoft.healthvision.common.model.MedicationInfo;
import com.mutecsoft.healthvision.common.model.MedicationLog;

public interface MedicationService {

	void insertMedicationInfo(MedicationInfo medInfo);
	void insertMedicationLog(MedicationLog medLog);
	
	List<MedicationInfo> selectMedicationInfoList(Long userId);
	List<MedicationLogResponse> selectMedicationLogList(Long userId);


}
