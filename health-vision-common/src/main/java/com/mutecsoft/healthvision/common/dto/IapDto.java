package com.mutecsoft.healthvision.common.dto;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class IapDto {
	
	@Getter
	@Setter
    public static class GooglePurchaseRequest {
		
		@NotNull
		private String packageName;
		
		@NotNull
		private String productId;
		
		@NotNull
		private String purchaseToken;
    }
	
	@Getter
	@Setter
    public static class ApplePurchaseRequest {
		@NotNull
		private String  transactionId;
    }

}
