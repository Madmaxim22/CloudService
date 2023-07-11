package com.example.cloudservice.controller.autentication;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponse implements Response{
    private String message;
    private final int id = 0;
}
