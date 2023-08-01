package com.example.cloudservice.controller.response;

import com.example.cloudservice.controller.response.Response;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponse implements Response {
    private String message;
    private final int id = 0;
}
