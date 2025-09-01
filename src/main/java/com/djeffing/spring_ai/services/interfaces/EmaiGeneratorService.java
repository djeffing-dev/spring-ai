package com.djeffing.spring_ai.services.interfaces;

import com.djeffing.spring_ai.dtos.emailGenerator.EmailGeneratorDto;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;

import java.util.List;

public interface EmaiGeneratorService {
    public String create(EmailGeneratorDto emailGeneratorDto);
    public List<EmailGeneratorDto> findAll();
    public String update(long emailGenerator_id, EmailGeneratorDto emailGeneratorDto);
    public List<EmailGeneratorDto> findByUserId(Long user_id);
    public List<EmailGeneratorDto> findByUserToken();
    public EmailGeneratorDto findById(long emailGenerator_id);
    public ResponseEntity<?> deleteById(Long emailGenerator_id);

}
