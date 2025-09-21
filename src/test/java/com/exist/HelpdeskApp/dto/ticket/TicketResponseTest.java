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
                Instant.parse("2025-09-19T04:39:18.617856200Z"),
                "Assignee Name",
                Instant.parse("2025-09-19T04:39:18.617856200Z"),
                "Assignee Name",
                "Remarks of the ticket"
                );

        assertEquals(1, response.getTicketNumber());
        assertEquals("Ticket 1", response.getTitle());
        assertEquals("Body of the ticket", response.getBody());
        assertEquals("Assignee Name", response.getAssigneeName());
        assertEquals(Instant.parse("2025-09-19T04:39:18.617856200Z"), response.getCreateDate());
        assertEquals("Assignee Name", response.getCreatedByEmployeeName());
        assertEquals(Instant.parse("2025-09-19T04:39:18.617856200Z"), response.getUpdatedDate());
        assertEquals("Assignee Name", response.getUpdatedByEmployeeName());
        assertEquals("Remarks of the ticket", response.getRemarks());
    }
}
