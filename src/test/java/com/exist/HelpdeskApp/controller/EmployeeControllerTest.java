package com.exist.HelpdeskApp.controller;

import com.exist.HelpdeskApp.TestDataFactory;
import com.exist.HelpdeskApp.dto.employee.EmployeeFilterRequest;
import com.exist.HelpdeskApp.dto.employee.EmployeeRequest;
import com.exist.HelpdeskApp.dto.employee.EmployeeResponse;
import com.exist.HelpdeskApp.dto.ticket.TicketFilterRequest;
import com.exist.HelpdeskApp.dto.ticket.TicketRequest;
import com.exist.HelpdeskApp.dto.ticket.TicketResponse;
import com.exist.HelpdeskApp.exception.businessexceptions.EmployeeNotFoundException;
import com.exist.HelpdeskApp.exception.businessexceptions.TicketNotFoundException;
import com.exist.HelpdeskApp.model.EmploymentStatus;
import com.exist.HelpdeskApp.model.TicketStatus;
import com.exist.HelpdeskApp.model.embeddable.Address;
import com.exist.HelpdeskApp.model.embeddable.Contacts;
import com.exist.HelpdeskApp.model.embeddable.Name;
import com.exist.HelpdeskApp.service.impl.EmployeeServiceImpl;
import com.exist.HelpdeskApp.service.impl.TicketServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;  

    @MockBean
    private EmployeeServiceImpl employeeServiceImpl;

    @MockBean
    private TicketServiceImpl ticketServiceImpl;

    private static EmployeeRequest employeeRequest1;
    private static EmployeeResponse employee1;

    private static final Integer VALID_EMPLOYEE_ID_1 = 2;
    private static final Integer INVALID_EMPLOYEE_ID = 99;

    @BeforeEach
    void setup() {
        Name name1 = new Name("First1", "Middle1", "Last1");
        Contacts contacts1 = new Contacts("0912345678", "sample@example.com", "021234567");
        Address address1 = new Address("123 Test St.", "Manila", "Region 1", "Philippines");
        employee1 = new EmployeeResponse(
                2,
                name1,
                25,
                address1,
                contacts1,
                EmploymentStatus.FULL_TIME,
                2,
                "role1"
        );

        employeeRequest1 = new EmployeeRequest(
                name1,
                25,
                address1,
                contacts1,
                EmploymentStatus.FULL_TIME,
                1
        );

    }

    @Test
    void testGetValidEmployee() throws Exception {
        when(employeeServiceImpl.getEmployee(VALID_EMPLOYEE_ID_1)).thenReturn(employee1);
        mockMvc.perform(get("/employee/{employeeId}", VALID_EMPLOYEE_ID_1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name.firstName").value("First1"));
    }

    @Test
    void testGetNonExistentEmployee() throws Exception {
        when(employeeServiceImpl.getEmployee(INVALID_EMPLOYEE_ID))
                .thenThrow(new EmployeeNotFoundException("Employee with ID " + INVALID_EMPLOYEE_ID + " not found!"));
        mockMvc.perform(get("/employee/{INVALID_EMPLOYEE_ID}", INVALID_EMPLOYEE_ID))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Employee with ID " + INVALID_EMPLOYEE_ID + " not found!"));
    }

    @Test
    void testGetAllValidEmployees() throws Exception {
        Page<EmployeeResponse> page = new PageImpl<>(List.of(employee1));

        when(employeeServiceImpl.getEmployees(any(EmployeeFilterRequest.class))).thenReturn(page);
        mockMvc.perform(get("/employee")
                        .param("page", "0")
                        .param("size", "5")
                        .param("sortBy", "id")
                        .param("sortDir", "asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(2));
    }

    @Test
    void testGetValidEmployeeAdmin() throws Exception {
        when(employeeServiceImpl.getEmployee(1)).thenReturn(TestDataFactory.adminEmployeeRequest());
        mockMvc.perform(get("/employee/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name.firstName").value("Admin"));
    }

    @Test
    void testAddValidEmployee() throws Exception {
        when(employeeServiceImpl.addEmployee(employeeRequest1)).thenReturn(employee1);
        mockMvc.perform(post("/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeRequest1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name.firstName").value("First1"));
    }

    @Test
    void testUpdateEmployee() throws Exception {
        when(employeeServiceImpl.updateEmployee(VALID_EMPLOYEE_ID_1, employeeRequest1)).thenReturn(employee1);
        mockMvc.perform(
                        patch("/employee/{id}", VALID_EMPLOYEE_ID_1)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(employeeRequest1))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("name.firstName").value("First1"))
                .andExpect(jsonPath("age").value("25"))
                .andExpect(jsonPath("address.country").value("Philippines"))
                .andExpect(jsonPath("contacts.phoneNumber").value("0912345678"))
                .andExpect(jsonPath("employmentStatus").value(EmploymentStatus.FULL_TIME.name()))
                .andExpect(jsonPath("roleId").value(2))
                .andExpect(jsonPath("roleName").value("role1"));
    }

    @Test
    void testDeleteEmployee() throws Exception {
        doNothing().when(employeeServiceImpl).deleteEmployee(INVALID_EMPLOYEE_ID);
        mockMvc.perform(
                        delete("/employee/{id}", INVALID_EMPLOYEE_ID))
                .andExpect(status().isOk());
        verify(employeeServiceImpl, times(1)).deleteEmployee(INVALID_EMPLOYEE_ID);
    }


}
