package com.mutecsoft.healthvision.common.constant;

public class Const {
	
	//공통
	public static final String APP_NAME = "health-vision";
	
	public static final String TOKEN_REQUEST_HEADER = "Authorization";
	public static final String TOKEN_REQUEST_HEADER_PREFIX = "Bearer";
	
	public static final String ROLE_PREFIX = "ROLE_";
	public static final String ROLE_ADMIN = "ADMIN";
	public static final String ROLE_ANALYST = "ANALYST";
	public static final String ROLE_USER = "USER";
	
	//즐겨먹기 최대 개수
	public static final int FOOD_PRESET_MAX_CNT = 10;
	
	//이메일 인증번호 자릿수
	public static final int MAIL_AUTH_CODE_LENGTH = 6;
	
	//이메일 인증 유효시간
	public static final int MAIL_AUTH_CODE_EXPIRED_TIME = 5 * 60; //5분 
	public static final int MAIL_AUTH_SIGNUP_EXPIRED_TIME = 30 * 60; //30분
	
	//로그인 시도 최대 횟수
	public static final int MAX_LOGIN_ATTEMPT_CNT = 5;
	public static final int LOGIN_ATTEMPT_WAIT_HOURS = 2; //2시간 
	
	//비밀번호 찾기 시도 최대 횟수
	public static final int MAX_PW_FIND_ATTEMPT_CNT = 5;
	public static final int PW_FIND_ATTEMPT_WAIT_HOURS = 2; //2시간
	
	//비밀번호 찾기 인증번호 자릿수
	public static final int MAIL_FIND_PW_CODE_LENGTH = 10;
	
	//임시 비밀번호 유효시간
	public static final int TEMP_PW_EXPIRED_TIME = 10 * 60; //10분
	
	//닉네임 Prefix
	public static final String DEFAULT_NICKNAME_PREFIX = "HV"; //HealthVision 약자
	
	
	
	
	//운동량 기준
	public static final int STANDARD_WORKOUT_DURATION_MINUTES = 150; //150분
	//수면시간 기준
	public static final int STANDARD_SLEEP_HOURS = 7; //7시간
	//심박수 기준
	public static final int STANDARD_HEART_RATE_CNT = 50; //50회
	//식이다이어리 기준
	public static final int STANDARD_FOOD_DIARY_CNT = 3; //3회 (아침, 점심, 저녁)
	
	
	
	
	//최신 혈당파일 목록 개수
	public static final Integer RECENT_BG_FILE_LIST_CNT = 3;
	
	//최신 리포트 조회 기준 (1달)
	public static final Integer RECENT_REPORT_DURATION_IN_MONTHS = 1;
	
	
	//Google RTDN 알림 관련 key 값
	public static final String GOOGLE_RTDN_NOTI_ONE_TIME = "oneTimeProductNotification"; //일회성 구매 관련 알림
	public static final String GOOGLE_RTDN_NOTI_VOIDED = "voidedPurchaseNotification"; //구매 무효 알림
	
	
	
	
	//Admin
	public static final String EXCEL_DATA_ZIP_FILE_NM_PREFIX = "HG_DATA_";
	
	//FCM 최대 발송 갯수
	public static final int MAX_FCM_TOKEN_COUNT = 500;

	//약관 내용 출력 글자수
	public static final int TERMS_DISPLAY_MAX_LENGTH = 30;
}
