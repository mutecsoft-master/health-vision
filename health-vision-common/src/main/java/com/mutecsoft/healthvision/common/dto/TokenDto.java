package com.mutecsoft.healthvision.common.dto;

import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;

public class TokenDto {

	
	@Getter
	@Setter
    public static class FcmTokenInsertRequest{
		@NotEmpty
		private String fcmToken;
    }
	
	@Getter
	@Setter
    public static class FcmTokenUpdateRequest{
		@NotEmpty
		private String oldFcmToken;
		
		@NotEmpty
		private String newFcmToken;
    }
}
