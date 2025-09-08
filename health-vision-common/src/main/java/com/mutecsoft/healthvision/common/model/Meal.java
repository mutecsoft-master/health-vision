package com.mutecsoft.healthvision.common.model;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Meal extends BaseModel {

	private Long mealId;
	private String mealInfo;
	private LocalDateTime mealDt;
	private Long userId;
	
}
