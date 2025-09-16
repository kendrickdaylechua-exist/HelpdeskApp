package com.exist.HelpdeskApp.controller;

import com.exist.HelpdeskApp.TestDataFactory;
import com.exist.HelpdeskApp.dto.employee.EmployeeFilterRequest;
import com.exist.HelpdeskApp.dto.employee.EmployeeRequest;
import com.exist.HelpdeskApp.dto.employee.EmployeeResponse;
import com.exist.HelpdeskApp.dto.role.RoleFilterRequest;
import com.exist.HelpdeskApp.dto.role.RoleRequest;
import com.exist.HelpdeskApp.dto.role.RoleResponse;
import com.exist.HelpdeskApp.dto.ticket.TicketFilterRequest;
import com.exist.HelpdeskApp.dto.ticket.TicketRequest;
import com.exist.HelpdeskApp.dto.ticket.TicketResponse;
import com.exist.HelpdeskApp.exception.businessexceptions.EmployeeNotFoundException;
import com.exist.HelpdeskApp.model.EmploymentStatus;
import com.exist.HelpdeskApp.model.TicketStatus;
import com.exist.HelpdeskApp.model.embeddable.Address;
import com.exist.HelpdeskApp.model.embeddable.Contacts;
import com.exist.HelpdeskApp.model.embeddable.Name;
import com.exist.HelpdeskApp.service.Implementations.EmployeeServiceImpl;
import com.exist.HelpdeskApp.service.Implementations.RoleServiceImpl;
import com.exist.HelpdeskApp.service.Implementations.TicketServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.Page;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
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
    private EmployeeServiceImpl employeeServiceImpl;

    @MockBean
    private RoleServiceImpl roleServiceImpl;

    @MockBean
    private TicketServiceImpl ticketServiceImpl;

    private static RoleResponse role1;

    private static EmployeeResponse employee1;

    private static TicketResponse ticket1;

    private static EmployeeRequest employeeRequest1;
    private static RoleRequest roleRequest1;

    private final static Integer VALID_EMPLOYEE_ID_1 = 1;
    private final static Integer INVALID_EMPLOYEE_ID = 99;

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
                name1,
                25,
                address1,
                contacts1,
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
        Page<EmployeeResponse> page = new PageImpl<>(List.of(employee1));

        when(employeeServiceImpl.getEmployees(any(EmployeeFilterRequest.class))).thenReturn(page);
        mockMvc.perform(get("/admin/employees")
                        .param("page", "0")
                        .param("size", "5")
                        .param("sortBy", "id")
                        .param("sortDir", "asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(2));
    }

    @Test
    void testGetValidEmployee() throws Exception {
        when(employeeServiceImpl.getEmployee(2)).thenReturn(employee1);
        mockMvc.perform(get("/admin/employees/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name.firstName").value("First1"));
    }

    @Test
    void testGetNonExistentEmployee() throws Exception {
        when(employeeServiceImpl.getEmployee(INVALID_EMPLOYEE_ID))
                .thenThrow(new EmployeeNotFoundException("Employee with ID " + INVALID_EMPLOYEE_ID + " not found!"));

        mockMvc.perform(get("/admin/employees/{employeeId}", INVALID_EMPLOYEE_ID))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Employee with ID " + INVALID_EMPLOYEE_ID + " not found!"));
    }

    @Test
    void testGetValidEmployeeAdmin() throws Exception {
        when(employeeServiceImpl.getEmployee(1)).thenReturn(TestDataFactory.adminEmployeeRequest());
        mockMvc.perform(get("/admin/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name.firstName").value("Admin"));
    }

    @Test
    void testAddValidEmployee() throws Exception {
        when(employeeServiceImpl.addEmployee(employeeRequest1)).thenReturn(employee1);
        mockMvc.perform(post("/admin/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeRequest1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name.firstName").value("First1"));
    }

    @Test
    void testUpdateEmployee() throws Exception {
//        Name name2 = new Name("First2", "Middle2", "Last2");
//        Contacts contacts2 = new Contacts("0912345678", "sample@example.com", "021234567");
//        Address address2 = new Address("321 Test St.", "Los Angeles", "California","USA");
//        EmployeeResponse employee2 = new EmployeeResponse(
//                3,
//                name2,
//                25,
//                address2,
//                contacts2,
//                EmploymentStatus.FULL_TIME,
//                3,
//                "role2"
//        );
        when(employeeServiceImpl.updateEmployee(VALID_EMPLOYEE_ID_1, employeeRequest1)).thenReturn(employee1);
        mockMvc.perform(
                patch("/admin/employees/{id}", VALID_EMPLOYEE_ID_1)
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
                delete("/admin/employees/{id}", INVALID_EMPLOYEE_ID))
                .andExpect(status().isOk());
        verify(employeeServiceImpl, times(1)).deleteEmployee(INVALID_EMPLOYEE_ID);
    }

    @Test
    void testGetAllValidRoles() throws Exception {
        Page<RoleResponse> page = new PageImpl<>(List.of(role1));
        when(roleServiceImpl.getRoles(any(RoleFilterRequest.class))).thenReturn(page);
        mockMvc.perform(get("/admin/roles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath(".content[0].roleName").value("role1"));
    }

    @Test
    void testGetValidRole() throws Exception {
        Integer roleId = 2;
        when(roleServiceImpl.getRole(2)).thenReturn(role1);
        mockMvc.perform(get("/admin/roles/{roleId}", roleId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("roleName").value("role1"));
    }

    @Test
    void testAddValidRole() throws Exception {
        when(roleServiceImpl.addRole(roleRequest1)).thenReturn(role1);
        mockMvc.perform(post("/admin/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(roleRequest1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("roleName").value("role1"));
    }

    @Test
    void testUpdateValidRole() throws Exception {
        Integer roleId = 2;
        when(roleServiceImpl.updateRole(roleId, roleRequest1)).thenReturn(role1);
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
        doNothing().when(roleServiceImpl).deleteRole(roleId);
        mockMvc.perform(
                delete("/admin/roles/{id}", roleId))
                .andExpect(status().isOk());
        verify(roleServiceImpl, times(1)).deleteRole(roleId);
    }

    @Test
    void testGetAllValidTickets() throws Exception {
        Page<TicketResponse> page = new PageImpl<>(List.of(ticket1));
        when(ticketServiceImpl.getTickets(eq(1), any(TicketFilterRequest.class))).thenReturn(page);
        mockMvc.perform(get("/admin/tickets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath(".content[0].title").value("Test Ticket 1"));
    }

    @Test
    void testGetValidTicket() throws Exception {
        Integer ticketNumber = 1;
        when(ticketServiceImpl.getTicket(1, ticketNumber)).thenReturn(ticket1);
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
        when(ticketServiceImpl.updateTicket(1, ticketId, ticketLocalRequest)).thenReturn(ticket1);
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
        when(ticketServiceImpl.updateTicket(1, ticketId, ticketLocalRequest)).thenReturn(ticket2);
        mockMvc.perform(patch("/admin/tickets/{ticketId}", ticketId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ticketLocalRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(TicketStatus.DUPLICATE.name()))
                .andExpect(jsonPath("remarks").value("Test remarks 2"));
    }
}
