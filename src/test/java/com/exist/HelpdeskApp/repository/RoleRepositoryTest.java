package com.exist.HelpdeskApp.repository;

import com.exist.HelpdeskApp.model.Employee;
import com.exist.HelpdeskApp.model.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class RoleRepositoryTest {
    @Autowired
    RoleRepository roleRepository;

    @Test
    void testSaveAndFindId() {
        Role role = new Role();
        role.setRoleName("Sample Role");

        role = roleRepository.save(role);

        Optional<Role> found = roleRepository.findById(role.getId());
        assertTrue(found.isPresent());
        assertEquals("Sample Role", found.get().getRoleName());
    }

    @Test
    void testDelete() {
        Role role = new Role();
        role.setRoleName("Sample Role");
        role = roleRepository.save(role);

        roleRepository.delete(role);

        assertTrue(roleRepository.findById(role.getId()).isEmpty());
    }
}
