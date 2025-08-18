package com.djeffing.spring_ai.controllers;

import com.djeffing.spring_ai.dtos.EmailGeneratorDto;
import com.djeffing.spring_ai.dtos.RoadMapDto;
import com.djeffing.spring_ai.services.interfaces.GptService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequestMapping("/gpt")
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
    public Flux<String> emailGenerator(@RequestBody EmailGeneratorDto emailGeneratorDto){
        return  gptService.emailGenerator(emailGeneratorDto);
    }

    @PostMapping("/prompt")
    public Flux<String> prompt(@RequestBody List<String> messages){
        return gptService.prompt(messages);
    }




}
