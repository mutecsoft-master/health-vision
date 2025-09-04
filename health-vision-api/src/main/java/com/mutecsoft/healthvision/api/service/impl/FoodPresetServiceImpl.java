package com.mutecsoft.healthvision.api.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.mutecsoft.healthvision.api.service.FileService;
import com.mutecsoft.healthvision.api.service.FoodPresetService;
import com.mutecsoft.healthvision.api.util.UserUtil;
import com.mutecsoft.healthvision.common.constant.Const;
import com.mutecsoft.healthvision.common.constant.FileCateCdEnum;
import com.mutecsoft.healthvision.common.constant.ResultCdEnum;
import com.mutecsoft.healthvision.common.dto.FileDto.FileInsertDto;
import com.mutecsoft.healthvision.common.dto.FoodPresetDto.FoodPresetResponse;
import com.mutecsoft.healthvision.common.dto.FoodPresetDto.FoodPresetSearchRequest;
import com.mutecsoft.healthvision.common.dto.UserDto.UserInfo;
import com.mutecsoft.healthvision.common.exception.CustomException;
import com.mutecsoft.healthvision.common.mapper.FoodPresetMapper;
import com.mutecsoft.healthvision.common.model.FileModel;
import com.mutecsoft.healthvision.common.model.FoodPreset;
import com.mutecsoft.healthvision.common.util.FileUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FoodPresetServiceImpl implements FoodPresetService{
	
	private final ModelMapper modelMapper;
	private final FoodPresetMapper foodPresetMapper;
	private final FileService fileService;
	private final FileUtil fileUtil;
	private final UserUtil userUtil;
	
	@Value("${img-api-url}")
    private String imgApiUrl;

	@Transactional
	@Override
	public void insertFoodPreset(FoodPreset foodPreset) throws IOException {
		
		//최대 갯수 확인
		UserInfo userInfo = userUtil.getUserInfo();
		FoodPresetSearchRequest searchReq = new FoodPresetSearchRequest();
		searchReq.setUserId(userInfo.getUserId());
		
		int cnt = foodPresetMapper.selectFoodPresetCount(searchReq);
		
		if(cnt >= Const.FOOD_PRESET_MAX_CNT) {
			throw new CustomException(ResultCdEnum.F001.getValue());
			
		}else {
			if(foodPreset.getFoodFile() != null) {
				//1. 파일저장
				FileInsertDto fileDto = new FileInsertDto(0, foodPreset.getFoodFile(), FileCateCdEnum.PRESET.getValue());
				FileModel fileModel = fileUtil.saveFile(fileDto);
				
				//2. 파일데이터 DB 저장
				fileService.insertFile(fileModel);
				
				foodPreset.setFoodFileId(fileModel.getFileId());
			}
			
			//3. foodDiaryList에 파일 식별자 세팅
			foodPresetMapper.insertFoodPreset(foodPreset);
		}
		
	}

	@Override
	public List<FoodPresetResponse> getFoodPresetList(FoodPresetSearchRequest searchReq) {
		
		List<FoodPreset> foodPresetList = foodPresetMapper.selectFoodPresetList(searchReq);
		
		List<FoodPresetResponse> dtoList = foodPresetList.stream()
				.map(f -> {
					FoodPresetResponse dto = modelMapper.map(f, FoodPresetResponse.class);
		            dto.setFoodImgUrl(fileUtil.makeImgApiUrl(dto.getFoodFileId()));
		            return dto;
		        })
				.collect(Collectors.toList());
		
		return dtoList;
	}

	@Transactional
	@Override
	public void updateFoodPreset(FoodPreset foodPreset) throws IOException {
		
		UserInfo userInfo = userUtil.getUserInfo();
		FoodPresetSearchRequest searchReq = new FoodPresetSearchRequest(foodPreset.getFoodPresetId());
		searchReq.setUserId(userInfo.getUserId());
		
		FoodPreset originFoodPreset = foodPresetMapper.selectFoodPreset(searchReq);
		if(originFoodPreset == null) {
			throw new CustomException(ResultCdEnum.E003.getValue());
		}
		
		if(foodPreset.getFoodFile() != null 
				&& !foodPreset.getFoodFile().isEmpty() 
				&& StringUtils.hasText(foodPreset.getFoodFile().getOriginalFilename())) {
		
			//기존 파일 삭제
			if(originFoodPreset.getFoodFileId() != null) {
				fileService.deleteFile(originFoodPreset.getFoodFileId());
			}
		
			//1. 파일저장
			FileInsertDto fileDto = new FileInsertDto(0, foodPreset.getFoodFile(), FileCateCdEnum.PRESET.getValue());
			FileModel fileModel = fileUtil.saveFile(fileDto);
			
			//2. 파일데이터 DB 저장
			if(StringUtils.hasText(fileModel.getFilePath())) {
				fileService.insertFile(fileModel);
				foodPreset.setFoodFileId(fileModel.getFileId());
			}
		}else {
			foodPreset.setFoodFileId(originFoodPreset.getFoodFileId());
		}
		
		foodPresetMapper.updateFoodPreset(foodPreset);
	}

	@Override
	public FoodPresetResponse getFoodPreset(FoodPresetSearchRequest searchReq) {
		FoodPreset foodPreset = foodPresetMapper.selectFoodPreset(searchReq);
		
		if(foodPreset == null) {
			return null;
		}
		
		FoodPresetResponse dto = modelMapper.map(foodPreset, FoodPresetResponse.class);
		dto.setFoodImgUrl(fileUtil.makeImgApiUrl(dto.getFoodFileId()));
		
		return dto;
	}

	@Transactional
	@Override
	public void deleteFoodPreset(Long foodPresetId) throws IOException {
		UserInfo userInfo = userUtil.getUserInfo();

		FoodPresetSearchRequest searchReq = new FoodPresetSearchRequest(foodPresetId);
		searchReq.setUserId(userInfo.getUserId());
		FoodPreset foodPreset = foodPresetMapper.selectFoodPreset(searchReq);
		
		if(foodPreset == null) {
			throw new CustomException(ResultCdEnum.E003.getValue());
		}
		
		//파일 삭제
		if(foodPreset.getFoodFileId() != null) {
			fileService.deleteFile(foodPreset.getFoodFileId());
		}
		
		foodPresetMapper.deleteFoodPreset(foodPresetId);
	}
	
}
