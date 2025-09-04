package com.mutecsoft.healthvision.api.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.mutecsoft.healthvision.api.service.FileService;
import com.mutecsoft.healthvision.api.service.FoodDiaryService;
import com.mutecsoft.healthvision.api.util.UserUtil;
import com.mutecsoft.healthvision.common.constant.FileCateCdEnum;
import com.mutecsoft.healthvision.common.constant.ResultCdEnum;
import com.mutecsoft.healthvision.common.dto.FileDto.FileInsertDto;
import com.mutecsoft.healthvision.common.dto.FoodDiaryDto.FoodDiaryInsertRequest;
import com.mutecsoft.healthvision.common.dto.FoodDiaryDto.FoodDiaryResponse;
import com.mutecsoft.healthvision.common.dto.FoodDiaryDto.FoodDiarySearchRequest;
import com.mutecsoft.healthvision.common.dto.FoodDiaryDto.FoodDiaryUpdateRequest;
import com.mutecsoft.healthvision.common.dto.UserDto.UserInfo;
import com.mutecsoft.healthvision.common.exception.CustomException;
import com.mutecsoft.healthvision.common.mapper.FoodDiaryMapper;
import com.mutecsoft.healthvision.common.model.FileModel;
import com.mutecsoft.healthvision.common.model.FoodDiary;
import com.mutecsoft.healthvision.common.util.FileUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FoodDiaryServiceImpl implements FoodDiaryService{
	
	private final ModelMapper modelMapper;
	private final FoodDiaryMapper foodDiaryMapper;
	private final FileService fileService;
	private final FileUtil fileUtil;
	private final UserUtil userUtil;
	
	@Transactional
	@Override
	public void insertFoodDiary(FoodDiaryInsertRequest insertReq) throws IOException {
		
		List<FoodDiary> foodDiaryList = insertReq.getFoodDiaryList();
		if(foodDiaryList != null) {
			//1. 파일저장
			//keyIndex : 파일순서와 식이다이어리 순서 맞추기 위한 용도
			List<FileInsertDto> fileList = IntStream.range(0, foodDiaryList.size())
				    .mapToObj(i -> new FileInsertDto(i, foodDiaryList.get(i).getFoodFile(), FileCateCdEnum.FOOD.getValue()))
				    .collect(Collectors.toList());
			
			List<FileModel> fileModelList = fileUtil.saveFileList(fileList);
			
			//2. 파일데이터 DB 저장
			for(FileModel fm : fileModelList) {
				fileService.insertFile(fm);
			}
			
			for(int i=0; i<foodDiaryList.size(); i++) {
				FoodDiary foodDiary = foodDiaryList.get(i);
				
				//mealTypeCd, mealDt만 1개만 받기 때문에 리스트에 각각 세팅
				foodDiary.setMealTypeCd(insertReq.getMealTypeCd());
				foodDiary.setMealDt(insertReq.getMealDt());
				
				//파일 id 세팅
				for(FileModel fileModel : fileModelList) {
					if(i == fileModel.getKeyIndex()) {
						foodDiary.setFoodFileId(fileModel.getFileId());
						break;
					}
				}
			}
			
			//3. foodDiaryList DB 저장
			foodDiaryMapper.insertFoodDiaryList(foodDiaryList);
		}
		
	}

	@Override
	public List<FoodDiaryResponse> getFoodDiaryList(FoodDiarySearchRequest searchReq) {
		List<FoodDiary> foodDiaryList = foodDiaryMapper.selectFoodDiaryList(searchReq);
		
		List<FoodDiaryResponse> dtoList = foodDiaryList.stream()
			.map(f -> {
	            FoodDiaryResponse dto = modelMapper.map(f, FoodDiaryResponse.class);
	            dto.setFoodImgUrl(fileUtil.makeImgApiUrl(dto.getFoodFileId()));
	            return dto;
	        })
			.collect(Collectors.toList());
			
		return dtoList;
	}

	@Override
	public FoodDiaryResponse getFoodDiary(FoodDiarySearchRequest searchReq) {
		FoodDiary foodDiary = foodDiaryMapper.selectFoodDiary(searchReq);
		
		if(foodDiary == null) {
			return null;
		}
		
		FoodDiaryResponse dto = modelMapper.map(foodDiary, FoodDiaryResponse.class);
		dto.setFoodImgUrl(fileUtil.makeImgApiUrl(dto.getFoodFileId()));
		
		return dto;
	}
	
	

	@Transactional
	@Override
	public void updateFoodDiary(FoodDiaryUpdateRequest updateReq) throws IOException {
		
		UserInfo userInfo = userUtil.getUserInfo();
		
		FoodDiarySearchRequest searchReq = new FoodDiarySearchRequest(updateReq.getFoodDiary().getFoodDiaryId());
		searchReq.setUserId(userInfo.getUserId());
		FoodDiary originFoodDiary = foodDiaryMapper.selectFoodDiary(searchReq);
		
		if(originFoodDiary == null) {
			throw new CustomException(ResultCdEnum.E003.getValue());
		}
		
		FoodDiary updateFoodDiary = updateReq.getFoodDiary();
		
		//새로운 음식 사진을 업로드 할 때에만 업데이트 
		if(updateFoodDiary.getFoodFile() != null 
				&& !updateFoodDiary.getFoodFile().isEmpty() 
				&& StringUtils.hasText(updateFoodDiary.getFoodFile().getOriginalFilename())) {
			
			//기존 파일 삭제
			if(originFoodDiary.getFoodFileId() != null) {
				fileService.deleteFile(originFoodDiary.getFoodFileId());
			}
			
			//1. 파일 저장
			FileInsertDto fileDto = new FileInsertDto(0, updateFoodDiary.getFoodFile(), FileCateCdEnum.FOOD.getValue());
			FileModel fileModel = fileUtil.saveFile(fileDto);

			//2. 파일데이터 DB 저장
			if(StringUtils.hasText(fileModel.getFilePath())) {
				fileService.insertFile(fileModel);
				updateFoodDiary.setFoodFileId(fileModel.getFileId());
			}
		}else {
			updateFoodDiary.setFoodFileId(originFoodDiary.getFoodFileId());
		}
		
		updateFoodDiary.setMealTypeCd(updateReq.getMealTypeCd());
		updateFoodDiary.setMealDt(updateReq.getMealDt());
		foodDiaryMapper.updateFoodDiary(updateFoodDiary);
	}

	@Transactional
	@Override
	public void deleteFoodDiary(Long foodDiaryId) throws IOException {
		UserInfo userInfo = userUtil.getUserInfo();

		FoodDiarySearchRequest searchReq = new FoodDiarySearchRequest(foodDiaryId);
		searchReq.setUserId(userInfo.getUserId());
		FoodDiary foodDiary = foodDiaryMapper.selectFoodDiary(searchReq);
		
		if(foodDiary == null) {
			throw new CustomException(ResultCdEnum.E003.getValue());
		}
		
		//파일 삭제
		if(foodDiary.getFoodFileId() != null) {
			fileService.deleteFile(foodDiary.getFoodFileId());
		}
		
		foodDiaryMapper.deleteFoodDiary(foodDiaryId);
		
	}

}
