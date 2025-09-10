package com.mutecsoft.healthvision.common.converter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;

import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.mutecsoft.healthvision.common.exception.CustomException;

//@ModelAttribute 에서 yyyy-MM-dd'T'HH:mm:ssZ 형태의 문자열 변환용 ('Z'문자열 변환하기 위해 사용)
@Component
public class StringToLocalDateTimeConverter implements Converter<String, LocalDateTime> {

    @Override
    public LocalDateTime convert(String source) {
        try {
            Instant instant = Instant.parse(source);
            return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        } catch (DateTimeParseException e) {
        	//CustomException은 처리과정 중 소비되고, 실제로는 BindException 발생 
        	throw new CustomException("복약정보를 찾을 수 없습니다.", HttpStatus.BAD_REQUEST);
        }
    }
}