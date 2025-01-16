package com.encription.mapper;

import com.encription.model.User;
import com.encription.dto.UserRequest;
import com.encription.dto.UserResponse;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class UserMapper {

    public User mapToUser(UserRequest userRequest) {
        if(Objects.isNull(userRequest)) {
            return new User();
        }

        return User.builder()
                .name(userRequest.getName())
                .password(userRequest.getPassword())
                .email(userRequest.getEmail())
                .roles(userRequest.getRoles())
                .build();
    }

    public UserResponse mapToUserResponse(User user) {
        if(Objects.isNull(user)) {
            return new UserResponse();
        }

        return UserResponse.builder()
                .username(user.getName())
                .roles(user.getRoles())
                .build();
    }
}
