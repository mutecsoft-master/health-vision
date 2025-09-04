package com.mutecsoft.healthvision.common.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.domain.Pageable;

import com.mutecsoft.healthvision.common.dto.admin.AdminBgDto.SearchBgFile;
import com.mutecsoft.healthvision.common.dto.admin.AdminHealthDto.BloodGlucoseForDataDownload;
import com.mutecsoft.healthvision.common.dto.admin.AdminHealthDto.FoodDiaryForDataDownload;
import com.mutecsoft.healthvision.common.dto.admin.AdminHealthDto.HeartRateForDataDownload;
import com.mutecsoft.healthvision.common.dto.admin.AdminHealthDto.SearchForDataDownload;
import com.mutecsoft.healthvision.common.dto.admin.AdminHealthDto.SleepForDataDownload;
import com.mutecsoft.healthvision.common.dto.admin.AdminHealthDto.WorkoutForDataDownload;
import com.mutecsoft.healthvision.common.dto.web.BgFileDto.BgFileResponse;

@Mapper
public interface AHealthMapper {

	List<BgFileResponse> selectBgFileList(SearchBgFile searchParam, Pageable pageable);

	int selectBgFileListCnt(SearchBgFile searchParam, Pageable pageable);

	BgFileResponse selectBgFileById(Long bgFileId);

	List<FoodDiaryForDataDownload> selectFoodDiaryForExcelDownload(SearchForDataDownload searchDto);
	List<WorkoutForDataDownload> selectWorkoutForExcelDownload(SearchForDataDownload searchDto);
	List<SleepForDataDownload> selectSleepForExcelDownload(SearchForDataDownload searchDto);
	List<HeartRateForDataDownload> selectHeartRateForExcelDownload(SearchForDataDownload searchDto);
	List<BloodGlucoseForDataDownload> selectBloodGlucoseForExcelDownload(Long bgFileId);


	
}
