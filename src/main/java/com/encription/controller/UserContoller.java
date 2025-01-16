package com.encription.controller;

import com.encription.dto.AuthResponse;
import com.encription.dto.AuthRequest;
import com.encription.dto.UserRequest;
import com.encription.dto.UserResponse;
import com.encription.service.AuthService;
import com.encription.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/user")
public class UserContoller {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    @PostMapping("/createUser")
    public ResponseEntity<UserResponse> createUser(@RequestBody UserRequest userRequest){
         UserResponse savedUser=userService.createUser(userRequest);
         return new ResponseEntity<>(savedUser, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticateUser(@RequestBody AuthRequest request) throws Exception {
        return authService.authenticateUser(request);
    }

}
