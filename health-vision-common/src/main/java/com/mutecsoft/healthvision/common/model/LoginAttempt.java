package com.mutecsoft.healthvision.common.model;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LoginAttempt {

	private Long loginAttemptId;
	private String email;
	private String ip;
	private Integer failCnt;
	private LocalDateTime lastAttemptDt;
    
}
