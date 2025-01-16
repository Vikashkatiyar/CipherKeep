package com.encription.service.Impl;

import com.encription.CipherKeepApplication;
import com.encription.dto.AuthResponse;
import com.encription.dto.AuthRequest;
import com.encription.service.AuthService;
import com.encription.service.UserAuthDetailService;
import com.encription.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserAuthDetailService userAuthDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    JwtUtils jwtUtils;


    public ResponseEntity<AuthResponse> authenticateUser(AuthRequest authRequest) throws Exception {
        CipherKeepApplication.logger.info("Authenticating user: {}", authRequest.getUsername());

        authenticate(authRequest.getUsername(), authRequest.getPassword());

        final UserDetails userDetails = userAuthDetailsService
                .loadUserByUsername(authRequest.getUsername());
        final String token = jwtUtils.generateJwtToken(userDetails);
        CipherKeepApplication.logger.info("User {} authenticated successfully.", authRequest.getUsername());
        return ResponseEntity.ok(AuthResponse.builder().token(token).tokenType("Bearer").build());
    }

    public void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            CipherKeepApplication.logger.debug("Authentication attempt for user: {}", username);
        } catch (BadCredentialsException e) {
            CipherKeepApplication.logger.warn("Invalid credentials provided for user: {}", username);
            throw new BadCredentialsException("INVALID_CREDENTIALS", e);
        }
    }
}
