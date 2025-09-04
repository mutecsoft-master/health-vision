package com.mutecsoft.healthvision.common.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PaymentHistoryDto {
	
	@Getter
	@Setter
	@ToString
	public static class SearchRequestForGoogleRdtn {
		private String purchaseToken;
		private String transactionType;
	}
	
	@Getter
	@Setter
	@ToString
	public static class SearchRequestForAppleRdtn {
		private String transactionId;
		private String transactionType;
	}
	
}
