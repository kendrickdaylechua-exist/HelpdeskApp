package com.exist.HelpdeskApp.controller;

import com.exist.HelpdeskApp.exception.businessexceptions.EmployeeNotFoundException;
import com.exist.HelpdeskApp.exception.GlobalExceptionHandler;
import com.exist.HelpdeskApp.exception.businessexceptions.RoleNotFoundException;
import com.exist.HelpdeskApp.service.Implementations.EmployeeServiceImpl;
import com.exist.HelpdeskApp.service.Implementations.RoleServiceImpl;
import com.exist.HelpdeskApp.service.Implementations.TicketServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {EmployeeController.class, TicketController.class, RoleController.class})
@Import(GlobalExceptionHandler.class)
public class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EmployeeServiceImpl employeeServiceImpl;

    @MockBean
    private RoleServiceImpl roleServiceImpl;

    @MockBean
    private TicketServiceImpl ticketServiceImpl;

    @Test
    void testGetInvalidEmployee_ShouldReturnNotFound() throws Exception {
        Integer invalidEmployeeId = 99;
        when(employeeServiceImpl.getEmployee(invalidEmployeeId))
                .thenThrow(new EmployeeNotFoundException("Employee with ID " + invalidEmployeeId + " not found!"));

        mockMvc.perform(get("/employee/{employeeId}", invalidEmployeeId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Employee with ID " + invalidEmployeeId + " not found!"))
                .andExpect(jsonPath("$.path").value("/employee/" + invalidEmployeeId))
                .andExpect(jsonPath("$.timestamp").exists());
    }


    @Test
    void testGetInvalidRole_ShouldReturnNotFound() throws Exception {
        Integer roleId = 99;
        when(roleServiceImpl.getRole(roleId))
                .thenThrow(new RoleNotFoundException("Role not found!"));

        mockMvc.perform(get("/admin/roles/{roleId}", roleId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Role not found!"))
                .andExpect(jsonPath("$.path").value("/admin/roles/" + roleId))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void testGetInvalidTicket_ShouldReturnNotFound() throws Exception {
        Integer employeeId = 1;
        Integer ticketId = 99;
        when(ticketServiceImpl.getTicket(employeeId, ticketId))
                .thenThrow(new RoleNotFoundException("Ticket not found!"));

        mockMvc.perform(get("/admin/tickets/{ticketId}", ticketId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Ticket not found!"))
                .andExpect(jsonPath("$.path").value("/admin/tickets/" + ticketId))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void testHandleRuntimeException_ShouldReturnInternalServerError() throws Exception {
        when(employeeServiceImpl.getEmployee(1))
                .thenThrow(new RuntimeException("Unexpected failure"));

        mockMvc.perform(get("/employee/{id}", 1))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.error").value("Please contact the devs"))
                .andExpect(jsonPath("$.message").value("Unexpected failure"))
                .andExpect(jsonPath("$.path").value("/employee/1"))
                .andExpect(jsonPath("$.timestamp").exists());
    }



}
