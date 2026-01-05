package com.djeffing.spring_ai.controllers;


import com.djeffing.spring_ai.dtos.oauth.google.UrlDto;
import com.djeffing.spring_ai.services.imp.GoogleAuthServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/google/auth")
public class GoogleAuthController {
    private final GoogleAuthServiceImpl googleAuthService;

    public GoogleAuthController(GoogleAuthServiceImpl googleAuthService) {
        this.googleAuthService = googleAuthService;
    }

    @GetMapping("/getUrl")
    public ResponseEntity<UrlDto> createAuthUrl(){
        return googleAuthService.createAuthUrl();
    }

    @GetMapping("/getToken")
    public ResponseEntity<?> callBack(@RequestParam String code){
        return googleAuthService.callBack(code);
    }

}
