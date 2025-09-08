package com.exist.HelpdeskApp.exception;

import org.springframework.http.HttpStatus;


public class UnauthorizedActionException extends BusinessException{
    public UnauthorizedActionException(String message) {
        super(message, HttpStatus.FORBIDDEN);
    }
}
