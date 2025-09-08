package com.exist.HelpdeskApp.controller;

import com.exist.HelpdeskApp.dto.employee.EmployeeResponse;
import com.exist.HelpdeskApp.dto.ticket.TicketRequest;
import com.exist.HelpdeskApp.dto.ticket.TicketResponse;
import com.exist.HelpdeskApp.service.EmployeeService;
import com.exist.HelpdeskApp.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    EmployeeService employeeService;
    TicketService ticketService;

    @Autowired
    public EmployeeController(EmployeeService employeeService,
                              TicketService ticketService) {
        this.employeeService = employeeService;
        this.ticketService = ticketService;
    }

    @RequestMapping("")
    public String employeeHome() {
        return "This is the employee's page. Please enter your ID number...";
    }

    @GetMapping("{employeeId}")
    public EmployeeResponse getOwnProfile(@PathVariable Integer employeeId) {
        return employeeService.getEmployee(employeeId);
    }

    @PostMapping("{employeeId}/tickets")
    public TicketResponse fileTicket(@PathVariable Integer employeeId, @Valid @RequestBody TicketRequest request) {
        return ticketService.fileTicket(employeeId, request);
    }

    @GetMapping("{employeeId}/tickets")
    public List<TicketResponse> getTickets(@PathVariable Integer employeeId) {
        return ticketService.getTickets(employeeId);
    }

    @GetMapping("{employeeId}/tickets/{ticketId}")
    public TicketResponse getTicket(@PathVariable Integer employeeId, @PathVariable Integer ticketId) {
        return ticketService.getTicket(employeeId, ticketId);
    }

    @PatchMapping("{employeeId}/tickets/{ticketId}")
    public TicketResponse updateTicket(@PathVariable Integer employeeId, @PathVariable Integer ticketId, @RequestBody TicketRequest ticketRequest) {
        return ticketService.updateTicket(employeeId, ticketId, ticketRequest);
    }

    @GetMapping("{employeeId}/tickets/assigned")
    public List<TicketResponse> assignedTickets(@PathVariable Integer employeeId) {
        return ticketService.getAssignedTickets(employeeId);
    }
}
