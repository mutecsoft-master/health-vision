package com.mutecsoft.healthvision.common.model;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class User extends BaseModel {

    private Long userId;
    private String email;
    private String userPw;
    private LocalDateTime lastLoginDt;
    private String delYn;
    private LocalDateTime delDt;

}
