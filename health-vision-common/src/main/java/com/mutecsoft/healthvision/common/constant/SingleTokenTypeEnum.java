package com.mutecsoft.healthvision.common.constant;

//user_id 당 1개 토큰만 존재하는 경우
public enum SingleTokenTypeEnum {

	ACCESS("access", "access"),
	REFRESH("refresh", "refresh"),
	PASSWORD("password", "password")
	;
	
	private final String value;
	private final String desc;
	
	SingleTokenTypeEnum(final String newValue, final String descVal) {
		value = newValue;
		desc = descVal;
	}
	
	public String getValue(){
		return value;
	}
	
	public String getDescValue(){
		return desc;
	}
	
	public static SingleTokenTypeEnum fromValue(String val) {
		if (val == null || val.isEmpty())
			return null;
		else {
			for (SingleTokenTypeEnum e : values())
				if (e.getValue().equals(val))
					return e;
		}
		return null;
	}
	
}
