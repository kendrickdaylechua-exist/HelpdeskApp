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

    @GetMapping("/employees/{id}")
    public EmployeeResponse getEmployee(@PathVariable int id) {
        return employeeService.getEmployee(id);
    }

    @PostMapping("/employees")
    public EmployeeResponse addEmployee( @RequestBody EmployeeRequest request) {
        return employeeService.addEmployee(request);
    }

    @PatchMapping("/employees/{id}")
    public EmployeeResponse updateEmployee(@PathVariable int id, @RequestBody EmployeeRequest request) {
        return employeeService.updateEmployee(id, request);
    }

    @DeleteMapping("/employees/{id}")
    public void deleteEmployee(@PathVariable int id) {
        employeeService.deleteEmployee(id);
    }

    /// ===============================================================================
    @GetMapping("/roles")
    public List<RoleResponse> getRoles() {
        return roleService.getRoles();
    }

    @GetMapping("/roles/{id}")
    public RoleResponse getRole(@PathVariable int id) {
        return roleService.getRole(id);
    }

    @PostMapping("/roles")
    public RoleResponse addRole(@RequestBody RoleRequest request) {
        return roleService.addRole(request);
    }

    @PatchMapping("/roles/{id}")
    public RoleResponse updateRole(@PathVariable int employeeId, @RequestBody RoleRequest request) {
        return roleService.updateRole(employeeId, request);
    }

    @DeleteMapping("/roles/{id}")
    public void deleteRole(@PathVariable int id) {
        roleService.deleteRole(id);
    }

    /// ===============================================================================

    @GetMapping("tickets")
    public List<TicketResponse> getTickets() {
        return ticketService.getTickets(1);
    }

    @GetMapping("tickets/{ticketId}")
    public TicketResponse getTicket(@PathVariable int ticketId) {
        return ticketService.getTicket(1, ticketId);
    }

    @PatchMapping("tickets/{ticketId}")
    public TicketResponse updateTicket(@PathVariable int ticketId, @RequestBody TicketRequest ticketRequest) {
        return ticketService.updateTicket(1, ticketId, ticketRequest);
    }

}
