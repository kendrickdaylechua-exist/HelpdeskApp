package com.exist.HelpdeskApp.exception.businessexceptions;

import com.exist.HelpdeskApp.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class DissabledAccountException extends BusinessException {
    public DissabledAccountException(String message) {
        super(message, HttpStatus.FORBIDDEN);
    }
}
