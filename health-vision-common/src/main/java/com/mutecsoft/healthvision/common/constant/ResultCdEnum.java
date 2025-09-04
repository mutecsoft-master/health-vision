package com.mutecsoft.healthvision.common.constant;

//value : 클라이언트에서 사용할 코드
//desc : 현재 직접 사용되는 부분 없음. 추후 확장을 위해 정의 
public enum ResultCdEnum {
	
	//정상 동작
	S001("S001", "Already exists account"), //이미 존재하는 계정
	S002("S002", "Code not matched"), //인증 코드 불일치
	S003("S003", "Code expired"), //인증 코드 만료
	S004("S004", "Mail verify expired"), //메일 인증 만료
	S005("S005", "Account not found"), //계정 없음
	S006("S006", "Nickname is duplicated"), //닉네임 중복

	//일반 오류
	E001("E001", "Internal Server Error"),
	E002("E002", "Access denied"), //권한 없음
	E003("E003", "Resource not found"), //자원 찾지 못함 (파일 등)
	E004("E004", "File upload failed"), //파일 업로드 실패
	E005("E005", "Mail send failed"), //메일 발송 실패
	E006("E006", "Request parameter validation failed"), //파라미터 유효성 검사 실패
	E007("E007", "Mail verification failed"), //메일 인증 실패
	E008("E008", "Purchase verification failed"), //결제 검증 실패

	//BG File 관련
	E101("E101", "Invalid file extension"),	// 파일 확장자가 맞지 않음.
	E102("E102", "Invalid file format"),	// 올바른 파일 형식이 아님.
	E103("E103", "Missing file header"),	// 파일의 헤더를 찾을 수 없음.
	E104("E104", "Missing required file column"),	// 파일의 컬럼을 찾을 수 없음.
	E105("E105", "Failed to extract data from the file"),	// 파일 데이터 추출 실패
	E106("E106", "Failed to extract Device Info"),	// Device 정보 추출 실패
	E107("E107", "I/O operation failed"), // 파일 읽기/쓰기 실패
	E108("E108", "Invalid CSV format"), // CSV 파싱 실패

	//Login 관련
	//일반 로그인
	L001("L001", "Authentication failed"), //인증 실패
	L002("L002", "Login attempt limit exceeded"), //로그인 시도 제한 횟수 초과
	L003("L003", "Invalid token"), //유효하지 않은 토큰
	L004("L004", "Password find attempt limit exceeded"), //비밀번호 찾기 시도 제한 횟수 초과
	
	L005("L005", "Mail authentication attempt limit exceeded"), //메일 인증 시도 제한 횟수 초과
	
	//SNS 로그인
	L101("L101", "Google login failed"),
	L102("L102", "Apple login failed"),
	
	//식이 관련
	F001("F001", "Food preset limit"), //즐겨먹기 최대 갯수 초과
	
	//리포트 관련
	R001("R001", "The report already exists"), //이미 신청한 리포트
	
	//유효성 검사 관련
	V001("V001", "Mail address validation failed"),  //메일 주소 유효성 검사 실패
	V002("V002", "Password validation failed"),  //비밀번호 유효성 검사 실패
	
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
