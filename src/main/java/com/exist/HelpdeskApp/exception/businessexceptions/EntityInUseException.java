package com.exist.HelpdeskApp.exception.businessexceptions;

import com.exist.HelpdeskApp.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class EntityInUseException extends BusinessException {
    public EntityInUseException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
