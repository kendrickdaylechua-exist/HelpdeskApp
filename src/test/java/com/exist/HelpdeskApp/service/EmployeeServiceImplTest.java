package com.exist.HelpdeskApp.service;

import com.exist.HelpdeskApp.dto.employee.EmployeeMapper;
import com.exist.HelpdeskApp.dto.employee.EmployeeRequest;
import com.exist.HelpdeskApp.dto.employee.EmployeeResponse;
import com.exist.HelpdeskApp.exception.businessexceptions.EmployeeNotFoundException;
import com.exist.HelpdeskApp.exception.businessexceptions.EntityInUseException;
import com.exist.HelpdeskApp.exception.businessexceptions.RoleNotFoundException;
import com.exist.HelpdeskApp.model.Employee;
import com.exist.HelpdeskApp.model.EmploymentStatus;
import com.exist.HelpdeskApp.model.Role;
import com.exist.HelpdeskApp.model.embeddable.Address;
import com.exist.HelpdeskApp.model.embeddable.Contacts;
import com.exist.HelpdeskApp.model.embeddable.Name;
import com.exist.HelpdeskApp.repository.EmployeeRepository;
import com.exist.HelpdeskApp.repository.RoleRepository;
import com.exist.HelpdeskApp.repository.TicketRepository;
import com.exist.HelpdeskApp.service.Implementations.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private EmployeeMapper employeeMapper;

    @InjectMocks
    private EmployeeServiceImpl employeeServiceImpl;

    private Employee employee;
    private EmployeeResponse response;
    private EmployeeRequest request;
    private Role role;

    @BeforeEach
    void setup() {
        Name name1 = new Name("First1", "Middle1", "Last1");
        Contacts contacts1 = new Contacts("0912345678", "sample@example.com", "021234567");
        Address address1 = new Address("123 Test St.", "Manila", "Region 1", "Philippines");
        role = new Role (1, "Sample Role", false, 1);
        employee = new Employee(
                1,
                name1,
                25,
                address1,
                contacts1,
                EmploymentStatus.FULL_TIME,
                role,
                false,
                null
        );

        response = new EmployeeResponse(
                1,
                name1,
                25,
                address1,
                contacts1,
                EmploymentStatus.FULL_TIME,
                1,
                "Sample Role"
        );
        request = new EmployeeRequest(
                name1,
                25,
                address1,
                contacts1,
                EmploymentStatus.FULL_TIME,
                1
        );
    }

//    @Test
//    void testGetAllEmployees() {
//        Name name1 = new Name("First1", "Middle1", "Last1");
//        Contacts contacts1 = new Contacts("0912345678", "sample@example.com", "021234567");
//        Address address1 = new Address("123 Test St.", "Manila", "Region 1", "Philippines");
//        List<Employee> employees = List.of(new Employee(
//                1,
//                name1,
//                25,
//                address1,
//                contacts1,
//                null,
//                null,
//                false,
//                null)
//        );
//
//        List<EmployeeResponse> employeeResponses = List.of(new EmployeeResponse(
//                1,
//                name1,
//                25,
//                address1,
//                contacts1,
//                null,
//                null,
//                null)
//        );
//
//        Mockito.when(employeeRepository.findAll()).thenReturn(employees);
//        Mockito.when(employeeMapper.toResponseList(employees)).thenReturn(employeeResponses);
//
//        List<EmployeeResponse> result = employeeServiceImpl.getEmployees();
//
//        assertEquals(1, result.size());
//        assertEquals("Sample Name", result.get(0).getName());
//    }

//    @Test
//    void testGetListOfEmptyEmployees() {
//        List<Employee> employees = new ArrayList<>();
//        List<EmployeeResponse> employeeResponses = new ArrayList<>();
//
//        Mockito.when(employeeRepository.findAll()).thenReturn(employees);
//        Mockito.when(employeeMapper.toResponseList(employees)).thenReturn(employeeResponses);
//
//        List<EmployeeResponse> result = employeeServiceImpl.getEmployees();
//
//        assertEquals(0, result.size());
//    }

    @Test
    void testGetValidEmployee() {
        Mockito.when(employeeRepository.findByIdAndDeletedFalse(1)).thenReturn(Optional.of(employee));
        Mockito.when(employeeMapper.toResponse(employee)).thenReturn(response);

        EmployeeResponse result = employeeServiceImpl.getEmployee(1);

        assertEquals("First1", result.getName().getFirstName());
        assertEquals(1, result.getId());
    }

    @Test
    void testGetEmployeeNotFound() {
        Mockito.when(employeeRepository.findByIdAndDeletedFalse(99)).thenReturn(Optional.empty());
        assertThrows(EmployeeNotFoundException.class, () -> employeeServiceImpl.getEmployee(99));
    }

    @Test
    void testAddValidEmployeeWithRole() {

        Mockito.when(roleRepository.findByIdAndDeletedFalse(employee.getRole().getId())).thenReturn(Optional.of(role));
        Mockito.when(employeeMapper.toEntity(request)).thenReturn(employee);
        Mockito.when(employeeRepository.save(employee)).thenReturn(employee);
        Mockito.when(employeeMapper.toResponse(employee)).thenReturn(response);

        EmployeeResponse result = employeeServiceImpl.addEmployee(request);

        assertEquals("First1", result.getName().getFirstName());
        assertEquals("Sample Role", result.getRoleName());
    }

    @Test
    void testGetEmployeeWithNoRoleProvided() {
        Mockito.when(roleRepository.findByIdAndDeletedFalse(request.getRoleId())).thenReturn(Optional.empty());
        assertThrows(RoleNotFoundException.class, () -> employeeServiceImpl.addEmployee(request));
    }

    @Test
    void testValidUpdateEmployee() {
        Integer employeeId = 1;
        Name name2 = new Name("First2", "Middle2", "Last2");
        Contacts contacts2 = new Contacts("0912345678", "sample@example.com", "021234567");
        Address address2 = new Address("321 Test St.", "Los Angeles", "California","USA");
        EmployeeRequest newEmployeeRequest = new EmployeeRequest(
                name2,
                25,
                address2,
                contacts2,
                EmploymentStatus.FULL_TIME,
                1
        );
        Employee newEmployee = new Employee(
                1,
                name2,
                25,
                address2,
                contacts2,
                EmploymentStatus.FULL_TIME,
                role,
                false,
                1
        );
        EmployeeResponse newResponse = new EmployeeResponse(
                1,
                name2,
                25,
                address2,
                contacts2,
                EmploymentStatus.FULL_TIME,
                1,
                "Sample Role"
        );

        Mockito.when(employeeRepository.findByIdAndDeletedFalse(employeeId)).thenReturn(Optional.of(employee));
        Mockito.when(roleRepository.findByIdAndDeletedFalse(newEmployeeRequest.getRoleId())).thenReturn(Optional.of(role));
        Mockito.doNothing().when(employeeMapper).toUpdate(newEmployeeRequest, employee);
        Mockito.when(employeeRepository.save(employee)).thenReturn(newEmployee);
        Mockito.when(employeeMapper.toResponse(newEmployee)).thenReturn(newResponse);

        EmployeeResponse result = employeeServiceImpl.updateEmployee(employeeId, newEmployeeRequest);

        assertEquals("First2", result.getName().getFirstName());
    }

    @Test
    void testUpdateEmployeeButRoleNotFound() {
        Name name2 = new Name("First2", "Middle2", "Last2");
        Contacts contacts2 = new Contacts("0912345678", "sample@example.com", "021234567");
        Address address2 = new Address("321 Test St.", "Los Angeles", "California","USA");
        EmployeeRequest newEmployeeRequest = new EmployeeRequest(
                name2,
                25,
                address2,
                contacts2,
                EmploymentStatus.FULL_TIME,
                1
        );
        Mockito.when(roleRepository.findByIdAndDeletedFalse(newEmployeeRequest.getRoleId())).thenReturn(Optional.empty());

        assertThrows(RoleNotFoundException.class, () -> employeeServiceImpl.addEmployee(newEmployeeRequest));
    }

    @Test
    void testUpdateEmployeeButEmployeeNotFound() {
        Integer employeeId = 99;
        Mockito.when(employeeRepository.findByIdAndDeletedFalse(employeeId)).thenReturn(Optional.empty());
        assertThrows(EmployeeNotFoundException.class, () -> employeeServiceImpl.updateEmployee(employeeId, request));
    }

    @Test
    void testUpdateEmployeeButRoleNotProvided() {
        Integer employeeId = 1;
        Name name2 = new Name("First2", "Middle2", "Last2");
        Contacts contacts2 = new Contacts("0912345678", "sample@example.com", "021234567");
        Address address2 = new Address("321 Test St.", "Los Angeles", "California","USA");
        EmployeeRequest newEmployeeRequest = new EmployeeRequest(
                name2,
                25,
                address2,
                contacts2,
                EmploymentStatus.FULL_TIME,
                null
        );
        Employee newEmployee = new Employee(
                1,
                name2,
                25,
                address2,
                contacts2,
                EmploymentStatus.FULL_TIME,
                role,
                false,
                1
        );
        EmployeeResponse newResponse = new EmployeeResponse(
                1,
                name2,
                25,
                address2,
                contacts2,
                EmploymentStatus.FULL_TIME,
                1,
                "Sample Role"
        );

        Mockito.when(employeeRepository.findByIdAndDeletedFalse(employeeId)).thenReturn(Optional.of(employee));
        Mockito.doNothing().when(employeeMapper).toUpdate(newEmployeeRequest, employee);
        Mockito.when(employeeRepository.save(employee)).thenReturn(newEmployee);
        Mockito.when(employeeMapper.toResponse(newEmployee)).thenReturn(newResponse);

        EmployeeResponse result = employeeServiceImpl.updateEmployee(employeeId, newEmployeeRequest);

        assertEquals("First2", result.getName().getFirstName());
        assertEquals(1, result.getRoleId());
    }


    @Test
    void testDeleteValidEmployee() {
        Integer employeeId = 1;
        employee.setDeleted(true);
        Mockito.when(employeeRepository.findByIdAndDeletedFalse(employeeId)).thenReturn(Optional.of(employee));
        Mockito.when(ticketRepository.existsByAssignee(employee)).thenReturn(false);
        employeeServiceImpl.deleteEmployee(employeeId);
        assertTrue(employee.isDeleted());
        Mockito.verify(employeeRepository).save(employee);
    }
    @Test
    void testDeleteEmployeeButEmployeeNotFound() {
        Integer employeeId = 99;
        Mockito.when(employeeRepository.findByIdAndDeletedFalse(employeeId)).thenReturn(Optional.empty());
        assertThrows(EmployeeNotFoundException.class, () -> employeeServiceImpl.deleteEmployee(employeeId));
    }

    @Test
    void testDeletedEmployeeButEmployeeLinkedToTicket() {
        Integer employeeId = 1;
        employee.setDeleted(true);
        Mockito.when(employeeRepository.findByIdAndDeletedFalse(employeeId)).thenReturn(Optional.of(employee));
        Mockito.when(ticketRepository.existsByAssignee(employee)).thenReturn(true);
        assertThrows(EntityInUseException.class, () -> employeeServiceImpl.deleteEmployee(employeeId));
    }
}
