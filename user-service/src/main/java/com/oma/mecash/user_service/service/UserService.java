package com.oma.mecash.user_service.service;

import com.oma.mecash.security_service.enums.Role;
import com.oma.mecash.security_service.model.SecurityUser;
import com.oma.mecash.security_service.model.SignUpResponse;
import com.oma.mecash.security_service.model.entity.AuthUser;
import com.oma.mecash.security_service.service.AuthUserService;
import com.oma.mecash.user_service.dto.AccessTokenRequest;
import com.oma.mecash.user_service.dto.AccessTokenResponse;
import com.oma.mecash.user_service.dto.CreateUserDTO;
import com.oma.mecash.user_service.dto.UpdateTransactionPinDTO;
import com.oma.mecash.user_service.dto.UpdateUserDTO;
import com.oma.mecash.user_service.dto.UserResponse;
import com.oma.mecash.user_service.exception.UserNotFoundException;
import com.oma.mecash.user_service.model.entity.User;
import com.oma.mecash.user_service.exception.UserExistsException;
import com.oma.mecash.user_service.model.Address;
import com.oma.mecash.user_service.repository.UserRepository;
import com.oma.mecash.user_service.service.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    private final AuthUserService authUserService;
    private final PasswordEncoder passwordEncoder;

    @Transactional(rollbackFor = Exception.class)
    public SignUpResponse createUser(CreateUserDTO createUser) {
        String email = createUser.getEmail();
        boolean exists = userRepository.existsByEmailIgnoreCase(email);
        if (exists) {
            throw new UserExistsException(String.format("Email %s has already been used", email));
        }
        User user = createUserFromDTO(createUser,createAddressFromDTO(createUser));
        User savedUser = userRepository.save(user);

        AuthUser authUser = createAuthUserFromSavedUser(savedUser, createUser);

        authUserService.saveUser(authUser);

        return new SignUpResponse(createUser.getEmail(), "User registered successfully");

    }

    public String updateUser(UpdateUserDTO updateUser) {
        Pair<AuthUser, User> pair = getPairUser();
        AuthUser authUser = pair.getLeft();
        User user = pair.getRight();

        if (updateUser.getPassword() != null) {
            authUser.setPassword(passwordEncoder.encode(updateUser.getPassword()));
            authUserService.saveUser(authUser);
        }
        user = new UserMapper().UpdateUserDTOToUser(user, updateUser);
        userRepository.save(user);
        return "User Updated successfully";
    }

    public AccessTokenResponse generateAccessToken(AccessTokenRequest accessTokenRequest) {
        String token = authUserService.generateAccessToken(accessTokenRequest.getEmail(), accessTokenRequest.getPassword());
        return AccessTokenResponse.builder().email(accessTokenRequest.getEmail()).token(token).build();
    }

    public UserResponse getUser() {
        SecurityUser loggedInUser = authUserService.getPrincipal();
        User user = userRepository.findByEmail(loggedInUser.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        UserMapper mapper = new UserMapper();
        return mapper.userToUserResponse(user);
    }

    public String createPin(String pin) {
        Pair<AuthUser, User> pair = getPairUser();
        AuthUser authUser = pair.getLeft();
        User user = pair.getRight();

        String encode = passwordEncoder.encode(pin);
        user.setTransactionPin(encode);
        userRepository.save(user);

        authUser.setTransactionPin(encode);
        authUserService.saveUser(authUser);
        return "Pin created successfully";
    }

    public String updateTransactionPin(UpdateTransactionPinDTO transactionPinDTO) {
        Pair<AuthUser, User> pair = getPairUser();
        String oldPin = transactionPinDTO.getOldPin();
        String newPin = transactionPinDTO.getNewPin();
        authUserService.updatePin(pair.getLeft(), oldPin, newPin);
        User user = pair.getRight();
        user.setTransactionPin(passwordEncoder.encode(newPin));
        userRepository.save(user);
        return "Pin updated successfully";
    }

    private Pair<AuthUser, User> getPairUser() {
        SecurityUser loggedInUser = authUserService.getPrincipal();
        AuthUser authUser = authUserService.findUserByEmail(loggedInUser.getEmail());
        User user = userRepository.findByEmailIgnoreCase(loggedInUser.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not exist"));
        return Pair.of(authUser, user);
    }

    private AuthUser createAuthUserFromSavedUser(User user, CreateUserDTO createUser) {
        return AuthUser.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .password(passwordEncoder.encode(createUser.getPassword()))
                .roles(addRole(new HashSet<>(), Role.USER))
                .build();
    }

    private Set<Role> addRole(Set<Role> roles, Role role) {
        roles.add(role);
        return roles;
    }

    private User createUserFromDTO(CreateUserDTO createUser, Address address) {
        return User.builder()
                .firstName(createUser.getFirstName())
                .lastName(createUser.getLastName())
                .userName(createUser.getUserName())
                .email(createUser.getEmail())
                .phoneNumber(createUser.getPhoneNumber())
                .dateOfBirth(createUser.getDateOfBirth())
                .address(address)
                .build();
    }

    private Address createAddressFromDTO(CreateUserDTO createUser) {
        return Address.builder()
                .city(createUser.getCity())
                .streetAddress(createUser.getStreetAddress())
                .state(createUser.getState())
                .country(createUser.getCountry())
                .build();
    }

}
