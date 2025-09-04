package com.mutecsoft.healthvision.common.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefreshToken {
    private Long refreshTokenId;
    private Long userId;
    private String refreshToken;
}
