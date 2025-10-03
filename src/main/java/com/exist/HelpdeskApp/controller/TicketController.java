package com.exist.HelpdeskApp.controller;

import com.exist.HelpdeskApp.dto.ticket.TicketFilterRequest;
import com.exist.HelpdeskApp.dto.ticket.TicketRequest;
import com.exist.HelpdeskApp.dto.ticket.TicketResponse;
import com.exist.HelpdeskApp.service.impl.TicketServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/ticket")
public class TicketController {

    private final TicketServiceImpl ticketService;

    @Autowired
    public TicketController(TicketServiceImpl ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public TicketResponse fileTicket(@Valid @RequestBody TicketRequest request, Authentication authentication) {
        return ticketService.fileTicket(request, authentication);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public Page<TicketResponse> getTickets(@ModelAttribute TicketFilterRequest request, Pageable pageable,
                                           Authentication authentication, @RequestParam(defaultValue = "false") boolean assigned) {
        return ticketService.getTickets(authentication, request, pageable, assigned);
    }

    @GetMapping("/{ticketId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public TicketResponse getTicket(Authentication authentication, @PathVariable Integer ticketId) {
        return ticketService.getTicket(authentication, ticketId);
    }

    @PatchMapping("/{ticketId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public TicketResponse updateTicket(Authentication authentication, @PathVariable Integer ticketId, @RequestBody TicketRequest ticketRequest) {
        return ticketService.updateTicket(authentication, ticketId, ticketRequest);
    }
}
