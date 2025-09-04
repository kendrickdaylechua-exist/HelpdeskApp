package com.exist.HelpdeskApp.controller;

import com.exist.HelpdeskApp.dto.employee.EmployeeRequest;
import com.exist.HelpdeskApp.dto.employee.EmployeeResponse;
import com.exist.HelpdeskApp.dto.role.RoleRequest;
import com.exist.HelpdeskApp.dto.role.RoleResponse;
import com.exist.HelpdeskApp.service.EmployeeService;
import com.exist.HelpdeskApp.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    EmployeeService employeeService;
    RoleService roleService;

    @Autowired
    public AdminController(EmployeeService employeeService, RoleService roleService) {
        this.employeeService = employeeService;
        this.roleService = roleService;
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
    public void addEmployee( @RequestBody EmployeeRequest request) {
        employeeService.addEmployee(request);
    }

    @PatchMapping("/employees/{id}")
    public EmployeeResponse updateEmployeeName(@PathVariable int id, @RequestBody EmployeeRequest request) {
        return employeeService.updateEmployee(id, request);
    }

    @DeleteMapping("/employees/{id}")
    public void deleteEmployee(@PathVariable int id) {
        employeeService.deleteEmployee(id);
    }

    @GetMapping("/roles")
    public List<RoleResponse> getRoles() {
        return roleService.getRoles();
    }

    @GetMapping("/roles/{id}")
    public RoleResponse getRole(@PathVariable int id) {
        return roleService.getRole(id);
    }

    @PostMapping("/roles")
    public void addRole(@RequestBody RoleRequest request) {
        roleService.addRoles(request);
    }

    @PatchMapping("/roles/{id}")
    public void updateRole(@PathVariable int id, @RequestBody RoleRequest request) {
        roleService.updateRole(id, request);
    }

    @DeleteMapping("/roles/{id}")
    public void deleteRole(@PathVariable int id) {
        roleService.deleteRole(id);
    }

}
