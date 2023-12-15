package com.caykhe.itforum.configs;

import com.caykhe.itforum.dtos.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@RequiredArgsConstructor
public class AOPConfig {
    private final MessageSource fieldMessageSource;

    @ExceptionHandler({ApiException.class})
    public ResponseEntity<?> handleException(final ApiException exception) {
        return new ResponseEntity<>(exception.getMessage(), exception.getStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String displayName = getDisplayName(error);
            String errorMessage = error.getDefaultMessage();
            errors.put(displayName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    public String getDisplayName(ObjectError error) {
        String fieldName = ((FieldError) error).getField();
        String objectName = error.getObjectName();
        String field = objectName + "." + fieldName;

        try {
            return fieldMessageSource.getMessage(field, null, LocaleContextHolder.getLocale());
        } catch (NoSuchMessageException e) {
            return fieldName;
        }
    }
}