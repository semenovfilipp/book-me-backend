package com.example.userservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {


    public String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.debug("Authentication: {}", authentication);

        if (authentication == null || authentication.getName() == null) {
            log.warn("Unable to determine current user");
            throw new SecurityException("Unable to determine current user");
        }

        try {
            return authentication.getName();
        } catch (NumberFormatException e) {
            log.error("Invalid user ID format in authentication");
            throw new SecurityException("Invalid user ID format in authentication");
        }
    }
}
