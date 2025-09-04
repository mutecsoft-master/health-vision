package com.mutecsoft.healthvision.common.constant;

//user_id 당 N개 토큰이 존재하는 경우
public enum MultiTokenTypeEnum {

	FCM("fcm", "fcm"),
	;
	
	private final String value;
	private final String desc;
	
	MultiTokenTypeEnum(final String newValue, final String descVal) {
		value = newValue;
		desc = descVal;
	}
	
	public String getValue(){
		return value;
	}
	
	public String getDescValue(){
		return desc;
	}
	
	public static MultiTokenTypeEnum fromValue(String val) {
		if (val == null || val.isEmpty())
			return null;
		else {
			for (MultiTokenTypeEnum e : values())
				if (e.getValue().equals(val))
					return e;
		}
		return null;
	}
	
}
