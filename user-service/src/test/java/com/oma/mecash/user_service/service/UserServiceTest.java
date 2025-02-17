package com.oma.mecash.user_service.service;

import com.oma.mecash.security_service.exception.AuthenticateUserException;
import com.oma.mecash.security_service.model.SecurityUser;
import com.oma.mecash.security_service.model.SignUpResponse;
import com.oma.mecash.security_service.model.entity.AuthUser;
import com.oma.mecash.security_service.service.AuthUserService;
import com.oma.mecash.user_service.dto.AccessTokenRequest;
import com.oma.mecash.user_service.dto.AccessTokenResponse;
import com.oma.mecash.user_service.dto.CreateUserDTO;
import com.oma.mecash.user_service.dto.UpdateUserDTO;
import com.oma.mecash.user_service.dto.UserResponse;
import com.oma.mecash.user_service.exception.UserExistsException;
import com.oma.mecash.user_service.exception.UserNotFoundException;
import com.oma.mecash.user_service.model.Address;
import com.oma.mecash.user_service.model.entity.User;
import com.oma.mecash.user_service.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthUserService authUserService;

    @Mock
    private PasswordEncoder passwordEncoder;

    private User user;

    @BeforeEach
    public void setup() {
        user = User.builder()
                .firstName("James")
                .lastName("Maxwell")
                .email("james.maxwell@email.com")
                .userName("jwell")
                .address(new Address())
                .build();
    }

    @Test
    void createUserTest() {
        CreateUserDTO createUserDTO = createUserDTO();
        when(userRepository.existsByEmailIgnoreCase(createUserDTO().getEmail())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);

        SignUpResponse signUpResponse = userService.createUser(createUserDTO);

        verify(authUserService).saveUser(any(AuthUser.class));
        assertEquals(createUserDTO.getEmail(), signUpResponse.getEmail());
        assertEquals("User registered successfully", signUpResponse.getMessage());
    }

    @Test
    void throwExceptionWhenEmailHasBeenUsed() {
        CreateUserDTO createUserDTO = createUserDTO();
        when(userRepository.existsByEmailIgnoreCase(createUserDTO().getEmail())).thenReturn(true);
        assertThrows(UserExistsException.class, () -> userService.createUser(createUserDTO));
    }

    @Test
    void updateUserTest() {
        UpdateUserDTO updateUser = updateUserDTO();
        SecurityUser securityUser = securityUser();
        when(authUserService.getPrincipal()).thenReturn(securityUser);
        when(userRepository.findByEmailIgnoreCase(securityUser.getEmail()))
                .thenReturn(Optional.of(user));
        when(authUserService.findUserByEmail(securityUser.getEmail()))
                .thenReturn(new AuthUser());

        assertNotEquals(updateUser.getFirstName(), user.getFirstName());
        assertNotEquals(updateUser.getLastName(), user.getLastName());

        String response = userService.updateUser(updateUser);

        verify(userRepository).save(user);

        assertEquals("User Updated successfully", response);
        assertEquals(updateUser.getFirstName(), user.getFirstName());
        assertEquals(updateUser.getLastName(), user.getLastName());
    }

    @Test
    void throwExceptionWhenUserNotFound() {
        UpdateUserDTO updateUser = updateUserDTO();
        SecurityUser securityUser = securityUser();
        when(authUserService.getPrincipal()).thenReturn(securityUser);
        when(userRepository.findByEmailIgnoreCase(securityUser.getEmail()))
                .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.updateUser(updateUser));
    }

    @Test
    void generateAccessTokenTest() {
        String password = "password";
        String token = "abcd.xyz";
        AccessTokenRequest accessTokenRequest = accessTokenRequest(password);
        when(authUserService.generateAccessToken(accessTokenRequest.getEmail(), password))
                .thenReturn(token);
        AccessTokenResponse accessTokenResponse = userService.generateAccessToken(accessTokenRequest);

        assertEquals(accessTokenRequest.getEmail(), accessTokenResponse.getEmail());
        assertEquals(token, accessTokenResponse.getToken());
    }

    @Test
    void getUserTest() {
        SecurityUser securityUser = securityUser();
        when(authUserService.getPrincipal()).thenReturn(securityUser);
        when(userRepository.findByEmail(securityUser.getEmail())).thenReturn(Optional.of(user));
        UserResponse fetchedUser = userService.getUser();

        assertEquals(user.getFirstName(), fetchedUser.getFirstName());
        assertEquals(user.getLastName(), fetchedUser.getLastName());
        assertEquals(user.getEmail(), fetchedUser.getEmail());
        assertEquals(user.getUserName(), fetchedUser.getUserName());

    }

    @Test
    void createPinTest() {
        String pin = "1234";
        SecurityUser securityUser = securityUser();
        AuthUser authUser = new AuthUser();
        when(authUserService.getPrincipal()).thenReturn(securityUser);
        when(authUserService.findUserByEmail(securityUser.getEmail())).thenReturn(authUser);
        when(userRepository.findByEmailIgnoreCase(securityUser.getEmail())).thenReturn(Optional.of(user));

        String response = userService.createPin(pin);

        verify(userRepository).save(any(User.class));
        verify(authUserService).saveUser(any(AuthUser.class));

        assertEquals("Pin created successfully", response);

    }

    @Test
    void updatePinTest() {
        String pin = "1234";
        SecurityUser securityUser = securityUser();
        when(authUserService.getPrincipal()).thenReturn(securityUser);
        when(authUserService.findUserByEmail(securityUser.getEmail())).thenReturn(new AuthUser());
        when(userRepository.findByEmailIgnoreCase(securityUser.getEmail())).thenReturn(Optional.of(user));

        String response = userService.updateTransactionPin(pin);

        verify(authUserService).updatePin(any(AuthUser.class), anyString());
        verify(userRepository).save(any(User.class));

        assertEquals("Pin updated successfully", response);

    }

    private UpdateUserDTO updateUserDTO() {
        return UpdateUserDTO.builder()
                .firstName("Judge")
                .lastName("Connery")
                .password("newpassword")
                .build();
    }

    private CreateUserDTO createUserDTO() {
        return CreateUserDTO.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .password("password")
                .userName(user.getUserName())
                .build();
    }

    private SecurityUser securityUser() {
        return SecurityUser.builder()
                .email(user.getEmail())
                .build();
    }

    private AccessTokenRequest accessTokenRequest(String password) {
        return AccessTokenRequest.builder()
                .email(user.getEmail())
                .password(password)
                .build();
    }
}