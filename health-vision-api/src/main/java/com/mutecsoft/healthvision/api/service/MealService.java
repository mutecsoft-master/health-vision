package com.mutecsoft.healthvision.api.service;

import java.io.IOException;
import java.util.List;

import com.mutecsoft.healthvision.common.dto.MealDto.MealResponse;
import com.mutecsoft.healthvision.common.model.Meal;

public interface MealService {

	void insertMeal(Meal meal) throws IOException;

	List<MealResponse> selectMealList(Long userId);

	

}
