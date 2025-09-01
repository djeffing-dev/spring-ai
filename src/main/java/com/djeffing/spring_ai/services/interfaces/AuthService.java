package com.djeffing.spring_ai.services.interfaces;


import com.djeffing.spring_ai.dtos.jwt.JwtResponse;
import com.djeffing.spring_ai.dtos.login.LoginRequest;
import com.djeffing.spring_ai.dtos.register.RegisterRequest;
import com.djeffing.spring_ai.dtos.users.UserResponseDto;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    public ResponseEntity<JwtResponse> authenticateUser(LoginRequest loginRequest);
    public ResponseEntity<UserResponseDto> registerUser(RegisterRequest registerRequest);
}
