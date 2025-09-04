package com.mutecsoft.healthvision.common.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
public class CsvDto {

    @Getter
    @Setter
    @ToString
    public static class BgDataDto {
        private LocalDateTime timestamp;
        private BigDecimal glucoseValue;
    }

    @Getter
    @Setter
    @ToString
    public static class BgFileParseResult  {
        List<BgDataDto> bgDataList;
        String deviceName;
        String unit;
        LocalDate recordStartDate;
        LocalDate recordEndDate;
    }
}
