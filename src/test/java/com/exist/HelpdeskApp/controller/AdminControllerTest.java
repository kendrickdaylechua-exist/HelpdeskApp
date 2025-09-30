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






}
