package com.oma.mecash.user_service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AccessTokenRequest {
    private String email;
    private String password;
}
