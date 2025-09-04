package com.mutecsoft.healthvision.common.dto.admin;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AdminNoticeDto {

    @Getter
    @Setter
    @ToString
    public static class SearchNotice extends DatatableDto {

        //검색 파라미터
        private String searchTitle;
        private String searchContent;
        private String searchLang;
        private String searchDel;
        private String searchRegDate;
    }

    @Getter
    @Setter
    @ToString
    public static class CreateNotice {
        private String title;
        private String content;
        private String lang;
        
        private Long regId;
    }

    @Getter
    @Setter
    @ToString
    public static class UpdateNotice {
        private Long noticeId;
        private String title;
        private String content;
        private String lang;
        private String delYn;

        private Long updId;
    }
}
