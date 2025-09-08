package com.exist.HelpdeskApp.controller;

import com.exist.HelpdeskApp.dto.employee.EmployeeResponse;
import com.exist.HelpdeskApp.dto.ticket.TicketRequest;
import com.exist.HelpdeskApp.dto.ticket.TicketResponse;
import com.exist.HelpdeskApp.exception.EmployeeNotFoundException;
import com.exist.HelpdeskApp.exception.TicketNotFoundException;
import com.exist.HelpdeskApp.model.EmploymentStatus;
import com.exist.HelpdeskApp.model.TicketStatus;
import com.exist.HelpdeskApp.service.EmployeeService;
import com.exist.HelpdeskApp.service.TicketService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EmployeeService employeeService;

    @MockBean
    private TicketService ticketService;

    private static EmployeeResponse employee1;
    private static EmployeeResponse employee2;

    private static TicketResponse ticket1;
    private static TicketResponse ticket2;

    private static List<EmployeeResponse> employeeList = new ArrayList<>();
    private static List<TicketResponse> ticketList = new ArrayList<>();

    private static TicketRequest ticketRequest1;
    private static TicketRequest ticketRequest2;

    private static final Integer VALID_EMPLOYEE_ID_1 = 2;
    private static final Integer INVALID_EMPLOYEE_ID = 99;

    @BeforeEach
    void setup() {
        employee1 = new EmployeeResponse(
                2,
                "name1",
                25,
                "test address 1",
                "09~~~~~~~~~~",
                EmploymentStatus.FULL_TIME,
                2,
                "role1"
        );

        employee2 = new EmployeeResponse(
                3,
                "name2",
                25,
                "test address 2",
                "09~~~~~~~~~~",
                EmploymentStatus.FULL_TIME,
                3,
                "role2"
        );

        ticket1 = new TicketResponse(
                1,
                "Ticket 1",
                "Body of ticket 1",
                "name1",
                TicketStatus.FILED,
                Instant.parse("2025-09-07T10:15:30Z"),
                "name2",
                Instant.parse("2025-09-07T10:15:30Z"),
                "name2",
                "Remarks of ticket 1"
        );

        ticket2 = new TicketResponse(
                2,
                "Ticket 2",
                "Body of ticket 2",
                "name2",
                TicketStatus.DUPLICATE,
                Instant.parse("2025-09-07T10:15:30Z"),
                "name1",
                Instant.parse("2025-09-07T10:15:30Z"),
                "name1",
                "Remarks of ticket 2"
        );

        employeeList = new ArrayList<>();
        ticketList = new ArrayList<>();

        employeeList.add(employee1);
        employeeList.add(employee2);
        ticketList.add(ticket1);
        ticketList.add(ticket2);

        ticketRequest1 = new TicketRequest(
                "Ticket 1",
                "Body of ticket 1",
                2,
                TicketStatus.FILED,
                "Remarks of ticket 1"
        );
        ticketRequest2 = new TicketRequest(
                "Ticket 2",
                "Body of ticket 2",
                2,
                TicketStatus.FILED,
                "Remarks of ticket 2"
        );

    }

    @Test
    void testEmployeeHome() throws Exception {
        mockMvc.perform(get("/employees")) // since @RequestMapping("")
                .andExpect(status().isOk())
                .andExpect(content().string("This is the employee's page. Please enter your ID number..."));
    }

    @Test
    void testGetValidEmployee() throws Exception {
        when(employeeService.getEmployee(VALID_EMPLOYEE_ID_1)).thenReturn(employee1);
        mockMvc.perform(get("/employees/{employeeId}", VALID_EMPLOYEE_ID_1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value("name1"));
    }

    @Test
    void testGetNonExistentEmployee() throws Exception {
        when(employeeService.getEmployee(INVALID_EMPLOYEE_ID))
                .thenThrow(new EmployeeNotFoundException("Employee with ID " + INVALID_EMPLOYEE_ID + " not found!"));
        mockMvc.perform(get("/employees/{INVALID_EMPLOYEE_ID}", INVALID_EMPLOYEE_ID))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Employee with ID " + INVALID_EMPLOYEE_ID + " not found!"));
    }

    @Test
    void testGetAllValidTickets() throws Exception {
        when(ticketService.getTickets(VALID_EMPLOYEE_ID_1)).thenReturn(ticketList);
        mockMvc.perform(get("/employees/{VALID_EMPLOYEE_ID_1}/tickets", VALID_EMPLOYEE_ID_1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("[0].ticketNumber").value(1))
                .andExpect(jsonPath("[1].ticketNumber").value(2));
    }

    @Test
    void testInvalidEmployeeIdToGetTicket() throws Exception {
        when(ticketService.getTickets(INVALID_EMPLOYEE_ID))
                .thenThrow(new EmployeeNotFoundException("Employee with ID " + INVALID_EMPLOYEE_ID + " not found!"));
        mockMvc.perform(get("/employees/{INVALID_EMPLOYEE_ID}/tickets", INVALID_EMPLOYEE_ID))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Employee with ID " + INVALID_EMPLOYEE_ID + " not found!"));
    }

    @Test
    void testFileValidTickets() throws Exception {
        when(ticketService.fileTicket(VALID_EMPLOYEE_ID_1, ticketRequest1))
                .thenReturn(ticket1);
        mockMvc.perform(post("/employees/{VALID_EMPLOYEE_ID}/tickets", VALID_EMPLOYEE_ID_1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ticketRequest1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("title").value("Ticket 1"));
    }

    @Test
    void testInvalidEmployeeIdToFileTicket() throws Exception{
        when(ticketService.fileTicket(INVALID_EMPLOYEE_ID, ticketRequest1))
                .thenThrow(new EmployeeNotFoundException("Employee with ID " + INVALID_EMPLOYEE_ID + " not found!"));
        mockMvc.perform(post("/employees/{INVALID_EMPLOYEE_ID}/tickets", INVALID_EMPLOYEE_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ticketRequest1)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Employee with ID " + INVALID_EMPLOYEE_ID + " not found!"));
    }

    @Test
    void testGetValidTicket() throws Exception {
        Integer ticketId = 1;
        when(ticketService.getTicket(VALID_EMPLOYEE_ID_1, ticketId))
                .thenReturn(ticket1);
        mockMvc.perform(get("/employees/{VALID_EMPLOYEE_ID_1}/tickets/{ticketId}", VALID_EMPLOYEE_ID_1, ticketId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("title").value("Ticket 1"));
    }

    @Test
    void testGetInvalidTicket() throws Exception {
        Integer ticketId = 99;
        when(ticketService.getTicket(VALID_EMPLOYEE_ID_1, ticketId))
                .thenThrow(new TicketNotFoundException("Ticket number " + ticketId + " not found!"));
        mockMvc.perform(get("/employees/{VALID_EMPLOYEE_ID_1}/tickets/{ticketId}", VALID_EMPLOYEE_ID_1, ticketId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Ticket number " + ticketId + " not found!"));;
    }

    @Test
    void testGetInvalidEmployeeToGetTicket() throws Exception{
        Integer ticketId = 1;
        when(ticketService.getTicket(INVALID_EMPLOYEE_ID, ticketId))
                .thenThrow(new EmployeeNotFoundException("Employee with ID " + INVALID_EMPLOYEE_ID + " not found!"));
        mockMvc.perform(get("/employees/{INVALID_EMPLOYEE_ID}/tickets/{ticketId}", INVALID_EMPLOYEE_ID, ticketId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Employee with ID " + INVALID_EMPLOYEE_ID + " not found!"));
    }

    @Test
    void testUpdateValidTicket() throws Exception{
        Integer ticketId = 1;
        when(ticketService.updateTicket(VALID_EMPLOYEE_ID_1, ticketId, ticketRequest2)).thenReturn(ticket2);
        mockMvc.perform(patch("/employees/{VALID_EMPLOYEE_ID_1}/tickets/{ticketId}", VALID_EMPLOYEE_ID_1, ticketId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ticketRequest2)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(TicketStatus.DUPLICATE.name()))
                .andExpect(jsonPath("remarks").value("Remarks of ticket 2"));
    }

    @Test
    void testUpdateInvalidTicket() throws Exception{
        Integer ticketId = 99;
        when(ticketService.updateTicket(VALID_EMPLOYEE_ID_1, ticketId, ticketRequest2))
                .thenThrow(new TicketNotFoundException("Ticket number " + ticketId + " not found!"));
        mockMvc.perform(patch("/employees/{VALID_EMPLOYEE_ID_1}/tickets/{ticketId}", VALID_EMPLOYEE_ID_1, ticketId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ticketRequest2)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Ticket number " + ticketId + " not found!"));
    }

    @Test
    void testUpdateInvalidEmployeeToUpdateTicket() throws Exception {
        Integer ticketId = 2;
        when(ticketService.updateTicket(INVALID_EMPLOYEE_ID, ticketId, ticketRequest2))
                .thenThrow(new EmployeeNotFoundException("Employee with ID " + INVALID_EMPLOYEE_ID + " not found!"));
        mockMvc.perform(patch("/employees/{INVALID_EMPLOYEE_ID}/tickets/{ticketId}", INVALID_EMPLOYEE_ID, ticketId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ticketRequest2)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Employee with ID " + INVALID_EMPLOYEE_ID + " not found!"));
    }

    @Test
    void testGetValidAssignedTickets() throws Exception {
        when(ticketService.getAssignedTickets(VALID_EMPLOYEE_ID_1))
                .thenReturn(ticketList);
        mockMvc.perform(get("/employees/{employeeId}/tickets/assigned", VALID_EMPLOYEE_ID_1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("[0].ticketNumber").value(1))
                .andExpect(jsonPath("[1].ticketNumber").value(2));
    }

    @Test
    void testGetInvalidEmployeeToGetAssignedTickets() throws Exception {
        when(ticketService.getAssignedTickets(INVALID_EMPLOYEE_ID))
                .thenThrow(new EmployeeNotFoundException("Employee with ID " + INVALID_EMPLOYEE_ID + " not found!"));
        mockMvc.perform(get("/employees/{INVALID_EMPLOYEE_ID}/tickets/assigned", INVALID_EMPLOYEE_ID))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Employee with ID " + INVALID_EMPLOYEE_ID + " not found!"));
    }
}
