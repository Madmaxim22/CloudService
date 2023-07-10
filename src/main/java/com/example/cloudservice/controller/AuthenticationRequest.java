package com.example.cloudservice.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class  AuthenticationRequest {
    @JsonProperty("login")
    private String email;
    private String password;
}
