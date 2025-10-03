package com.exist.HelpdeskApp.service;

import com.exist.HelpdeskApp.dto.ticket.TicketFilterRequest;
import com.exist.HelpdeskApp.dto.ticket.TicketRequest;
import com.exist.HelpdeskApp.dto.ticket.TicketResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface TicketService {
    Page<TicketResponse> getTickets(Authentication authentication, TicketFilterRequest request, Pageable pageable, boolean assigned);
    TicketResponse getTicket(Authentication authentication, Integer ticketId);
    TicketResponse fileTicket(TicketRequest ticketRequest, Authentication authentication);
    TicketResponse updateTicket(Authentication authentication, Integer ticketId, TicketRequest ticketRequest);
}