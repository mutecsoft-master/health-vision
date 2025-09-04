package com.mutecsoft.healthvision.common.constant;

//식사종류 코드
public enum MealTypeCdEnum {

	BREAKFAST("B", "Breakfast"), //아침
	LUNCH("L", "Lunch"), //점심
	DINNER("D", "Dinner"), //저녁
	SNACK("S", "Snack") ; //간식
	
	private final String value;
	private final String desc;
	
	MealTypeCdEnum(final String newValue, final String descVal) {
		value = newValue;
		desc = descVal;
	}
	
	public String getValue(){
		return value;
	}
	
	public String getDescValue(){
		return desc;
	}
	
	public static MealTypeCdEnum fromValue(String val) {
		if (val == null || val.isEmpty())
			return null;
		else {
			for (MealTypeCdEnum e : values())
				if (e.getValue().equals(val))
					return e;
		}
		return null;
	}
	
}
