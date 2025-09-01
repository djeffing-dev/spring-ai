package com.djeffing.spring_ai.dtos.jwt;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class JwtResponse {
    private String token;
    private String type ="Bearer ";
    private String username;
    private String email;
    private List<String> roles;
}
