package com.exist.HelpdeskApp.dto.employee;

import com.exist.HelpdeskApp.model.EmploymentStatus;
import com.exist.HelpdeskApp.model.embeddable.Address;
import com.exist.HelpdeskApp.model.embeddable.Contacts;
import com.exist.HelpdeskApp.model.embeddable.Name;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EmployeeProfileResponseTest {

    @Test
    void testGetterAndSetter() {
        Name name1 = new Name("First1", "Middle1", "Last1");
        Contacts contacts1 = new Contacts("0912345678", "sample@example.com", "021234567");
        Address address1 = new Address("123 Test St.", "Manila", "Region 1", "Philippines");

        EmployeeResponse response = new EmployeeResponse();
        response.setId(1);
        response.setName(name1);
        response.setAge(30);
        response.setAddress(address1);
        response.setContacts(contacts1);
        response.setEmploymentStatus(EmploymentStatus.FULL_TIME);
        response.setRoleId(1);
        response.setRoleName("Test Role");

        assertEquals(1, response.getId());
        assertEquals("First1", response.getName().getFirstName());
        assertEquals(30, response.getAge());
        assertEquals("Philippines", response.getAddress().getCountry());
        assertEquals("0912345678", response.getContacts().getPhoneNumber());
        assertEquals(EmploymentStatus.FULL_TIME, response.getEmploymentStatus());
        assertEquals(1, response.getRoleId());
        assertEquals("Test Role", response.getRoleName());
    }

    @Test
    void testAllArgsConstructor() {
        Name name1 = new Name("First1", "Middle1", "Last1");
        Contacts contacts1 = new Contacts("0912345678", "sample@example.com", "021234567");
        Address address1 = new Address("123 Test St.", "Manila", "Region 1", "Philippines");
        EmployeeResponse response = new EmployeeResponse(
                1,
                name1,
                30,
                address1,
                contacts1,
                EmploymentStatus.FULL_TIME,
                1,
                "Test Role"
        );

        assertEquals(1, response.getId());
        assertEquals("First1", response.getName().getFirstName());
        assertEquals(30, response.getAge());
        assertEquals("Philippines", response.getAddress().getCountry());
        assertEquals("0912345678", response.getContacts().getPhoneNumber());
        assertEquals(EmploymentStatus.FULL_TIME, response.getEmploymentStatus());
        assertEquals(1, response.getRoleId());
        assertEquals("Test Role", response.getRoleName());
    }
}
