package com.exist.HelpdeskApp.controller;

import com.exist.HelpdeskApp.dto.employee.EmployeeRequest;
import com.exist.HelpdeskApp.dto.employee.EmployeeResponse;
import com.exist.HelpdeskApp.dto.role.RoleRequest;
import com.exist.HelpdeskApp.dto.role.RoleResponse;
import com.exist.HelpdeskApp.dto.ticket.TicketRequest;
import com.exist.HelpdeskApp.dto.ticket.TicketResponse;
import com.exist.HelpdeskApp.service.EmployeeService;
import com.exist.HelpdeskApp.service.RoleService;
import com.exist.HelpdeskApp.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    EmployeeService employeeService;
    RoleService roleService;
    TicketService ticketService;

    @Autowired
    public AdminController(EmployeeService employeeService,
                           RoleService roleService,
                           TicketService ticketService) {
        this.employeeService = employeeService;
        this.roleService = roleService;
        this.ticketService = ticketService;
    }

    @RequestMapping("")
    public String adminHome() {
        return "This is the admin page.\n" +
                "=================================\n" +
                "APIs:\n" +
                "/employees";
    }

    @GetMapping("/employees")
    public List<EmployeeResponse> getEmployees() {
        return employeeService.getEmployees();
    }

    @GetMapping("/employees/{employeeId}")
    public EmployeeResponse getEmployee(@PathVariable Integer employeeId) {
        return employeeService.getEmployee(employeeId);
    }

    @PostMapping("/employees")
    public EmployeeResponse addEmployee(@RequestBody @Valid EmployeeRequest request) {
        return employeeService.addEmployee(request);
    }

    @PatchMapping("/employees/{employeeId}")
    public EmployeeResponse updateEmployee(@PathVariable Integer employeeId, @RequestBody EmployeeRequest request) {
        return employeeService.updateEmployee(employeeId, request);
    }

    @DeleteMapping("/employees/{employeeId}")
    public void deleteEmployee(@PathVariable Integer employeeId) {
        employeeService.deleteEmployee(employeeId);
    }

    /// ===============================================================================
    @GetMapping("/roles")
    public List<RoleResponse> getRoles() {
        return roleService.getRoles();
    }

    @GetMapping("/roles/{roleId}")
    public RoleResponse getRole(@PathVariable Integer roleId) {
        return roleService.getRole(roleId);
    }

    @PostMapping("/roles")
    public RoleResponse addRole(@RequestBody RoleRequest request) {
        return roleService.addRole(request);
    }

    @PatchMapping("/roles/{roleId}")
    public RoleResponse updateRole(@PathVariable Integer roleId, @RequestBody RoleRequest request) {
        return roleService.updateRole(roleId, request);
    }

    @DeleteMapping("/roles/{roleId}")
    public void deleteRole(@PathVariable Integer roleId) {
        roleService.deleteRole(roleId);
    }

    /// ===============================================================================

    @GetMapping("tickets")
    public List<TicketResponse> getTickets() {
        return ticketService.getTickets(1);
    }

    @GetMapping("tickets/{ticketId}")
    public TicketResponse getTicket(@PathVariable Integer ticketId) {
        return ticketService.getTicket(1, ticketId);
    }

    @PatchMapping("tickets/{ticketId}")
    public TicketResponse updateTicket(@PathVariable Integer ticketId, @RequestBody TicketRequest ticketRequest) {
        return ticketService.updateTicket(1, ticketId, ticketRequest);
    }

}
