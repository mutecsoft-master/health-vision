package com.mutecsoft.healthvision.common.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.mutecsoft.healthvision.common.annotation.LoginId;
import com.mutecsoft.healthvision.common.dto.FoodDiaryDto.FoodDiarySearchRequest;
import com.mutecsoft.healthvision.common.dto.HealthDto.HealthInfoRequest;
import com.mutecsoft.healthvision.common.model.FoodDiary;

@Mapper
public interface FoodDiaryMapper {

	@LoginId
	void updateFoodDiary(FoodDiary foodDiary);
	
	@LoginId
	void insertFoodDiaryList(List<FoodDiary> foodDiaryList);

	List<FoodDiary> selectFoodDiaryList(FoodDiarySearchRequest searchReq);

	FoodDiary selectFoodDiary(FoodDiarySearchRequest searchReq);

	void deleteFoodDiary(Long foodDiaryId);

	List<FoodDiary> selectFoodDiaryListForHealthInfo(HealthInfoRequest healthInfoReq);
	
}
