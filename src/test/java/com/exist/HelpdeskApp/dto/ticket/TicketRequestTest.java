package com.exist.HelpdeskApp.dto.ticket;

import com.exist.HelpdeskApp.model.TicketStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class TicketRequestTest {
    private Validator validator;

    @BeforeEach
    void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidRequest() {
        TicketRequest request = new TicketRequest(
                "Ticket 1",
                "Body of the ticket",
                1,
                TicketStatus.FILED,
                "Remarks of the ticket"
        );

        assertEquals("Ticket 1", request.getTitle());
        assertEquals("Body of the ticket", request.getBody());
        assertEquals(1, request.getAssigneeId());
        assertEquals(TicketStatus.FILED, request.getStatus());
        assertEquals("Remarks of the ticket", request.getRemarks());
    }

    @Test
    void testInvalidRequest() {
        TicketRequest request = new TicketRequest();
        Set<ConstraintViolation<TicketRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertNotNull(violations);
    }
}
