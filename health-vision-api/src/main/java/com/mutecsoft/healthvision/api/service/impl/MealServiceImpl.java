package com.mutecsoft.healthvision.api.service.impl;

import org.springframework.stereotype.Service;

import com.mutecsoft.healthvision.api.service.MealService;
import com.mutecsoft.healthvision.common.mapper.MealMapper;
import com.mutecsoft.healthvision.common.model.Meal;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MealServiceImpl implements MealService {

    private final MealMapper mealMapper;
    
	@Override
	public void insertMeal(Meal meal) {
		
		mealMapper.insertMeal(meal);
		
	}

}
