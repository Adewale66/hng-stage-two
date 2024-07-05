package com.hngstagetwo.users;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UsersController {

    private final UsersService usersService;


    @GetMapping
    public ResponseEntity<List<User>> getUsers() {
        return usersService.findAll();
    }

    @GetMapping("users/{id}")
    public ResponseEntity<Object> getUser(@PathVariable("id") String id) {
        return  usersService.getUser(Long.valueOf(id));
    }
}
