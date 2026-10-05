package com.klu.userregistration.controller;

import com.klu.userregistration.entity.User;
import com.klu.userregistration.service.UserService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(
            @Valid @RequestBody User user) {

        String result = userService.registerUser(user);

        if (result.equals("Username already exists") ||
            result.equals("Email already exists")) {

            return ResponseEntity.badRequest().body(result);
        }

        return ResponseEntity.ok(result);
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(
            @RequestParam String username,
            @RequestParam String password) {

        String result = userService.loginUser(username, password);

        if (result.equals("Invalid username or password")) {
            return ResponseEntity.badRequest().body(result);
        }

        return ResponseEntity.ok(result);
    }
}