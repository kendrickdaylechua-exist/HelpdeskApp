package com.exist.HelpdeskApp.dto.error;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ErrorResponseTest {
    @Test
    void testGetterAndSetter() {
        ErrorResponse response = new ErrorResponse();
        response.setTimestamp("2025-09-08T06:45:39.523817800Z");
        response.setStatus(404);
        response.setError("Not Found");
        response.setMessage("Sample message");
        response.setPath("/sample-path");

        assertEquals("2025-09-08T06:45:39.523817800Z", response.getTimestamp());
        assertEquals(404, response.getStatus());
        assertEquals("Not Found", response.getError());
        assertEquals("Sample message", response.getMessage());
        assertEquals("/sample-path", response.getPath());
    }

    @Test
    void testAllArgsContructor() {
        ErrorResponse response = new ErrorResponse(
                "2025-09-08T06:45:39.523817800Z",
                404,
                "Not Found",
                "Sample message",
                "/sample-path"
        );
        assertEquals("2025-09-08T06:45:39.523817800Z", response.getTimestamp());
        assertEquals(404, response.getStatus());
        assertEquals("Not Found", response.getError());
        assertEquals("Sample message", response.getMessage());
        assertEquals("/sample-path", response.getPath());
    }
}
