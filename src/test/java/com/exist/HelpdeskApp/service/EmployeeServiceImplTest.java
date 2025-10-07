package com.exist.HelpdeskApp.service;

import com.exist.HelpdeskApp.dto.employee.EmployeeFilterRequest;
import com.exist.HelpdeskApp.dto.employee.EmployeeMapper;
import com.exist.HelpdeskApp.dto.employee.EmployeeRequest;
import com.exist.HelpdeskApp.dto.employee.EmployeeResponse;
import com.exist.HelpdeskApp.exception.businessexceptions.EmployeeNotFoundException;
import com.exist.HelpdeskApp.exception.businessexceptions.EntityInUseException;
import com.exist.HelpdeskApp.exception.businessexceptions.RoleNotFoundException;
import com.exist.HelpdeskApp.model.*;
import com.exist.HelpdeskApp.model.embeddable.Address;
import com.exist.HelpdeskApp.model.embeddable.Contacts;
import com.exist.HelpdeskApp.model.embeddable.Name;
import com.exist.HelpdeskApp.repository.AccountRepository;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
    
    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private static Employee employee;
    private static EmployeeResponse response;
    private static EmployeeRequest request;
    private static Role role;
    private static final Pageable pageable = PageRequest.of(0, 5);
    private static Authentication authAdmin;
    private static Authentication authUser;
    private static Account account;

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
        authAdmin = new UsernamePasswordAuthenticationToken(
            "ADMIN",
            null,
            List.of(
                    new SimpleGrantedAuthority("READ_SELF_EMPLOYEE"),
                    new SimpleGrantedAuthority("READ_ALL_EMPLOYEES"),
                    new SimpleGrantedAuthority("CREATE_EMPLOYEE"),
                    new SimpleGrantedAuthority("UPDATE_OTHER_EMPLOYEE"),
                    new SimpleGrantedAuthority("UPDATE_SELF_EMPLOYEE"),
                    new SimpleGrantedAuthority("DELETE_EMPLOYEE"),
                    new SimpleGrantedAuthority("UPDATE_EMPLOYEE_ROLE")
            )
        );
        authUser = new UsernamePasswordAuthenticationToken(
            "USER",
            null,
            List.of(
                    new SimpleGrantedAuthority("READ_SELF_EMPLOYEE"),
                    new SimpleGrantedAuthority("UPDATE_SELF_EMPLOYEE")
            )
        );
        Permission permission = new Permission(1, "samplePermission");
        SecurityRole securityRole = new SecurityRole(1, "sampleSecurityRole", Set.of(permission));
        account = new Account(
                1,
                "user",
                "samplePassword",
                employee,
                true,
                Set.of(securityRole)
        );
    }

    @Test
    void testAdminGetAllEmployees() {
        EmployeeFilterRequest request = new EmployeeFilterRequest();

        Page<Employee> employeePage = new PageImpl<>(List.of(employee));

        when(employeeRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(employeePage);
        when(employeeMapper.toResponse(any(Employee.class))).thenReturn(response);

        Page<EmployeeResponse> result = employeeService.getEmployees(request, pageable, authAdmin);

        assertEquals(1, result.getTotalElements());
        assertEquals("First1", result.getContent().get(0).getName().getFirstName());

        verify(employeeRepository).findAll(any(Specification.class), any(Pageable.class));
    }


    @Test
    void testAdminNoFilterGetEmployees() {
        EmployeeFilterRequest request = new EmployeeFilterRequest();

        Page<Employee> employeePage = new PageImpl<>(List.of(employee));
        when(employeeRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(employeePage);
        when(employeeMapper.toResponse(any(Employee.class))).thenReturn(response);

        Page<EmployeeResponse> result = employeeService.getEmployees(request, pageable, authAdmin);

        assertEquals(1, result.getTotalElements());
        assertEquals("First1", result.getContent().get(0).getName().getFirstName());
        verify(employeeRepository).findAll(any(Specification.class), any(Pageable.class));
        verify(employeeMapper).toResponse(any(Employee.class));
    }

    @Test
    void testAdminNoEmployeeFoundInFilter() {
        EmployeeFilterRequest request = new EmployeeFilterRequest();
        request.setName("Invalid");
        Page<Role> emptyPage = Page.empty(pageable);
        when(employeeRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(emptyPage);

        Page<EmployeeResponse> result = employeeService.getEmployees(request, pageable, authAdmin);

        assertEquals(0, result.getTotalElements());
        assertEquals(0, result.getContent().size());
        verify(employeeRepository).findAll(any(Specification.class), any(Pageable.class));
        verify(employeeMapper, never()).toResponse(any(Employee.class));
    }

    @Test
    void testUserGetAllEmployees() {
        EmployeeFilterRequest requestFilter = new EmployeeFilterRequest();
        when(accountRepository.findByUsername(anyString()))
                .thenReturn(Optional.of(account));
        when(employeeMapper.toResponse(any()))
                .thenReturn(new EmployeeResponse());

        Page<EmployeeResponse> result = employeeService.getEmployees(requestFilter, pageable, authUser);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    void testAdminGetValidEmployee() {
        when(employeeRepository.findByIdAndDeletedFalse(1)).thenReturn(Optional.of(employee));
        when(employeeMapper.toResponse(employee)).thenReturn(response);

        EmployeeResponse result = employeeService.getEmployeeById(1, authAdmin);

        assertEquals("First1", result.getName().getFirstName());
        assertEquals(1, result.getId());
    }

    @Test
    void testAdminGetEmployeeNotFound() {
        when(employeeRepository.findByIdAndDeletedFalse(99)).thenReturn(Optional.empty());
        assertThrows(EmployeeNotFoundException.class, () -> employeeService.getEmployeeById(99, authAdmin));
    }

    @Test
    void testUserGetOwnDetails() {
        when(accountRepository.findByUsername(anyString()))
                .thenReturn(Optional.of(account));
        when(employeeRepository.findByIdAndDeletedFalse(1)).thenReturn(Optional.of(employee));
        when(employeeMapper.toResponse(employee)).thenReturn(response);

        EmployeeResponse result = employeeService.getEmployeeById(1, authUser);

        assertEquals("First1", result.getName().getFirstName());
        assertEquals(1, result.getId());
    }

    @Test
    void testAddValidEmployeeWithRole() {

        when(roleRepository.findByIdAndDeletedFalse(employee.getRole().getId())).thenReturn(Optional.of(role));
        when(employeeMapper.toEntity(request)).thenReturn(employee);
        when(employeeRepository.save(employee)).thenReturn(employee);
        when(employeeMapper.toResponse(employee)).thenReturn(response);

        EmployeeResponse result = employeeService.addEmployee(request);

        assertEquals("First1", result.getName().getFirstName());
        assertEquals("Sample Role", result.getRoleName());
    }

    @Test
    void testAdminValidUpdateEmployee() {
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

        EmployeeResponse result = employeeService.updateEmployee(employeeId, newEmployeeRequest, authAdmin);

        assertEquals("First2", result.getName().getFirstName());
    }

    @Test
    void testUserValidUpdateOwnProfileWithoutUpdatingRole() {
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
        when(accountRepository.findByUsername(anyString()))
                .thenReturn(Optional.of(account));
        when(employeeRepository.findByIdAndDeletedFalse(employeeId)).thenReturn(Optional.of(employee));
        Mockito.doNothing().when(employeeMapper).toUpdate(newEmployeeRequest, employee);
        when(employeeRepository.save(employee)).thenReturn(newEmployee);
        when(employeeMapper.toResponse(newEmployee)).thenReturn(newResponse);

        EmployeeResponse result = employeeService.updateEmployee(employeeId, newEmployeeRequest, authUser);

        assertEquals("First2", result.getName().getFirstName());
    }

    @Test
    void testUserUpdateRole_ThrowAccessDeniedException() {
        Integer employeeId = 1;
        EmployeeRequest newEmployeeRequest = new EmployeeRequest(
                null,
                null,
                null,
                null,
                null,
                2
        );
        when(accountRepository.findByUsername(anyString()))
                .thenReturn(Optional.of(account));
        assertThrows(AccessDeniedException.class, () -> employeeService.updateEmployee(employeeId, newEmployeeRequest, authUser));
    }

    @Test
    void testUserUpdateOtherUser_ThrowAccessDeniedException() {
        Integer employeeId = 99;
        when(accountRepository.findByUsername(anyString()))
                .thenReturn(Optional.of(account));
        assertThrows(AccessDeniedException.class, () -> employeeService.updateEmployee(employeeId, any(EmployeeRequest.class), authUser));
    }

    @Test
    void testAdminUpdateEmployeeButRoleNotFound() {
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

        assertThrows(RoleNotFoundException.class, () -> employeeService.addEmployee(newEmployeeRequest));
    }

    @Test
    void testAdminUpdateEmployeeButEmployeeNotFound() {
        Integer employeeId = 99;
        when(employeeRepository.findByIdAndDeletedFalse(employeeId)).thenReturn(Optional.empty());
        assertThrows(EmployeeNotFoundException.class, () -> employeeService.updateEmployee(employeeId, request, authAdmin));
    }

    @Test
    void testUserAddEmployee_ThrowAccessDeniedException() {
        EmployeeServiceImpl employeeServiceSpy = spy(employeeService);

        doThrow(new AccessDeniedException("Access is denied"))
                .when(employeeServiceSpy)
                .addEmployee(any());

        assertThrows(AccessDeniedException.class, () -> {
            employeeServiceSpy.addEmployee(new EmployeeRequest());
        });
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

        EmployeeResponse result = employeeService.updateEmployee(employeeId, newEmployeeRequest, authAdmin);

        assertEquals("First2", result.getName().getFirstName());
        assertEquals(1, result.getRoleId());
    }


    @Test
    void testAdminDeleteValidEmployee() {
        Integer employeeId = 1;
        employee.setDeleted(true);
        when(employeeRepository.findByIdAndDeletedFalse(employeeId)).thenReturn(Optional.of(employee));
        when(ticketRepository.existsByAssignee(employee)).thenReturn(false);
        employeeService.deleteEmployee(employeeId);
        assertTrue(employee.isDeleted());
        verify(employeeRepository).save(employee);
    }

    @Test
    void testAdminDeleteEmployeeButEmployeeNotFound() {
        Integer employeeId = 99;
        when(employeeRepository.findByIdAndDeletedFalse(employeeId)).thenReturn(Optional.empty());
        assertThrows(EmployeeNotFoundException.class, () -> employeeService.deleteEmployee(employeeId));
    }

    @Test
    void testUserDeleteEmployee_ThrowAccessDeniedException() {
        EmployeeServiceImpl employeeServiceSpy = spy(employeeService);

        doThrow(new AccessDeniedException("Access is denied"))
                .when(employeeServiceSpy)
                .deleteEmployee(any());

        assertThrows(AccessDeniedException.class, () -> {
            employeeServiceSpy.deleteEmployee(any());
        });
    }


    @Test
    void testDeletedEmployeeButEmployeeLinkedToTicket() {
        Integer employeeId = 1;
        employee.setDeleted(true);
        when(employeeRepository.findByIdAndDeletedFalse(employeeId)).thenReturn(Optional.of(employee));
        when(ticketRepository.existsByAssignee(employee)).thenReturn(true);
        assertThrows(EntityInUseException.class, () -> employeeService.deleteEmployee(employeeId));
    }
}