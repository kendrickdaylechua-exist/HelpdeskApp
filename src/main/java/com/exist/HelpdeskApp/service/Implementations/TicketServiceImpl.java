package com.exist.HelpdeskApp.service.Implementations;

import com.exist.HelpdeskApp.dto.ticket.TicketFilterRequest;
import com.exist.HelpdeskApp.dto.ticket.TicketMapper;
import com.exist.HelpdeskApp.dto.ticket.TicketRequest;
import com.exist.HelpdeskApp.dto.ticket.TicketResponse;
import com.exist.HelpdeskApp.exception.businessexceptions.EmployeeNotFoundException;
import com.exist.HelpdeskApp.exception.businessexceptions.TicketNotFoundException;
import com.exist.HelpdeskApp.exception.businessexceptions.UnauthorizedActionException;
import com.exist.HelpdeskApp.model.Employee;
import com.exist.HelpdeskApp.model.Ticket;
import com.exist.HelpdeskApp.repository.EmployeeRepository;
import com.exist.HelpdeskApp.repository.TicketRepository;
import com.exist.HelpdeskApp.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

@Service
public class TicketServiceImpl implements TicketService {
    EmployeeRepository employeeRepository;
    TicketRepository ticketRepository;
    TicketMapper ticketMapper;

    @Autowired
    public TicketServiceImpl(EmployeeRepository employeeRepository,
                             TicketRepository ticketRepository,
                             TicketMapper ticketMapper) {
        this.employeeRepository = employeeRepository;
        this.ticketRepository = ticketRepository;
        this.ticketMapper = ticketMapper;
    }

    @Transactional
    public Page<TicketResponse> getTickets(Integer employeeId, TicketFilterRequest request) {
        Employee employee = employeeRepository.findByIdAndDeletedFalse(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee with ID " + employeeId + " not found!"));

        Specification<Ticket> spec = request.toSpec();

        Sort sort = request.getSortDir().equalsIgnoreCase("desc") ? Sort.by(request.getSortBy()).descending() : Sort.by(request.getSortBy()).ascending();
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);
        Page<Ticket> ticketPage = ticketRepository.findAll(spec, pageable);
        List<TicketResponse> ticketResponses = ticketMapper.toResponseList(ticketPage.getContent());
        return new PageImpl<>(ticketResponses, pageable, ticketPage.getTotalElements());
    }

    @Transactional
    public TicketResponse getTicket(Integer employeeId, Integer ticketId) {
        Employee employee = employeeRepository.findByIdAndDeletedFalse(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee with ID " + employeeId + " not found!"));
        Ticket ticket = ticketRepository.findByTicketNumberAndDeletedFalse(ticketId).orElseThrow(() -> new TicketNotFoundException("Ticket number " + ticketId + " not found!"));
        return ticketMapper.toResponse(ticket);
    }

    @Transactional
    public TicketResponse fileTicket(Integer employeeId, TicketRequest ticketRequest) {
        Employee employee = employeeRepository.findByIdAndDeletedFalse(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee with ID " + employeeId + " not found!"));
        Employee assignedEmployee = employeeRepository.findByIdAndDeletedFalse(ticketRequest.getAssigneeId())
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
        Employee updater = employeeRepository.findByIdAndDeletedFalse(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException("Updater employee with ID " + employeeId + " not found!"));
        Ticket ticket = ticketRepository.findByTicketNumberAndDeletedFalse(ticketId)
                .orElseThrow(() -> new TicketNotFoundException("Ticket number " + ticketId + " not found!"));

        if (!Objects.equals(employeeId, ticket.getAssignee().getId()) && employeeId != 1) {
            throw new UnauthorizedActionException("You cannot alter tickets that you are not assigned to!");
        }

        if (ticketRequest.getAssigneeId() != null && employeeId != 1) {
            throw new UnauthorizedActionException("You cannot reassign tickets to other employees!");
        }

        else if (ticketRequest.getAssigneeId() != null) {
            Employee assignee = employeeRepository.findByIdAndDeletedFalse(ticketRequest.getAssigneeId())
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
        employeeRepository.findByIdAndDeletedFalse(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee with ID " + employeeId + " not found!"));
        return ticketMapper.toResponseList(ticketRepository.findByAssigneeIdAndDeletedFalse(employeeId));
    }
}
