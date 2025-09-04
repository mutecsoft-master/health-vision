package com.mutecsoft.healthvision.common.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PushFail {

	private Long pushFailId;
	private String errorCd;
	private String errorDesc;
	private String token;
	private Long pushId;
	
}
