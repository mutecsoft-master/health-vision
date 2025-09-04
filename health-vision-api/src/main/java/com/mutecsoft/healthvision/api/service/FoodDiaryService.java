package com.mutecsoft.healthvision.api.service;

import java.io.IOException;
import java.util.List;

import com.mutecsoft.healthvision.common.dto.FoodDiaryDto.FoodDiaryInsertRequest;
import com.mutecsoft.healthvision.common.dto.FoodDiaryDto.FoodDiaryResponse;
import com.mutecsoft.healthvision.common.dto.FoodDiaryDto.FoodDiarySearchRequest;
import com.mutecsoft.healthvision.common.dto.FoodDiaryDto.FoodDiaryUpdateRequest;

public interface FoodDiaryService {

	void insertFoodDiary(FoodDiaryInsertRequest inserReq) throws IOException;

	List<FoodDiaryResponse> getFoodDiaryList(FoodDiarySearchRequest searchReq);

	FoodDiaryResponse getFoodDiary(FoodDiarySearchRequest searchReq);

	void updateFoodDiary(FoodDiaryUpdateRequest updateReq) throws IOException;

	void deleteFoodDiary(Long foodDiaryId) throws IOException;


}
