package com.mutecsoft.healthvision.admin.service.impl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mutecsoft.healthvision.admin.constant.BgExcelHeaderEnum;
import com.mutecsoft.healthvision.admin.constant.FoodDiaryExcelHeaderEnum;
import com.mutecsoft.healthvision.admin.constant.HrExcelHeaderEnum;
import com.mutecsoft.healthvision.admin.constant.SleepExcelHeaderEnum;
import com.mutecsoft.healthvision.admin.constant.WorkoutExcelHeaderEnum;
import com.mutecsoft.healthvision.admin.service.HealthService;
import com.mutecsoft.healthvision.admin.util.ExcelUtil;
import com.mutecsoft.healthvision.common.constant.Const;
import com.mutecsoft.healthvision.common.constant.MealTypeCdEnum;
import com.mutecsoft.healthvision.common.constant.ReportStatusCdEnum;
import com.mutecsoft.healthvision.common.constant.ResultCdEnum;
import com.mutecsoft.healthvision.common.dto.admin.AdminBgDto.SearchBgFile;
import com.mutecsoft.healthvision.common.dto.admin.AdminHealthDto.BloodGlucoseForDataDownload;
import com.mutecsoft.healthvision.common.dto.admin.AdminHealthDto.FoodDiaryForDataDownload;
import com.mutecsoft.healthvision.common.dto.admin.AdminHealthDto.HeartRateForDataDownload;
import com.mutecsoft.healthvision.common.dto.admin.AdminHealthDto.SearchForDataDownload;
import com.mutecsoft.healthvision.common.dto.admin.AdminHealthDto.SleepForDataDownload;
import com.mutecsoft.healthvision.common.dto.admin.AdminHealthDto.WorkoutForDataDownload;
import com.mutecsoft.healthvision.common.dto.web.BgFileDto.BgFileResponse;
import com.mutecsoft.healthvision.common.exception.CustomException;
import com.mutecsoft.healthvision.common.mapper.AHealthMapper;
import com.mutecsoft.healthvision.common.util.FileUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HealthServiceImpl implements HealthService {
	
	private final AHealthMapper aHealthMapper;
	private final ExcelUtil excelUtil;
	private final ObjectMapper objectMapper;
	
	@Override
	public PageImpl<BgFileResponse> selectBgFileListPage(SearchBgFile searchParam, Pageable pageable) {
		
		List<BgFileResponse> bgFileList = aHealthMapper.selectBgFileList(searchParam, pageable);
		
		bgFileList.forEach(bgFile -> {
			if(StringUtils.hasText(bgFile.getReportStatusCd())) {
				ReportStatusCdEnum reportStatusCdEnum = ReportStatusCdEnum.fromValue(bgFile.getReportStatusCd());
				bgFile.setReportStatusNm(reportStatusCdEnum.getName());
			}
		} );
		
		int totalCnt = aHealthMapper.selectBgFileListCnt(searchParam, pageable);
		
		return new PageImpl<>(bgFileList, pageable, totalCnt);
	}

	
	@Override
	public void downloadData(Long bgFileId, HttpServletResponse response) {

		BgFileResponse bgFile = aHealthMapper.selectBgFileById(bgFileId);
		
		SearchForDataDownload searchDto = new SearchForDataDownload(
				bgFile.getUserId(), 
				bgFile.getRecordStartDate(), 
				bgFile.getRecordEndDate());
		
		List<BloodGlucoseForDataDownload> bgList = aHealthMapper.selectBloodGlucoseForExcelDownload(bgFileId);
		
		List<FoodDiaryForDataDownload> foodDiaryList = aHealthMapper.selectFoodDiaryForExcelDownload(searchDto);
		foodDiaryList.forEach(foodDiary -> {
			if(StringUtils.hasText(foodDiary.getMealTypeCd())) {
				MealTypeCdEnum mealTypeEnum = MealTypeCdEnum.fromValue(foodDiary.getMealTypeCd());
	            foodDiary.setMealTypeNm(mealTypeEnum.getDescValue());
			}
        });
		
		List<WorkoutForDataDownload> workoutList = aHealthMapper.selectWorkoutForExcelDownload(searchDto);
		List<SleepForDataDownload> sleepList = aHealthMapper.selectSleepForExcelDownload(searchDto);
		List<HeartRateForDataDownload> hrList = aHealthMapper.selectHeartRateForExcelDownload(searchDto);
		
		List<Map<String, Object>> bgMapList = objectMapper.convertValue(bgList, new TypeReference<List<Map<String, Object>>>(){});
		List<Map<String, Object>> foodDiaryMapList = objectMapper.convertValue(foodDiaryList, new TypeReference<List<Map<String, Object>>>(){});
		List<Map<String, Object>> workoutMapList = objectMapper.convertValue(workoutList, new TypeReference<List<Map<String, Object>>>(){});
		List<Map<String, Object>> sleepMapList = objectMapper.convertValue(sleepList, new TypeReference<List<Map<String, Object>>>(){});
		List<Map<String, Object>> hrMapList = objectMapper.convertValue(hrList, new TypeReference<List<Map<String, Object>>>(){});
		
		try {
			byte[] bgExcel = excelUtil.healthDataListToExcel(BgExcelHeaderEnum.values(), bgMapList);
			byte[] foodDiaryExcel = excelUtil.healthDataListToExcel(FoodDiaryExcelHeaderEnum.values(), foodDiaryMapList);
			byte[] workoutExcel = excelUtil.healthDataListToExcel(WorkoutExcelHeaderEnum.values(), workoutMapList);
			byte[] sleepExcel = excelUtil.healthDataListToExcel(SleepExcelHeaderEnum.values(), sleepMapList);
			byte[] hrExcel = excelUtil.healthDataListToExcel(HrExcelHeaderEnum.values(), hrMapList);
			
			//압축
			Map<String, byte[]> excelFiles = new HashMap<>();
			excelFiles.put("bg.xlsx", bgExcel);
			excelFiles.put("foodDiary.xlsx", foodDiaryExcel);
			excelFiles.put("workout.xlsx", workoutExcel);
			excelFiles.put("sleep.xlsx", sleepExcel);
			excelFiles.put("hr.xlsx", hrExcel);
			
			byte[] zipBytes = FileUtil.toZip(excelFiles);

			LocalDateTime now = LocalDateTime.now();
	        String timestamp = now.format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
			String fileNm = Const.EXCEL_DATA_ZIP_FILE_NM_PREFIX + timestamp;
			
			response.setContentType("application/zip");
			response.setHeader("Content-Disposition", "attachment; filename=" + fileNm + ".zip");
			response.getOutputStream().write(zipBytes);
			
		} catch (IOException e) {
			throw new CustomException(ResultCdEnum.E107.getValue());
			
		}
		
	}
	

}
