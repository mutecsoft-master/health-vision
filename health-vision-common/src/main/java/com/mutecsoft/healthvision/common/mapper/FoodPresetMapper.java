package com.mutecsoft.healthvision.common.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.mutecsoft.healthvision.common.annotation.LoginId;
import com.mutecsoft.healthvision.common.dto.FoodPresetDto.FoodPresetSearchRequest;
import com.mutecsoft.healthvision.common.model.FoodPreset;

@Mapper
public interface FoodPresetMapper {

	@LoginId
	void insertFoodPreset(FoodPreset foodPreset);

	List<FoodPreset> selectFoodPresetList(FoodPresetSearchRequest searchReq);

	FoodPreset selectFoodPreset(FoodPresetSearchRequest searchReq);

	@LoginId
	void updateFoodPreset(FoodPreset foodPreset);

	void deleteFoodPreset(Long foodPresetId);

	int selectFoodPresetCount(FoodPresetSearchRequest searchReq);
	
}
