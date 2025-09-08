package com.exist.HelpdeskApp.service;

import com.exist.HelpdeskApp.dto.employee.EmployeeMapper;
import com.exist.HelpdeskApp.dto.employee.EmployeeRequest;
import com.exist.HelpdeskApp.dto.employee.EmployeeResponse;
import com.exist.HelpdeskApp.exception.EmployeeNotFoundException;
import com.exist.HelpdeskApp.exception.RoleNotFoundException;
import com.exist.HelpdeskApp.model.Employee;
import com.exist.HelpdeskApp.model.Role;
import com.exist.HelpdeskApp.repository.EmployeeRepository;
import com.exist.HelpdeskApp.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final RoleRepository roleRepository;
    private final EmployeeMapper employeeMapper;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository,
                           EmployeeMapper employeeMapper,
                           RoleRepository roleRepository) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
        this.roleRepository = roleRepository;
    }

    public List<EmployeeResponse> getEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        return employeeMapper.toResponseList(employees);
    }

    @Transactional
    public EmployeeResponse getEmployee(int employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee with ID " + employeeId + " not found!"));
        return employeeMapper.toResponse(employee);
    }

    @Transactional
    public EmployeeResponse addEmployee(EmployeeRequest employeeRequest) {
        Employee employee = employeeMapper.toEntity(employeeRequest);
        Role role = (employeeRequest.getRoleId() != null)
                ? roleRepository.findById(employeeRequest.getRoleId())
                .orElseGet(() -> roleRepository.findById(1)
                        .orElseThrow(() -> new RoleNotFoundException("Default role not found")))
                : roleRepository.findById(1)
                .orElseThrow(() -> new RoleNotFoundException("Default role not found"));
        employee.setRole(role);
        Employee updated = employeeRepository.save(employee);
        return employeeMapper.toResponse(updated);
    }

    @Transactional
    public EmployeeResponse updateEmployee(int employeeId, EmployeeRequest request) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Not found"));
        Role role = roleRepository.findById(request.getRoleId())
                        .orElseThrow(() -> new RuntimeException("Not found"));
        employee.setRole(role);

        employeeMapper.toUpdate(request, employee);
        Employee updated = employeeRepository.save(employee);
        return employeeMapper.toResponse(updated);
    }

    @Transactional
    public void deleteEmployee(int employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee with ID " + employeeId + " not found!"));
        employeeRepository.delete(employee);
    }
//
//    @Transactional
//    public void checkEmployeeId(int employeeId) {
//        employeeRepository.findById(employeeId)
//                .orElseThrow(() -> new EmployeeNotFoundException("Employee with ID " + employeeId + " not found!"));
//    }
}
