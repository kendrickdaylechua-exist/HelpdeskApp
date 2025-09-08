package com.exist.HelpdeskApp.dto.employee;
import com.exist.HelpdeskApp.model.Employee;
import com.exist.HelpdeskApp.model.EmploymentStatus;
import com.exist.HelpdeskApp.model.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class EmployeeMapperTest {

    private EmployeeMapper mapper;

    @BeforeEach
    void setup() {
        mapper = Mappers.getMapper(EmployeeMapper.class);
    }

    @Test
    void testToEntity() {
        EmployeeRequest request = new EmployeeRequest(
                "name1",
                25,
                "test address 1",
                "09~~~~~~~~~~",
                EmploymentStatus.FULL_TIME,
                1
        );
        Employee entity = mapper.toEntity(request);
        assertEquals("name1", entity.getName());
        assertEquals(25, entity.getAge());
        assertEquals("test address 1", entity.getAddress());
        assertEquals("09~~~~~~~~~~", entity.getContactNumber());
        assertEquals(EmploymentStatus.FULL_TIME, entity.getEmploymentStatus());
        assertNull(entity.getRole());
    }

    @Test
    void testToResponse() {
        Role role = new Role (
                1,
                "role1",
                1
        );
        Employee employee = new Employee(
                2,
                "name1",
                25,
                "test address 1",
                "09~~~~~~~~~~",
                EmploymentStatus.FULL_TIME,
                role,
                1
        );

        EmployeeResponse response = mapper.toResponse(employee);

        assertEquals(2, response.getId());
        assertEquals("name1", response.getName());
        assertEquals(25, response.getAge());
        assertEquals("test address 1", response.getAddress());
        assertEquals("09~~~~~~~~~~", response.getContactNumber());
        assertEquals(EmploymentStatus.FULL_TIME, response.getEmploymentStatus());
        assertEquals("role1", role.getRoleName());
        assertEquals(1, role.getVersion());
    }

    @Test
    void testToUpdate() {
        EmployeeRequest request = new EmployeeRequest(
                "New Name",
                25,
                "New address 1",
                "09~~~~~~~~~~",
                EmploymentStatus.FULL_TIME,
                1
        );
        Employee employee = new Employee();
        employee.setName("Old Name");
        employee.setAge(40);
        employee.setAddress("Old Address");
        employee.setContactNumber("0000000000");
        employee.setEmploymentStatus(EmploymentStatus.INTERN);

        mapper.toUpdate(request, employee);

        assertEquals("New Name", employee.getName());
        assertEquals(25, employee.getAge());
        assertEquals("New address 1", employee.getAddress());
        assertEquals("09~~~~~~~~~~", employee.getContactNumber());
        assertEquals(EmploymentStatus.FULL_TIME, employee.getEmploymentStatus());
        assertNull(employee.getRole());
    }

    @Test
    void testToResponseList() {
        Role role = new Role (
                1,
                "role1",
                1
        );
        Employee employee1 = new Employee(
                2,
                "name1",
                25,
                "test address 1",
                "09~~~~~~~~~~",
                EmploymentStatus.FULL_TIME,
                role,
                1
        );
        Employee employee2 = new Employee(
                3,
                "name2",
                30,
                "test address 2",
                "09~~~~~~~~~~",
                EmploymentStatus.INTERN,
                role,
                1
        );

        List<Employee> employees = new ArrayList<>();
        employees.add(employee1);
        employees.add(employee2);

        List<EmployeeResponse> responses = mapper.toResponseList(employees);

        assertEquals(2, responses.size());
        assertEquals("name1", responses.get(0).getName());
        assertEquals("name2", responses.get(1).getName());
    }
}
