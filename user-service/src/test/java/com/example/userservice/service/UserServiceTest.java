package com.example.userservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class UserServiceTest {

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetCurrentUsername() {
        String username = "testUser";
        when(authentication.getName()).thenReturn(username);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        String currentUsername = userService.getCurrentUsername();

        assertEquals(username, currentUsername);
    }

    @Test
    public void testGetCurrentUsernameWhenAuthenticationIsNull() {
        when(securityContext.getAuthentication()).thenReturn(null);
        SecurityContextHolder.setContext(securityContext);

        assertThrows(SecurityException.class, () -> userService.getCurrentUsername());
    }

    @Test
    public void testGetCurrentUsernameWhenAuthenticationNameIsNull() {
        when(authentication.getName()).thenReturn(null);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        assertThrows(SecurityException.class, () -> userService.getCurrentUsername());
    }

    @Test
    public void testGetCurrentUsernameWhenAuthenticationNameIsInvalid() {
        when(authentication.getName()).thenReturn("invalidUserID");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        assertThrows(SecurityException.class, () -> userService.getCurrentUsername());
    }
}
