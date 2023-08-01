package com.example.cloudservice.repository;

import com.example.cloudservice.model.authentication.User;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Cacheable("users")
    Optional<User> findByEmail(String email);
}
