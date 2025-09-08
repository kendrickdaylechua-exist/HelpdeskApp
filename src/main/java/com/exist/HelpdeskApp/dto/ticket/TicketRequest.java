package com.exist.HelpdeskApp.dto.ticket;

import com.exist.HelpdeskApp.model.TicketStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketRequest {
    @NotBlank(message = "A ticket must have a title")
    private String title;

    @NotBlank(message = "A ticket must have a body")
    private String body;

    @NotNull(message = "A ticket must be assigned to an employee.")
    private Integer assigneeId;

    @NotNull(message = "A ticket must have a valid status.")
    private TicketStatus status;

    private String remarks;
}
