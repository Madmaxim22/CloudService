package com.example.cloudservice.controller;

import com.example.cloudservice.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class FileController {
    private final FileStorageService fileStorageService;


}
