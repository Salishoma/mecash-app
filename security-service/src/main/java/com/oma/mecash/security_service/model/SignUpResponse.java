package com.oma.mecash.security_service.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SignUpResponse {
    private String email;
    private String message;
}
