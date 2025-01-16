package com.encription.service.Impl;

import com.encription.CipherKeepApplication;
import com.encription.exception.UserAlreadyExistsException;
import com.encription.model.User;
import com.encription.repository.UserRepository;
import com.encription.exception.UserNotFoundException;
import com.encription.mapper.UserMapper;
import com.encription.dto.UserRequest;
import com.encription.dto.UserResponse;
import com.encription.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;



    @Override
    public UserResponse createUser(UserRequest userRequest) {
        CipherKeepApplication.logger.info("Attempting to create user with email: {}", userRequest.getEmail());
        if (Objects.isNull(userRequest)) {
            CipherKeepApplication.logger.error("User request is null.");
            throw new UserNotFoundException("User Request is null");
        }

        if (userRepository.existsByEmail(userRequest.getEmail())) {
            CipherKeepApplication.logger.error("User already exists with email: {}", userRequest.getEmail());
            throw new UserAlreadyExistsException("User already exists with email: " + userRequest.getEmail());
        }

        User userToSave = userMapper.mapToUser(userRequest);
        userToSave.setPassword(passwordEncoder.encode(userToSave.getPassword()));
        User savedUser = userRepository.save(userToSave);
        CipherKeepApplication.logger.info("User created successfully with email: {}", savedUser.getEmail());

        return userMapper.mapToUserResponse(savedUser);
    }


}
