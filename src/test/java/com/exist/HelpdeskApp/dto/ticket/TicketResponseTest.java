package com.exist.HelpdeskApp.dto.ticket;

import com.exist.HelpdeskApp.model.TicketStatus;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TicketResponseTest {
    @Test
    void testGetterAndSetter() {
        TicketResponse response = new TicketResponse();
        response.setTicketNumber(1);
        response.setTitle("Ticket 1");

        assertEquals(1, response.getTicketNumber());
        assertEquals("Ticket 1", response.getTitle());
    }

    @Test
    void testAllArgsConstructor() {
        TicketResponse response = new TicketResponse(
                1,
                "Ticket 1",
                "Body of the ticket",
                "Assignee Name",
                TicketStatus.FILED,
                Instant.now(),
                "Assignee Name",
                Instant.now(),
                "Assignee Name",
                "Remarks of the ticket"
                );

        assertEquals(1, response.getTicketNumber());
        assertEquals("Ticket 1", response.getTitle());
        assertEquals("Body of the ticket", response.getBody());
        assertEquals("Assignee Name", response.getAssigneeName());
        assertEquals(Instant.now(), response.getCreateDate());
        assertEquals("Assignee Name", response.getCreatedByEmployeeName());
        assertEquals(Instant.now(), response.getUpdatedDate());
        assertEquals("Assignee Name", response.getUpdatedByEmployeeName());
        assertEquals("Remarks of the ticket", response.getRemarks());
    }
}
