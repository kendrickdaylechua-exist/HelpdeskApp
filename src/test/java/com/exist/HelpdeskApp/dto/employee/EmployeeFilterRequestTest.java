package com.exist.HelpdeskApp.dto.employee;

import com.exist.HelpdeskApp.model.Employee;
import com.exist.HelpdeskApp.model.EmploymentStatus;
import com.exist.HelpdeskApp.model.Role;
import com.exist.HelpdeskApp.model.embeddable.Address;
import com.exist.HelpdeskApp.model.embeddable.Contacts;
import com.exist.HelpdeskApp.model.embeddable.Name;
import com.exist.HelpdeskApp.repository.EmployeeRepository;
import com.exist.HelpdeskApp.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.TestPropertySource;

import javax.validation.*;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestPropertySource(properties = {
        "spring.liquibase.enabled=false"
})
public class EmployeeFilterRequestTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private RoleRepository roleRepository;

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    private Role role;


    @BeforeEach
    void setup() {
        employeeRepository.deleteAll();

        Name name1 = new Name("First1", "Middle1", "Last1");
        Contacts contacts1 = new Contacts("0912345678", "sample@example.com", "021234567");
        Address address1 = new Address("123 Test St.", "Manila", "Region 1", "Philippines");

        role = new Role();
        role.setRoleName("ADMIN");
        role = roleRepository.save(role);

        Employee e1 = new Employee();
        e1.setDeleted(false);
        e1.setName(name1);
        e1.setAddress(address1);
        e1.setContacts(contacts1);
        e1.setEmploymentStatus(EmploymentStatus.FULL_TIME);
        e1.setRole(role);
        employeeRepository.save(e1);
    }

    @Test
    void testValidRequest() {
        EmployeeFilterRequest request = new EmployeeFilterRequest();
        request.setPage(0);
        request.setSize(1);
        request.setSortBy("id");
        request.setSortDir("desc");
        request.setName("First1");
        request.setAddress("Address1");
        request.setStatus("full");
        request.setRoleId(1);
        request.setRoleName("test");
        request.setDeleted(false);
        Set<ConstraintViolation<EmployeeFilterRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testInvalidPage() {
        EmployeeFilterRequest request = new EmployeeFilterRequest();
        request.setPage(-1);
        Set<ConstraintViolation<EmployeeFilterRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
    }

    @Test
    void testInvalidSize() {
        EmployeeFilterRequest request = new EmployeeFilterRequest();
        request.setSize(0);
        Set<ConstraintViolation<EmployeeFilterRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
    }

    @Test
    void testInvalidSortDir() {
        EmployeeFilterRequest request = new EmployeeFilterRequest();
        request.setSortDir("ascending");
        Set<ConstraintViolation<EmployeeFilterRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
    }

    @Test
    void testFilterByName() {
        EmployeeFilterRequest filter = new EmployeeFilterRequest();
        filter.setDeleted(false);
        filter.setName("first");

        Specification<Employee> spec = filter.toSpec();
        List<Employee> result = employeeRepository.findAll(spec);

        assertEquals(1, result.size());
        assertEquals("First1", result.get(0).getName().getFirstName());
    }
    @Test
    void testFilterByStatus() {
        EmployeeFilterRequest filter = new EmployeeFilterRequest();
        filter.setDeleted(false);
        filter.setStatus("full");

        Specification<Employee> spec = filter.toSpec();
        List<Employee> result = employeeRepository.findAll(spec);

        assertEquals(1, result.size());
        assertEquals(EmploymentStatus.FULL_TIME, result.get(0).getEmploymentStatus());
    }

    @Test
    void testFilterByAddress() {
        EmployeeFilterRequest filter = new EmployeeFilterRequest();
        filter.setDeleted(false);
        filter.setAddress("manila");

        Specification<Employee> spec = filter.toSpec();
        List<Employee> result = employeeRepository.findAll(spec);

        assertEquals(1, result.size());
        assertEquals(EmploymentStatus.FULL_TIME, result.get(0).getEmploymentStatus());
    }

    @Test
    void testFilterByContacts() {
        EmployeeFilterRequest filter = new EmployeeFilterRequest();
        filter.setDeleted(false);
        filter.setContacts("sample");

        Specification<Employee> spec = filter.toSpec();
        List<Employee> result = employeeRepository.findAll(spec);

        assertEquals(1, result.size());
        assertEquals(EmploymentStatus.FULL_TIME, result.get(0).getEmploymentStatus());
    }

}
