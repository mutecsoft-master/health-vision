package com.mutecsoft.healthvision.api.controller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mutecsoft.healthvision.api.service.MealService;
import com.mutecsoft.healthvision.common.dto.ResponseDto;
import com.mutecsoft.healthvision.common.model.Meal;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Meal", description = "식사관리")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/meal")
public class MealController {
	
	private final MealService mealService;

	@Operation(summary = "식사 등록", description = "식사 등록")
	@PostMapping
	public ResponseEntity<ResponseDto> insertMeal(@RequestBody Meal meal) {
		
		mealService.insertMeal(meal);
		
		return ResponseEntity.ok(new ResponseDto(true));
	}
	
	
	
}
