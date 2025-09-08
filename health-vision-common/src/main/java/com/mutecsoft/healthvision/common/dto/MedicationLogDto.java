package com.mutecsoft.healthvision.common.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;

public class MedicationLogDto {

	@Data
	public static class MedicationLogResponse {
		private Long medLogId;
		private Long medInfoId;
		private LocalDateTime doseDt;
		private Long userId;
		private LocalDateTime regDt;

		//MedicationInfo
		private String medNm;
		private String dosePeriod;
		private LocalDate doseStartDate;
		private LocalDate doseEndDate;
	}
	
}
