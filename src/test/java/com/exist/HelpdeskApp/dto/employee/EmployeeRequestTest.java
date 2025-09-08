package com.exist.HelpdeskApp.dto.employee;

import com.exist.HelpdeskApp.model.EmploymentStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import javax.validation.*;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class EmployeeRequestTest {

    private Validator validator;

    @BeforeEach
    void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @ParameterizedTest
    @CsvSource({
            "name1, 30, test address 1, 09123456789, FULL_TIME",
            "name2, 25, test address 2, 09123456789, PART_TIME",
            "name3, 35, test address 3, 09123456789, CONTRACT",
            "name4, 45, test address 4, 09123456789, INTERN",
    })
    void testAllValidRequests(String name, int age, String address, String contactNumber, EmploymentStatus employmentStatus) {
        EmployeeRequest request = new EmployeeRequest(
                name,
                age,
                address,
                contactNumber,
                employmentStatus,
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
        assertNotNull(violations);
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
        assertNotNull(violations);
        assertEquals("Name must not be blank", violations.iterator().next().getMessage());
    }

    @ParameterizedTest
    @ValueSource(ints = {16, 25, 50, 130})
    void testValidAge(Integer tests) {
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
    void testBelowAge(Integer tests) {
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
    void testAboveAge(Integer tests) {
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
    void testNullAge(Integer test) {
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

    @Test
    void testNullAddress() {
        EmployeeRequest request = new EmployeeRequest(
                "name1",
                25,
                null,
                "09~~~~~~~~~~",
                EmploymentStatus.FULL_TIME,
                1
        );
        Set<ConstraintViolation<EmployeeRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertNotNull(violations);
        assertEquals("Address is required" ,violations.iterator().next().getMessage());
    }

    @Test
    void testNullContactNumber() {
        EmployeeRequest request = new EmployeeRequest(
                "name1",
                25,
                "test address 1",
                null,
                EmploymentStatus.FULL_TIME,
                1
        );
        Set<ConstraintViolation<EmployeeRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertNotNull(violations);
        assertEquals("Contact number is required" ,violations.iterator().next().getMessage());
    }

    @Test
    void testNullEmploymentStatus() {
        EmployeeRequest request = new EmployeeRequest(
                "name1",
                25,
                "test address 1",
                "09~~~~~~~~~~",
                null,
                1
        );
        Set<ConstraintViolation<EmployeeRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertNotNull(violations);
        assertEquals("Employment status is required" ,violations.iterator().next().getMessage());
    }

    @Test
    void testNullRole() {
        EmployeeRequest request = new EmployeeRequest(
                "name1",
                25,
                "test address 1",
                "09~~~~~~~~~~",
                EmploymentStatus.FULL_TIME,
                null
        );
        Set<ConstraintViolation<EmployeeRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertNotNull(violations);
        assertEquals("Employee must have a role" ,violations.iterator().next().getMessage());
    }

}
