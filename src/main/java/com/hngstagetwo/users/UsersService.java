package com.hngstagetwo.users;

import com.hngstagetwo.dtos.RegisterDto;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UsersService {
    Optional<User> findByEmail(String email);

    User create(RegisterDto registerDto);

    Optional<User> findById(UUID id);

    ResponseEntity<Object> getUser(UUID id, String username);

    void save(User user);
}