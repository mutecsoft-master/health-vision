package com.mutecsoft.healthvision.common.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.mutecsoft.healthvision.common.model.FoodDiary;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FoodDiaryDto {

	@Getter
	@Setter
	@ToString
    public static class FoodDiaryInsertRequest{
		
		@NotNull
		private LocalDateTime mealDt;
		
		@NotNull
		private String mealTypeCd;
		
		@Valid
		private List<FoodDiary> foodDiaryList;
		
    }
	
	@Getter
	@Setter
	@ToString
    public static class FoodDiaryUpdateRequest{
		
		@NotNull
		private LocalDateTime mealDt;
		
		@NotNull
		private String mealTypeCd;
		
		@Valid
		private FoodDiary foodDiary;
		
    }
	
	@Getter
	@Setter
	@ToString
	@NoArgsConstructor
    public static class FoodDiarySearchRequest{
		private Long userId;
		private Long foodDiaryId;
		
		
		private String searchType;
		private String date;
		private String yearMonth;
		private String fromDate;
		private String toDate;
		
		public FoodDiarySearchRequest(Long foodDiaryId) {
			this.foodDiaryId = foodDiaryId;
		}
    }
	
	@Getter
	@Setter
	@ToString
    public static class FoodDiaryResponse{
		private Long foodDiaryId;
	    private String foodNm;
	    private BigDecimal calories;
	    private Long foodFileId;
	    private String foodDesc;
	    private String mealTypeCd;
	    private LocalDateTime mealDt;
	    private Long userId;
	    private Long regId;
		private LocalDateTime regDt;
		private Long updId;
		private LocalDateTime updDt;
		
		private String foodImgUrl;
	    
    }
	
}
