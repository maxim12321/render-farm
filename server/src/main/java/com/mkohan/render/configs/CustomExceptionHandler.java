package com.mkohan.render.configs;

import com.mkohan.render.dtos.ErrorDto;
import com.mkohan.render.exceptions.BadCredentialsException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {BadCredentialsException.class, UsernameNotFoundException.class})
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public ErrorDto handleBadCredentialsException(RuntimeException e) {
        return new ErrorDto(e.getMessage());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        String errorMessage = e.getBindingResult().getAllErrors().stream()
                .map(FieldError.class::cast)
                .map(FieldError::getDefaultMessage)
                .reduce((m1, m2) -> m1 + ", " + m2)
                .orElse("");

        return ResponseEntity.badRequest().body(new ErrorDto(errorMessage));
    }
}
