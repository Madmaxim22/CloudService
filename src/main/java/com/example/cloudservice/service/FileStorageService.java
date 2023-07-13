package com.example.cloudservice.service;

import com.example.cloudservice.model.authentication.User;
import com.example.cloudservice.repository.FileDBRepository;
import com.example.cloudservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FileStorageService {
    private final FileDBRepository fileDBRepository;
    private final UserRepository userRepository;

    private User getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        return userRepository.findByEmail(currentPrincipalName).get();
    }

}
