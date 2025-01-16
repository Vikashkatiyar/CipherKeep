package com.encription.service;

import com.encription.dto.UserRequest;
import com.encription.dto.UserResponse;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    UserResponse createUser(UserRequest userRequest );
}
