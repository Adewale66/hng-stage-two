package com.hngstagetwo.auth;

import com.hngstagetwo.dtos.LoginDto;
import com.hngstagetwo.dtos.RegisterDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService service;


    @PostMapping("register")
    public ResponseEntity<Object> register(@RequestBody @Valid RegisterDto registerDto) {
        return service.register(registerDto);
    }

    @PostMapping("login")
    public ResponseEntity<Object> login(@RequestBody @Valid LoginDto loginDto) throws Exception {
        return service.login(loginDto);
    }
}
