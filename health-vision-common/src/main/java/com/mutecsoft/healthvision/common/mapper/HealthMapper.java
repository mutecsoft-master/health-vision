package com.mutecsoft.healthvision.common.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.mutecsoft.healthvision.common.annotation.LoginId;
import com.mutecsoft.healthvision.common.dto.HealthDto.BgInfo;
import com.mutecsoft.healthvision.common.dto.HealthDto.HealthInfoRequest;
import com.mutecsoft.healthvision.common.model.health.MobileHrData;
import com.mutecsoft.healthvision.common.model.health.MobileSleepData;
import com.mutecsoft.healthvision.common.model.health.MobileWorkoutData;

@Mapper
public interface HealthMapper {

	@LoginId
	void insertMobileHrDataBatch(List<MobileHrData> list);
	
	@LoginId
	void insertMobileSleepDataBatch(List<MobileSleepData> list);
	
	@LoginId
	void insertMobileWorkoutDataBatch(List<MobileWorkoutData> list);
	
	
	
	
	
	
	
	List<BgInfo> selectBgCountByDateRange(HealthInfoRequest healthInfoReq);

	List<MobileWorkoutData> selectWorkoutList(HealthInfoRequest healthInfoReq);

	Long selectTotSleepMin(HealthInfoRequest healthInfoReq);

	List<MobileHrData> selectHeartRateList(HealthInfoRequest healthInfoReq);
	
}
