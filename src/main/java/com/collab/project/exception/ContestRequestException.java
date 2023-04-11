package com.collab.project.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ContestRequestException extends RuntimeException {
    public ContestRequestException(String exception) {
        super(exception);
    }
}