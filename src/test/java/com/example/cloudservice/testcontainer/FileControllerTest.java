package com.example.cloudservice.testcontainer;

import com.example.cloudservice.model.authentication.User;
import com.example.cloudservice.model.file.FileDB;
import com.example.cloudservice.repository.FileDBRepository;
import com.example.cloudservice.service.FileStorageService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(value = "test")
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource("/application-test.properties")
@Sql(value = {"/data/create-user-before.sql", "/data/create-file-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/data/create-file-after.sql", "/data/create-user-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class FileControllerTest extends MySQLContainerBaseTest {

    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private FileDBRepository repository;
    @Autowired
    private MockMvc mvc;
    @MockBean
    private FileStorageService service;

    @Test
    void whenUpload_thenIsOk() throws Exception {
        MockMultipartFile multipartFile = new MockMultipartFile(
                "file", "test.txt", "text/plain", "Spring Framework".getBytes());

        Mockito.when(service.getUser()).thenReturn(new User());

        this.mvc.perform(multipart("/file")
                        .file(multipartFile)
                        .param("filename", "123.txt"))
                .andDo(print())
                .andExpect(status().isOk());
    }


    @Test
    void whenDelete_thenIsOk() {
        String url = "http://localhost:" + port + "/list?filename=123.txt";

        restTemplate.delete(url);

        Optional<FileDB> fileDB = repository.findFirstByNameAndUser_Id("123.txt", 1L);

        assertTrue(fileDB.isPresent());
    }

    @Test
    void whenDownload_thenIsOk() {
        String url = "http://localhost:" + port + "/list?filename=123.txt";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void whenEditFileNameBodyNameIsEmpty_thenBadRequest() {
        String url = "http://localhost:" + port + "/list?filename=123.txt";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("name", "file.txt");
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(map, headers);
        restTemplate.put(url, request);

        Optional<FileDB> fileDB = repository.findFirstByNameAndUser_Id("123.txt", 1L);
        assertTrue(fileDB.isPresent());
    }

    @Test
    void whenList_thenListFileDBObject() {
        String url = "http://localhost:" + port + "/list?limit=3";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}