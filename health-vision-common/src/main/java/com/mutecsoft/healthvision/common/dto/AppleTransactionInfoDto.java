package com.mutecsoft.healthvision.common.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppleTransactionInfoDto {

    private String transactionId;
    private String originalTransactionId;
    private String bundleId;
    private String productId;
    private Long purchaseDate;
    private Long originalPurchaseDate;
    private Integer quantity;
    private String type;
    private String inAppOwnershipType;
    private Long signedDate;
    private String environment;
    private String transactionReason;
    private String storefront;
    private String storefrontId;
    private Integer price;
    private String currency;
    private String appTransactionId;
    private Long revocationDate;
    private Integer revocationReason;
    
}