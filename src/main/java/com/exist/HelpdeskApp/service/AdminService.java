package com.exist.HelpdeskApp.service;

import com.exist.HelpdeskApp.dto.employee.EmployeeMapper;
import com.exist.HelpdeskApp.dto.employee.EmployeeRequest;
import com.exist.HelpdeskApp.dto.employee.EmployeeResponse;
import com.exist.HelpdeskApp.model.Employee;
import com.exist.HelpdeskApp.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class AdminService {
    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    @Autowired
    public AdminService(EmployeeRepository employeeRepository, EmployeeMapper employeeMapper) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
    }

    @Transactional
    public List<EmployeeResponse> getEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        return employeeMapper.toResponseList(employees);
    }

    @Transactional
    public EmployeeResponse getEmployee(int id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
        return employeeMapper.toResponse(employee);
    }

    @Transactional
    public void addEmployee(EmployeeRequest request) {
        Employee employee = employeeMapper.toEntity(request);
        employeeRepository.save(employee);
    }

    @Transactional
    public EmployeeResponse updateEmployee(int id, EmployeeRequest request) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
        employeeMapper.toUpdate(request, employee);
        Employee updated = employeeRepository.save(employee);
        return employeeMapper.toResponse(updated);
    }

    @Transactional
    public void deleteEmployee(int id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Employee not found with id: " + id));
        employeeRepository.delete(employee);
    }
}
