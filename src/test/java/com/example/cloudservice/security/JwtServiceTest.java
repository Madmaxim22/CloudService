package com.example.cloudservice.security;

import com.example.cloudservice.model.authentication.User;
import com.example.cloudservice.model.token.Token;
import com.example.cloudservice.repository.TokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(value = "test")
class JwtServiceTest {
    String token;
    User user;
    @Autowired
    private JwtService service;
    @MockBean
    private TokenRepository repository;

    @BeforeEach
    public void setup() {
        user = User.builder()
                .email("test@mail.com").build();
        token = service.generateToken(user);
    }

    @Test
    public void shouldGenerateTokenTest() {
        assertEquals(service.extractUsername(token), user.getEmail());
    }

    @Test
    public void whenExtractClaimTest_thenSignatureException() {
        assertThrows(SignatureException.class, () -> {
            service.extractClaim(token + 1, Claims::getSubject);
        });
    }

    @Test
    public void isTokenValidRevokedTest() {
        Token tokenDB = Token.builder().revoker(false).build();
        Mockito.when(repository.findByToken(token)).thenReturn(Optional.ofNullable(tokenDB));
        assertTrue(service.isTokenValid(token, user));
    }
}