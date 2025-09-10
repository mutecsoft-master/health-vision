package com.mutecsoft.healthvision.common.model;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

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
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate doseStartDate;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate doseEndDate;
	private String delYn;
	private Long userId;
	private Long fileId;
	
	//추가 정보
    private MultipartFile file;
	
}
