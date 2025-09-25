package com.exist.HelpdeskApp.dto.employee;

import com.exist.HelpdeskApp.model.EmploymentStatus;
import com.exist.HelpdeskApp.model.embeddable.Address;
import com.exist.HelpdeskApp.model.embeddable.Contacts;
import com.exist.HelpdeskApp.model.embeddable.Name;
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

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    private Name name1;
    private Contacts contacts1;
    private Address address1;

    @BeforeEach
    void setup() {
        name1 = new Name("First1", "Middle1", "Last1");
        contacts1 = new Contacts("0912345678", "sample@example.com", "021234567");
        address1 = new Address("123 Test St.", "Manila", "Region 1", "Philippines");
    }

    @ParameterizedTest
    @CsvSource({
            "30, FULL_TIME",
            "25, PART_TIME",
            "35, CONTRACT",
            "45, INTERN",
    })
    void testAllValidRequests(int age, EmploymentStatus employmentStatus) {
        EmployeeRequest request = new EmployeeRequest(
                name1,
                age,
                address1,
                contacts1,
                employmentStatus,
                1
        );
        Set<ConstraintViolation<EmployeeRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty());
    }

    @ParameterizedTest
    @NullSource
    void testNameIsNull(Name test) {
        EmployeeRequest request = new EmployeeRequest(
                test,
                25,
                address1,
                contacts1,
                EmploymentStatus.FULL_TIME,
                1
        );
        Set<ConstraintViolation<EmployeeRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertNotNull(violations);
        assertEquals("Name must not be null", violations.iterator().next().getMessage());
    }

    @ParameterizedTest
    @ValueSource(ints = {16, 25, 50, 130})
    void testValidAge(Integer tests) {
        EmployeeRequest request = new EmployeeRequest(
                name1,
                tests,
                address1,
                contacts1,
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
                name1,
                tests,
                address1,
                contacts1,
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
                name1,
                tests,
                address1,
                contacts1,
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
                name1,
                test,
                address1,
                contacts1,
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
                name1,
                25,
                null,
                contacts1,
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
                name1,
                25,
                address1,
                null,
                EmploymentStatus.FULL_TIME,
                1
        );
        Set<ConstraintViolation<EmployeeRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertNotNull(violations);
        assertEquals("Contacts is required" ,violations.iterator().next().getMessage());
    }

    @Test
    void testNullEmploymentStatus() {
        EmployeeRequest request = new EmployeeRequest(
                name1,
                25,
                address1,
                contacts1,
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
                name1,
                25,
                address1,
                contacts1,
                EmploymentStatus.FULL_TIME,
                null
        );
        Set<ConstraintViolation<EmployeeRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertNotNull(violations);
        assertEquals("Employee must have a role" ,violations.iterator().next().getMessage());
    }

}
