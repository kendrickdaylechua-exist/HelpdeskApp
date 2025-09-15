package com.exist.HelpdeskApp.exception.businessexceptions;
import org.springframework.http.converter.HttpMessageNotReadableException;

public class InvalidEmployeeStatusException extends HttpMessageNotReadableException {

    public InvalidEmployeeStatusException(String message) {
        super(message);
    }
}
