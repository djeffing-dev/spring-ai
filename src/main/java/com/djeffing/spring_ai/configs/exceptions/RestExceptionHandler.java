package com.djeffing.spring_ai.configs.exceptions;

import com.djeffing.spring_ai.dtos.error.ErrorDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class RestExceptionHandler {
@ExceptionHandler(value = {AppException.class})
    @ResponseBody
    public ResponseEntity<ErrorDto> handleException(AppException e){
    return  ResponseEntity.status(e.getHttpStatus())
            .body(new  ErrorDto(e.getHttpStatus()));
}
}
