package com.exist.HelpdeskApp.controller;

import com.exist.HelpdeskApp.model.Employee;
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
    public List<Employee> getEmployees() {
        return adminService.getEmployees();
    }

    @GetMapping("/employees/{id}")
    public Employee getEmployee(@PathVariable int id) {
        return adminService.getEmployee(id);
    }

    @PostMapping("/employees")
    public void addEmployee(@RequestBody Employee employee) {
        adminService.addEmployee(employee);
    }

    @PatchMapping("/employees/{id}/name")
    public Employee updateEmployeeName(@PathVariable int id, @RequestParam String name) {
        return adminService.updateEmployeeName(id, name);
    }

}
