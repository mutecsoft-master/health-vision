package com.mutecsoft.healthvision.common.constant;

// 혈당 단위 코드
public enum BgUnitCdEnum {
    MG_DL("mgPerDeciliter", "mg/dL"),
    MMOL_L("mmolPerLiter", "mmol/L");

    private final String value;
    private final String desc;

    BgUnitCdEnum(final String newVale, final String descVal) {
        value = newVale;
        desc = descVal;
    }

    public String getValue() {
        return value;
    }

    public String getDescValue() {
        return desc;
    }

    public static BgUnitCdEnum fromValue(String val) {
        if (val == null || val.isEmpty())
            return null;
        else {
            for (BgUnitCdEnum e : values())
                if (e.getValue().equals(val))
                    return e;
        }
        return null;
    }
}
