//package com.exist.HelpdeskApp.controller;
//
//import com.exist.HelpdeskApp.dto.ticket.TicketFilterRequest;
//import com.exist.HelpdeskApp.dto.ticket.TicketRequest;
//import com.exist.HelpdeskApp.dto.ticket.TicketResponse;
//import com.exist.HelpdeskApp.exception.businessexceptions.EmployeeNotFoundException;
//import com.exist.HelpdeskApp.exception.businessexceptions.TicketNotFoundException;
//import com.exist.HelpdeskApp.model.TicketStatus;
//import com.exist.HelpdeskApp.service.impl.TicketServiceImpl;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.time.Instant;
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@WebMvcTest(TicketController.class)
//public class TicketControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @MockBean
//    private TicketServiceImpl ticketService;
//
//    private static TicketRequest ticketRequest1;
//    private static TicketResponse ticket1;
//
//    private final static Integer VALID_EMPLOYEE_ID_1 = 1;
//    private final static Integer INVALID_EMPLOYEE_ID = 99;
//
//    @BeforeEach
//    void setup() {
//        ticket1 = new TicketResponse(
//                1,
//                "Test Ticket 1",
//                "Test ticket body 1",
//                "name1",
//                TicketStatus.FILED,
//                Instant.now(),
//                "name2",
//                Instant.now(),
//                "name2",
//                "Test remarks 1"
//        );
//
//        ticketRequest1 = new TicketRequest(
//                "Ticket 1",
//                "Body of ticket 1",
//                2,
//                TicketStatus.FILED,
//                "Remarks of ticket 1"
//        );
//    }
//
//    @Test
//    void testAssignTicket() throws Exception {
//        TicketRequest ticketLocalRequest = new TicketRequest(
//                null,
//                null,
//                2,
//                null,
//                null
//        );
//        Integer ticketId = 1;
//        when(ticketService.updateTicket(1, ticketId, ticketLocalRequest)).thenReturn(ticket1);
//        mockMvc.perform(patch("/ticket/{employeeId}/{ticketId}", VALID_EMPLOYEE_ID_1, ticketId, ticketRequest1)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(ticketLocalRequest)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("assigneeName").value("name1"));
//    }
//
//    @Test
//    void testUpdateTicketStatusAndRemarks() throws Exception {
//        TicketResponse ticket2 = new TicketResponse(
//                2,
//                "Test Ticket 2",
//                "Test ticket body 2",
//                "name2",
//                TicketStatus.DUPLICATE,
//                Instant.now(),
//                "name1",
//                Instant.now(),
//                "name1",
//                "Test remarks 2"
//        );
//        TicketRequest ticketLocalRequest = new TicketRequest(
//                null,
//                null,
//                null,
//                TicketStatus.DUPLICATE,
//                "Test Remarks 2"
//        );
//        Integer ticketId = 2;
//        when(ticketService.updateTicket(1, ticketId, ticketLocalRequest)).thenReturn(ticket2);
//        mockMvc.perform(patch("/ticket/{employeeId}/{ticketId}", VALID_EMPLOYEE_ID_1, ticketId, ticketRequest1)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(ticketLocalRequest)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("status").value(TicketStatus.DUPLICATE.name()))
//                .andExpect(jsonPath("remarks").value("Test remarks 2"));
//    }
//
//    @Test
//    void testGetAllValidTickets() throws Exception {
//        Page<TicketResponse> page = new PageImpl<>(List.of(ticket1));
//        when(ticketService.getTickets(eq(VALID_EMPLOYEE_ID_1), any(TicketFilterRequest.class))).thenReturn(page);
//        mockMvc.perform(get("/ticket/{VALID_EMPLOYEE_ID_1}", VALID_EMPLOYEE_ID_1))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath(".content[0].ticketNumber").value(1));
//    }
//
//    @Test
//    void testInvalidEmployeeIdToGetTicket() throws Exception {
//        when(ticketService.getTickets(eq(INVALID_EMPLOYEE_ID), any(TicketFilterRequest.class)))
//                .thenThrow(new EmployeeNotFoundException("Employee with ID " + INVALID_EMPLOYEE_ID + " not found!"));
//        mockMvc.perform(get("/ticket/{INVALID_EMPLOYEE_ID}", INVALID_EMPLOYEE_ID))
//                .andExpect(status().isNotFound())
//                .andExpect(jsonPath("$.message").value("Employee with ID " + INVALID_EMPLOYEE_ID + " not found!"));
//    }
//
//    @Test
//    void testFileValidTickets() throws Exception {
//        when(ticketService.fileTicket(VALID_EMPLOYEE_ID_1, ticketRequest1))
//                .thenReturn(ticket1);
//        mockMvc.perform(post("/ticket/{VALID_EMPLOYEE_ID}", VALID_EMPLOYEE_ID_1, ticketRequest1)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(ticketRequest1)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("title").value("Test Ticket 1"));
//    }
//
//    @Test
//    void testInvalidEmployeeIdToFileTicket() throws Exception{
//        when(ticketService.fileTicket(INVALID_EMPLOYEE_ID, ticketRequest1))
//                .thenThrow(new EmployeeNotFoundException("Employee with ID " + INVALID_EMPLOYEE_ID + " not found!"));
//        mockMvc.perform(post("/ticket/{INVALID_EMPLOYEE_ID}", INVALID_EMPLOYEE_ID)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(ticketRequest1)))
//                .andExpect(status().isNotFound())
//                .andExpect(jsonPath("$.message").value("Employee with ID " + INVALID_EMPLOYEE_ID + " not found!"));
//    }
//
//    @Test
//    void testGetValidTicket() throws Exception {
//        Integer ticketId = 1;
//        when(ticketService.getTicket(VALID_EMPLOYEE_ID_1, ticketId))
//                .thenReturn(ticket1);
//        mockMvc.perform(get("/ticket/{VALID_EMPLOYEE_ID_1}/{ticketId}", VALID_EMPLOYEE_ID_1, ticketId))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("title").value("Test Ticket 1"));
//    }
//
//    @Test
//    void testGetInvalidTicket() throws Exception {
//        Integer ticketId = 99;
//        when(ticketService.getTicket(VALID_EMPLOYEE_ID_1, ticketId))
//                .thenThrow(new TicketNotFoundException("Ticket number " + ticketId + " not found!"));
//        mockMvc.perform(get("/ticket/{VALID_EMPLOYEE_ID_1}/{ticketId}", VALID_EMPLOYEE_ID_1, ticketId))
//                .andExpect(status().isNotFound())
//                .andExpect(jsonPath("$.message").value("Ticket number " + ticketId + " not found!"));;
//    }
//
//    @Test
//    void testGetInvalidEmployeeToGetTicket() throws Exception{
//        Integer ticketId = 1;
//        when(ticketService.getTicket(INVALID_EMPLOYEE_ID, ticketId))
//                .thenThrow(new EmployeeNotFoundException("Employee with ID " + INVALID_EMPLOYEE_ID + " not found!"));
//        mockMvc.perform(get("/ticket/{INVALID_EMPLOYEE_ID}/{ticketId}", INVALID_EMPLOYEE_ID, ticketId))
//                .andExpect(status().isNotFound())
//                .andExpect(jsonPath("$.message").value("Employee with ID " + INVALID_EMPLOYEE_ID + " not found!"));
//    }
//
//    @Test
//    void testUpdateValidTicket() throws Exception{
//        TicketResponse ticket2 = new TicketResponse(
//                2,
//                "Ticket 2",
//                "Body of ticket 2",
//                "name2",
//                TicketStatus.DUPLICATE,
//                Instant.parse("2025-09-07T10:15:30Z"),
//                "name1",
//                Instant.parse("2025-09-07T10:15:30Z"),
//                "name1",
//                "Remarks of ticket 2"
//        );
//        TicketRequest ticketRequest2 = new TicketRequest(
//                "Ticket 2",
//                "Body of ticket 2",
//                2,
//                TicketStatus.FILED,
//                "Remarks of ticket 2"
//        );
//        Integer ticketId = 1;
//        when(ticketService.updateTicket(VALID_EMPLOYEE_ID_1, ticketId, ticketRequest2)).thenReturn(ticket2);
//        mockMvc.perform(patch("/ticket/{VALID_EMPLOYEE_ID_1}/{ticketId}", VALID_EMPLOYEE_ID_1, ticketId)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(ticketRequest2)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("status").value(TicketStatus.DUPLICATE.name()))
//                .andExpect(jsonPath("remarks").value("Remarks of ticket 2"));
//    }
//
//    @Test
//    void testUpdateInvalidTicket() throws Exception{
//        Integer ticketId = 99;
//        TicketRequest ticketRequest2 = new TicketRequest(
//                "Ticket 2",
//                "Body of ticket 2",
//                2,
//                TicketStatus.FILED,
//                "Remarks of ticket 2"
//        );
//        when(ticketService.updateTicket(VALID_EMPLOYEE_ID_1, ticketId, ticketRequest2))
//                .thenThrow(new TicketNotFoundException("Ticket number " + ticketId + " not found!"));
//        mockMvc.perform(patch("/ticket/{VALID_EMPLOYEE_ID_1}/{ticketId}", VALID_EMPLOYEE_ID_1, ticketId)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(ticketRequest2)))
//                .andExpect(status().isNotFound())
//                .andExpect(jsonPath("$.message").value("Ticket number " + ticketId + " not found!"));
//    }
//
//    @Test
//    void testUpdateInvalidEmployeeToUpdateTicket() throws Exception {
//        Integer ticketId = 2;
//        TicketRequest ticketRequest2 = new TicketRequest(
//                "Ticket 2",
//                "Body of ticket 2",
//                2,
//                TicketStatus.FILED,
//                "Remarks of ticket 2"
//        );
//        when(ticketService.updateTicket(INVALID_EMPLOYEE_ID, ticketId, ticketRequest2))
//                .thenThrow(new EmployeeNotFoundException("Employee with ID " + INVALID_EMPLOYEE_ID + " not found!"));
//        mockMvc.perform(patch("/ticket/{INVALID_EMPLOYEE_ID}/{ticketId}", INVALID_EMPLOYEE_ID, ticketId)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(ticketRequest2)))
//                .andExpect(status().isNotFound())
//                .andExpect(jsonPath("$.message").value("Employee with ID " + INVALID_EMPLOYEE_ID + " not found!"));
//    }
//
//    @Test
//    void testGetValidAssignedTickets() throws Exception {
//        List<TicketResponse> ticketResponses = new ArrayList<>();
//        ticketResponses.add(ticket1);
//        when(ticketService.getAssignedTickets(VALID_EMPLOYEE_ID_1))
//                .thenReturn(ticketResponses);
//        mockMvc.perform(get("/ticket/{employeeId}/assigned", VALID_EMPLOYEE_ID_1))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.size()").value(1))
//                .andExpect(jsonPath("[0].ticketNumber").value(1));
//    }
//
//    @Test
//    void testGetInvalidEmployeeToGetAssignedTickets() throws Exception {
//        when(ticketService.getAssignedTickets(INVALID_EMPLOYEE_ID))
//                .thenThrow(new EmployeeNotFoundException("Employee with ID " + INVALID_EMPLOYEE_ID + " not found!"));
//        mockMvc.perform(get("/ticket/{INVALID_EMPLOYEE_ID}/assigned", INVALID_EMPLOYEE_ID))
//                .andExpect(status().isNotFound())
//                .andExpect(jsonPath("$.message").value("Employee with ID " + INVALID_EMPLOYEE_ID + " not found!"));
//    }
//}
