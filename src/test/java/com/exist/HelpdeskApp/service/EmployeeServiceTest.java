package com.exist.HelpdeskApp.service;

import com.exist.HelpdeskApp.dto.employee.EmployeeMapper;
import com.exist.HelpdeskApp.dto.employee.EmployeeRequest;
import com.exist.HelpdeskApp.dto.employee.EmployeeResponse;
import com.exist.HelpdeskApp.exception.EmployeeNotFoundException;
import com.exist.HelpdeskApp.exception.RoleNotFoundException;
import com.exist.HelpdeskApp.model.Employee;
import com.exist.HelpdeskApp.model.EmploymentStatus;
import com.exist.HelpdeskApp.model.Role;
import com.exist.HelpdeskApp.repository.EmployeeRepository;
import com.exist.HelpdeskApp.repository.RoleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private EmployeeMapper employeeMapper;

    @InjectMocks
    private EmployeeService employeeService;

    @Test
    void testGetAllEmployees() {
        List<Employee> employees = List.of(new Employee(
                1,
                "Sample Name",
                25,
                "Sample Address",
                "0912345678",
                null,
                null,
                null)
        );

        List<EmployeeResponse> employeeResponses = List.of(new EmployeeResponse(
                1,
                "Sample Name",
                25,
                "Sample Address",
                "0912345678",
                null,
                null,
                null)
        );

        Mockito.when(employeeRepository.findAll()).thenReturn(employees);
        Mockito.when(employeeMapper.toResponseList(employees)).thenReturn(employeeResponses);

        List<EmployeeResponse> result = employeeService.getEmployees();

        assertEquals(1, result.size());
        assertEquals("Sample Name", result.get(0).getName());
    }

    @Test
    void testGetListOfEmptyEmployees() {
        List<Employee> employees = new ArrayList<>();
        List<EmployeeResponse> employeeResponses = new ArrayList<>();

        Mockito.when(employeeRepository.findAll()).thenReturn(employees);
        Mockito.when(employeeMapper.toResponseList(employees)).thenReturn(employeeResponses);

        List<EmployeeResponse> result = employeeService.getEmployees();

        assertEquals(0, result.size());
    }

    @Test
    void testGetValidEmployee() {
        Employee employee = new Employee(
                1,
                "Sample Name",
                25,
                "Sample Address",
                "0912345678",
                null,
                null,
                null
        );

        EmployeeResponse response = new EmployeeResponse(
                1,
                "Sample Name",
                25,
                "Sample Address",
                "0912345678",
                null,
                null,
                null
        );

        Mockito.when(employeeRepository.findById(1)).thenReturn(Optional.of(employee));
        Mockito.when(employeeMapper.toResponse(employee)).thenReturn(response);

        EmployeeResponse result = employeeService.getEmployee(1);

        assertEquals("Sample Name", result.getName());
        assertEquals(1, result.getId());
    }

    @Test
    void testGetEmployeeNotFound() {
        Mockito.when(employeeRepository.findById(99)).thenReturn(Optional.empty());
        assertThrows(EmployeeNotFoundException.class, () -> employeeService.getEmployee(99));
    }

    @Test
    void testAddValidEmployeeWithRole() {
        Role role = new Role (
            1, "Sample Role", 1
        );
        EmployeeRequest employeeRequest = new EmployeeRequest(
                "Sample Name",
                25,
                "Sample Address",
                "0912345678",
                EmploymentStatus.FULL_TIME,
                1
        );
        Employee employee = new Employee(
                1,
                "Sample Name",
                25,
                "Sample Address",
                "0912345678",
                EmploymentStatus.FULL_TIME,
                role,
                1
        );
        EmployeeResponse employeeResponse = new EmployeeResponse(
                1,
                "Sample Name",
                25,
                "Sample Address",
                "0912345678",
                EmploymentStatus.FULL_TIME,
                1,
                "Sample Role"
        );

        Mockito.when(roleRepository.findById(employee.getRole().getId())).thenReturn(Optional.of(role));
        Mockito.when(employeeMapper.toEntity(employeeRequest)).thenReturn(employee);
        Mockito.when(employeeRepository.save(employee)).thenReturn(employee);
        Mockito.when(employeeMapper.toResponse(employee)).thenReturn(employeeResponse);

        EmployeeResponse result = employeeService.addEmployee(employeeRequest);

        assertEquals("Sample Name", result.getName());
        assertEquals("Sample Role", result.getRoleName());
    }

    @Test
    void testGetEmployeeWithNoRoleProvided() {
        EmployeeRequest employeeRequest = new EmployeeRequest(
                "Sample Name",
                25,
                "Sample Address",
                "0912345678",
                EmploymentStatus.FULL_TIME,
                99
        );
        Mockito.when(roleRepository.findById(employeeRequest.getRoleId())).thenReturn(Optional.empty());
        assertThrows(RoleNotFoundException.class, () -> employeeService.addEmployee(employeeRequest));
    }

    @Test
    void testValidUpdateEmployee() {
        Integer employeeId = 1;
        Role role = new Role (
                1, "Sample Role", 1
        );
        Employee oldEmployee = new Employee(
                1,
                "Old Name",
                25,
                "Old Address",
                "0912345678",
                EmploymentStatus.FULL_TIME,
                role,
                1
        );
        EmployeeRequest newEmployeeRequest = new EmployeeRequest(
                "New Name",
                25,
                "New Address",
                "0912345678",
                EmploymentStatus.FULL_TIME,
                1
        );
        Employee newEmployee = new Employee(
                1,
                "New Name",
                25,
                "New Address",
                "0912345678",
                EmploymentStatus.FULL_TIME,
                role,
                1
        );
        EmployeeResponse employeeResponse = new EmployeeResponse(
                1,
                "New Name",
                25,
                "New Address",
                "0912345678",
                EmploymentStatus.FULL_TIME,
                1,
                "Sample Role"
        );

        Mockito.when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(oldEmployee));
        Mockito.when(roleRepository.findById(newEmployeeRequest.getRoleId())).thenReturn(Optional.of(role));
        Mockito.doNothing().when(employeeMapper).toUpdate(newEmployeeRequest, oldEmployee);
        Mockito.when(employeeRepository.save(oldEmployee)).thenReturn(newEmployee);
        Mockito.when(employeeMapper.toResponse(newEmployee)).thenReturn(employeeResponse);

        EmployeeResponse result = employeeService.updateEmployee(employeeId, newEmployeeRequest);

        assertEquals("New Name", result.getName());
    }

    @Test
    void testUpdateEmployeeButRoleNotFound() {
        EmployeeRequest newEmployeeRequest = new EmployeeRequest(
                "New Name",
                25,
                "New Address",
                "0912345678",
                EmploymentStatus.FULL_TIME,
                99
        );
        Mockito.when(roleRepository.findById(newEmployeeRequest.getRoleId())).thenReturn(Optional.empty());

        assertThrows(RoleNotFoundException.class, () -> employeeService.addEmployee(newEmployeeRequest));
    }

    @Test
    void testUpdateEmployeeButEmployeeNotFound() {
        Integer employeeId = 99;
        EmployeeRequest employeeRequest = new EmployeeRequest(
                "New Name",
                25,
                "New Address",
                "0912345678",
                EmploymentStatus.FULL_TIME,
                1
        );

        Mockito.when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());
        assertThrows(EmployeeNotFoundException.class, () -> employeeService.updateEmployee(employeeId, employeeRequest));
    }

    @Test
    void testUpdateEmployeeButRoleNotProvided() {
        Integer employeeId = 1;
        Role role = new Role (
                1, "Sample Role", 1
        );
        Employee oldEmployee = new Employee(
                1,
                "Old Name",
                25,
                "Old Address",
                "0912345678",
                EmploymentStatus.FULL_TIME,
                role,
                1
        );
        EmployeeRequest newEmployeeRequest = new EmployeeRequest(
                "New Name",
                25,
                "New Address",
                "0912345678",
                EmploymentStatus.FULL_TIME,
                null
        );
        Employee newEmployee = new Employee(
                1,
                "New Name",
                25,
                "New Address",
                "0912345678",
                EmploymentStatus.FULL_TIME,
                role,
                1
        );
        EmployeeResponse employeeResponse = new EmployeeResponse(
                1,
                "New Name",
                25,
                "New Address",
                "0912345678",
                EmploymentStatus.FULL_TIME,
                1,
                "Sample Role"
        );

        Mockito.when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(oldEmployee));
        Mockito.doNothing().when(employeeMapper).toUpdate(newEmployeeRequest, oldEmployee);
        Mockito.when(employeeRepository.save(oldEmployee)).thenReturn(newEmployee);
        Mockito.when(employeeMapper.toResponse(newEmployee)).thenReturn(employeeResponse);

        EmployeeResponse result = employeeService.updateEmployee(employeeId, newEmployeeRequest);

        assertEquals("New Name", result.getName());
        assertEquals(1, result.getRoleId());
    }

    @Test
    void testDeleteValidEmployee() {
        Integer employeeId = 1;
        Role role = new Role (
                1, "Sample Role", 1
        );
        Employee employee = new Employee(
                1,
                "Sample Name",
                25,
                "Sample Address",
                "0912345678",
                EmploymentStatus.FULL_TIME,
                role,
                1
        );
        Mockito.when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
        employeeService.deleteEmployee(employeeId);
        Mockito.verify(employeeRepository).delete(employee);
    }

    @Test
    void testDeleteEmployeeButEmployeeNotFound() {
        Integer employeeId = 99;
        Mockito.when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());
        assertThrows(EmployeeNotFoundException.class, () -> employeeService.deleteEmployee(employeeId));
    }
}
