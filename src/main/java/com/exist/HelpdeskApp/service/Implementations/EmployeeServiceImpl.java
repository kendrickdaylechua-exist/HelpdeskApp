package com.exist.HelpdeskApp.service.Implementations;

import com.exist.HelpdeskApp.dto.employee.EmployeeMapper;
import com.exist.HelpdeskApp.dto.employee.EmployeeRequest;
import com.exist.HelpdeskApp.dto.employee.EmployeeResponse;
import com.exist.HelpdeskApp.dto.employee.EmployeeFilterRequest;
import com.exist.HelpdeskApp.exception.businessexceptions.EmployeeNotFoundException;
import com.exist.HelpdeskApp.exception.businessexceptions.EntityInUseException;
import com.exist.HelpdeskApp.exception.businessexceptions.RoleNotFoundException;
import com.exist.HelpdeskApp.model.Employee;
import com.exist.HelpdeskApp.model.Role;
import com.exist.HelpdeskApp.repository.EmployeeRepository;
import com.exist.HelpdeskApp.repository.RoleRepository;
import com.exist.HelpdeskApp.repository.TicketRepository;
import com.exist.HelpdeskApp.repository.specifications.EmployeeSpecifications;
import com.exist.HelpdeskApp.service.EmployeeService;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final RoleRepository roleRepository;
    private final EmployeeMapper employeeMapper;
    private final TicketRepository ticketRepository;
    private static final Logger log = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository,
                               EmployeeMapper employeeMapper,
                               RoleRepository roleRepository,
                               TicketRepository ticketRepository) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
        this.roleRepository = roleRepository;
        this.ticketRepository = ticketRepository;
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
    public Page<EmployeeResponse> getEmployees(EmployeeFilterRequest request) {
        Specification<Employee> spec = Specification.where(null);

        spec = spec.and(EmployeeSpecifications.hasDeleted(request.isDeleted()));

        if (request.getFirstName() != null) spec = spec.and(EmployeeSpecifications.hasFirstName(request.getFirstName(), request.getFirstNameMatchType()));
        if (request.getMiddleName() != null) spec = spec.and(EmployeeSpecifications.hasMiddleName(request.getMiddleName(), request.getMiddleNameMatchType()));
        if (request.getLastName() != null) spec = spec.and(EmployeeSpecifications.hasLastName(request.getLastName(), request.getLastNameMatchType()));
        if (request.getNameKeyword() != null) spec = spec.and(EmployeeSpecifications.nameContains(request.getNameKeyword()));

        if (request.getStreet() != null) spec = spec.and(EmployeeSpecifications.hasStreet(request.getStreet()));
        if (request.getCity() != null) spec = spec.and(EmployeeSpecifications.hasCity(request.getCity()));
        if (request.getRegion() != null) spec = spec.and(EmployeeSpecifications.hasRegion(request.getRegion()));
        if (request.getCountry() != null) spec = spec.and(EmployeeSpecifications.hasCountry(request.getCountry()));
        if (request.getAddressKeyword() != null) spec = spec.and(EmployeeSpecifications.addressContains(request.getAddressKeyword()));

        if (request.getPhoneNumber() != null) spec = spec.and(EmployeeSpecifications.hasPhoneNumber(request.getPhoneNumber()));
        if (request.getEmail() != null) spec = spec.and(EmployeeSpecifications.hasEmail(request.getEmail()));
        if (request.getTelephoneNumber() != null) spec = spec.and(EmployeeSpecifications.hasTelephoneNumber(request.getTelephoneNumber()));
        if (request.getEmploymentStatus() != null) spec = spec.and(EmployeeSpecifications.hasEmploymentStatus(request.getEmploymentStatus()));
        if (request.getRoleId() != null) spec = spec.and(EmployeeSpecifications.hasRoleId(request.getRoleId()));
        if (request.getRoleName() != null) spec = spec.and(EmployeeSpecifications.hasRoleName(request.getRoleName()));

        Sort sort = request.getSortDir().equalsIgnoreCase("desc") ? Sort.by(request.getSortBy()).descending() : Sort.by(request.getSortBy()).ascending();
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);
        Page<Employee> employeePage = employeeRepository.findAll(spec, pageable);
        List<EmployeeResponse> employeeResponses = employeeMapper.toResponseList(employeePage.getContent());
        return new PageImpl<>(employeeResponses, pageable, employeePage.getTotalElements());
    }

    @Transactional
    public EmployeeResponse getEmployee(Integer employeeId) {
        Employee employee = employeeRepository.findByIdAndDeletedFalse(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee with ID " + employeeId + " not found!"));
        return employeeMapper.toResponse(employee);
    }

    @Transactional
    public EmployeeResponse addEmployee(@Valid EmployeeRequest employeeRequest) {
        Role role = roleRepository.findByIdAndDeletedFalse(employeeRequest.getRoleId())
                .orElseThrow(() -> new RoleNotFoundException("Role not found"));
        Employee employee = employeeMapper.toEntity(employeeRequest);
        employee.setRole(role);
        Employee updated = employeeRepository.save(employee);
        return employeeMapper.toResponse(updated);
    }

    @Transactional
    public EmployeeResponse updateEmployee(Integer employeeId, EmployeeRequest request) {
        Employee employee = employeeRepository.findByIdAndDeletedFalse(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee with ID " + employeeId + " not found!"));
        if (request.getRoleId() != null) {
            Role role = roleRepository.findByIdAndDeletedFalse(request.getRoleId())
                    .orElseThrow(() -> new RoleNotFoundException("Role not found"));
            employee.setRole(role);
        }
        employeeMapper.toUpdate(request, employee);
        Employee updated = employeeRepository.save(employee);
        return employeeMapper.toResponse(updated);
    }

    @Transactional
    public void deleteEmployee(Integer employeeId) {
        Employee employee = employeeRepository.findByIdAndDeletedFalse(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee with ID " + employeeId + " not found!"));
        boolean isLinkedToTicket = ticketRepository.existsByAssignee(employee);
        if (isLinkedToTicket) {
            log.warn("Attempting to delete employee that is still assigned to at least 1 ticket");
            throw new EntityInUseException("Cannot delete employee with ID " + employeeId + ". Employee is still assigned to at least 1 ticket");
        }
        employee.setDeleted(true);
        employeeRepository.save(employee);
    }
}
