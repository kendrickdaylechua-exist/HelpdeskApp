package com.exist.HelpdeskApp.dto.ticket;

import com.exist.HelpdeskApp.model.TicketStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketResponse {
    private Integer ticketNumber;
    private String title;
    private String body;
    private Integer assigneeId;
    private String assigneeName;
    private TicketStatus status;
    private Instant createDate;
    private Integer createdByEmployeeId;
    private String createdByEmployeeName;
    private Instant updatedDate;
    private Integer updatedByEmployeeId;
    private String updatedByEmployeeName;
    private String remarks;
}
