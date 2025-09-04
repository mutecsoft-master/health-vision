package com.mutecsoft.healthvision.common.constant;

//파일 카테고리 코드
public enum FileCateCdEnum {

	FOOD("FOOD", "Food Diary"), //식이 다이어리 음식 사진
	PRESET("PRESET", "Food Preset"), //즐겨먹기 음식 사진
	PROFILE("PROFILE", "User Profile"), //사용자 프로필
	REPORT("REPORT", "Report"); //리포트
	
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
