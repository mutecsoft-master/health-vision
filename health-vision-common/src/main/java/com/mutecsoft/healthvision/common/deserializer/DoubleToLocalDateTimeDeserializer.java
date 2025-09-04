package com.mutecsoft.healthvision.common.deserializer;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class DoubleToLocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

    @Override
    public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        // double 값을 LocalDateTime로 변환
        double value = p.getDoubleValue();
        
        long seconds = (long) value;  // 초 부분
        int nanos = (int) ((value - seconds) * 1_000_000_000);  // 나노초

        // Instant 객체 생성
        Instant instant = Instant.ofEpochSecond(seconds, nanos);
        return LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
    }
}