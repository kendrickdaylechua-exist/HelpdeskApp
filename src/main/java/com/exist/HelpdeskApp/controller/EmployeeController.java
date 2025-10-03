package com.exist.HelpdeskApp.controller;

import com.exist.HelpdeskApp.dto.employee.EmployeeFilterRequest;
import com.exist.HelpdeskApp.dto.employee.EmployeeRequest;
import com.exist.HelpdeskApp.dto.employee.EmployeeResponse;
import com.exist.HelpdeskApp.service.impl.EmployeeServiceImpl;
import com.exist.HelpdeskApp.service.impl.TicketServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    EmployeeServiceImpl employeeServiceImpl;
    TicketServiceImpl ticketServiceImpl;

    @Autowired
    public EmployeeController(EmployeeServiceImpl employeeServiceImpl,
                              TicketServiceImpl ticketServiceImpl) {
        this.employeeServiceImpl = employeeServiceImpl;
        this.ticketServiceImpl = ticketServiceImpl;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public Page<EmployeeResponse> getEmployees(@ModelAttribute @Valid EmployeeFilterRequest employeeFilterRequest,
                                               Pageable pageable,
                                               Authentication authentication) {
        return employeeServiceImpl.getEmployees(employeeFilterRequest, pageable, authentication);
    }

    @GetMapping("{employeeId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public EmployeeResponse getEmployeeById(@PathVariable Integer employeeId, Authentication authentication) {
        return employeeServiceImpl.getEmployeeById(employeeId, authentication);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public EmployeeResponse addEmployee(@RequestBody @Valid EmployeeRequest request) {
        return employeeServiceImpl.addEmployee(request);
    }

    @PatchMapping("/{employeeId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public EmployeeResponse updateEmployee(@PathVariable Integer employeeId, @RequestBody EmployeeRequest request, Authentication authentication) {
        return employeeServiceImpl.updateEmployee(employeeId, request, authentication);
    }

    @DeleteMapping("/{employeeId}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteEmployee(@PathVariable Integer employeeId) {
        employeeServiceImpl.deleteEmployee(employeeId);
    }
}
