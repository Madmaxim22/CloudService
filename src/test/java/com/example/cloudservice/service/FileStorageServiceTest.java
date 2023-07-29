package com.example.cloudservice.service;

import com.example.cloudservice.model.authentication.User;
import com.example.cloudservice.model.file.FileDB;
import com.example.cloudservice.repository.FileDBRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;


class FileStorageServiceTest {

    private FileDB fileDB;
    @MockBean
    private FileDBRepository fileDBRepository;

    @BeforeEach
    public void createFile() {
        fileDB = FileDB.builder()
                .name("1.png")
                .type("image/png")
                .data("1.png".getBytes())
                .size(22411L)
                .user(new User())
                .build();
    }

    @Test
    void save() {
    }

    @Test
    void delete() {
    }

    @Test
    void download() {
    }

    @Test
    void editFileName() {
    }

    @Test
    void lists() {
    }
}