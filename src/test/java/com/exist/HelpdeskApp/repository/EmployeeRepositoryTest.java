package com.exist.HelpdeskApp.repository;

import com.exist.HelpdeskApp.model.EmployeeProfile;
import com.exist.HelpdeskApp.model.Role;
import com.exist.HelpdeskApp.model.embeddable.Address;
import com.exist.HelpdeskApp.model.embeddable.Contacts;
import com.exist.HelpdeskApp.model.embeddable.Name;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@TestPropertySource(properties = {
        "spring.liquibase.enabled=false"
})
public class EmployeeProfileRepositoryTest {

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    RoleRepository roleRepository;

    private EmployeeProfile employeeProfile;
    private Role role;

    @BeforeEach
    void setup() {
        role = new Role();
        role.setRoleName("Developer");
        role = roleRepository.save(role);

        Name name1 = new Name("First1", "Middle1", "Last1");
        Contacts contacts1 = new Contacts("0912345678", "sample@example.com", "021234567");
        Address address1 = new Address("123 Test St.", "Manila", "Region 1", "Philippines");

        employeeProfile = new EmployeeProfile();
        employeeProfile.setName(name1);
        employeeProfile.setAge(30);
        employeeProfile.setAddress(address1);
        employeeProfile.setContacts(contacts1);
        employeeProfile.setRole(role);
    }

    @Test
    void testSaveAndFindId() {
        employeeProfile = employeeRepository.saveAndFlush(employeeProfile);

        Optional<EmployeeProfile> found = employeeRepository.findById(employeeProfile.getId());

        assertTrue(found.isPresent());
        assertEquals("First1", found.get().getName().getFirstName());
    }

    @Test
    void testFindByIdAndDeletedFalse() {
        employeeProfile = employeeRepository.saveAndFlush(employeeProfile);
        Optional<EmployeeProfile> found = employeeRepository.findByIdAndDeletedFalse(employeeProfile.getId());

        assertTrue(found.isPresent());
        assertEquals("First1", found.get().getName().getFirstName());
    }

    @Test
    void testExistsByRole() {
        employeeRepository.saveAndFlush(employeeProfile);

        boolean exists = employeeRepository.existsByRole(role);

        assertTrue(exists);
    }

}
