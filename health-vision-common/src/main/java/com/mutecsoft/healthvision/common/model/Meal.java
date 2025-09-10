package com.mutecsoft.healthvision.common.model;

import java.time.LocalDateTime;

import org.springframework.web.multipart.MultipartFile;

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
	private Long fileId;
	
	//추가 정보
    private MultipartFile file;
	
}
