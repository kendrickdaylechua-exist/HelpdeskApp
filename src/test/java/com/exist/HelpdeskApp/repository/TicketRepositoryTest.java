package com.exist.HelpdeskApp.repository;

import com.exist.HelpdeskApp.model.Employee;
import com.exist.HelpdeskApp.model.Role;
import com.exist.HelpdeskApp.model.Ticket;
import com.exist.HelpdeskApp.model.TicketStatus;
import com.exist.HelpdeskApp.model.embeddable.Address;
import com.exist.HelpdeskApp.model.embeddable.Contacts;
import com.exist.HelpdeskApp.model.embeddable.Name;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@TestPropertySource(properties = {
        "spring.liquibase.enabled=false"
})
public class TicketRepositoryTest {
    @Autowired
    TicketRepository ticketRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    RoleRepository roleRepository;

    private Ticket ticket;
    private Employee employee;

    @BeforeEach
    void setup() {
        Role role = new Role();
        role.setRoleName("Sample Role");
        role = roleRepository.saveAndFlush(role);

        Name name1 = new Name("First1", "Middle1", "Last1");
        Contacts contacts1 = new Contacts("0912345678", "sample@example.com", "021234567");
        Address address1 = new Address("123 Test St.", "Manila", "Region 1", "Philippines");

        employee = new Employee();
        employee.setName(name1);
        employee.setAddress(address1);
        employee.setAge(30);
        employee.setContacts(contacts1);
        employee.setRole(role);
        employee.setDeleted(false);
        employee = employeeRepository.saveAndFlush(employee);

        ticket = new Ticket();
        ticket.setTicketNumber(1);
        ticket.setTitle("Sample Ticket");
        ticket.setBody("Sample body");
        ticket.setStatus(TicketStatus.FILED);
        ticket.setUpdatedDate(Instant.now());
        ticket.setCreateDate(Instant.now());
        ticket.setAssignee(employee);
        ticket.setUpdatedBy(employee);
        ticket.setCreatedBy(employee);
        ticket.setRemarks("Sample remarks");
        ticket.setDeleted(false);
    }

    @Test
    void testFindByAssigneeIdAndDeletedFalse() {
        ticketRepository.saveAndFlush(ticket);

        List<Ticket> results = ticketRepository.findByAssigneeIdAndDeletedFalse(employee.getId());

        assertEquals(1, results.size());
    }

    @Test
    void testFindByTicketNumberAndDeletedFalse() {
        ticketRepository.saveAndFlush(ticket);

        Integer generatedTicketNumber = ticket.getTicketNumber();

        Optional<Ticket> result = ticketRepository.findByTicketNumberAndDeletedFalse(generatedTicketNumber);

        assertTrue(result.isPresent());
    }

    @Test
    void testExistsByAssignee() {
        ticketRepository.saveAndFlush(ticket);

        boolean exists = ticketRepository.existsByAssignee(employee);

        assertTrue(exists);
    }

    @Test
    void testSaveAndFindId() {
        ticket = ticketRepository.saveAndFlush(ticket);

        Optional<Ticket> found = ticketRepository.findById(ticket.getTicketNumber());
        assertTrue(found.isPresent());
        assertEquals("Sample Ticket", found.get().getTitle());
    }


}
