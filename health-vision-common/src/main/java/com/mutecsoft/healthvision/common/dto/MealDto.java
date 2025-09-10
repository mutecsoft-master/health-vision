package com.mutecsoft.healthvision.common.dto;

import java.time.LocalDateTime;

import lombok.Data;

public class MealDto {

	@Data
	public static class MealResponse {
		
		private Long mealId;
		private String mealInfo;
		private LocalDateTime mealDt;
		private Long userId;
		private Long fileId;
		private Long regId;
		private LocalDateTime regDt;
		
	    private String imgUrl;
		
	}
	
}
