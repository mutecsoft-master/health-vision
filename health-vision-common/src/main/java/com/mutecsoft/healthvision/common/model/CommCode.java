package com.mutecsoft.healthvision.common.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommCode extends BaseModel {
	
	private Long codeId;
	private String codeGroup;
	private String codeGroupNm;
	private String code;
	private String codeNm;
	private int sortOrder;
	private String useYn = "Y"; //디폴트 값 설정
	
}
