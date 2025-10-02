package com.exist.HelpdeskApp.controller;

import com.exist.HelpdeskApp.dto.ticket.TicketFilterRequest;
import com.exist.HelpdeskApp.dto.ticket.TicketRequest;
import com.exist.HelpdeskApp.dto.ticket.TicketResponse;
import com.exist.HelpdeskApp.service.impl.TicketServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/ticket")
public class TicketController {

    private final TicketServiceImpl ticketService;

    @Autowired
    public TicketController(TicketServiceImpl ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping("{employeeId}")
    public TicketResponse fileTicket(@PathVariable Integer employeeId, @Valid @RequestBody TicketRequest request) {
        return ticketService.fileTicket(employeeId, request);
    }

    @GetMapping("{employeeId}")
    public Page<TicketResponse> getTickets(@PathVariable Integer employeeId, @ModelAttribute TicketFilterRequest request, Pageable pageable) {
        return ticketService.getTickets(employeeId, request, pageable);
    }

    @GetMapping("{employeeId}/{ticketId}")
    public TicketResponse getTicket(@PathVariable Integer employeeId, @PathVariable Integer ticketId) {
        return ticketService.getTicket(employeeId, ticketId);
    }

    @PatchMapping("{employeeId}/{ticketId}")
    public TicketResponse updateTicket(@PathVariable Integer employeeId, @PathVariable Integer ticketId, @RequestBody TicketRequest ticketRequest) {
        return ticketService.updateTicket(employeeId, ticketId, ticketRequest);
    }

    @GetMapping("{employeeId}/assigned")
    public List<TicketResponse> assignedTickets(@PathVariable Integer employeeId) {
        return ticketService.getAssignedTickets(employeeId);
    }
}
