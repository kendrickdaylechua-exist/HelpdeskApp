package com.exist.HelpdeskApp.model;

import com.exist.HelpdeskApp.repository.RoleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
public class RoleTest {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    void testSaveAndRetrieveRole() {
        Role role = new Role();
        role.setRoleName("Test Role");

        Role saved = roleRepository.saveAndFlush(role);
        Role retrieved = roleRepository.findById(saved.getId()).orElseThrow();

        assertEquals("Test Role", retrieved.getRoleName());
    }

    @Test
    void testNotNullConstraints() {
        Role role = new Role();
        assertThrows(Exception.class, () -> roleRepository.save(role));
    }
}
