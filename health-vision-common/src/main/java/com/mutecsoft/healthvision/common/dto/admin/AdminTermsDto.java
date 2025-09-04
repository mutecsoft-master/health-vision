package com.mutecsoft.healthvision.common.dto.admin;

import java.time.LocalDateTime;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AdminTermsDto {

    @Getter
    @Setter
    @ToString
    public static class SearchTerms extends DatatableDto {

        //검색 파라미터
        private String searchType;
        private String searchTitle;
        private String searchContent;
        private Integer searchVersion;
        private String searchRequiredYn;
        private String searchLang;
        private String searchDelYn;
        private String searchRegDt;
    }
    
    
    @Getter
	@Setter
	@ToString
	public static class RegisterTerms {
    	private String type;
    	private Integer version;
    	private String title;
    	private String content;
    	private String requiredYn;
    	private String lang;
    	
    	private Long regId;
    	
	}
    
    @Getter
	@Setter
	@ToString
	public static class UpdateTerms {
    	private Long termsId;
    	private String type;
    	private Integer version;
    	private String title;
    	private String content;
    	private String requiredYn;
    	private String lang;
    	private String delYn;
    	
    	private Long updId;
    	
	}
    
    @Getter
    @Setter
    @ToString
    public static class TermsResponse {

    	private Long termsId;
    	private String type;
    	private Integer version;
    	private String title;
    	private String content;
    	private String requiredYn;
    	private String lang;
    	private String delYn;
    	
    	private String regId;
    	private LocalDateTime regDt;
    	private String updId;
    	private LocalDateTime updDt;
    	
    	private String plainContent;
    	private String regDate;
    	private String updDate;
    }

}
