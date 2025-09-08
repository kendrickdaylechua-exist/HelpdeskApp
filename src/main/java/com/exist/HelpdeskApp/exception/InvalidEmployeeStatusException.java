package com.exist.HelpdeskApp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidEmployeeStatusException extends BusinessException {

    public InvalidEmployeeStatusException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
