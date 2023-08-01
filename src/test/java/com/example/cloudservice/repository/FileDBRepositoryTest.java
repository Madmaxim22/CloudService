package com.example.cloudservice.repository;

import com.example.cloudservice.model.file.FileDB;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/data/create-user-before.sql", "/data/create-file-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/data/create-file-after.sql", "/data/create-user-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class FileDBRepositoryTest {

    @Autowired
    private FileDBRepository fileDBRepository;

    @Test
    void givenUserId_whenFindByuser_Id_thenListFileDB() {
        List<FileDB> fileDBList = fileDBRepository.findAllByUser_Id(1L);

        assertNotNull(fileDBList);
        assertEquals(fileDBList.size(), 2);
        assertEquals(fileDBList.get(0).getName(), "123.txt");
        assertEquals(fileDBList.get(1).getName(), "fludland.torrent");
    }

    @Test
    void givenUserIdAndFilename_whenFirstByNameAndUser_Id_thenFileDBObject() {
        FileDB saveFileDB = fileDBRepository.findFirstByNameAndUser_Id("123.txt", 1L).get();

        assertNotNull(saveFileDB);
        assertEquals(saveFileDB.getName(), "123.txt");
        assertEquals(saveFileDB.getType(), "application/pdf");
        assertEquals(saveFileDB.getSize(), 467321);
    }

    @Test
    void givenLong_whenFirstByNameAndUser_Id_thenFileDBObject() {
        long delete = fileDBRepository.deleteByNameAndUser_Id("fludland.torrent", 1L);

        assertEquals(delete, 1);
    }
}