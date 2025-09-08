package com.exist.HelpdeskApp.service;

import com.exist.HelpdeskApp.dto.ticket.TicketMapper;
import com.exist.HelpdeskApp.dto.ticket.TicketRequest;
import com.exist.HelpdeskApp.dto.ticket.TicketResponse;
import com.exist.HelpdeskApp.exception.EmployeeNotFoundException;
import com.exist.HelpdeskApp.exception.TicketNotFoundException;
import com.exist.HelpdeskApp.exception.UnauthorizedActionException;
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

    @Transactional
    public List<TicketResponse> getTickets(Integer employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee with ID " + employeeId + " not found!"));
        return ticketMapper.toResponseList(ticketRepository.findAll());
    }

    @Transactional
    public TicketResponse getTicket(Integer employeeId, Integer ticketId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee with ID " + employeeId + " not found!"));
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(() -> new TicketNotFoundException("Ticket number " + ticketId + " not found!"));
        return ticketMapper.toResponse(ticket);
    }

    @Transactional
    public TicketResponse fileTicket(Integer employeeId, TicketRequest ticketRequest) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee with ID " + employeeId + " not found!"));
        Employee assignedEmployee = employeeRepository.findById(ticketRequest.getAssigneeId())
                .orElseThrow(() -> new EmployeeNotFoundException("Assigned employee with ID " + ticketRequest.getAssigneeId() + " not found!"));

        Ticket ticket = ticketMapper.toEntity(ticketRequest);
        ticket.setCreateDate(Instant.now());
        ticket.setCreatedBy(employee);
        ticket.setUpdatedDate(Instant.now());
        ticket.setUpdatedBy(employee);
        ticket.setAssignee(assignedEmployee);
        ticketRepository.save(ticket);
        return ticketMapper.toResponse(ticket);
    }

    @Transactional
    public TicketResponse updateTicket(Integer employeeId, Integer ticketId, TicketRequest ticketRequest) {
        Employee updater = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException("Updater employee with ID " + employeeId + " not found!"));
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new TicketNotFoundException("Ticket number " + ticketId + " not found!"));

        if (!Objects.equals(employeeId, ticket.getAssignee().getId()) && employeeId != 1) {
            throw new UnauthorizedActionException("You cannot alter tickets that you are not assigned to!");
        }

        if (ticketRequest.getAssigneeId() != null && employeeId != 1) {
            throw new UnauthorizedActionException("You cannot reassign tickets to other employees!");
        }

        else if (ticketRequest.getAssigneeId() != null) {
            Employee assignee = employeeRepository.findById(ticketRequest.getAssigneeId())
                    .orElseThrow(() -> new EmployeeNotFoundException("Employee with ID " + employeeId + " not found!"));
            ticket.setAssignee(assignee);
        }
        ticket.setUpdatedDate(Instant.now());
        ticket.setUpdatedBy(updater);
        ticketMapper.toUpdate(ticketRequest, ticket);
        Ticket updated = ticketRepository.save(ticket);
        return ticketMapper.toResponse(updated);
    }

    public List<TicketResponse> getAssignedTickets(Integer employeeId) {
        employeeRepository.findById(employeeId).orElseThrow(() -> new EmployeeNotFoundException("Employee with ID " + employeeId + " not found!"));
        return ticketMapper.toResponseList(ticketRepository.findByAssigneeId(employeeId));
    }
}
