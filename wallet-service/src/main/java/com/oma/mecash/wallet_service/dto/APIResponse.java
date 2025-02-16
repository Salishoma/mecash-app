package com.oma.mecash.wallet_service.dto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class APIResponse<T> {
    private String message;
    private int status;
    private T data;


}
