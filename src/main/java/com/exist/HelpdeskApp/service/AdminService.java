package com.exist.HelpdeskApp.service;

import com.exist.HelpdeskApp.model.Employee;
import com.exist.HelpdeskApp.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class AdminService {
    EmployeeRepository employeeRepository;

    @Autowired
    public AdminService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> getEmployees() {
        return employeeRepository.findAll();
    }

    public Employee getEmployee(int id) {
        return employeeRepository.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
    }

    @Transactional
    public void addEmployee(Employee employee) {
        employeeRepository.save(employee);
    }

    @Transactional
    public void updateEmployee(int id, String name) {
        Employee employee = employeeRepository.findById(id).orElseThrow();
    }

    @Transactional
    public Employee updateEmployeeName(int id, String name) {
        Employee employee = employeeRepository.findById(id).orElseThrow();
        employee.setName(name);
        return employee;
    }
}
