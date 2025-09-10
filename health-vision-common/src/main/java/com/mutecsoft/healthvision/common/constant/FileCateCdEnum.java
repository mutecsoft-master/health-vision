package com.mutecsoft.healthvision.common.constant;

//파일 카테고리 코드
public enum FileCateCdEnum {

	MEAL("MEAL", "Meal"), //식사 사진
	MEDICATION("MEDICATION", "Medication"), //복약 사진
	;
	
	private final String value;
	private final String desc;
	
	FileCateCdEnum(final String newValue, final String descVal) {
		value = newValue;
		desc = descVal;
	}
	
	public String getValue(){
		return value;
	}
	
	public String getDescValue(){
		return desc;
	}
	
	public static FileCateCdEnum fromValue(String val) {
		if (val == null || val.isEmpty())
			return null;
		else {
			for (FileCateCdEnum e : values())
				if (e.getValue().equals(val))
					return e;
		}
		return null;
	}
	
}
