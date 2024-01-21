package com.example.userservice.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class JwtServiceTest {

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private JwtService jwtService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testExtractUsername() {
        String token = jwtService.generateToken(userDetails);
        String extractedUsername = jwtService.extractUsername(token);

        assertEquals(userDetails.getUsername(), extractedUsername);
    }

    @Test
    public void testExtractClaim() {
        String token = jwtService.generateToken(userDetails);
        String extractedSubject = jwtService.extractClaim(token, Claims::getSubject);

        assertEquals(userDetails.getUsername(), extractedSubject);
    }

    @Test
    public void testGenerateTokenWithUserDetails() {
        when(userDetails.getUsername()).thenReturn("testUser");
        String generatedToken = jwtService.generateToken(userDetails);

        assertNotNull(generatedToken);
        assertTrue(jwtService.isTokenValid(generatedToken, userDetails));
    }

    @Test
    public void testGenerateTokenWithExtraClaims() {
        when(userDetails.getUsername()).thenReturn("testUser");
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("key", "value");
        String generatedToken = jwtService.generateToken(extraClaims, userDetails);

        assertNotNull(generatedToken);
        assertTrue(jwtService.isTokenValid(generatedToken, userDetails));

        Jws<Claims> parsedToken = Jwts.parserBuilder()
                .setSigningKey(jwtService.getSignInKey())
                .build()
                .parseClaimsJws(generatedToken);

        assertEquals("value", parsedToken.getBody().get("key"));
    }

    @Test
    public void testGenerateRefreshToken() {
        when(userDetails.getUsername()).thenReturn("testUser");
        String generatedRefreshToken = jwtService.generateRefreshToken(userDetails);

        assertNotNull(generatedRefreshToken);
        assertTrue(jwtService.isTokenValid(generatedRefreshToken, userDetails));
    }

    @Test
    public void testIsTokenValid() {
        when(userDetails.getUsername()).thenReturn("testUser");
        String validToken = jwtService.generateToken(userDetails);
        String invalidToken = "invalidToken";

        assertTrue(jwtService.isTokenValid(validToken, userDetails));
        assertFalse(jwtService.isTokenValid(invalidToken, userDetails));
    }

    @Test
    public void testIsTokenExpired() {
        when(userDetails.getUsername()).thenReturn("testUser");
        String expiredToken = Jwts.builder()
                .setSubject("testUser")
                .setIssuedAt(new Date(System.currentTimeMillis() - 10000))
                .setExpiration(new Date(System.currentTimeMillis() - 5000))
                .signWith(jwtService.getSignInKey(), SignatureAlgorithm.HS256)
                .compact();

        assertTrue(jwtService.isTokenExpired(expiredToken));
    }

    @Test
    public void testExtractExpiration() {
        when(userDetails.getUsername()).thenReturn("testUser");
        String token = jwtService.generateToken(userDetails);
        Date expiration = jwtService.extractExpiration(token);

        assertNotNull(expiration);
    }

    @Test
    public void testExtractAllClaims() {
        when(userDetails.getUsername()).thenReturn("testUser");
        String token = jwtService.generateToken(userDetails);
        Claims claims = jwtService.extractAllClaims(token);

        assertNotNull(claims);
        assertEquals("testUser", claims.getSubject());
    }

    @Test
    public void testGetSignInKey() {
        Key signInKey = jwtService.getSignInKey();
        assertNotNull(signInKey);
    }
}
