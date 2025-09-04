package com.mutecsoft.healthvision.common.constant;

//리포트 상태 코드
public enum ReportDownloadTypeEnum {

	PREVIEW("PREVIEW", "Preview"),
	DOWNLOAD("DOWNLOAD", "다운로드");
	
	private final String value;
	private final String name;
	
	ReportDownloadTypeEnum(final String newValue, final String newName) {
		value = newValue;
		name = newName;
	}
	
	public String getValue(){
		return value;
	}
	
	public String getName(){
		return name;
	}
	
	public static ReportDownloadTypeEnum fromValue(String val) {
		if (val == null || val.isEmpty())
			return null;
		else {
			for (ReportDownloadTypeEnum e : values())
				if (e.getValue().equals(val))
					return e;
		}
		return null;
	}
	
}
