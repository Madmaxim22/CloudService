package com.example.cloudservice.service;

import com.example.cloudservice.controller.request.AuthenticationRequest;
import com.example.cloudservice.controller.request.RegisterRequest;
import com.example.cloudservice.controller.response.AuthenticationResponse;
import com.example.cloudservice.model.authentication.User;
import com.example.cloudservice.repository.TokenRepository;
import com.example.cloudservice.repository.UserRepository;
import com.example.cloudservice.security.JwtService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(value = "test")
class AuthenticationServiceTest {
    @Autowired
    private AuthenticationService authenticationService;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private JwtService jwtService;
    @MockBean
    private AuthenticationManager authenticationManager;
    @MockBean
    private TokenRepository tokenRepository;

    private final String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtYWtzaW1AbWFpbC5jb20iLCJpYXQiOjE2OTAzMDg1MzQsImV4cCI6MTY5MDM5NDkzNH0.VM0ACNXe-Xl8_o_NnosQWPGEvczrHzr4Nv7cOTKNW8c";

    @Test
    void whenRegister_thenReturnToken() throws SQLIntegrityConstraintViolationException {
        RegisterRequest request = RegisterRequest.builder()
                .email("test@mail.ru")
                .firstname("maksim")
                .lastname("petrov")
                .password("1234")
                .build();
        Mockito.when(jwtService.generateToken(any(UserDetails.class))).thenReturn(token);

        AuthenticationResponse response = authenticationService.register(request);

        assertEquals(response.getToken(), token);
    }

    @Test
    void whenAuthenticate_thenReturnToken() {
        AuthenticationRequest request = AuthenticationRequest.builder()
                .email("test@mail.ru")
                .password("1234")
                .build();
        Mockito.when(userRepository.findByEmail("test@mail.ru")).thenReturn(Optional.of(new User()));
        Mockito.when(jwtService.generateToken(any(UserDetails.class))).thenReturn(token);

        AuthenticationResponse response = authenticationService.authenticate(request);

        assertEquals(response.getToken(), token);
    }

}