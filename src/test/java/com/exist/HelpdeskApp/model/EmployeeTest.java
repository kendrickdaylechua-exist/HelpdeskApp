package com.exist.HelpdeskApp.model;

import com.exist.HelpdeskApp.model.embeddable.Address;
import com.exist.HelpdeskApp.model.embeddable.Contacts;
import com.exist.HelpdeskApp.model.embeddable.Name;
import com.exist.HelpdeskApp.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@TestPropertySource(properties = {
        "spring.liquibase.enabled=false"
})
class EmployeeTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    void testSaveAndRetrieveEmployee() {
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

        Employee savedEmployee = employeeRepository.saveAndFlush(employee);

        Employee retrieved = employeeRepository.findById(savedEmployee.getId()).orElseThrow();

        assertEquals("First1", retrieved.getName().getFirstName());
        assertEquals(30, retrieved.getAge());
        assertEquals("Philippines", retrieved.getAddress().getCountry());
        assertEquals("0912345678", retrieved.getContacts().getPhoneNumber());
        assertEquals(EmploymentStatus.FULL_TIME, retrieved.getEmploymentStatus());
        assertEquals("Test Role", retrieved.getRole().getRoleName());
    }
}
