package com.mutecsoft.healthvision.common.model;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MailSendAttempt {

	private Long mailSendAttemptId;
	private String email;
	private String ip;
	private String mailTypeCd;
	private Integer reqCnt;
	private LocalDateTime lastAttemptDt;
    
}
