package com.hngstagetwo.users;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UsersController {

    private final UsersService usersService;


    @GetMapping
    public ResponseEntity<List<User>> getUsers() {
        return usersService.findAll();
    }

    @GetMapping("users/{id}")
    public ResponseEntity<Object> getUser(@PathVariable("id") UUID id, Principal principal) {
        return  usersService.getUser(id, principal.getName());
    }
}
