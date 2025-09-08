package com.mutecsoft.healthvision.common.dto;

import lombok.Data;

public class MedicationInfoDto {

	@Data
	public static class MedicationInfoSearchRequest {
		private Long medInfoId;
		private Long userId;
	}
	
}
