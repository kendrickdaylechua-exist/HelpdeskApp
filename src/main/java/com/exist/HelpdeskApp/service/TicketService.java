package com.exist.HelpdeskApp.service;

import com.exist.HelpdeskApp.dto.ticket.TicketFilterRequest;
import com.exist.HelpdeskApp.dto.ticket.TicketRequest;
import com.exist.HelpdeskApp.dto.ticket.TicketResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TicketService {
    Page<TicketResponse> getTickets(Integer employeeId, TicketFilterRequest request, Pageable pageable);
    TicketResponse getTicket(Integer employeeId, Integer ticketId);
    TicketResponse fileTicket(Integer employeeId, TicketRequest ticketRequest);
    TicketResponse updateTicket(Integer employeeId, Integer ticketId, TicketRequest ticketRequest);
    List<TicketResponse> getAssignedTickets(Integer employeeId);
}