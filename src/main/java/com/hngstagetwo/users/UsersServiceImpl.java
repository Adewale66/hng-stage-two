package com.hngstagetwo.users;

import com.hngstagetwo.dtos.RegisterDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersService{

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Optional<User> findByEmail(String email) {
        return usersRepository.findByEmail(email);
    }

    @Override
    public User save(RegisterDto registerDto) {
        var user = User.
                builder()
                .lastName(registerDto.lastName())
                .firstName(registerDto.firstName())
                .phone(registerDto.phone())
                .email(registerDto.email())
                .password(passwordEncoder.encode(registerDto.password()))
                .build();
        return usersRepository.save(user);
    }

    @Override
    public ResponseEntity<List<User>> findAll() {
        return new ResponseEntity<>(usersRepository.findAll(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> getUser(Long id) {
        return null;
    }
}
