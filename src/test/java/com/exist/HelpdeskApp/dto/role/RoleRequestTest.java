package com.exist.HelpdeskApp.dto.role;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RoleRequestTest {
    private Validator validator;

    @BeforeEach
    void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidRequest() {
        RoleRequest request = new RoleRequest("Role Name");
        Set<ConstraintViolation<RoleRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testNullRequest() {
        RoleRequest request = new RoleRequest();
        Set<ConstraintViolation<RoleRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertNotNull(violations);
        assertEquals("Role name must not be blank", violations.iterator().next().getMessage());
    }
}
