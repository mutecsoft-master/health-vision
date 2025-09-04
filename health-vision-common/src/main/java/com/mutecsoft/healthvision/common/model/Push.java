package com.mutecsoft.healthvision.common.model;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Push {

	private Long pushId;
	private String title;
	private String body;
	private String type;
	private Integer successCnt;
	private Integer failCnt;
	private Integer targetCnt;
	private LocalDateTime regDt;
	
}
