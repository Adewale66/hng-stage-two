package com.hngstagetwo.configuration;

import com.hngstagetwo.errors.InvalidCredentialException;
import com.hngstagetwo.errors.ResourceNotFound;
import com.hngstagetwo.errors.ValidationError;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestControllerAdvice
public class GlobalErrorHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseEntity<Object> handleNotValidError(MethodArgumentNotValidException exception) {


        List<ObjectError> allErrors = exception.getBindingResult().getAllErrors();
        List<ValidationError> validationErrors = new ArrayList<>();

        String regex = "default message \\[(.*?)]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher;

        for (ObjectError error : allErrors) {
            matcher = pattern.matcher(Arrays.toString(error.getArguments()));
            if (matcher.find()) {
                validationErrors.add(
                        new ValidationError(matcher.group(1), error.getDefaultMessage())
                );
            }
        }

        Map<String, List<ValidationError>> response = new HashMap<>();
        response.put("errors", validationErrors);
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> dataIntegrityException() {
        Map<String, Object> response = new HashMap<>() {{
            put("status", "Bad request");
            put("message", "Registration unsuccessful");
            put("statusCode", 400);
        }};
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(InvalidCredentialException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<Object> invalidCredentials() {
        Map<String, Object> response = new HashMap<>() {{
            put("status", "Bad request");
            put("message", "Authentication failed");
            put("statusCode", 401);
        }};
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(ResourceNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> resourceNotFound() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new HashMap<>() {{
            put("message", "Resource not found");
        }});
    }
}