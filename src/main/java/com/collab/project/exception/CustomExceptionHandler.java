package com.collab.project.exception;

import com.amazonaws.services.mq.model.UnauthorizedException;
import com.collab.project.model.response.ErrorResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllExceptions(Exception e, WebRequest request) {
        e.printStackTrace();
        ErrorResponse error = new ErrorResponse("Something went wrong", e.getLocalizedMessage());
        return new ResponseEntity(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RecordNotFoundException.class)
    public final ResponseEntity<Object> handleNoRecordFoundExceptions(RecordNotFoundException e, WebRequest request) {
        ErrorResponse error = new ErrorResponse("No records found", e.getLocalizedMessage());
        return new ResponseEntity(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(JsonProcessingException.class)
    public final ResponseEntity<Object> handleJsonProcessingException(JsonProcessingException e, WebRequest request) {
        ErrorResponse error = new ErrorResponse("JSON parse error", e.getLocalizedMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IOException.class)
    public final ResponseEntity<Object> handleIOException(IOException e, WebRequest request) {
        ErrorResponse error = new ErrorResponse("IO Exception while processing.", e.getLocalizedMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public final ResponseEntity<Object> handleUnAuthException(UnauthorizedException e, WebRequest request) {
        ErrorResponse error = new ErrorResponse("IO Exception while processing.", e.getLocalizedMessage());
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }
}
