package com.mutecsoft.healthvision.common.constant;

//식이 다이어리 검색 종류
public enum FoodDiarySearchTypeEnum {

	MONTHLY("MONTHLY"), //월별
	WEEKLY("WEEKLY"), //주간
	DAILY("DAILY"); //일별
	
	private final String value;
	
	FoodDiarySearchTypeEnum(final String newValue) {
		value = newValue;
	}
	
	public String getValue(){
		return value;
	}
	
	public static FoodDiarySearchTypeEnum fromValue(String val) {
		if (val == null || val.isEmpty())
			return null;
		else {
			for (FoodDiarySearchTypeEnum e : values())
				if (e.getValue().equals(val))
					return e;
		}
		return null;
	}
	
}
