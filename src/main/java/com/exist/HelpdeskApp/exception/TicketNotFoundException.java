package com.exist.HelpdeskApp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TicketNotFoundException extends BusinessException{
    public TicketNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
