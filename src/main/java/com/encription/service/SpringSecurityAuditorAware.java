package com.encription.service;

import com.encription.CipherKeepApplication;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class SpringSecurityAuditorAware implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        CipherKeepApplication.logger.info("Entering getCurrentAuditor method in SpringSecurityAuditorAware");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            CipherKeepApplication.logger.warn("Authentication object is null or not authenticated");
            return Optional.empty();
        }
        String username = authentication.getName();
        return Optional.of(username);
    }
}
