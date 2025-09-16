//package com.exist.HelpdeskApp.repository;
//
//import com.exist.HelpdeskApp.model.Employee;
//import com.exist.HelpdeskApp.model.Role;
//import com.exist.HelpdeskApp.model.Ticket;
//import com.exist.HelpdeskApp.model.TicketStatus;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//
//import javax.persistence.criteria.CriteriaBuilder;
//import java.time.Instant;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//@DataJpaTest
//public class TicketRepositoryTest {
//    @Autowired
//    TicketRepository ticketRepository;
//
//    @Autowired
//    EmployeeRepository employeeRepository;
//
//    @Autowired
//    RoleRepository roleRepository;
//
//    @Test
//    void testSaveAndFindId() {
//        Role role = new Role();
//        role.setRoleName("Sample Role");
//        role = roleRepository.save(role);
//
//        Employee employee = new Employee();
//        employee.setName("Sample Name");
//        employee.setAddress("Test address");
//        employee.setAge(30);
//        employee.setContactNumber("09~~~~~~~~~");
//        employee.setRole(role);
//        employee = employeeRepository.save(employee);
//
//        Ticket ticket = new Ticket();
//        ticket.setTitle("Sample Ticket");
//        ticket.setBody("Sample body");
//        ticket.setStatus(TicketStatus.FILED);
//        ticket.setUpdatedDate(Instant.now());
//        ticket.setCreateDate(Instant.now());
//        ticket.setAssignee(employee);
//        ticket.setUpdatedBy(employee);
//        ticket.setCreatedBy(employee);
//        ticket.setRemarks("Sample remarks");
//
//        ticket = ticketRepository.save(ticket);
//
//        Optional<Ticket> found = ticketRepository.findById(ticket.getTicketNumber());
//        assertTrue(found.isPresent());
//        assertEquals("Sample Ticket", found.get().getTitle());
//    }
//
//    @Test
//    void testDelete() {
//        Role role = new Role();
//        role.setRoleName("Sample Role");
//        role = roleRepository.save(role);
//
//        Employee employee = new Employee();
//        employee.setName("Sample Name");
//        employee.setAddress("Test address");
//        employee.setAge(30);
//        employee.setContactNumber("09~~~~~~~~~");
//        employee.setRole(role);
//        employee = employeeRepository.save(employee);
//
//        Ticket ticket = new Ticket();
//        ticket.setTitle("Sample Ticket");
//        ticket.setBody("Sample body");
//        ticket.setStatus(TicketStatus.FILED);
//        ticket.setUpdatedDate(Instant.now());
//        ticket.setCreateDate(Instant.now());
//        ticket.setAssignee(employee);
//        ticket.setUpdatedBy(employee);
//        ticket.setCreatedBy(employee);
//        ticket.setRemarks("Sample remarks");
//
//        ticket = ticketRepository.save(ticket);
//
//        ticketRepository.delete(ticket);
//
//        assertTrue(ticketRepository.findById(ticket.getTicketNumber()).isEmpty());
//    }
//}
