package com.exist.HelpdeskApp.dto.employee;
import com.exist.HelpdeskApp.model.EmployeeProfile;
import com.exist.HelpdeskApp.model.EmploymentStatus;
import com.exist.HelpdeskApp.model.Role;
import com.exist.HelpdeskApp.model.embeddable.Address;
import com.exist.HelpdeskApp.model.embeddable.Contacts;
import com.exist.HelpdeskApp.model.embeddable.Name;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class EmployeeProfileMapperTest {

    private EmployeeMapper mapper;

    @BeforeEach
    void setup() {
        mapper = Mappers.getMapper(EmployeeMapper.class);
    }

    @Test
    void testToEntity() {
        Name name1 = new Name("First1", "Middle1", "Last1");
        Contacts contacts1 = new Contacts("0912345678", "sample@example.com", "021234567");
        Address address1 = new Address("123 Test St.", "Manila", "Region 1", "Philippines");
        EmployeeRequest request = new EmployeeRequest(
                name1,
                25,
                address1,
                contacts1,
                EmploymentStatus.FULL_TIME,
                1
        );
        EmployeeProfile entity = mapper.toEntity(request);
        assertEquals("First1", entity.getName().getFirstName());
        assertEquals(25, entity.getAge());
        assertEquals("Philippines", entity.getAddress().getCountry());
        assertEquals("sample@example.com", entity.getContacts().getEmail());
        assertEquals(EmploymentStatus.FULL_TIME, entity.getEmploymentStatus());
        assertNull(entity.getRole());
    }

    @Test
    void testToResponse() {
        Name name1 = new Name("First1", "Middle1", "Last1");
        Contacts contacts1 = new Contacts("0912345678", "sample@example.com", "021234567");
        Address address1 = new Address("123 Test St.", "Manila", "Region 1", "Philippines");
        Role role = new Role (
                1,
                "role1",
                false,
                1
        );
        EmployeeProfile employeeProfile = new EmployeeProfile(
                2,
                name1,
                25,
                address1,
                contacts1,
                EmploymentStatus.FULL_TIME,
                role,
                false,
                1
        );

        EmployeeResponse response = mapper.toResponse(employeeProfile);

        assertEquals(2, response.getId());
        assertEquals("First1", response.getName().getFirstName());
        assertEquals(25, response.getAge());
        assertEquals("Philippines", response.getAddress().getCountry());
        assertEquals("0912345678", response.getContacts().getPhoneNumber());
        assertEquals(EmploymentStatus.FULL_TIME, response.getEmploymentStatus());
        assertEquals("role1", role.getRoleName());
        assertEquals(1, role.getVersion());
    }

    @Test
    void testToUpdate() {
        Name name1 = new Name("First1", "Middle1", "Last1");
        Contacts contacts1 = new Contacts("0912345678", "sample@example.com", "021234567");
        Address address1 = new Address("123 Test St.", "Manila", "Region 1", "Philippines");
        EmployeeRequest request = new EmployeeRequest(
                name1,
                25,
                address1,
                contacts1,
                EmploymentStatus.FULL_TIME,
                1
        );
        EmployeeProfile employeeProfile = new EmployeeProfile();
        employeeProfile.setName(name1);
        employeeProfile.setAge(40);
        employeeProfile.setAddress(address1);
        employeeProfile.setContacts(contacts1);
        employeeProfile.setEmploymentStatus(EmploymentStatus.INTERN);

        mapper.toUpdate(request, employeeProfile);

        assertEquals("First1", employeeProfile.getName().getFirstName());
        assertEquals(25, employeeProfile.getAge());
        assertEquals("Philippines", employeeProfile.getAddress().getCountry());
        assertEquals("0912345678", employeeProfile.getContacts().getPhoneNumber());
        assertEquals(EmploymentStatus.FULL_TIME, employeeProfile.getEmploymentStatus());
        assertNull(employeeProfile.getRole());
    }

    @Test
    void testToResponseList() {
        Name name1 = new Name("First1", "Middle1", "Last1");
        Contacts contacts1 = new Contacts("0912345678", "sample@example.com", "021234567");
        Address address1 = new Address("123 Test St.", "Manila", "Region 1", "Philippines");

        Name name2 = new Name("First2", "Middle2", "Last2");
        Contacts contacts2 = new Contacts("0912345678", "sample@example.com", "021234567");
        Address address2 = new Address("321 Test St.", "Los Angeles", "California","USA");

        Role role = new Role (
                1,
                "role1",
                false,
                1
        );
        EmployeeProfile employeeProfile1 = new EmployeeProfile(
                2,
                name1,
                25,
                address1,
                contacts1,
                EmploymentStatus.FULL_TIME,
                role,
                false,
                1
        );
        EmployeeProfile employeeProfile2 = new EmployeeProfile(
                3,
                name2,
                30,
                address2,
                contacts2,
                EmploymentStatus.INTERN,
                role,
                false,
                1
        );

        List<EmployeeProfile> employeeProfiles = new ArrayList<>();
        employeeProfiles.add(employeeProfile1);
        employeeProfiles.add(employeeProfile2);

        List<EmployeeResponse> responses = mapper.toResponseList(employeeProfiles);

        assertEquals(2, responses.size());
        assertEquals("First1", responses.get(0).getName().getFirstName());
        assertEquals("First2", responses.get(1).getName().getFirstName());
    }
}
