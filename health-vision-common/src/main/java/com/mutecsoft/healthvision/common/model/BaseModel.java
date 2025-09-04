package com.mutecsoft.healthvision.common.model;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseModel {
	
	private Long regId;
	private LocalDateTime regDt;
	private Long updId;
	private LocalDateTime updDt;

}
