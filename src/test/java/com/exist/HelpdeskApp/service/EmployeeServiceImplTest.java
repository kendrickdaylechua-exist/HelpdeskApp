package com.exist.HelpdeskApp.service;

import com.exist.HelpdeskApp.dto.employee.EmployeeFilterRequest;
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
import com.exist.HelpdeskApp.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

    @Test
    void testGetAllEmployeesAscending() {
        EmployeeFilterRequest request = new EmployeeFilterRequest();
        request.setPage(0);
        request.setSize(10);
        request.setSortBy("id");
        request.setSortDir("asc");
        request.setName("first");
        request.setAddress("phil");
        request.setContacts("region");
        request.setStatus("full_time");
        request.setRoleId(1);
        request.setRoleName("Sample Role");
        request.setDeleted(false);


        Page<Employee> employeePage = new PageImpl<>(List.of(employee));
        when(employeeRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(employeePage);
        when(employeeMapper.toResponseList(List.of(employee))).thenReturn(List.of(response));

        Page<EmployeeResponse> result = employeeServiceImpl.getEmployees(request);

        assertEquals(1, result.getTotalElements());
        assertEquals("First1", result.getContent().get(0).getName().getFirstName());
        verify(employeeRepository).findAll(any(Specification.class), any(Pageable.class));
        verify(employeeMapper).toResponseList(anyList());
    }

    @Test
    void testGetAllEmployeesDescending() {
        EmployeeFilterRequest request = new EmployeeFilterRequest();
        request.setPage(0);
        request.setSize(10);
        request.setSortBy("id");
        request.setSortDir("desc");

        Page<Employee> employeePage = new PageImpl<>(List.of(employee));
        when(employeeRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(employeePage);
        when(employeeMapper.toResponseList(List.of(employee))).thenReturn(List.of(response));

        Page<EmployeeResponse> result = employeeServiceImpl.getEmployees(request);

        assertEquals(1, result.getTotalElements());
        assertEquals("First1", result.getContent().get(0).getName().getFirstName());
        verify(employeeRepository).findAll(any(Specification.class), any(Pageable.class));
        verify(employeeMapper).toResponseList(anyList());
    }

    @Test
    void testNoFilterGetEmployees() {
        EmployeeFilterRequest request = new EmployeeFilterRequest();

        Page<Employee> employeePage = new PageImpl<>(List.of(employee));
        when(employeeRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(employeePage);
        when(employeeMapper.toResponseList(List.of(employee))).thenReturn(List.of(response));

        Page<EmployeeResponse> result = employeeServiceImpl.getEmployees(request);

        assertEquals(1, result.getTotalElements());
        assertEquals("First1", result.getContent().get(0).getName().getFirstName());
        verify(employeeRepository).findAll(any(Specification.class), any(Pageable.class));
        verify(employeeMapper).toResponseList(anyList());
    }

    @Test
    void testNoEmployeeFoundInFilter() {
        EmployeeFilterRequest request = new EmployeeFilterRequest();
        request.setName("First1");
        Page<Employee> employeePage = new PageImpl<>(List.of(employee));
        when(employeeRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(employeePage);
        when(employeeMapper.toResponseList(List.of(employee))).thenReturn(List.of());

        Page<EmployeeResponse> result = employeeServiceImpl.getEmployees(request);

        assertEquals(1, result.getTotalElements());
        assertEquals(0, result.getContent().size());
        verify(employeeRepository).findAll(any(Specification.class), any(Pageable.class));
        verify(employeeMapper).toResponseList(anyList());
    }

    @Test
    void testGetValidEmployee() {
        when(employeeRepository.findByIdAndDeletedFalse(1)).thenReturn(Optional.of(employee));
        when(employeeMapper.toResponse(employee)).thenReturn(response);

        EmployeeResponse result = employeeServiceImpl.getEmployee(1);

        assertEquals("First1", result.getName().getFirstName());
        assertEquals(1, result.getId());
    }

    @Test
    void testGetEmployeeNotFound() {
        when(employeeRepository.findByIdAndDeletedFalse(99)).thenReturn(Optional.empty());
        assertThrows(EmployeeNotFoundException.class, () -> employeeServiceImpl.getEmployee(99));
    }

    @Test
    void testAddValidEmployeeWithRole() {

        when(roleRepository.findByIdAndDeletedFalse(employee.getRole().getId())).thenReturn(Optional.of(role));
        when(employeeMapper.toEntity(request)).thenReturn(employee);
        when(employeeRepository.save(employee)).thenReturn(employee);
        when(employeeMapper.toResponse(employee)).thenReturn(response);

        EmployeeResponse result = employeeServiceImpl.addEmployee(request);

        assertEquals("First1", result.getName().getFirstName());
        assertEquals("Sample Role", result.getRoleName());
    }

    @Test
    void testGetEmployeeWithNoRoleProvided() {
        when(roleRepository.findByIdAndDeletedFalse(request.getRoleId())).thenReturn(Optional.empty());
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

        when(employeeRepository.findByIdAndDeletedFalse(employeeId)).thenReturn(Optional.of(employee));
        when(roleRepository.findByIdAndDeletedFalse(newEmployeeRequest.getRoleId())).thenReturn(Optional.of(role));
        Mockito.doNothing().when(employeeMapper).toUpdate(newEmployeeRequest, employee);
        when(employeeRepository.save(employee)).thenReturn(newEmployee);
        when(employeeMapper.toResponse(newEmployee)).thenReturn(newResponse);

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
        when(roleRepository.findByIdAndDeletedFalse(newEmployeeRequest.getRoleId())).thenReturn(Optional.empty());

        assertThrows(RoleNotFoundException.class, () -> employeeServiceImpl.addEmployee(newEmployeeRequest));
    }

    @Test
    void testUpdateEmployeeButEmployeeNotFound() {
        Integer employeeId = 99;
        when(employeeRepository.findByIdAndDeletedFalse(employeeId)).thenReturn(Optional.empty());
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

        when(employeeRepository.findByIdAndDeletedFalse(employeeId)).thenReturn(Optional.of(employee));
        Mockito.doNothing().when(employeeMapper).toUpdate(newEmployeeRequest, employee);
        when(employeeRepository.save(employee)).thenReturn(newEmployee);
        when(employeeMapper.toResponse(newEmployee)).thenReturn(newResponse);

        EmployeeResponse result = employeeServiceImpl.updateEmployee(employeeId, newEmployeeRequest);

        assertEquals("First2", result.getName().getFirstName());
        assertEquals(1, result.getRoleId());
    }


    @Test
    void testDeleteValidEmployee() {
        Integer employeeId = 1;
        employee.setDeleted(true);
        when(employeeRepository.findByIdAndDeletedFalse(employeeId)).thenReturn(Optional.of(employee));
        when(ticketRepository.existsByAssignee(employee)).thenReturn(false);
        employeeServiceImpl.deleteEmployee(employeeId);
        assertTrue(employee.isDeleted());
        verify(employeeRepository).save(employee);
    }
    @Test
    void testDeleteEmployeeButEmployeeNotFound() {
        Integer employeeId = 99;
        when(employeeRepository.findByIdAndDeletedFalse(employeeId)).thenReturn(Optional.empty());
        assertThrows(EmployeeNotFoundException.class, () -> employeeServiceImpl.deleteEmployee(employeeId));
    }

    @Test
    void testDeletedEmployeeButEmployeeLinkedToTicket() {
        Integer employeeId = 1;
        employee.setDeleted(true);
        when(employeeRepository.findByIdAndDeletedFalse(employeeId)).thenReturn(Optional.of(employee));
        when(ticketRepository.existsByAssignee(employee)).thenReturn(true);
        assertThrows(EntityInUseException.class, () -> employeeServiceImpl.deleteEmployee(employeeId));
    }
}
