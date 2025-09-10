package com.mutecsoft.healthvision.common.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

//File 클래스와 차별화를 두기 위해 FileModel이라 명명함

@Getter
@Setter
@ToString
public class FileModel extends BaseModel {
    private Long fileId;
    private String fileCateCd;
    private String fileNm;
    private String filePath;
    private String originFileNm;
    private Long fileSize;
    
}
