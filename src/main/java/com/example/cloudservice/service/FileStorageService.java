package com.example.cloudservice.service;

import com.example.cloudservice.model.authentication.User;
import com.example.cloudservice.model.file.FileDB;
import com.example.cloudservice.model.file.FileDto;
import com.example.cloudservice.repository.FileDBRepository;
import com.example.cloudservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
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
        log.info("User: {} save {} in data base.", getUser().getEmail(), filename);
        return fileDB;
    }

    @Transactional
    public long delete(String filename) {
        long delete = fileDBRepository.deleteByNameAndUser_Id(filename, getUser().getId());
        log.info("User: {} delete {} in data base.", getUser().getEmail(), filename);
        return delete;
    }


    public FileDB download(String filename) {
        FileDB fileDB = fileDBRepository.findFirstByNameAndUser_Id(filename, getUser().getId()).orElseThrow();
        log.info("User: {} download {} in data base.", getUser().getEmail(), filename);
        return fileDB;
    }

    @Transactional
    public FileDB editFileName(String filename, Map<String, Object> payload) {
        FileDB editFile = fileDBRepository.findFirstByNameAndUser_Id(filename, getUser().getId()).orElseThrow();
        editFile.setName((String) payload.get("filename"));
        fileDBRepository.save(editFile);
        log.info("User: {} edit filename {} in data base.", getUser().getEmail(), editFile.getName());
        return editFile;
    }

    public List<FileDto> lists(Integer limit) {
        List<FileDB> files = fileDBRepository.findAllByUser_Id(getUser().getId());
        return files.stream()
                .map(i -> new FileDto(i.getName(), i.getSize())).limit(limit).toList();
    }

    public User getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        return userRepository.findByEmail(currentPrincipalName).get();
    }

}
