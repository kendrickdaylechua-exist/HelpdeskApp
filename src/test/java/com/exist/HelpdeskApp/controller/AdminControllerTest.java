package com.exist.HelpdeskApp.controller;

import com.exist.HelpdeskApp.TestDataFactory;
import com.exist.HelpdeskApp.dto.employee.EmployeeRequest;
import com.exist.HelpdeskApp.dto.employee.EmployeeResponse;
import com.exist.HelpdeskApp.dto.role.RoleRequest;
import com.exist.HelpdeskApp.dto.role.RoleResponse;
import com.exist.HelpdeskApp.dto.ticket.TicketRequest;
import com.exist.HelpdeskApp.dto.ticket.TicketResponse;
import com.exist.HelpdeskApp.exception.EmployeeNotFoundException;
import com.exist.HelpdeskApp.model.EmploymentStatus;
import com.exist.HelpdeskApp.model.TicketStatus;
import com.exist.HelpdeskApp.service.EmployeeService;
import com.exist.HelpdeskApp.service.RoleService;
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

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminController.class)
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EmployeeService employeeService;

    @MockBean
    private RoleService roleService;

    @MockBean
    private TicketService ticketService;

    private static RoleResponse role1;

    private static EmployeeResponse employee1;

    private static TicketResponse ticket1;

    private static EmployeeRequest employeeRequest1;
    private static RoleRequest roleRequest1;

    private final static Integer VALID_EMPLOYEE_ID_1 = 1;
    private final static Integer INVALID_EMPLOYEE_ID = 99;

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



        role1 = new RoleResponse(
                2,
                "role1"
        );

        ticket1 = new TicketResponse(
                1,
                "Test Ticket 1",
                "Test ticket body 1",
                "name1",
                TicketStatus.FILED,
                Instant.now(),
                "name2",
                Instant.now(),
                "name2",
                "Test remarks 1"
        );

        employeeRequest1 = new EmployeeRequest(
                "name1",
                25,
                "test address 1",
                "09~~~~~~~~~~",
                EmploymentStatus.FULL_TIME,
                1
        );

        roleRequest1 = new RoleRequest(
                "role1"
        );
    }

    @Test
    void testAdminHome() throws Exception {
        mockMvc.perform(get("/admin")) // since @RequestMapping("")
                .andExpect(status().isOk())
                .andExpect(content().string("This is the admin page.\n" +
                        "=================================\n" +
                        "APIs:\n" +
                        "/employees"));
    }

    @Test
    void testGetAllValidEmployees() throws Exception {
        List<EmployeeResponse> employeeList = new ArrayList<>();
        employeeList.add(employee1);
        when(employeeService.getEmployees()).thenReturn(employeeList);
        mockMvc.perform(get("/admin/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("[0].name").value("name1"));
    }

    @Test
    void testGetValidEmployee() throws Exception {
        when(employeeService.getEmployee(2)).thenReturn(employee1);
        mockMvc.perform(get("/admin/employees/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value("name1"));
    }

    @Test
    void testGetNonExistentEmployee() throws Exception {
        when(employeeService.getEmployee(INVALID_EMPLOYEE_ID))
                .thenThrow(new EmployeeNotFoundException("Employee with ID " + INVALID_EMPLOYEE_ID + " not found!"));

        mockMvc.perform(get("/admin/employees/{employeeId}", INVALID_EMPLOYEE_ID))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Employee with ID " + INVALID_EMPLOYEE_ID + " not found!"));
    }

    @Test
    void testGetValidEmployeeAdmin() throws Exception {
        when(employeeService.getEmployee(1)).thenReturn(TestDataFactory.adminEmployeeRequest());
        mockMvc.perform(get("/admin/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value("Admin"));
    }

    @Test
    void testAddValidEmployee() throws Exception {
        when(employeeService.addEmployee(employeeRequest1)).thenReturn(employee1);
        mockMvc.perform(post("/admin/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeRequest1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value("name1"));
    }

    @Test
    void testUpdateEmployee() throws Exception {
        EmployeeResponse employee2 = new EmployeeResponse(
                3,
                "name2",
                25,
                "test address 2",
                "09~~~~~~~~~~",
                EmploymentStatus.FULL_TIME,
                3,
                "role2"
        );
        when(employeeService.updateEmployee(VALID_EMPLOYEE_ID_1, employeeRequest1)).thenReturn(employee1);
        mockMvc.perform(
                patch("/admin/employees/{id}", VALID_EMPLOYEE_ID_1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeRequest1))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value("name1"))
                .andExpect(jsonPath("age").value("25"))
                .andExpect(jsonPath("address").value("test address 1"))
                .andExpect(jsonPath("contactNumber").value("09~~~~~~~~~~"))
                .andExpect(jsonPath("employmentStatus").value(EmploymentStatus.FULL_TIME.name()))
                .andExpect(jsonPath("roleId").value(2))
                .andExpect(jsonPath("roleName").value("role1"));
    }

    @Test
    void testDeleteEmployee() throws Exception {
        doNothing().when(employeeService).deleteEmployee(INVALID_EMPLOYEE_ID);
        mockMvc.perform(
                delete("/admin/employees/{id}", INVALID_EMPLOYEE_ID))
                .andExpect(status().isOk());
        verify(employeeService, times(1)).deleteEmployee(INVALID_EMPLOYEE_ID);
    }

    @Test
    void testGetAllValidRoles() throws Exception {
        List<RoleResponse> roleResponses = new ArrayList<>();
        roleResponses.add(role1);
        when(roleService.getRoles()).thenReturn(roleResponses);
        mockMvc.perform(get("/admin/roles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("[0].roleName").value("role1"));
    }

    @Test
    void testGetValidRole() throws Exception {
        Integer roleId = 2;
        when(roleService.getRole(2)).thenReturn(role1);
        mockMvc.perform(get("/admin/roles/{roleId}", roleId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("roleName").value("role1"));
    }

    @Test
    void testAddValidRole() throws Exception {
        when(roleService.addRole(roleRequest1)).thenReturn(role1);
        mockMvc.perform(post("/admin/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(roleRequest1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("roleName").value("role1"));
    }

    @Test
    void testUpdateValidRole() throws Exception {
        Integer roleId = 2;
        when(roleService.updateRole(roleId, roleRequest1)).thenReturn(role1);
        mockMvc.perform(
                        patch("/admin/roles/{id}", roleId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(roleRequest1))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("roleName").value("role1"));
    }

    @Test
    void testDeleteValidRole() throws Exception {
        Integer roleId = 2;
        doNothing().when(roleService).deleteRole(roleId);
        mockMvc.perform(
                delete("/admin/roles/{id}", roleId))
                .andExpect(status().isOk());
        verify(roleService, times(1)).deleteRole(roleId);
    }

    @Test
    void testGetAllValidTickets() throws Exception {
        List<TicketResponse> ticketResponses = new ArrayList<>();
        ticketResponses.add(ticket1);
        when(ticketService.getTickets(1)).thenReturn(ticketResponses);
        mockMvc.perform(get("/admin/tickets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("[0].title").value("Test Ticket 1"));
    }

    @Test
    void testGetValidTicket() throws Exception {
        Integer ticketNumber = 1;
        when(ticketService.getTicket(1, ticketNumber)).thenReturn(ticket1);
        mockMvc.perform(get("/admin/tickets/{ticketNumber}", ticketNumber))
                .andExpect(status().isOk())
                .andExpect(jsonPath("title").value("Test Ticket 1"));
    }

    @Test
    void testAssignTicket() throws Exception {
        TicketRequest ticketLocalRequest = new TicketRequest(
                null,
                null,
                2,
                null,
                null
        );
        Integer ticketId = 1;
        when(ticketService.updateTicket(1, ticketId, ticketLocalRequest)).thenReturn(ticket1);
        mockMvc.perform(patch("/admin/tickets/{ticketId}", ticketId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ticketLocalRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("assigneeName").value("name1"));
    }

    @Test
    void testUpdateTicketStatusAndRemarks() throws Exception {
        TicketResponse ticket2 = new TicketResponse(
                2,
                "Test Ticket 2",
                "Test ticket body 2",
                "name2",
                TicketStatus.DUPLICATE,
                Instant.now(),
                "name1",
                Instant.now(),
                "name1",
                "Test remarks 2"
        );
        TicketRequest ticketLocalRequest = new TicketRequest(
                null,
                null,
                null,
                TicketStatus.DUPLICATE,
                "Test Remarks 2"
        );
        Integer ticketId = 2;
        when(ticketService.updateTicket(1, ticketId, ticketLocalRequest)).thenReturn(ticket2);
        mockMvc.perform(patch("/admin/tickets/{ticketId}", ticketId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ticketLocalRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(TicketStatus.DUPLICATE.name()))
                .andExpect(jsonPath("remarks").value("Test remarks 2"));
    }
}
