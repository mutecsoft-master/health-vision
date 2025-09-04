package com.mutecsoft.healthvision.common.constant;

// BG File 형식 코드
public enum BgFileTypeCdEnum {
    MEDTRONIC("MEDTRONIC", "MEDTRONIC"), // medtronic 기기 혈당 데이터 파일 형식
    DEXCOM("DEXCOM", "DEXCOM"),          // dexcom 기기 혈당 데이터 파일 형식
    LIBRE("LIBRE", "LIBRE"),
    UNKNOWN("UNKNOWN", "UNKNOWN");       // 파일 형식을 찾을 수 없음.

    private final String value;
    private final String desc;


    BgFileTypeCdEnum(final String newValue, final String descVal) {
        value = newValue;
        desc = descVal;
    }

    public String getValue() {
        return value;
    }

    public String getDescValue() {
        return desc;
    }
}

