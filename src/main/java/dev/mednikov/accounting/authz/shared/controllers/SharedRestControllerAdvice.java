package dev.mednikov.accounting.authz.shared.controllers;

import dev.mednikov.accounting.authz.roles.exceptions.UserRoleDoesNotExistException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class SharedRestControllerAdvice {

    @ExceptionHandler(UserRoleDoesNotExistException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String userRoleDoesNotExistHandler (UserRoleDoesNotExistException ex){
        return ex.getMessage();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> validationExceptionHandler (MethodArgumentNotValidException ex){
        Map<String, String> result = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String field = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            result.put(field, message);
        });
        return result;
    }

}
