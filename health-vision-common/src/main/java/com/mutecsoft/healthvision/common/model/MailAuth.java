package com.mutecsoft.healthvision.common.model;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MailAuth {
	
	private Long mailAuthId;
	private String email;
	private String code;
	private LocalDateTime codeExpiredDt;
	private String verifiedYn;
	private LocalDateTime signupExpiredDt;
	private LocalDateTime regDt;
	private LocalDateTime updDt;
	
}
