package com.mutecsoft.healthvision.common.constant;

//리포트 상태 코드
public enum ReportStatusCdEnum {

	PENDING("P", "결제대기"),
	APPLY("A", "신청"),
	COMPLETE("C", "분석완료");
	
	private final String value;
	private final String name;
	
	ReportStatusCdEnum(final String newValue, final String newName) {
		value = newValue;
		name = newName;
	}
	
	public String getValue(){
		return value;
	}
	
	public String getName(){
		return name;
	}
	
	public static ReportStatusCdEnum fromValue(String val) {
		if (val == null || val.isEmpty())
			return null;
		else {
			for (ReportStatusCdEnum e : values())
				if (e.getValue().equals(val))
					return e;
		}
		return null;
	}
	
}
