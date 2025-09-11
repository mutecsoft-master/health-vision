package com.mutecsoft.healthvision.api.service;

import java.io.IOException;
import java.util.List;

import com.mutecsoft.healthvision.common.dto.MedicationInfoDto.MedicationInfoResponse;
import com.mutecsoft.healthvision.common.dto.MedicationLogDto.MedicationLogRequest;
import com.mutecsoft.healthvision.common.dto.MedicationLogDto.MedicationLogResponse;
import com.mutecsoft.healthvision.common.model.MedicationInfo;
import com.mutecsoft.healthvision.common.model.MedicationLog;

public interface MedicationService {

	void insertMedicationLog(MedicationLogRequest medLogDto) throws IOException;

	List<MedicationLogResponse> selectMedicationLogList(Long userId);

	void deleteMedicationLog(Long medLogId);

	
	
	//기획문서(한글파일) 토대로 작업한 내용. 뮤텍 내부 회의를 거쳐 API 변경 - 25-09-11
//	void insertMedicationInfo(MedicationInfo medInfo) throws IOException;
//	void insertMedicationLog(MedicationLog medLog);
//	List<MedicationInfoResponse> selectMedicationInfoList(Long userId);
//	List<MedicationLogResponse> selectMedicationLogList(Long userId);
//	void deleteMedicationInfo(Long medInfoId);


}
