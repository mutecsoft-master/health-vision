package com.mutecsoft.healthvision.common.constant;

// 파일 확장자 코드
public enum FileExtenstionCdEnum {
    CSV("csv", "CSV File"), // CSV 파일
    TSV("tsv", "TSV File"); // TSV 파일

    private final String value;
    private final String desc;

    FileExtenstionCdEnum(final String newVale, final String descVal) {
        value = newVale;
        desc = descVal;
    }

    public String getValue() {
        return value;
    }

    public String getDescValue() {
        return desc;
    }

    public static FileExtenstionCdEnum fromValue(String val) {
        if (val == null || val.isEmpty())
            return null;
        else {
            for (FileExtenstionCdEnum e : values())
                if (e.getValue().equals(val))
                    return e;
        }
        return null;
    }
}
