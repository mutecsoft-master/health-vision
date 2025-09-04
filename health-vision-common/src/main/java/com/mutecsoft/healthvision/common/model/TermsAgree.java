package com.mutecsoft.healthvision.common.model;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TermsAgree {
	
	private Long termsAgreeId;
	private Long userId;
	private Long termsId;
	private LocalDateTime regDt;
	
}
