package com.djeffing.spring_ai.controllers;

import com.djeffing.spring_ai.configs.securities.userDetails.UserDetailsImpl;
import com.djeffing.spring_ai.dtos.emailGenerator.EmailGeneratorDto;
import com.djeffing.spring_ai.models.User;
import com.djeffing.spring_ai.services.interfaces.EmaiGeneratorService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequestMapping("api/emailGenrator")
@Validated
public class EmailGeneratorController {

    final private EmaiGeneratorService emaiGeneratorService;

    public EmailGeneratorController(EmaiGeneratorService emaiGeneratorService) {
        this.emaiGeneratorService = emaiGeneratorService;
    }

    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public String create(@RequestBody  EmailGeneratorDto emailGeneratorDto){
        return  emaiGeneratorService.create(emailGeneratorDto);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<EmailGeneratorDto> findAll(){
        return emaiGeneratorService.findAll();
    }

    @PutMapping
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public String update(@RequestParam  long id, @RequestBody EmailGeneratorDto emailGeneratorDto){
        return emaiGeneratorService.update(id, emailGeneratorDto);
    }

    @GetMapping("/findByUserId")
    @PreAuthorize("hasRole('ADMIN')")
    public List<EmailGeneratorDto> findByUserId(@RequestParam Long id){
        return emaiGeneratorService.findByUserId(id);
    }

    @GetMapping("/findByUserToken")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public List<EmailGeneratorDto> findByUserToken(){
        return emaiGeneratorService.findByUserToken();
    }


    @GetMapping("/findById")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public EmailGeneratorDto findById(@RequestParam long id){
        return emaiGeneratorService.findById(id);
    }
    @DeleteMapping("/deleteById")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteById(@RequestParam Long id){
        return emaiGeneratorService.deleteById(id);
    }
}
