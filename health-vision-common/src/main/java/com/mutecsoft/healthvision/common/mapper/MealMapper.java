package com.mutecsoft.healthvision.common.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.mutecsoft.healthvision.common.annotation.LoginId;
import com.mutecsoft.healthvision.common.model.Meal;

@Mapper
public interface MealMapper {

	@LoginId
	void insertMeal(Meal meal);

	List<Meal> selectMealList(Long userId);
	
}
