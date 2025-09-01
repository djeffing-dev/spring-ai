package com.djeffing.spring_ai.controllers;

import com.djeffing.spring_ai.dtos.emailGenerator.EmailGeneratorDto;
import com.djeffing.spring_ai.dtos.RoadMapDto;
import com.djeffing.spring_ai.services.interfaces.GptService;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequestMapping("api/gpt/")
@Hidden
public class GptController {
    final  private GptService gptService;

    public GptController(GptService gptService) {
        this.gptService = gptService;
    }

    @PostMapping("/getRoadmap")
    public Flux<String> getRoadmap(@RequestBody RoadMapDto roadMapDto){
        return gptService.getRoadmap(roadMapDto);
    }

    @PostMapping("/emailGenerator")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public String emailGenerator(@RequestBody EmailGeneratorDto emailGeneratorDto){
        return  gptService.emailGenerator(emailGeneratorDto);
    }

    @PostMapping("/prompt")
    public String prompt(@RequestBody List<String> messages){
        return gptService.prompt(messages);
    }




}
