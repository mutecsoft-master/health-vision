package com.mutecsoft.healthvision.api.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mutecsoft.healthvision.api.service.HealthService;
import com.mutecsoft.healthvision.api.util.UserUtil;
import com.mutecsoft.healthvision.common.constant.Const;
import com.mutecsoft.healthvision.common.constant.ResultCdEnum;
import com.mutecsoft.healthvision.common.dto.HealthDataDto;
import com.mutecsoft.healthvision.common.dto.HealthDataDto.HeartRate;
import com.mutecsoft.healthvision.common.dto.HealthDataDto.Sleep;
import com.mutecsoft.healthvision.common.dto.HealthDataDto.Workout;
import com.mutecsoft.healthvision.common.dto.HealthDto.BgInfo;
import com.mutecsoft.healthvision.common.dto.HealthDto.HealthInfoRequest;
import com.mutecsoft.healthvision.common.dto.HealthDto.HealthInfoResponse;
import com.mutecsoft.healthvision.common.dto.HealthDto.HealthSaveDto;
import com.mutecsoft.healthvision.common.dto.UserDto.UserInfo;
import com.mutecsoft.healthvision.common.exception.CustomException;
import com.mutecsoft.healthvision.common.mapper.FinalUploadMapper;
import com.mutecsoft.healthvision.common.mapper.FoodDiaryMapper;
import com.mutecsoft.healthvision.common.mapper.HealthMapper;
import com.mutecsoft.healthvision.common.model.FinalUpload;
import com.mutecsoft.healthvision.common.model.FoodDiary;
import com.mutecsoft.healthvision.common.model.health.MobileHrData;
import com.mutecsoft.healthvision.common.model.health.MobileSleepData;
import com.mutecsoft.healthvision.common.model.health.MobileWorkoutData;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class HealthServiceImpl implements HealthService{
	
	private final UserUtil userUtil;
	private final ObjectMapper objectMapper;
	private final HealthMapper healthMapper;
	private final FoodDiaryMapper foodDiaryMapper;
	private final FinalUploadMapper finalUploadMapper;

	@Override
	@Transactional
	public void saveHealth(HealthSaveDto healthDto) {
		
		UserInfo userInfo = userUtil.getUserInfo();
		
		try (InputStream inputStream = healthDto.getFile().getInputStream()) {
	        HealthDataDto healthData = objectMapper.readValue(inputStream, HealthDataDto.class);
	        
	        //심박수
	        List<HeartRate> hrDtoList = healthData.getHeartRates();
	        List<MobileHrData> hrModelList = hrDtoList.stream()
	        		.map(h -> {
	        			MobileHrData hrModel = h.toModel();
	        			hrModel.setUserId(userInfo.getUserId());
	        			return hrModel;
	        		}).collect(Collectors.toList());
	        if(!hrModelList.isEmpty()) {
	        	healthMapper.insertMobileHrDataBatch(hrModelList);
	        }
	        
	        //수면
	        List<Sleep> sleepDtoList = healthData.getSleeps();
	        List<MobileSleepData> sleepModelList = sleepDtoList.stream()
	        		.map(s -> {
	        			MobileSleepData sleepModel = s.toModel();
	        			sleepModel.setUserId(userInfo.getUserId());
	        			return sleepModel;
	        		}).collect(Collectors.toList());
	        
	        if(!sleepModelList.isEmpty()) {
	        	healthMapper.insertMobileSleepDataBatch(sleepModelList);
	        }
	        
	        //운동
	        List<Workout> workoutDtoList = healthData.getWorkouts();
	        List<MobileWorkoutData> workoutModelList = workoutDtoList.stream()
	        		.map(w -> {
	        			MobileWorkoutData sleepModel = w.toModel();
	        			sleepModel.setUserId(userInfo.getUserId());
	        			return sleepModel;
	        		}).collect(Collectors.toList());
	        
	        if(!workoutModelList.isEmpty()) {
	        	healthMapper.insertMobileWorkoutDataBatch(workoutModelList);
	        }
	        
	        //마지막 업로드 저장
	        FinalUpload finalUpload = new FinalUpload();
	        finalUpload.setUserId(userInfo.getUserId());
	        finalUpload.setUploadDt(healthDto.getUploadDt());
	        finalUploadMapper.insertFinalUpload(finalUpload);
	        
	    } catch (IOException e) {
	    	log.error("## Health 데이터 업로드 실패 : {}", e.getMessage());
	        throw new CustomException(ResultCdEnum.E001.getValue());
	    }
	}

	@Override
	public FinalUpload getFinalUpload() {
		UserInfo userInfo = userUtil.getUserInfo();
		FinalUpload finalUpload = finalUploadMapper.getFinalUpload(userInfo.getUserId());
		return finalUpload;
	}

	@Override
	public HealthInfoResponse getHealthInfo(HealthInfoRequest healthInfoReq) {
		
		LocalDate startDate = LocalDate.parse(healthInfoReq.getStartDate());
        LocalDate endDate = LocalDate.parse(healthInfoReq.getEndDate());
        long days = ChronoUnit.DAYS.between(startDate, endDate);
		
		UserInfo userInfo = userUtil.getUserInfo();
		healthInfoReq.setUserId(userInfo.getUserId());
		
		HealthInfoResponse healthInfoResponse = new HealthInfoResponse();
		
		//혈당 데이터량
		List<BgInfo> bgInfoList = healthMapper.selectBgCountByDateRange(healthInfoReq);
		healthInfoResponse.setBloodGlucosePer(calcBloodGlucosePer(days, bgInfoList));
		
		//운동 데이터량
		List<MobileWorkoutData> workoutList = healthMapper.selectWorkoutList(healthInfoReq);
		healthInfoResponse.setWorkoutPer(calcWorkoutPer(days, workoutList));
		
		//수면 데이터량
		Long totSleepMin = healthMapper.selectTotSleepMin(healthInfoReq);
		healthInfoResponse.setSleepPer(calcSleepPer(days, totSleepMin));
		
		//심박수 데이터량
		List<MobileHrData> heartRateList = healthMapper.selectHeartRateList(healthInfoReq);
		healthInfoResponse.setHeartRatePer(calcHeartRatePer(days, heartRateList));
		
		//식이 다이어리 데이터량
		List<FoodDiary> foodDiaryList = foodDiaryMapper.selectFoodDiaryListForHealthInfo(healthInfoReq);
		healthInfoResponse.setFoodDiaryPer(calcFoodDiaryPer(days, foodDiaryList));
		
		return healthInfoResponse;
	}

	//혈당 데이터량 퍼센트 계산
	private String calcBloodGlucosePer(long days, List<BgInfo> bgInfoList) {
		//하루에 측정데이터 1개만 있어도 계산에 포함. 데이터 건 수로 포함여부를 판단할 경우 cnt 값으로 포함여부 판단해야함 
		double per = (double) bgInfoList.size() / days * 100;
		int roundedPer = (int) Math.round(per);
		return roundedPer + "%";
	}
	
	//운동 데이터량 퍼센트 계산
	private String calcWorkoutPer(long days, List<MobileWorkoutData> workoutList) {
		
		double totDurationSec = workoutList.stream()
				.mapToDouble(MobileWorkoutData::getDuration)
				.sum();
		
		double totDurationMin = totDurationSec / 60; //총 분수
		double goalMin = Const.STANDARD_WORKOUT_DURATION_MINUTES * (days / 7); //목표시간(주당)
		
		double per = totDurationMin / goalMin * 100;
		int roundedPer = (int) Math.round(per);
		return roundedPer + "%";
	}
	
	//수면 데이터량 퍼센트 계산
	private String calcSleepPer(long days, Long totSleepMin) {
		
		if(totSleepMin == null) {
			return "0%";
		}else {
			double goalHour = days * Const.STANDARD_SLEEP_HOURS;
			double per = (totSleepMin / 60) / goalHour * 100;
			int roundedPer = (int) Math.round(per);
			return roundedPer + "%";
		}
		
	}

	//심박수 데이터량 퍼센트 계산
	private String calcHeartRatePer(long days, List<MobileHrData> heartRateList) {
		
		double goalCnt = Const.STANDARD_HEART_RATE_CNT * days;
		double per = (double) heartRateList.size() / goalCnt * 100;
		int roundedPer = (int) Math.round(per);
		return roundedPer + "%";
	}
	
	//식이다이어리 데이터량 퍼센트 계산
	private String calcFoodDiaryPer(long days, List<FoodDiary> foodDiaryList) {
		
		double goalCnt = Const.STANDARD_FOOD_DIARY_CNT * days;
		double per = (double) foodDiaryList.size() / goalCnt * 100;
		int roundedPer = (int) Math.round(per);
		return roundedPer + "%";
		
	}
		
	
	
}
