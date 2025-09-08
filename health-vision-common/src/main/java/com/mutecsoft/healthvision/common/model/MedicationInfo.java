package com.mutecsoft.healthvision.common.model;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MedicationInfo extends BaseModel {

	private Long medInfoId;
	private String medNm;
	private String dosePeriod;
	private LocalDate doseStartDate;
	private LocalDate doseEndDate;
	private String delYn;
	private Long userId;
	
}
