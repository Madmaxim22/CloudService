package com.example.cloudservice.testcontainer;

import com.example.cloudservice.controller.request.AuthenticationRequest;
import com.example.cloudservice.controller.request.RegisterRequest;
import com.example.cloudservice.controller.response.AuthenticationResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("/application-test.properties")
public class AuthenticationControllerTest extends MySQLContainerBaseTest {
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;


    @Test
    public void whenRegister_thenReturnToken() {
        RegisterRequest request = RegisterRequest.builder()
                .email("test@mail.ru")
                .firstname("masksim")
                .lastname("patkov")
                .password("1234")
                .build();

        AuthenticationResponse response = restTemplate.postForObject(
                "http://localhost:" + port + "/register", request, AuthenticationResponse.class);

        assertNotNull(response);
    }

    @Test
    void whenAuthenticate_thenReturnTokenAndIsOk() {
        AuthenticationRequest request = AuthenticationRequest.builder()
                .email("test@mail.com")
                .password("1234")
                .build();

        AuthenticationResponse response = restTemplate.postForObject(
                "http://localhost:" + port + "/login", request, AuthenticationResponse.class);

        assertNotNull(response);
    }

}
