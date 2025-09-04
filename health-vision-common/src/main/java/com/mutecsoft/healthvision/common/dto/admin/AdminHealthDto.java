package com.mutecsoft.healthvision.common.dto.admin;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminHealthDto {
    
	@Getter
	@Setter
	@AllArgsConstructor
	public static class SearchForDataDownload {
		private Long userId;
		private LocalDate startDate;
		private LocalDate endDate;
	}
	
	@Getter
	@Setter
    public static class FoodDiaryForDataDownload {
		private Long foodDiaryId;
	    private String foodNm;
	    private BigDecimal calories;
	    private String foodDesc;
	    private String mealTypeCd;
	    private LocalDateTime mealDt;
	    private Long userId;
		
		private String mealTypeNm;
    }
	
	@Getter
	@Setter
    public static class WorkoutForDataDownload {
		private Long workoutDataId;
		private String activityType;
		private double duration;
		private LocalDateTime startDt;
		private LocalDateTime endDt;
		private BigDecimal burnedCalories;
		private Long userId;
    }
	
	@Getter
	@Setter
    public static class SleepForDataDownload {
		private Long sleepDataId;
		private String category;
		private LocalDateTime startDt;
	    private LocalDateTime endDt;
		private Long userId;
    }
	
	
	@Getter
	@Setter
    public static class HeartRateForDataDownload {
		private Long hrDataId;
		private BigDecimal value;
		private LocalDateTime recordDt;
		private Long userId;
    }
	
	@Getter
	@Setter
    public static class BloodGlucoseForDataDownload {
		private Long bgDataId;
		private String unit;
		private BigDecimal value;
		private LocalDateTime recordDt;
		private Long userId;
    }
	
	
}
