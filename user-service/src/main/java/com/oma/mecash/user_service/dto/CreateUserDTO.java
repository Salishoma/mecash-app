package com.oma.mecash.user_service.dto;

import java.time.LocalDate;

import lombok.Builder;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CreateUserDTO {

    @NotBlank(message = "First name is required")
    private String firstName;

    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    @Size(min = 8, max = 13, message = "Not a valid phone number")
    @Pattern(regexp = "^\\+?[0-9]+$", message = "Invalid phone number format")
    private String phoneNumber;

    @NotBlank(message = "Username is required")
    private String userName;

    @NotNull(message = "Date of birth is required")
    private LocalDate dateOfBirth;

    private String streetAddress;
    private String city;
    private String state;
    private String country;
}
