package com.exist.HelpdeskApp.service;

import com.exist.HelpdeskApp.dto.employee.EmployeeRequest;
import com.exist.HelpdeskApp.dto.employee.EmployeeResponse;
import com.exist.HelpdeskApp.dto.employee.EmployeeFilterRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

import javax.validation.Valid;

public interface EmployeeService {
    void init();
    void cleanup();
    Page<EmployeeResponse> getEmployees(EmployeeFilterRequest request, Pageable pageable, Authentication authentication);
    EmployeeResponse addEmployee(@Valid EmployeeRequest employeeRequest);
    EmployeeResponse updateEmployee(Integer employeeId, EmployeeRequest request, Authentication authentication);
    void deleteEmployee(Integer employeeId);
}
