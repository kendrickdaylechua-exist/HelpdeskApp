package com.exist.HelpdeskApp.dto.ticket;

import com.exist.HelpdeskApp.model.Employee;
import com.exist.HelpdeskApp.model.TicketStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketResponse {
    private int ticketNumber;
    private String title;
    private String body;
    private Employee assignee;
    private TicketStatus status;
    private Instant createDate;
    private Employee createdBy;
    private Instant updatedDate;
    private Employee updatedBy;
    private String remarks;
}
