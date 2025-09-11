package com.mutecsoft.healthvision.common.model;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MedicationLog extends BaseModel {

	private Long medLogId;
	private Long medInfoId;
	private LocalDateTime doseDt;
	private Long userId;
	private String delYn;
	private LocalDateTime delDt;
	private Long regId;
	private LocalDateTime regDt;
	
}
