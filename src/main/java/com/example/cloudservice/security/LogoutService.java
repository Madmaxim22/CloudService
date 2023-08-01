package com.example.cloudservice.security;

import com.example.cloudservice.repository.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

    private final TokenRepository tokenRepository;

    @Override
    public void logout(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) {
        final String authHeader = request.getHeader("auth-token");
        final String jwt;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn(request.getRequestURI() + " - header authentication is empty or not starts with Bearer");
            return;
        }
        jwt = authHeader.substring(7);
        var storedToken = tokenRepository.findByToken(jwt)
                .orElse(null);
        if (storedToken == null) {
            log.warn(request.getRequestURI() + " - the token: " + jwt + " - does not exist in the database");
            return;
        }
        storedToken.setRevoker(true);
        storedToken.setExpired(true);
        tokenRepository.save(storedToken);
    }
}
