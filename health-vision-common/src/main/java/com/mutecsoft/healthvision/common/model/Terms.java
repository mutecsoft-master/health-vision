package com.mutecsoft.healthvision.common.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Terms extends BaseModel {
	
	private Long termsId;
	private String type;
	private Integer version;
	private String title;
	private String content;
	private String requiredYn;
	private String lang;
	private String delYn;
	
}
