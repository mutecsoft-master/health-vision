package com.mutecsoft.healthvision.common.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Notice extends BaseModel {
	
	private Long NoticeId;
	private String title;
	private String content;
	private String lang;
	private String delYn;
	
}
