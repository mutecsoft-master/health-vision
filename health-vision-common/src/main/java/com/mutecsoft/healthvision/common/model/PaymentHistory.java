package com.mutecsoft.healthvision.common.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PaymentHistory {

	private Long paymentHistoryId;
	private Long userId;
	private Long reportId;
	private String transactionId;
	private String transactionType;
	private String purchaseToken;
	private String productId;
	private BigDecimal amount;
	private String currency;
	private LocalDateTime paymentDt;
	private String refundReason;
	private LocalDateTime refundDt;
	private String platform;
	private String status;
	private LocalDateTime regDt;
	private LocalDateTime updDt;
    
}
