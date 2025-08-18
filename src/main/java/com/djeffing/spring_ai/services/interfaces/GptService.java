package com.djeffing.spring_ai.services.interfaces;

import com.djeffing.spring_ai.dtos.EmailGeneratorDto;
import com.djeffing.spring_ai.dtos.RoadMapDto;
import reactor.core.publisher.Flux;

import java.util.List;

public interface GptService {
    public  Flux<String> getRoadmap(RoadMapDto roadMapDto);
    public Flux<String> prompt(List<String> messages);
    public Flux<String> emailGenerator(EmailGeneratorDto emailGeneratorDto);
}
