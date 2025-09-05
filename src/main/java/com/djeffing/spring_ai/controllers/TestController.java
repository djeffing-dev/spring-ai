package com.djeffing.spring_ai.controllers;

import com.djeffing.spring_ai.configs.rateLimit.RateLimit;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
@Tag(name = "Test")
public class TestController {
    @GetMapping("/all")
    @RateLimit(limit = 4, duration = 86400) // 4 requêtes / 24h
    public String allAcess(){
        return "Public Content";
    }

    @GetMapping("/user")
    @Hidden
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public String userAccess(){
        return "User Content";
    }

    @GetMapping("/mod")
    @PreAuthorize("hasRole('MODERATOR')")
    @Hidden
    public String moderatorAccess() {
        return "Moderator Board.";
    }

    @GetMapping("/admin")
    @Hidden
    @PreAuthorize("hasRole('ADMIN')")
    public String adminAccess() {
        return "Admin Board.";
    }
}
