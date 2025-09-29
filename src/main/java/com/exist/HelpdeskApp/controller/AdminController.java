package com.exist.HelpdeskApp.controller;

import com.exist.HelpdeskApp.dto.account.AccountListRequest;
import com.exist.HelpdeskApp.dto.account.AccountResponse;
import com.exist.HelpdeskApp.dto.employee.EmployeeRequest;
import com.exist.HelpdeskApp.dto.employee.EmployeeResponse;
import com.exist.HelpdeskApp.dto.role.RoleFilterRequest;
import com.exist.HelpdeskApp.dto.role.RoleRequest;
import com.exist.HelpdeskApp.dto.role.RoleResponse;
import com.exist.HelpdeskApp.dto.employee.EmployeeFilterRequest;
import com.exist.HelpdeskApp.dto.ticket.TicketFilterRequest;
import com.exist.HelpdeskApp.dto.ticket.TicketRequest;
import com.exist.HelpdeskApp.dto.ticket.TicketResponse;
import com.exist.HelpdeskApp.service.Implementations.AccountServiceImpl;
import com.exist.HelpdeskApp.service.Implementations.EmployeeServiceImpl;
import com.exist.HelpdeskApp.service.Implementations.RoleServiceImpl;
import com.exist.HelpdeskApp.service.Implementations.TicketServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    EmployeeServiceImpl employeeServiceImpl;
    RoleServiceImpl roleServiceImpl;
    TicketServiceImpl ticketServiceImpl;
    AccountServiceImpl accountServiceImpl;

    @Autowired
    public AdminController(EmployeeServiceImpl employeeServiceImpl,
                           RoleServiceImpl roleServiceImpl,
                           TicketServiceImpl ticketServiceImpl,
                           AccountServiceImpl accountServiceImpl) {
        this.employeeServiceImpl = employeeServiceImpl;
        this.roleServiceImpl = roleServiceImpl;
        this.ticketServiceImpl = ticketServiceImpl;
        this.accountServiceImpl = accountServiceImpl;
    }

    @RequestMapping("")
    public String adminHome() {
        return "This is the admin page.\n" +
                "=================================\n" +
                "APIs:\n" +
                "/employees\n" +
                "/roles";
    }

    @GetMapping("/employees")
    public Page<EmployeeResponse> getEmployees(@ModelAttribute @Valid EmployeeFilterRequest employeeFilterRequest) {
        return employeeServiceImpl.getEmployees(employeeFilterRequest);
    }

    @GetMapping("/employees/{employeeId}")
    public EmployeeResponse getEmployee(@PathVariable Integer employeeId) {
        return employeeServiceImpl.getEmployee(employeeId);
    }

    @PostMapping("/employees")
    public EmployeeResponse addEmployee(@RequestBody @Valid EmployeeRequest request) {
        return employeeServiceImpl.addEmployee(request);
    }

    @PatchMapping("/employees/{employeeId}")
    public EmployeeResponse updateEmployee(@PathVariable Integer employeeId, @RequestBody EmployeeRequest request) {
        return employeeServiceImpl.updateEmployee(employeeId, request);
    }

    @DeleteMapping("/employees/{employeeId}")
    public void deleteEmployee(@PathVariable Integer employeeId) {
        employeeServiceImpl.deleteEmployee(employeeId);
    }

    /// ===============================================================================
    @GetMapping("/roles")
    public Page<RoleResponse> getRoles(@ModelAttribute @Valid RoleFilterRequest request) {
        return roleServiceImpl.getRoles(request);
    }

    @GetMapping("/roles/{roleId}")
    public RoleResponse getRole(@PathVariable Integer roleId) {
        return roleServiceImpl.getRole(roleId);
    }

    @PostMapping("/roles")
    public RoleResponse addRole(@RequestBody RoleRequest request) {
        return roleServiceImpl.addRole(request);
    }

    @PatchMapping("/roles/{roleId}")
    public RoleResponse updateRole(@PathVariable Integer roleId, @RequestBody RoleRequest request) {
        return roleServiceImpl.updateRole(roleId, request);
    }

    @DeleteMapping("/roles/{roleId}")
    public void deleteRole(@PathVariable Integer roleId) {
        roleServiceImpl.deleteRole(roleId);
    }

    /// ===============================================================================

    @GetMapping("tickets")
    public Page<TicketResponse> getTickets(@ModelAttribute TicketFilterRequest request) {
        return ticketServiceImpl.getTickets(1, request);
    }

    @GetMapping("tickets/{ticketId}")
    public TicketResponse getTicket(@PathVariable Integer ticketId) {
        return ticketServiceImpl.getTicket(1, ticketId);
    }

    @PatchMapping("tickets/{ticketId}")
    public TicketResponse updateTicket(@PathVariable Integer ticketId, @RequestBody TicketRequest ticketRequest) {
        return ticketServiceImpl.updateTicket(1, ticketId, ticketRequest);
    }

    @GetMapping("accounts")
    public Page<AccountResponse> getAccounts(@ModelAttribute AccountListRequest request) {
        return accountServiceImpl.getAccounts(request);
    }

}
