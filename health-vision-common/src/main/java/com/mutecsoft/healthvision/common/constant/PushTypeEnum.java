package com.mutecsoft.healthvision.common.constant;

//Push 타입
public enum PushTypeEnum {

	REPORT_COMPLETE("REPORT COMPLETE", "Report Complete");
	
	private final String value;
	private final String desc;
	
	PushTypeEnum(final String newValue, final String descVal) {
		value = newValue;
		desc = descVal;
	}
	
	public String getValue(){
		return value;
	}
	
	public String getDescValue(){
		return desc;
	}
	
	public static PushTypeEnum fromValue(String val) {
		if (val == null || val.isEmpty())
			return null;
		else {
			for (PushTypeEnum e : values())
				if (e.getValue().equals(val))
					return e;
		}
		return null;
	}
	
}
