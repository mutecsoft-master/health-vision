package com.mutecsoft.healthvision.common.model;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FoodDiary extends BaseModel {
    private Long foodDiaryId;
    
    @NotBlank
    private String foodNm;
    private Integer calories;
    private Long foodFileId;
    private String foodDesc;
    private String mealTypeCd;
    private LocalDateTime mealDt;
    private Long userId;
    
    //추가 정보
    private MultipartFile foodFile;
}
