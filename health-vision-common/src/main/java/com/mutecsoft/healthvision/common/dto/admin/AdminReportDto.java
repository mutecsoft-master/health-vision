package com.mutecsoft.healthvision.common.dto.admin;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AdminReportDto {
	
	@Getter
	@Setter
	@ToString
    public static class SearchReport extends DatatableDto {
		
		//검색 파라미터
		private String searchAplyEmail;
		private String searchAplyDate;
		
		private String searchAnlysEmail;
		private String searchAnlysDate;
		private String searchStatusCd;
		private String searchPurchaseStatusCd;
		
    }
}
