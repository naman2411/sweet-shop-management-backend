package com.example.demo;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return authService.registerUser(user);
    }
    @PostMapping("/login")
    public User login(@RequestBody User loginRequest) {
        return authService.login(loginRequest.getUsername(), loginRequest.getPassword());
    }
}