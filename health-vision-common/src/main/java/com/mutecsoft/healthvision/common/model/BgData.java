package com.mutecsoft.healthvision.common.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class BgData extends BaseModel {
    private Long bgDataId;
    private BigDecimal value;
    private String unit;
    private LocalDateTime recordDt;
    private Long deviceId;
    private Long userId;
    private Long bgFileId;
}
