package com.hngstagetwo.users;

import com.hngstagetwo.dtos.RegisterDto;
import com.hngstagetwo.dtos.UserResponseDto;
import com.hngstagetwo.errors.ResourceNotFound;
import com.hngstagetwo.organisation.Organisation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @Override
    public Optional<User> findByEmail(String email) {
        return usersRepository.findByEmail(email);
    }

    @Override
    public User create(RegisterDto registerDto) {
        return User.
                builder()
                .lastName(registerDto.lastName())
                .firstName(registerDto.firstName())
                .phone(registerDto.phone())
                .email(registerDto.email())
                .password(passwordEncoder.encode(registerDto.password()))
                .build();
    }

    @Override
    public Optional<User> findById(UUID id) {
        return usersRepository.findById(id);
    }

    @Override
    public ResponseEntity<Object> getUser(UUID id, String username) {
        var user = usersRepository.findByEmail(username).orElseThrow(ResourceNotFound::new);
        var returnUser = usersRepository.findById(id).orElseThrow(ResourceNotFound::new);
        boolean found = false;
        for (Organisation organisation : user.organisations) {
            if (organisation.getMembers().contains(returnUser)) {
                found = true;
                break;
            }
        }
        if (!found) {
            throw new ResourceNotFound();
        }
        UserResponseDto userResponseDto = modelMapper.map(returnUser, UserResponseDto.class);
        var response = new HashMap<>() {{
            put("status", "success");
            put("message", "Retrieval successful");
            put("data", userResponseDto);
        }};
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public void save(User user) {
        usersRepository.save(user);
    }
}
