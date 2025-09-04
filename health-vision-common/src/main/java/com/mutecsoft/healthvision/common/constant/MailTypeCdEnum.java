package com.mutecsoft.healthvision.common.constant;

//메일 종류 코드
public enum MailTypeCdEnum {

	AUTH("AUTH", 5, 2), //메일 인증
	FIND_PW("FIND_PW", 5, 2); //비밀번호 찾기
	
	private final String value;
	private final int limitReqCnt;	//횟수 제한 (n회까지 발송 가능)
	private final int waitHours; 	//대기 시간 (m시간 후 재발송 가능)
	
	MailTypeCdEnum(final String newValue, final int limitReqCntValue, int waitHoursValue) {
		value = newValue;
		limitReqCnt = limitReqCntValue;
		waitHours = waitHoursValue;
	}
	
	public String getValue(){
		return value;
	}
	
	public int getLimitReqCnt() {
		return limitReqCnt;
	}

	public int getWaitHours() {
		return waitHours;
	}

	public static MailTypeCdEnum fromValue(String val) {
		if (val == null || val.isEmpty())
			return null;
		else {
			for (MailTypeCdEnum e : values())
				if (e.getValue().equals(val))
					return e;
		}
		return null;
	}
	
}
