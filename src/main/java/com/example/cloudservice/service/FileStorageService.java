package com.example.cloudservice.service;

import com.example.cloudservice.model.authentication.User;
import com.example.cloudservice.model.file.FileDB;
import com.example.cloudservice.repository.FileDBRepository;
import com.example.cloudservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class FileStorageService {
    private final FileDBRepository fileDBRepository;
    private final UserRepository userRepository;

    @Transactional
    public FileDB save(String filename, MultipartFile file) throws IOException {
        var fileDB = FileDB.builder()
                .name(filename)
                .type(file.getContentType())
                .size(file.getSize())
                .data(file.getBytes())
                .user(getUser())
                .build();
        fileDBRepository.save(fileDB);
        return fileDB;
    }

    @Transactional
    public long delete(String filename) {
        return fileDBRepository.deleteByNameAndUser_Id(filename, getUser().getId());
    }


    public FileDB download(String filename) {
        return fileDBRepository.findFirstByNameAndUser_Id(filename, getUser().getId()).orElseThrow();
    }

    private User getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        return userRepository.findByEmail(currentPrincipalName).get();
    }

}
