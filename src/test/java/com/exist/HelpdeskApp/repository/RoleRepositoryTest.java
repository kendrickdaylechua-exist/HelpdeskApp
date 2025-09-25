package com.exist.HelpdeskApp.repository;

import com.exist.HelpdeskApp.model.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestPropertySource(properties = {
        "spring.liquibase.enabled=false"
})
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
    void testFindByIdAndDeletedFalse() {
        Role role = new Role();
        role.setRoleName("Sample Role");
        role.setDeleted(false);
        role = roleRepository.save(role);

        Optional<Role> result = roleRepository.findByIdAndDeletedFalse(role.getId());

        assertTrue(result.isPresent());
        assertEquals("Sample Role", result.get().getRoleName());
        assertFalse(result.get().isDeleted());
    }


}
