package com.exist.HelpdeskApp.service.impl;

import com.exist.HelpdeskApp.dto.ticket.TicketFilterRequest;
import com.exist.HelpdeskApp.dto.ticket.TicketMapper;
import com.exist.HelpdeskApp.dto.ticket.TicketRequest;
import com.exist.HelpdeskApp.dto.ticket.TicketResponse;
import com.exist.HelpdeskApp.exception.businessexceptions.AccountNotFoundException;
import com.exist.HelpdeskApp.exception.businessexceptions.EmployeeNotFoundException;
import com.exist.HelpdeskApp.exception.businessexceptions.TicketNotFoundException;
import com.exist.HelpdeskApp.exception.businessexceptions.UnauthorizedActionException;
import com.exist.HelpdeskApp.model.Account;
import com.exist.HelpdeskApp.model.Employee;
import com.exist.HelpdeskApp.model.Ticket;
import com.exist.HelpdeskApp.repository.AccountRepository;
import com.exist.HelpdeskApp.repository.EmployeeRepository;
import com.exist.HelpdeskApp.repository.TicketRepository;
import com.exist.HelpdeskApp.service.AccountService;
import com.exist.HelpdeskApp.service.TicketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
public class TicketServiceImpl implements TicketService {
    private final EmployeeRepository employeeRepository;
    private final TicketRepository ticketRepository;
    private final TicketMapper ticketMapper;
    private final AccountRepository accountRepository;
    private static final Logger logger = LoggerFactory.getLogger(TicketServiceImpl.class);

    @Autowired
    public TicketServiceImpl(EmployeeRepository employeeRepository,
                             TicketRepository ticketRepository,
                             TicketMapper ticketMapper,
                             AccountRepository accountRepository) {
        this.employeeRepository = employeeRepository;
        this.ticketRepository = ticketRepository;
        this.ticketMapper = ticketMapper;
        this.accountRepository = accountRepository;
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public Page<TicketResponse> getTickets(Authentication authentication, TicketFilterRequest request, Pageable pageable, boolean assigned) {
//        Set<String> permissions = authentication.getAuthorities().stream()
//                .map(GrantedAuthority::getAuthority)
//                .collect(Collectors.toSet());

//        Consumer<String> requirePermission = perm -> {
//            if (!permissions.contains(perm)) {
//                logger.info("User {} attempted to perform this action: {}", authentication.getName(), perm);
//                throw new AccessDeniedException("You do not have permission to perform this action");
//            }
//        };
//
//        if (permissions.contains("GET_ASSIGNED_TICKETS") && !permissions.contains("GET_ALL_TICKETS") &&
//                request.getAssigneeId() == null && request.getAssigneeName() == null) {
//            request.setAssigneeId(getLoggedInEmployee(authentication).getId());
//        }
//        else if (request.getAssigneeName() != null || request.getAssigneeId() != null) {
//            requirePermission.accept("GET_OTHER_TICKETS");
//        }

        if (assigned) {
            request.setAssigneeId(getLoggedInEmployee(authentication).getId());
        }
        Specification<Ticket> spec = request.toSpec();

        Page<Ticket> ticketPage = ticketRepository.findAll(spec, pageable);
        return ticketPage.map(ticketMapper::toResponse);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public TicketResponse getTicket(Authentication authentication, Integer ticketId) {
//        Set<String> permissions = authentication.getAuthorities().stream()
//                .map(GrantedAuthority::getAuthority)
//                .collect(Collectors.toSet());
//
//        if (permissions.contains("GET_ASSIGNED_TICKETS") && !permissions.contains("GET_ALL_TICKETS") &&
//                ((getLoggedInEmployee(authentication).getId() != checkticketById(ticketId).getAssignee().getId()) &&
//                        (getLoggedInEmployee(authentication).getId() != checkticketById(ticketId).getCreatedBy().getId()))) {
//            logger.info("User {} attempted to access ticket with ID {}", authentication.getName(), ticketId);
//            throw new AccessDeniedException("You cannot access other tickets that are not assigned or created by you");
//        }
        Ticket ticket = checkticketById(ticketId);
        return ticketMapper.toResponse(ticket);
    }

    @Transactional
    @PreAuthorize("hasAuthority('FILE_TICKET')")
    public TicketResponse fileTicket(TicketRequest ticketRequest, Authentication authentication) {
        String assignedEmployeeMessageNotFound = "Assigned employee with ID " + ticketRequest.getAssigneeId() + " not found!";
        Employee loggedInEmployee = getLoggedInEmployee(authentication);
        Employee assignedEmployee = checkEmployeeById(ticketRequest.getAssigneeId(), assignedEmployeeMessageNotFound);
        Ticket ticket = ticketMapper.toEntity(ticketRequest);
        ticket.setCreateDate(Instant.now());
        ticket.setCreatedBy(loggedInEmployee);
        ticket.setUpdatedDate(Instant.now());
        ticket.setUpdatedBy(loggedInEmployee);
        ticket.setAssignee(assignedEmployee);
        ticketRepository.save(ticket);
        return ticketMapper.toResponse(ticket);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public TicketResponse updateTicket(Authentication authentication, Integer ticketId, TicketRequest ticketRequest) {
        Set<String> permissions = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        Consumer<String> requirePermission = perm -> {
            if (!permissions.contains(perm)) {
                logger.info("User {} attempted to perform this action: {}", authentication.getName(), perm);
                throw new AccessDeniedException("You do not have permission to perform this action");
            }
        };

        Employee loggedInEmployee = getLoggedInEmployee(authentication);
        String updaterNotFoundMessage = "Updater employee with ID " + loggedInEmployee.getId() + " not found!";
        Employee updater = checkEmployeeById(loggedInEmployee.getId(), updaterNotFoundMessage);

        Ticket ticket = checkticketById(ticketId);

        if (permissions.contains("UPDATE_ASSIGNED_TICKETS") && !permissions.contains("UPDATE_ANY_TICKETS") &&
                loggedInEmployee.getId() != ticket.getAssignee().getId()) {
            logger.info("User {} attempted to update unassigned ticket with ID {}", authentication.getName(), ticketId);
            throw new AccessDeniedException("You cannot alter tickets that you are not assigned to!");
        }

        if (ticketRequest.getAssigneeId() != null && !permissions.contains("REASSIGNED_TICKET")) {
            throw new AccessDeniedException("You cannot reassign tickets to other employees!");
        } else if (ticketRequest.getAssigneeId() != null) {
            Employee assignee = checkEmployeeById(ticketRequest.getAssigneeId());
            ticket.setAssignee(assignee);
        }
        ticket.setUpdatedDate(Instant.now());
        ticket.setUpdatedBy(updater);
        ticketMapper.toUpdate(ticketRequest, ticket);
        Ticket updated = ticketRepository.save(ticket);
        return ticketMapper.toResponse(updated);
    }

    @Transactional
    public Employee getLoggedInEmployee(Authentication authentication) {
        Account account = accountRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));
        if (account.getEmployee() == null) {
            throw new EmployeeNotFoundException("No employee linked to this account. Please contact the admin!");
        }
        return account.getEmployee();
    }

    @Transactional
    private Employee checkEmployeeById(Integer id) {
        return employeeRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee with ID " + id + " either not found or deleted!"));
    }

    @Transactional
    private Employee checkEmployeeById(Integer id, String message) {
        return employeeRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new EmployeeNotFoundException(message));
    }

    @Transactional
    private Ticket checkticketById(Integer ticketId) {
        return ticketRepository.findByTicketNumberAndDeletedFalse(ticketId)
            .orElseThrow(() -> new TicketNotFoundException("Ticket number " + ticketId + " not found!"));
    }
}
