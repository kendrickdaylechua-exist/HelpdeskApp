package com.exist.HelpdeskApp.exception.businessexceptions;

import com.exist.HelpdeskApp.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EmployeeNotFoundException extends BusinessException {
    public EmployeeNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
