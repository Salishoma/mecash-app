package com.oma.mecash.user_service.dto;

import com.oma.mecash.user_service.model.enums.AccountType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponse {
    private String firstName;
    private String lastName;
    private String email;
    private String userName;
    private String phoneNumber;

    private AccountType accountType;

    private String streetAddress;

    private String city;

    private String state;

    private String country;

}
