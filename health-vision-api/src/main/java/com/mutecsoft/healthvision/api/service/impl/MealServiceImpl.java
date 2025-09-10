package com.mutecsoft.healthvision.api.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mutecsoft.healthvision.api.service.FileService;
import com.mutecsoft.healthvision.api.service.MealService;
import com.mutecsoft.healthvision.common.constant.FileCateCdEnum;
import com.mutecsoft.healthvision.common.dto.FileDto.FileInsertDto;
import com.mutecsoft.healthvision.common.dto.MealDto.MealResponse;
import com.mutecsoft.healthvision.common.mapper.MealMapper;
import com.mutecsoft.healthvision.common.model.FileModel;
import com.mutecsoft.healthvision.common.model.Meal;
import com.mutecsoft.healthvision.common.util.FileUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MealServiceImpl implements MealService {

	private final FileService fileService;
	private final ModelMapper modelMapper;
	private final FileUtil fileUtil;
    private final MealMapper mealMapper;
    
    @Transactional
	@Override
	public void insertMeal(Meal meal) throws IOException {
		
		if(meal.getFile() != null) {
			//1. 파일저장
			FileInsertDto fileDto = new FileInsertDto(meal.getFile(), FileCateCdEnum.MEAL.getValue());
			FileModel fileModel = fileUtil.saveFile(fileDto);
			
			//2. 파일데이터 DB 저장
			fileService.insertFile(fileModel);
			
			meal.setFileId(fileModel.getFileId());
		}

		mealMapper.insertMeal(meal);
		
	}

	@Override
	public List<MealResponse> selectMealList(Long userId) {
		
		List<Meal> list = mealMapper.selectMealList(userId);
		List<MealResponse> dtoList = list.stream()
				.map(m -> {
					MealResponse dto = modelMapper.map(m, MealResponse.class);
		            dto.setImgUrl(fileUtil.makeImgApiUrl(dto.getFileId()));
		            return dto;
		        })
				.collect(Collectors.toList());
				
		return dtoList;
		
	}

}
