package com.hngstagetwo.configuration;

import com.hngstagetwo.errors.InvalidCredentialException;
import com.hngstagetwo.errors.ResourceNotFound;
import com.hngstagetwo.errors.ValidationError;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ControllerAdvice
public class GlobalErrorHandler {
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object>handleNotValidError(MethodArgumentNotValidException exception) {


        List<ObjectError> allErrors = exception.getBindingResult().getAllErrors();
        List<ValidationError> validationErrors = new ArrayList<>();

        String regex = "default message \\[(.*?)]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher;

        for( ObjectError error : allErrors )
        {
            matcher = pattern.matcher(Arrays.toString(error.getArguments()));
            if(matcher.find()) {
                validationErrors.add(
                        new ValidationError(matcher.group(1), error.getDefaultMessage() )
                );
            }
        }

        Map<String, List<ValidationError>> response = new HashMap<>();
        response.put("errors", validationErrors);
        return  new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> dataIntegrityException() {
        Map<String, Object> response = new HashMap<>(){{
            put("status", "Bad request");
            put("message", "Registration unsuccessful");
            put("statusCode", 400);
        }};
        return  new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidCredentialException.class)
    public ResponseEntity<Object> invalidCredentials() {
        Map<String, Object> response = new HashMap<>(){{
            put("status", "Bad request");
            put("message", "Authentication failed");
            put("statusCode", 401);
        }};
        return  new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ResourceNotFound.class)
    public ResponseEntity<Object> resourceNotFound() {
        return new ResponseEntity<>("Resource not found", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> catchAll() {
        return new ResponseEntity<>("Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
