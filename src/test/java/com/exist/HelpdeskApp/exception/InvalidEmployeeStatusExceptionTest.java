package com.exist.HelpdeskApp.exception;

import com.exist.HelpdeskApp.exception.businessexceptions.InvalidEmployeeStatusException;
import org.junit.jupiter.api.Test;
import org.springframework.http.converter.HttpMessageNotReadableException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class InvalidEmployeeProfileStatusExceptionTest {
    @Test
    void testMessageIsStored() {
        String message = "Unauthorized Access!";
        InvalidEmployeeStatusException ex = new InvalidEmployeeStatusException(message);

        assertEquals(message, ex.getMessage());
    }

    @Test
    void testIsHttpMessageNotReadableException() {
        InvalidEmployeeStatusException ex = new InvalidEmployeeStatusException("error");
        assertTrue(ex instanceof HttpMessageNotReadableException);
    }
}
