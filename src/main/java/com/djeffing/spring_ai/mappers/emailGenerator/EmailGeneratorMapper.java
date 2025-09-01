package com.djeffing.spring_ai.mappers.emailGenerator;

import com.djeffing.spring_ai.dtos.emailGenerator.EmailGeneratorDto;
import com.djeffing.spring_ai.dtos.emailGenerator.EmailGeneratorRequest;
import com.djeffing.spring_ai.models.EmailGenerator;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EmailGeneratorMapper {
    EmailGeneratorDto toEmailGeneratorDto(EmailGenerator emailGenerator);
    EmailGeneratorDto emailGeneratorRequestToEmailGeneratorDto(EmailGeneratorRequest emailGeneratorRequest);
    EmailGenerator toEmailGenerator(EmailGeneratorDto emailGeneratorDto);
}
