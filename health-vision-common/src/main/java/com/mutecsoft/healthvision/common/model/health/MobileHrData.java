package com.mutecsoft.healthvision.common.model.health;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.mutecsoft.healthvision.common.model.BaseModel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MobileHrData extends BaseModel{
	
	private Long hrDataId;
	private BigDecimal value;
	private LocalDateTime recordDt;
	private Long userId;
    
}
