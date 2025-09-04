package com.mutecsoft.healthvision.common.model;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PushTarget {

	private Long pushTargetId;
	private String readYn;
	private LocalDateTime readDt;
	private Long pushId;
	private Long userId;
	
}
