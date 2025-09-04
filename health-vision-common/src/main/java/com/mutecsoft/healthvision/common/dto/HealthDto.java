package com.mutecsoft.healthvision.common.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HealthDto {
    
	@Getter
	@Setter
	public static class HealthSaveDto {
		
		@NotNull
		private LocalDateTime uploadDt;
		
		@NotNull
		private MultipartFile file;
	}
	
	@Getter
	@Setter
	public static class HealthInfoRequest {
		@NotNull
		private String startDate;
		@NotNull
		private String endDate;
		
		private Long userId;
	}
	
	@Getter
	@Setter
	public static class HealthInfoResponse {
		private String bloodGlucosePer;
		private String workoutPer;
		private String sleepPer;
		private String heartRatePer;
		private String foodDiaryPer;
	}
	
	@Getter
	@Setter
	public static class BgInfo {
		private Long cnt;
		private Date recordDate;
	}
	
}
