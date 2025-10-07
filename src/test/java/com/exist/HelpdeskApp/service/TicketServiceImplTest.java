package com.exist.HelpdeskApp.service;

import com.exist.HelpdeskApp.dto.ticket.TicketFilterRequest;
import com.exist.HelpdeskApp.dto.ticket.TicketMapper;
import com.exist.HelpdeskApp.dto.ticket.TicketRequest;
import com.exist.HelpdeskApp.dto.ticket.TicketResponse;
import com.exist.HelpdeskApp.exception.businessexceptions.AccountNotFoundException;
import com.exist.HelpdeskApp.exception.businessexceptions.EmployeeNotFoundException;
import com.exist.HelpdeskApp.exception.businessexceptions.TicketNotFoundException;
import com.exist.HelpdeskApp.model.*;
import com.exist.HelpdeskApp.model.embeddable.Address;
import com.exist.HelpdeskApp.model.embeddable.Contacts;
import com.exist.HelpdeskApp.model.embeddable.Name;
import com.exist.HelpdeskApp.repository.AccountRepository;
import com.exist.HelpdeskApp.repository.EmployeeRepository;
import com.exist.HelpdeskApp.repository.TicketRepository;
import com.exist.HelpdeskApp.service.impl.TicketServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TicketServiceImplTest {
    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private TicketMapper ticketMapper;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private TicketServiceImpl ticketService;

    private static Ticket ticket;
    private static Employee employee;
    private static TicketRequest ticketRequest1;
    private static TicketResponse ticketResponse1;
    private static final Integer VALID_EMPLOYEE_ID = 2;
    private static final Integer INVALID_EMPLOYEE_ID = 99;
    private static final Integer ADMIN_EMPLOYEE_ID = 1;
    private static Pageable pageable = PageRequest.of(0, 5);
    private static Authentication authAdmin;
    private static Authentication authUser;
    private static Account accountUser;
    private static Account accountAdmin;

    @BeforeEach
    void setup() {
        Name name1 = new Name("First1", "Middle1", "Last1");
        Contacts contacts1 = new Contacts("0912345678", "sample@example.com", "021234567");
        Address address1 = new Address("123 Test St.", "Manila", "Region 1", "Philippines");
        employee = new Employee(
                1,
                name1,
                25,
                address1,
                contacts1,
                null,
                null,
                false,
                1
        );
        ticket = new Ticket(
                1,
                "Sample Ticket",
                "Sample Body",
                employee,
                TicketStatus.FILED,
                Instant.parse("2025-04-16T10:15:30.00Z"),
                employee,
                Instant.parse("2025-04-16T10:15:30.00Z"),
                employee,
                "Sample Remarks",
                false,
                1
        );
        ticketRequest1 = new TicketRequest(
                "Sample Ticket",
                "Sample Body",
                2,
                TicketStatus.FILED,
                "Sample Remarks"
        );
        ticketResponse1 = new TicketResponse(
                1,
                "Sample Ticket",
                "Sample Body",
                1,
                "Employee 1",
                TicketStatus.FILED,
                Instant.parse("2025-04-16T10:15:30.00Z"),
                1,
                "Employee 1",
                Instant.parse("2025-04-16T10:15:30.00Z"),
                1,
                "Employee 1",
                "Sample Remarks"
        );
        authAdmin = new UsernamePasswordAuthenticationToken(
                "ADMIN",
                null,
                List.of(
                        new SimpleGrantedAuthority("GET_ALL_TICKETS"),
                        new SimpleGrantedAuthority("GET_ASSIGNED_TICKETS"),
                        new SimpleGrantedAuthority("GET_OTHER_TICKETS"),
                        new SimpleGrantedAuthority("UPDATE_ASSIGNED_TICKETS"),
                        new SimpleGrantedAuthority("UPDATE_ANY_TICKETS"),
                        new SimpleGrantedAuthority("REASSIGNED_TICKET")
                )
        );
        authUser = new UsernamePasswordAuthenticationToken(
                "USER",
                null,
                List.of(
                        new SimpleGrantedAuthority("FILE_TICKET"),
                        new SimpleGrantedAuthority("GET_ALL_TICKETS"),
                        new SimpleGrantedAuthority("GET_ASSIGNED_TICKETS"),
                        new SimpleGrantedAuthority("GET_OTHER_TICKETS"),
                        new SimpleGrantedAuthority("UPDATE_ASSIGNED_TICKETS")
                )
        );
        Permission permission = new Permission(1, "samplePermission");
        SecurityRole securityRole = new SecurityRole(1, "sampleSecurityRole", Set.of(permission));
        accountUser = new Account(
                2,
                "user",
                "samplePassword",
                employee,
                true,
                Set.of(securityRole)
        );
        accountAdmin = new Account(
                1,
                "admin",
                "samplePassword",
                employee,
                true,
                Set.of(securityRole)
        );
    }

    @Test
    void testAdminGetValidTickets() {
        TicketFilterRequest request = new TicketFilterRequest();
        request.setTitle("Sample Ticket");
        request.setBody("Sample Body");
        request.setStatus("filed");
        request.setAssigneeId(2);
        request.setAssigneeName("First1");
        request.setCreatedAfter(Instant.parse("2023-09-16T10:15:30.00Z"));
        request.setCreatedBefore(Instant.parse("2025-09-16T10:15:30.00Z"));
        request.setCreatorName("First1");
        request.setCreatorId(2);
        request.setUpdatedAfter(Instant.parse("2023-09-16T10:15:30.00Z"));
        request.setUpdatedBefore(Instant.parse("2025-09-16T10:15:30.00Z"));
        request.setUpdaterName("First1");
        request.setUpdaterId(2);
        request.setRemarks("Sample Remarks");
        request.setDeleted(false);

        Page<Ticket> ticketPage = new PageImpl<>(List.of(ticket));
        when(ticketRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(ticketPage);
        when(ticketMapper.toResponse(ticket)).thenReturn(ticketResponse1);

        Page<TicketResponse> result = ticketService.getTickets(authAdmin, request, pageable, false);

        assertEquals(1, result.getTotalElements());
        assertEquals("Sample Ticket", result.getContent().get(0).getTitle());
        verify(ticketRepository).findAll(any(Specification.class), any(Pageable.class));
        verify(ticketMapper).toResponse(any(Ticket.class));
    }

    @Test
    void testUserGetValidTickets() {
        TicketFilterRequest request = new TicketFilterRequest();
        request.setTitle("Sample Ticket");
        request.setBody("Sample Body");
        request.setStatus("filed");
        request.setAssigneeId(2);
        request.setAssigneeName("First1");
        request.setCreatedAfter(Instant.parse("2023-09-16T10:15:30.00Z"));
        request.setCreatedBefore(Instant.parse("2025-09-16T10:15:30.00Z"));
        request.setCreatorName("First1");
        request.setCreatorId(2);
        request.setUpdatedAfter(Instant.parse("2023-09-16T10:15:30.00Z"));
        request.setUpdatedBefore(Instant.parse("2025-09-16T10:15:30.00Z"));
        request.setUpdaterName("First1");
        request.setUpdaterId(2);
        request.setRemarks("Sample Remarks");
        request.setDeleted(false);

        Page<Ticket> ticketPage = new PageImpl<>(List.of(ticket));
        when(ticketRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(ticketPage);
        when(ticketMapper.toResponse(ticket)).thenReturn(ticketResponse1);

        Page<TicketResponse> result = ticketService.getTickets(authUser, request, pageable, false);

        assertEquals(1, result.getTotalElements());
        assertEquals("Sample Ticket", result.getContent().get(0).getTitle());
        verify(ticketRepository).findAll(any(Specification.class), any(Pageable.class));
        verify(ticketMapper).toResponse(any(Ticket.class));
    }

    @Test
    void testAdminGetAssignedValidTickets() {
        TicketFilterRequest request = new TicketFilterRequest();
        Page<Ticket> ticketPage = new PageImpl<>(List.of(ticket));
        when(accountRepository.findByUsername(anyString())).thenReturn(Optional.of(accountUser));
        when(ticketRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(ticketPage);
        when(ticketMapper.toResponse(ticket)).thenReturn(ticketResponse1);

        Page<TicketResponse> result = ticketService.getTickets(authAdmin, request, pageable, true);

        assertEquals(1, result.getTotalElements());
        assertEquals("Sample Ticket", result.getContent().get(0).getTitle());
        verify(ticketRepository).findAll(any(Specification.class), any(Pageable.class));
        verify(ticketMapper).toResponse(any(Ticket.class));
    }

    @Test
    void testAdminGetAllTickets() {
        TicketFilterRequest request = new TicketFilterRequest();

        Page<Ticket> ticketPage = new PageImpl<>(List.of(ticket));
        when(ticketRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(ticketPage);
        when(ticketMapper.toResponse(ticket)).thenReturn(ticketResponse1);

        Page<TicketResponse> result = ticketService.getTickets(authAdmin, request, pageable, false);

        assertEquals(1, result.getTotalElements());
        assertEquals("Sample Ticket", result.getContent().get(0).getTitle());
        verify(ticketRepository).findAll(any(Specification.class), any(Pageable.class));
        verify(ticketMapper).toResponse(any(Ticket.class));
    }

    @Test
    void testUserGetAllTickets() {
        TicketFilterRequest request = new TicketFilterRequest();

        Page<Ticket> ticketPage = new PageImpl<>(List.of(ticket));
        when(ticketRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(ticketPage);
        when(ticketMapper.toResponse(ticket)).thenReturn(ticketResponse1);

        Page<TicketResponse> result = ticketService.getTickets(authUser, request, pageable, false);

        assertEquals(1, result.getTotalElements());
        assertEquals("Sample Ticket", result.getContent().get(0).getTitle());
        verify(ticketRepository).findAll(any(Specification.class), any(Pageable.class));
        verify(ticketMapper).toResponse(any(Ticket.class));
    }

    @Test
    void testGetValidDateAfter() {
        TicketFilterRequest request = new TicketFilterRequest();
        request.setCreatedAfter(Instant.parse("2023-09-16T10:15:30.00Z"));
        request.setUpdatedAfter(Instant.parse("2023-09-16T10:15:30.00Z"));

        Page<Ticket> ticketPage = new PageImpl<>(List.of(ticket));
        when(ticketRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(ticketPage);
        when(ticketMapper.toResponse(ticket)).thenReturn(ticketResponse1);

        Page<TicketResponse> result = ticketService.getTickets(authAdmin, request, pageable, false);

        assertEquals(1, result.getTotalElements());
        assertEquals("Sample Ticket", result.getContent().get(0).getTitle());
        verify(ticketRepository).findAll(any(Specification.class), any(Pageable.class));
        verify(ticketMapper).toResponse(any(Ticket.class));
    }

    @Test
    void testGetValidDateBefore() {
        TicketFilterRequest request = new TicketFilterRequest();
        request.setCreatedBefore(Instant.parse("2025-09-16T10:15:30.00Z"));
        request.setUpdatedBefore(Instant.parse("2025-09-16T10:15:30.00Z"));

        Page<Ticket> ticketPage = new PageImpl<>(List.of(ticket));
        when(ticketRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(ticketPage);
        when(ticketMapper.toResponse(ticket)).thenReturn(ticketResponse1);

        Page<TicketResponse> result = ticketService.getTickets(authAdmin, request, pageable, false);

        assertEquals(1, result.getTotalElements());
        assertEquals("Sample Ticket", result.getContent().get(0).getTitle());
        verify(ticketRepository).findAll(any(Specification.class), any(Pageable.class));
        verify(ticketMapper).toResponse(any(Ticket.class));
    }

    @Test
    void testNoFilterGetTickets() {
        TicketFilterRequest request = new TicketFilterRequest();

        Page<Ticket> ticketPage = new PageImpl<>(List.of(ticket));

        when(ticketRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(ticketPage);
        when(ticketMapper.toResponse(any(Ticket.class)))
                .thenReturn(ticketResponse1);

        Page<TicketResponse> result = ticketService.getTickets(authAdmin, request, pageable, false);

        assertEquals(1, result.getTotalElements());
        assertEquals("Sample Ticket", result.getContent().get(0).getTitle());

        verify(ticketRepository).findAll(any(Specification.class), any(Pageable.class));
        verify(ticketMapper).toResponse(any(Ticket.class));
    }


    @Test
    void testNoTicketFoundInFilter() {
        TicketFilterRequest request = new TicketFilterRequest();
        request.setTitle("invalid title");

        Page<Ticket> emptyPage = Page.empty(pageable);
        when(ticketRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(emptyPage);

        Page<TicketResponse> result = ticketService.getTickets(authAdmin, request, pageable, false);

        assertEquals(0, result.getTotalElements());
        assertEquals(0, result.getContent().size());
        verify(ticketRepository).findAll(any(Specification.class), any(Pageable.class));
        verify(ticketMapper, never()).toResponse(any());
    }

    @Test
    void testGetValidTicket() {
        when(ticketRepository.findByTicketNumberAndDeletedFalse(ticket.getTicketNumber())).thenReturn(Optional.of(ticket));
        when(ticketMapper.toResponse(ticket)).thenReturn(ticketResponse1);
        TicketResponse result = ticketService.getTicket(ticket.getTicketNumber());

        assertEquals("Sample Ticket", result.getTitle());
    }

    @Test
    void testGetValidTicketButTicketNotFound() {
        when(ticketRepository.findByTicketNumberAndDeletedFalse(99)).thenReturn(Optional.empty());
        assertThrows(TicketNotFoundException.class, () -> ticketService.getTicket(99));
    }

    @Test
    void testUserFileValidTicket() {
        Name name2 = new Name("First2", "Middle2", "Last2");
        Contacts contacts2 = new Contacts("0912345678", "sample@example.com", "021234567");
        Address address2 = new Address("321 Test St.", "Los Angeles", "California","USA");
        Employee assignedEmployee = new Employee(
                3,
                name2,
                25,
                address2,
                contacts2,
                null,
                null,
                false,
                1
        );

        TicketRequest ticketRequest = new TicketRequest(
                "Sample Ticket",
                "Sample Body",
                3,
                TicketStatus.DUPLICATE,
                "Sample Remarks"
        );
        TicketResponse ticketResponse = new TicketResponse(
                1,
                "Sample Ticket",
                "Sample Body",
                1,
                "Assigned Employee",
                TicketStatus.FILED,
                Instant.now(),
                1,
                "Employee 1",
                Instant.now(),
                1,
                "Employee 1",
                "Sample Remarks"
        );
        when(accountRepository.findByUsername(anyString())).thenReturn(Optional.of(accountUser));
        when(employeeRepository.findByIdAndDeletedFalse(assignedEmployee.getId())).thenReturn(Optional.of(assignedEmployee));
        when(ticketMapper.toEntity(ticketRequest)).thenReturn(ticket);
        when(ticketRepository.save(ticket)).thenReturn(ticket);
        when(ticketMapper.toResponse(ticket)).thenReturn(ticketResponse);

        TicketResponse result = ticketService.fileTicket(ticketRequest, authUser);

        assertEquals("Sample Ticket", result.getTitle());
        assertEquals("Assigned Employee", result.getAssigneeName());
        assertEquals("Employee 1", result.getCreatedByEmployeeName());
    }

    @Test
    void testAdminFileTicket_ThrowAccessDeniedException() {
        TicketServiceImpl ticketServiceSpy = spy(ticketService);
        TicketRequest ticketRequest2 = new TicketRequest();

        doThrow(new AccessDeniedException("Access is denied"))
                .when(ticketServiceSpy)
                .fileTicket(ticketRequest2, authAdmin);

        assertThrows(AccessDeniedException.class, () -> {
            ticketServiceSpy.fileTicket(ticketRequest2, authAdmin);
        });
    }

    @Test
    void testAdminUpdateValidTicketAssignedToEmployee() {
        TicketRequest ticketRequest = new TicketRequest(
                null,
                null,
                null,
                TicketStatus.CLOSED,
                "Updated Remarks"
        );
        Ticket updatedTicket = new Ticket(
                1,
                "Sample Ticket",
                "Sample Body",
                employee,
                TicketStatus.CLOSED,
                Instant.now(),
                employee,
                Instant.now(),
                employee,
                "Updated Remarks",
                false,
                1
        );
        TicketResponse ticketResponse = new TicketResponse(
                1,
                "Sample Ticket",
                "Sample Body",
                1,
                "Employee 1",
                TicketStatus.CLOSED,
                Instant.now(),
                1,
                "Employee 1",
                Instant.now(),
                1,
                "Employee 1",
                "Updated Remarks"
        );
        when(accountRepository.findByUsername(anyString())).thenReturn(Optional.of(accountUser));
        when(employeeRepository.findByIdAndDeletedFalse(1)).thenReturn(Optional.of(employee));
        when(ticketRepository.findByTicketNumberAndDeletedFalse(1)).thenReturn(Optional.of(ticket));
        doNothing().when(ticketMapper).toUpdate(ticketRequest, ticket);
        when(ticketRepository.save(ticket)).thenReturn(updatedTicket);
        when(ticketMapper.toResponse(updatedTicket)).thenReturn(ticketResponse);

        TicketResponse result = ticketService.updateTicket(authAdmin, 1, ticketRequest);

        assertEquals(TicketStatus.CLOSED, result.getStatus());
        assertEquals("Updated Remarks", result.getRemarks());
    }

    @Test
    void testUpdateTicketButAccountNotFound_ThrowAccountNotFoundException() {
        TicketRequest request = new TicketRequest();
        when(accountRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        assertThrows(AccountNotFoundException.class, () -> ticketService.updateTicket(authAdmin, anyInt(), request));
    }

    @Test
    void testUpdateTicketButUpdaterNotFound_ThrowEmployeeNotFoundException() {
        Account account = new Account();
        TicketRequest request = new TicketRequest();
        when(accountRepository.findByUsername(anyString())).thenReturn(Optional.of(account));
        when(employeeRepository.findByIdAndDeletedFalse(INVALID_EMPLOYEE_ID)).thenReturn(Optional.empty());
//        when(ticketRepository.findByTicketNumberAndDeletedFalse(anyInt())).thenReturn(Optional.empty());
        assertThrows(EmployeeNotFoundException.class, () -> ticketService.updateTicket(authAdmin, anyInt(), request));
    }

    @Test
    void testUpdateTicketButTicketNotFound_ThrowTicketNotFoundException() {
        TicketRequest request = new TicketRequest();
        when(accountRepository.findByUsername(anyString())).thenReturn(Optional.of(accountAdmin));
        when(employeeRepository.findByIdAndDeletedFalse(1)).thenReturn(Optional.of(employee));
        when(ticketRepository.findByTicketNumberAndDeletedFalse(99)).thenReturn(Optional.empty());
        assertThrows(TicketNotFoundException.class, () -> ticketService.updateTicket(authAdmin, 99, request));
    }
//
//    @Test
//    void testUnauthorizedAttemptToAlterTicketsThatAreNotAssigned() {
//        Employee employee1 = new Employee();
//        employee1.setId(3);
//        Ticket ticket = new Ticket(
//                2,
//                "Sample Ticket",
//                "Sample Body",
//                employee1,
//                TicketStatus.FILED,
//                Instant.now(),
//                employee1,
//                Instant.now(),
//                employee1,
//                "Sample Remarks",
//                false,
//                1
//        );
//        TicketRequest ticketRequest = new TicketRequest(
//                null,
//                null,
//                null,
//                TicketStatus.CLOSED,
//                "Updated Remarks"
//        );
//
//        when(employeeRepository.findByIdAndDeletedFalse(VALID_EMPLOYEE_ID)).thenReturn(Optional.of(employee1));
//        when(ticketRepository.findByTicketNumberAndDeletedFalse(2)).thenReturn(Optional.of(ticket));
//        assertThrows(UnauthorizedActionException.class, () -> ticketServiceImpl.updateTicket(VALID_EMPLOYEE_ID, 2, ticketRequest));
//    }
//
//    @Test
//    void testUnauthorizedAttemptToUpdateAssignedEmployee() {
//        TicketRequest ticketRequest = new TicketRequest(
//                null,
//                null,
//                3,
//                null,
//                null
//        );
//
//        when(employeeRepository.findByIdAndDeletedFalse(VALID_EMPLOYEE_ID)).thenReturn(Optional.of(employee));
//        when(ticketRepository.findByTicketNumberAndDeletedFalse(1)).thenReturn(Optional.of(ticket));
//        assertThrows(UnauthorizedActionException.class, () -> ticketServiceImpl.updateTicket(VALID_EMPLOYEE_ID, 1, ticketRequest));
//    }
//
//    @Test
//    void testAdminUpdateAssignedEmployee() {
//        Employee employee1 = new Employee();
//        employee1.setId(3);
//        Ticket ticket = new Ticket(
//                2,
//                "Sample Ticket",
//                "Sample Body",
//                employee1,
//                TicketStatus.FILED,
//                Instant.now(),
//                employee1,
//                Instant.now(),
//                employee1,
//                "Sample Remarks",
//                false,
//                1
//        );
//        TicketRequest ticketRequest = new TicketRequest(
//                null,
//                null,
//                1,
//                null,
//                null
//        );
//        Ticket updatedTicket = new Ticket(
//                2,
//                "Sample Ticket",
//                "Sample Body",
//                employee1,
//                TicketStatus.CLOSED,
//                Instant.now(),
//                employee1,
//                Instant.now(),
//                TestDataFactory.admin(),
//                "Updated Remarks",
//                false,
//                1
//        );
//        TicketResponse ticketResponse = new TicketResponse(
//                1,
//                "Sample Ticket",
//                "Sample Body",
//                "Employee 2",
//                TicketStatus.CLOSED,
//                Instant.now(),
//                "Employee 1",
//                Instant.now(),
//                "Admin",
//                "Updated Remarks"
//        );
//        when(employeeRepository.findByIdAndDeletedFalse(TestDataFactory.admin().getId())).thenReturn(Optional.of(TestDataFactory.admin()));
//        when(ticketRepository.findByTicketNumberAndDeletedFalse(2)).thenReturn(Optional.of(ticket));
//        Mockito.doNothing().when(ticketMapper).toUpdate(ticketRequest, ticket);
//        when(ticketRepository.save(ticket)).thenReturn(updatedTicket);
//        when(ticketMapper.toResponse(updatedTicket)).thenReturn(ticketResponse);
//
//        TicketResponse result = ticketServiceImpl.updateTicket(ADMIN_EMPLOYEE_ID, 2, ticketRequest);
//
//        assertEquals("Employee 2", result.getAssigneeName());
//        assertEquals("Admin", result.getUpdatedByEmployeeName());
//    }
//
//    @Test
//    void testAdminUpdateAssignedEmployeeButEmployeeNotFound() {
//        TicketRequest ticketRequest = new TicketRequest(
//                null,
//                null,
//                99,
//                null,
//                null
//        );
//        when(employeeRepository.findByIdAndDeletedFalse(TestDataFactory.admin().getId())).thenReturn(Optional.of(TestDataFactory.admin()));
//        when(ticketRepository.findByTicketNumberAndDeletedFalse(2)).thenReturn(Optional.of(ticket));
//        when(employeeRepository.findByIdAndDeletedFalse(99)).thenReturn(Optional.empty());
//        assertThrows(EmployeeNotFoundException.class, () -> ticketServiceImpl.updateTicket(ADMIN_EMPLOYEE_ID, 2, ticketRequest));
//    }
//
//    @Test
//    void testGetAssignedTickets() {
//        Employee employee1 = new Employee();
//        employee1.setId(3);
//        Ticket ticket2 = new Ticket(
//                2,
//                "Sample Ticket",
//                "Sample Body",
//                employee,
//                TicketStatus.FILED,
//                Instant.now(),
//                employee,
//                Instant.now(),
//                employee,
//                "Sample Remarks",
//                false,
//                1
//        );
//        List<Ticket> tickets = new ArrayList<>();
//        tickets.add(ticket);
//        tickets.add(ticket2); //should not be included
//        List<TicketResponse> ticketResponses = new ArrayList<>();
//        ticketResponses.add(ticketResponse1);
//
//        when(employeeRepository.findByIdAndDeletedFalse(VALID_EMPLOYEE_ID)).thenReturn(Optional.of(TicketServiceImplTest.employee));
//        when(ticketRepository.findByAssigneeIdAndDeletedFalse(VALID_EMPLOYEE_ID)).thenReturn(tickets);
//        when(ticketMapper.toResponseList(tickets)).thenReturn(ticketResponses);
//
//        List<TicketResponse> result = ticketServiceImpl.getAssignedTickets(VALID_EMPLOYEE_ID);
//        assertEquals(1, result.size());
//    }
//
//    @Test
//    void testGetAssignedTicketsButEmployeeNotFound() {
//        when(employeeRepository.findByIdAndDeletedFalse(INVALID_EMPLOYEE_ID)).thenReturn(Optional.empty());
//        assertThrows(EmployeeNotFoundException.class, () -> ticketServiceImpl.getAssignedTickets(INVALID_EMPLOYEE_ID));
//    }
}