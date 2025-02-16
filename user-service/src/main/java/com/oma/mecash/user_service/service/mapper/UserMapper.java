package com.oma.mecash.user_service.service.mapper;

import com.oma.mecash.user_service.dto.UpdateUserDTO;
import com.oma.mecash.user_service.dto.UserResponse;
import com.oma.mecash.user_service.model.entity.User;
import com.oma.mecash.user_service.model.Address;
import io.micrometer.common.util.StringUtils;

public class UserMapper {

    public User UpdateUserDTOToUser(User user, UpdateUserDTO userDTO) {
        if (StringUtils.isNotBlank(userDTO.getFirstName())) {
            user.setFirstName(userDTO.getFirstName());
        }
        if (StringUtils.isNotBlank(userDTO.getLastName())) {
            user.setLastName(userDTO.getLastName());
        }
        Address address = user.getAddress();
        if (StringUtils.isNotBlank(userDTO.getStreetAddress())) {
            address.setStreetAddress(userDTO.getStreetAddress());
        }
        if (StringUtils.isNotBlank(userDTO.getCity())) {
            address.setCity(userDTO.getCity());
        }
        if (StringUtils.isNotBlank(userDTO.getState())) {
            address.setState(userDTO.getState());
        }
        if (StringUtils.isNotBlank(userDTO.getCountry())) {
            address.setCountry(userDTO.getCountry());
        }
        user.setAddress(address);
        return user;
    }

    public UserResponse userToUserResponse(User user) {
        return UserResponse.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .userName(user.getUserName())
                .accountType(user.getAccountType())
                .state(user.getAddress().getState())
                .streetAddress(user.getAddress().getStreetAddress())
                .city(user.getAddress().getCity())
                .country(user.getAddress().getCountry())
                .build();
    }
}
