package com.djeffing.spring_ai.dtos.error;

import org.springframework.http.HttpStatus;

public record ErrorDto(HttpStatus httpStatus) {
}
