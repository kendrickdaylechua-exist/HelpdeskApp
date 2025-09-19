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

public class ContactsRequestTest {
    private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void testValidRequests() {
        ContactsRequest request = new ContactsRequest(
                "0912345678",
                "sample@example.com",
                "021234567"
        );

        Set<ConstraintViolation<ContactsRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty());
        assertEquals("0912345678", request.getPhoneNumber());
    }

    @ParameterizedTest
    @NullSource
    void testNullPhoneNumber(String test) {
        ContactsRequest request = new ContactsRequest(
                test,
                "sample@example.com",
                "021234567"
        );

        Set<ConstraintViolation<ContactsRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertEquals("Phone number cannot be empty", violations.iterator().next().getMessage());
    }

    @ParameterizedTest
    @ValueSource(ints = {17, 999})
    void testInvalidPhoneNumber(int stringLength) {
        String test = "1".repeat(stringLength);

        ContactsRequest request = new ContactsRequest(
                test,
                "sample@example.com",
                "021234567"
        );

        Set<ConstraintViolation<ContactsRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertEquals("There is no phone number that exceeds 16 digits", violations.iterator().next().getMessage());
    }

    @ParameterizedTest
    @NullSource
    void testNullEmail(String test) {
        ContactsRequest request = new ContactsRequest(
                "0912345678",
                test,
                "021234567"
        );

        Set<ConstraintViolation<ContactsRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertEquals("Email cannot be empty", violations.iterator().next().getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"sample", "sample@", "sample.com"})
    void testInvalidPhoneNumber(String test) {
        ContactsRequest request = new ContactsRequest(
                "0912345678",
                test,
                "021234567"
        );

        Set<ConstraintViolation<ContactsRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertEquals("Invalid email format", violations.iterator().next().getMessage());
    }

    @ParameterizedTest
    @NullSource
    void testNullTelephoneNumber_ShouldReturnValid(String test) {
        ContactsRequest request = new ContactsRequest(
                "0912345678",
                "sample@example.com",
                test
        );

        Set<ConstraintViolation<ContactsRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty());
        assertEquals(null, request.getTelephoneNumber());
    }

    @ParameterizedTest
    @ValueSource(ints = {17, 999})
    void testInvalidTelephoneNumber(int stringLength) {
        String test = "1".repeat(stringLength);

        ContactsRequest request = new ContactsRequest(
                "0912345678",
                "sample@example.com",
                test
        );

        Set<ConstraintViolation<ContactsRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertEquals("There is no telephone number that exceeds 16 digits", violations.iterator().next().getMessage());
    }
}
