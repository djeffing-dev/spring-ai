package com.djeffing.spring_ai.services.interfaces;

import com.djeffing.spring_ai.dtos.emailGenerator.EmailGeneratorDto;
import com.djeffing.spring_ai.dtos.RoadMapDto;
import reactor.core.publisher.Flux;

import java.util.List;

public interface GptService {
    public  Flux<String> getRoadmap(RoadMapDto roadMapDto);
    public Flux<String> prompt(List<String> messages);
    public String emailGenerator(EmailGeneratorDto emailGeneratorDto);
}
