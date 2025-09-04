package com.mutecsoft.healthvision.common.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@ToString
public class User extends BaseModel {

    private Long userId;
    private String email;
    private String userPw;
    private String snsProvider;
    private String snsUserId;
    private String gender;
    private String birthDate;
    private Double height;
    private Double weight;
    private String nickname;
    private Long profileFileId;
    private LocalDateTime lastLoginDt;
    private String delYn;
    private LocalDateTime delDt;
    private String tempPw;
    private LocalDateTime tempPwExpireDt;
    private String lockYn;
    private LocalDateTime lockDt;

    //추가 정보
    private List<String> roleNmList;
    
}
