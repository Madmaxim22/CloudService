package com.example.cloudservice.service;

import com.example.cloudservice.repository.FileDBRepository;
import com.example.cloudservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FileStorageService {
    private final FileDBRepository fileDBRepository;
    private final UserRepository userRepository;
}
