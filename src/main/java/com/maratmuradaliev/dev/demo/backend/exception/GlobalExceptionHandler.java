package com.maratmuradaliev.dev.demo.backend.exception;
import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InternalServerError.class)
    public ResponseEntity<ErrorMessage> internalServerError(Exception ex, WebRequest request) {
        ErrorMessage errors = 
           new ErrorMessage(500, new Date(),
              ex.getMessage(), "Internal Server Error");

        return new ResponseEntity<>
            (errors, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorMessage> ResourceNotFound(Exception ex, WebRequest request) {
        ErrorMessage errors =
         new ErrorMessage(404, new Date(), 
           ex.getMessage(), "Resource Not Found");

        return new ResponseEntity<>
            (errors, HttpStatus.NOT_FOUND);
    }
}

