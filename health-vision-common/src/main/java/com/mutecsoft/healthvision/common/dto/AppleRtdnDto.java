package com.mutecsoft.healthvision.common.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AppleRtdnDto {

    private String notificationType;
    private String subtype;
    private Data data;
    private String version;
    private Long signedDate;
    private String notificationUUID;
    
    @Getter
    @Setter
    @ToString
    public static class Data {
    	private Long appAppleId;
    	private String bundleId;
    	private String bundleVersion;
    	private String consumptionRequestReason;
    	private String environment;
    	private String signedTransactionInfo;
    }
    
}