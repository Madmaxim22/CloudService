package com.example.cloudservice.controller;

import com.example.cloudservice.controller.autentication.ErrorResponse;
import com.example.cloudservice.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class FileController {
    private final FileStorageService fileStorageService;

    @PostMapping("/file")
    public ResponseEntity<?> upload(@RequestParam String filename,
                                    @RequestParam MultipartFile file) {
        try {
            fileStorageService.save(filename, file);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IOException | IllegalArgumentException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/file")
    public ResponseEntity<?> delete(@RequestParam String filename) {
        try {
            fileStorageService.delete(filename);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
