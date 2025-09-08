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
    private static RoleResponse role2;

    private static EmployeeResponse employee1;
    private static EmployeeResponse employee2;

    private static TicketResponse ticket1;
    private static TicketResponse ticket2;

    private static List<EmployeeResponse> employeeList = new ArrayList<>();
    private static List<RoleResponse> roleList = new ArrayList<>();
    private static List<TicketResponse> ticketList = new ArrayList<>();
//
    private static EmployeeRequest employeeRequest1;
    private static RoleRequest roleRequest1;

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

        role1 = new RoleResponse(
                2,
                "role1"
        );

        role2 = new RoleResponse(
                3,
                "role2"
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

        ticket2 = new TicketResponse(
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

        employeeList = new ArrayList<>();
        roleList = new ArrayList<>();
        ticketList = new ArrayList<>();

        employeeList.add(employee1);
        employeeList.add(employee2);
        roleList.add(role1);
        roleList.add(role2);
        ticketList.add(ticket1);
        ticketList.add(ticket2);

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
        when(employeeService.getEmployees()).thenReturn(employeeList);
        mockMvc.perform(get("/admin/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("[0].name").value("name1"))
                .andExpect(jsonPath("[1].name").value("name2"));
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
        int employeeId = 99;
        when(employeeService.getEmployee(employeeId))
                .thenThrow(new EmployeeNotFoundException("Employee with ID " + employeeId + " not found!"));

        mockMvc.perform(get("/admin/employees/{employeeId}", employeeId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Employee with ID " + employeeId + " not found!"));
    }

    @Test
    void testGetValidEmployeeAdmin() throws Exception {
        when(employeeService.getEmployee(1)).thenReturn(TestDataFactory.admin());
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
        int employeeId = 1;
        when(employeeService.updateEmployee(employeeId, employeeRequest1)).thenReturn(employee1);
        mockMvc.perform(
                patch("/admin/employees/{id}", employeeId)
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
        int employeeId = 1;
        doNothing().when(employeeService).deleteEmployee(employeeId);
        mockMvc.perform(
                delete("/admin/employees/{id}", employeeId))
                .andExpect(status().isOk());
        verify(employeeService, times(1)).deleteEmployee(employeeId);
    }

    @Test
    void testGetAllValidRoles() throws Exception {
        when(roleService.getRoles()).thenReturn(roleList);
        mockMvc.perform(get("/admin/roles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("[0].roleName").value("role1"))
                .andExpect(jsonPath("[1].roleName").value("role2"));
    }

    @Test
    void testGetValidRole() throws Exception {
        int roleId = 2;
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
        int roleId = 2;
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
        int roleId = 2;
        doNothing().when(roleService).deleteRole(roleId);
        mockMvc.perform(
                delete("/admin/roles/{id}", roleId))
                .andExpect(status().isOk());
        verify(roleService, times(1)).deleteRole(roleId);
    }

    @Test
    void testGetAllValidTickets() throws Exception {
        when(ticketService.getTickets(1)).thenReturn(ticketList);
        mockMvc.perform(get("/admin/tickets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("[0].title").value("Test Ticket 1"))
                .andExpect(jsonPath("[1].title").value("Test Ticket 2"));
    }

    @Test
    void testGetValidTicket() throws Exception {
        int ticketNumber = 1;
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
        int ticketId = 1;
        when(ticketService.updateTicket(1, ticketId, ticketLocalRequest)).thenReturn(ticket1);
        mockMvc.perform(patch("/admin/tickets/{ticketId}", ticketId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ticketLocalRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("assigneeName").value("name1"));
    }

    @Test
    void testUpdateTicketStatusAndRemarks() throws Exception {
        TicketRequest ticketLocalRequest = new TicketRequest(
                null,
                null,
                null,
                TicketStatus.DUPLICATE,
                "Test Remarks 2"
        );
        int ticketId = 2;
        when(ticketService.updateTicket(1, ticketId, ticketLocalRequest)).thenReturn(ticket2);
        mockMvc.perform(patch("/admin/tickets/{ticketId}", ticketId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ticketLocalRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(TicketStatus.DUPLICATE.name()))
                .andExpect(jsonPath("remarks").value("Test remarks 2"));
    }
}
