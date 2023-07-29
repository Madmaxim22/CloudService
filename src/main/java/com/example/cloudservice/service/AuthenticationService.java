package com.example.cloudservice.service;

import com.example.cloudservice.controller.request.AuthenticationRequest;
import com.example.cloudservice.controller.response.AuthenticationResponse;
import com.example.cloudservice.controller.request.RegisterRequest;
import com.example.cloudservice.model.authentication.Role;
import com.example.cloudservice.model.authentication.User;
import com.example.cloudservice.model.token.Token;
import com.example.cloudservice.model.token.TokenType;
import com.example.cloudservice.repository.TokenRepository;
import com.example.cloudservice.repository.UserRepository;
import com.example.cloudservice.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    private final UserRepository repository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(encoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        var savedUser = repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        saveUserToken(savedUser, jwtToken);
        log.info("User: {} add in data base.", user.getEmail());
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
    // отзыв всех пользовательских токенов
    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokensByUser(user.getId());
        if (validUserTokens.isEmpty()) return;
        validUserTokens.forEach(t -> {
            t.setExpired(true);
            t.setRevoker(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }


    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .revoker(false)
                .expired(false)
                .build();
        tokenRepository.save(token);
    }
}
