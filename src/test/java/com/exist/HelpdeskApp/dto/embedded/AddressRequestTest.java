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

public class AddressRequestTest {
    private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void testAllValidRequests() {
        AddressRequest request = new AddressRequest();
        request.setStreet("123 Street St.");
        request.setCity("Manila");
        request.setRegion("Region 1");
        request.setCountry("Philippines");

        Set<ConstraintViolation<AddressRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty());
        assertEquals("123 Street St.", request.getStreet());
        assertEquals("Manila", request.getCity());
        assertEquals("Region 1", request.getRegion());
        assertEquals("Philippines", request.getCountry());
    }

    @ParameterizedTest
    @NullSource
    void testNullStreet(String test) {
        AddressRequest request = new AddressRequest();
        request.setStreet(test);
        request.setCity("Manila");
        request.setRegion("Region 1");
        request.setCountry("Philippines");

        Set<ConstraintViolation<AddressRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertEquals("Street cannot be blank", violations.iterator().next().getMessage());
    }

    @ParameterizedTest
    @ValueSource(ints = {101, 999})
    void testInvalidStreet(int stringLength) {
        String test = "a".repeat(stringLength);

        AddressRequest request = new AddressRequest();
        request.setStreet(test);
        request.setCity("Manila");
        request.setRegion("Region 1");
        request.setCountry("Philippines");

        Set<ConstraintViolation<AddressRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertEquals("Street cannot exceed 100 characters", violations.iterator().next().getMessage());
    }

    @ParameterizedTest
    @NullSource
    void testNullCity(String test){
        AddressRequest request = new AddressRequest();
        request.setStreet("123 Street St.");
        request.setCity(test);
        request.setRegion("Region 1");
        request.setCountry("Philippines");

        Set<ConstraintViolation<AddressRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertEquals("City cannot be blank", violations.iterator().next().getMessage());
    }

    @ParameterizedTest
    @ValueSource(ints = {51, 999})
    void testInvalidCity(int stringLength) {
        String test = "a".repeat(stringLength);

        AddressRequest request = new AddressRequest();
        request.setStreet("123 Street St.");
        request.setCity(test);
        request.setRegion("Region 1");
        request.setCountry("Philippines");

        Set<ConstraintViolation<AddressRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertEquals("City cannot exceed 50 characters", violations.iterator().next().getMessage());
    }

    @ParameterizedTest
    @NullSource
    void testNullRegion(String test){
        AddressRequest request = new AddressRequest();
        request.setStreet("123 Street St.");
        request.setCity("Manila");
        request.setRegion(test);
        request.setCountry("Philippines");

        Set<ConstraintViolation<AddressRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertEquals("Region cannot be blank", violations.iterator().next().getMessage());
    }

    @ParameterizedTest
    @ValueSource(ints = {101, 999})
    void testInvalid(int stringLength) {
        String test = "a".repeat(stringLength);

        AddressRequest request = new AddressRequest();
        request.setStreet("123 Street St.");
        request.setCity("Manila");
        request.setRegion(test);
        request.setCountry("Philippines");

        Set<ConstraintViolation<AddressRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertEquals("Region cannot exceed 50 characters", violations.iterator().next().getMessage());
    }

    @ParameterizedTest
    @ValueSource(ints = {101, 999})
    void testInvalidCountry(int stringLength) {
        String test = "a".repeat(stringLength);

        AddressRequest request = new AddressRequest();
        request.setStreet("123 Street St.");
        request.setCity("Manila");
        request.setRegion("Region 1");
        request.setCountry(test);

        Set<ConstraintViolation<AddressRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertEquals("Country cannot exceed 50 characters", violations.iterator().next().getMessage());
    }

    @ParameterizedTest
    @NullSource
    void testNullCountry(String test){
        AddressRequest request = new AddressRequest();
        request.setStreet("123 Street St.");
        request.setCity("Manila");
        request.setRegion("Region 1");
        request.setCountry(test);

        Set<ConstraintViolation<AddressRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertEquals("Country cannot be blank", violations.iterator().next().getMessage());
    }

}
