package com.encription.service;

import com.encription.dto.AuthResponse;
import com.encription.dto.AuthRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {

    public ResponseEntity<AuthResponse> authenticateUser(AuthRequest authRequest) throws Exception;
}