package com.mutecsoft.healthvision.api.util;

import java.math.BigDecimal;

public class NumberUtils {

    // String이 Decimal로 변환이 가능한지 확인하는 API
    public static boolean isNumeric(String value) {
        try {
            new BigDecimal(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
