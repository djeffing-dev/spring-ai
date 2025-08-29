package com.djeffing.spring_ai.services.interfaces;


import com.djeffing.spring_ai.dtos.login.LoginRequest;
import com.djeffing.spring_ai.dtos.register.RegisterRequest;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    public ResponseEntity<?> authenticateUser(LoginRequest loginRequest);
    public ResponseEntity<?> registerUser(RegisterRequest registerRequest);
}
