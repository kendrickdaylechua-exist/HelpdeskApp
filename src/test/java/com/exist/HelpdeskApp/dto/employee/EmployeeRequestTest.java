package com.exist.HelpdeskApp.dto.employee;

import com.exist.HelpdeskApp.model.EmploymentStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.*;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class EmployeeRequestTest {

    private Validator validator;

    @BeforeEach
    void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testAllValidRequests() {
        EmployeeRequest request = new EmployeeRequest(
                "name1",
                25,
                "test address 1",
                "09~~~~~~~~~~",
                EmploymentStatus.FULL_TIME,
                1
        );
        Set<ConstraintViolation<EmployeeRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    void testNameIsBlankAndWhitespace(String tests) {
        EmployeeRequest request = new EmployeeRequest(
                tests,
                25,
                "test address 1",
                "09~~~~~~~~~~",
                EmploymentStatus.FULL_TIME,
                1
        );
        Set<ConstraintViolation<EmployeeRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertNotNull(violations, "There should be a violation for the name field");
        assertEquals("Name must not be blank", violations.iterator().next().getMessage());
    }

    @ParameterizedTest
    @NullSource
    void testNameIsNull(String test) {
        EmployeeRequest request = new EmployeeRequest(
                test,
                25,
                "test address 1",
                "09~~~~~~~~~~",
                EmploymentStatus.FULL_TIME,
                1
        );
        Set<ConstraintViolation<EmployeeRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertNotNull(violations, "There should be a violation for the name field");
        assertEquals("Name must not be blank", violations.iterator().next().getMessage());
    }

    @ParameterizedTest
    @ValueSource(ints = {16, 25, 50, 130})
    void testValidAge(int tests) {
        EmployeeRequest request = new EmployeeRequest(
                "name1",
                tests,
                "test address 1",
                "09~~~~~~~~~~",
                EmploymentStatus.FULL_TIME,
                1
        );
        Set<ConstraintViolation<EmployeeRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(ints = {-5, 0, 1, 10, 15})
    void testBelowAge(int tests) {
        EmployeeRequest request = new EmployeeRequest(
                "name1",
                tests,
                "test address 1",
                "09~~~~~~~~~~",
                EmploymentStatus.FULL_TIME,
                1
        );
        Set<ConstraintViolation<EmployeeRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertNotNull(violations);
        assertEquals("Age must be at least 16 to be able to work" ,violations.iterator().next().getMessage());
    }

    @ParameterizedTest
    @ValueSource(ints = {131, 150, 200, 99999})
    void testAboveAge(int tests) {
        EmployeeRequest request = new EmployeeRequest(
                "name1",
                tests,
                "test address 1",
                "09~~~~~~~~~~",
                EmploymentStatus.FULL_TIME,
                1
        );
        Set<ConstraintViolation<EmployeeRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertNotNull(violations);
        assertEquals("A person cannot be that old! That's impossible!" ,violations.iterator().next().getMessage());
    }

    @ParameterizedTest
    @NullSource()
    void testNullAge(int test) {
        EmployeeRequest request = new EmployeeRequest(
                "name1",
                test,
                "test address 1",
                "09~~~~~~~~~~",
                EmploymentStatus.FULL_TIME,
                1
        );
        Set<ConstraintViolation<EmployeeRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertNotNull(violations);
        assertEquals("Age is required" ,violations.iterator().next().getMessage());
    }
}
