package com.mutecsoft.healthvision.common.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.mutecsoft.healthvision.common.dto.MedicationInfoDto.MedicationInfoRequest;

import lombok.Data;

public class MedicationLogDto {
	
	//약물정보, 복약기록 통합 insert 용
	@Data
	public static class MedicationLogRequest {
		private MultipartFile file;
		private Long fileId;
		List<MedicationInfoRequest> medInfoList;
	}
	
	@Data
	public static class MedicationLogSearchRequest {
		private Long medLogId;
		private Long userId;
	}
	

	@Data
	public static class MedicationLogResponse {
		private Long medLogId;
		private Long medInfoId;
		private LocalDateTime doseDt;
		private Long fileId;
		private Long userId;
		private LocalDateTime regDt;
		
		private String imgUrl;

		//MedicationInfo
		private String medNm;
		private String dosePeriod;
		private LocalDate doseStartDate;
		private LocalDate doseEndDate;
	}
	
}
