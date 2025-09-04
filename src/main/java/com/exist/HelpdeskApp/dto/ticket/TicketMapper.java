package com.exist.HelpdeskApp.dto.ticket;

import com.exist.HelpdeskApp.model.Ticket;

public interface TicketMapper {
    Ticket toEntity(TicketRequest request);
    TicketResponse toResponse(Ticket entity);
}
