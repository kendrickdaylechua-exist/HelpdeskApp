package com.exist.HelpdeskApp.exception.businessexceptions;

import com.exist.HelpdeskApp.exception.BusinessException;
import org.springframework.http.HttpStatus;


public class UnauthorizedActionException extends BusinessException {
    public UnauthorizedActionException(String message) {
        super(message, HttpStatus.FORBIDDEN);
    }
}
