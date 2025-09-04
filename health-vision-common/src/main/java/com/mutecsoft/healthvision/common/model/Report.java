package com.mutecsoft.healthvision.common.model;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Report {

	private Long reportId;
	private Long aplyId;
	private LocalDateTime aplyDt;
	private String statusCd;
	private String purchaseStatusCd;
	private Long anlysId;
	private LocalDateTime anlysDt;
	private Long reportFileId;
	private Long bgFileId;
	
}
