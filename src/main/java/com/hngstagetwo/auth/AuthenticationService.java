package com.hngstagetwo.auth;

import com.hngstagetwo.dtos.LoginDto;
import com.hngstagetwo.dtos.RegisterDto;
import com.hngstagetwo.dtos.UserResponseDto;
import com.hngstagetwo.errors.InvalidCredentialException;
import com.hngstagetwo.jwt.JwtService;
import com.hngstagetwo.organisation.Organisation;
import com.hngstagetwo.organisation.OrganisationService;
import com.hngstagetwo.users.User;
import com.hngstagetwo.users.UsersService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UsersService usersService;
    private final OrganisationService organisationService;
    private final ModelMapper modelMapper;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public ResponseEntity<Object> login(LoginDto loginDto) {
        User user = usersService.findByEmail(loginDto.email()).orElseThrow(InvalidCredentialException::new);

        if (!passwordEncoder.matches(loginDto.password(), user.getPassword())) {
            throw new InvalidCredentialException();
        }
        String jwt = jwtService.generateToken(user);
        UserResponseDto userResponseDto = modelMapper.map(user, UserResponseDto.class);
        Map<String, Object> response = response("Login", jwt, userResponseDto);

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    public ResponseEntity<Object> register(RegisterDto registerDto) {
        User user = usersService.create(registerDto);
        Organisation organisation = organisationService.create(user.getFirstName());

        Set<User> members = new HashSet<>() {{
            add(user);
        }};
        Set<Organisation> organisations = new HashSet<>() {{
            add(organisation);
        }};
        user.setOrganisations(organisations);
        organisation.setMembers(members);

        usersService.save(user);
        organisationService.save(organisation);

        UserResponseDto userResponseDto = modelMapper.map(user, UserResponseDto.class);
        var jwt = jwtService.generateToken(user);
        var response = response("Registration", jwt, userResponseDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    private Map<String, Object> response(String type, String accessToken, UserResponseDto userResponseDto) {
        var message = String.format("%s successful", type);
        Map<String, Object> data = new HashMap<>() {{
            put("accessToken", accessToken);
            put("user", userResponseDto);
        }};

        return new HashMap<>() {{
            put("status", "success");
            put("message", message);
            put("data", data);
        }};


    }
}
