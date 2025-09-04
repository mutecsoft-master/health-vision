package com.mutecsoft.healthvision.common.dto.web;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import com.mutecsoft.healthvision.common.dto.CsvDto.BgDataDto;
import com.mutecsoft.healthvision.common.dto.admin.DatatableDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class BgFileDto {
    
    @Getter
    @Setter
    public static class UploadRequest {
        private List<MultipartFile> files;
    }

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BgFileInsertDto {
        private Long userId;
        private MultipartFile file;
        private LocalDate recordStartDate;
        private LocalDate recordEndDate;
    }

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BgDataInsertDto {
        private List<BgDataDto> bgDataList;
        private String unit;
        private Long bgFileId;
        private Long deviceId;
    }

    @Getter
    @Setter
    @ToString
    public static class SearchBgFile extends DatatableDto {
        //검색 파라미터
        private String searchFileName;
        private Long searchSize;
        private String searchRecordStartDate;
        private String searchRecordEndDate;
        private String searchRegDt;
        private String orderColumn;
        private String orderDirection;

        // userId는 서버에서 세팅
        private Long userId;
    }
    
    
    
    @Getter
    @Setter
    @ToString
    public static class BgFileSearchRequest {
    	private Long userId;
    	private Integer limit;
    }
    
    
    @Getter
    @Setter
    @ToString
    public static class BgFileResponse {
    	private Long bgFileId;
        private String fileNm;
        private String filePath;
        private String originFileNm;
        private Long fileSize;
        private LocalDate recordStartDate;
        private LocalDate recordEndDate;
        private Long userId;
        private Long regId;
        private LocalDateTime regDt;
		private Long updId;
		private LocalDateTime updDt;

		private String reportStatusCd;
		private String reportStatusNm;
		
		private String email;
		private String aplyDate;
		

    }
}
