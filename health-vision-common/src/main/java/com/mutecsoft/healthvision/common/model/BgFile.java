package com.mutecsoft.healthvision.common.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
public class BgFile extends BaseModel{
    private Long bgFileId;
    private String fileNm;
    private String filePath;
    private String originFileNm;
    private Long fileSize;
    private LocalDate recordStartDate;
    private LocalDate recordEndDate;
    private Long userId;
}
