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
    private Long fileGroupId;
    
    private String fileCateCd;
    private String fileNm;
    private String filePath;
    private String originFileNm;
    private Long fileSize;
    
    //추가 정보
    private int keyIndex; //메인 엔티티와 파일을 매핑하기 위한 인덱스
}
