package com.mutecsoft.healthvision.common.model.health;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.mutecsoft.healthvision.common.model.BaseModel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MobileWorkoutData extends BaseModel{
	
	private Long workoutDataId;
	private String activityType;
	private double duration;
	private LocalDateTime startDt;
    private LocalDateTime endDt;
	private BigDecimal burnedCalories;
	private Long userId;
    
}
