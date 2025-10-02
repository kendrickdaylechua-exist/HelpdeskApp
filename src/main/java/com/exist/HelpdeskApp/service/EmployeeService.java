package com.exist.HelpdeskApp.service;

import com.exist.HelpdeskApp.dto.employee.EmployeeRequest;
import com.exist.HelpdeskApp.dto.employee.EmployeeResponse;
import com.exist.HelpdeskApp.dto.employee.EmployeeFilterRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.validation.Valid;

public interface EmployeeService {
    void init();
    void cleanup();
    Page<EmployeeResponse> getEmployees(EmployeeFilterRequest employeeFilterRequest, Pageable pageable);
    EmployeeResponse addEmployee(@Valid EmployeeRequest employeeRequest);
    EmployeeResponse updateEmployee(Integer employeeId, EmployeeRequest request);
    void deleteEmployee(Integer employeeId);
}
