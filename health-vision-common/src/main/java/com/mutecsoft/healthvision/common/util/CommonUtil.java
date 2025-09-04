package com.mutecsoft.healthvision.common.util;

import java.security.SecureRandom;
import java.time.LocalDateTime;

import org.apache.commons.text.RandomStringGenerator;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.stereotype.Component;

import com.mutecsoft.healthvision.common.constant.ResultCdEnum;
import com.mutecsoft.healthvision.common.exception.CustomException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CommonUtil {
	
	private static final String ALLOWED_CHARS_NUMERIC = "0123456789";
	private static final String ALLOWED_CHARS_UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final String ALLOWED_CHARS_LOWER = "abcdefghijklmnopqrstuvwxyz";
	private static final String ALLOWED_CHARS_SPECIAL = "!@#$%^&*()_+-=[]{}|;:,.?"; //꺽쇠(<, >)는 html 태그로 인식될 가능성이 있어 제외
	
	//인증 코드 생성(숫자)
	public String generateNumberCode(int length) {
		if (length <= 0) {
	        throw new CustomException(ResultCdEnum.E001.getValue());
	    }

	    RandomStringGenerator generator = new RandomStringGenerator.Builder()
	        .withinRange('0', '9')
	        .get();

	    return generator.generate(length);
		
	}
	
	//인증 만료 시간 생성
	public LocalDateTime calcExpiryTime(int seconds) {
	    LocalDateTime now = LocalDateTime.now();
	    return now.plusSeconds(seconds);
	}
	
	//현재 시간으로 만료되었는지 확인
	public boolean isExpired(LocalDateTime expiredDt) {
		 return expiredDt.isBefore(LocalDateTime.now());
	}

	
	//인증 코드 생성(문자, 숫자, 특수문자)
	public String generateStringCode(int length) {
		
		final String ALLOWED_CHARS = ALLOWED_CHARS_NUMERIC +
                ALLOWED_CHARS_UPPER +
                ALLOWED_CHARS_LOWER +
                ALLOWED_CHARS_SPECIAL;
		
		SecureRandom random = new SecureRandom();
		
		StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(ALLOWED_CHARS.length());
            sb.append(ALLOWED_CHARS.charAt(index));
        }
        return sb.toString();
	}
	
	//회원 탈퇴(삭제) 시 email 변환 접미사
	public static String makeDeleteUserSuffix() {
		return "_deleted_" + System.currentTimeMillis() / 1000;
	}
	
	
	
	//Html -> Plain Text
	public static String htmlToPlainText(String html) {
		if (html == null || html.isEmpty()) {
            return "";
        }
		
		return Jsoup.clean(html, Safelist.none());
	}
	
	public static String truncateWithEllipsis(String input, int maxLength) {
	    if (input == null || maxLength <= 0) return "";
	    return input.length() <= maxLength ? input : input.substring(0, maxLength) + "...";
	}
}
