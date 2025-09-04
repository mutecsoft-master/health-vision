package com.mutecsoft.healthvision.common.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.domain.Pageable;

import com.mutecsoft.healthvision.common.dto.ReportV2Dto.ReportResponse;
import com.mutecsoft.healthvision.common.dto.admin.AdminReportDto.SearchReport;
import com.mutecsoft.healthvision.common.model.Report;

@Mapper
public interface AReportMapper {

	List<ReportResponse> selectReportList(SearchReport searchParam, Pageable pageable);
	int selectReportListCnt(SearchReport searchParam, Pageable pageable);
	Report selectReportById(Long reportId);
	void updateReportFile(Report report);
	void deleteReportFile(Report report);


	
}
