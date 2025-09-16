package com.exist.HelpdeskApp.model;

import com.exist.HelpdeskApp.model.embeddable.Address;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AddressTest {

    @Test
    void testAllArgsConstructorAndGetters() {
        Address address = new Address("123 Main St", "First1City", "First1Region", "First1Country");

        assertEquals("123 Main St", address.getStreet());
        assertEquals("First1City", address.getCity());
        assertEquals("First1Region", address.getRegion());
        assertEquals("First1Country", address.getCountry());
    }

    @Test
    void testNoArgsConstructorAndSetters() {
        Address address = new Address();
        address.setStreet("123 Main St");
        address.setCity("First1City");
        address.setRegion("First1Region");
        address.setCountry("First1Country");

        assertEquals("123 Main St", address.getStreet());
        assertEquals("First1City", address.getCity());
        assertEquals("First1Region", address.getRegion());
        assertEquals("First1Country", address.getCountry());
    }

    @Test
    void testToString() {
        Address address = new Address("123 Main St", "First1City", "First1Region", "First1Country");
        String str = address.toString();

        assertTrue(str.contains("123 Main St"));
        assertTrue(str.contains("First1City"));
        assertTrue(str.contains("First1Region"));
        assertTrue(str.contains("First1Country"));
    }
}

