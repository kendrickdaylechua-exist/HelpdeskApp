package com.exist.HelpdeskApp.service;

import com.exist.HelpdeskApp.dto.ticket.TicketMapper;
import com.exist.HelpdeskApp.dto.ticket.TicketRequest;
import com.exist.HelpdeskApp.dto.ticket.TicketResponse;
import com.exist.HelpdeskApp.model.Employee;
import com.exist.HelpdeskApp.model.Ticket;
import com.exist.HelpdeskApp.repository.EmployeeRepository;
import com.exist.HelpdeskApp.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
public class TicketService {
    EmployeeRepository employeeRepository;
    TicketRepository ticketRepository;
    TicketMapper ticketMapper;

    @Autowired
    public TicketService(EmployeeRepository employeeRepository,
                         TicketRepository ticketRepository,
                         TicketMapper ticketMapper) {
        this.employeeRepository = employeeRepository;
        this.ticketRepository = ticketRepository;
        this.ticketMapper = ticketMapper;
    }

    public List<TicketResponse> getTickets() {
        return ticketMapper.toResponseList(ticketRepository.findAll());
    }

    public TicketResponse getTicket(int id) {
        Ticket ticket = ticketRepository.findById(id).orElseThrow(() -> new NoSuchElementException("ID cannot be found"));
        return ticketMapper.toResponse(ticket);
    }

    @Transactional
    public void fileTicket(int employeeId, TicketRequest ticketRequest) {
        Ticket ticket = ticketMapper.toEntity(ticketRequest);
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Invalid employee ID!"));
        Employee assignedEmployee = employeeRepository.findById(ticketRequest.getAssigneeId())
                .orElseThrow(() -> new RuntimeException("Cannot find assignee ID!"));

        ticket.setCreateDate(Instant.now());
        ticket.setCreatedBy(employee);
        ticket.setUpdatedDate(Instant.now());
        ticket.setUpdatedBy(employee);
        ticket.setAssignee(assignedEmployee);
        ticketRepository.save(ticket);
    }

    @Transactional
    public TicketResponse updateTicket(int employeeId, int ticketId, TicketRequest ticketRequest) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Invalid ticket ID"));

        if (!Objects.equals(employeeId, ticket.getAssignee().getId()) && employeeId != 1) {
            throw new RuntimeException("You cannot alter tickets that you are not assigned to!");
        }

        if (ticketRequest.getAssigneeId() != null && employeeId != 1) {
            throw new RuntimeException("You cannot reassign tickets to other employees!");
        }

        else if (ticketRequest.getAssigneeId() != null) {
            Employee assignee = employeeRepository.findById(ticketRequest.getAssigneeId())
                    .orElseThrow(() -> new RuntimeException("Invalid assignee employee ID"));
            ticket.setAssignee(assignee);
        }

        Employee updater = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Invalid updater employee ID"));
        ticket.setUpdatedDate(Instant.now());
        ticket.setUpdatedBy(updater);
        ticketMapper.toUpdate(ticketRequest, ticket);
        Ticket updated = ticketRepository.save(ticket);
        return ticketMapper.toResponse(updated);
    }

    @Transactional
    public TicketResponse respondTicket(int ticketId, TicketRequest ticketRequest) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Invalid ticket ID"));
        ticket.setRemarks(ticketRequest.getRemarks());
        ticket.setStatus(ticketRequest.getStatus());
        ticket.setUpdatedDate(Instant.now());
        ticketMapper.toUpdate(ticketRequest, ticket);
        Ticket updated = ticketRepository.save(ticket);
        return ticketMapper.toResponse(updated);
    }

    public List<TicketResponse> getAssignedTickets(int employeeId) {
        employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Invalid employee ID!"));
        return ticketMapper.toResponseList(ticketRepository.findByAssigneeId(employeeId));
    }
}
