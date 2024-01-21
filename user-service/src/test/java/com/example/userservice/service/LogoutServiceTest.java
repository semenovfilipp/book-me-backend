package com.example.userservice.service;

import com.example.userservice.model.Token;
import com.example.userservice.repository.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class LogoutServiceTest {

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private LogoutService logoutService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLogoutWithValidToken() {
        when(request.getHeader("Authorization")).thenReturn("Bearer validToken");
        when(tokenRepository.findByToken("validToken")).thenReturn(Optional.of(new Token()));

        logoutService.logout(request, response, authentication);


        verify(tokenRepository, times(1)).save(any());
    }

    @Test
    public void testLogoutWithInvalidToken() {
        when(request.getHeader("Authorization")).thenReturn("Bearer invalidToken");
        when(tokenRepository.findByToken("invalidToken")).thenReturn(Optional.empty());

        logoutService.logout(request, response, authentication);


        verify(tokenRepository, never()).save(any());
    }

    @Test
    public void testLogoutWithMissingAuthorizationHeader() {
        when(request.getHeader("Authorization")).thenReturn(null);

        logoutService.logout(request, response, authentication);

        verify(tokenRepository, never()).save(any());
    }
}
