package com.exist.HelpdeskApp.dto.ticket;

import com.exist.HelpdeskApp.model.Employee;
import com.exist.HelpdeskApp.model.TicketStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;

import javax.validation.constraints.NotBlank;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketRequest {
    private int ticketNumber;

    @NotBlank(message = "A ticket must have a title")
    private String title;

    @NotBlank(message = "A ticket must have a body")
    private String body;

    @NotBlank(message = "A ticket must be assigned to an employee.")
    private String assigneeName;

    @NotBlank(message = "A ticket must have a valid status.")
    private TicketStatus status;

//    @CreatedDate
//    @NotBlank(message = "There should be a time where the ticket was created.")
//    private Instant createDate;

    @NotBlank(message = "A ticket must be created by someone")
    private String createdByEmployeeName;

    @UpdateTimestamp
    private Instant updatedDate;

    private Employee updatedBy;

    private String remarks;
}
