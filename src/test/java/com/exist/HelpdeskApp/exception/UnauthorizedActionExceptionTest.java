package com.exist.HelpdeskApp.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UnauthorizedActionExceptionTest {
    @Test
    void testMessageIsStored() {
        String message = "Unauthorized Access!";
        UnauthorizedActionException ex = new UnauthorizedActionException(message);

        assertEquals(message, ex.getMessage());
    }

    @Test
    void testIsBusinessException() {
        UnauthorizedActionException ex = new UnauthorizedActionException("error");
        assertTrue(ex instanceof BusinessException);
    }
}
