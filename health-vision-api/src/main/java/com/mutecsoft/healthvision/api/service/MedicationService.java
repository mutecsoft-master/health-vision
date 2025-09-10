package com.mutecsoft.healthvision.api.service;

import java.io.IOException;
import java.util.List;

import com.mutecsoft.healthvision.common.dto.MedicationInfoDto.MedicationInfoResponse;
import com.mutecsoft.healthvision.common.dto.MedicationLogDto.MedicationLogResponse;
import com.mutecsoft.healthvision.common.model.MedicationInfo;
import com.mutecsoft.healthvision.common.model.MedicationLog;

public interface MedicationService {

	void insertMedicationInfo(MedicationInfo medInfo) throws IOException;
	void insertMedicationLog(MedicationLog medLog);
	
	List<MedicationInfoResponse> selectMedicationInfoList(Long userId);
	List<MedicationLogResponse> selectMedicationLogList(Long userId);
	void deleteMedicationInfo(Long medInfoId);


}
