package com.example.cloudservice.controller;

import com.example.cloudservice.model.authentication.User;
import com.example.cloudservice.model.file.FileDB;
import com.example.cloudservice.model.file.FileDto;
import com.example.cloudservice.service.FileStorageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles(value = "test")
@TestPropertySource("/application-test.properties")
class FileControllerTest {

    @Autowired
    private MockMvc mvc;
    @MockBean
    private FileStorageService fileStorageService;

    @Test
    void whenUpload_thenIsOk() throws Exception {
        MockMultipartFile multipartFile = new MockMultipartFile(
                "file", "test.txt", "text/plain", "Spring Framefork".getBytes());

        this.mvc.perform(multipart("/file")
                        .file(multipartFile)
                        .param("filename", "123.txt"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void whenUploadRequestParamFilenameIsNotPresent_thenBadRequest() throws Exception {
        MockMultipartFile multipartFile = new MockMultipartFile(
                "file", "test.txt", "text/plain", "Spring Framefork".getBytes());

        this.mvc.perform(multipart("/file")
                        .file(multipartFile))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenUploadRequestParamMultipartFileIsEmpty_thenBadRequest() throws Exception {
        MockMultipartFile multipartFile = new MockMultipartFile("name", "name".getBytes());

        this.mvc.perform(multipart("/file")
                        .file(multipartFile)
                        .param("filename", "123.txt"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenDelete_thenIsOk() throws Exception {
        this.mvc.perform(delete("/file")
                        .param("filename", "123.txt"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void whenDeleteRequestParamFilenameIsNotPresent_thenBadRequest() throws Exception {
        this.mvc.perform(delete("/file"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenDownload_thenIsOk() throws Exception {
        var fileDB = FileDB.builder()
                .name("123.txt")
                .type("application/octet-stream")
                .size(7054L)
                .data("123.txt".getBytes())
                .user(new User())
                .build();

        Mockito.when(fileStorageService.download("123.txt")).thenReturn(fileDB);

        this.mvc.perform(get("/file")
                        .param("filename", "123.txt"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/octet-stream"))
                .andExpect(content().bytes("123.txt".getBytes()));
    }

    @Test
    void whenDownloadRequestParamFilenameIsNotPresent_thenBadRequest() throws Exception {
        this.mvc.perform(get("/file"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenDownloadRequestParamFilenameIsNotPresent_thenInternalServerError() throws Exception {
        Mockito.when(fileStorageService.download("123.txt")).thenThrow(new RuntimeException());

        this.mvc.perform(get("/file")
                        .param("filename", "123.txt"))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }

    @Test
    void whenEditFileNameBodyNameIsEmpty_thenBadRequest() throws Exception {
        Map<String, Object> payload = new HashMap<>();
        payload.put("name", "123.pdf");
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(payload);

        doThrow(new NullPointerException())
                .when(fileStorageService)
                .editFileName(anyString(), anyMap());

        this.mvc.perform(put("/file")
                        .param("filename", "123.txt")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenList_thenListFileDBObject() throws Exception {
        Mockito.when(fileStorageService.lists(2)).thenReturn(List.of(
                new FileDto("for.txt", 233L),
                new FileDto("wer.pdf", 322L)));

        mvc.perform(get("/list")
                        .param("limit", "2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].filename", containsInAnyOrder("for.txt", "wer.pdf")))
                .andExpect(jsonPath("$[0].filename").value("for.txt"))
                .andExpect(jsonPath("$[0].size").value(233))
                .andExpect(jsonPath("$[1].filename").value("wer.pdf"))
                .andExpect(jsonPath("$[1].size").value(322));
    }
}