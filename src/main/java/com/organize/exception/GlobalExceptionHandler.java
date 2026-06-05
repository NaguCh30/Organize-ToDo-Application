package com.organize.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.organize.dto.response.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailAlreadyExistsException.class) 
    public ResponseEntity<ErrorResponse> handleEmailAlreadyExists(EmailAlreadyExistsException ex) {
        ErrorResponse response = new ErrorResponse(
                                    LocalDateTime.now(), 
                                    HttpStatus.CONFLICT.value(), 
                                    "Email Already Exists", 
                                    ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                                .body(response);
    }

    @ExceptionHandler(PasswordMismatchException.class)
    public ResponseEntity<ErrorResponse> handlePasswordMismatch(PasswordMismatchException ex) {
        ErrorResponse response = new ErrorResponse(LocalDateTime.now(), 
                                    HttpStatus.BAD_REQUEST.value(), 
                                    "Password Mismatch", 
                                    ex.getMessage());
    
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCredentials(InvalidCredentialsException ex) {
        ErrorResponse response = new ErrorResponse(LocalDateTime.now(), 
                                    HttpStatus.UNAUTHORIZED.value(),
                                    "Invalid Credintials",
                                    ex.getMessage());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errors.put(
                                error.getField(),
                                error.getDefaultMessage()));

        return ResponseEntity.badRequest()
                .body(errors);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex) {
        ErrorResponse response = new ErrorResponse(LocalDateTime.now(), 
                                                    HttpStatus.NOT_FOUND.value(), 
                                                    "Not Found", 
                                                    ex.getMessage());   

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(GoalNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleGoalNotFound(GoalNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(LocalDateTime.now(), 
                                                HttpStatus.NOT_FOUND.value(), 
                                                "Not Found", 
                                                ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(DuplicateGoalException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateGoal(DuplicateGoalException ex) {
        ErrorResponse error = new ErrorResponse(LocalDateTime.now(), 
                                                HttpStatus.CONFLICT.value(), 
                                                "Conflict", 
                                                ex.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }
}
