package com.mutecsoft.healthvision.api.controller.api;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mutecsoft.healthvision.api.service.FoodPresetService;
import com.mutecsoft.healthvision.api.util.UserUtil;
import com.mutecsoft.healthvision.common.dto.ResponseDto;
import com.mutecsoft.healthvision.common.dto.FoodPresetDto.FoodPresetResponse;
import com.mutecsoft.healthvision.common.dto.FoodPresetDto.FoodPresetSearchRequest;
import com.mutecsoft.healthvision.common.dto.UserDto.UserInfo;
import com.mutecsoft.healthvision.common.model.FoodPreset;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Food Preset", description = "즐겨먹기")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/food")
public class FoodPresetController {

	private final FoodPresetService foodPresetService;
	private final UserUtil userUtil;
	
	@Operation(summary = "즐겨먹기 등록", description = "즐겨먹기 등록")
	@PostMapping("/preset")
    public ResponseEntity<ResponseDto> insertFoodPreset(
    		@ModelAttribute("foodPreset") @Valid FoodPreset foodPreset
    		, HttpServletRequest request
            , HttpServletResponse response
    		) throws IOException {
		
		foodPresetService.insertFoodPreset(foodPreset);

        return ResponseEntity.ok(new ResponseDto(true));
    }
	
	@Operation(summary = "즐겨먹기 목록 조회", description = "즐겨먹기 목록 조회")
	@GetMapping("/presetList")
	public ResponseEntity<ResponseDto> getFoodPresetList(
			HttpServletRequest request
			, HttpServletResponse resopnse) {
		
		UserInfo userInfo = userUtil.getUserInfo();
		FoodPresetSearchRequest searchReq = new FoodPresetSearchRequest();
		searchReq.setUserId(userInfo.getUserId());
		
		List<FoodPresetResponse> foodPresetList =  foodPresetService.getFoodPresetList(searchReq);
		
		ResponseDto responseDto = new ResponseDto(true, foodPresetList);
		return ResponseEntity.ok(responseDto);
	}
	
	@Operation(summary = "즐겨먹기 수정", description = "즐겨먹기 수정")
	@PutMapping("/preset/{foodPresetId}")
	public ResponseEntity<ResponseDto> updateFoodPreset(
			@PathVariable("foodPresetId") String foodPresetId
			, @ModelAttribute("foodPreset") @Valid FoodPreset foodPreset
			, HttpServletRequest request
			  , HttpServletResponse response
			) throws IOException {
		
		foodPreset.setFoodPresetId(Long.parseLong(foodPresetId));
		foodPresetService.updateFoodPreset(foodPreset);

		return ResponseEntity.ok(new ResponseDto(true));
	}
	
	@Operation(summary = "즐겨먹기 조회", description = "즐겨먹기 조회")
	@GetMapping("/preset/{foodPresetId}")
	public ResponseEntity<ResponseDto> getFoodPreset(
			@PathVariable("foodPresetId") String foodPresetId
			, HttpServletRequest request
			, HttpServletResponse resopnse) {

		FoodPresetSearchRequest searchReq = new FoodPresetSearchRequest(Long.parseLong(foodPresetId));
		UserInfo userInfo = userUtil.getUserInfo();
		searchReq.setUserId(userInfo.getUserId());
		FoodPresetResponse foodPreset =  foodPresetService.getFoodPreset(searchReq);
		
		ResponseDto responseDto = new ResponseDto(true, foodPreset);
		return ResponseEntity.ok(responseDto);
		
	}
	
	@Operation(summary = "즐겨먹기 삭제", description = "즐겨먹기 삭제")
	@DeleteMapping("/preset/{foodPresetId}")
    public ResponseEntity<ResponseDto> deleteFoodPreset(
    		@PathVariable("foodPresetId") String foodPresetId
    		, HttpServletRequest request
            , HttpServletResponse response
    		) throws IOException {
		
		foodPresetService.deleteFoodPreset(Long.parseLong(foodPresetId));

        return ResponseEntity.ok(new ResponseDto(true));
    }
	
}
