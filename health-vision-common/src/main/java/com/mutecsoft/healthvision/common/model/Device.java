package com.mutecsoft.healthvision.common.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Device extends BaseModel{
    private Long deviceId;
    private String deviceNm;
    private String manufacturer;
    private String model;
    private String hwVersion;
    private String swVersion;
    private String fwVersion;
    private String localId;
}
