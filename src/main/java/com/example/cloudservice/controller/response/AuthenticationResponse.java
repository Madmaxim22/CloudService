package com.example.cloudservice.controller.response;

import com.example.cloudservice.controller.response.Response;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse implements Response {
    @JsonProperty("auth-token")
    private String token;
}
