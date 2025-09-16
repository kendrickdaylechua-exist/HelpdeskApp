package com.exist.HelpdeskApp.dto.employee;

import org.junit.jupiter.api.Test;
import javax.validation.*;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class EmployeeFilterRequestTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void testValidRequest() {
        EmployeeFilterRequest request = new EmployeeFilterRequest();
        Set<ConstraintViolation<EmployeeFilterRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testInvalidPage() {
        EmployeeFilterRequest request = new EmployeeFilterRequest();
        request.setPage(-1);
        Set<ConstraintViolation<EmployeeFilterRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
    }

    @Test
    void testInvalidSize() {
        EmployeeFilterRequest request = new EmployeeFilterRequest();
        request.setSize(0);
        Set<ConstraintViolation<EmployeeFilterRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
    }

    @Test
    void testInvalidSortDir() {
        EmployeeFilterRequest request = new EmployeeFilterRequest();
        request.setSortDir("ascending");
        Set<ConstraintViolation<EmployeeFilterRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
    }
}
