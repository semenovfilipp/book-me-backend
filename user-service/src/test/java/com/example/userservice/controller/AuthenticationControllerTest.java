package com.example.userservice.controller;

import com.example.userservice.model.auth.AuthenticationRequest;
import com.example.userservice.model.auth.AuthenticationResponse;
import com.example.userservice.model.auth.RegisterRequest;
import com.example.userservice.model.enums.Role;
import com.example.userservice.service.AuthenticationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationControllerTest {



    @Mock
    private AuthenticationService authenticationService;

    private final MockMvc mockMvc;

    public AuthenticationControllerTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    public void testRegister() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest("username", "lastname", "email", "password", Role.USER);
        AuthenticationResponse authenticationResponse = new AuthenticationResponse("access_token", "refresh_token");

        Mockito.when(authenticationService.register(Mockito.any(RegisterRequest.class)))
                .thenReturn(authenticationResponse);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/register")
                        .content(asJsonString(registerRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.token").value("token"));
    }

    @Test
    public void testAuthenticate() throws Exception {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest("username", "password");
        AuthenticationResponse authenticationResponse = new AuthenticationResponse("access_token", "refresh_token");

        Mockito.when(authenticationService.authenticate(Mockito.any(AuthenticationRequest.class)))
                .thenReturn(authenticationResponse);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/authenticate")
                        .content(asJsonString(authenticationRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.token").value("token"));
    }


    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
