package com.mutecsoft.healthvision.common.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class MenuDto {
    private Long menuId;
    private String menuNm;
    private String url;
    private Long parentId;
    private Integer sortOrder;
    private String useYn;
    private String delYn;
    private LocalDateTime regDt;
    private Long regId;
    private LocalDateTime updDt;
    private Long updId;
}
