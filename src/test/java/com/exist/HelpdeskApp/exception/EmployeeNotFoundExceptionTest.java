package com.exist.HelpdeskApp.exception;

import com.exist.HelpdeskApp.exception.businessexceptions.EmployeeNotFoundException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EmployeeNotFoundExceptionTest {

    @Test
    void testMessageIsStored() {
        String message = "Employee with ID 1 not found!";
        EmployeeNotFoundException ex = new EmployeeNotFoundException(message);

        assertEquals(message, ex.getMessage());
    }

    @Test
    void testIsBusinessException() {
        EmployeeNotFoundException ex = new EmployeeNotFoundException("error");
        assertTrue(ex instanceof BusinessException);
    }
}
