package com.mutecsoft.healthvision.common.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FoodPreset extends BaseModel {
    private Long foodPresetId;
    
    @NotBlank
    private String foodNm;
    @NotNull
    private Integer calories;
    private Long foodFileId;
    private Long userId;
    
    //추가 정보
    private MultipartFile foodFile;
}
