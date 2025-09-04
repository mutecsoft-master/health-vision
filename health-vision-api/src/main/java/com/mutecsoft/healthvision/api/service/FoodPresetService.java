package com.mutecsoft.healthvision.api.service;

import java.io.IOException;
import java.util.List;

import com.mutecsoft.healthvision.common.dto.FoodPresetDto.FoodPresetResponse;
import com.mutecsoft.healthvision.common.dto.FoodPresetDto.FoodPresetSearchRequest;
import com.mutecsoft.healthvision.common.model.FoodPreset;

public interface FoodPresetService {

	void insertFoodPreset(FoodPreset inserReq) throws IOException;

	List<FoodPresetResponse> getFoodPresetList(FoodPresetSearchRequest searchReq);

	void updateFoodPreset(FoodPreset foodPreset) throws IOException;

	FoodPresetResponse getFoodPreset(FoodPresetSearchRequest searchReq);

	void deleteFoodPreset(Long foodPresetId) throws IOException;

}
