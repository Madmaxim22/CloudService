package com.example.cloudservice.controller;

import com.example.cloudservice.controller.request.AuthenticationRequest;
import com.example.cloudservice.controller.request.RegisterRequest;
import com.example.cloudservice.controller.response.AuthenticationResponse;
import com.example.cloudservice.controller.response.ErrorResponse;
import com.example.cloudservice.controller.response.Response;
import com.example.cloudservice.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<Response> register(
            @Valid @RequestBody RegisterRequest request
    ) {
        try {
            return ResponseEntity.ok(service.register(request));
        } catch (SQLIntegrityConstraintViolationException e) {
            return new ResponseEntity<>(new ErrorResponse("Пользователь с таким логином уже существует."), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Response> authenticate(
            @Valid @RequestBody AuthenticationRequest request
    ) {
        try {
            return ResponseEntity.ok(service.authenticate(request));
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(new ErrorResponse("Неверное имя или пароль."), HttpStatus.BAD_REQUEST);
        }
    }
}
