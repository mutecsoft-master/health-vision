package com.mutecsoft.healthvision.common.constant;

//리포트 상태 코드
public enum ReportPurchaseStatusCdEnum {

	UNPAID("U", "미결제"),
	PENDING("P", "결제대기"),
	COMPLETE("C", "결제완료");
	
	private final String value;
	private final String name;
	
	ReportPurchaseStatusCdEnum(final String newValue, final String newName) {
		value = newValue;
		name = newName;
	}
	
	public String getValue(){
		return value;
	}
	
	public String getName(){
		return name;
	}
	
	public static ReportPurchaseStatusCdEnum fromValue(String val) {
		if (val == null || val.isEmpty())
			return null;
		else {
			for (ReportPurchaseStatusCdEnum e : values())
				if (e.getValue().equals(val))
					return e;
		}
		return null;
	}
	
}
