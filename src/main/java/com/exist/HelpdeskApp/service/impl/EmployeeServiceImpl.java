package com.exist.HelpdeskApp.service.impl;

import com.exist.HelpdeskApp.dto.employee.EmployeeMapper;
import com.exist.HelpdeskApp.dto.employee.EmployeeRequest;
import com.exist.HelpdeskApp.dto.employee.EmployeeResponse;
import com.exist.HelpdeskApp.dto.employee.EmployeeFilterRequest;
import com.exist.HelpdeskApp.exception.businessexceptions.EmployeeNotFoundException;
import com.exist.HelpdeskApp.exception.businessexceptions.EntityInUseException;
import com.exist.HelpdeskApp.exception.businessexceptions.RoleNotFoundException;
import com.exist.HelpdeskApp.model.Account;
import com.exist.HelpdeskApp.model.Employee;
import com.exist.HelpdeskApp.model.Role;
import com.exist.HelpdeskApp.repository.AccountRepository;
import com.exist.HelpdeskApp.repository.EmployeeRepository;
import com.exist.HelpdeskApp.repository.RoleRepository;
import com.exist.HelpdeskApp.repository.TicketRepository;
import com.exist.HelpdeskApp.service.EmployeeService;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final RoleRepository roleRepository;
    private final EmployeeMapper employeeMapper;
    private final TicketRepository ticketRepository;
    private final AccountRepository accountRepository;
    private static final Logger log = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository,
                               EmployeeMapper employeeMapper,
                               RoleRepository roleRepository,
                               TicketRepository ticketRepository,
                               AccountRepository accountRepository) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
        this.roleRepository = roleRepository;
        this.ticketRepository = ticketRepository;
        this.accountRepository =   accountRepository;
    }

    @PostConstruct
    public void init() {
        log.info("EmployeeService initialized at {}", System.currentTimeMillis());
    }

    @PreDestroy
    public void cleanup() {
        log.info("EmployeeService is being destroyed at {}", System.currentTimeMillis());
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public Page<EmployeeResponse> getEmployees(EmployeeFilterRequest request, Pageable pageable, Authentication authentication) {
        Set<String> permissions = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        if (permissions.contains("READ_SELF_EMPLOYEE") && !permissions.contains("READ_ALL_EMPLOYEES")) {
            return new PageImpl<>(Collections.singletonList(employeeMapper.toResponse(getLoggedInEmployee(authentication))), pageable, 1);
        }

        Specification<Employee> spec = request.toSpec();
        Page<Employee> employeePage = employeeRepository.findAll(spec, pageable);

        return employeePage.map(employeeMapper::toResponse);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public EmployeeResponse getEmployeeById(Integer employeeId, Authentication authentication) {
        Set<String> permissions = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        if (permissions.contains("READ_SELF_EMPLOYEE") && !permissions.contains("READ_ALL_EMPLOYEES") && (employeeId != getLoggedInEmployee(authentication).getId())) {
            log.info("User {} attempted to access employee with ID {}", authentication.getName(), employeeId);
            throw new AccessDeniedException("You cannot access other employee");
        }

        Employee employee = checkAccountById(employeeId);
        return employeeMapper.toResponse(employee);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public EmployeeResponse addEmployee(@Valid EmployeeRequest employeeRequest) {
        Role role = checkRoleById(employeeRequest.getRoleId());
        Employee employee = employeeMapper.toEntity(employeeRequest);
        employee.setRole(role);
        Employee updated = employeeRepository.save(employee);
        return employeeMapper.toResponse(updated);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public EmployeeResponse updateEmployee(Integer employeeId, EmployeeRequest request, Authentication authentication) {
        Set<String> permissions = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        if (permissions.contains("UPDATE_SELF_EMPLOYEE") && !permissions.contains("UPDATE_OTHER_EMPLOYEE") && (employeeId != getLoggedInEmployee(authentication).getId())) {
            log.info("User {} attempted to modify employee details with ID {}", authentication.getName(), employeeId);
            throw new AccessDeniedException("You cannot access other employee");
        }

        if (!permissions.contains("UPDATE_EMPLOYEE_ROLE") && (request.getRoleId() != null)) {
            log.info("User {} attempted to modify their role with ID {}", authentication.getName(), request.getRoleId());
            throw new AccessDeniedException("You cannot modify your role");
        }

        Employee employee = checkAccountById(employeeId);
        if (request.getRoleId() != null) {
            Role role = checkRoleById(request.getRoleId());
            employee.setRole(role);
        }
        employeeMapper.toUpdate(request, employee);
        Employee updated = employeeRepository.save(employee);
        return employeeMapper.toResponse(updated);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteEmployee(Integer employeeId) {
        Employee employee = checkAccountById(employeeId);
        boolean isLinkedToTicket = ticketRepository.existsByAssignee(employee);
        if (isLinkedToTicket) {
            log.warn("Attempting to delete employee that is still assigned to at least 1 ticket");
            throw new EntityInUseException("Cannot delete employee with ID " + employeeId + ". Employee is still assigned to at least 1 ticket");
        }
        employee.setDeleted(true);
        employeeRepository.save(employee);
    }

    @Transactional
    public Employee getLoggedInEmployee(Authentication authentication) {
        Account account = accountRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (account.getEmployee() == null) {
            throw new EmployeeNotFoundException("No employee linked to this account. Please contact the admin!");
        }

        return account.getEmployee();
    }

    private Employee checkAccountById(Integer employeeId) {
        return employeeRepository.findByIdAndDeletedFalse(employeeId)
            .orElseThrow(() -> new EmployeeNotFoundException("Employee with ID " + employeeId + " not found!"));
    }

    private Role checkRoleById(Integer roleId) {
        return roleRepository.findByIdAndDeletedFalse(roleId)
            .orElseThrow(() -> new RoleNotFoundException("Role not found"));
    }
}
