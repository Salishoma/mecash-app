package com.oma.mecash.user_service.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AccessTokenResponse {
    private String email;
    private String token;
}
