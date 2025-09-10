package com.mutecsoft.healthvision.common.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;

public class MedicationInfoDto {

	@Data
	public static class MedicationInfoSearchRequest {
		private Long medInfoId;
		private Long userId;
	}
	
	@Data
	public static class MedicationInfoResponse {
		
		private Long medInfoId;
		private String medNm;
		private String dosePeriod;
		private LocalDate doseStartDate;
		private LocalDate doseEndDate;
		private String delYn;
		private Long userId;
		private Long fileId;
		private Long regId;
		private LocalDateTime regDt;
		
	    private String imgUrl;
		
	}
	
}
