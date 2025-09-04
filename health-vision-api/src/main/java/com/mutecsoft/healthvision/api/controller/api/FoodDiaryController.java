package com.mutecsoft.healthvision.api.controller.api;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mutecsoft.healthvision.api.service.FoodDiaryService;
import com.mutecsoft.healthvision.api.util.UserUtil;
import com.mutecsoft.healthvision.common.constant.FoodDiarySearchTypeEnum;
import com.mutecsoft.healthvision.common.dto.ResponseDto;
import com.mutecsoft.healthvision.common.dto.FoodDiaryDto.FoodDiaryInsertRequest;
import com.mutecsoft.healthvision.common.dto.FoodDiaryDto.FoodDiaryResponse;
import com.mutecsoft.healthvision.common.dto.FoodDiaryDto.FoodDiarySearchRequest;
import com.mutecsoft.healthvision.common.dto.FoodDiaryDto.FoodDiaryUpdateRequest;
import com.mutecsoft.healthvision.common.dto.UserDto.UserInfo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Food Diary", description = "식이 다이어리")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/food")
public class FoodDiaryController {

	private final FoodDiaryService foodDiaryService;
	private final UserUtil userUtil;
	
	@Operation(summary = "식이 다이어리 등록", description = "하나의 식사시간에 따른 다중 식이 등록")
	@PostMapping("/diary")
    public ResponseEntity<ResponseDto> insertFoodDiary(
    		@ModelAttribute("foodDiary") @Valid FoodDiaryInsertRequest inserReq
    		, HttpServletRequest request
            , HttpServletResponse response
    		) throws IOException {
		
		foodDiaryService.insertFoodDiary(inserReq);

        return ResponseEntity.ok(new ResponseDto(true));
    }
	
	@Operation(summary = "식이 다이어리 목록 조회(전체)", description = "전체 조회")
	@GetMapping("/diaryList")
	public ResponseEntity<ResponseDto> getFoodDiaryList(
			@RequestBody(required = false) FoodDiarySearchRequest searchReq
			, HttpServletRequest request
			, HttpServletResponse resopnse) {
		
		if(searchReq == null) {
			searchReq = new FoodDiarySearchRequest();
		}
		
		UserInfo userInfo = userUtil.getUserInfo();
		searchReq.setUserId(userInfo.getUserId());
		List<FoodDiaryResponse> foodDiaryList =  foodDiaryService.getFoodDiaryList(searchReq);
		
		ResponseDto responseDto = new ResponseDto(true, foodDiaryList);
		return ResponseEntity.ok(responseDto);
	}
	
	
	@Operation(summary = "식이 다이어리 목록 조회(일별)", description = "일별 조회")
	@GetMapping("/diaryList/daily")
	public ResponseEntity<ResponseDto> getFoodDiaryListDaily(
			@ModelAttribute FoodDiarySearchRequest searchReq
			, HttpServletRequest request
			, HttpServletResponse resopnse) {
		
		if(searchReq == null) {
			searchReq = new FoodDiarySearchRequest();
		}
		
		UserInfo userInfo = userUtil.getUserInfo();
		searchReq.setUserId(userInfo.getUserId());
		searchReq.setSearchType(FoodDiarySearchTypeEnum.DAILY.getValue());
		List<FoodDiaryResponse> foodDiaryList =  foodDiaryService.getFoodDiaryList(searchReq);
		
		ResponseDto responseDto = new ResponseDto(true, foodDiaryList);
		return ResponseEntity.ok(responseDto);
	}
	
	@Operation(summary = "식이 다이어리 목록 조회(주간)", description = "주간 조회")
	@GetMapping("/diaryList/weekly")
	public ResponseEntity<ResponseDto> getFoodDiaryListWeekly(
			@ModelAttribute FoodDiarySearchRequest searchReq
			, HttpServletRequest request
			, HttpServletResponse resopnse) {
		
		if(searchReq == null) {
			searchReq = new FoodDiarySearchRequest();
		}
		
		UserInfo userInfo = userUtil.getUserInfo();
		searchReq.setUserId(userInfo.getUserId());
		searchReq.setSearchType(FoodDiarySearchTypeEnum.WEEKLY.getValue());
		
		LocalDate date = LocalDate.parse(searchReq.getDate());
        LocalDate fromDate = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
        LocalDate toDate = date.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY));
        searchReq.setFromDate(fromDate.toString());
        searchReq.setToDate(toDate.toString());
		
		List<FoodDiaryResponse> foodDiaryList =  foodDiaryService.getFoodDiaryList(searchReq);
		
		ResponseDto responseDto = new ResponseDto(true, foodDiaryList);
		return ResponseEntity.ok(responseDto);
	}
	
	@Operation(summary = "식이 다이어리 목록 조회(월별)", description = "월별 조회")
	@GetMapping("/diaryList/monthly")
	public ResponseEntity<ResponseDto> getFoodDiaryListMonthly(
			@ModelAttribute FoodDiarySearchRequest searchReq
			, HttpServletRequest request
			, HttpServletResponse resopnse) {
		
		if(searchReq == null) {
			searchReq = new FoodDiarySearchRequest();
		}
		
		UserInfo userInfo = userUtil.getUserInfo();
		searchReq.setUserId(userInfo.getUserId());
		searchReq.setSearchType(FoodDiarySearchTypeEnum.MONTHLY.getValue());
		
		List<FoodDiaryResponse> foodDiaryList =  foodDiaryService.getFoodDiaryList(searchReq);
		
		ResponseDto responseDto = new ResponseDto(true, foodDiaryList);
		return ResponseEntity.ok(responseDto);
	}
	
	
	@Operation(summary = "식이 다이어리 조회(단건)", description = "단건 조회")
	@GetMapping("/diary/{foodDiaryId}")
	public ResponseEntity<ResponseDto> getFoodDiary(
			@PathVariable("foodDiaryId") String foodDiaryId
			, HttpServletRequest request
			, HttpServletResponse resopnse) {

		FoodDiarySearchRequest searchReq = new FoodDiarySearchRequest(Long.parseLong(foodDiaryId));
		UserInfo userInfo = userUtil.getUserInfo();
		searchReq.setUserId(userInfo.getUserId());
		FoodDiaryResponse foodDiary =  foodDiaryService.getFoodDiary(searchReq);
		
		ResponseDto responseDto = new ResponseDto(true, foodDiary);
		return ResponseEntity.ok(responseDto);
		
	}
	
	@Operation(summary = "식이 다이어리 수정", description = "단건 수정")
	@PutMapping("/diary/{foodDiaryId}")
    public ResponseEntity<ResponseDto> updateFoodDiary(
    		@PathVariable("foodDiaryId") String foodDiaryId
    		, @ModelAttribute("foodDiary") @Valid FoodDiaryUpdateRequest updateReq
    		, HttpServletRequest request
            , HttpServletResponse response
    		) throws IOException {
		
		updateReq.getFoodDiary().setFoodDiaryId(Long.parseLong(foodDiaryId));
		foodDiaryService.updateFoodDiary(updateReq);

        return ResponseEntity.ok(new ResponseDto(true));
    }
	
	@Operation(summary = "식이 다이어리 삭제", description = "단건 삭제")
	@DeleteMapping("/diary/{foodDiaryId}")
    public ResponseEntity<ResponseDto> deleteFoodDiary(
    		@PathVariable("foodDiaryId") String foodDiaryId
    		, HttpServletRequest request
            , HttpServletResponse response
    		) throws IOException {
		
		foodDiaryService.deleteFoodDiary(Long.parseLong(foodDiaryId));

        return ResponseEntity.ok(new ResponseDto(true));
    }
	
}
