package com.exist.HelpdeskApp.controller;

import com.exist.HelpdeskApp.dto.employee.EmployeeResponse;
import com.exist.HelpdeskApp.dto.ticket.TicketFilterRequest;
import com.exist.HelpdeskApp.dto.ticket.TicketRequest;
import com.exist.HelpdeskApp.dto.ticket.TicketResponse;
import com.exist.HelpdeskApp.service.Implementations.EmployeeServiceImpl;
import com.exist.HelpdeskApp.service.Implementations.TicketServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    EmployeeServiceImpl employeeServiceImpl;
    TicketServiceImpl ticketServiceImpl;

    @Autowired
    public EmployeeController(EmployeeServiceImpl employeeServiceImpl,
                              TicketServiceImpl ticketServiceImpl) {
        this.employeeServiceImpl = employeeServiceImpl;
        this.ticketServiceImpl = ticketServiceImpl;
    }

    @RequestMapping("")
    public String employeeHome() {
        return "This is the employee's page. Please enter your ID number...";
    }

    @GetMapping("{employeeId}")
    public EmployeeResponse getOwnProfile(@PathVariable Integer employeeId) {
        return employeeServiceImpl.getEmployee(employeeId);
    }

    @PostMapping("{employeeId}/tickets")
    public TicketResponse fileTicket(@PathVariable Integer employeeId, @Valid @RequestBody TicketRequest request) {
        return ticketServiceImpl.fileTicket(employeeId, request);
    }

    @GetMapping("{employeeId}/tickets")
    public Page<TicketResponse> getTickets(@PathVariable Integer employeeId, @ModelAttribute TicketFilterRequest request) {
        return ticketServiceImpl.getTickets(employeeId, request);
    }

    @GetMapping("{employeeId}/tickets/{ticketId}")
    public TicketResponse getTicket(@PathVariable Integer employeeId, @PathVariable Integer ticketId) {
        return ticketServiceImpl.getTicket(employeeId, ticketId);
    }

    @PatchMapping("{employeeId}/tickets/{ticketId}")
    public TicketResponse updateTicket(@PathVariable Integer employeeId, @PathVariable Integer ticketId, @RequestBody TicketRequest ticketRequest) {
        return ticketServiceImpl.updateTicket(employeeId, ticketId, ticketRequest);
    }

    @GetMapping("{employeeId}/tickets/assigned")
    public List<TicketResponse> assignedTickets(@PathVariable Integer employeeId) {
        return ticketServiceImpl.getAssignedTickets(employeeId);
    }
}
