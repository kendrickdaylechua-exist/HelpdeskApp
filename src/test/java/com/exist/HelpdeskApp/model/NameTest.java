package com.exist.HelpdeskApp.model;

import com.exist.HelpdeskApp.model.embeddable.Name;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class NameTest {

    @Test
    void testAllArgsConstructorAndGetters() {
        Name name = new Name("First1", "Middle1", "Last1");

        assertEquals("First1", name.getFirstName());
        assertEquals("Middle1", name.getMiddleName());
        assertEquals("Last1", name.getLastName());
    }

    @Test
    void testNoArgsConstructorAndSetters() {
        Name name = new Name();
        name.setFirstName("First1");
        name.setMiddleName("Middle1");
        name.setLastName("Last1");

        assertEquals("First1", name.getFirstName());
        assertEquals("Middle1", name.getMiddleName());
        assertEquals("Last1", name.getLastName());
    }

    @Test
    void testToString() {
        Name name = new Name("First1", "Middle1", "Last1");
        String str = name.toString();

        assertTrue(str.contains("First1"));
        assertTrue(str.contains("Middle1"));
        assertTrue(str.contains("Last1"));
    }
}

