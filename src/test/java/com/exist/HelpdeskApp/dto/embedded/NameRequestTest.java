package com.exist.HelpdeskApp.dto.embedded;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class NameRequestTest {
    private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void testValidRequests() {
        NameRequest request = new NameRequest(
                "First1",
                "Middle1",
                "Last1"
        );

        Set<ConstraintViolation<NameRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty());
        assertEquals("First1", request.getFirstName());
        assertEquals("Middle1", request.getMiddleName());
        assertEquals("Last1", request.getLastName());
    }

    @ParameterizedTest
    @NullSource
    void testNullFirstName(String test) {
        NameRequest request = new NameRequest(
                test,
                "Middle1",
                "Last1"
        );

        Set<ConstraintViolation<NameRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertEquals("First name cannot be blank", violations.iterator().next().getMessage());
    }

    @ParameterizedTest
    @ValueSource(ints = {31, 999})
    void testInvalidFirstName(int stringLength) {
        String test = "1".repeat(stringLength);

        NameRequest request = new NameRequest(
                test,
                "Middle1",
                "Last1"
        );

        Set<ConstraintViolation<NameRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertEquals("First name cannot exceeds 30 characters", violations.iterator().next().getMessage());
    }

    @ParameterizedTest
    @NullSource
    void testNullMiddleName(String test) {
        NameRequest request = new NameRequest(
                "First1",
                test,
                "Last1"
        );

        Set<ConstraintViolation<NameRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertEquals("Middle name cannot be blank", violations.iterator().next().getMessage());
    }

    @ParameterizedTest
    @ValueSource(ints = {31, 999})
    void testInvalidMiddleName(int stringLength) {
        String test = "1".repeat(stringLength);

        NameRequest request = new NameRequest(
                "First1",
                test,
                "Last1"
        );

        Set<ConstraintViolation<NameRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertEquals("Middle name cannot exceeds 30 characters", violations.iterator().next().getMessage());
    }

    @ParameterizedTest
    @NullSource
    void testNullLastName(String test) {
        NameRequest request = new NameRequest(
                "First1",
                "Middle1",
                test
        );

        Set<ConstraintViolation<NameRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertEquals("Last name cannot be blank", violations.iterator().next().getMessage());
    }

    @ParameterizedTest
    @ValueSource(ints = {31, 999})
    void testInvalidLastName(int stringLength) {
        String test = "1".repeat(stringLength);

        NameRequest request = new NameRequest(
                "First1",
                "Middle1",
                test
        );

        Set<ConstraintViolation<NameRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertEquals("Last name cannot exceeds 30 characters", violations.iterator().next().getMessage());
    }
}
