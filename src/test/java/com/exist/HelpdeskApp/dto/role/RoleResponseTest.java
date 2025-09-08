package com.exist.HelpdeskApp.dto.role;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RoleResponseTest {

    @Test
    void testGetterAndSetter() {
        RoleResponse response = new RoleResponse();
        response.setId(1);
        response.setRoleName("Test Role");

        assertEquals(1, response.getId());
        assertEquals("Test Role", response.getRoleName());
    }

    @Test
    void testAllArgsConstructor() {
        RoleResponse response = new RoleResponse(1, "Test Role");

        assertEquals(1, response.getId());
        assertEquals("Test Role", response.getRoleName());
    }
}
