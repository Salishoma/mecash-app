package com.oma.mecash.user_service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class AccessTokenRequest {
    private String email;
    private String password;
}
