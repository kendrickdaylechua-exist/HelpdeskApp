package com.exist.HelpdeskApp.exception;

import com.exist.HelpdeskApp.exception.businessexceptions.TicketNotFoundException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TicketNotFoundExceptionTest {
    @Test
    void testMessageIsStored() {
        String message = "Ticket Number with ID 1 not found!";
        TicketNotFoundException ex = new TicketNotFoundException(message);

        assertEquals(message, ex.getMessage());
    }

    @Test
    void testIsBusinessException() {
        TicketNotFoundException ex = new TicketNotFoundException("error");
        assertTrue(ex instanceof BusinessException);
    }
}
