package com.djeffing.spring_ai.controllers;

import com.djeffing.spring_ai.dtos.jwt.JwtResponse;
import com.djeffing.spring_ai.dtos.login.LoginRequest;
import com.djeffing.spring_ai.dtos.register.RegisterRequest;
import com.djeffing.spring_ai.dtos.users.UserResponseDto;
import com.djeffing.spring_ai.services.interfaces.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentification", description = "API pour la création de compte et l'authentification des utilisateurs")
public class AuthController {
    final private AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(
            summary = "Authentification d'un utilisateur",
            description = "Génère les informations de l'utilisateur connecté (username, email, roles) ainsi qu'un token d'authentification",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Utilisateur connecté avec succès"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Email ou mot de passe incorrect"
                    ),
            }
    )
    @PostMapping("/signin")
    public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return authService.authenticateUser(loginRequest);
    }

    @Operation(
            summary = "Enregistrement d'un nouvel utilisateur",
            description = "Enregistre un nouvel utilisateur et vérifie l'unicité de son nom d'utilisateur et de son email",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Utilisateur enregistré avec succès"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Nom d'utilisateur ou email déjà utilisé"
                    )
            }
    )
    @PostMapping("/signup")
    public ResponseEntity<UserResponseDto> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        return authService.registerUser(registerRequest);
    }

}
