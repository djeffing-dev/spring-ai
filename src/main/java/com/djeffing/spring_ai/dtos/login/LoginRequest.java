package com.djeffing.spring_ai.dtos.login;

import lombok.Builder;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Builder
public class LoginRequest {
    @NotBlank
    private String email;
    @NotBlank
    private String password;
}
