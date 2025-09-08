package com.exist.HelpdeskApp.model;

import com.exist.HelpdeskApp.repository.TicketRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
public class TicketTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TicketRepository ticketRepository;

    @Test
    void saveAndRetrieveTicket() {
        Role role = new Role();
        role.setRoleName("Test Role");
        Role savedRole = entityManager.persistAndFlush(role);
        Employee employee = new Employee();
        employee.setName("Test Name");
        employee.setAge(30);
        employee.setAddress("Test Address");
        employee.setContactNumber("09~~~~~~~~~~");
        employee.setEmploymentStatus(EmploymentStatus.FULL_TIME);
        employee.setRole(savedRole);
        Employee savedEmployee = entityManager.persistAndFlush(employee);

        Ticket ticket = new Ticket();
        ticket.setTitle("Ticket 1");
        ticket.setBody("Body of the ticket");
        ticket.setAssignee(savedEmployee);
        ticket.setStatus(TicketStatus.FILED);
        ticket.setCreateDate(Instant.now());
        ticket.setCreatedBy(savedEmployee);
        ticket.setUpdatedDate(Instant.now());
        ticket.setUpdatedBy(savedEmployee);
        ticket.setRemarks("Remarks of the ticket");

        Ticket saved = ticketRepository.saveAndFlush(ticket);
        Ticket retrieve = ticketRepository.findById(saved.getTicketNumber()).orElseThrow();

        assertEquals("Ticket 1", retrieve.getTitle());
        assertEquals("Body of the ticket", retrieve.getBody());
        assertEquals("Test Name", retrieve.getAssignee().getName());
        assertEquals(TicketStatus.FILED, retrieve.getStatus());
        assertEquals("Test Name", retrieve.getCreatedBy().getName());
        assertEquals("Test Name", retrieve.getUpdatedBy().getName());
        assertEquals("Remarks of the ticket", retrieve.getRemarks());
    }

    @Test
    void testNotNullConstraints() {
        Ticket ticket = new Ticket();
        assertThrows(Exception.class, () -> ticketRepository.save(ticket));
    }
}
