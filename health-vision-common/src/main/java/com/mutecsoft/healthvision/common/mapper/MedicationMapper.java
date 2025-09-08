package com.mutecsoft.healthvision.common.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.mutecsoft.healthvision.common.annotation.LoginId;
import com.mutecsoft.healthvision.common.dto.MedicationInfoDto.MedicationInfoSearchRequest;
import com.mutecsoft.healthvision.common.dto.MedicationLogDto.MedicationLogResponse;
import com.mutecsoft.healthvision.common.model.MedicationInfo;
import com.mutecsoft.healthvision.common.model.MedicationLog;

@Mapper
public interface MedicationMapper {

	@LoginId
	void insertMedicationInfo(MedicationInfo medInfo);

	@LoginId
	void insertMedicationLog(MedicationLog medLog);

	List<MedicationInfo> selectMedicationInfoList(Long userId);

	List<MedicationLogResponse> selectMedicationLogList(Long userId);

	MedicationInfo selectMedicationInfo(MedicationInfoSearchRequest searchReq);

	void deleteMedicationInfo(Long medInfoId);

}
