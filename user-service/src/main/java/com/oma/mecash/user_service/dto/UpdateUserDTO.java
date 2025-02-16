package com.oma.mecash.user_service.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UpdateUserDTO {

    private String firstName;

    private String lastName;

    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    @Size(min = 8, max = 13, message = "Not a valid phone number")
    @Pattern(regexp = "^\\+?[0-9]+$", message = "Invalid phone number format")
    private String phoneNumber;

    private String streetAddress;
    private String city;
    private String state;
    private String country;
}
