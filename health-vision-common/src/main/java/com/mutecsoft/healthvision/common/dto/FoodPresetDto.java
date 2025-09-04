package com.mutecsoft.healthvision.common.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FoodPresetDto {
	
	@Getter
	@Setter
	@ToString
	@NoArgsConstructor
    public static class FoodPresetSearchRequest{
		private Long foodPresetId;
		private Long userId;
		
		public FoodPresetSearchRequest(Long foodPresetId) {
			this.foodPresetId = foodPresetId;
		}
    }

	@Getter
	@Setter
	@ToString
    public static class FoodPresetResponse{
		private Long foodPresetId;
	    private String foodNm;
	    private BigDecimal calories;
	    private Long foodFileId;
	    private Long userId;
	    private Long regId;
		private LocalDateTime regDt;
		private Long updId;
		private LocalDateTime updDt;
		
		private String foodImgUrl;
	    
    }
	
}
