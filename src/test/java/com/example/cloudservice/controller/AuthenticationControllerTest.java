package com.example.cloudservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthenticationControllerTest {
    private static final String USERNAME = "madmaxim22@gmail.com";
    private static final String PASSWORD = "1234";
    @Autowired
    private MockMvc mvc;

    @Test
    void register() throws Exception {
        Map<String, Object> payload = new HashMap<>();
        payload.put("email", USERNAME);
        payload.put("password", PASSWORD);
        payload.put("firstname", "maksim");
        payload.put("lastname", "ytehin");
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(payload);

        MvcResult result = mvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        String token = result.getResponse().getContentAsString();
        assertNotNull(token);
    }

    @Test
    void whenAuthenticate_thenIsOk() throws Exception {
        Map<String, Object> payload = new HashMap<>();
        payload.put("login", USERNAME);
        payload.put("password", PASSWORD);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(payload);

        MvcResult result = mvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        String token = result.getResponse().getContentAsString();
        assertNotNull(token);
    }

    @Test
    void whenAuthenticate_thenIsBadRequest() throws Exception {
        Map<String, Object> payload = new HashMap<>();
        payload.put("login", "test@mail.com");
        payload.put("password", PASSWORD);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(payload);

        mvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Неверное имя или пароль"))
                .andExpect(jsonPath("$.id").value(0));

    }
}