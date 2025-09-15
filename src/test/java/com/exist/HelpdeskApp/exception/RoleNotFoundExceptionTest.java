package com.exist.HelpdeskApp.exception;

import com.exist.HelpdeskApp.exception.businessexceptions.RoleNotFoundException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RoleNotFoundExceptionTest {
    @Test
    void testMessageIsStored() {
        String message = "Employee with ID 1 not found!";
        RoleNotFoundException ex = new RoleNotFoundException(message);

        assertEquals(message, ex.getMessage());
    }

    @Test
    void testIsBusinessException() {
        RoleNotFoundException ex = new RoleNotFoundException("error");
        assertTrue(ex instanceof BusinessException);
    }
}
