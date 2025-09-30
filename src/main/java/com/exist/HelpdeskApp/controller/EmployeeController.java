package com.exist.HelpdeskApp.controller;

import com.exist.HelpdeskApp.dto.employee.EmployeeFilterRequest;
import com.exist.HelpdeskApp.dto.employee.EmployeeRequest;
import com.exist.HelpdeskApp.dto.employee.EmployeeResponse;
import com.exist.HelpdeskApp.dto.ticket.TicketFilterRequest;
import com.exist.HelpdeskApp.dto.ticket.TicketRequest;
import com.exist.HelpdeskApp.dto.ticket.TicketResponse;
import com.exist.HelpdeskApp.service.Implementations.EmployeeServiceImpl;
import com.exist.HelpdeskApp.service.Implementations.TicketServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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
    public Page<EmployeeResponse> getEmployees(@ModelAttribute @Valid EmployeeFilterRequest employeeFilterRequest) {
        return employeeServiceImpl.getEmployees(employeeFilterRequest);
    }

    @GetMapping("{employeeId}")
    public EmployeeResponse getEmployee(@PathVariable Integer employeeId) {
        return employeeServiceImpl.getEmployee(employeeId);
    }

    @PostMapping
    public EmployeeResponse addEmployee(@RequestBody @Valid EmployeeRequest request) {
        return employeeServiceImpl.addEmployee(request);
    }

    @PatchMapping("/{employeeId}")
    public EmployeeResponse updateEmployee(@PathVariable Integer employeeId, @RequestBody EmployeeRequest request) {
        return employeeServiceImpl.updateEmployee(employeeId, request);
    }

    @DeleteMapping("/{employeeId}")
    public void deleteEmployee(@PathVariable Integer employeeId) {
        employeeServiceImpl.deleteEmployee(employeeId);
    }
}
