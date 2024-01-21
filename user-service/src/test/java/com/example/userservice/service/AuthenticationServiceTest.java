package com.example.userservice.service;

import com.example.userservice.model.User;
import com.example.userservice.model.auth.AuthenticationRequest;
import com.example.userservice.model.auth.AuthenticationResponse;
import com.example.userservice.model.auth.RegisterRequest;
import com.example.userservice.model.enums.Role;
import com.example.userservice.repository.TokenRepository;
import com.example.userservice.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private TokenRepository tokenRepository;

    @InjectMocks
    private AuthenticationService authenticationService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegister() {
        RegisterRequest registerRequest = new RegisterRequest("John", "Doe", "john@example.com", "password", Role.USER);

        when(passwordEncoder.encode(registerRequest.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(new User());

        AuthenticationResponse response = authenticationService.register(registerRequest);

        assertNotNull(response.getAccessToken());
        assertNotNull(response.getRefreshToken());

        verify(tokenRepository, times(1)).save(any());
    }

    @Test
    public void testAuthenticate() {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest("john@example.com", "password");
        User user = new User();
        when(userRepository.findByEmail(authenticationRequest.getEmail())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn("accessToken");
        when(jwtService.generateRefreshToken(user)).thenReturn("refreshToken");

        AuthenticationResponse response = authenticationService.authenticate(authenticationRequest);


        assertNotNull(response.getAccessToken());
        assertNotNull(response.getRefreshToken());

        verify(tokenRepository, times(1)).save(any());
        verify(tokenRepository, times(1)).findAllValidTokensByUser(anyLong());
        verify(tokenRepository, times(1)).saveAll(any());
    }

    @Test
    public void testRefreshToken() throws IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);

        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer refreshToken");
        when(jwtService.extractUsername("refreshToken")).thenReturn("john@example.com");
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(new User()));
        when(jwtService.isTokenValid("refreshToken", any(User.class))).thenReturn(true);
        when(jwtService.generateToken(any(User.class))).thenReturn("newAccessToken");

        authenticationService.refreshToken(request, response);

        verify(tokenRepository, times(1)).save(any());
        verify(response.getOutputStream(), times(1)).write(any(byte[].class));
    }
}
