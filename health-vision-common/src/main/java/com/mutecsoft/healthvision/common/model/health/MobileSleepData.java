package com.mutecsoft.healthvision.common.model.health;

import java.time.LocalDateTime;

import com.mutecsoft.healthvision.common.model.BaseModel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MobileSleepData extends BaseModel{
	
	private Long sleepDataId;
	private String category;
	private LocalDateTime startDt;
    private LocalDateTime endDt;
	private Long userId;
    
}
