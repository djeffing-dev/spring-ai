package com.djeffing.spring_ai.controllers;

import com.djeffing.spring_ai.dtos.login.LoginRequest;
import com.djeffing.spring_ai.dtos.register.RegisterRequest;
import com.djeffing.spring_ai.services.interfaces.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    final private AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid  @RequestBody LoginRequest loginRequest){
        return authService.authenticateUser(loginRequest);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid  @RequestBody RegisterRequest registerRequest){
        return authService.registerUser(registerRequest);
    }


}
