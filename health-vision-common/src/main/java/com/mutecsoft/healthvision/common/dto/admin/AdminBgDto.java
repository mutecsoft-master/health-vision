package com.mutecsoft.healthvision.common.dto.admin;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AdminBgDto {
	
	@Getter
	@Setter
	@ToString
    public static class SearchBgFile extends DatatableDto {
		
		//검색 파라미터
		private String searchEmail;
		private String searchRecordStartDate;
		private String searchRecordEndDate;
		private String searchReportApplyDate;
		private String searchReportStatusCd;
		
    }
}
