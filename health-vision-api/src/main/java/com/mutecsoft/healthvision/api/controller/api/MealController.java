package com.mutecsoft.healthvision.api.controller.api;

import java.io.IOException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mutecsoft.healthvision.api.service.MealService;
import com.mutecsoft.healthvision.api.util.UserUtil;
import com.mutecsoft.healthvision.common.dto.MealDto.MealResponse;
import com.mutecsoft.healthvision.common.dto.ResponseDto;
import com.mutecsoft.healthvision.common.dto.UserDto.UserInfo;
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
	private final UserUtil userUtil;

	@Operation(summary = "식사 등록", description = "식사 등록")
	@PostMapping
	public ResponseEntity<ResponseDto> insertMeal(@ModelAttribute Meal meal) throws IOException {
		
		mealService.insertMeal(meal);
		
		return ResponseEntity.ok(new ResponseDto(true));
	}
	
	@Operation(summary = "식사 목록 조회", description = "식사 목록 조회")
	@GetMapping("/list")
	public ResponseEntity<ResponseDto> selectMealList() {
		
		UserInfo userInfo = userUtil.getUserInfo();
		List<MealResponse> list = mealService.selectMealList(userInfo.getUserId());
		
		return ResponseEntity.ok(new ResponseDto(true, list));
	}
	
}
