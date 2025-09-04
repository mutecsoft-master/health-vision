package com.mutecsoft.healthvision.common.model;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Token {
    private Long tokenId;
    private Long userId;
    private String tokenType;
    private String token;
    private LocalDateTime regDt;
    private LocalDateTime updDt;
}
