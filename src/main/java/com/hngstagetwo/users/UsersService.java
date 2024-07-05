package com.hngstagetwo.users;

import com.hngstagetwo.dtos.RegisterDto;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface UsersService {
    Optional<User> findByEmail(String email);
    User save(RegisterDto registerDto);

    ResponseEntity<List<User>> findAll();

    ResponseEntity<Object> getUser(Long id);
}
