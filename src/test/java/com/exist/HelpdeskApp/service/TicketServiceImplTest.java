//package com.exist.HelpdeskApp.service;
//
//import com.exist.HelpdeskApp.TestDataFactory;
//import com.exist.HelpdeskApp.dto.ticket.TicketMapper;
//import com.exist.HelpdeskApp.dto.ticket.TicketRequest;
//import com.exist.HelpdeskApp.dto.ticket.TicketResponse;
//import com.exist.HelpdeskApp.exception.businessexceptions.EmployeeNotFoundException;
//import com.exist.HelpdeskApp.exception.businessexceptions.TicketNotFoundException;
//import com.exist.HelpdeskApp.exception.businessexceptions.UnauthorizedActionException;
//import com.exist.HelpdeskApp.model.Employee;
//import com.exist.HelpdeskApp.model.Ticket;
//import com.exist.HelpdeskApp.model.TicketStatus;
//import com.exist.HelpdeskApp.repository.EmployeeRepository;
//import com.exist.HelpdeskApp.repository.TicketRepository;
//import com.exist.HelpdeskApp.service.Implementations.TicketServiceImpl;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.time.Instant;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//
//@ExtendWith(MockitoExtension.class)
//public class TicketServiceImplTest {
//    @Mock
//    EmployeeRepository employeeRepository;
//
//    @Mock
//    TicketRepository ticketRepository;
//
//    @Mock
//    TicketMapper ticketMapper;
//
//    @InjectMocks
//    TicketServiceImpl ticketServiceImpl;
//
//    private static Ticket ticket1;
//    private static Employee employee1;
//    private static TicketRequest ticketRequest1;
//    private static TicketResponse ticketResponse1;
//    private static final Integer VALID_EMPLOYEE_ID = 2;
//    private static final Integer INVALID_EMPLOYEE_ID = 99;
//    private static final Integer ADMIN_EMPLOYEE_ID = 1;
//
//    @BeforeEach
//    void setup() {
//        employee1 = new Employee(
//                2,
//                "Employee 1",
//                25,
//                "Sample Address",
//                "0912345678",
//                null,
//                null,
//                false,
//                1
//        );
//        ticket1 = new Ticket(
//                1,
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
//        ticketRequest1 = new TicketRequest(
//                "Sample Ticket",
//                "Sample Body",
//                2,
//                TicketStatus.FILED,
//                "Sample Remarks"
//        );
//        ticketResponse1 = new TicketResponse(
//                1,
//                "Sample Ticket",
//                "Sample Body",
//                "Employee 1",
//                TicketStatus.FILED,
//                Instant.now(),
//                "Employee 1",
//                Instant.now(),
//                "Employee 1",
//                "Sample Remarks"
//        );
//    }
//
//    @Test
//    void testGetValidTickets() {
//        List<Ticket> tickets = new ArrayList<>();
//        tickets.add(ticket1);
//        List<TicketResponse> ticketResponses = new ArrayList<>();
//        ticketResponses.add(ticketResponse1);
//
//        Mockito.when(employeeRepository.findById(VALID_EMPLOYEE_ID)).thenReturn(Optional.of(employee1));
//        Mockito.when(ticketRepository.findAll()).thenReturn(tickets);
//        Mockito.when(ticketMapper.toResponseList(tickets)).thenReturn(ticketResponses);
//
//        List<TicketResponse> result = ticketServiceImpl.getTickets(VALID_EMPLOYEE_ID);
//
//        assertEquals(1, result.size());
//        assertEquals("Sample Ticket", result.get(0).getTitle());
//    }
//
//    @Test
//    void testGetValidTicketsButEmployeeNotFound() {
//        Mockito.when(employeeRepository.findById(INVALID_EMPLOYEE_ID)).thenReturn(Optional.empty());
//        assertThrows(EmployeeNotFoundException.class, () -> ticketServiceImpl.getTickets(INVALID_EMPLOYEE_ID));
//    }
//
//    @Test
//    void testGetValidTicket() {
//        Mockito.when(employeeRepository.findById(VALID_EMPLOYEE_ID)).thenReturn(Optional.of(employee1));
//        Mockito.when(ticketRepository.findById(ticket1.getTicketNumber())).thenReturn(Optional.of(ticket1));
//        Mockito.when(ticketMapper.toResponse(ticket1)).thenReturn(ticketResponse1);
//
//        TicketResponse result = ticketServiceImpl.getTicket(VALID_EMPLOYEE_ID, ticket1.getTicketNumber());
//
//        assertEquals("Sample Ticket", result.getTitle());
//    }
//
//    @Test
//    void testGetValidTicketButEmployeeNotFound() {
//        Mockito.when(employeeRepository.findById(INVALID_EMPLOYEE_ID)).thenReturn(Optional.empty());
//        assertThrows(EmployeeNotFoundException.class, () -> ticketServiceImpl.getTickets(INVALID_EMPLOYEE_ID));
//    }
//
//    @Test
//    void testGetValidTicketButTicketNotFound() {
//        Mockito.when(employeeRepository.findById(VALID_EMPLOYEE_ID)).thenReturn(Optional.of(employee1));
//        Mockito.when(ticketRepository.findById(99)).thenReturn(Optional.empty());
//        assertThrows(TicketNotFoundException.class, () -> ticketServiceImpl.getTicket(VALID_EMPLOYEE_ID, 99));
//    }
//
//    @Test
//    void testFileValidTicket() {
//        Employee assignedEmployee = new Employee(
//                3,
//                "Assigned Employee",
//                25,
//                "Sample Address",
//                "0912345678",
//                null,
//                null,
//                false,
//                1
//        );
//        TicketRequest ticketRequest = new TicketRequest(
//                "Sample Ticket",
//                "Sample Body",
//                3,
//                TicketStatus.DUPLICATE,
//                "Sample Remarks"
//        );
//        TicketResponse ticketResponse = new TicketResponse(
//                1,
//                "Sample Ticket",
//                "Sample Body",
//                "Assigned Employee",
//                TicketStatus.FILED,
//                Instant.now(),
//                "Employee 1",
//                Instant.now(),
//                "Employee 1",
//                "Sample Remarks"
//        );
//        Mockito.when(employeeRepository.findById(VALID_EMPLOYEE_ID)).thenReturn(Optional.of(employee1));
//        Mockito.when(employeeRepository.findById(assignedEmployee.getId())).thenReturn(Optional.of(assignedEmployee));
//        Mockito.when(ticketMapper.toEntity(ticketRequest)).thenReturn(ticket1);
//        Mockito.when(ticketRepository.save(ticket1)).thenReturn(ticket1);
//        Mockito.when(ticketMapper.toResponse(ticket1)).thenReturn(ticketResponse);
//
//        TicketResponse result = ticketServiceImpl.fileTicket(VALID_EMPLOYEE_ID, ticketRequest);
//
//        assertEquals("Sample Ticket", result.getTitle());
//        assertEquals("Assigned Employee", result.getAssigneeName());
//        assertEquals("Employee 1", result.getCreatedByEmployeeName());
//    }
//
//    @Test
//    void testFileTicketButEmployeeNotFound() {
//        Mockito.when(employeeRepository.findById(INVALID_EMPLOYEE_ID)).thenReturn(Optional.empty());
//        assertThrows(EmployeeNotFoundException.class, () -> ticketServiceImpl.fileTicket(INVALID_EMPLOYEE_ID, ticketRequest1));
//    }
//
//    @Test
//    void testUpdateValidTicketAssignedToEmployee() {
//        TicketRequest ticketRequest = new TicketRequest(
//                null,
//                null,
//                null,
//                TicketStatus.CLOSED,
//                "Updated Remarks"
//        );
//        Ticket updatedTicket = new Ticket(
//                1,
//                "Sample Ticket",
//                "Sample Body",
//                employee1,
//                TicketStatus.CLOSED,
//                Instant.now(),
//                employee1,
//                Instant.now(),
//                employee1,
//                "Updated Remarks",
//                false,
//                1
//        );
//        TicketResponse ticketResponse = new TicketResponse(
//                1,
//                "Sample Ticket",
//                "Sample Body",
//                "Employee 1",
//                TicketStatus.CLOSED,
//                Instant.now(),
//                "Employee 1",
//                Instant.now(),
//                "Employee 1",
//                "Updated Remarks"
//        );
//        Mockito.when(employeeRepository.findById(VALID_EMPLOYEE_ID)).thenReturn(Optional.of(employee1));
//        Mockito.when(ticketRepository.findById(1)).thenReturn(Optional.of(ticket1));
//        Mockito.doNothing().when(ticketMapper).toUpdate(ticketRequest, ticket1);
//        Mockito.when(ticketRepository.save(ticket1)).thenReturn(updatedTicket);
//        Mockito.when(ticketMapper.toResponse(updatedTicket)).thenReturn(ticketResponse);
//
//        TicketResponse result = ticketServiceImpl.updateTicket(VALID_EMPLOYEE_ID, 1, ticketRequest);
//
//        assertEquals(TicketStatus.CLOSED, result.getStatus());
//        assertEquals("Updated Remarks", result.getRemarks());
//    }
//
//    @Test
//    void testUpdateTicketButEmployeeNotFound() {
//        Mockito.when(employeeRepository.findById(INVALID_EMPLOYEE_ID)).thenReturn(Optional.empty());
//        assertThrows(EmployeeNotFoundException.class, () -> ticketServiceImpl.getTickets(INVALID_EMPLOYEE_ID));
//    }
//
//    @Test
//    void testUpdateTicketButTicketNotFound() {
//        Mockito.when(employeeRepository.findById(VALID_EMPLOYEE_ID)).thenReturn(Optional.of(employee1));
//        Mockito.when(ticketRepository.findById(99)).thenReturn(Optional.empty());
//        assertThrows(TicketNotFoundException.class, () -> ticketServiceImpl.getTicket(VALID_EMPLOYEE_ID, 99));
//    }
//
//    @Test
//    void testUnauthorizedAttemptToAlterTicketsThatAreNotAssigned() {
//        Employee employee = new Employee(
//                3,
//                "Employee 2",
//                25,
//                "Sample Address",
//                "0912345678",
//                null,
//                null,
//                false,
//                1
//        );
//        Ticket ticket = new Ticket(
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
//        TicketRequest ticketRequest = new TicketRequest(
//                null,
//                null,
//                null,
//                TicketStatus.CLOSED,
//                "Updated Remarks"
//        );
//
//        Mockito.when(employeeRepository.findById(VALID_EMPLOYEE_ID)).thenReturn(Optional.of(employee1));
//        Mockito.when(ticketRepository.findById(2)).thenReturn(Optional.of(ticket));
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
//        Mockito.when(employeeRepository.findById(VALID_EMPLOYEE_ID)).thenReturn(Optional.of(employee1));
//        Mockito.when(ticketRepository.findById(1)).thenReturn(Optional.of(ticket1));
//        assertThrows(UnauthorizedActionException.class, () -> ticketServiceImpl.updateTicket(VALID_EMPLOYEE_ID, 1, ticketRequest));
//    }
//
//    @Test
//    void testAdminUpdateAssignedEmployee() {
//        Employee employee = new Employee(
//                3,
//                "Employee 2",
//                25,
//                "Sample Address",
//                "0912345678",
//                null,
//                null,
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
//                employee,
//                TicketStatus.CLOSED,
//                Instant.now(),
//                employee,
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
//        Mockito.when(employeeRepository.findById(TestDataFactory.admin().getId())).thenReturn(Optional.of(TestDataFactory.admin()));
//        Mockito.when(ticketRepository.findById(2)).thenReturn(Optional.of(ticket1));
//        Mockito.doNothing().when(ticketMapper).toUpdate(ticketRequest, ticket1);
//        Mockito.when(ticketRepository.save(ticket1)).thenReturn(updatedTicket);
//        Mockito.when(ticketMapper.toResponse(updatedTicket)).thenReturn(ticketResponse);
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
//        Mockito.when(employeeRepository.findById(TestDataFactory.admin().getId())).thenReturn(Optional.of(TestDataFactory.admin()));
//        Mockito.when(ticketRepository.findById(2)).thenReturn(Optional.of(ticket1));
//        Mockito.when(employeeRepository.findById(99)).thenReturn(Optional.empty());
//        assertThrows(EmployeeNotFoundException.class, () -> ticketServiceImpl.updateTicket(ADMIN_EMPLOYEE_ID, 2, ticketRequest));
//    }
//
//    @Test
//    void testGetAssignedTickets() {
//        Employee employee = new Employee(
//                3,
//                "Employee 2",
//                25,
//                "Sample Address",
//                "0912345678",
//                null,
//                null,
//                false,
//                1
//        );
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
//        tickets.add(ticket1);
//        tickets.add(ticket2); //should not be included
//        List<TicketResponse> ticketResponses = new ArrayList<>();
//        ticketResponses.add(ticketResponse1);
//
//        Mockito.when(employeeRepository.findById(VALID_EMPLOYEE_ID)).thenReturn(Optional.of(employee1));
//        Mockito.when(ticketRepository.findByAssigneeId(VALID_EMPLOYEE_ID)).thenReturn(tickets);
//        Mockito.when(ticketMapper.toResponseList(tickets)).thenReturn(ticketResponses);
//
//        List<TicketResponse> result = ticketServiceImpl.getAssignedTickets(VALID_EMPLOYEE_ID);
//        assertEquals(1, result.size());
//    }
//
//    @Test
//    void testGetAssignedTicketsButEmployeeNotFound() {
//        Mockito.when(employeeRepository.findById(INVALID_EMPLOYEE_ID)).thenReturn(Optional.empty());
//        assertThrows(EmployeeNotFoundException.class, () -> ticketServiceImpl.getAssignedTickets(INVALID_EMPLOYEE_ID));
//    }
//}
