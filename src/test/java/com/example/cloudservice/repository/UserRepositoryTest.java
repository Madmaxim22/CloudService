package com.example.cloudservice.repository;

import com.example.cloudservice.model.authentication.Role;
import com.example.cloudservice.model.authentication.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/data/create-user-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/data/create-user-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    void givenUserObject_whenSave_thenUserObject() {
        User user = User.builder()
                .firstname("maksim")
                .lastname("trubkin")
                .email("maksim@mail.com")
                .password("$2y$10$gL5cNkdWdsB.Q44E5FsCt.fKLBYpg5uVH3Q83aX/lUFsGiz53G1Om")
                .role(Role.USER).build();

        User saveUser = userRepository.save(user);

        assertNotNull(saveUser);
        assertEquals(user, saveUser);
    }

    @Test
    void givenUserObject_whenFindByEmail_thenUserObject() {
        User saveUser = userRepository.findByEmail("test@mail.com").get();

        assertNotNull(saveUser);
        assertEquals("petr", saveUser.getFirstname());
        assertEquals("semin", saveUser.getLastname());
        assertEquals(Role.USER, saveUser.getRole());
    }
}