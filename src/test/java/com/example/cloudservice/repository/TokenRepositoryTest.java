package com.example.cloudservice.repository;

import com.example.cloudservice.model.token.Token;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/data/create-user-before.sql", "/data/create-token-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/data/create-token-after.sql", "/data/create-user-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class TokenRepositoryTest {
    private final String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtYWtzaW1AbWFpbC5jb20iLCJpYXQiOjE2OTAzMDg1MzQsImV4cCI6MTY5MDM5NDkzNH0.VM0ACNXe-Xl8_o_NnosQWPGEvczrHzr4Nv7cOTKNW8c";
    @Autowired
    private TokenRepository tokenRepository;

    @Test
    void givenTokenObject_whenByToken_thenTokenObject() {
        Token saveToken = tokenRepository.findByToken(token).get();

        assertNotNull(saveToken);
        assertEquals(saveToken.getToken(), token);
    }

    @Test
    void givenUserId_whenfindAllValidTokensByUser_thenTokensList() {
        List<Token> tokenList = tokenRepository.findAllValidTokensByUser(1L);

        assertNotNull(tokenList);
        assertEquals(tokenList.size(), 1);
        assertEquals(tokenList.get(0).getToken(), token);
    }
}
