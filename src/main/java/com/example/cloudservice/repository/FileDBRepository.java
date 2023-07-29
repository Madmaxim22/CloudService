package com.example.cloudservice.repository;

import com.example.cloudservice.model.file.FileDB;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileDBRepository extends JpaRepository<FileDB, Long> {
    List<FileDB> findAllByUser_Id(Long userId);

    Optional<FileDB> findFirstByNameAndUser_Id(String filename, long userId);

    @Transactional
    long deleteByNameAndUser_Id(String filename, long userId);
}
