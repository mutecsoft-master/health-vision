package com.mutecsoft.healthvision.common.constant;

//결제 플랫폼
public enum PlatformEnum {

	GOOGLE("GOOGLE", "Google"),
	APPLE("APPLE", "Apple");
	
	private final String value;
	private final String desc;
	
	PlatformEnum(final String newValue, final String descVal) {
		value = newValue;
		desc = descVal;
	}
	
	public String getValue(){
		return value;
	}
	
	public String getDescValue(){
		return desc;
	}
	
	public static PlatformEnum fromValue(String val) {
		if (val == null || val.isEmpty())
			return null;
		else {
			for (PlatformEnum e : values())
				if (e.getValue().equals(val))
					return e;
		}
		return null;
	}
	
}
