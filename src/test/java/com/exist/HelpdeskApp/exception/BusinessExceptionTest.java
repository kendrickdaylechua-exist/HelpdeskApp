package com.exist.HelpdeskApp.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;

class BusinessExceptionTest {

    @Test
    void testBusinessExceptionProperties() {
        String message = "Something went wrong";
        HttpStatus status = HttpStatus.BAD_REQUEST;

        BusinessException ex = new BusinessException(message, status);

        assertEquals(message, ex.getMessage());
        assertEquals(status, ex.getStatus());
    }

    @Test
    void testIsRuntimeException() {
        BusinessException ex = new BusinessException("error", HttpStatus.INTERNAL_SERVER_ERROR);

        assertTrue(ex instanceof RuntimeException);
    }

    @Test
    void testGetStatus() {
        BusinessException ex = new BusinessException("error occurred", HttpStatus.NOT_FOUND);

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
    }
}

