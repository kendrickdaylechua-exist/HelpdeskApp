package com.exist.HelpdeskApp.model;

import com.exist.HelpdeskApp.model.embeddable.Contacts;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ContactsTest {

    @Test
    void testAllArgsConstructorAndGetters() {
        Contacts contacts = new Contacts("09171234567", "first1.last1@example.com", "123-4567");

        assertEquals("09171234567", contacts.getPhoneNumber());
        assertEquals("first1.last1@example.com", contacts.getEmail());
        assertEquals("123-4567", contacts.getTelephoneNumber());
    }

    @Test
    void testNoArgsConstructorAndSetters() {
        Contacts contacts = new Contacts();
        contacts.setPhoneNumber("09171234567");
        contacts.setEmail("first1.last1@example.com");
        contacts.setTelephoneNumber("123-4567");

        assertEquals("09171234567", contacts.getPhoneNumber());
        assertEquals("first1.last1@example.com", contacts.getEmail());
        assertEquals("123-4567", contacts.getTelephoneNumber());
    }

    @Test
    void testToString() {
        Contacts contacts = new Contacts("09171234567", "first1.last1@example.com", "123-4567");
        String str = contacts.toString();

        assertTrue(str.contains("09171234567"));
        assertTrue(str.contains("first1.last1@example.com"));
        assertTrue(str.contains("123-4567"));
    }
}

