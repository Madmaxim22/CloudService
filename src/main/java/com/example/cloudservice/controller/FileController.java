package com.example.cloudservice.controller;

import com.example.cloudservice.controller.response.ErrorResponse;
import com.example.cloudservice.model.file.FileDB;
import com.example.cloudservice.model.file.FileDto;
import com.example.cloudservice.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class FileController {
    private final FileStorageService fileStorageService;

    @PostMapping("/file")
    public ResponseEntity<?> upload(@RequestParam String filename,
                                 @RequestParam MultipartFile file) {
        try {
            fileStorageService.save(filename,file);
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

    @GetMapping("/file")
    public ResponseEntity<?> download(@RequestParam String filename) {
        try {
            FileDB fileDB = fileStorageService.download(filename);
            return new ResponseEntity<>(fileDB.getData(), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/file")
    public ResponseEntity<?> editFileName(@RequestParam String filename,
                                 @RequestBody Map<String, Object> payload) {
        try {
            fileStorageService.editFileName(filename, payload);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NullPointerException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/list")
    public ResponseEntity<?> lists(@RequestParam Integer limit) {
        try {
            List<FileDto> files = fileStorageService.lists(limit);
            return new ResponseEntity<>(files, HttpStatus.OK);
        }  catch (IllegalArgumentException | NullPointerException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
