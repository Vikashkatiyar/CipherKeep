package com.encription.service.Impl;

import com.encription.model.Role;
import com.encription.model.User;
import com.encription.dto.UserRequest;
import com.encription.dto.UserResponse;
import com.encription.mapper.UserMapper;
import com.encription.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserRequest userRequest;
    private User user;
    private UserResponse userResponse;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        userRequest = new UserRequest();
        userRequest.setName("John Doe");
        userRequest.setEmail("john.doe@example.com");
        userRequest.setPassword("password123");
        Set<Role> roles = new HashSet<>();

        userRequest.setRoles(roles);

        user = new User();
        user.setId(1L);
        user.setName(userRequest.getName());
        user.setEmail(userRequest.getEmail());
        user.setPassword(userRequest.getPassword());
        user.setRoles(userRequest.getRoles());

        userResponse = new UserResponse();
        userResponse.setUsername(user.getName());
        userResponse.setRoles(user.getRoles());

        when(userMapper.mapToUser(any(UserRequest.class))).thenReturn(user);
        when(userMapper.mapToUserResponse(any(User.class))).thenReturn(userResponse);

        when(passwordEncoder.encode(any(CharSequence.class))).thenReturn("encodedPassword123");
    }

    @Test
    public void testCreateUser_Success() {

        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponse result = userServiceImpl.createUser(userRequest);

        assertEquals(userResponse.getUsername(), result.getUsername());
        assertEquals(userResponse.getRoles().size(), result.getRoles().size());
    }
}
