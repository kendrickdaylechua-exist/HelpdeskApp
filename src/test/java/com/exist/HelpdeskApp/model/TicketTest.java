package com.exist.HelpdeskApp.model;

import com.exist.HelpdeskApp.model.embeddable.Address;
import com.exist.HelpdeskApp.model.embeddable.Contacts;
import com.exist.HelpdeskApp.model.embeddable.Name;
import com.exist.HelpdeskApp.repository.TicketRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@TestPropertySource(properties = {
        "spring.liquibase.enabled=false"
})
public class TicketTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TicketRepository ticketRepository;

    @Test
    void saveAndRetrieveTicket() {
        Name name1 = new Name("First1", "Middle1", "Last1");
        Contacts contacts1 = new Contacts("0912345678", "sample@example.com", "021234567");
        Address address1 = new Address("123 Test St.", "Manila", "Region 1", "Philippines");

        Role role = new Role();
        role.setRoleName("Test Role");
        Role savedRole = entityManager.persistAndFlush(role);
        Employee employee = new Employee();
        employee.setName(name1);
        employee.setAge(30);
        employee.setAddress(address1);
        employee.setContacts(contacts1);
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
        assertEquals("First1", retrieve.getAssignee().getName().getFirstName());
        assertEquals(TicketStatus.FILED, retrieve.getStatus());
        assertEquals("First1", retrieve.getCreatedBy().getName().getFirstName());
        assertEquals("First1", retrieve.getUpdatedBy().getName().getFirstName());
        assertEquals("Remarks of the ticket", retrieve.getRemarks());
    }

    @Test
    void testNotNullConstraints() {
        Ticket ticket = new Ticket();
        assertThrows(Exception.class, () -> ticketRepository.save(ticket));
    }
}
