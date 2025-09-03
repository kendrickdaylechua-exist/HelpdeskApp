package com.exist.HelpdeskApp.controller;

import com.exist.HelpdeskApp.dto.employee.EmployeeRequest;
import com.exist.HelpdeskApp.dto.employee.EmployeeResponse;
import com.exist.HelpdeskApp.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
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
        return adminService.getEmployees();
    }

    @GetMapping("/employees/{id}")
    public EmployeeResponse getEmployee(@PathVariable int id) {
        return adminService.getEmployee(id);
    }

    @PostMapping("/employees")
    public void addEmployee(@RequestBody EmployeeRequest request) {
        adminService.addEmployee(request);
    }

    @PatchMapping("/employees/{id}")
    public EmployeeResponse updateEmployeeName(@PathVariable int id, @RequestBody EmployeeRequest request) {
        return adminService.updateEmployee(id, request);
    }

    @DeleteMapping("/employees/{id}")
    public void deleteEmployee(@PathVariable int id) {
        adminService.deleteEmployee(id);
    }

}
