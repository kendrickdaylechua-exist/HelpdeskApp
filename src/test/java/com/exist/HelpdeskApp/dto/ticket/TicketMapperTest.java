//package com.exist.HelpdeskApp.dto.ticket;
//
//
//import com.exist.HelpdeskApp.model.*;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mapstruct.factory.Mappers;
//
//import java.time.Instant;
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//public class TicketMapperTest {
//    private TicketMapper mapper;
//
//    @BeforeEach
//    void setup() {
//        mapper = Mappers.getMapper(TicketMapper.class);
//    }
//
//    @Test
//    void testToEntity() {
//        TicketRequest request = new TicketRequest(
//                "Ticket 1",
//                "Body of the ticket",
//                1,
//                TicketStatus.FILED,
//                "Remarks of the ticket"
//        );
//
//        Ticket ticket = mapper.toEntity(request);
//
//        assertEquals("Ticket 1", ticket.getTitle());
//        assertEquals("Body of the ticket", ticket.getBody());
//        assertEquals(TicketStatus.FILED, ticket.getStatus());
//        assertEquals("Remarks of the ticket", ticket.getRemarks());
//    }
//
//    @Test
//    void testToResponse() {
//        Role role = new Role (1, "Role Name", false, 1);
//        Employee employee = new Employee(1, "Employee Name", 25, "Address of the employee", "0912345678", EmploymentStatus.FULL_TIME, role, false, 1);
//        Ticket entity = new Ticket(1, "Ticket 1", "Body of the ticket", employee, TicketStatus.FILED, Instant.now(), employee, Instant.now(), employee, "Remarks of the ticket", false, 1);
//
//        TicketResponse response = mapper.toResponse(entity);
//
//        assertEquals("Ticket 1", response.getTitle());
//        assertEquals("Body of the ticket", response.getBody());
//        assertEquals(TicketStatus.FILED, response.getStatus());
//        assertEquals("Remarks of the ticket", response.getRemarks());
//    }
//
//    @Test
//    void testToResponseList() {
//        Role role = new Role (1, "Role Name", false, 1);
//        Employee employee = new Employee(1, "Employee Name", 25, "Address of the employee", "0912345678", EmploymentStatus.FULL_TIME, role, false, 1);
//        Ticket entity = new Ticket(1, "Ticket 1", "Body of the ticket", employee, TicketStatus.FILED, Instant.now(), employee, Instant.now(), employee, "Remarks of the body", false, 1);
//
//
//        List<Ticket> ticketList = new ArrayList<>();
//        ticketList.add(entity);
//        List<TicketResponse> result = mapper.toResponseList(ticketList);
//        assertEquals(1, result.size());
//        assertEquals("Ticket 1", result.get(0).getTitle());
//    }
//
//    @Test
//    void testToUpdate() {
//        TicketRequest request = new TicketRequest(
//                "Ticket 1",
//                "Body of the ticket",
//                1,
//                TicketStatus.FILED,
//                "Remarks of the ticket"
//        );
//        Role role = new Role (1, "Role Name", false, 1);
//        Employee employee = new Employee(1, "Employee Name", 25, "Address of the employee", "0912345678", EmploymentStatus.FULL_TIME, role, false, 1);
//        Ticket entity = new Ticket(1, "Ticket 1", "Body of the ticket", employee, TicketStatus.FILED, Instant.now(), employee, Instant.now(), employee, "Remarks of the ticket", false, 1);
//
//        mapper.toUpdate(request, entity);
//
//        assertEquals("Ticket 1", request.getTitle());
//    }
//
//}
