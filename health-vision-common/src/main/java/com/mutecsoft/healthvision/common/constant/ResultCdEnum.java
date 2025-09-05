package com.mutecsoft.healthvision.common.constant;

//value : 클라이언트에서 사용할 코드
//desc : 현재 직접 사용되는 부분 없음. 추후 확장을 위해 정의 
public enum ResultCdEnum {
	
	//일반 오류
	E001("E001", "Internal Server Error"),
	E002("E002", "Access denied"), //권한 없음
	E003("E003", "Resource not found"), //자원 찾지 못함 (파일 등)
	E004("E004", "File upload failed"), //파일 업로드 실패
//	E005("E005", "Mail send failed"), //메일 발송 실패
	E006("E006", "Request parameter validation failed"), //파라미터 유효성 검사 실패
//	E007("E007", "Mail verification failed"), //메일 인증 실패
//	E008("E008", "Purchase verification failed"), //결제 검증 실패

	;
	
	private final String value;
	private final String desc;
	
	ResultCdEnum(final String newValue, final String descVal) {
		value = newValue;
		desc = descVal;
	}
	
	public String getValue(){
		return value;
	}
	
	public String getDescValue(){
		return desc;
	}
	
	public static ResultCdEnum fromValue(String val) {
		if (val == null || val.isEmpty())
			return null;
		else {
			for (ResultCdEnum e : values())
				if (e.getValue().equals(val))
					return e;
		}
		return null;
	}
	
}
