package com.mutecsoft.healthvision.common.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

public class MedicationInfoDto {
	
	
	@Data
	public static class MedicationInfoRequest {
		private String medNm;
		private String dosePeriod;
		
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private LocalDate doseStartDate;
		
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private LocalDate doseEndDate;
		
		private String doseYn; //복약여부
		private LocalDateTime doseDt;
	}
	

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
