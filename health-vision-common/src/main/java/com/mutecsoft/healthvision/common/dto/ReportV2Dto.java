package com.mutecsoft.healthvision.common.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

import com.mutecsoft.healthvision.common.dto.IapDto.ApplePurchaseRequest;
import com.mutecsoft.healthvision.common.dto.IapDto.GooglePurchaseRequest;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ReportV2Dto {
	
	@Getter
	@Setter
	@ToString
	public static class PurchaseReportGoogleRequest {
		@NotNull
		private Long reportId;
		
		@Valid
	    @NotNull
	    private GooglePurchaseRequest purchase;
	}
	
	@Getter
	@Setter
	@ToString
	public static class PurchaseReportAppleRequest {
		@NotNull
		private Long reportId;
		
		@Valid
	    @NotNull
	    private ApplePurchaseRequest purchase;
	}
	
	@Getter
	@Setter
	@ToString
	public static class ReportApplyRequest {
		@NotNull
		private Long bgFileId;
	}
	
	@Getter
    @Setter
    @ToString
    public static class ReportSearchRequest {
    	private Long userId;
    	private Integer limitMonth;
    }

	@Getter
	@Setter
	@ToString
    public static class ReportResponse {
		
		private Long reportId;
//		private LocalDate reportStartDate;
//		private LocalDate reportEndDate;
		private Long aplyId;
		private LocalDateTime aplyDt;
		private String statusCd;
		private String purchaseStatusCd;
		private Long anlysId;
		private LocalDateTime anlysDt;
		private Long reportFileId;
		private Long bgFileId;
		
		private String aplyEmail;
		private String anlysEmail;
		private String aplyDate;
		private String anlysDate;
		private String statusNm;
		private String purchaseStatusNm;
		private String reportFileNm;
		private LocalDate recordStartDate;
        private LocalDate recordEndDate;
        
        private String reportPreviewUrl;
        private String reportDownloadUrl;

    }
	
	
	@Getter
	@Setter
	public static class ReportUploadRequest {
		private Long reportId;
		private MultipartFile file;
	}
	
}
